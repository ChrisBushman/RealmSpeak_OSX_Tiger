package com.robin.magic_realm.components.quest.requirement;

import java.util.Hashtable;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.quest.*;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

/**
 * Quest requirements determine what must happen before the step can be
 * completed, and rewards (if any) are given.
 */
public abstract class QuestRequirement extends AbstractQuestObject {

	public static final String ANY = "any";
	public static final String NONE = "none";
	
	public static final class RequirementType {
		private final String _name;
		private final int _ordinal;
		private RequirementType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final RequirementType Action = new RequirementType("Action", 0);
		public static final RequirementType Active = new RequirementType("Active", 1);
		public static final RequirementType CastMultipleSpells = new RequirementType("CastMultipleSpells", 2);
		public static final RequirementType CastSpell = new RequirementType("CastSpell", 3);
		public static final RequirementType CharacterClass = new RequirementType("CharacterClass", 4);
		public static final RequirementType CharacterType = new RequirementType("CharacterType", 5);
		public static final RequirementType Chit = new RequirementType("Chit", 6);
		public static final RequirementType Clearing = new RequirementType("Clearing", 7);
		public static final RequirementType ColorMagic = new RequirementType("ColorMagic", 8);
		public static final RequirementType Counter = new RequirementType("Counter", 9);
		public static final RequirementType Curse = new RequirementType("Curse", 10);
		public static final RequirementType Discovery = new RequirementType("Discovery", 11);
		public static final RequirementType Fighter = new RequirementType("Fighter", 12);
		public static final RequirementType Fly = new RequirementType("Fly", 13);
		public static final RequirementType FoundHiddenEnemies = new RequirementType("FoundHiddenEnemies", 14);
		public static final RequirementType GameEnd = new RequirementType("GameEnd", 15);
		public static final RequirementType GamePhase = new RequirementType("GamePhase", 16);
		public static final RequirementType end = new RequirementType("end", 17);
		public static final RequirementType start = new RequirementType("start", 18);
		public static final RequirementType midnight = new RequirementType("midnight", 19);
		public static final RequirementType birdsong = new RequirementType("birdsong", 20);
		public static final RequirementType Guild = new RequirementType("Guild", 21);
		public static final RequirementType GuildLocation = new RequirementType("GuildLocation", 22);
		public static final RequirementType Hidden = new RequirementType("Hidden", 23);
		public static final RequirementType HideResult = new RequirementType("HideResult", 24);
		public static final RequirementType Hire = new RequirementType("Hire", 25);
		public static final RequirementType Hirelings = new RequirementType("Hirelings", 26);
		public static final RequirementType Inventory = new RequirementType("Inventory", 27);
		public static final RequirementType Kill = new RequirementType("Kill", 28);
		public static final RequirementType KillDenizenSummonedByChit = new RequirementType("KillDenizenSummonedByChit", 29);
		public static final RequirementType KillGuardian = new RequirementType("KillGuardian", 30);
		public static final RequirementType LearnAwaken = new RequirementType("LearnAwaken", 31);
		public static final RequirementType LocationExists = new RequirementType("LocationExists", 32);
		public static final RequirementType Loot = new RequirementType("Loot", 33);
		public static final RequirementType NoDenizens = new RequirementType("NoDenizens", 34);
		public static final RequirementType MagicUser = new RequirementType("MagicUser", 35);
		public static final RequirementType MinorCharacter = new RequirementType("MinorCharacter", 36);
		public static final RequirementType MissionCampaign = new RequirementType("MissionCampaign", 37);
		public static final RequirementType Open = new RequirementType("Open", 38);
		public static final RequirementType OccupyLocation = new RequirementType("OccupyLocation", 39);
		public static final RequirementType Path = new RequirementType("Path", 40);
		public static final RequirementType Relationship = new RequirementType("Relationship", 41);
		public static final RequirementType Season = new RequirementType("Season", 42);
		public static final RequirementType SearchResult = new RequirementType("SearchResult", 43);
		public static final RequirementType Paths = new RequirementType("Paths", 44);
		public static final RequirementType Passages = new RequirementType("Passages", 45);
		public static final RequirementType Discover = new RequirementType("Discover", 47);
		public static final RequirementType Learn = new RequirementType("Learn", 48);
		public static final RequirementType Awaken = new RequirementType("Awaken", 50);
		public static final RequirementType Counters = new RequirementType("Counters", 51);
		public static final RequirementType Treasure = new RequirementType("Treasure", 52);
		public static final RequirementType Perceive = new RequirementType("Perceive", 53);
		public static final RequirementType TimePassed = new RequirementType("TimePassed", 54);
		public static final RequirementType Teleport = new RequirementType("Teleport", 55);
		public static final RequirementType Trade = new RequirementType("Trade", 56);
		public static final RequirementType Traveler = new RequirementType("Traveler", 57);
		public static final RequirementType Treachery = new RequirementType("Treachery", 58);
		public static final RequirementType Weather = new RequirementType("Weather", 59);
		public static final RequirementType Attribute = new RequirementType("Attribute", 60);
		public static final RequirementType Enchant = new RequirementType("Enchant", 61);
		public static final RequirementType Gender = new RequirementType("Gender", 62);
		public static final RequirementType InventoryValue = new RequirementType("InventoryValue", 63);
		public static final RequirementType NextPhase = new RequirementType("NextPhase", 64);
		public static final RequirementType Probability = new RequirementType("Probability", 65);
		public static final RequirementType SearchTile = new RequirementType("SearchTile", 66);

		private static final RequirementType[] _VALUES = { Action, Active, CastMultipleSpells, CastSpell, CharacterClass, CharacterType, Chit, Clearing, ColorMagic, Counter, Curse, Discovery, Fighter, Fly, FoundHiddenEnemies, GameEnd, GamePhase, end, start, midnight, birdsong, Guild, GuildLocation, Hidden, HideResult, Hire, Hirelings, Inventory, Kill, KillDenizenSummonedByChit, KillGuardian, LearnAwaken, LocationExists, Loot, NoDenizens, MagicUser, MinorCharacter, MissionCampaign, Open, OccupyLocation, Path, Relationship, Season, SearchResult, Paths, Passages, Discover, Learn, Awaken, Counters, Treasure, Perceive, TimePassed, Teleport, Trade, Traveler, Treachery, Weather, Attribute, Enchant, Gender, InventoryValue, NextPhase, Probability, SearchTile };
		public static RequirementType[] values() { RequirementType[] r = new RequirementType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static RequirementType valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
		// --- enum methods ---
		public String getDescription() {
			if (this == Action) {
				return "Tests for the character to execute a specific action.";
			}
			else if (this == Active) {
				return "Tests whether the quest is active or not (needed for Quest Cards).";
			}
			else if (this == CastMultipleSpells) {
				return "Tests whether a certain number of (unique) spells have been cast.";
			}
			else if (this == CastSpell) {
				return "Tests whether a spell has just been cast.";
			}
			else if (this == CharacterClass) {
				return "Tests for the characters class.";
			}
			else if (this == CharacterType) {
				return "Tests for the characters name or transmorphed form.";
			}
			else if (this == Chit) {
				return "Tests for whether the characters has certain chits.";
			}
			else if (this == Clearing) {
				return "Tests for chits at character's clearing or tile or that character is in specific clearing.";
			}
			else if (this == ColorMagic) {
				return "Tests whether the character is in the presence of a specific color of magic (either permanent or burning a chit).";
			}
			else if (this == Counter) {
				return "Tests whether a specific counter has reached a certain value.";
			}
			else if (this == Curse) {
				return "Tests for a specific curse.";
			}
			else if (this == Discovery) {
				return "Tests for a specific recorded discovery.";
			}
			else if (this == Fighter) {
				return "Tests if the character is a fighter.";
			}
			else if (this == Fly) {
				return "Tests if the character is currently flying or has the ability to fly.";
			}
			else if (this == FoundHiddenEnemies) {
				return "Tests if the character has found hidden enemies.";
			}
			else if (this == GameEnd) {
				return "Tests if the game ended.";
			}
			else if (this == GamePhase) {
				return "Tests for a specific game phase (Birdsong, End-of-phase, End-of-turn, Evening).";
			}
			else if (this == Guild) {
				return "Tests if the character has a certain guild level.";
			}
			else if (this == GuildLocation) {
				return "Tests if the character is in the clearing with a certain guild.";
			}
			else if (this == Hidden) {
				return "Tests if the character is hidden.";
			}
			else if (this == HideResult) {
				return "Tests if the character succeeds a hide roll (below or equal to a specfic value).";
			}
			else if (this == Hire) {
				return "Tests if character hires certain natives.";
			}
			else if (this == Hirelings) {
				return "Tests if character owns certain hirelings.";
			}
			else if (this == Inventory) {
				return "Tests the contents of inventory.";
			}
			else if (this == Kill) {
				return "Tests for a specific kill or kills.";
			}
			else if (this == KillDenizenSummonedByChit) {
				return "Tests for a kill of denizen summoned by a specific chit.";
			}
			else if (this == KillGuardian) {
				return "Tests for a kill of a specific Guardian.";
			}
			else if (this == LearnAwaken) {
				return "Tests whether a spell has just been awakened and/or learned.";
			}
			else if (this == LocationExists) {
				return "Tests whether a location exists.";
			}
			else if (this == Loot) {
				return "Tests for a specific item result of looting.";
			}
			else if (this == NoDenizens) {
				return "Tests for the absence of monster/natives in the clearing or tile.";
			}
			else if (this == MagicUser) {
				return "Tests if the character is a magic user.";
			}
			else if (this == MinorCharacter) {
				return "Tests for the presence of a minor character in the character inventory.";
			}
			else if (this == MissionCampaign) {
				return "Tests for start/stop of Mission/Campaign chits.";
			}
			else if (this == OccupyLocation) {
				return "Tests whether the character is in a specific location.";
			}
			else if (this == Open) {
				return "Tests whether the character (or anyone) opens specific location.";
			}
			else if (this == Path) {
				return "Tests whether the character has followed a specific path.";
			}
			else if (this == Relationship) {
				return "Tests whether the character has certain relationship with specific natives.";
			}
			else if (this == SearchResult) {
				return "Tests for a specific search result.";
			}
			else if (this == Season) {
				return "Tests for a specific season.";
			}
			else if (this == Teleport) {
				return "Tests for that character is teleported.";
			}
			else if (this == TimePassed) {
				return "Tests for a specific length of time (in days) passed.";
			}
			else if (this == Trade) {
				return "Tests for a specific TRADE occurrence.";
			}
			else if (this == Traveler) {
				return "Tests for any or a specific Traveler to be in a certain location or hired by the character.";
			}
			else if (this == Treachery) {
				return "Tests for that character commiting treachery.";
			}
			else if (this == Weather) {
				return "Tests for a specific weather.";
			}
			return "(No Description)";
		}
	}
	
	public String toString() {
		return buildDescription();
	}
	
	public static final String AUTO_JOURNAL = "_aj";
	public static final String NOT = "_not__";

	protected abstract boolean testFulfillsRequirement(JFrame frame, CharacterWrapper character, QuestRequirementParams reqParams);
	public abstract RequirementType getRequirementType();
	protected abstract String buildDescription();

	public QuestRequirement(GameObject go) {
		super(go);
	}

	public void init() {
		setName("Requirement");
		getGameObject().setThisAttribute(Quest.QUEST_REQUIREMENT, getRequirementType().toString());
	}

	/**
	 * Called when quest is first activated to allow setting up requirement at
	 * quest start.
	 */
	public void activate(CharacterWrapper character) {
		// This implementation does nothing.  Override to add this functionality.
	}

	/**
	 * Override this method if location is relevant, and handle appropriately.
	 */
	public boolean usesLocationTag(String tag) {
		return false;
	}
	
	/**
	 * Override this method if minor character is relevant, and handle appropriately.
	 */
	public boolean usesMinorCharacter(QuestMinorCharacter mc) {
		return false;
	}

	/**
	 * Override this method if counter is relevant, and handle appropriately.
	 */
	public boolean usesCounterTag(String tag) {
		return false;
	}
	
	/**
	 * Override this method if auto journal is relevant, and handle appropriately.
	 */
	public boolean usesAutoJournal() {
		return false;
	}

	public void updateIds(Hashtable lookup) {
		// override if IDs need to be updated!
	}

	public boolean isAutoJournal() {
		return getBoolean(AUTO_JOURNAL);
	}

	public boolean isNot() {
		return getBoolean(NOT);
	}

	public String getDescription() {
		return (isNot() ? "NOT " : "") + buildDescription();
	}

	public boolean fulfillsRequirement(JFrame frame, CharacterWrapper character, QuestRequirementParams reqParams) {
		boolean result = testFulfillsRequirement(frame, character, reqParams);
		return isNot() ? !result : result;
	}

	public static QuestRequirement getRequirement(RequirementType type, GameObject go) {
		QuestRequirement requirement = null;
		if (type == RequirementType.Action) {
			requirement = new QuestRequirementAction(go);
		} else if (type == RequirementType.Active) {
			requirement = new QuestRequirementActive(go);
		} else if (type == RequirementType.Attribute) {
			requirement = new QuestRequirementAttribute(go);
		} else if (type == RequirementType.CastMultipleSpells) {
			requirement = new QuestRequirementCastMultipleSpells(go);
		} else if (type == RequirementType.CastSpell) {
			requirement = new QuestRequirementCastSpell(go);
		} else if (type == RequirementType.CharacterClass) {
			requirement = new QuestRequirementCharacterClass(go);
		} else if (type == RequirementType.CharacterType) {
			requirement = new QuestRequirementCharacterType(go);
		} else if (type == RequirementType.Chit) {
			requirement = new QuestRequirementChit(go);
		} else if (type == RequirementType.Clearing) {
			requirement = new QuestRequirementClearing(go);
		} else if (type == RequirementType.ColorMagic) {
			requirement = new QuestRequirementColorMagic(go);
		} else if (type == RequirementType.Counter) {
			requirement = new QuestRequirementCounter(go);
		} else if (type == RequirementType.Curse) {
			requirement = new QuestRequirementCurse(go);
		} else if (type == RequirementType.Discovery) {
			requirement = new QuestRequirementDiscovery(go);
		} else if (type == RequirementType.Enchant) {
			requirement = new QuestRequirementEnchant(go);
		} else if (type == RequirementType.Fighter) {
			requirement = new QuestRequirementFighter(go);
		} else if (type == RequirementType.Fly) {
			requirement = new QuestRequirementFly(go);
		} else if (type == RequirementType.FoundHiddenEnemies) {
			requirement = new QuestRequirementFoundHiddenEnemies(go);
		} else if (type == RequirementType.GameEnd) {
			requirement = new QuestRequirementGameEnd(go);
		} else if (type == RequirementType.GamePhase) {
			requirement = new QuestRequirementGamePhase(go);
		} else if (type == RequirementType.Gender) {
			requirement = new QuestRequirementGender(go);
		} else if (type == RequirementType.Guild) {
			requirement = new QuestRequirementGuild(go);
		} else if (type == RequirementType.GuildLocation) {
			requirement = new QuestRequirementGuildLocation(go);
		} else if (type == RequirementType.Hidden) {
			requirement = new QuestRequirementHidden(go);
		} else if (type == RequirementType.HideResult) {
			requirement = new QuestRequirementHideResult(go);
		} else if (type == RequirementType.Hire) {
			requirement = new QuestRequirementHire(go);
		} else if (type == RequirementType.Hirelings) {
			requirement = new QuestRequirementHirelings(go);
		} else if (type == RequirementType.Inventory) {
			requirement = new QuestRequirementInventory(go);
		} else if (type == RequirementType.InventoryValue) {
			requirement = new QuestRequirementInventoryValue(go);
		} else if (type == RequirementType.Kill) {
			requirement = new QuestRequirementKill(go);
		} else if (type == RequirementType.KillDenizenSummonedByChit) {
			requirement = new QuestRequirementKillDenizenSummonedByChit(go);
		} else if (type == RequirementType.KillGuardian) {
			requirement = new QuestRequirementKillGuardian(go);
		} else if (type == RequirementType.LearnAwaken) {
			requirement = new QuestRequirementLearnAwaken(go);
		} else if (type == RequirementType.LocationExists) {
			requirement = new QuestRequirementLocationExists(go);
		} else if (type == RequirementType.Loot) {
			requirement = new QuestRequirementLoot(go);
		} else if (type == RequirementType.NextPhase) {
			requirement = new QuestRequirementNextPhase(go);
		} else if (type == RequirementType.NoDenizens) {
			requirement = new QuestRequirementNoDenizens(go);
		} else if (type == RequirementType.MagicUser) {
			requirement = new QuestRequirementMagicUser(go);
		} else if (type == RequirementType.MinorCharacter) {
			requirement = new QuestRequirementMinorCharacter(go);
		} else if (type == RequirementType.MissionCampaign) {
			requirement = new QuestRequirementMissionCampaign(go);
		} else if (type == RequirementType.OccupyLocation) {
			requirement = new QuestRequirementLocation(go);
		} else if (type == RequirementType.Open) {
			requirement = new QuestRequirementOpen(go);
		} else if (type == RequirementType.Path) {
			requirement = new QuestRequirementPath(go);
		} else if (type == RequirementType.Probability) {
			requirement = new QuestRequirementProbability(go);
		} else if (type == RequirementType.Relationship) {
			requirement = new QuestRequirementRelationship(go);
		} else if (type == RequirementType.SearchResult) {
			requirement = new QuestRequirementSearchResult(go);
		} else if (type == RequirementType.SearchTile) {
			requirement = new QuestRequirementSearchTile(go);
		} else if (type == RequirementType.Season) {
			requirement = new QuestRequirementSeason(go);
		} else if (type == RequirementType.Teleport) {
			requirement = new QuestRequirementTeleport(go);
		} else if (type == RequirementType.TimePassed) {
			requirement = new QuestRequirementTimePassed(go);
		} else if (type == RequirementType.Trade) {
			requirement = new QuestRequirementTrade(go);
		} else if (type == RequirementType.Traveler) {
			requirement = new QuestRequirementTraveler(go);
		} else if (type == RequirementType.Treachery) {
			requirement = new QuestRequirementTreachery(go);
		} else if (type == RequirementType.Weather) {
			requirement = new QuestRequirementWeather(go);
		} else {
			throw new IllegalArgumentException("Unsupported RequirementType: " + type.toString());
		}
		return requirement;
	}
}