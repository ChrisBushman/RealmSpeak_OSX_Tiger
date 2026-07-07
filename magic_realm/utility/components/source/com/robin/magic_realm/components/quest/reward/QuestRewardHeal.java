package com.robin.magic_realm.components.quest.reward;

import java.util.Collection;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.CharacterActionChitComponent;
import com.robin.magic_realm.components.quest.HealType;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRewardHeal extends QuestReward {
	
	public static final String HEAL = "_heal";
	
	public QuestRewardHeal(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame, CharacterWrapper character) {
		Collection chits;
		HealType _ht = getHealType();
		if (_ht == HealType.Fatigued) {
			chits = character.getFatiguedChits();
		} else if (_ht == HealType.Wounded) {
			chits = character.getWoundedChits();
		} else if (_ht == HealType.Restable) {
			chits = character.getRestableChits();
		} else if (_ht == HealType.Magic) {
			chits = character.getAllMagicChits();
		} else {
			chits = character.getAllChits();
		}

		for (java.util.Iterator _j14it2403 = (chits).iterator(); _j14it2403.hasNext(); ) {
		  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it2403.next();
			chit.makeActive();
		}
	}
	
	public RewardType getRewardType() {
		return RewardType.Heal;
	}
	public String getDescription() {
		return "Heals all chits of the character.";
	}
	private HealType getHealType() {
		String armor = getString(HEAL);
		if (armor == null) {
			return HealType.All;
		}
		return HealType.valueOf(getString(HEAL));
	}	
}