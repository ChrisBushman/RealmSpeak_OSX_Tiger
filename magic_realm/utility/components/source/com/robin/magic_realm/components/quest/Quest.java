package com.robin.magic_realm.components.quest;

import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.swing.*;

import com.robin.game.objects.*;
import com.robin.general.swing.IconGroup;
import com.robin.magic_realm.components.CardComponent;
import com.robin.magic_realm.components.QuestCardComponent;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.quest.requirement.*;
import com.robin.magic_realm.components.quest.reward.QuestReward.RewardType;
import com.robin.magic_realm.components.quest.rule.QuestRule;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.GameVariant;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.HostPrefWrapper;

public class Quest extends GameObjectWrapper {
	private static Logger logger = Logger.getLogger(Quest.class.getName());

	public static final String GAME_DATA_NAME = "QuestBuilder";

	public static final String QUEST_STEP = "quest_step";
	public static final String QUEST_LOCATION = "quest_location";
	public static final String QUEST_MINOR_CHARS = "quest_minor_chars";
	public static final String QUEST_COUNTER = "quest_counter";
	public static final String QUEST_ACTION = "quest_action";
	public static final String QUEST_REQUIREMENT = "quest_requirement";
	public static final String QUEST_REWARD = "quest_reward";

	public static final String QUEST_BLOCK = "qb";
	private static final String DESCRIPTION = "_d";
	public static final String STATE = "_q_state";
	public static final String QUEST_STICKY = "quest_sticky";
	private static final String LOST_INVENTORY_RULE = "_li";
	private static final String LOST_INVENTORY_LOCATION = "_lil";
	private static final String REQ_RULES = "_rr";
	private static final String JOURNAL_KEYS = "_jk";
	public static final String QUEST_OWNER = "_qo";
	public static final String QUEST_UNIQUE_ID = "unique_id";
	
	public static final String QUEST_FINISHED_STEP_COUNT = "_fsc";
	
	public static Quest currentQuest;

	private ArrayList steps;
	private ArrayList questRules;
	private ArrayList locations;
	private ArrayList minorCharacters;
	private ArrayList counters;
	
	public String filepath; // This is just here so that the builder can save a quest it just loaded for viewDeck() - not guaranteed!
	
	public Quest(GameObject go) {
		super(go);
		steps = new ArrayList();
		questRules = new ArrayList();
		locations = new ArrayList();
		minorCharacters = new ArrayList();
		counters = new ArrayList();
		

		for (java.util.Iterator _j14it2214 = (go.getHold()).iterator(); _j14it2214.hasNext(); ) {
		  GameObject held = (GameObject) _j14it2214.next();
			if (held.hasThisAttribute(Quest.QUEST_STEP)) {
				steps.add(new QuestStep(held));
			}
			else if (held.hasThisAttribute(Quest.QUEST_LOCATION)) {
				locations.add(new QuestLocation(held));
			}
			else if (held.hasThisAttribute(Quest.QUEST_MINOR_CHARS)) {
				minorCharacters.add(new QuestMinorCharacter(held));
			}
			else if (held.hasThisAttribute(Quest.QUEST_COUNTER)) {
				counters.add(new QuestCounter(held, held.getThisInt("count")));
			}
		}
		Collections.sort(steps, new Comparator() {
			public int compare(Object o1, Object o2) {
				QuestStep q1 = (QuestStep) o1;
				QuestStep q2 = (QuestStep) o2;
				return q1.getId() - q2.getId();
			}
		});
	}

	public void autoRepair() {
		// Repair broken steps
		for (java.util.Iterator _j14it2215 = (steps).iterator(); _j14it2215.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2215.next();
			ArrayList req = filterMissingSteps(step.getRequiredSteps());
			step.clearRequiredSteps();
			for (java.util.Iterator _j14it2216 = (req).iterator(); _j14it2216.hasNext(); ) {
			  QuestStep rs = (QuestStep) _j14it2216.next();
				step.addRequiredStep(rs);
			}
			ArrayList fail = filterMissingSteps(step.getFailSteps());
			step.clearFailSteps();
			for (java.util.Iterator _j14it2217 = (fail).iterator(); _j14it2217.hasNext(); ) {
			  QuestStep rs = (QuestStep) _j14it2217.next();
				step.addFailStep(rs);
			}
			ArrayList pre = filterMissingSteps(step.getPreemptedSteps());
			step.clearPreemptedSteps();
			for (java.util.Iterator _j14it2218 = (pre).iterator(); _j14it2218.hasNext(); ) {
			  QuestStep rs = (QuestStep) _j14it2218.next();
				step.addPreemptedStep(rs);
			}
		}

		// Remove unused objects (but only when quest is in a gamedata by itself)
		if (!getGameData().getGameName().equals(GAME_DATA_NAME))
			return;
		ArrayList toRemove = new ArrayList();
		for (java.util.Iterator _j14it2219 = (getGameData().getGameObjects()).iterator(); _j14it2219.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2219.next();
			if (go.getId() > 0 && go.getHeldBy() == null) { // Not the quest, and not held by anything... get rid of it!!
				toRemove.add(go);
			}
		}
		for (java.util.Iterator _j14it2220 = (toRemove).iterator(); _j14it2220.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2220.next();
			getGameData().removeObject(go);
		}
		
		updateActivatePossible();
	}
	
	public boolean isActivateable() {
		return getBoolean(QuestConstants.ACTIVATEABLE);
	}
	
	public void updateActivatePossible() {
		boolean foundActive = false;
		for (java.util.Iterator _j14it2221 = (getSteps()).iterator(); _j14it2221.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2221.next();
			for (java.util.Iterator _j14it2222 = (step.getRequirements()).iterator(); _j14it2222.hasNext(); ) {
			  QuestRequirement req = (QuestRequirement) _j14it2222.next();
				if (req instanceof QuestRequirementActive) {
					foundActive = true;
					break;
				}
			}
			if (foundActive) break;
		}
		setBoolean(QuestConstants.ACTIVATEABLE,foundActive);
	}

	private ArrayList filterMissingSteps(ArrayList ids) {
		ArrayList found = new ArrayList();
		if (ids != null) {
			for (java.util.Iterator _j14it2223 = (ids).iterator(); _j14it2223.hasNext(); ) {
			  String id = (String) _j14it2223.next();
				for (java.util.Iterator _j14it2224 = (steps).iterator(); _j14it2224.hasNext(); ) {
				  QuestStep step = (QuestStep) _j14it2224.next();
					if (step.getGameObject().getStringId().equals(id)) {
						found.add(step);
						break;
					}
				}
			}
		}
		return found;
	}

	public void setOwner(CharacterWrapper owner) {
		setString(QUEST_OWNER, owner.getGameObject().getStringId());
	}

	public CharacterWrapper getOwner() {
		String id = getString(QUEST_OWNER);
		if (id == null)
			return null; // Original template card will never have an owner id
		GameObject owner = getGameData().getGameObject(Long.valueOf(id));
		return new CharacterWrapper(owner);
	}

	private void removeFromOwner() {
		CharacterWrapper character = getOwner();
		if (character != null) {
			character.removeQuest(this);
		}
	}
	
	public void unassign() {
		removeFromOwner();
		clear(QUEST_OWNER);
	}

	public String toString() {
		return getName();
	}

	public boolean isAllPlay() {
		return getBoolean(QuestConstants.QTR_ALL_PLAY);
	}
	
	public String getGuild() {
		if (getGameObject().hasAttribute(getBlockName(),QuestConstants.FOR_FIGHTERS_GUILD)) {
			return "fighters";
		}
		if (getGameObject().hasAttribute(getBlockName(),QuestConstants.FOR_MAGIC_GUILD)) {
			return "magic";
		}
		if (getGameObject().hasAttribute(getBlockName(),QuestConstants.FOR_THIEVES_GUILD)) {
			return "thieves";
		}
		return null;
	}
	
	public void clearAllPlay() {
		clear(QuestConstants.QTR_ALL_PLAY);
	}
	
	public boolean isEvent() {
		return getBoolean(QuestConstants.BOQ_EVENT);
	}
	
	public void setEvent(boolean val) {
		setBoolean(QuestConstants.BOQ_EVENT, val);
	}
	
	public boolean isTesting() {
		return getBoolean(QuestConstants.FLAG_TESTING);
	}
	
	public boolean isBroken() {
		return getBoolean(QuestConstants.FLAG_BROKEN);
	}
	
	public boolean isSecretQuest() {
		return getBoolean(QuestConstants.QTR_SECRET_QUEST);
	}

	public boolean usesMinorCharacter(QuestMinorCharacter mc) {
		for (java.util.Iterator _j14it2225 = (steps).iterator(); _j14it2225.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2225.next();
			if (step.usesMinorCharacter(mc)) {
				return true;
			}
		}

		return false;
	}

	public boolean usesLocationTag(String tag) {
		String desc = getDescription();
		if (desc != null && desc.contains(tag)) {
			return true;
		}

		for (java.util.Iterator _j14it2226 = (steps).iterator(); _j14it2226.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2226.next();
			if (step.usesLocationTag(tag)) {
				return true;
			}
		}

		return false;
	}

	public ArrayList getLocationTags() {
		ArrayList list = new ArrayList();
		for (java.util.Iterator _j14it2227 = (getLocations()).iterator(); _j14it2227.hasNext(); ) {
		  QuestLocation loc = (QuestLocation) _j14it2227.next();
			list.add(loc.getTagName());
		}
		return list;
	}
	
	public boolean usesCounterTag(String tag) {
		String desc = getDescription();
		if (desc != null && desc.contains(tag)) {
			return true;
		}

		for (java.util.Iterator _j14it2228 = (steps).iterator(); _j14it2228.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2228.next();
			if (step.usesCounterTag(tag)) {
				return true;
			}
		}

		return false;
	}

	public ArrayList getCounterTags() {
		ArrayList list = new ArrayList();
		for (java.util.Iterator _j14it2229 = (getCounters()).iterator(); _j14it2229.hasNext(); ) {
		  QuestCounter counter = (QuestCounter) _j14it2229.next();
			list.add(counter.getTagName());
		}
		return list;
	}

	public ArrayList getLocations() {
		return locations;
	}
	
	public ArrayList getMinorCharacters() {
		return minorCharacters;
	}

	public ArrayList getCounters() {
		return counters;
	}

	public ArrayList getSteps() {
		return steps;
	}

	public ArrayList getQuestRules() {
		return questRules;
	}

	public void init() {
		getGameObject().setThisAttribute(Constants.QUEST);
	}

	public String getBlockName() {
		return QUEST_BLOCK;
	}

	public void setDescription(String description) {
		setString(DESCRIPTION, description);
	}

	public String getDescription() {
		return getString(DESCRIPTION);
	}

	public void clearStates() {
		clear(STATE);
		QuestState[] _vals2230 = QuestState.values();
		for (int _i2230 = 0; _i2230 < _vals2230.length; _i2230++) {
			QuestState state = _vals2230[_i2230];
			clear(state.toString());
		}
		for (java.util.Iterator _j14it2231 = (steps).iterator(); _j14it2231.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2231.next();
			step.clearStates();
		}
	}
	
	private void activateRequirements(CharacterWrapper character) {
		for (java.util.Iterator _j14it2232 = (steps).iterator(); _j14it2232.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2232.next();
			step.activateRequirements(character);
		}
	}

	public void setState(QuestState state, String dayKey, CharacterWrapper character) {
		setString(STATE, state.toString());
		setString(state.toString(), dayKey);
		if (state == QuestState.Active) {
			activateRequirements(character);
		}
		if (state == QuestState.Complete && !isMultipleUse()) revertAllPlay(dayKey,character);
	}
	
	public void revertAllPlay(String dayKey, CharacterWrapper character) {
		if (!isAllPlay()) return;
		HostPrefWrapper hostPrefs = HostPrefWrapper.findHostPrefs(getGameData());
		if (hostPrefs.hasPref(Constants.QST_QUEST_CARDS) || hostPrefs.hasPref(Constants.QST_SR_QUESTS)) {
			// Get rid of all the clones of this quest by removing from the owning character
			for (java.util.Iterator _j14it2233 = (findClones(getGameData().getGameObjects())).iterator(); _j14it2233.hasNext(); ) {
			  GameObject go = (GameObject) _j14it2233.next();
				Quest clone = new Quest(go);
				clone.setState(QuestState.Failed, dayKey, character);
				clone.removeFromOwner();
			}
		}
	}
	
	public int getUniqueId() {
		return getInt(Quest.QUEST_UNIQUE_ID);
	}

	public ArrayList findClones(ArrayList objects) {
		ArrayList list = new ArrayList();
		GamePool pool = new GamePool(objects);
		for (java.util.Iterator _j14it2234 = (pool.find(Quest.QUEST_UNIQUE_ID + "=" + getInt(Quest.QUEST_UNIQUE_ID))).iterator(); _j14it2234.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2234.next();
			if (go.equals(getGameObject()))
				continue; // not THIS quest of course!
			list.add(go);
		}
		return list;
	}

	public QuestState getState() {
		String val = getString(STATE);
		return val == null ? QuestState.New : QuestState.valueOf(val);
	}
	
	public void setSticky(boolean sticky) {
		setBoolean(QUEST_STICKY,sticky);
	}
	
	public boolean isSticky() {
		return getBoolean(QUEST_STICKY);
	}

	/**
	 * Happens in-game ONLY (never during quest builder). Defines the rule to be
	 * applied when the character loses inventory.
	 */
	public void setLostInventoryRule(RewardType rule) {
		if (rule != RewardType.LostInventoryToLocation && rule != RewardType.LostInventoryToDefault) {
			throw new IllegalArgumentException("Illegal lost inventory rule!");
		}
		setString(LOST_INVENTORY_RULE, rule.toString());
	}

	public RewardType getLostInventoryRule() {
		String val = getString(LOST_INVENTORY_RULE);
		if (val == null)
			return RewardType.LostInventoryToDefault;
		return RewardType.valueOf(val);
	}

	/**
	 * Happens in-game ONLY (never during quest builder). Defines the location
	 * to be used when the character loses inventory.
	 */
	public void setLostInventoryLocation(QuestLocation location) {
		setString(LOST_INVENTORY_LOCATION, location.getGameObject().getStringId());
	}

	public QuestLocation getLostInventoryLocation() {
		String id = getString(LOST_INVENTORY_LOCATION);
		if (id != null) {
			GameObject go = getGameData().getGameObject(Long.valueOf(id));
			if (go != null) {
				return new QuestLocation(go);
			}
		}
		return null;
	}

	public void addRequiredRuleKey(String ruleKey) {
		addListItem(REQ_RULES, ruleKey);
	}

	public void clearRequiredRuleKeys() {
		clear(REQ_RULES);
	}

	public ArrayList getRequiredRuleKeys() {
		return getList(REQ_RULES);
	}

	public QuestLocation createQuestLocation() {
		QuestLocation ql = new QuestLocation(getGameData().createNewObject());
		ql.init();
		int num = 1;
		while (true) {
			String testName = "Location" + (num++);
			for (java.util.Iterator _j14it2235 = (locations).iterator(); _j14it2235.hasNext(); ) {
			  QuestLocation test = (QuestLocation) _j14it2235.next();
				if (test.getName().equals(testName)) {
					testName = null;
					break;
				}
			}
			if (testName != null) {
				ql.setName(testName);
				break;
			}
		}
		ql.setLocationType(LocationType.Any);
		getGameObject().add(ql.getGameObject());
		locations.add(ql);
		return ql;
	}
	
	public void deleteQuestLocation(QuestLocation location) {
		locations.remove(location);
		location.getGameObject().delete();
	}
	
	public QuestMinorCharacter createMinorCharacter() {
		QuestMinorCharacter mc = new QuestMinorCharacter(getGameData().createNewObject());
		mc.init();
		int num = 1;
		while (true) {
			String testName = "MinorChar" + (num++);
			for (java.util.Iterator _j14it2236 = (minorCharacters).iterator(); _j14it2236.hasNext(); ) {
			  QuestMinorCharacter test = (QuestMinorCharacter) _j14it2236.next();
				if (test.getName().equals(testName)) {
					testName = null;
					break;
				}
			}
			if (testName != null) {
				mc.setName(testName);
				break;
			}
		}
		mc.getGameObject().setThisAttribute(Constants.ICON_FOLDER, "traveler");
		mc.getGameObject().setThisAttribute(Constants.ICON_TYPE, "t1"); // TODO Make this configurable

		getGameObject().add(mc.getGameObject());
		minorCharacters.add(mc);
		return mc;
	}

	public void deleteMinorCharacter(QuestMinorCharacter mc) {
		minorCharacters.remove(mc);
		mc.getGameObject().delete();
	}
	
	public QuestCounter createQuestCounter() {
		QuestCounter qc = new QuestCounter(getGameData().createNewObject());
		qc.init();
		int num = 1;
		while (true) {
			String testName = "Counter" + (num++);
			for (java.util.Iterator _j14it2237 = (counters).iterator(); _j14it2237.hasNext(); ) {
			  QuestCounter test = (QuestCounter) _j14it2237.next();
				if (test.getName().equals(testName)) {
					testName = null;
					break;
				}
			}
			if (testName != null) {
				qc.setName(testName);
				break;
			}
		}
		getGameObject().add(qc.getGameObject());
		counters.add(qc);
		return qc;
	}

	public void deleteQuestCounter(QuestCounter counter) {
		counters.remove(counter);
		counter.getGameObject().delete();
	}
	
	public QuestStep createQuestStep(boolean autoConnect) {
		QuestStep step = new QuestStep(getGameData().createNewObject());
		step.init();
		getGameObject().add(step.getGameObject());
		if (autoConnect) {
			QuestStep previousStep = steps.size() > 0 ? (QuestStep) steps.get(steps.size() - 1) : null;
			if (previousStep != null) {
				step.addRequiredStep(previousStep);
			}
		}
		steps.add(step);
		step.setId(steps.size());
		step.setName("Quest Step " + step.getId());
		return step;
	}
	
	public void deleteStepAt(int index) {
		QuestStep step = (QuestStep) steps.get(index);
		deleteQuestStep(step);
		renumberSteps();
	}

	public void moveStep(int index, int direction) {
		int newIndex = index + direction;
		if (newIndex < 0 || newIndex >= steps.size())
			return;
		QuestStep moving = (QuestStep) steps.remove(index);
		if (newIndex == steps.size()) {
			steps.add(moving);
		}
		else {
			steps.add(newIndex, moving);
		}
		renumberSteps();
	}

	private void renumberSteps() {
		int n = 1;
		for (java.util.Iterator _j14it2238 = (steps).iterator(); _j14it2238.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2238.next();
			step.setId(n++);
		}
	}

	private void deleteQuestStep(QuestStep step) {
		steps.remove(step);
		step.getGameObject().delete();
	}

	public boolean isValid() {
		return steps.size() > 0;
	}

	public boolean canChooseQuest(CharacterWrapper character, HostPrefWrapper hostPrefs) {
		if (!verifyCharacterForQuest(character)) return false;
		if (!verifyBoardSize(hostPrefs)) return false;
		if (!verifyGameVariant(hostPrefs)) return false;
		
		return true;
	}
	private boolean verifyCharacterForQuest(CharacterWrapper character) {
		// Specific name
		String specificChar = getString(QuestConstants.CHARACTER_SPEC_REGEX);
		if (specificChar!=null && specificChar.trim().length()>0) {
			Pattern pattern = Pattern.compile(specificChar);
			if (!pattern.matcher(character.getName()).find()) return false;
		}
		
		// Gender
		String pronoun = character.getGameObject().getThisAttribute("pronoun");
		boolean male = "he".equals(pronoun);
		boolean forMale = getBoolean(QuestConstants.CHARACTER_MALE);
		boolean forFemale = getBoolean(QuestConstants.CHARACTER_FEMALE);
		boolean forBothGender = (forMale && forFemale) || (!forMale && !forFemale);
		if (!forBothGender && ((!forMale && male) || (!forFemale && !male))) return false;
		
		// Class
		boolean fighter = character.getGameObject().hasThisAttribute("fighter");
		boolean forFighter = getBoolean(QuestConstants.CHARACTER_FIGHTER);
		boolean forMagic = getBoolean(QuestConstants.CHARACTER_MAGIC);
		boolean forBothClass = (forFighter && forMagic) || (!forFighter && !forMagic);
		if (!forBothClass && ((!forFighter && fighter) || (!forMagic && !fighter))) return false;
		
		return true;
	}
	
	private boolean verifyBoardSize(HostPrefWrapper hostPrefs) {
		boolean forSingle = getBoolean(QuestConstants.SINGLE_BOARD);
		boolean forDouble = getBoolean(QuestConstants.DOUBLE_BOARD);
		boolean forTriple = getBoolean(QuestConstants.TRIPLE_BOARD);
		boolean forAny = (forSingle && forDouble && forTriple) || (!forSingle && !forDouble && !forTriple);
		if (forAny) return true;
		
		int num = hostPrefs.getMultiBoardCount();
		if ((num==1 && !forSingle) || (num==2 && !forDouble) || (num==3 && !forTriple)) return false;
		
		return true;
	}
	
	public boolean verifyGameVariant(HostPrefWrapper hostPrefs) {
		boolean forOriginal = getBoolean(QuestConstants.VARIANT_ORIGINAL);
		boolean forPruitts = getBoolean(QuestConstants.VARIANT_PRUITTS);
		boolean forExpansion = getBoolean(QuestConstants.VARIANT_EXP1);
		boolean forSuperRealm = getBoolean(QuestConstants.VARIANT_SUPER_REALM);
		boolean forAny = (forOriginal && forPruitts && forExpansion && forSuperRealm) || (!forOriginal && !forPruitts && !forExpansion && !forSuperRealm);
		if (forAny) return true;
		
		String keyVals = hostPrefs.getGameKeyVals();
		boolean original = GameVariant.ORIGINAL_GAME_VARIANT.getKeyVals().equals(keyVals);
		boolean pruitts = GameVariant.PRUITTS_GAME_VARIANT.getKeyVals().equals(keyVals);
		boolean expansion = GameVariant.EXP1_GAME_VARIANT.getKeyVals().equals(keyVals);
		boolean superRealm = GameVariant.SUPER_REALM.getKeyVals().equals(keyVals);
		if ((!forOriginal && original) || (!forPruitts && pruitts) || (!forExpansion && expansion) || (!forSuperRealm && superRealm)) return false;
		
		return true;
	}

	/**
	 * Initializes the quest for use by a character, with resetting the locations.
	 */
	public void initialize(JFrame parentFrame, CharacterWrapper character) {
		initialize(parentFrame, character, true);
	}
	
	/**
	 * Initializes the quest for use by a character.
	 */
	public void initialize(JFrame parentFrame, CharacterWrapper character, boolean resetLocation) {
		setOwner(character);
		String dayKey = character.getCurrentDayKey();
		for (java.util.Iterator _j14it2239 = (steps).iterator(); _j14it2239.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2239.next();
			step.setState(QuestStepState.Pending, dayKey);
		}
		HostPrefWrapper hostPrefs = HostPrefWrapper.findHostPrefs(getGameData());
		if (hostPrefs.hasPref(Constants.QST_SR_QUESTS)) {
			setState(QuestState.Active,character.getCurrentDayKey(), character);
		}
		updateStepStates(dayKey);
		if (resetLocation) {
			for (java.util.Iterator _j14it2240 = (getLocations()).iterator(); _j14it2240.hasNext(); ) {
			  QuestLocation location = (QuestLocation) _j14it2240.next();
				location.resolveQuestStart(parentFrame, character);
			}
		}
	}

	/**
	 * Removes all previous quest information, so that it can be used again.
	 */
	public void reset() {
		clear(QUEST_OWNER);
		clearJournalEntries();
		clearStates();
		// How to handle MinorCharacters?  Might they be with the character?  Maybe I can prevent minor characters from moving to character until quest is active (which can't then be discarded?)
	}

	public void updateStepStates(String dayKey) {
		Hashtable lookup = new Hashtable();
		for (java.util.Iterator _j14it2241 = (steps).iterator(); _j14it2241.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2241.next();
			lookup.put(step.getGameObject().getStringId(), step);
		}
		for (java.util.Iterator _j14it2242 = (steps).iterator(); _j14it2242.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2242.next();
			if (step.getState() != QuestStepState.Pending)
				continue; // only check pending steps!!

			QuestStepType stepType = step.getLogicType();

			ArrayList requiredSteps = step.getRequiredSteps();
			boolean markAsReady;
			if (stepType == QuestStepType.And) {
				// For AND, assume true, and mark false if any unfinished steps are found
				markAsReady = true;
			}
			else {
				// For OR, assume false if any required steps, and mark true if any finished steps are found
				markAsReady = requiredSteps == null || requiredSteps.isEmpty();
			}
			ArrayList failSteps = step.getFailSteps();
			if (requiredSteps != null) {
				for (java.util.Iterator _j14it2243 = (requiredSteps).iterator(); _j14it2243.hasNext(); ) {
				  String reqId = (String) _j14it2243.next();
					QuestStep requiredStep = (QuestStep) lookup.get(reqId);
					if (requiredStep.getState() == QuestStepState.Finished) {
						if (stepType == QuestStepType.Or) {
							markAsReady = true;
							break;
						}
					}
					else {
						if (stepType == QuestStepType.And) {
							markAsReady = false;
							break;
						}
					}
				}
			}
			if (failSteps!=null && failSteps.size()>0) markAsReady=false; 
			if (markAsReady) {
				step.setState(QuestStepState.Ready, dayKey);
			}
		}
	}

	public void addJournalEntry(String journalKey, QuestStepState entryType, String text) {
		if (!hasListItem(JOURNAL_KEYS, journalKey)) {
			addListItem(JOURNAL_KEYS, journalKey);
		}
		String blockKey = JOURNAL_KEYS + journalKey;
		getGameObject().setAttribute(blockKey, "entryType", entryType.toString());
		getGameObject().setAttribute(blockKey, "text", text);
	}

	private void clearJournalEntries() {
		ArrayList list = getList(JOURNAL_KEYS);
		if (list != null) {
			for (java.util.Iterator _j14it2244 = (list).iterator(); _j14it2244.hasNext(); ) {
			  String i = (String) _j14it2244.next();
				String blockKey = JOURNAL_KEYS + i;
				getGameObject().removeAttributeBlock(blockKey);
			}
			clear(JOURNAL_KEYS);
		}
	}

	public ArrayList getJournalEntries() {
		ArrayList entries = new ArrayList();
		ArrayList list = getList(JOURNAL_KEYS);
		if (list != null) {
			for (java.util.Iterator _j14it2245 = (list).iterator(); _j14it2245.hasNext(); ) {
			  String i = (String) _j14it2245.next();
				String blockKey = JOURNAL_KEYS + i;
				QuestStepState entryType = QuestStepState.valueOf(getGameObject().getAttribute(blockKey, "entryType"));
				String text = getGameObject().getAttribute(blockKey, "text");
				entries.add(new QuestJournalEntry(entryType, text));
			}
		}
		return entries;
	}

	/**
	 * @return true when rewards are given
	 */
	public boolean testRequirements(JFrame parentFrame, CharacterWrapper character, QuestRequirementParams reqParams) {
		QuestState state = getState();

		boolean canTest = state == QuestState.Active || ((isAllPlay() || isEvent() || isSecretQuest()) && state == QuestState.Assigned);
		if (!canTest) {
			return false;
		}

		if (reqParams.dayKey == null) {
			reqParams.dayKey = character.getCurrentDayKey();
		}
		boolean rewards = false;
		for (java.util.Iterator _j14it2246 = (steps).iterator(); _j14it2246.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2246.next();
			QuestStepState stepState = step.getState();
			if (stepState == QuestStepState.Ready) {
				logger.fine("TESTING " + getGameObject().getName() + " step " + step.getId() + ": " + step.getGameObject().getName());
				if (step.fulfillsRequirements(parentFrame, character, reqParams)) {
					logger.fine("SUCCESS");
					logger.fine("");
					step.preemptSteps(steps, reqParams.dayKey);
					step.setState(QuestStepState.Finished, reqParams.dayKey);
					Quest.currentQuest = this; // I don't like this hack... but it makes it easy to do the special QuestRewardChooseNextStep
					QuestStep.currentStep = step;
					step.doRewards(parentFrame, character);
					Quest.currentQuest = null;
					QuestStep.currentStep = null;
					rewards = true;
				}
				else {
					// Mark any steps dependent on fail as READY
					for (java.util.Iterator _j14it2247 = (step.findPendingFailTriggeredSteps(steps)).iterator(); _j14it2247.hasNext(); ) {
					  QuestStep ft = (QuestStep) _j14it2247.next();
						ft.setState(QuestStepState.Ready,reqParams.dayKey);
					}
					
					logger.fine("FAIL");
					logger.fine("");
				}
			}
		}
		if (rewards) {
			updateStepStates(reqParams.dayKey);
			if (reqParams != null) {
				reqParams = reqParams.copy(getGameData());
				reqParams.clearTables();
			}
			testRequirements(parentFrame, character, reqParams); // yes, recursive...  Notice that tableName and dieResult are not passed on... wouldn't want to satisfy multiple quest steps at once!!
			return true;
		}
		return false;
	}

	public Quest copyQuestToGameData(GameData gameData) {
		// Duplicate all the objects in the quest
		ArrayList allQuestObjects = getGameObject().getAllContainedGameObjects();
		Hashtable lookup = new Hashtable();
		for (java.util.Iterator _j14it2248 = (allQuestObjects).iterator(); _j14it2248.hasNext(); ) {
		  GameObject questGo = (GameObject) _j14it2248.next();
			GameObject go = gameData.createNewObject(questGo);
			lookup.put(Long.valueOf(questGo.getId()), go);
		}

		// Now make sure the holds are setup correctly
		for (java.util.Iterator _j14it2249 = (allQuestObjects).iterator(); _j14it2249.hasNext(); ) {
		  GameObject questGo = (GameObject) _j14it2249.next();
			GameObject go = (GameObject) lookup.get(Long.valueOf(questGo.getId()));
			for (java.util.Iterator _j14it2250 = (questGo.getHold()).iterator(); _j14it2250.hasNext(); ) {
			  Object obj = (Object) _j14it2250.next();
				GameObject held = (GameObject) obj;
				go.add((GameObject) lookup.get(Long.valueOf(held.getId())));
			}
		}
		Quest quest = new Quest((GameObject) lookup.get(Long.valueOf(getGameObject().getId())));
		for (java.util.Iterator _j14it2251 = (quest.getSteps()).iterator(); _j14it2251.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2251.next();
			step.updateIds(lookup);
		}
		return quest;
	}

	public static void showQuestMessage(JFrame frame,Quest quest, String message, String title) {
		showQuestMessage(frame,quest,message,title,null);
	}
	public static void showQuestMessage(JFrame frame,Quest quest, String message, String title,RealmComponent rc) {
		JTextArea area = new JTextArea();
		area.setFont(Constants.RESULT_FONT);
		area.setText(message);
		area.setWrapStyleWord(true);
		area.setLineWrap(true);
		area.setEditable(false);
		area.setOpaque(false);
		area.setSize(600, CardComponent.CARD_HEIGHT);
		QuestCardComponent card = (QuestCardComponent)RealmComponent.getRealmComponent(quest.getGameObject());
		ImageIcon icon = null;
		if (rc==null) {
			icon = card.getFaceUpIcon();
		}
		else {
			IconGroup group = new IconGroup(IconGroup.HORIZONTAL,5);
			group.addIcon(card.getFaceUpIcon());
			group.addIcon(rc.getFaceUpIcon());
			icon = group;
		}
		JOptionPane.showMessageDialog(frame, area, title, JOptionPane.PLAIN_MESSAGE, icon);
	}
	public boolean isMultipleUse() {
		return getBoolean(QuestConstants.QUEST_MULTIPLE_USE);
	}
	public void setMultipleUse(boolean val) {
		setBoolean(QuestConstants.QUEST_MULTIPLE_USE,val);
	}
}