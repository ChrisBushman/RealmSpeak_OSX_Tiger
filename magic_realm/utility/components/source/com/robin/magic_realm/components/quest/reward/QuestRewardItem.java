package com.robin.magic_realm.components.quest.reward;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.robin.game.objects.GameObject;
import com.robin.game.objects.GamePool;
import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.ArmorChitComponent;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.quest.ChitItemType;
import com.robin.magic_realm.components.quest.ItemGainType;
import com.robin.magic_realm.components.quest.QuestConstants;
import com.robin.magic_realm.components.swing.RealmComponentOptionChooser;
import com.robin.magic_realm.components.table.Loot;
import com.robin.magic_realm.components.utility.ClearingUtility;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.TreasureUtility;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRewardItem extends QuestReward {
	
	public static final String GAIN_TYPE = "_g"; // gain or lose
	public static final String DAMAGED_ITEM = "_d";
	public static final String ITEM_DESC = "_idsc";
	public static final String ITEM_CHITTYPES = "_ict";
	public static final String ITEM_REGEX = "_irx";
	public static final String NATIVE_REGEX = "_nrx";
	public static final String FORCE_DEACTIVATION = "_fd";
	public static final String MARK_ITEM = "_mi";
	public static final String REQ_MARK = "_rm";
	public static final String RANDOM = "_random";
	
	public QuestRewardItem(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame,CharacterWrapper character) {
		String actionDescription;
		ArrayList objects;
		if (isGain()) {
			actionDescription = ": Select ONE item to gain.";
			if (getGainType()==ItemGainType.GainFromNativeHq || getGainType()==ItemGainType.GainClonedFromNativeHq) {
				ArrayList filteredHq = getNativeHqs();
				ArrayList sourceObjects = new ArrayList();
				for (java.util.Iterator _j14it2372 = (filteredHq).iterator(); _j14it2372.hasNext(); ) {
				  GameObject hq = (GameObject) _j14it2372.next();
					sourceObjects.addAll(hq.getHold());
				}
				objects = getObjectList(sourceObjects,getChitTypes(),getItemRegex());
			}
			else {
				objects = getObjectList(getGameData().getGameObjects(),getChitTypes(),getItemRegex());
			}
		}
		else {
			actionDescription = ": Select ONE item from your inventory to lose.";
			objects = getObjectList(character.getInventory(),getChitTypes(),getItemRegex());
		}
		
		if (requiresMark()) {
			ArrayList objectsToCheck = new ArrayList();
			objectsToCheck.addAll(objects);
			objects.clear();
			String questId = getParentQuest().getGameObject().getStringId();
			for (java.util.Iterator _j14it2373 = (objectsToCheck).iterator(); _j14it2373.hasNext(); ) {
			  GameObject item = (GameObject) _j14it2373.next();
				String mark = item.getThisAttribute(QuestConstants.QUEST_MARK);
				if (mark==null || !mark.equals(questId)) continue;
				objects.add(item);
			}
		}
		
		GameObject selected = null;
		if (objects.size()==1) {
			selected = (GameObject) objects.get(0);
		}
		else if(objects.size()==0) {
			return;
		}
		else if (selectRandom()) {
			selected = (GameObject) objects.get(RandomNumber.getRandom(objects.size()));
		}
		else {
			RealmComponentOptionChooser chooser = new RealmComponentOptionChooser(frame,getTitleForDialog()+actionDescription,false);
			chooser.addGameObjects(objects,true);
			chooser.setVisible(true);
			selected = chooser.getFirstSelectedComponent().getGameObject();
		}
		
		if (getGainType()==ItemGainType.GainCloned || getGainType()==ItemGainType.GainClonedFromNativeHq) {
			GameObject newItem = selected.copy();
			newItem.setThisAttribute(Constants.CLONED);
			selected = newItem;
		}
		
		if (markItem()) {
			selected.setThisAttribute(QuestConstants.QUEST_MARK,getParentQuest().getGameObject().getStringId());
		}
		
		if (isGain()) {
			if (damagedItem() && selected.hasThisAttribute(RealmComponent.ARMOR)) {
				ArmorChitComponent selectedItem = (ArmorChitComponent) RealmComponent.getRealmComponent(selected);
				selectedItem.setLightSideUp();
			}
			Loot.addItemToCharacter(frame,null,character,selected);
			return;
		}
		if (TreasureUtility.doDeactivate(null,character,selected,forceDeactivation())) { // null JFrame so that character isn't hit with any popups
			ItemGainType _igt = getGainType();
			if (_igt == ItemGainType.RemoveFromGame) {
				selected.getHeldBy().remove(selected);
			} else if (_igt == ItemGainType.LoseToClearing) {
				TileLocation location = character.getCurrentLocation();
				if (location.clearing == null) {
					location.setRandomClearing();
				}
				ClearingUtility.moveToLocation(selected,location);
			} else if (_igt == ItemGainType.LoseToLocation) {
				lostItem(selected);
			} else if (_igt == ItemGainType.LoseToNativeHq) {
				ArrayList filteredHq = getNativeHqs();
				if (filteredHq.isEmpty()) {
					JOptionPane.showMessageDialog(frame,"The "+selected.getName()+" could not be removed from your inventory (no matching NativeHQ found).","Quest Error",JOptionPane.ERROR_MESSAGE);
					return;
				}
				GameObject nativeHq = (GameObject) filteredHq.get(RandomNumber.getRandom(filteredHq.size()));
				nativeHq.add(selected);
			} else {
				lostItemToDefault(selected);
			}
		}
		else {
			JOptionPane.showMessageDialog(frame,"The "+selected.getName()+" could not be removed from your inventory.","Quest Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private boolean isGain() {
		return getGainType()==ItemGainType.Gain || getGainType()==ItemGainType.GainCloned || getGainType()==ItemGainType.GainFromNativeHq || getGainType()==ItemGainType.GainClonedFromNativeHq;
	}
	
	private boolean damagedItem() {
		return getBoolean(DAMAGED_ITEM);
	}
	
	public String getDescription() {
		return (isGain()?"Gains":"Loses")+" "+getItemDescription();
	}

	public RewardType getRewardType() {
		return RewardType.Item;
	}
	
	public ItemGainType getGainType() {
		String gainType = getString(GAIN_TYPE);
		if (gainType.matches("Lose")) { // compatibility for old quests
			return ItemGainType.LoseToLocation;
		}
		return ItemGainType.valueOf(getString(GAIN_TYPE));
	}
	
	public boolean forceDeactivation() {
		return getBoolean(FORCE_DEACTIVATION);
	}
	
	public boolean markItem() {
		return getBoolean(MARK_ITEM);
	}
	
	public boolean requiresMark() {
		return getBoolean(REQ_MARK);
	}
	
	public String getItemDescription() {
		return getString(ITEM_DESC);
	}
	
	public String getItemRegex() {
		return getString(ITEM_REGEX);
	}
	
	public String getNativeRegex() {
		return getString(NATIVE_REGEX);
	}
	
	public boolean selectRandom() {
		return getBoolean(RANDOM);
	}
	
	public ArrayList getChitTypes() {
		return ChitItemType.listToTypes(getList(ITEM_CHITTYPES));
	}
	
	public static ArrayList getObjectList(ArrayList sourceObjects,ArrayList chitItemTypes,String regEx) {
		Pattern pattern = (regEx==null || regEx.length()==0)?null:Pattern.compile(regEx);
		ArrayList objects = new ArrayList();
		GamePool pool = new GamePool(sourceObjects);
		for (java.util.Iterator _j14it2374 = (chitItemTypes).iterator(); _j14it2374.hasNext(); ) {
		  ChitItemType cit = (ChitItemType) _j14it2374.next();
			for (java.util.Iterator _j14it2375 = (pool.extract(Arrays.asList(cit.getKeyVals()))).iterator(); _j14it2375.hasNext(); ) {
			  GameObject obj = (GameObject) _j14it2375.next();
				if (pattern==null || pattern.matcher(obj.getName()).find()) {
					objects.add(obj);
				}
			}
		}
		return objects;
	}
	
	private ArrayList getNativeHqs() {
		GamePool pool = new GamePool(getGameData().getGameObjects());
		ArrayList allHq = pool.find("native,rank=HQ");
		ArrayList filteredHq = new ArrayList();
		String regEx = getNativeRegex();
		Pattern pattern = (regEx==null || regEx.length()==0)?null:Pattern.compile(regEx);
		for (java.util.Iterator _j14it2376 = (allHq).iterator(); _j14it2376.hasNext(); ) {
		  GameObject hq = (GameObject) _j14it2376.next();
			if (pattern==null || pattern.matcher(hq.getName()).find()) {
				filteredHq.add(hq);
			}
		}
		return filteredHq;
	}
}