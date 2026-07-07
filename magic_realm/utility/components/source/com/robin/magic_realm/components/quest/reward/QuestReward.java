package com.robin.magic_realm.components.quest.reward;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.quest.*;
import com.robin.magic_realm.components.utility.*;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

/*
 * Rewards are given to a character once the quest step requirements have been completed.  Rewards can be clustered into groups, in case there is a choice of rewards to give.
 */
public abstract class QuestReward extends AbstractQuestObject {
	
	public static final String REWARD_GROUP = "_rwg";
	public static final String ALL_REWARD_GROUP = "---";
	
	public static final class RewardType {
		private final String _name;
		private final int _ordinal;
		private RewardType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final RewardType ActivateQuest = new RewardType("ActivateQuest", 0);
		public static final RewardType AlterBlock = new RewardType("AlterBlock", 1);
		public static final RewardType AlterHide = new RewardType("AlterHide", 2);
		public static final RewardType Attribute = new RewardType("Attribute", 3);
		public static final RewardType ChooseNextStep = new RewardType("ChooseNextStep", 4);
		public static final RewardType ClonedQuestsComplete = new RewardType("ClonedQuestsComplete", 5);
		public static final RewardType ClonedQuestsFailed = new RewardType("ClonedQuestsFailed", 6);
		public static final RewardType ClonedQuestsCounter = new RewardType("ClonedQuestsCounter", 7);
		public static final RewardType Companion = new RewardType("Companion", 8);
		public static final RewardType Control = new RewardType("Control", 9);
		public static final RewardType Counter = new RewardType("Counter", 10);
		public static final RewardType Curse = new RewardType("Curse", 11);
		public static final RewardType CustomTreasure = new RewardType("CustomTreasure", 12);
		public static final RewardType Damage = new RewardType("Damage", 13);
		public static final RewardType DamageChit = new RewardType("DamageChit", 14);
		public static final RewardType DeactivateQuest = new RewardType("DeactivateQuest", 15);
		public static final RewardType DeductVps = new RewardType("DeductVps", 16);
		public static final RewardType DiscardOption = new RewardType("DiscardOption", 17);
		public static final RewardType DiscardQuest = new RewardType("DiscardQuest", 18);
		public static final RewardType DiscoverTreasureSite = new RewardType("DiscoverTreasureSite", 19);
		public static final RewardType DrawQuests = new RewardType("DrawQuests", 20);
		public static final RewardType EnchantTile = new RewardType("EnchantTile", 21);
		public static final RewardType Exorcise = new RewardType("Exorcise", 22);
		public static final RewardType FindHiddenEnemies = new RewardType("FindHiddenEnemies", 23);
		public static final RewardType Guild = new RewardType("Guild", 24);
		public static final RewardType Heal = new RewardType("Heal", 25);
		public static final RewardType Hireling = new RewardType("Hireling", 26);
		public static final RewardType Information = new RewardType("Information", 27);
		public static final RewardType Item = new RewardType("Item", 28);
		public static final RewardType Journal = new RewardType("Journal", 29);
		public static final RewardType KillDenizen = new RewardType("KillDenizen", 30);
		public static final RewardType LostInventoryToDefault = new RewardType("LostInventoryToDefault", 31);
		public static final RewardType LostInventoryToLocation = new RewardType("LostInventoryToLocation", 32);
		public static final RewardType MagicColor = new RewardType("MagicColor", 33);
		public static final RewardType MakeWhole = new RewardType("MakeWhole", 34);
		public static final RewardType MarkDenizen = new RewardType("MarkDenizen", 35);
		public static final RewardType MarkItem = new RewardType("MarkItem", 36);
		public static final RewardType MarkTraveler = new RewardType("MarkTraveler", 37);
		public static final RewardType MarkedView = new RewardType("MarkedView", 38);
		public static final RewardType Mesmerize = new RewardType("Mesmerize", 39);
		public static final RewardType MinorCharacter = new RewardType("MinorCharacter", 40);
		public static final RewardType MoveDenizen = new RewardType("MoveDenizen", 41);
		public static final RewardType NoCombat = new RewardType("NoCombat", 42);
		public static final RewardType NoProwling = new RewardType("NoProwling", 43);
		public static final RewardType NoSummoning = new RewardType("NoSummoning", 44);
		public static final RewardType Note = new RewardType("Note", 45);
		public static final RewardType PathsPassages = new RewardType("PathsPassages", 46);
		public static final RewardType Phantasm = new RewardType("Phantasm", 47);
		public static final RewardType PowerOfThePit = new RewardType("PowerOfThePit", 48);
		public static final RewardType QuestComplete = new RewardType("QuestComplete", 49);
		public static final RewardType QuestFailed = new RewardType("QuestFailed", 50);
		public static final RewardType QuestSticky = new RewardType("QuestSticky", 51);
		public static final RewardType QuestVps = new RewardType("QuestVps", 52);
		public static final RewardType RegenerateDenizen = new RewardType("RegenerateDenizen", 53);
		public static final RewardType RelationshipChange = new RewardType("RelationshipChange", 54);
		public static final RewardType RelationshipSet = new RewardType("RelationshipSet", 55);
		public static final RewardType Repair = new RewardType("Repair", 56);
		public static final RewardType ResetQuest = new RewardType("ResetQuest", 57);
		public static final RewardType ResetQuestLocations = new RewardType("ResetQuestLocations", 58);
		public static final RewardType ResetQuestSteps = new RewardType("ResetQuestSteps", 59);
		public static final RewardType ResetQuestToDeck = new RewardType("ResetQuestToDeck", 60);
		public static final RewardType Rest = new RewardType("Rest", 61);
		public static final RewardType ScareMonsters = new RewardType("ScareMonsters", 62);
		public static final RewardType SpellEffectOnCharacter = new RewardType("SpellEffectOnCharacter", 63);
		public static final RewardType SpellEffectOnClearing = new RewardType("SpellEffectOnClearing", 64);
		public static final RewardType SpellEffectOnTile = new RewardType("SpellEffectOnTile", 65);
		public static final RewardType SpellEffectSummon = new RewardType("SpellEffectSummon", 66);
		public static final RewardType SpellFromSite = new RewardType("SpellFromSite", 67);
		public static final RewardType StripInventory = new RewardType("StripInventory", 68);
		public static final RewardType SummonGeneratedMonster = new RewardType("SummonGeneratedMonster", 69);
		public static final RewardType SummonGuardian = new RewardType("SummonGuardian", 70);
		public static final RewardType SummonMonster = new RewardType("SummonMonster", 71);
		public static final RewardType SummonFromAppearanceToChit = new RewardType("SummonFromAppearanceToChit", 72);
		public static final RewardType SummonRoll = new RewardType("SummonRoll", 73);
		public static final RewardType SummonTraveler = new RewardType("SummonTraveler", 74);
		public static final RewardType TalkToWiseBird = new RewardType("TalkToWiseBird", 75);
		public static final RewardType Teleport = new RewardType("Teleport", 76);
		public static final RewardType TeleportChoose = new RewardType("TeleportChoose", 77);
		public static final RewardType Transmorph = new RewardType("Transmorph", 78);
		public static final RewardType TreasureFromHq = new RewardType("TreasureFromHq", 79);
		public static final RewardType TreasureFromSite = new RewardType("TreasureFromSite", 80);
		public static final RewardType Weather = new RewardType("Weather", 81);
		public static final RewardType Wish = new RewardType("Wish", 82);
		public static final RewardType Visitor = new RewardType("Visitor", 83);

		private static final RewardType[] _VALUES = { ActivateQuest, AlterBlock, AlterHide, Attribute, ChooseNextStep, ClonedQuestsComplete, ClonedQuestsFailed, ClonedQuestsCounter, Companion, Control, Counter, Curse, CustomTreasure, Damage, DamageChit, DeactivateQuest, DeductVps, DiscardOption, DiscardQuest, DiscoverTreasureSite, DrawQuests, EnchantTile, Exorcise, FindHiddenEnemies, Guild, Heal, Hireling, Information, Item, Journal, KillDenizen, LostInventoryToDefault, LostInventoryToLocation, MagicColor, MakeWhole, MarkDenizen, MarkItem, MarkTraveler, MarkedView, Mesmerize, MinorCharacter, MoveDenizen, NoCombat, NoProwling, NoSummoning, Note, PathsPassages, Phantasm, PowerOfThePit, QuestComplete, QuestFailed, QuestSticky, QuestVps, RegenerateDenizen, RelationshipChange, RelationshipSet, Repair, ResetQuest, ResetQuestLocations, ResetQuestSteps, ResetQuestToDeck, Rest, ScareMonsters, SpellEffectOnCharacter, SpellEffectOnClearing, SpellEffectOnTile, SpellEffectSummon, SpellFromSite, StripInventory, SummonGeneratedMonster, SummonGuardian, SummonMonster, SummonFromAppearanceToChit, SummonRoll, SummonTraveler, TalkToWiseBird, Teleport, TeleportChoose, Transmorph, TreasureFromHq, TreasureFromSite, Weather, Wish, Visitor };
		public static RewardType[] values() { RewardType[] r = new RewardType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static RewardType valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
		// --- enum methods ---
		public boolean isShown() {
			if (this == MarkDenizen) {
				return false;
			}
			return true;
		}

		public String getDescription() {
			if (this == ActivateQuest) {
				return "Activates the quest if not already active.  Primarily used for \"Questing the Realm\" gameplay option.";
			}
			else if (this == AlterBlock) {
				return "Change questing character's blocked status (from blocked to unblocked, or the other way around).";
			}
			else if (this == AlterHide) {
				return "Change questing character's hide status (from hidden to unhidden, or the other way around).";
			}
			else if (this == Attribute) {
				return "Modify Fame, Notoriety, or Gold.  Can either add or subtract points/gold.";
			}
			else if (this == ChooseNextStep) {
				return "Player chooses the next step to process from those steps that follow this step, and fullfill requirements.";
			}
			else if (this == ClonedQuestsComplete) {
				return "Tells RealmSpeak that the corresponding cloned quests have been completed.";
			}
			else if (this == ClonedQuestsFailed) {
				return "Tells RealmSpeak that the corresponding cloned quests have been failed.";
			}
			else if (this == ClonedQuestsCounter) {
				return "Modify count value of a counters of cloned quests.";
			}
			else if (this == Companion) {
				return "Add or remove a monster ally.";
			}
			else if (this == Control) {
				return "Gives the character control over denizens.";
			}
			else if (this == Counter) {
				return "Modify count value of a counter.";
			}
			else if (this == Curse) {
				return "Curses the character.";
			}
			else if (this == CustomTreasure) {
				return "Create a new treasure by taking an existing treasure, renaming it, and giving it new base attributes.";
			}
			else if (this == Damage) {
				return "Character receives fatigue or wounds.";
			}
			else if (this == DamageChit) {
				return "Character must fatigue or wound a specific chit.";
			}
			else if (this == DeductVps) {
				return "Character must deduct certain amount of VPs.";
			}
			else if (this == DiscardOption) {
				return "Defines if or if not quest can be discarded.";
			}
			else if (this == DiscardQuest) {
				return "Current quest is discarded.";
			}
			else if (this == DiscoverTreasureSite) {
				return "Character discovers treasure site(s).";
			}
			else if (this == DrawQuests) {
				return "Character draws quest card(s).";
			}
			else if (this == EnchantTile) {
				return "Enchants (or unenchants) characters tile and/or tile(s) of a location.";
			}
			else if (this == Exorcise) {
				return "Exorcise spell is cast";
			}
			else if (this == FindHiddenEnemies) {
				return "Character finds hidden enemies.";
			}
			else if (this == Guild) {
				return "Sets the characters guild and guild level.";
			}
			else if (this == Heal) {
				return "Heals action chits of the character.";
			}
			else if (this == Hireling) {
				return "Add or remove a hireling.";
			}
			else if (this == Information) {
				return "Displays a dialog with information in it.  This is a good way to inform the player what is happening.";
			}
			else if (this == Item) {
				return "Add an item to the character inventory, or take one away (placed to location defined by 'LostInventoryToLocation/Default').  Allows for choosing items from a group.";
			}
			else if (this == Journal) {
				return "Add or update a journal entry for this quest.";
			}
			else if (this == KillDenizen) {
				return "Kills a denizen.";
			}
			else if (this == LostInventoryToDefault) {
				return "All future lost inventory from this quest will go to wherever they started the game, including treasures.  This is the default setting.";
			}
			else if (this == LostInventoryToLocation) {
				return "All future lost inventory from this quest will go to a specified location.";
			}
			else if (this == MagicColor) {
				return "Provides a magic color to a clearing.";
			}
			else if (this == MakeWhole) {
				return "Heals all fatigue and wounds, cancels wither curse and repairs items.";
			}
			else if (this == MarkDenizen) {
				return "Mark a particular denizen for later reference.  This is useful if you want to make sure a character kills (for example) a particular monster.";
			}
			else if (this == MarkItem) {
				return "Marks particular item(s) for later reference.  This is useful if you want to make sure a character owns (for example) a particular item.";
			}
			else if (this == MarkTraveler) {
				return "Marks a random or specific traveler.  This is useful if you want to make sure a character hires a traveler or that a traveler is in a certain location.";
			}
			else if (this == MarkedView) {
				return "Enables or disables the Marked Things view in the quest view.";
			}
			else if (this == MinorCharacter) {
				return "Add or remove a Minor Character.  Must create Minor Characters BEFORE creating this reward.";
			}
			else if (this == MoveDenizen) {
				return "Move one or several denizen (or travelers) to a location.";
			}
			else if (this == NoCombat) {
				return "Character does not participate in combat this round.";
			}
			else if (this == NoProwling) {
				return "Character does not make denizens prowling this round.";
			}
			else if (this == NoSummoning) {
				return "Character does not summon monsters this round.";
			}
			else if (this == Note) {
				return "Add a note to characters journal.";
			}
			else if (this == PathsPassages) {
				return "Discover Paths and/or Passages in the current clearing or tile.";
			}
			else if (this == Phantasm) {
				return "Give or remove a phantasm to the character.";
			}
			else if (this == PowerOfThePit) {
				return "Grants the character a Power of the Pit.";
			}
			else if (this == QuestComplete) {
				return "Tells RealmSpeak that the character has completed this quest.";
			}
			else if (this == QuestFailed) {
				return "Tells RealmSpeak that the character has failed this quest.";
			}
			else if (this == QuestVps) {
				return "Changes the VP reward of the quest or add bonus quest points for character.";
			}
			else if (this == RegenerateDenizen) {
				return "Regenerates denizen back to the chart of appearance.";
			}
			else if (this == RelationshipChange) {
				return "Modify the relationship of the character with a particular native group, or all natives in the clearing.";
			}
			else if (this == RelationshipSet) {
				return "Set the relationship of the character with a particular native group, or all natives in the clearing.";
			}
			else if (this == Repair) {
				return "Repairs all items ot fhe character.";
			}
			else if (this == ResetQuest) {
				return "Completely resets the quest, unmarking all quest steps and journal entries.";
			}
			else if (this == ResetQuestLocations) {
				return "Completely resets the quest locations.";
			}
			else if (this == ResetQuestSteps) {
				return "Resets all quest steps depending (with a certain 'depth') on current step.";
			}
			else if (this == ResetQuestToDeck) {
				return "Resets the quest and shuffles it into the available quests again.";
			}
			else if (this == Rest) {
				return "Rests characters chits.";
			}
			else if (this == ScareMonsters) {
				return "Randomly move all monsters in current clearing to other clearings either in the same tile or other tiles, as defined.";
			}
			else if (this == SpellEffectOnCharacter) {
				return "Cast a spell effect on the character which grants/removes an ability forever.";
			}
			else if (this == SpellEffectOnClearing) {
				return "Cast a spell effect on the character clearing or removes it.";
			}
			else if (this == SpellEffectOnTile) {
				return "Cast a spell effect on the character tile or removes it.";
			}
			else if (this == SpellEffectSummon) {
				return "Summon creatures for the character.";
			}
			else if (this == SpellFromSite) {
				return "Learn a spell from a specific site, book, artifact, or Shaman.";
			}
			else if (this == StripInventory) {
				return "Removes ALL inventory and (optionally) gold from the character (placed to location defined by 'LostInventoryToLocation/Default').";
			}
			else if (this == SummonGeneratedMonster) {
				return "Generate and summon a specific monster to the a clearing.";
			}
			else if (this == SummonGuardian) {
				return "For a specific quest location, summon the treasure site guardian (if any)";
			}
			else if (this == SummonMonster) {
				return "Summon a specific monster to a clearing.";
			}
			else if (this == SummonFromAppearanceToChit) {
				return "Summon a specific monster from the chart of appearance to a sound or warning chit.";
			}
			else if (this == SummonRoll) {
				return "Force a monster summoning roll with a specific number.";
			}
			else if (this == SummonTraveler) {
				return "Summon a random or specific traveler to a clearing.";
			}
			else if (this == TalkToWiseBird) {
				return "Character does instantly a free peer action";
			}
			else if (this == Teleport) {
				return "Teleport the character to a new location. Must create a QuestLocation BEFORE creating this reward.";
			}
			else if (this == TeleportChoose) {
				return "Teleport the character to another clearing. Note: no effect in QuestTester";
			}
			else if (this == Transmorph) {
				return "Transmorphs the character.";
			}
			else if (this == TreasureFromSite) {
				return "Gain a treasure from a specific site, dwelling, or Scholar.";
			}
			else if (this == TreasureFromHq) {
				return "Gain a treasure from a specific HQ.";
			}
			else if (this == Weather) {
				return "Sets the weather.";
			}
			else if (this == Wish) {
				return "Grants the character a wish.";
			}
			else {
				return "(No Description)";
			}
		}
		public boolean requiresLocations() {
			return this==LostInventoryToLocation || this==SummonGuardian || this==Teleport;
		}
	}
	
	public QuestReward(GameObject go) {
		super(go);
	}
	public void init() {
		setName("Reward");
		getGameObject().setThisAttribute(Quest.QUEST_REWARD,getRewardType().toString());
	}
	public String toString() {
		return getDescription();
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
	
	public void setRewardGroup(String val) {
		setString(REWARD_GROUP,val);
	}
	
	public String getRewardGroup() {
		return getString(REWARD_GROUP);
	}
		
	public void updateIds(Hashtable lookup) {
		// override if IDs need to be updated!
	}

	public void lostItem(GameObject toRemove) {
		Quest quest = getParentQuest();
		RewardType lostInventoryRule = quest.getLostInventoryRule();
		if (lostInventoryRule==RewardType.LostInventoryToDefault) {
			lostItemToDefault(toRemove);
		}
		else if (lostInventoryRule==RewardType.LostInventoryToLocation) {
			QuestLocation location = quest.getLostInventoryLocation();
			lostItemToLocation(toRemove,location);
		}
	}
	public void lostItemToDefault(GameObject go) {
		GameObject originalSetupOwner = go.getGameObjectFromAttribute("this",Constants.SETUP);
		if (originalSetupOwner!=null) {
			originalSetupOwner.add(go);
		}
		else {
			go.detach(); // Might happen in the quest tester
			RealmLogging.logMessage(QuestConstants.QUEST_ERROR,"Unable to identify setup start for "+go.getName()+" when removing item for quest \""+getParentQuest().getName()+"\".");
		}
	}
	
	private void lostItemToLocation(GameObject go,QuestLocation location) {
		ArrayList validLocations = new ArrayList();
		validLocations = location.fetchAllLocations(getGameData());
		if(validLocations.isEmpty()) {
			RealmLogging.logMessage(QuestConstants.QUEST_ERROR,"Item "+go.getName()+" didn't get moved to QuestLocation "+location.getName()+" for some reason...");
			return;
		}
		int random = RandomNumber.getRandom(validLocations.size());
		TileLocation tileLocation = (TileLocation) validLocations.get(random);
		
		ArrayList clearingComponents = tileLocation.clearing.getClearingComponents();
		if (!clearingComponents.isEmpty()) {
			for (java.util.Iterator _j14it2416 = (clearingComponents).iterator(); _j14it2416.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it2416.next();
				if (rc.isTreasureLocation() || rc.isDwelling() || rc.isVisitor()) {
					rc.getGameObject().add(go);
					return;
				}
			}
		}
		ClearingUtility.moveToLocation(go,tileLocation);
	}

	public abstract void processReward(JFrame frame,CharacterWrapper character);
	public abstract RewardType getRewardType();
	public abstract String getDescription();
	
	/**
	 * Override this to supply an icon.
	 */
	public ImageIcon getIcon() {
		return null;
	}
	
	public static QuestReward getReward(RewardType type,GameObject go) {
		QuestReward reward = null;
		if (type == RewardType.ActivateQuest) {
			reward = new QuestRewardActivateQuest(go);
		} else if (type == RewardType.AlterBlock) {
			reward = new QuestRewardAlterBlock(go);
		} else if (type == RewardType.AlterHide) {
			reward = new QuestRewardAlterHide(go);
		} else if (type == RewardType.Attribute) {
			reward = new QuestRewardAttribute(go);
		} else if (type == RewardType.ChooseNextStep) {
			reward = new QuestRewardChooseNextStep(go);
		} else if (type == RewardType.ClonedQuestsComplete) {
			reward = new QuestRewardClonedQuestsComplete(go);
		} else if (type == RewardType.ClonedQuestsFailed) {
			reward = new QuestRewardClonedQuestsFailed(go);
		} else if (type == RewardType.ClonedQuestsCounter) {
			reward = new QuestRewardClonedQuestsCounter(go);
		} else if (type == RewardType.Companion) {
			reward = new QuestRewardCompanion(go);
		} else if (type == RewardType.Control) {
			reward = new QuestRewardControl(go);
		} else if (type == RewardType.Counter) {
			reward = new QuestRewardCounter(go);
		} else if (type == RewardType.Curse) {
			reward = new QuestRewardCurse(go);
		} else if (type == RewardType.CustomTreasure) {
			reward = new QuestRewardCustomTreasure(go);
		} else if (type == RewardType.Damage) {
			reward = new QuestRewardDamage(go);
		} else if (type == RewardType.DamageChit) {
			reward = new QuestRewardDamageChit(go);
		} else if (type == RewardType.DeactivateQuest) {
			reward = new QuestRewardDeactivateQuest(go);
		} else if (type == RewardType.DeductVps) {
			reward = new QuestRewardDeductVps(go);
		} else if (type == RewardType.DiscardOption) {
			reward = new QuestRewardDiscardOption(go);
		} else if (type == RewardType.DiscardQuest) {
			reward = new QuestRewardDiscardQuest(go);
		} else if (type == RewardType.DiscoverTreasureSite) {
			reward = new QuestRewardDiscoverTreasureSite(go);
		} else if (type == RewardType.DrawQuests) {
			reward = new QuestRewardDrawQuests(go);
		} else if (type == RewardType.EnchantTile) {
			reward = new QuestRewardEnchantTile(go);
		} else if (type == RewardType.Exorcise) {
			reward = new QuestRewardExorcise(go);
		} else if (type == RewardType.FindHiddenEnemies) {
			reward = new QuestRewardFindHiddenEnemies(go);
		} else if (type == RewardType.Guild) {
			reward = new QuestRewardGuild(go);
		} else if (type == RewardType.Heal) {
			reward = new QuestRewardHeal(go);
		} else if (type == RewardType.Hireling) {
			reward = new QuestRewardHireling(go);
		} else if (type == RewardType.Information) {
			reward = new QuestRewardInformation(go);
		} else if (type == RewardType.Item) {
			reward = new QuestRewardItem(go);
		} else if (type == RewardType.Journal) {
			reward = new QuestRewardJournal(go);
		} else if (type == RewardType.KillDenizen) {
			reward = new QuestRewardKillDenizen(go);
		} else if (type == RewardType.LostInventoryToDefault) {
			reward = new QuestRewardLostInventoryToDefault(go);
		} else if (type == RewardType.LostInventoryToLocation) {
			reward = new QuestRewardLostInventoryToLocation(go);
		} else if (type == RewardType.MagicColor) {
			reward = new QuestRewardMagicColor(go);
		} else if (type == RewardType.MakeWhole) {
			reward = new QuestRewardMakeWhole(go);
		} else if (type == RewardType.MarkDenizen) {
			reward = new QuestRewardMarkDenizen(go);
		} else if (type == RewardType.MarkItem) {
			reward = new QuestRewardMarkItem(go);
		} else if (type == RewardType.MarkTraveler) {
			reward = new QuestRewardMarkTraveler(go);
		} else if (type == RewardType.MarkedView) {
			reward = new QuestRewardMarkedView(go);
		} else if (type == RewardType.Mesmerize) {
			reward = new QuestRewardMesmerize(go);
		} else if (type == RewardType.MinorCharacter) {
			reward = new QuestRewardMinorCharacter(go);
		} else if (type == RewardType.MoveDenizen) {
			reward = new QuestRewardMoveDenizen(go);
		} else if (type == RewardType.NoCombat) {
			reward = new QuestRewardNoCombat(go);
		} else if (type == RewardType.NoProwling) {
			reward = new QuestRewardNoProwling(go);
		} else if (type == RewardType.NoSummoning) {
			reward = new QuestRewardNoSummoning(go);
		} else if (type == RewardType.Note) {
			reward = new QuestRewardNote(go);
		} else if (type == RewardType.PathsPassages) {
			reward = new QuestRewardPathsPassages(go);
		} else if (type == RewardType.Phantasm) {
			reward = new QuestRewardPhantasm(go);
		} else if (type == RewardType.PowerOfThePit) {
			reward = new QuestRewardPowerOfThePit(go);
		} else if (type == RewardType.QuestComplete) {
			reward = new QuestRewardComplete(go);
		} else if (type == RewardType.QuestFailed) {
			reward = new QuestRewardFailed(go);
		} else if (type == RewardType.QuestSticky) {
			reward = new QuestRewardQuestSticky(go);
		} else if (type == RewardType.QuestVps) {
			reward = new QuestRewardQuestVps(go);
		} else if (type == RewardType.RegenerateDenizen) {
			reward = new QuestRewardRegenerateDenizen(go);
		} else if (type == RewardType.RelationshipChange) {
			reward = new QuestRewardRelationshipChange(go);
		} else if (type == RewardType.RelationshipSet) {
			reward = new QuestRewardRelationshipSet(go);
		} else if (type == RewardType.Repair) {
			reward = new QuestRewardRepair(go);
		} else if (type == RewardType.ResetQuest) {
			reward = new QuestRewardResetQuest(go);
		} else if (type == RewardType.ResetQuestLocations) {
			reward = new QuestRewardResetQuestLocations(go);
		} else if (type == RewardType.ResetQuestSteps) {
			reward = new QuestRewardResetQuestSteps(go);
		} else if (type == RewardType.ResetQuestToDeck) {
			reward = new QuestRewardResetQuestToDeck(go);
		} else if (type == RewardType.Rest) {
			reward = new QuestRewardRest(go);
		} else if (type == RewardType.ScareMonsters) {
			reward = new QuestRewardScareMonsters(go);
		} else if (type == RewardType.SpellEffectOnCharacter) {
			reward = new QuestRewardSpellEffectOnCharacter(go);
		} else if (type == RewardType.SpellEffectOnClearing) {
			reward = new QuestRewardSpellEffectOnClearing(go);
		} else if (type == RewardType.SpellEffectOnTile) {
			reward = new QuestRewardSpellEffectOnTile(go);
		} else if (type == RewardType.SpellEffectSummon) {
			reward = new QuestRewardSpellEffectSummon(go);
		} else if (type == RewardType.SpellFromSite) {
			reward = new QuestRewardSpellFromSite(go);
		} else if (type == RewardType.StripInventory) {
			reward = new QuestRewardStripInventory(go);
		} else if (type == RewardType.SummonGeneratedMonster) {
			reward = new QuestRewardSummonGeneratedMonster(go);
		} else if (type == RewardType.SummonGuardian) {
			reward = new QuestRewardSummonGuardian(go);
		} else if (type == RewardType.SummonMonster) {
			reward = new QuestRewardSummonMonster(go);
		} else if (type == RewardType.SummonFromAppearanceToChit) {
			reward = new QuestRewardSummonFromAppearanceToChit(go);
		} else if (type == RewardType.SummonRoll) {
			reward = new QuestRewardSummonRoll(go);
		} else if (type == RewardType.SummonTraveler) {
			reward = new QuestRewardSummonTraveler(go);
		} else if (type == RewardType.TalkToWiseBird) {
			reward = new QuestRewardTalkToWiseBird(go);
		} else if (type == RewardType.Teleport) {
			reward = new QuestRewardTeleport(go);
		} else if (type == RewardType.TeleportChoose) {
			reward = new QuestRewardTeleportChoose(go);
		} else if (type == RewardType.Transmorph) {
			reward = new QuestRewardTransmorph(go);
		} else if (type == RewardType.TreasureFromHq) {
			reward = new QuestRewardTreasureFromHq(go);
		} else if (type == RewardType.TreasureFromSite) {
			reward = new QuestRewardTreasureFromSite(go);
		} else if (type == RewardType.Visitor) {
			reward = new QuestRewardVisitor(go);
		} else if (type == RewardType.Weather) {
			reward = new QuestRewardWeather(go);
		} else if (type == RewardType.Wish) {
			reward = new QuestRewardWish(go);
		} else {
			throw new IllegalArgumentException("Unsupported RewardType: "+type.toString());
		}
		return reward;
	}
	public static void main(String[] args) {
		RewardType[] _j14v2417 = RewardType.values();
		for (int _j14i2417 = 0; _j14i2417 < _j14v2417.length; _j14i2417++) {
		  RewardType rt = _j14v2417[_j14i2417];
			StringBuffer sb = new StringBuffer();
			sb.append("<tr><th valign=\"top\">");
			sb.append(rt.toString());
			sb.append("</th><td>");
			sb.append(rt.getDescription());
			sb.append("</td></tr><br>");
			System.out.println(sb.toString());
		}
	}
	public static int getDieRoll(DieRollType dieRoll) {
		if (dieRoll == DieRollType.One) {
			return 1;
		} else if (dieRoll == DieRollType.Two) {
			return 2;
		} else if (dieRoll == DieRollType.Three) {
			return 3;
		} else if (dieRoll == DieRollType.Four) {
			return 4;
		} else if (dieRoll == DieRollType.Five) {
			return 5;
		} else if (dieRoll == DieRollType.Six) {
			return 6;
		} else {
			return RandomNumber.getDieRoll(6);
		}
	}
}