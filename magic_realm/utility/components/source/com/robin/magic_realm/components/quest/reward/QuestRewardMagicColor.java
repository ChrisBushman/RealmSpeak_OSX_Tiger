package com.robin.magic_realm.components.quest.reward;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.game.objects.GamePool;
import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.ClearingDetail;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.TileComponent;
import com.robin.magic_realm.components.attribute.ColorMagic;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.quest.QuestLocation;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRewardMagicColor extends QuestReward {
	
	public static final String COLOR = "_color";
	public static final String REMOVE = "_remove";
	public static final String LOCATION = "_loc";
	public static final String AFFECT = "_affeact";
	
	public static final String CHARACTERS_CLEARING = "Characters clearing";
	public static final String CHARACTERS_TILE = "Characters tile";
	public static final String LOC_RANDOM_CLEARING = "Random clearing of the location";
	public static final String LOC_RANDOM_TILE = "Random tile of the location";
	public static final String LOC_ALL_TILES = "All tiles of the location";
	public static final String ALL = "All clearings in the realm";
	
	public QuestRewardMagicColor(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame,CharacterWrapper character) {
		ArrayList clearingsToAffect = new ArrayList();
		String target = getTarget();
		if (CHARACTERS_TILE.equals(target)) {
			clearingsToAffect.addAll(character.getCurrentLocation().tile.getClearings());
		} else if (LOC_RANDOM_CLEARING.equals(target)) {
			QuestLocation loc1 = getQuestLocation();
			ArrayList loc1Tiles = loc1.fetchAllLocations(frame, character, character.getGameData());
			int random1 = RandomNumber.getRandom(loc1Tiles.size());
			clearingsToAffect.add(((TileLocation) loc1Tiles.get(random1)).clearing);
		} else if (LOC_RANDOM_TILE.equals(target)) {
			QuestLocation loc2 = getQuestLocation();
			ArrayList loc2Tiles = loc2.fetchAllLocations(frame, character, character.getGameData());
			int random2 = RandomNumber.getRandom(loc2Tiles.size());
			clearingsToAffect.addAll(((TileLocation) loc2Tiles.get(random2)).tile.getClearings());
		} else if (LOC_ALL_TILES.equals(target)) {
			QuestLocation loc3 = getQuestLocation();
			ArrayList loc3Tiles = loc3.fetchAllLocations(frame, character, character.getGameData());
			for (java.util.Iterator _j14it2411 = (loc3Tiles).iterator(); _j14it2411.hasNext(); ) {
			  TileLocation tileLoc = (TileLocation) _j14it2411.next();
				clearingsToAffect.addAll(tileLoc.tile.getClearings());
			}
		} else if (ALL.equals(target)) {
			GamePool pool = new GamePool(character.getGameData().getGameObjects());
			for (java.util.Iterator _j14it2412 = (pool.find("tile")).iterator(); _j14it2412.hasNext(); ) {
			  GameObject go = (GameObject) _j14it2412.next();
				RealmComponent rc = RealmComponent.getRealmComponent(go);
				if (rc != null && rc.isTile()) {
					TileComponent tc = (TileComponent) rc;
					clearingsToAffect.addAll(tc.getClearings());
				}
			}
		} else {
			clearingsToAffect.add(character.getCurrentClearing());
		}
		ColorMagic color = ColorMagic.makeColorMagic(getColor(), false);
		for (java.util.Iterator _j14it2413 = (clearingsToAffect).iterator(); _j14it2413.hasNext(); ) {
		  ClearingDetail clearing = (ClearingDetail) _j14it2413.next();
			clearing.setMagic(color.getColorNumber()-1, !remove());
		}
	}
	
	private String getColor() {
		return getString(COLOR);
	}
	
	private boolean remove() {
		return getBoolean(REMOVE);
	}
	
	private String getTarget() {
		return getString(AFFECT);
	}
	
	public String getDescription() {
		if (remove()) {
			return getColor()+" Magic is removed from "+getTarget()+".";
		}
		return getTarget()+" is/are provided with "+getColor()+" Magic.";
	}

	public RewardType getRewardType() {
		return RewardType.MagicColor;
	}

	
	public void setQuestLocation(QuestLocation location) {
		setString(LOCATION,location.getGameObject().getStringId());
	}
	public QuestLocation getQuestLocation() {
		String id = getString(LOCATION);
		if (id!=null) {
			GameObject go = getGameData().getGameObject(Long.valueOf(id));
			if (go!=null) {
				return new QuestLocation(go);
			}
		}
		return null;
	}
	public void updateIds(Hashtable lookup) {
		updateIdsForKey(lookup,LOCATION);
	}
}