package com.robin.magic_realm.components.quest.reward;

import java.util.ArrayList;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.general.swing.ButtonOptionDialog;
import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.quest.*;
import com.robin.magic_realm.components.quest.requirement.QuestRequirementParams;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRewardChooseNextStep extends QuestReward {
	
	public static final String TEXT = "_tx";
	public static final String RANDOM = "_rnd";

	public QuestRewardChooseNextStep(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame, CharacterWrapper character) {
		QuestRequirementParams params = new QuestRequirementParams();
		params.timeOfCall = character.getCurrentGamePhase();
		
		ArrayList dependentSteps = new ArrayList();
		for (java.util.Iterator _j14it2404 = (Quest.currentQuest.getSteps()).iterator(); _j14it2404.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2404.next();
			if (step.getState()!=QuestStepState.Pending) continue;
			if (step.requires(QuestStep.currentStep)) {
				dependentSteps.add(step);
			}
		}
		if (dependentSteps.isEmpty()) return;
		
		String dayKey = character.getCurrentDayKey();
		String stepName=null;
		
		ArrayList availableSteps = new ArrayList();
		for (java.util.Iterator _j14it2405 = (dependentSteps).iterator(); _j14it2405.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2405.next();
			if (step.fulfillsRequirements(frame,character,params)) {
				availableSteps.add(step.getName());
			}
		}
		
		if (randomNextStep()) {
			int random = RandomNumber.getRandom(availableSteps.size());
			stepName = (String) availableSteps.get(random);
		}
		else {
			RealmComponent rc = RealmComponent.getRealmComponent(Quest.currentQuest.getGameObject());
			ButtonOptionDialog dialog = new ButtonOptionDialog(frame,rc.getIcon(),getString(TEXT),"Choose",false);
			for (java.util.Iterator _j14it2406 = (availableSteps).iterator(); _j14it2406.hasNext(); ) {
			  String availableStepName = (String) _j14it2406.next();
				dialog.addSelectionObject(availableStepName);
			}
			if (dialog.getSelectionObjectCount()>0) {
				dialog.setVisible(true);
				stepName = (String)dialog.getSelectedObject();
			}
		}
		
		for (java.util.Iterator _j14it2407 = (dependentSteps).iterator(); _j14it2407.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2407.next();
			if (!step.getName().equals(stepName)) {
				step.setState(QuestStepState.Failed,dayKey);
			}
		}
	}
	
	public RewardType getRewardType() {
		return RewardType.ChooseNextStep;
	}

	public String getDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("Choose ");
		if (randomNextStep()) {
			sb.append("randomly ");
		}
		sb.append("a path from steps dependent on this step.");
		return sb.toString();
	}
	
	private boolean randomNextStep() {
		return getBoolean(RANDOM);
	}
}