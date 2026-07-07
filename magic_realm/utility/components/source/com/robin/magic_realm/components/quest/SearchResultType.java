package com.robin.magic_realm.components.quest;

import java.util.ArrayList;

public final class SearchResultType {
	private final String _name;
	private final int _ordinal;
	private SearchResultType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final SearchResultType Any = new SearchResultType("Any", 0);
	public static final SearchResultType Awaken = new SearchResultType("Awaken", 1);
	public static final SearchResultType Clues = new SearchResultType("Clues", 2);
	public static final SearchResultType Counters = new SearchResultType("Counters", 3);
	public static final SearchResultType Curse = new SearchResultType("Curse", 4);
	public static final SearchResultType Mesmerize = new SearchResultType("Mesmerize", 5);
	public static final SearchResultType DiscoverChits = new SearchResultType("DiscoverChits", 6);
	public static final SearchResultType HiddenEnemies = new SearchResultType("HiddenEnemies", 7);
	public static final SearchResultType LearnAndAwaken = new SearchResultType("LearnAndAwaken", 8);
	public static final SearchResultType LearnSpell = new SearchResultType("LearnSpell", 9);
	public static final SearchResultType Passages = new SearchResultType("Passages", 10);
	public static final SearchResultType Paths = new SearchResultType("Paths", 11);
	public static final SearchResultType PerceiveSpell = new SearchResultType("PerceiveSpell", 12);
	public static final SearchResultType TakeTopTreasure = new SearchResultType("TakeTopTreasure", 13);
	public static final SearchResultType Take2ndTreasure = new SearchResultType("Take2ndTreasure", 14);
	public static final SearchResultType Take3rdTreasure = new SearchResultType("Take3rdTreasure", 15);
	public static final SearchResultType Take4thTreasure = new SearchResultType("Take4thTreasure", 16);
	public static final SearchResultType Take5thTreasure = new SearchResultType("Take5thTreasure", 17);
	public static final SearchResultType Take6thTreasure = new SearchResultType("Take6thTreasure", 18);
	public static final SearchResultType TreasureCards = new SearchResultType("TreasureCards", 19);
	public static final SearchResultType CaveTeleport = new SearchResultType("CaveTeleport", 20);
	public static final SearchResultType MountainTeleport = new SearchResultType("MountainTeleport", 21);
	public static final SearchResultType WoodsTeleport = new SearchResultType("WoodsTeleport", 22);
	public static final SearchResultType RuinsTeleport = new SearchResultType("RuinsTeleport", 23);
	public static final SearchResultType PeerEnchantAnyClearing = new SearchResultType("PeerEnchantAnyClearing", 24);
	public static final SearchResultType PowerOfThePit = new SearchResultType("PowerOfThePit", 25);
	public static final SearchResultType Gold = new SearchResultType("Gold", 26);
	public static final SearchResultType Notoriety = new SearchResultType("Notoriety", 27);
	public static final SearchResultType Wish = new SearchResultType("Wish", 28);
	public static final SearchResultType Heal = new SearchResultType("Heal", 29);
	public static final SearchResultType SummonDemon = new SearchResultType("SummonDemon", 30);
	public static final SearchResultType ReadRunesAnySite = new SearchResultType("ReadRunesAnySite", 31);
	public static final SearchResultType Rest = new SearchResultType("Rest", 32);
	public static final SearchResultType RemoveCurse = new SearchResultType("RemoveCurse", 33);
	public static final SearchResultType Wound = new SearchResultType("Wound", 34);
	public static final SearchResultType Unhide = new SearchResultType("Unhide", 35);

	private static final SearchResultType[] _VALUES = { Any, Awaken, Clues, Counters, Curse, Mesmerize, DiscoverChits, HiddenEnemies, LearnAndAwaken, LearnSpell, Passages, Paths, PerceiveSpell, TakeTopTreasure, Take2ndTreasure, Take3rdTreasure, Take4thTreasure, Take5thTreasure, Take6thTreasure, TreasureCards, CaveTeleport, MountainTeleport, WoodsTeleport, RuinsTeleport, PeerEnchantAnyClearing, PowerOfThePit, Gold, Notoriety, Wish, Heal, SummonDemon, ReadRunesAnySite, Rest, RemoveCurse, Wound, Unhide };
	public static SearchResultType[] values() { SearchResultType[] r = new SearchResultType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static SearchResultType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}

	// --- enum methods ---
	public boolean canGetTreasure() {
		if (this == TakeTopTreasure) {
				return true;
			}
			else if (this == Take2ndTreasure) {
				return true;
			}
			else if (this == Take3rdTreasure) {
				return true;
			}
			else if (this == Take4thTreasure) {
				return true;
			}
			else if (this == Take5thTreasure) {
				return true;
			}
			else if (this == Take6thTreasure) {
				return true;
			}
			else if (this == TreasureCards) {
				return true;
			}
			else if (this == Counters) {
				return true;
			}
			else {
				return false;
			}
	}
	public boolean canGetSpell() {
		if (this == LearnAndAwaken) {
				return true;
			}
			else if (this == Awaken) {
				return true;
			}
			else if (this == PerceiveSpell) {
				return true;
			}
			else if (this == LearnSpell) {
				return true;
			}
			else {
				return false;
			}
	}
	public static String[] optionalValues() {
		ArrayList list = new ArrayList();
		list.add("");
		SearchResultType[] _vals2180 = values();
		for (int _i2180 = 0; _i2180 < _vals2180.length; _i2180++) {
			SearchResultType rt = _vals2180[_i2180];
			list.add(rt.toString());
		}
		return (String[]) list.toArray(new String[0]);
	}
	public static SearchResultType getLootSearchResultType(int treasureNumber) {
		switch(treasureNumber) {
			case 1:		return SearchResultType.TakeTopTreasure;
			case 2:		return SearchResultType.Take2ndTreasure;
			case 3:		return SearchResultType.Take3rdTreasure;
			case 4:		return SearchResultType.Take4thTreasure;
			case 5:		return SearchResultType.Take5thTreasure;
			case 6:		return SearchResultType.Take6thTreasure;
		}
		return null;
	}
	public static SearchResultType[] getSearchResultTypes(SearchTableType table) {
		ArrayList list = new ArrayList();
		list.add(SearchResultType.Any);
		if (table == SearchTableType.Any) {
				return SearchResultType.values();
			}
			else if (table == SearchTableType.Locate) {
				list.add(SearchResultType.Passages);
				list.add(SearchResultType.Clues);
				list.add(SearchResultType.DiscoverChits);
			}
			else if (table == SearchTableType.Loot) {
				list.add(SearchResultType.TakeTopTreasure);
				list.add(SearchResultType.Take2ndTreasure);
				list.add(SearchResultType.Take3rdTreasure);
				list.add(SearchResultType.Take4thTreasure);
				list.add(SearchResultType.Take5thTreasure);
				list.add(SearchResultType.Take6thTreasure);
			}
			else if (table == SearchTableType.MagicSight) {
				list.add(SearchResultType.Counters);
				list.add(SearchResultType.TreasureCards);
				list.add(SearchResultType.PerceiveSpell);
				list.add(SearchResultType.DiscoverChits);
			}
			else if (table == SearchTableType.Peer) {
				list.add(SearchResultType.Clues);
				list.add(SearchResultType.Paths);
				list.add(SearchResultType.HiddenEnemies);
			}
			else if (table == SearchTableType.ReadRunes) {
				list.add(SearchResultType.LearnAndAwaken);
				list.add(SearchResultType.Awaken);
				list.add(SearchResultType.Curse);
			}
			else if (table == SearchTableType.ToadstoolCircle) {
				list.add(SearchResultType.Counters);
				list.add(SearchResultType.TreasureCards);
				list.add(SearchResultType.CaveTeleport);
				list.add(SearchResultType.PeerEnchantAnyClearing);
				list.add(SearchResultType.PowerOfThePit);
			}
			else if (table == SearchTableType.CryptOfTheKnight) {
				list.add(SearchResultType.Counters);
				list.add(SearchResultType.TreasureCards);
				list.add(SearchResultType.Gold);
				list.add(SearchResultType.Curse);
			}
			else if (table == SearchTableType.EnchantedMeadow) {
				list.add(SearchResultType.Counters);
				list.add(SearchResultType.Wish);
				list.add(SearchResultType.Heal);
				list.add(SearchResultType.Curse);
			}
			else if (table == SearchTableType.FountainOfHealth) {
				list.add(SearchResultType.Heal);
				list.add(SearchResultType.Rest);
				list.add(SearchResultType.RemoveCurse);
				list.add(SearchResultType.Wound);
			}
			else if (table == SearchTableType.ArcheologicalDig) {
				list.add(SearchResultType.DiscoverChits);
				list.add(SearchResultType.Clues);
				list.add(SearchResultType.Gold);
				list.add(SearchResultType.Wound);
			}
			else if (table == SearchTableType.CircleOfStones) {
				list.add(SearchResultType.Counters);
				list.add(SearchResultType.TreasureCards);
				list.add(SearchResultType.MountainTeleport);
				list.add(SearchResultType.RuinsTeleport);
				list.add(SearchResultType.Curse);
			}
			else if (table == SearchTableType.EtherealAbbey) {
				list.add(SearchResultType.Counters);
				list.add(SearchResultType.TreasureCards);
				list.add(SearchResultType.LearnSpell);
				list.add(SearchResultType.Mesmerize);
			}
			else if (table == SearchTableType.FairyGrove) {
				list.add(SearchResultType.Counters);
				list.add(SearchResultType.TreasureCards);
				list.add(SearchResultType.LearnSpell);
				list.add(SearchResultType.WoodsTeleport);
				list.add(SearchResultType.Mesmerize);
				list.add(SearchResultType.Gold);
			}
			else if (table == SearchTableType.HauntedGrave) {
				list.add(SearchResultType.Counters);
				list.add(SearchResultType.TreasureCards);
				list.add(SearchResultType.Notoriety);
				list.add(SearchResultType.LearnSpell);
				list.add(SearchResultType.SummonDemon);
				list.add(SearchResultType.Unhide);
			}
			else if (table == SearchTableType.MageLibrary) {
				list.add(SearchResultType.Counters);
				list.add(SearchResultType.TreasureCards);
				list.add(SearchResultType.LearnSpell);
				list.add(SearchResultType.Mesmerize);
				list.add(SearchResultType.LearnAndAwaken);
				list.add(SearchResultType.Awaken);
				list.add(SearchResultType.Curse);
			}

		return (SearchResultType[]) list.toArray(new SearchResultType[list.size()]);
	}
}