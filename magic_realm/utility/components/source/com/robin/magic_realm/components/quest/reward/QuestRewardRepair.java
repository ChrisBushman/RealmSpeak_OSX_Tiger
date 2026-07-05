package com.robin.magic_realm.components.quest.reward;

import java.util.regex.Pattern;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.ArmorChitComponent;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.utility.SpellUtility;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRewardRepair extends QuestReward {
	
	public final static String ITEM = "_item";
	
	public QuestRewardRepair(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame,CharacterWrapper character) {
		if (!getRegex().isEmpty()) {
			Pattern pattern = Pattern.compile(getRegex());
			for (GameObject obj : character.getInventory()) {
				if (!pattern.matcher(obj.getName()).find()) continue;
				RealmComponent rc = RealmComponent.getRealmComponent(obj);
				if (!rc.isArmor()) continue;
				ArmorChitComponent armor = (ArmorChitComponent) rc;
				if (armor.isDamaged()) armor.setIntact(true);
			}
		}
		else {
			SpellUtility.repair(character);
		}
	}
	
	private String getRegex() {
		return getString(ITEM);
	}
	
	public String getDescription() {
		if (!getRegex().isEmpty()) {
			return "Repairs '"+getRegex()+"' of the character.";
		}
		return "Repairs all items of the character.";
	}
	public RewardType getRewardType() {
		return RewardType.Repair;
	}

}