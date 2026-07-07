package com.robin.magic_realm.components.quest;

import com.robin.magic_realm.components.RealmComponent;

public final class ChitType {
	private final String _name;
	private final int _ordinal;
	private ChitType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final ChitType Any = new ChitType("Any", 0);
	public static final ChitType TreasureLocation = new ChitType("TreasureLocation", 1);
	public static final ChitType Gate = new ChitType("Gate", 2);
	public static final ChitType Guild = new ChitType("Guild", 3);
	public static final ChitType Dwelling = new ChitType("Dwelling", 4);
	public static final ChitType Denizen = new ChitType("Denizen", 5);
	public static final ChitType Native = new ChitType("Native", 6);
	public static final ChitType ControlledNative = new ChitType("ControlledNative", 7);
	public static final ChitType NativeLeader = new ChitType("NativeLeader", 8);
	public static final ChitType HiredNativeLeader = new ChitType("HiredNativeLeader", 9);
	public static final ChitType Monster = new ChitType("Monster", 10);
	public static final ChitType ControlledMonster = new ChitType("ControlledMonster", 11);
	public static final ChitType Horse = new ChitType("Horse", 12);
	public static final ChitType NativeHorse = new ChitType("NativeHorse", 13);
	public static final ChitType Hireling = new ChitType("Hireling", 14);
	public static final ChitType Companion = new ChitType("Companion", 15);
	public static final ChitType Familiar = new ChitType("Familiar", 16);
	public static final ChitType MinorCharacter = new ChitType("MinorCharacter", 17);
	public static final ChitType HiredOrControlled = new ChitType("HiredOrControlled", 18);
	public static final ChitType Character = new ChitType("Character", 19);
	public static final ChitType Phantasm = new ChitType("Phantasm", 20);
	public static final ChitType Mist = new ChitType("Mist", 21);
	public static final ChitType TransformedAnimal = new ChitType("TransformedAnimal", 22);
	public static final ChitType Traveler = new ChitType("Traveler", 23);
	public static final ChitType Visitor = new ChitType("Visitor", 24);
	public static final ChitType VisitorMissionCampaign = new ChitType("VisitorMissionCampaign", 25);
	public static final ChitType Collectable = new ChitType("Collectable", 26);
	public static final ChitType Item = new ChitType("Item", 27);
	public static final ChitType Weapon = new ChitType("Weapon", 28);
	public static final ChitType Armor = new ChitType("Armor", 29);
	public static final ChitType ArmorCard = new ChitType("ArmorCard", 30);
	public static final ChitType Treasure = new ChitType("Treasure", 31);
	public static final ChitType GoldSpecialChit = new ChitType("GoldSpecialChit", 32);
	public static final ChitType Spell = new ChitType("Spell", 33);
	public static final ChitType ActionChit = new ChitType("ActionChit", 34);
	public static final ChitType MagicChit = new ChitType("MagicChit", 35);
	public static final ChitType RedSpecialChit = new ChitType("RedSpecialChit", 36);
	public static final ChitType Cloned = new ChitType("Cloned", 37);
	public static final ChitType Summoned = new ChitType("Summoned", 38);

	private static final ChitType[] _VALUES = { Any, TreasureLocation, Gate, Guild, Dwelling, Denizen, Native, ControlledNative, NativeLeader, HiredNativeLeader, Monster, ControlledMonster, Horse, NativeHorse, Hireling, Companion, Familiar, MinorCharacter, HiredOrControlled, Character, Phantasm, Mist, TransformedAnimal, Traveler, Visitor, VisitorMissionCampaign, Collectable, Item, Weapon, Armor, ArmorCard, Treasure, GoldSpecialChit, Spell, ActionChit, MagicChit, RedSpecialChit, Cloned, Summoned };
	public static ChitType[] values() { ChitType[] r = new ChitType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static ChitType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}

	// --- enum methods ---
	public boolean matches(RealmComponent rc) {
		if (this == Any) {
				return true;
			}
			else if (this == TreasureLocation) {
				return rc.isTreasureLocation();
			}
			else if (this == Gate) {
				return rc.isGate();
			}
			else if (this == Guild) {
				return rc.isGuild();
			}
			else if (this == Dwelling) {
				return rc.isDwelling();
			}
			else if (this == Denizen) {
				return rc.isDenizen();
			}
			else if (this == Native) {
				return rc.isNative();
			}
			else if (this == ControlledNative) {
				return rc.isControlledNative();
			}
			else if (this == NativeLeader) {
				return rc.isNativeLeader();
			}
			else if (this == HiredNativeLeader) {
				return rc.isHiredLeader();
			}
			else if (this == Monster) {
				return rc.isMonster();
			}
			else if (this == ControlledMonster) {
				return rc.isControlledMonster();
			}
			else if (this == Horse) {
				return rc.isHorse();
			}
			else if (this == NativeHorse) {
				return rc.isNativeHorse();
			}
			else if (this == Hireling) {
				return rc.isHireling();
			}
			else if (this == Companion) {
				return rc.isCompanion();
			}
			else if (this == Familiar) {
				return rc.isFamiliar();
			}
			else if (this == MinorCharacter) {
				return rc.isMinorCharacter();
			}
			else if (this == HiredOrControlled) {
				return rc.isHiredOrControlled();
			}
			else if (this == Character) {
				return rc.isCharacter();
			}
			else if (this == Phantasm) {
				return rc.isPhantasm();
			}
			else if (this == Mist) {
				return rc.isMistLike();
			}
			else if (this == TransformedAnimal) {
				return rc.isTransformAnimal();
			}
			else if (this == Traveler) {
				return rc.isTraveler();
			}
			else if (this == Visitor) {
				return rc.isVisitor();
			}
			else if (this == VisitorMissionCampaign) {
				return rc.isRedSpecial();
			}
			else if (this == Collectable) {
				return rc.isCollectibleThing();
			}
			else if (this == Item) {
				return rc.isItem();
			}
			else if (this == Weapon) {
				return rc.isWeapon();
			}
			else if (this == Armor) {
				return rc.isArmor();
			}
			else if (this == ArmorCard) {
				return rc.isArmorCard();
			}
			else if (this == Treasure) {
				return rc.isTreasure();
			}
			else if (this == GoldSpecialChit) {
				return rc.isGoldSpecial();
			}
			else if (this == Spell) {
				return rc.isSpell();
			}
			else if (this == ActionChit) {
				return rc.isActionChit();
			}
			else if (this == MagicChit) {
				return rc.isMagicChit();
			}
			else if (this == RedSpecialChit) {
				return rc.isRedSpecial();
			}
			else if (this == Cloned) {
				return rc.isCloned();
			}
			else if (this == Summoned) {
				return rc.isSummoned();
			}
		return false;
	}
}