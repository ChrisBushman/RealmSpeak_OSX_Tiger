package com.robin.magic_realm.RealmQuestBuilder;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;

import com.robin.game.objects.*;
import com.robin.magic_realm.RealmQuestBuilder.QuestPropertyBlock.FieldType;
import com.robin.magic_realm.components.*;
import com.robin.magic_realm.components.attribute.ColorMagic;
import com.robin.magic_realm.components.attribute.RelationshipType;
import com.robin.magic_realm.components.quest.*;
import com.robin.magic_realm.components.quest.requirement.*;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.RealmCalendar;
import com.robin.magic_realm.components.utility.SpellUtility.TeleportType;

public class QuestRequirementEditor extends QuestBlockEditor {

	private Quest quest;
	private QuestRequirement requirement;

	public QuestRequirementEditor(JFrame parent, GameData realmSpeakData, Quest quest, QuestRequirement requirement) {
		super(parent, realmSpeakData, requirement);
		this.quest = quest;
		this.requirement = requirement;
		setLocationRelativeTo(parent);
		read();
	}

	public String getEditorTitle() {
		return "Quest Requirement: " + requirement.getRequirementType();
	}

	public boolean getCanceledEdit() {
		return canceledEdit;
	}

	protected ArrayList createPropertyBlocks() {
		ArrayList list = new ArrayList();
		list.add(new QuestPropertyBlock(QuestRequirement.NOT,"NOT",FieldType.Boolean));
		{
			QuestRequirement.RequirementType _rt = requirement.getRequirementType();
			if (_rt == QuestRequirement.RequirementType.Action) {
				list.add(new QuestPropertyBlock(QuestRequirementAction.ACTION, "Action", FieldType.StringSelector, CharacterActionType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementAction.ACTION_WITHOUT_NATIVES, "Hire and trade action without natives", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.Attribute) {
				list.add(new QuestPropertyBlock(QuestRequirementAttribute.ATTRIBUTE_TYPE, "Which attribute to measure", FieldType.StringSelector, AttributeType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementAttribute.TARGET_VALUE_TYPE, "Only count points gained during the", FieldType.StringSelector, TargetValueType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementAttribute.VALUE, "How much should it be", FieldType.NumberAll));
				list.add(new QuestPropertyBlock(QuestRequirementAttribute.REGEX_FILTER, "Filter points to what things (regex)", FieldType.Regex, null, new String[] { "item", "spell", "denizen" }));
				list.add(new QuestPropertyBlock(QuestRequirementAttribute.REGEX_DESCRIPTION, "Optional description for regex", FieldType.TextLine));
				list.add(new QuestPropertyBlock(QuestRequirementAttribute.INCLUDE_INVENTORY, "Do count fame/notoriety of inventory", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirement.AUTO_JOURNAL, "Auto Journal Entry", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.CastMultipleSpells) {
				list.add(new QuestPropertyBlock(QuestRequirementCastMultipleSpells.NUMBER_OF_SPELLS, "Number of spells", FieldType.Number));
				list.add(new QuestPropertyBlock(QuestRequirementCastMultipleSpells.UNIQUE, "Unique spells", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementCastMultipleSpells.TARGET_VALUE_TYPE, "Only count points gained during the", FieldType.StringSelector, TargetValueType.values()));
			} else if (_rt == QuestRequirement.RequirementType.CastSpell) {
				list.add(new QuestPropertyBlock(QuestRequirementCastSpell.REGEX_FILTER, "Spell to be cast (regex)", FieldType.Regex, null, new String[] {"spell"}));
				list.add(new QuestPropertyBlock(QuestRequirementCastSpell.SCROLL_OR_BOOK, "Must use a scroll or book", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementCastSpell.ARTIFACT, "Must use an artifact", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementCastSpell.RING, "Must use a ring", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.CharacterClass) {
				list.add(new QuestPropertyBlock(QuestRequirementCharacterClass.REGEX_FILTER, "Character(s)", FieldType.Regex, null, new String[] {"character"}));
			} else if (_rt == QuestRequirement.RequirementType.CharacterType) {
				list.add(new QuestPropertyBlock(QuestRequirementCharacterType.REQUIRES_TRANSMORPHED, "Must be transmorphed", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementCharacterType.TYPE, "Type", FieldType.StringSelector, TransmorphType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementCharacterType.REGEX_FILTER, "Characters or transmorphed name", FieldType.Regex, null, new String[] {"denizen", "character"}));
			} else if (_rt == QuestRequirement.RequirementType.Chit) {
				list.add(new QuestPropertyBlock(QuestRequirementChit.TYPE, "Chit type", FieldType.StringSelector, QuestRequirementChit.ChitType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementChit.AMOUNT, "Number of chits", FieldType.Number));
				list.add(new QuestPropertyBlock(QuestRequirementChit.STRENGTH, "Strength of the chits", FieldType.StringSelector, VulnerabilityType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementChit.SPEED, "Speed of the chits (0=any)", FieldType.NumberAll));
				list.add(new QuestPropertyBlock(QuestRequirementChit.MAGIC_COLOR, "Magic color (only for magic chits)", FieldType.StringSelector, new Object[] { "Any",ColorMagic.White,ColorMagic.Grey,ColorMagic.Gold,ColorMagic.Purple,ColorMagic.Black }));
				list.add(new QuestPropertyBlock(QuestRequirementChit.MAGIC_TYPE, "Magic type (only for magic chits)", FieldType.Number));
				list.add(new QuestPropertyBlock(QuestRequirementChit.ONLY_ACTIVE, "Chits must be active", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementChit.NOT_FATIGUED, "Chits must not be fatigued", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementChit.NOT_WOUNDED, "Chits must not be wounded", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.Clearing) {
				list.add(new QuestPropertyBlock(QuestRequirementClearing.TILE, "Check all clearings of tile", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementClearing.TILE_SIDE, "Tile side", FieldType.StringSelector, LocationTileSideType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementClearing.TYPE, "Clearing type", FieldType.StringSelector, LocationClearingType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementClearing.CHIT_AMOUNT, "Amount of chits in clearing(s)", FieldType.NumberAll));
				list.add(new QuestPropertyBlock(QuestRequirementClearing.CHIT_TYPE, "Chit type", FieldType.StringSelector, ChitType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementClearing.CHIT_NAME, "Chit name", FieldType.Regex, null, null));
				list.add(new QuestPropertyBlock(QuestRequirementClearing.MARK_REQUIRED, "Chit requires a mark", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.ColorMagic) {
				list.add(new QuestPropertyBlock(QuestRequirementColorMagic.COLOR_KEY, "In the presence of color magic.", FieldType.StringSelector, Constants.MAGIC_COLORS));
			} else if (_rt == QuestRequirement.RequirementType.Counter) {
				list.add(new QuestPropertyBlock(QuestRequirementCounter.COUNTER, "Quest Counter", FieldType.GameObjectWrapperSelector, quest.getCounters().toArray()));
				list.add(new QuestPropertyBlock(QuestRequirementCounter.TARGET_VALUE, "Target count", FieldType.NumberAll));
				list.add(new QuestPropertyBlock(QuestRequirementCounter.EXCEED_TARGET_VALUE, "Exceed target value allowed", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementCounter.SUBCEED_TARGET_VALUE, "Subceed target value allowed", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirement.AUTO_JOURNAL, "Auto Journal Entry", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.Curse) {
				list.add(new QuestPropertyBlock(QuestRequirementCurse.CURSE, "Curse", FieldType.StringSelector, getCurseStrings().toArray()));
			} else if (_rt == QuestRequirement.RequirementType.Discovery) {
				list.add(new QuestPropertyBlock(QuestRequirementDiscovery.DISCOVERY_KEY, "Discovery", FieldType.StringSelector, getDiscoveryStrings().toArray()));
			} else if (_rt == QuestRequirement.RequirementType.Enchant) {
				list.add(new QuestPropertyBlock(QuestRequirementEnchant.TYPE, "Target", FieldType.StringSelector, new String[] { "chit", RealmComponent.TILE} ));
				list.add(new QuestPropertyBlock(QuestRequirementEnchant.CHIT, "Enchanted chit / chit must be on the tile", FieldType.StringSelector, getAllChitTypes() ));
				list.add(new QuestPropertyBlock(QuestRequirementEnchant.SITE, "Site ust be on the tile?", FieldType.StringSelector, getTreasureLocationsAndRedSpecialsAndDwellingsStrings().toArray()));
			} else if (_rt == QuestRequirement.RequirementType.Fly) {
				list.add(new QuestPropertyBlock(QuestRequirementFly.FLY, "Needs to fly?", FieldType.StringSelector, new String[] { QuestRequirementFly.FLYING,QuestRequirementFly.ABILITY_TO_FLY }));
			} else if (_rt == QuestRequirement.RequirementType.GamePhase) {
				list.add(new QuestPropertyBlock(QuestRequirementGamePhase.GAME_PHASE_TYPE, "Only at", FieldType.StringSelector, GamePhaseType.values()));
			} else if (_rt == QuestRequirement.RequirementType.Gender) {
				list.add(new QuestPropertyBlock(QuestRequirementGender.GENDER, "Gender", FieldType.StringSelector, new String[] { GenderType.Female.toString(), GenderType.Male.toString() }));
			} else if (_rt == QuestRequirement.RequirementType.Guild) {
				list.add(new QuestPropertyBlock(QuestRequirementGuild.GUILD, "Guild", FieldType.Regex, null, new String[] { "guild" }));
				list.add(new QuestPropertyBlock(QuestRequirementGuild.GUILD_LEVEL, "Guild level (1-3)", FieldType.Number));
				list.add(new QuestPropertyBlock(QuestRequirementGuild.EXCEED_LEVEL, "Exceed target level allowed", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementGuild.SUBCEED_LEVEL, "Subceed target level allowed", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.GuildLocation) {
				list.add(new QuestPropertyBlock(QuestRequirementGuildLocation.GUILD, "Guild", FieldType.Regex, null, new String[] { "guild" }));
				list.add(new QuestPropertyBlock(QuestRequirementGuildLocation.CURRENT_GUILD, "Check for current Guild", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.HideResult) {
				list.add(new QuestPropertyBlock(QuestRequirementHideResult.DIE_ROLL, "Die roll", FieldType.StringSelector, DieRollType.values()));
			} else if (_rt == QuestRequirement.RequirementType.Hire) {
				list.add(new QuestPropertyBlock(QuestRequirementHire.REGEX_FILTER, "Denizen", FieldType.Regex, null, new String[] { "denizen" }));
				list.add(new QuestPropertyBlock(QuestRequirementHire.NUMBER_OF_HIRELINGS, "Number of hirelings", FieldType.Number));
			} else if (_rt == QuestRequirement.RequirementType.Hirelings) {
				list.add(new QuestPropertyBlock(QuestRequirementHirelings.HIRELING_REGEX, "Hireling RegEx", FieldType.Regex, null, new String[] { "denizen" }));
				list.add(new QuestPropertyBlock(QuestRequirementHirelings.AMOUNT, "Number of hirelings", FieldType.Number));
				list.add(new QuestPropertyBlock(QuestRequirementHirelings.MUST_FOLLOW, "Must be following", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementHirelings.SAME_LOCATION, "Same location (incl. character riding it)", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementHirelings.VULNERARBILITY, "Vulnerability", FieldType.StringSelector, VulnerabilityType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementHirelings.ATTACK_STRENGTH, "Attack strength", FieldType.StringSelector, VulnerabilityType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementHirelings.ATTACK_SPEED, "Attack speed", FieldType.NumberAll));
				list.add(new QuestPropertyBlock(QuestRequirementHirelings.ATTACK_LENGTH, "Attack length", FieldType.NumberAll));
				list.add(new QuestPropertyBlock(QuestRequirementHirelings.SHARPNESS, "Number of sharpness stars", FieldType.NumberAll));
				list.add(new QuestPropertyBlock(QuestRequirementHirelings.MOVE_SPEED, "Move speed", FieldType.NumberAll));
				list.add(new QuestPropertyBlock(QuestRequirementHirelings.FLY_SPEED, "Fly speed", FieldType.NumberAll));
				list.add(new QuestPropertyBlock(QuestRequirementHirelings.MISSILE, "Must own missile attack", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementHirelings.ARMORED, "Must be armored", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementHirelings.CHECK_BOTH_SIDES, "Check ligth and dark chit sides", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementHirelings.INCLUDE_WEAPONS, "Include monster weapons", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.Inventory) {
				list.add(new QuestPropertyBlock(QuestRequirementLoot.TREASURE_TYPE, "Type of inventory", FieldType.StringSelector, TreasureType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementLoot.REGEX_FILTER, "Inventory name filter (regex)", FieldType.Regex, null, new String[] { "item","treasure_within_treasure" }));
				list.add(new QuestPropertyBlock(QuestRequirementInventory.NUMBER, "How many in inventory?", FieldType.NumberAll));
				list.add(new QuestPropertyBlock(QuestRequirementInventory.EXACT_NUMBER, "Exactly the amount?", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementInventory.ITEM_ACTIVE, "Require activated?", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementInventory.ITEM_DEACTIVE, "Require deactivated?", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementLoot.REQ_MARK, "Require mark?", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementLoot.ADD_MARK, "Add marks to items?", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementLoot.REQ_ABILITY, "Requires ability?", FieldType.TextLine));
			} else if (_rt == QuestRequirement.RequirementType.InventoryValue) {
				list.add(new QuestPropertyBlock(QuestRequirementInventoryValue.GOLD_VALUE, "Value of Gold", FieldType.NumberAll));
				list.add(new QuestPropertyBlock(QuestRequirementInventoryValue.FAME_VALUE, "Value of Fame", FieldType.NumberAll));
				list.add(new QuestPropertyBlock(QuestRequirementLoot.TREASURE_TYPE, "Type of inventory", FieldType.StringSelector, TreasureType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementLoot.REGEX_FILTER, "Inventory name filter (regex)", FieldType.Regex, null, new String[] { "item","treasure_within_treasure" }));
				list.add(new QuestPropertyBlock(QuestRequirementLoot.REQ_MARK, "Add marks to items?", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementLoot.REQ_ABILITY, "Requires ability?", FieldType.TextLine));
			} else if (_rt == QuestRequirement.RequirementType.Kill) {
				list.add(new QuestPropertyBlock(QuestRequirementKill.REGEX_FILTER, "Denizen name filter (regex)", FieldType.Regex, null, new String[] { "denizen" }));
				list.add(new QuestPropertyBlock(QuestRequirementKill.REGEX_DESCRIPTION, "Optional description for regex", FieldType.TextLine));
				list.add(new QuestPropertyBlock(QuestRequirementKill.VALUE, "How many (" + QuestConstants.ALL_VALUE + " means ALL)", FieldType.Number));
				list.add(new QuestPropertyBlock(QuestRequirementKill.VULNERABILITY, "Vulnerability", FieldType.StringSelector, VulnerabilityType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementKill.ARMORED, "Armor", FieldType.StringSelector, ArmoredType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementKill.TARGET_VALUE_TYPE, "Only count points gained during the", FieldType.StringSelector, TargetValueType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementKill.REQUIRE_MARK, "Mark is required", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementKill.KILL_CHARACTERS, "Must kill characters (not denizens)", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.KillDenizenSummonedByChit) {
				list.add(new QuestPropertyBlock(QuestRequirementKillDenizenSummonedByChit.CHIT, "Chit type", FieldType.StringSelector, getAllChitTypes()));
				list.add(new QuestPropertyBlock(QuestRequirementKillDenizenSummonedByChit.TILE_TYPE, "Tile type", FieldType.StringSelector, getAllTileTypes()));
				list.add(new QuestPropertyBlock(QuestRequirementKillDenizenSummonedByChit.SAME_CLEARING, "Must be in the clearing with the chit", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.KillGuardian) {
				list.add(new QuestPropertyBlock(QuestRequirementKillGuardian.GUARDIAN_AND_SITE, "Guardian", FieldType.StringSelector, getGuardians().toArray()));
				list.add(new QuestPropertyBlock(QuestRequirementKillGuardian.TARGET_VALUE_TYPE, "Only count points gained during the", FieldType.StringSelector, TargetValueType.values()));
			} else if (_rt == QuestRequirement.RequirementType.LearnAwaken) {
				list.add(new QuestPropertyBlock(QuestRequirementLearnAwaken.REGEX_FILTER, "Spell filter (regex)", FieldType.Regex, null, new String[] { "spell,learnable" }));
				list.add(new QuestPropertyBlock(QuestRequirementLearnAwaken.MUST_LEARN, "Must Learn Spell", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.LocationExists) {
				list.add(new QuestPropertyBlock(QuestRequirementLocationExists.LOCATION, "Quest Location", FieldType.GameObjectWrapperSelector, quest.getLocations().toArray()));
			} else if (_rt == QuestRequirement.RequirementType.Loot) {
				list.add(new QuestPropertyBlock(QuestRequirementLoot.TREASURE_TYPE, "Type of Loot to acquire", FieldType.StringSelector, TreasureType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementLoot.REGEX_FILTER, "Loot name filter (regex)", FieldType.Regex, null, new String[] { "item","treasure_within_treasure" }));
				list.add(new QuestPropertyBlock(QuestRequirementLoot.REQ_MARK, "Loot requires mark", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementLoot.ADD_MARK, "Add marks to loot?", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementLoot.REQ_ABILITY, "Requires ability?", FieldType.TextLine));
			} else if (_rt == QuestRequirement.RequirementType.NextPhase) {
				list.add(new QuestPropertyBlock(QuestRequirementNextPhase.PHASES_TO_SKIP, "Phases to pass (1 day has 4 phases)", FieldType.Number));
			} else if (_rt == QuestRequirement.RequirementType.NoDenizens) {
				list.add(new QuestPropertyBlock(QuestRequirementNoDenizens.NO_MONSTERS, "No Monsters", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementNoDenizens.NO_NATIVES, "No Natives", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementNoDenizens.TILE_WIDE, "All Tile Clearings", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.MinorCharacter) {
				list.add(new QuestPropertyBlock(QuestRequirementMinorCharacter.MINOR_CHARACTER, "Minor character ", FieldType.SmartTextLine, quest.getMinorCharacters().toArray()));
			} else if (_rt == QuestRequirement.RequirementType.MissionCampaign) {
				list.add(new QuestPropertyBlock(QuestRequirementMissionCampaign.ACTION_TYPE, "Mission/Campaign action", FieldType.StringSelector, CharacterActionType.mcValues()));
				list.add(new QuestPropertyBlock(QuestRequirementMissionCampaign.DISABLE_ON_PICKUP, "Disable on Pickup", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementMissionCampaign.REGEX_FILTER, "Mission/Campaign filter (regex)", FieldType.Regex, null, new String[] { "mission","campaign" }));
			} else if (_rt == QuestRequirement.RequirementType.OccupyLocation) {
				list.add(new QuestPropertyBlock(QuestRequirementLocation.LOCATION, "Quest Location", FieldType.GameObjectWrapperSelector, quest.getLocations().toArray()));
				list.add(new QuestPropertyBlock(QuestRequirementLocation.NON_CLEARING, "Non-clearing location allowed", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.Open) {
				list.add(new QuestPropertyBlock(QuestRequirementOpen.LOCATION_REGEX, "Location to open/search", FieldType.Regex, null, new String[] { "needs_open", "search" }));
				list.add(new QuestPropertyBlock(QuestRequirementOpen.OPEN_BY_ANYONE, "Open by anyone (chest or vault)", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.Path) {
				list.add(new QuestPropertyBlock(QuestRequirementPath.PATH, "Specific Path (like \"CV1 CV3 CV6 CV4 CV5\")", FieldType.SmartTextArea, getAllClearingCodes()));
				list.add(new QuestPropertyBlock(QuestRequirementPath.TIME_RESTRICTION, "Only count moves made during the", FieldType.StringSelector, TargetValueType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementPath.CHECK_REVERSE, "Either direction", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementPath.ALLOW_TRANSPORT, "Allow teleport", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.Probability) {
				list.add(new QuestPropertyBlock(QuestRequirementProbability.CHANCE, "Probability in % (1-100)", FieldType.Number));
				list.add(new QuestPropertyBlock(QuestRequirementProbability.MAX_NUMBER_OF_CHECKS, "Max. checks per day (0: unlimited)", FieldType.NumberAll));
			} else if (_rt == QuestRequirement.RequirementType.Relationship) {
				list.add(new QuestPropertyBlock(QuestRequirementRelationship.NATIVE_GROUP, "Native group", FieldType.Regex, null, new String[] {"native,rank=HQ", "visitor"}));
				list.add(new QuestPropertyBlock(QuestRequirementRelationship.RELATIONSHIP_LEVEL, "Relationship", FieldType.StringSelector, RelationshipType.RelationshipNames));
				list.add(new QuestPropertyBlock(QuestRequirementRelationship.EXCEED_LEVEL, "Exceed target level allowed", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementRelationship.SUBCEED_LEVEL, "Subceed target value allowed", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.SearchResult) {
				list.add(new QuestPropertyBlock(QuestRequirementSearchResult.REQ_TABLENAME, "Search table", FieldType.StringSelector, SearchTableType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementSearchResult.LOCATION, "Location of search", FieldType.GameObjectWrapperSelector, getOptionalQuestLocationArray().toArray()));
				list.add(new QuestPropertyBlock(QuestRequirementSearchResult.TARGET_LOC, "Target of search", FieldType.GameObjectWrapperSelector, getOptionalQuestLocationArray().toArray()));
				list.add(new QuestPropertyBlock(QuestRequirementSearchResult.TARGET_REGEX, "OR", FieldType.Regex, null, new String[] { "ts_section,!dwelling,!summon,!guild" }));
				list.add(new QuestPropertyBlock(QuestRequirementSearchResult.RESULT1, "Search result", FieldType.StringSelector, SearchResultType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementSearchResult.RESULT2, "OR", FieldType.StringSelector, SearchResultType.optionalValues()));
				list.add(new QuestPropertyBlock(QuestRequirementSearchResult.RESULT3, "OR", FieldType.StringSelector, SearchResultType.optionalValues()));
				list.add(new QuestPropertyBlock(QuestRequirementSearchResult.REQUIRES_GAIN, "Require search effect", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementSearchResult.REQUIRES_NO_GAIN, "Requires no search effect", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.SearchTile) {
				list.add(new QuestPropertyBlock(QuestRequirementSearchTile.TILE_TYPE, "Tile type", FieldType.StringSelector, getAllTileTypes()));
				list.add(new QuestPropertyBlock(QuestRequirementSearchTile.CHIT, "Chit", FieldType.StringSelector, getAllChitTypes()));
				list.add(new QuestPropertyBlock(QuestRequirementSearchTile.HIGHEST_NUMBERED_CLREAING, "Highest numbered clearing with site/sound", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementSearchTile.TABLENAME, "Search table", FieldType.StringSelector, SearchTableType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementSearchTile.RESULT1, "Search result", FieldType.StringSelector, SearchResultType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementSearchTile.RESULT2, "OR", FieldType.StringSelector, SearchResultType.optionalValues()));
				list.add(new QuestPropertyBlock(QuestRequirementSearchTile.RESULT3, "OR", FieldType.StringSelector, SearchResultType.optionalValues()));
			} else if (_rt == QuestRequirement.RequirementType.Season) {
				list.add(new QuestPropertyBlock(QuestRequirementSeason.SEASON, "Season", FieldType.StringSelector, getSeasonStrings().toArray() ));
			} else if (_rt == QuestRequirement.RequirementType.TimePassed) {
				list.add(new QuestPropertyBlock(QuestRequirementTimePassed.VALUE, "How many days", FieldType.Number));
			} else if (_rt == QuestRequirement.RequirementType.Trade) {
				list.add(new QuestPropertyBlock(QuestRequirementTrade.TRADE_TYPE, "Buy or Sell", FieldType.StringSelector,  TradeType.values()));
				list.add(new QuestPropertyBlock(QuestRequirementTrade.TRADE_ITEM_REGEX, "Item Traded", FieldType.Regex, null, new String[] { "item" }));
				list.add(new QuestPropertyBlock(QuestRequirementTrade.TRADE_WITH_REGEX, "Trade With", FieldType.Regex, null, new String[] { "native,rank=HQ","visitor" }));
				list.add(new QuestPropertyBlock(QuestRequirementTrade.TRADE_ITEM, "Item?", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementTrade.TRADE_TREASURE, "Treasure?", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementTrade.TRADE_SPELL, "Spell?", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementTrade.TRADE_CONDITIONAL_FAME, "Trade item with conditional fame?", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementTrade.ADD_MARK, "Add mark to traded item?", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.Traveler) {
				list.add(new QuestPropertyBlock(QuestRequirementTraveler.TRAVELER_REGEX, "Name", FieldType.Regex, null, new String[] { Constants.TRAVELER_TEMPLATE }));
				list.add(new QuestPropertyBlock(QuestRequirementTraveler.MARK, "Requires a mark?", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.Treachery) {
				list.add(new QuestPropertyBlock(QuestRequirementTreachery.REGEX_FILTER, "Denizen", FieldType.Regex, null, new String[] {"denizen"}));
				list.add(new QuestPropertyBlock(QuestRequirementTreachery.NATIVE_ONLY, "Only natives?", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementTreachery.KILL_IN_COMBAT, "Kill betrayed denizen in same combat?", FieldType.Boolean));
			} else if (_rt == QuestRequirement.RequirementType.Teleport) {
				list.add(new QuestPropertyBlock(QuestRequirementTeleport.TELEPORT_TYPE, "Teleport type", FieldType.StringSelector, getTeleportTypes().toArray()));
			} else if (_rt == QuestRequirement.RequirementType.Weather) {
				list.add(new QuestPropertyBlock(QuestRequirementWeather.WEATHER_ENABLED, "Weather must be enabled", FieldType.Boolean));
				list.add(new QuestPropertyBlock(QuestRequirementWeather.WEATHER, "Weather", FieldType.StringSelector, new String[] {"", RealmCalendar.WEATHER_CLEAR, RealmCalendar.WEATHER_SHOWERS, RealmCalendar.WEATHER_STORM, RealmCalendar.WEATHER_SPECIAL}));
			}
		}
		return list;
	}

	private String[] getAllClearingCodes() {
		ArrayList list = new ArrayList();
		GamePool pool = new GamePool(realmSpeakData.getGameObjects());
		for (java.util.Iterator _j14it282 = (pool.find("tile")).iterator(); _j14it282.hasNext(); ) {
		  GameObject go = (GameObject) _j14it282.next();
			TileComponent tile = (TileComponent)RealmComponent.getRealmComponent(go);
			String code = tile.getTileCode();
			for (java.util.Iterator _j14it283 = (tile.getClearings()).iterator(); _j14it283.hasNext(); ) {
			  ClearingDetail clearing = (ClearingDetail) _j14it283.next();
				list.add(code+clearing.getNumString());
			}
		}
		Collections.sort(list);
		return (String[]) list.toArray(new String[list.size()]);
	}

	private String[] getAllTileTypes() {
		ArrayList list = new ArrayList();
		ArrayList choices = new ArrayList();
		GamePool pool = new GamePool(realmSpeakData.getGameObjects());
		for (java.util.Iterator _j14it284 = (pool.find("tile")).iterator(); _j14it284.hasNext(); ) {
		  GameObject go = (GameObject) _j14it284.next();
			TileComponent tile = (TileComponent)RealmComponent.getRealmComponent(go);
			String type = tile.getTileType();
			if (list.indexOf(type) < 0) {
				list.add(type);
			}
		}
		Collections.sort(list);

		choices.add(QuestRequirement.ANY);
		choices.addAll(list);

		return (String[]) choices.toArray(new String[list.size()]);
	}

	private String[] getAllChitTypes() {
		ArrayList list = new ArrayList();
		ArrayList choices = new ArrayList();
		GamePool pool = new GamePool(realmSpeakData.getGameObjects());
		for (java.util.Iterator _j14it285 = (pool.find("chit,!red_special,!treasure_location,!minor_tl,!traveler,!gold,!guild,!gate")).iterator(); _j14it285.hasNext(); ) {
		  GameObject go = (GameObject) _j14it285.next();
			if (!list.contains(go.getName())) {
				list.add(go.getName());
			}
			RealmComponent rc = RealmComponent.getRealmComponent(go);
			if (rc.isSound()) {
				String sound = go.getThisAttribute(RealmComponent.SOUND);
				if (!list.contains(sound.toString())) {
					list.add(sound);
				}
			}
			if (rc.isWarning()) {
				String warning = go.getThisAttribute(RealmComponent.WARNING);
				if (!list.contains(warning.toString())) {
					list.add(warning);
				}
			}
		}
		Collections.sort(list);

		choices.add(QuestRequirement.NONE);
		choices.addAll(list);

		return (String[]) choices.toArray(new String[list.size()]);
	}

	private static ArrayList getCurseStrings() {
		ArrayList list = new ArrayList();
		list.add(Constants.ASHES);
		list.add(Constants.DISGUST);
		list.add(Constants.EYEMIST);
		list.add(Constants.ILL_HEALTH);
		list.add(Constants.SQUEAK);
		list.add(Constants.WITHER);
		list.add(Constants.MESMERIZE);
		return list;
	}

	private ArrayList getDiscoveryStrings() {
		ArrayList list = new ArrayList();

		// Build the discovery lists
		GamePool pool = new GamePool(realmSpeakData.getGameObjects());
		for (java.util.Iterator _j14it286 = (pool.find("treasure_location,discovery")).iterator(); _j14it286.hasNext(); ) {
		  GameObject go = (GameObject) _j14it286.next();
			if(!list.contains(go.getName())) list.add(go.getName());
		}
		for (java.util.Iterator _j14it287 = (pool.find("red_special")).iterator(); _j14it287.hasNext(); ) {
		  GameObject go = (GameObject) _j14it287.next();
			if(!list.contains(go.getName())) list.add(go.getName());
		}
		Collections.sort(list);

		ArrayList sublist = new ArrayList();
		for (java.util.Iterator _j14it288 = (pool.find("tile")).iterator(); _j14it288.hasNext(); ) {
		  GameObject go = (GameObject) _j14it288.next();
			TileComponent tile = (TileComponent)RealmComponent.getRealmComponent(go);
			for (java.util.Iterator _j14it289 = (tile.getHiddenPaths()).iterator(); _j14it289.hasNext(); ) {
			  PathDetail path = (PathDetail) _j14it289.next();
				if(!sublist.contains(path.getFullPathKey())) sublist.add(path.getFullPathKey());
			}
			for (java.util.Iterator _j14it290 = (tile.getSecretPassages()).iterator(); _j14it290.hasNext(); ) {
			  PathDetail path = (PathDetail) _j14it290.next();
				if(!sublist.contains(path.getFullPathKey())) sublist.add(path.getFullPathKey());
			}
		}
		Collections.sort(sublist);
		list.addAll(sublist);

		return list;
	}

	private ArrayList getTreasureLocationsAndRedSpecialsAndDwellingsStrings() {
		ArrayList list = new ArrayList();
		ArrayList choices = new ArrayList();

		// Build the discovery lists
		GamePool pool = new GamePool(realmSpeakData.getGameObjects());
		for (java.util.Iterator _j14it291 = (pool.find("treasure_location")).iterator(); _j14it291.hasNext(); ) {
		  GameObject go = (GameObject) _j14it291.next();
			if(!list.contains(go.getName())) list.add(go.getName());
		}
		for (java.util.Iterator _j14it292 = (pool.find("red_special")).iterator(); _j14it292.hasNext(); ) {
		  GameObject go = (GameObject) _j14it292.next();
			if(!list.contains(go.getName())) list.add(go.getName());
		}
		for (java.util.Iterator _j14it293 = (pool.find("dwelling,!virtual_dwelling")).iterator(); _j14it293.hasNext(); ) {
		  GameObject go = (GameObject) _j14it293.next();
			if(!list.contains(go.getName())) list.add(go.getName());
		}
		Collections.sort(list);

		choices.add(QuestRequirement.NONE);
		choices.addAll(list);

		return choices;
	}

	private ArrayList getOptionalQuestLocationArray() {
		ArrayList list = new ArrayList();
		ArrayList locations = quest.getLocations();
		if (locations != null) {
			list.addAll(locations);
		}
		list.add(0, null);
		return list;
	}

	private ArrayList getSeasonStrings() {
		ArrayList list = new ArrayList();
		GamePool pool = new GamePool(realmSpeakData.getGameObjects());
		for (java.util.Iterator _j14it294 = (pool.find("season")).iterator(); _j14it294.hasNext(); ) {
		  GameObject go = (GameObject) _j14it294.next();
			list.add(go.getName());
		}
		Collections.sort(list);
		return list;
	}

	private ArrayList getGuardians() {
		ArrayList guardians = new ArrayList();
		GamePool pool = new GamePool(realmSpeakData.getGameObjects());
		ArrayList monsters = pool.find("monster,setup_start");
		for (java.util.Iterator _j14it295 = (monsters).iterator(); _j14it295.hasNext(); ) {
		  GameObject monster = (GameObject) _j14it295.next();
			String setupStart = monster.getThisAttribute("setup_start");
			if (setupStart!=null && setupStart.length() > 0) {
				ArrayList boxes = pool.find("name="+setupStart+",treasure_location");
				for (java.util.Iterator _j14it296 = (boxes).iterator(); _j14it296.hasNext(); ) {
				  GameObject box = (GameObject) _j14it296.next();
					String guardianAndSite = monster.getName()+" ("+box.getName()+")";
					if (guardians.indexOf(guardianAndSite) < 0) {
						guardians.add(guardianAndSite);
					}
				}
			}
		}
		Collections.sort(guardians);
		return guardians;
	}

	private static ArrayList getTeleportTypes() {
		ArrayList list = new ArrayList();
		ArrayList teleportTypes = new ArrayList();
		TeleportType[] _j14arr297 = TeleportType.values();
		for (int _j14i297 = 0; _j14i297 < _j14arr297.length; _j14i297++) {
		  TeleportType type = _j14arr297[_j14i297];
			list.add(type.toString());
		}
		teleportTypes.add(QuestRequirementTeleport.ANY_TYPE);
		Collections.sort(list);
		teleportTypes.addAll(list);
		return teleportTypes;
	}
}
