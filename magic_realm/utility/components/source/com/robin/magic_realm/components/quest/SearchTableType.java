package com.robin.magic_realm.components.quest;

public final class SearchTableType {
	private final String _name;
	private final int _ordinal;
	private SearchTableType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final SearchTableType Any = new SearchTableType("Any", 0);
	public static final SearchTableType Locate = new SearchTableType("Locate", 1);
	public static final SearchTableType Loot = new SearchTableType("Loot", 2);
	public static final SearchTableType MagicSight = new SearchTableType("MagicSight", 3);
	public static final SearchTableType Peer = new SearchTableType("Peer", 4);
	public static final SearchTableType ReadRunes = new SearchTableType("ReadRunes", 5);
	public static final SearchTableType ToadstoolCircle = new SearchTableType("ToadstoolCircle", 6);
	public static final SearchTableType CryptOfTheKnight = new SearchTableType("CryptOfTheKnight", 7);
	public static final SearchTableType EnchantedMeadow = new SearchTableType("EnchantedMeadow", 8);
	public static final SearchTableType ArcheologicalDig = new SearchTableType("ArcheologicalDig", 9);
	public static final SearchTableType FountainOfHealth = new SearchTableType("FountainOfHealth", 10);
	public static final SearchTableType CircleOfStones = new SearchTableType("CircleOfStones", 11);
	public static final SearchTableType EtherealAbbey = new SearchTableType("EtherealAbbey", 12);
	public static final SearchTableType FairyGrove = new SearchTableType("FairyGrove", 13);
	public static final SearchTableType HauntedGrave = new SearchTableType("HauntedGrave", 14);
	public static final SearchTableType MageLibrary = new SearchTableType("MageLibrary", 15);

	private static final SearchTableType[] _VALUES = { Any, Locate, Loot, MagicSight, Peer, ReadRunes, ToadstoolCircle, CryptOfTheKnight, EnchantedMeadow, ArcheologicalDig, FountainOfHealth, CircleOfStones, EtherealAbbey, FairyGrove, HauntedGrave, MageLibrary };
	public static SearchTableType[] values() { SearchTableType[] r = new SearchTableType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static SearchTableType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}
}