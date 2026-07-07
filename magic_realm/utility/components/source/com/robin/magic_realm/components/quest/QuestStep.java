package com.robin.magic_realm.components.quest;

import java.util.*;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.game.objects.GameObjectWrapper;
import com.robin.general.swing.ButtonOptionDialog;
import com.robin.general.swing.IconGroup;
import com.robin.general.util.HashLists;
import com.robin.magic_realm.components.quest.requirement.*;
import com.robin.magic_realm.components.quest.requirement.QuestRequirement.RequirementType;
import com.robin.magic_realm.components.quest.reward.QuestReward;
import com.robin.magic_realm.components.quest.reward.QuestReward.RewardType;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.DayKey;

public class QuestStep extends GameObjectWrapper {
	private static Logger logger = Logger.getLogger(QuestStep.class.getName());

	private static final String ID = "_id";
	private static final String STATE = "_state";
	private static final String LOGIC_TYPE = "_type";
	private static final String DESCRIPTION = "_d";
	private static final String REQ_STEPS = "_rs";
	private static final String PREEMPT_STEPS = "_ps";
	private static final String FAIL_STEPS = "_fs";
	private static final String REQ_TYPE = "_rtype";
	
	public static QuestStep currentStep;

	private ArrayList requirements;
	private ArrayList rewards;

	public QuestStep(GameObject go) {
		super(go);
		refresh();
	}

	public String toString() {
		return "Step " + getId();
	}

	public void refresh() {
		requirements = new ArrayList();
		rewards = new ArrayList();
		for (java.util.Iterator _j14it2266 = (getGameObject().getHold()).iterator(); _j14it2266.hasNext(); ) {
		  GameObject held = (GameObject) _j14it2266.next();
			if (held.hasThisAttribute(Quest.QUEST_REQUIREMENT)) {
				String val = held.getThisAttribute(Quest.QUEST_REQUIREMENT);
				if (val.equals("Location"))
					val = "OccupyLocation";
				RequirementType type = RequirementType.valueOf(val);
				requirements.add(QuestRequirement.getRequirement(type, held));
			}
			else if (held.hasThisAttribute(Quest.QUEST_REWARD)) {
				RewardType type = RewardType.valueOf(held.getThisAttribute(Quest.QUEST_REWARD));
				rewards.add(QuestReward.getReward(type, held));
			}
		}
	}

	public void init() {
		getGameObject().setThisAttribute(Quest.QUEST_STEP);
	}
	
	public String getBlockName() {
		return Quest.QUEST_BLOCK;
	}

	public boolean usesLocationTag(String tag) {
		String desc = getDescription();
		if (desc != null && desc.contains(tag)) {
			return true;
		}
		for (java.util.Iterator _j14it2267 = (requirements).iterator(); _j14it2267.hasNext(); ) {
		  QuestRequirement req = (QuestRequirement) _j14it2267.next();
			if (req.usesLocationTag(tag))
				return true;
		}
		for (java.util.Iterator _j14it2268 = (rewards).iterator(); _j14it2268.hasNext(); ) {
		  QuestReward reward = (QuestReward) _j14it2268.next();
			if (reward.usesLocationTag(tag))
				return true;
		}
		return false;
	}
	
	public boolean usesMinorCharacter(QuestMinorCharacter mc) {
		for (java.util.Iterator _j14it2269 = (requirements).iterator(); _j14it2269.hasNext(); ) {
		  QuestRequirement req = (QuestRequirement) _j14it2269.next();
			if (req.usesMinorCharacter(mc))
				return true;
		}
		for (java.util.Iterator _j14it2270 = (rewards).iterator(); _j14it2270.hasNext(); ) {
		  QuestReward reward = (QuestReward) _j14it2270.next();
			if (reward.usesMinorCharacter(mc))
				return true;
		}
		return false;
	}
	
	public boolean usesCounterTag(String tag) {
		String desc = getDescription();
		if (desc != null && desc.contains(tag)) {
			return true;
		}
		for (java.util.Iterator _j14it2271 = (requirements).iterator(); _j14it2271.hasNext(); ) {
		  QuestRequirement req = (QuestRequirement) _j14it2271.next();
			if (req.usesCounterTag(tag))
				return true;
		}
		for (java.util.Iterator _j14it2272 = (rewards).iterator(); _j14it2272.hasNext(); ) {
		  QuestReward reward = (QuestReward) _j14it2272.next();
			if (reward.usesCounterTag(tag))
				return true;
		}
		return false;
	}

	public void setId(int id) {
		setInt(ID, id);
	}

	public int getId() {
		return getInt(ID);
	}

	public void clearStates() {
		clear(STATE);
		QuestStepState[] _vals2273 = QuestStepState.values();
		for (int _i2273 = 0; _i2273 < _vals2273.length; _i2273++) {
			QuestStepState state = _vals2273[_i2273];
			clear(state.toString());
		}
		GameObject questObject = getGameObject().getHeldBy();
		questObject.removeThisAttribute(Quest.QUEST_FINISHED_STEP_COUNT);
	}
	
	public void activateRequirements(CharacterWrapper character) {
		for (java.util.Iterator _j14it2274 = (requirements).iterator(); _j14it2274.hasNext(); ) {
		  QuestRequirement requirement = (QuestRequirement) _j14it2274.next();
			requirement.activate(character);
		}
	}

	public void setState(QuestStepState state, String dayKey) {
		setString(STATE, state.toString());
		setString(state.toString(), dayKey);
		if (requirements.isEmpty()) return; // no requirements, then nothing significant to note
		if (state==QuestStepState.Finished) {// || state==QuestStepState.Failed) {
			String key = Quest.QUEST_FINISHED_STEP_COUNT;
			GameObject questObject = getGameObject().getHeldBy();
			questObject.setThisAttribute(key,questObject.getThisInt(key)+1);
		}
	}

	public QuestStepState getState() {
		String val = getString(STATE);
		return val == null ? QuestStepState.None : QuestStepState.valueOf(val);
	}

	public void setLogicType(QuestStepType type) {
		setString(LOGIC_TYPE, type.toString());
	}

	public QuestStepType getLogicType() {
		String val = getString(LOGIC_TYPE);
		return val == null ? QuestStepType.And : QuestStepType.valueOf(val);
	}

	public void setReqType(QuestStepType type) {
		setString(REQ_TYPE, type.toString());
	}

	public QuestStepType getReqType() {
		String val = getString(REQ_TYPE);
		return val == null ? QuestStepType.And : QuestStepType.valueOf(val);
	}

	public DayKey getQuestStartTime() {
		return getStateTime(QuestStepState.Pending);
	}

	public DayKey getQuestStepStartTime() {
		return getStateTime(QuestStepState.Ready);
	}

	public DayKey getQuestStepFinishTime() {
		return getStateTime(QuestStepState.Finished);
	}

	private DayKey getStateTime(QuestStepState state) {
		String dayKey = getString(state.toString());
		return dayKey == null ? null : new DayKey(dayKey);
	}

	public void setDescription(String description) {
		setString(DESCRIPTION, description);
	}

	public String getDescription() {
		return getString(DESCRIPTION);
	}

	public void addRequiredStep(QuestStep step) {
		addListItem(REQ_STEPS, step.getGameObject().getStringId());
	}

	public void removeRequiredStep(QuestStep step) {
		removeListItem(REQ_STEPS, step.getGameObject().getStringId());
	}

	public void clearRequiredSteps() {
		clear(REQ_STEPS);
	}

	public ArrayList getRequiredSteps() {
		return getList(REQ_STEPS);
	}

	public boolean isOrigin() {
		ArrayList requiredSteps = getRequiredSteps();
		ArrayList failSteps = getFailSteps();
		return (requiredSteps == null || requiredSteps.size() == 0) && (failSteps == null || failSteps.size() == 0);
	}

	public boolean requires(QuestStep step) {
		ArrayList requiredSteps = getRequiredSteps();
		return requiredSteps != null && requiredSteps.contains(step.getGameObject().getStringId());
	}
	
	public boolean requiresFail(QuestStep step) {
		ArrayList failSteps = getFailSteps();
		return failSteps != null && failSteps.contains(step.getGameObject().getStringId());
	}

	public void addPreemptedStep(QuestStep step) {
		addListItem(PREEMPT_STEPS, step.getGameObject().getStringId());
	}

	public void removePreemptedStep(QuestStep step) {
		removeListItem(PREEMPT_STEPS, step.getGameObject().getStringId());
	}

	public void clearPreemptedSteps() {
		clear(PREEMPT_STEPS);
	}

	public ArrayList getPreemptedSteps() {
		return getList(PREEMPT_STEPS);
	}

	public void addFailStep(QuestStep step) {
		addListItem(FAIL_STEPS, step.getGameObject().getStringId());
	}

	public void removeFailStep(QuestStep step) {
		removeListItem(FAIL_STEPS, step.getGameObject().getStringId());
	}

	public void clearFailSteps() {
		clear(FAIL_STEPS);
	}

	public ArrayList getFailSteps() {
		return getList(FAIL_STEPS);
	}

	public ArrayList getRequirements() {
		return requirements;
	}

	public QuestRequirement createRequirement(RequirementType type) {
		QuestRequirement requirement = QuestRequirement.getRequirement(type, getGameData().createNewObject());
		requirement.init();
		getGameObject().add(requirement.getGameObject());
		requirements.add(requirement);
		return requirement;
	}

	public void deleteRequirement(QuestRequirement requirement) {
		requirements.remove(requirement);
		requirement.getGameObject().delete();
	}

	public ArrayList getRewards() {
		return rewards;
	}

	public QuestReward createReward(RewardType type) {
		QuestReward reward = QuestReward.getReward(type, getGameData().createNewObject());
		reward.init();
		getGameObject().add(reward.getGameObject());
		rewards.add(reward);
		return reward;
	}

	public void deleteReward(QuestReward reward) {
		rewards.remove(reward);
		reward.getGameObject().delete();
	}

	public void updateIds(Hashtable lookup) {
		updateIds(lookup, REQ_STEPS);
		updateIds(lookup, FAIL_STEPS);
		updateIds(lookup, PREEMPT_STEPS);
		for (java.util.Iterator _j14it2275 = (getRequirements()).iterator(); _j14it2275.hasNext(); ) {
		  QuestRequirement req = (QuestRequirement) _j14it2275.next();
			req.updateIds(lookup);
		}
		for (java.util.Iterator _j14it2276 = (getRewards()).iterator(); _j14it2276.hasNext(); ) {
		  QuestReward rew = (QuestReward) _j14it2276.next();
			rew.updateIds(lookup);
		}
	}

	private void updateIds(Hashtable lookup, String key) {
		ArrayList oldIds = getList(key);
		if (oldIds == null || oldIds.size() == 0)
			return;
		clear(key);
		for (java.util.Iterator _j14it2277 = (oldIds).iterator(); _j14it2277.hasNext(); ) {
		  Object obj = (Object) _j14it2277.next();
			String stringId = (String) obj;
			Long oldId = Long.valueOf(stringId);
			if (!lookup.containsKey(oldId)) {
				throw new IllegalStateException("cannot find conversion for id " + oldId + " in quest step " + getId() + " of quest " + getGameObject().getHeldBy());
			}
			addListItem(key, ((GameObject) lookup.get(oldId)).getStringId());
		}
	}

	private static String getClassName(Class c) {
		String className = c.getName();
		int firstChar;
		firstChar = className.lastIndexOf('.') + 1;
		if (firstChar > 0) {
			className = className.substring(firstChar);
		}
		return className;
	}

	public boolean fulfillsRequirements(JFrame frame, CharacterWrapper character, QuestRequirementParams reqParams) {
		ArrayList reqs = getRequirements();
		if (reqs.isEmpty()) return true; // no requirements means auto-success
		QuestStepType type = getReqType();
		if (type == QuestStepType.And) {
			boolean ret = true;
			for (java.util.Iterator _j14it2278 = (reqs).iterator(); _j14it2278.hasNext(); ) {
			  QuestRequirement req = (QuestRequirement) _j14it2278.next();
				if (req instanceof QuestRequirementNextPhase && ret == false) continue;
				if (!req.usesAutoJournal() && ret == false) continue;
				
				if (req.fulfillsRequirement(frame, character, reqParams)) {
					logger.fine(getClassName(req.getClass()) + " SUCCESS");
				}
				else {
					ret = false;
					// we continue the loop here in case there are any "Auto-Journal" requirements that need updates
				}
			}
			return ret;
		}
		// OR - any requirement fulfilled is a success
		boolean oneFulfilled = false;
		for (java.util.Iterator _j14it2279 = (reqs).iterator(); _j14it2279.hasNext(); ) {
		  QuestRequirement req = (QuestRequirement) _j14it2279.next();
			if (oneFulfilled && !(req instanceof QuestRequirementLocationExists) && !(req instanceof QuestRequirementLocation)) continue;
			if (req.fulfillsRequirement(frame, character, reqParams)) {
				logger.fine("Requirement " + req.getName() + " fulfilled.");
				oneFulfilled = true;
				// we continue here in case there are any "Lock" type requirements!
			}
		}
		return oneFulfilled;
	}

	public void preemptSteps(ArrayList steps, String dayKey) {
		ArrayList preempt = getPreemptedSteps();
		if (preempt == null)
			return;
		Hashtable lookup = new Hashtable();
		for (java.util.Iterator _j14it2280 = (steps).iterator(); _j14it2280.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2280.next();
			lookup.put(step.getGameObject().getStringId(), step);
		}
		for (java.util.Iterator _j14it2281 = (preempt).iterator(); _j14it2281.hasNext(); ) {
		  String reqId = (String) _j14it2281.next();
			QuestStep preemptedStep = (QuestStep) lookup.get(reqId);
			preemptedStep.setState(QuestStepState.Failed, dayKey);
		}
	}

	public void doRewards(JFrame frame, CharacterWrapper character) {
		HashLists rewardGroups = new HashLists();
		for (java.util.Iterator _j14it2282 = (getRewards()).iterator(); _j14it2282.hasNext(); ) {
		  QuestReward reward = (QuestReward) _j14it2282.next();
			rewardGroups.put(reward.getRewardGroup(), reward);
		}
		if (rewardGroups.size() == 0)
			return; // No rewards. Awww, how sad.
		
		// First, complete all the "ALL REWARD" rewards.
		ArrayList allRewards = rewardGroups.getList(QuestReward.ALL_REWARD_GROUP);
		if (allRewards!=null) {
			for (java.util.Iterator _j14it2283 = (allRewards).iterator(); _j14it2283.hasNext(); ) {
			  QuestReward reward = (QuestReward) _j14it2283.next();
				reward.processReward(frame,character);
			}
		}
		
		// If there are any group choice awards, get a choice (if needed) and proceed.
		String selectedRewardGroup = chooseReward(frame, rewardGroups);
		ArrayList list = new ArrayList();
		if (selectedRewardGroup != QuestReward.ALL_REWARD_GROUP) {
			list.addAll(rewardGroups.getList(selectedRewardGroup));
		}
		for (java.util.Iterator _j14it2284 = (list).iterator(); _j14it2284.hasNext(); ) {
		  QuestReward reward = (QuestReward) _j14it2284.next();
			reward.processReward(frame, character);
		}
	}

	private static String chooseReward(JFrame frame, HashLists rewardGroups) {
		// First see if the choice is obvious
		ArrayList keys = new ArrayList(rewardGroups.keySet());
		keys.remove(QuestReward.ALL_REWARD_GROUP);
		if (keys.size() == 1)
			return (String) keys.get(0);
		if (keys.size() == 0)
			return QuestReward.ALL_REWARD_GROUP;

		// Nope! Better ask the player then
		int columns = (int) Math.ceil(rewardGroups.size()/5.0);
		ButtonOptionDialog chooser = new ButtonOptionDialog(frame, null, "Select a reward group:", "Reward Chooser", false, columns);
		keys = new ArrayList(rewardGroups.keySet());
		Collections.sort(keys);
		Hashtable reverseLookup = new Hashtable();
		for (java.util.Iterator _j14it2285 = (keys).iterator(); _j14it2285.hasNext(); ) {
		  String key = (String) _j14it2285.next();
			if (QuestReward.ALL_REWARD_GROUP.equals(key))
				continue;
			ArrayList rewards = new ArrayList();
			rewards.addAll(rewardGroups.getList(key));
			StringBuffer sb = new StringBuffer();
			IconGroup icon = null;
			for (java.util.Iterator _j14it2286 = (rewards).iterator(); _j14it2286.hasNext(); ) {
			  QuestReward reward = (QuestReward) _j14it2286.next();
				if (!reward.getRewardType().isShown())
					continue;
				if (sb.length() > 0) {
					sb.append("\n");
				}
				sb.append(reward.getDescription());
				ImageIcon rewardIcon = reward.getIcon();
				if (rewardIcon!=null) {
					if (icon==null) icon = new IconGroup(IconGroup.HORIZONTAL,0);
					icon.addIcon(rewardIcon);
				}
			}
			String object = sb.toString();
			chooser.addSelectionObject(object);
			if (icon!=null) chooser.setSelectionObjectIcon(object,icon);
			reverseLookup.put(object, key);
		}
		chooser.setVisible(true);
		String selectedString = (String) chooser.getSelectedObject();
		return (String) reverseLookup.get(selectedString);
	}
	/**
	 * Find all the pending steps that are dependent on this step's failure
	 */
	public ArrayList findPendingFailTriggeredSteps(ArrayList steps) {
		String id = getGameObject().getStringId();
		ArrayList ret = new ArrayList();
		for (java.util.Iterator _j14it2287 = (steps).iterator(); _j14it2287.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2287.next();
			if (step.getState()!=QuestStepState.Pending) continue;
			ArrayList list = step.getFailSteps();
			if (list!=null && list.contains(id)) ret.add(step);
		}
		return ret;
	}
}