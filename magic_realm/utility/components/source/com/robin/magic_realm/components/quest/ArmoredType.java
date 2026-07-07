package com.robin.magic_realm.components.quest;

public final class ArmoredType {
	private final String _name;
	private final int _ordinal;
	private ArmoredType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final ArmoredType Any = new ArmoredType("Any", 0);
	public static final ArmoredType Unarmored = new ArmoredType("Unarmored", 1);
	public static final ArmoredType Armored = new ArmoredType("Armored", 2);

	private static final ArmoredType[] _VALUES = { Any, Unarmored, Armored };
	public static ArmoredType[] values() { ArmoredType[] r = new ArmoredType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static ArmoredType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}
}