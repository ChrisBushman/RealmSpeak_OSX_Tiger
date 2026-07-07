package com.robin.magic_realm.components.quest;

public final class ChitAcquisitionType {
	private final String _name;
	private final int _ordinal;
	private ChitAcquisitionType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final ChitAcquisitionType Available = new ChitAcquisitionType("Available", 0);
	public static final ChitAcquisitionType Lose = new ChitAcquisitionType("Lose", 1);
	public static final ChitAcquisitionType Clone = new ChitAcquisitionType("Clone", 2);

	private static final ChitAcquisitionType[] _VALUES = { Available, Lose, Clone };
	public static ChitAcquisitionType[] values() { ChitAcquisitionType[] r = new ChitAcquisitionType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static ChitAcquisitionType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}
}