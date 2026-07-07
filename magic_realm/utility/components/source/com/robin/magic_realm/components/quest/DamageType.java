package com.robin.magic_realm.components.quest;

public final class DamageType {
	private final String _name;
	private final int _ordinal;
	private DamageType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final DamageType WeatherFatigue = new DamageType("WeatherFatigue", 0);
	public static final DamageType Wounds = new DamageType("Wounds", 1);

	private static final DamageType[] _VALUES = { WeatherFatigue, Wounds };
	public static DamageType[] values() { DamageType[] r = new DamageType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static DamageType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}
}