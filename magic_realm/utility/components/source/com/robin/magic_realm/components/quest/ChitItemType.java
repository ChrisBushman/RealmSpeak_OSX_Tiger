package com.robin.magic_realm.components.quest;

import java.util.ArrayList;

public final class ChitItemType {
	private final String _name;
	private final int _ordinal;
	private ChitItemType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final ChitItemType None = new ChitItemType("None", 0);
	public static final ChitItemType Treasure = new ChitItemType("Treasure", 1);
	public static final ChitItemType Weapon = new ChitItemType("Weapon", 2);
	public static final ChitItemType Armor = new ChitItemType("Armor", 3);
	public static final ChitItemType Great = new ChitItemType("Great", 4);
	public static final ChitItemType Horse = new ChitItemType("Horse", 5);

	private static final ChitItemType[] _VALUES = { None, Treasure, Weapon, Armor, Great, Horse };
	public static ChitItemType[] values() { ChitItemType[] r = new ChitItemType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static ChitItemType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}

	// --- enum methods ---
	static String[] ItemKeyVals = {"item"};
	static String[] TreasureKeyVals = {"item","treasure"};
	static String[] WeaponKeyVals = {"item","weapon","!character","!treasure","!magic"};
	static String[] ArmorKeyVals = {"item","armor","!character","!treasure","!magic"};
	static String[] GreatKeyVals = {"item","great"};
	static String[] HorseKeyVals = {"item","horse"};
	public String[] getKeyVals() {
		if (this == None) {
				return ItemKeyVals;
			}
			else if (this == Treasure) {
				return TreasureKeyVals;
			}
			else if (this == Weapon) {
				return WeaponKeyVals;
			}
			else if (this == Armor) {
				return ArmorKeyVals;
			}
			else if (this == Great) {
				return GreatKeyVals;
			}
			else if (this == Horse) {
				return HorseKeyVals;
			}
		throw new IllegalStateException("Unknown ChitItemType?"); // can this even happen?
	}
	public static ArrayList listToStrings(ArrayList types) {
		if (types==null) return null;
		ArrayList list = new ArrayList();
		for (java.util.Iterator _j14it2178 = (types).iterator(); _j14it2178.hasNext(); ) {
		  ChitItemType cit = (ChitItemType) _j14it2178.next();
			list.add(cit.toString());
		}
		return list;
	}
	public static ArrayList listToTypes(ArrayList strings) {
		if (strings==null) return null;
		ArrayList list = new ArrayList();
		for (java.util.Iterator _j14it2179 = (strings).iterator(); _j14it2179.hasNext(); ) {
		  String string = (String) _j14it2179.next();
			list.add(ChitItemType.valueOf(string));
		}
		return list;
	}
}