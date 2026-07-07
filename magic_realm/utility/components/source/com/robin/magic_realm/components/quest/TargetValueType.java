package com.robin.magic_realm.components.quest;

public final class TargetValueType {
	private final String _name;
	private final int _ordinal;
	private TargetValueType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final TargetValueType Game = new TargetValueType("Game", 0);
	public static final TargetValueType Quest = new TargetValueType("Quest", 1);
	public static final TargetValueType Step = new TargetValueType("Step", 2);
	public static final TargetValueType Day = new TargetValueType("Day", 3);

	private static final TargetValueType[] _VALUES = { Game, Quest, Step, Day };
	public static TargetValueType[] values() { TargetValueType[] r = new TargetValueType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static TargetValueType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}
}