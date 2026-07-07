package com.robin.magic_realm.components.quest.reward;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.PathDetail;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.quest.MapScopeType;
import com.robin.magic_realm.components.quest.QuestLocation;
import com.robin.magic_realm.components.quest.RoadDiscoveryType;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRewardPathsPassages extends QuestReward {
	
	public static String DISCOVERY_TYPE = "_dst";
	public static String DISCOVERY_SCOPE = "_dss";
	public static String LOCATION_ONLY = "_loc_only";
	public static String LOCATION = "_loc";

	public QuestRewardPathsPassages(GameObject go) {
		super(go);
	}
	
	public void processReward(JFrame frame, CharacterWrapper character) {
		MapScopeType mapScope = getScopeType();
		RoadDiscoveryType discoveryType = getDiscoveryType();
		
		TileLocation current = character.getCurrentLocation();
		
		ArrayList roads = new ArrayList();
		if (mapScope==MapScopeType.Clearing) {
			if (locationOnly()) {
				QuestLocation questLoc = getQuestLocation();
				if (questLoc == null) return;
				ArrayList locations = questLoc.fetchAllLocations(frame, character, character.getGameData());
				for (java.util.Iterator _j14it2380 = (locations).iterator(); _j14it2380.hasNext(); ) {
				  TileLocation loc = (TileLocation) _j14it2380.next();
					for (java.util.Iterator _j14it2381 = (loc.clearing.getAllConnectedPaths()).iterator(); _j14it2381.hasNext(); ) {
					  PathDetail path = (PathDetail) _j14it2381.next();
						if (discoveryType.matches(path)) {
							roads.add(path);
						}
					}
				}
			}
			else {
				if (!current.isInClearing()) return;
				for (java.util.Iterator _j14it2382 = (current.clearing.getAllConnectedPaths()).iterator(); _j14it2382.hasNext(); ) {
				  PathDetail path = (PathDetail) _j14it2382.next();
					if (discoveryType.matches(path)) {
						roads.add(path);
					}
				}
			}
		}
		else { // Tile
			if (locationOnly()) {
				QuestLocation questLoc = getQuestLocation();
				if (questLoc == null) return;
				ArrayList locations = questLoc.fetchAllLocations(frame, character, character.getGameData());
				for (java.util.Iterator _j14it2383 = (locations).iterator(); _j14it2383.hasNext(); ) {
				  TileLocation loc = (TileLocation) _j14it2383.next();
					if (discoveryType.matchesSecretPassages()) {
						roads.addAll(loc.tile.getSecretPassages(true));
					}
					if (discoveryType.matchesHiddenPaths()) {
						roads.addAll(loc.tile.getHiddenPaths(true));
					}
				}
			}
			else {
				if (discoveryType.matchesSecretPassages()) {
					roads.addAll(current.tile.getSecretPassages(true));
				}
				if (discoveryType.matchesHiddenPaths()) {
					roads.addAll(current.tile.getHiddenPaths(true));
				}
			}
		}
		
		for (java.util.Iterator _j14it2384 = (roads).iterator(); _j14it2384.hasNext(); ) {
		  PathDetail road = (PathDetail) _j14it2384.next();
			String key = road.getFullPathKey();
			if (road.isSecret() && !character.hasSecretPassageDiscovery(key)) {
				character.addSecretPassageDiscovery(key);
			}
			if (road.isHidden() && !character.hasHiddenPathDiscovery(key)) {
				character.addHiddenPathDiscovery(key);
			}
		}
	}
	
	public String getDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("Immediately discover all ");
		RoadDiscoveryType rdt = getDiscoveryType();
		if (rdt == RoadDiscoveryType.HiddenPaths) {
			sb.append("hidden paths");
		} else if (rdt == RoadDiscoveryType.SecretPassages) {
			sb.append("secret passages");
		} else if (rdt == RoadDiscoveryType.PathsOrPassages) {
			sb.append("hidden paths and secret passages");
		}
		MapScopeType scope = getScopeType();
		sb.append(" in the");
		if (!locationOnly() ) {
			sb.append(" current");
		}
		if (scope == MapScopeType.Clearing) {
			sb.append(" clearing");
		} else if (scope == MapScopeType.Tile) {
			sb.append(" tile");
		}
		if (locationOnly() && getQuestLocation() != null) {
			sb.append(" of "+getQuestLocation().getName());
		}
		sb.append(".");
		return sb.toString();
	}

	public RewardType getRewardType() {
		return RewardType.PathsPassages;
	}
	
	public RoadDiscoveryType getDiscoveryType() {
		return RoadDiscoveryType.valueOf(getString(DISCOVERY_TYPE));
	}
	
	public MapScopeType getScopeType() {
		return MapScopeType.valueOf(getString(DISCOVERY_SCOPE));
	}
	
	private boolean locationOnly() {
		return getBoolean(LOCATION_ONLY);
	}

	public boolean usesLocationTag(String tag) {
		QuestLocation loc = getQuestLocation();
		return loc!=null && tag.equals(loc.getName());
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
	
	public void setQuestLocation(QuestLocation location) {
		setString(LOCATION,location.getGameObject().getStringId());
	}
	public void updateIds(Hashtable lookup) {
		updateIdsForKey(lookup,LOCATION);
	}
}