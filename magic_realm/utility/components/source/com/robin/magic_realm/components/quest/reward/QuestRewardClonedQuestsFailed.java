package com.robin.magic_realm.components.quest.reward;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.quest.Quest;
import com.robin.magic_realm.components.quest.QuestState;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRewardClonedQuestsFailed extends QuestReward {

	public final static String WIN_BOQ = "_win_boq";
	
	public QuestRewardClonedQuestsFailed(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame,CharacterWrapper character) {
		GameObject questGo = getGameObject().getHeldBy();
		Quest quest = new Quest(questGo);
		for (java.util.Iterator _j14it2387 = (quest.findClones(getGameData().getGameObjects())).iterator(); _j14it2387.hasNext(); ) {
		  GameObject clonedQuestGo = (GameObject) _j14it2387.next();
			Quest clonedQuest = new Quest(clonedQuestGo);
			if (clonedQuest.getOwner()==null) continue;
			clonedQuest.setState(QuestState.Failed,clonedQuest.getOwner().getCurrentDayKey(), clonedQuest.getOwner());
		}
	}
	
	public String getDescription() {
		return "Fail cloned quests.";
	}

	public RewardType getRewardType() {
		return RewardType.ClonedQuestsFailed;
	}
}