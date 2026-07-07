package com.robin.magic_realm.components.quest;

public final class TreasureType {
	private final String _name;
	private final int _ordinal;
	private TreasureType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final TreasureType Any = new TreasureType("Any", 0);
	public static final TreasureType Armor = new TreasureType("Armor", 1);
	public static final TreasureType Artifact = new TreasureType("Artifact", 2);
	public static final TreasureType Book = new TreasureType("Book", 3);
	public static final TreasureType Boots = new TreasureType("Boots", 4);
	public static final TreasureType Gloves = new TreasureType("Gloves", 5);
	public static final TreasureType Great = new TreasureType("Great", 6);
	public static final TreasureType Large = new TreasureType("Large", 7);
	public static final TreasureType MagicArmor = new TreasureType("MagicArmor", 8);
	public static final TreasureType MagicWeapon = new TreasureType("MagicWeapon", 9);
	public static final TreasureType Scroll = new TreasureType("Scroll", 10);
	public static final TreasureType Small = new TreasureType("Small", 11);
	public static final TreasureType Treasure = new TreasureType("Treasure", 12);
	public static final TreasureType TWT = new TreasureType("TWT", 13);
	public static final TreasureType Weapon = new TreasureType("Weapon", 14);

	private static final TreasureType[] _VALUES = { Any, Armor, Artifact, Book, Boots, Gloves, Great, Large, MagicArmor, MagicWeapon, Scroll, Small, Treasure, TWT, Weapon };
	public static TreasureType[] values() { TreasureType[] r = new TreasureType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static TreasureType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}
}