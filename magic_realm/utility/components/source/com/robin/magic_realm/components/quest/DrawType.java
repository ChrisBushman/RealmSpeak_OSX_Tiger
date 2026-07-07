package com.robin.magic_realm.components.quest;

public final class DrawType {
	private final String _name;
	private final int _ordinal;
	private DrawType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final DrawType Top = new DrawType("Top", 0);
	public static final DrawType Bottom = new DrawType("Bottom", 1);
	public static final DrawType Random = new DrawType("Random", 2);
	public static final DrawType Choice = new DrawType("Choice", 3);

	private static final DrawType[] _VALUES = { Top, Bottom, Random, Choice };
	public static DrawType[] values() { DrawType[] r = new DrawType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static DrawType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}
}