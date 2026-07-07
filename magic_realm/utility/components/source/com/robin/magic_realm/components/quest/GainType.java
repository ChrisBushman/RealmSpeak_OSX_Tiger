package com.robin.magic_realm.components.quest;

public final class GainType {
	private final String _name;
	private final int _ordinal;
	private GainType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final GainType Gain = new GainType("Gain", 0);
	public static final GainType Lose = new GainType("Lose", 1);

	private static final GainType[] _VALUES = { Gain, Lose };
	public static GainType[] values() { GainType[] r = new GainType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static GainType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}
}