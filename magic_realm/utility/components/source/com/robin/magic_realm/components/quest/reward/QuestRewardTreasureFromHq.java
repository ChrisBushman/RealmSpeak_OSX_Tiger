package com.robin.magic_realm.components.quest.reward;

import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.robin.game.objects.GameObject;
import com.robin.game.objects.GamePool;
import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.quest.DrawType;
import com.robin.magic_realm.components.quest.Quest;
import com.robin.magic_realm.components.swing.RealmComponentOptionChooser;
import com.robin.magic_realm.components.table.Loot;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.SetupCardUtility;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRewardTreasureFromHq extends QuestReward {
	
	public static final String HQ_REGEX = "_hqrx";
	public static final String DRAW_TYPE = "_dt";
	public static final String CLEARING = "_cl";

	public QuestRewardTreasureFromHq(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame, CharacterWrapper character) {
		GamePool pool = new GamePool(getGameData().getGameObjects());
		ArrayList sourceObjects = pool.find("rank=HQ");
		ArrayList objects = getObjectList(sourceObjects,getHqRegex(),character);
		ArrayList validObjects = new ArrayList();
		for (java.util.Iterator _j14it2367 = (objects).iterator(); _j14it2367.hasNext(); ) {
		  GameObject object = (GameObject) _j14it2367.next();
			if (!object.hasThisAttribute(Constants.CLONED)) {
				validObjects.add(object);
			}
		}
		if (validObjects.isEmpty()) {
			JOptionPane.showMessageDialog(frame,"The HQ specified by the reward was not found!","Quest Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		GameObject selected = null;
		if (validObjects.size()==1) {
			selected = (GameObject) validObjects.get(0);
		}
		else {
			RealmComponentOptionChooser chooser = new RealmComponentOptionChooser(frame,getTitleForDialog()+" Select a HQ to recieve treasure from:",false);
			chooser.addGameObjects(validObjects,true);
			chooser.setVisible(true);
			selected = chooser.getFirstSelectedComponent().getGameObject();
		}
		
		GameObject holder = SetupCardUtility.getDenizenHolder(selected);
		ArrayList hold = new ArrayList(holder.getHold());
		ArrayList treasures = new ArrayList();
		for (java.util.Iterator _j14it2368 = (hold).iterator(); _j14it2368.hasNext(); ) {
		  Object o = (Object) _j14it2368.next();
			RealmComponent rc = RealmComponent.getRealmComponent((GameObject)o);
			if (rc.isTreasure()) {
				treasures.add((GameObject)o);
			}
		}
		if (treasures.isEmpty()) {
			Quest.showQuestMessage(frame,getParentQuest(),"The HQ specified by the reward was empty.",getTitleForDialog(),RealmComponent.getRealmComponent(selected));
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
			Loot.addItemToCharacter(frame,null,character,treasure);
		}
	}
	
	public RewardType getRewardType() {
		return RewardType.TreasureFromHq;
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
		sb.append(getHqRegex());
		sb.append("/.");
		return sb.toString();
	}
	
	public DrawType getDrawType() {
		return DrawType.valueOf(getString(DRAW_TYPE));
	}
	
	public String getHqRegex() {
		return getString(HQ_REGEX);
	}
	
	public boolean sameClearing() {
		return getBoolean(CLEARING);
	}
	
	private ArrayList getObjectList(ArrayList sourceObjects,String regEx, CharacterWrapper character) {
		ArrayList objects = new ArrayList();
		TileLocation loc = null;
		if (sameClearing()) {
			loc = character.getCurrentLocation();
			if (loc==null||loc.tile==null||loc.clearing==null) return objects;
		}
		
		Pattern pattern = (regEx==null || regEx.length()==0)?null:Pattern.compile(regEx);
		for (java.util.Iterator _j14it2369 = (sourceObjects).iterator(); _j14it2369.hasNext(); ) {
		  GameObject obj = (GameObject) _j14it2369.next();
			if (pattern==null || pattern.matcher(obj.getName()).find()) {
				if (sameClearing()) {
					RealmComponent rc = RealmComponent.getRealmComponent(obj);
					TileLocation rcLoc = rc.getCurrentLocation();
					if (rcLoc==null||rcLoc.tile!=loc.tile||rcLoc.clearing!=loc.clearing) continue;
				}
				objects.add(obj);
			}
		}
		return objects;
	}
}