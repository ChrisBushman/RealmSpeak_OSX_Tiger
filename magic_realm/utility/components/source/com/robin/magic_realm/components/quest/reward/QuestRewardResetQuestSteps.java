package com.robin.magic_realm.components.quest.reward;

import java.util.ArrayList;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.quest.*;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRewardResetQuestSteps extends QuestReward {
	
	public static final String RESET_METHOD = "_reset_method";
	public static final String QUEST_STEPS_DEPTH = "_depth";
	public static final String QUEST_STEP_NAME = "_step_name";
	public static final String RESET_DEPENDENT_QUEST_STEPS = "_reset_dependent_steps";
	public static final String RESET_DEPENDENT_FAILED_QUEST_STEPS = "_reset_dependent_failed_steps";
	public static final String READY_RESETTED_STEPS = "_ready_resetted_steps";

	public static final class ResetMethod {
		private final String _name;
		private final int _ordinal;
		private ResetMethod(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final ResetMethod CascadedReset = new ResetMethod("CascadedReset", 0);
		public static final ResetMethod SingleQuestStep = new ResetMethod("SingleQuestStep", 1);

		private static final ResetMethod[] _VALUES = { CascadedReset, SingleQuestStep };
		public static ResetMethod[] values() { ResetMethod[] r = new ResetMethod[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static ResetMethod valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	
	public QuestRewardResetQuestSteps(GameObject go) {
		super(go);
	}
	
	public void processReward(JFrame frame, CharacterWrapper character) {
		String currentDay = character.getCurrentDayKey();
		ArrayList stepsToReset = new ArrayList();
		ArrayList dependentSteps = new ArrayList();
		Quest quest = getParentQuest();
		QuestStep currentStep = getParentStep();
		
		ResetMethod _rm = resetMode();
		if (_rm == ResetMethod.SingleQuestStep) {
			for (java.util.Iterator _j14it2437 = (quest.getSteps()).iterator(); _j14it2437.hasNext(); ) {
			  QuestStep step = (QuestStep) _j14it2437.next();
				if (step.toString().matches(questStepToReset())) {
					stepsToReset.add(step);
				}
			}
		} else {
			stepsToReset.add(currentStep);
			dependentSteps.add(currentStep);
			int i=0;
			while (i < questStepsDepthToReset()) {
				for (java.util.Iterator _j14it2438 = (quest.getSteps()).iterator(); _j14it2438.hasNext(); ) {
				  QuestStep step = (QuestStep) _j14it2438.next();
					for (java.util.Iterator _j14it2439 = (dependentSteps).iterator(); _j14it2439.hasNext(); ) {
					  QuestStep dependentStep = (QuestStep) _j14it2439.next();
						if (dependentStep.requires(step) && !stepsToReset.contains(step)) {
							stepsToReset.add(step);
						}
					}
				}
				dependentSteps.addAll(stepsToReset);
				i++;
			}
		}
		
		if (resetRequiredSteps() || resetRequiredFailedSteps()) {
			ArrayList moreStepsToReset = new ArrayList();
			boolean newStepsToAdd = true;
			while (newStepsToAdd) {
				newStepsToAdd = false;
				for (java.util.Iterator _j14it2440 = (quest.getSteps()).iterator(); _j14it2440.hasNext(); ) {
				  QuestStep step = (QuestStep) _j14it2440.next();
					for (java.util.Iterator _j14it2441 = (stepsToReset).iterator(); _j14it2441.hasNext(); ) {
					  QuestStep resettedStep = (QuestStep) _j14it2441.next();
						if (!moreStepsToReset.contains(step) && ((resetRequiredSteps() && step.requires(resettedStep)) || (resetRequiredFailedSteps() && step.requiresFail(resettedStep)))) {
							moreStepsToReset.add(step);
							newStepsToAdd = true;
						}
					}
				}
				stepsToReset.addAll(moreStepsToReset);
			}
		}
		
		for (java.util.Iterator _j14it2442 = (stepsToReset).iterator(); _j14it2442.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2442.next();
			if (step.getState() == QuestStepState.Pending) continue;
			step.clearStates();
			if (readyResettedSteps()) {
				step.setState(QuestStepState.Ready, currentDay);
			}
			else {
				step.setState(QuestStepState.Pending, currentDay);
			}
		}
		quest.updateStepStates(currentDay);
	}
	
	public RewardType getRewardType() {
		return RewardType.ResetQuestSteps;
	}

	public String getDescription() {
		ResetMethod _rm2 = resetMode();
		if (_rm2 == ResetMethod.SingleQuestStep) {
			return questStepToReset()+" is reset.";
		}
		return "Resets all quest steps depending on current step with a depth of "+questStepsDepthToReset()+". ";
	}
	
	private ResetMethod resetMode() {
		return ResetMethod.valueOf(getString(RESET_METHOD));
	}
	private int questStepsDepthToReset() {
		return getInt(QUEST_STEPS_DEPTH);
	}
	private String questStepToReset() {
		return getString(QUEST_STEP_NAME);
	}
	private boolean resetRequiredSteps() {
		return getBoolean(RESET_DEPENDENT_QUEST_STEPS);
	}
	private boolean resetRequiredFailedSteps() {
		return getBoolean(RESET_DEPENDENT_FAILED_QUEST_STEPS);
	}
	private boolean readyResettedSteps() {
		return getBoolean(READY_RESETTED_STEPS);
	}
}