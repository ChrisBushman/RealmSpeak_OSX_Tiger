package com.robin.magic_realm.components.quest;

import com.robin.magic_realm.components.TileComponent;

public final class LocationTileSideType {
	private final String _name;
	private final int _ordinal;
	private LocationTileSideType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final LocationTileSideType Any = new LocationTileSideType("Any", 0);
	public static final LocationTileSideType Unenchanted = new LocationTileSideType("Unenchanted", 1);
	public static final LocationTileSideType Enchanted = new LocationTileSideType("Enchanted", 2);

	private static final LocationTileSideType[] _VALUES = { Any, Unenchanted, Enchanted };
	public static LocationTileSideType[] values() { LocationTileSideType[] r = new LocationTileSideType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static LocationTileSideType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}

	// --- enum methods ---
	public boolean matches(TileComponent tile) {
		if (this == Any) {
				return true;
			}
			else if (this == Unenchanted) {
				return !tile.isEnchanted();
			}
			else if (this == Enchanted) {
				return tile.isEnchanted();
			}
		return false;
	}
}