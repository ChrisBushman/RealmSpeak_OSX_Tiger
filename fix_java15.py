#!/usr/bin/env python3
"""
Fix Java 1.5 incompatibilities in Java source files:
1. Diamond operator <> -> explicit type params (multi-pass: same-line, field lookup, return type)
2. Remaining unfixable cases reported for manual review
"""

import re
import os
import sys
import glob


def extract_nested_angle_content(s, start):
    """
    Starting at position 'start' (where s[start]=='<'),
    return (content_between_angles, end_position).
    Handles nested angle brackets.
    """
    if start >= len(s) or s[start] != '<':
        return None, -1
    depth = 0
    i = start
    while i < len(s):
        c = s[i]
        if c == '<':
            depth += 1
        elif c == '>':
            depth -= 1
            if depth == 0:
                return s[start + 1:i], i
        i += 1
    return None, -1


def find_type_params_from_lhs(lhs):
    """
    Given the left-hand side of an assignment (up to and including '='),
    find the generic type parameters of the declared variable.
    e.g. "  ArrayList<String> list = " -> "String"
         "  HashMap<String, List<Integer>> m = " -> "String, List<Integer>"
    """
    eq = lhs.rfind('=')
    if eq < 0:
        return None
    decl = lhs[:eq].rstrip()

    last_gt = decl.rfind('>')
    if last_gt < 0:
        return None

    depth = 0
    i = last_gt
    while i >= 0:
        c = decl[i]
        if c == '>':
            depth += 1
        elif c == '<':
            depth -= 1
            if depth == 0:
                return decl[i + 1:last_gt]
        i -= 1
    return None


def build_field_type_map(lines):
    """
    Scan all lines in a file for field/local variable declarations.
    Returns a dict: varname -> type_params_string
    e.g. "private ArrayList<String> names;" -> {"names": "String"}
    Also captures: "private HashMap<String, Integer> scores;"
    """
    field_map = {}
    # Match: optional-modifier Type<Params> varName [= ...] ;
    # Also handle: Type<P1, P2> varName
    decl_re = re.compile(
        r'(?:private|protected|public|static|final|volatile|transient|\s)*'
        r'\b(\w+(?:\.\w+)*)'   # base type
        r'(<[^;{]+?>)'         # generic params (non-greedy, no semicolons/braces)
        r'\s+(\w+)'            # variable name
        r'\s*(?:[=;,\)])'      # followed by =, ;, ,  or )
    )
    for line in lines:
        stripped = line.strip()
        if stripped.startswith('//') or stripped.startswith('*'):
            continue
        for m in decl_re.finditer(line):
            type_params_raw = m.group(2)  # e.g. "<String, Integer>"
            var_name = m.group(3)
            # Extract content between < and >
            content, _ = extract_nested_angle_content(type_params_raw, 0)
            if content:
                field_map[var_name] = content
    return field_map


def fix_diamond_operators_in_content(content):
    """Replace new TypeName<>(...) with new TypeName<ExplicitParams>(...)"""
    lines = content.split('\n')
    changed = False

    # Build field type map for this file (for field re-assignment cases)
    field_map = build_field_type_map(lines)

    for idx, line in enumerate(lines):
        if '<>' not in line:
            continue

        new_diamond_re = re.compile(r'new\s+(\w+)<>')
        matches = list(new_diamond_re.finditer(line))
        if not matches:
            continue

        new_line = line
        offset = 0

        for m in matches:
            rhs_class = m.group(1)
            adj_start = m.start(0) + offset
            lhs = new_line[:adj_start]

            type_params = None

            # Strategy 1: type declaration on same line (e.g. "ArrayList<X> v = new ArrayList<>")
            type_params = find_type_params_from_lhs(lhs)

            # Strategy 2: re-assignment to a known field (e.g. "this.names = new ArrayList<>")
            if type_params is None:
                # Find the variable being assigned: look for "varName = new" or "this.varName = new"
                assign_m = re.search(r'(?:this\.)?(\w+)\s*=\s*$', lhs.rstrip())
                if assign_m:
                    var_name = assign_m.group(1)
                    if var_name in field_map:
                        type_params = field_map[var_name]

            # Strategy 3: return statement -- look for method return type above
            if type_params is None:
                stripped_lhs = lhs.strip()
                if stripped_lhs in ('return', 'return (') or re.match(r'^\s*return\s*$', lhs.rstrip()):
                    for back in range(idx - 1, max(idx - 25, -1), -1):
                        prev = lines[back].strip()
                        if prev.startswith('//') or prev.startswith('*'):
                            continue
                        sig_m = re.search(
                            r'(?:public|private|protected|static|final|\s)+\s+'
                            r'(\w+)<([^>]+(?:<[^>]*>[^>]*)*)>\s+\w+\s*\(',
                            prev
                        )
                        if sig_m:
                            type_params = sig_m.group(2)
                            break
                        if '{' in prev or '}' in prev:
                            break

            # Strategy 4: ternary / method arg with Vector<> or known patterns
            # For setListData(new Vector<>()), the element type comes from what was passed in
            # or from the list field. Skip these for now.

            # Strategy 5: Common constructor-with-collection-arg patterns
            # e.g. new ArrayList<>(someList) -- use the same type as someList if known
            if type_params is None:
                # Check if next token is a known var
                after_diamond = new_line[adj_start + len(m.group(0)) + offset - len(m.group(0)):]
                args_m = re.match(r'<>\((\w+)', m.group(0) + new_line[adj_start + len(m.group(0)):])
                # Actually look at text after <>
                rest = new_line[adj_start + len(m.group(0)) + offset - adj_start + m.start(0):]
                args2 = re.match(r'<>\((\w+)', 'new '+rhs_class + new_line[adj_start + len(m.group(0)):])
                if args2:
                    arg_name = args2.group(1)
                    if arg_name in field_map:
                        type_params = field_map[arg_name]

            if type_params is None:
                continue  # Can't determine -- skip

            lt_pos = m.group(0).index('<')
            adj_lt = adj_start + lt_pos
            replacement = f'<{type_params}>'
            new_line = new_line[:adj_lt] + replacement + new_line[adj_lt + 2:]
            offset += len(replacement) - 2
            changed = True

        lines[idx] = new_line

    return '\n'.join(lines), changed


def process_file(path):
    with open(path, 'r', encoding='utf-8', errors='replace') as f:
        original = f.read()

    content = original
    total_changed = False

    # Run multiple passes until stable (handles cases where same file has both field decl and usage)
    for _ in range(3):
        new_content, changed = fix_diamond_operators_in_content(content)
        if changed:
            content = new_content
            total_changed = True
        else:
            break

    if total_changed:
        with open(path, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False


def main():
    root = sys.argv[1] if len(sys.argv) > 1 else '.'
    java_files = glob.glob(os.path.join(root, '**/*.java'), recursive=True)

    fixed = 0
    skipped = 0
    for path in sorted(java_files):
        if '.git' in path:
            continue
        try:
            if process_file(path):
                print(f'  fixed: {path}')
                fixed += 1
            else:
                skipped += 1
        except Exception as e:
            print(f'  ERROR {path}: {e}', file=sys.stderr)

    print(f'\nDone: {fixed} files modified, {skipped} files unchanged.')

    # Report remaining unfixed
    print('\nRemaining diamond usages (need manual fix):')
    count = 0
    for path in sorted(java_files):
        if '.git' in path:
            continue
        try:
            with open(path) as f:
                for i, line in enumerate(f, 1):
                    if '<>' in line and 'new ' in line and not line.strip().startswith('//'):
                        print(f'  {path}:{i}: {line.rstrip()}')
                        count += 1
        except:
            pass
    print(f'\nTotal remaining: {count}')


if __name__ == '__main__':
    main()
