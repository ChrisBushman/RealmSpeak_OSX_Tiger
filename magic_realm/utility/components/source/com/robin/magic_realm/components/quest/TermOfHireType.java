package com.robin.magic_realm.components.quest;

public final class TermOfHireType {
	private final String _name;
	private final int _ordinal;
	private TermOfHireType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final TermOfHireType Normal = new TermOfHireType("Normal", 0);
	public static final TermOfHireType Permanent = new TermOfHireType("Permanent", 1);
	public static final TermOfHireType PlaceInClearing = new TermOfHireType("PlaceInClearing", 2);

	private static final TermOfHireType[] _VALUES = { Normal, Permanent, PlaceInClearing };
	public static TermOfHireType[] values() { TermOfHireType[] r = new TermOfHireType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static TermOfHireType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}
}