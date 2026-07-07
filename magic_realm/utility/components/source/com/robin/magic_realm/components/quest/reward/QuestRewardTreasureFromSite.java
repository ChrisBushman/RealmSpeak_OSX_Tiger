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
import com.robin.magic_realm.components.table.Loot;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRewardTreasureFromSite extends QuestReward {
	
	public static final String SITE_REGEX = "_siterx";
	public static final String DRAW_TYPE = "_dt";

	public QuestRewardTreasureFromSite(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame, CharacterWrapper character) {
		GamePool pool = new GamePool(getGameData().getGameObjects());
		ArrayList sourceObjects = pool.find("treasure_location,!treasure_within_treasure,!cannot_move");
		sourceObjects.addAll(pool.find("visitor=scholar")); 
		sourceObjects.addAll(pool.find("dwelling,!native")); 
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
			RealmComponentOptionChooser chooser = new RealmComponentOptionChooser(frame,getTitleForDialog()+" Select a Site to recieve treasure from:",false);
			chooser.addGameObjects(objects,true);
			chooser.setVisible(true);
			selected = chooser.getFirstSelectedComponent().getGameObject();
		}
		
		ArrayList hold = selected.getHold();
		ArrayList treasures = new ArrayList();
		for (java.util.Iterator _j14it2365 = (hold).iterator(); _j14it2365.hasNext(); ) {
		  GameObject o = (GameObject) _j14it2365.next();
			RealmComponent rc = RealmComponent.getRealmComponent(o);
			if (rc.isTreasure()) {
				treasures.add(o);
			}
		}
		if (treasures.isEmpty()) {
			Quest.showQuestMessage(frame,getParentQuest(),"The site specified by the reward was empty.",getTitleForDialog(),RealmComponent.getRealmComponent(selected));
			return;
		}
		GameObject treasure = null;
		DrawType _dt = getDrawType();
		if (_dt == DrawType.Top) {
			treasure = (GameObject) treasures.get(0);
		} else if (_dt == DrawType.Bottom) {
			treasure = (GameObject) treasures.get(treasures.size()-1);
		} else if (_dt == DrawType.Random) {
			treasure = (GameObject) treasures.get(RandomNumber.getRandom(treasures.size()));
		} else if (_dt == DrawType.Choice) {
			RealmComponentOptionChooser chooser = new RealmComponentOptionChooser(frame,getTitleForDialog()+" Which treasure?",false);
			chooser.addGameObjects(treasures,true);
			chooser.setVisible(true);
			treasure = chooser.getFirstSelectedComponent().getGameObject();
		}		
		if (treasure!=null) { // shouldn't ever be null
			Loot loot = new Loot(frame, character, selected, null);
			loot.characterFindsItem(character, treasure);
		}
	}
	
	public RewardType getRewardType() {
		return RewardType.TreasureFromSite;
	}

	public String getDescription() {
		StringBuffer sb = new StringBuffer();
		DrawType _dt2 = getDrawType();
		if (_dt2 == DrawType.Top) {
			sb.append("Take the top");
		} else if (_dt2 == DrawType.Bottom) {
			sb.append("Take the bottom");
		} else if (_dt2 == DrawType.Choice) {
			sb.append("Choose a");
		} else if (_dt2 == DrawType.Random) {
			sb.append("Get a random");
		}
		sb.append(" treasure from /");
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
		for (java.util.Iterator _j14it2366 = (sourceObjects).iterator(); _j14it2366.hasNext(); ) {
		  GameObject obj = (GameObject) _j14it2366.next();
			if (pattern==null || pattern.matcher(obj.getName()).find()) {
				objects.add(obj);
			}
		}
		return objects;
	}
}