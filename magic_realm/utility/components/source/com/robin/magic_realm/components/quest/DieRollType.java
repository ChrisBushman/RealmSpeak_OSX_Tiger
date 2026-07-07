package com.robin.magic_realm.components.quest;

public final class DieRollType {
	private final String _name;
	private final int _ordinal;
	private DieRollType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final DieRollType Random = new DieRollType("Random", 0);
	public static final DieRollType One = new DieRollType("One", 1);
	public static final DieRollType Two = new DieRollType("Two", 2);
	public static final DieRollType Three = new DieRollType("Three", 3);
	public static final DieRollType Four = new DieRollType("Four", 4);
	public static final DieRollType Five = new DieRollType("Five", 5);
	public static final DieRollType Six = new DieRollType("Six", 6);

	private static final DieRollType[] _VALUES = { Random, One, Two, Three, Four, Five, Six };
	public static DieRollType[] values() { DieRollType[] r = new DieRollType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static DieRollType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}
}