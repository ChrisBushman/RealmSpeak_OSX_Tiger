package com.robin.magic_realm.components.quest;

public final class TradeType {
	private final String _name;
	private final int _ordinal;
	private TradeType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final TradeType Buy = new TradeType("Buy", 0);
	public static final TradeType Sell = new TradeType("Sell", 1);

	private static final TradeType[] _VALUES = { Buy, Sell };
	public static TradeType[] values() { TradeType[] r = new TradeType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static TradeType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}
}