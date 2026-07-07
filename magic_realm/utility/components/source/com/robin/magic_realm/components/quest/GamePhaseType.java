package com.robin.magic_realm.components.quest;

public final class GamePhaseType {
	private final String _name;
	private final int _ordinal;
	private GamePhaseType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final GamePhaseType Unspecified = new GamePhaseType("Unspecified", 0);
	public static final GamePhaseType Birdsong = new GamePhaseType("Birdsong", 1);
	public static final GamePhaseType EndOfPhase = new GamePhaseType("EndOfPhase", 2);
	public static final GamePhaseType EndOfTurn = new GamePhaseType("EndOfTurn", 3);
	public static final GamePhaseType StartOfEvening = new GamePhaseType("StartOfEvening", 4);
	public static final GamePhaseType Midnight = new GamePhaseType("Midnight", 5);

	private static final GamePhaseType[] _VALUES = { Unspecified, Birdsong, EndOfPhase, EndOfTurn, StartOfEvening, Midnight };
	public static GamePhaseType[] values() { GamePhaseType[] r = new GamePhaseType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static GamePhaseType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}
}