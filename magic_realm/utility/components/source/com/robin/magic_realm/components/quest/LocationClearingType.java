package com.robin.magic_realm.components.quest;

import com.robin.magic_realm.components.ClearingDetail;

public final class LocationClearingType {
	private final String _name;
	private final int _ordinal;
	private LocationClearingType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final LocationClearingType Any = new LocationClearingType("Any", 0);
	public static final LocationClearingType Plain = new LocationClearingType("Plain", 1);
	public static final LocationClearingType Cave = new LocationClearingType("Cave", 2);
	public static final LocationClearingType Mountain = new LocationClearingType("Mountain", 3);
	public static final LocationClearingType Woods = new LocationClearingType("Woods", 4);
	public static final LocationClearingType Water = new LocationClearingType("Water", 5);

	private static final LocationClearingType[] _VALUES = { Any, Plain, Cave, Mountain, Woods, Water };
	public static LocationClearingType[] values() { LocationClearingType[] r = new LocationClearingType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static LocationClearingType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}

	// --- enum methods ---
	public boolean matches(ClearingDetail clearing) {
		if (this == Any) {
				return true;
			}
			else if (this == Plain) {
				return clearing.isNormal();
			}
			else if (this == Cave) {
				return clearing.isCave();
			}
			else if (this == Mountain) {
				return clearing.isMountain();
			}
			else if (this == Woods) {
				return clearing.isWoods();
			}
			else if (this == Water) {
				return clearing.isWater();
			}
		return false;
	}
}