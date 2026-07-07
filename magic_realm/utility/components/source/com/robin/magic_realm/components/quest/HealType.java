package com.robin.magic_realm.components.quest;

public final class HealType {
	private final String _name;
	private final int _ordinal;
	private HealType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final HealType All = new HealType("All", 0);
	public static final HealType Fatigued = new HealType("Fatigued", 1);
	public static final HealType Wounded = new HealType("Wounded", 2);
	public static final HealType Restable = new HealType("Restable", 3);
	public static final HealType Magic = new HealType("Magic", 4);

	private static final HealType[] _VALUES = { All, Fatigued, Wounded, Restable, Magic };
	public static HealType[] values() { HealType[] r = new HealType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static HealType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}
}