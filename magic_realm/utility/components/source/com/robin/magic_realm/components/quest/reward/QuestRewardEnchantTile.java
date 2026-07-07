package com.robin.magic_realm.components.quest.reward;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.TileComponent;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.quest.QuestLocation;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRewardEnchantTile extends QuestReward {
	
	public static final String UNENCHANT = "_unenc";
	public static final String CHARACTERS_TILE = "_en_ch_tile";
	public static final String LOCATION = "_loc";
	public static final String AFFECT_LOCATION = "_affect_loc";
	
	public static final String NONE = "None";
	public static final String RANDOM_TILE = "Random tile";
	public static final String ALL_TILES = "All tiles";
	
	public QuestRewardEnchantTile(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame, CharacterWrapper character) {
		if (enchantCharactersTile()) {
			TileComponent tile = character.getCurrentLocation().tile;
			SetTileSide(tile);
		}
		
		QuestLocation loc = getQuestLocation();
		if (loc == null || affectLocation().matches(NONE)) {
			return;
		}
		
		ArrayList validLocations = new ArrayList();
		ArrayList validTiles = new ArrayList();
		validLocations = loc.fetchAllLocations(frame, character, getGameData());
		for (java.util.Iterator _j14it2385 = (validLocations).iterator(); _j14it2385.hasNext(); ) {
		  TileLocation location = (TileLocation) _j14it2385.next();
			if (!validTiles.contains(location.tile)) {
				validTiles.add(location.tile);
			}
		}
		if (affectLocation().matches(ALL_TILES)) {
			for (java.util.Iterator _j14it2386 = (validTiles).iterator(); _j14it2386.hasNext(); ) {
			  TileComponent tile = (TileComponent) _j14it2386.next();
				SetTileSide(tile);
			}
		}
		else {
			int random = RandomNumber.getRandom(validTiles.size());
			TileComponent tile = (TileComponent) validTiles.get(random);
			SetTileSide(tile);
		}
	}
	
	private void SetTileSide(TileComponent tile) {
		if (unenchant()) {
			tile.setLightSideUp();
		}
		else {
			tile.setDarkSideUp();
		}
	}
	
	public RewardType getRewardType() {
		return RewardType.EnchantTile;
	}
	public String getDescription() {
		StringBuffer sb = new StringBuffer();
		if (enchantCharactersTile()) {
			if (unenchant()) {
			sb.append("UnEchants the characters tile. ");
			}
			else {
				sb.append("Enchants the characters tile. "); 
			}
		}
		if (getQuestLocation() != null && !affectLocation().matches(NONE)) {
			if (affectLocation().matches(ALL_TILES)) {
				if (unenchant()) {
						sb.append("Unenchants all tiles of "+getQuestLocation().getName()+".");
					}
					else {
						sb.append("Enchants all tiles of "+getQuestLocation().getName()+".");
					}
				
			}
			else {
				if (unenchant()) {
						sb.append("Unenchants a single tile of "+getQuestLocation().getName()+".");
					}
					else {
						sb.append("Enchants a single tile of "+getQuestLocation().getName()+".");
					}
			}
		}
		
		return sb.toString();
	}
	
	private boolean unenchant() {
		return getBoolean(UNENCHANT);
	}
	private boolean enchantCharactersTile() {
		return getBoolean(CHARACTERS_TILE);
	}
	private String affectLocation() {
		return getString(AFFECT_LOCATION);
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