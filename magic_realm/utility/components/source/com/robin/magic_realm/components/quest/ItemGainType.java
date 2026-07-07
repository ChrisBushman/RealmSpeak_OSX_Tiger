package com.robin.magic_realm.components.quest;

public final class ItemGainType {
	private final String _name;
	private final int _ordinal;
	private ItemGainType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final ItemGainType Gain = new ItemGainType("Gain", 0);
	public static final ItemGainType GainCloned = new ItemGainType("GainCloned", 1);
	public static final ItemGainType GainFromNativeHq = new ItemGainType("GainFromNativeHq", 2);
	public static final ItemGainType GainClonedFromNativeHq = new ItemGainType("GainClonedFromNativeHq", 3);
	public static final ItemGainType LoseToClearing = new ItemGainType("LoseToClearing", 4);
	public static final ItemGainType LoseToLocation = new ItemGainType("LoseToLocation", 5);
	public static final ItemGainType LoseToNativeHq = new ItemGainType("LoseToNativeHq", 6);
	public static final ItemGainType LoseToChartOfAppearance = new ItemGainType("LoseToChartOfAppearance", 7);
	public static final ItemGainType RemoveFromGame = new ItemGainType("RemoveFromGame", 8);

	private static final ItemGainType[] _VALUES = { Gain, GainCloned, GainFromNativeHq, GainClonedFromNativeHq, LoseToClearing, LoseToLocation, LoseToNativeHq, LoseToChartOfAppearance, RemoveFromGame };
	public static ItemGainType[] values() { ItemGainType[] r = new ItemGainType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static ItemGainType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}
}