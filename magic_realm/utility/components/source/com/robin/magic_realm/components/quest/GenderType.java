package com.robin.magic_realm.components.quest;

public final class GenderType {
	private final String _name;
	private final int _ordinal;
	private GenderType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final GenderType Any = new GenderType("Any", 0);
	public static final GenderType Female = new GenderType("Female", 1);
	public static final GenderType Male = new GenderType("Male", 2);
	public static final GenderType Undefined = new GenderType("Undefined", 3);

	private static final GenderType[] _VALUES = { Any, Female, Male, Undefined };
	public static GenderType[] values() { GenderType[] r = new GenderType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static GenderType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}
}