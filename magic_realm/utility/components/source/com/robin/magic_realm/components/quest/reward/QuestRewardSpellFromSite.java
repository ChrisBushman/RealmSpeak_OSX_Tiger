package com.robin.magic_realm.components.quest.reward;

import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.robin.game.objects.GameObject;
import com.robin.game.objects.GamePool;
import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.quest.DrawType;
import com.robin.magic_realm.components.quest.Quest;
import com.robin.magic_realm.components.swing.RealmComponentOptionChooser;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRewardSpellFromSite extends QuestReward {
	
	public static final String SITE_REGEX = "_siterx";
	public static final String DRAW_TYPE = "_dt";

	public QuestRewardSpellFromSite(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame, CharacterWrapper character) {
		GamePool pool = new GamePool(getGameData().getGameObjects());
		ArrayList sourceObjects = pool.find("spell_site");
		sourceObjects.addAll(pool.find("visitor,!name=Scholar")); 
		sourceObjects.addAll(pool.find("artifact")); 
		sourceObjects.addAll(pool.find("book,magic")); 
		ArrayList objects = getObjectList(sourceObjects,getSiteRegex());
		if (objects.isEmpty()) {
			JOptionPane.showMessageDialog(frame,"The site specified by the reward was not found!","Quest Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		GameObject selected = null;
		if (objects.size()==1) {
			selected = (GameObject) objects.get(0);
		}
		else {
			RealmComponentOptionChooser chooser = new RealmComponentOptionChooser(frame,getTitleForDialog()+" Select a Site to learn a spell from:",false);
			chooser.addGameObjects(objects,true);
			chooser.setVisible(true);
			selected = chooser.getFirstSelectedComponent().getGameObject();
		}
		
		ArrayList hold = selected.getHold();
		ArrayList learnable = new ArrayList();
		for (java.util.Iterator _j14it2408 = (hold).iterator(); _j14it2408.hasNext(); ) {
		  GameObject spell = (GameObject) _j14it2408.next();
			if (character.canLearn(spell)) {
				learnable.add(spell);
			}
		}
		if (learnable.isEmpty()) {
			Quest.showQuestMessage(frame,getParentQuest(),"There are no spells to learn here.",getTitleForDialog(),RealmComponent.getRealmComponent(selected));
			return;
		}
		GameObject spell = null;
		DrawType _dt = getDrawType();
		if (_dt == DrawType.Top) {
			spell = (GameObject) learnable.get(0);
		} else if (_dt == DrawType.Bottom) {
			spell = (GameObject) learnable.get(learnable.size()-1);
		} else if (_dt == DrawType.Random) {
			spell = (GameObject) learnable.get(RandomNumber.getRandom(learnable.size()));
		} else if (_dt == DrawType.Choice) {
			RealmComponentOptionChooser chooser = new RealmComponentOptionChooser(frame,getTitleForDialog()+" Which spell?",false);
			chooser.addGameObjects(learnable,true);
			chooser.setVisible(true);
			spell = chooser.getFirstSelectedComponent().getGameObject();
		}		
		if (spell!=null) { // shouldn't ever be null
			character.recordNewSpell(frame,spell);
		}
	}
	
	public RewardType getRewardType() {
		return RewardType.SpellFromSite;
	}

	public String getDescription() {
		StringBuffer sb = new StringBuffer();
		DrawType _dt2 = getDrawType();
		if (_dt2 == DrawType.Top) {
			sb.append("Learn the top");
		} else if (_dt2 == DrawType.Bottom) {
			sb.append("Learn the bottom");
		} else if (_dt2 == DrawType.Choice) {
			sb.append("Learn a");
		} else if (_dt2 == DrawType.Random) {
			sb.append("Learn a random");
		}
		sb.append(" spell from /");
		sb.append(getSiteRegex());
		sb.append("/.");
		return sb.toString();
	}
	
	public DrawType getDrawType() {
		return DrawType.valueOf(getString(DRAW_TYPE));
	}
	
	public String getSiteRegex() {
		return getString(SITE_REGEX);
	}
	public static ArrayList getObjectList(ArrayList sourceObjects,String regEx) {
		Pattern pattern = (regEx==null || regEx.length()==0)?null:Pattern.compile(regEx);
		ArrayList objects = new ArrayList();
		for (java.util.Iterator _j14it2409 = (sourceObjects).iterator(); _j14it2409.hasNext(); ) {
		  GameObject obj = (GameObject) _j14it2409.next();
			if (pattern==null || pattern.matcher(obj.getName()).find()) {
				objects.add(obj);
			}
		}
		return objects;
	}
}