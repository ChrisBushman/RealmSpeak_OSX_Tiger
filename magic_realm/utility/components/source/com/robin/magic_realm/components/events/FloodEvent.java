package com.robin.magic_realm.components.events;

import java.util.ArrayList;

import com.robin.game.objects.GameData;
import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.ClearingDetail;
import com.robin.magic_realm.components.TileComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.RealmLogging;

public class FloodEvent implements IEvent {
	private static final String title = "Flood";
	private static final String description = "In all non-mountain clearings of a random River hex and all adjacent hexes Flood is cast in the first round of combat.";
	
	public void applyBirdsong(GameData data) {
	}
	public void applySunset(GameData data) {
		ArrayList waterTiles = RealmEvents.chooseRandomWaterAndAdjacentTiles(data);
		if (!waterTiles.isEmpty()) {
			for (java.util.Iterator _j14it2468 = (waterTiles).iterator(); _j14it2468.hasNext(); ) {
			  TileComponent tile = (TileComponent) _j14it2468.next();
				GameObject config = RealmEvents.findEventsConfig(data);
				for (java.util.Iterator _j14it2469 = (tile.getClearings()).iterator(); _j14it2469.hasNext(); ) {
				  ClearingDetail clearing = (ClearingDetail) _j14it2469.next();
					if (!clearing.isMountain()) {
						tile.getGameObject().addThisAttributeListItem(Constants.EVENT_FLOOD,clearing.getNumString());
					}
				}
				RealmEvents.addEffectForTile(config,Constants.EVENT_FLOOD,tile.getGameObject().getStringId());
				RealmLogging.logMessage("Event","Flood: Cast Flood in all non-mountain clearings in "+tile.getGameObject().getNameWithNumber()+".");
			}
		}
	}
	public void expire(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_FLOOD);
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2470 = (ids).iterator(); _j14it2470.hasNext(); ) {
			  String id = (String) _j14it2470.next();
				GameObject tile = data.getGameObject(new Long(id));
				tile.removeThisAttribute(Constants.EVENT_FLOOD);
				RealmEvents.removeEffectForTile(config,Constants.EVENT_FLOOD,id);
			}
		}
	}
	public String getTitle() {
		return title;
	}
	public String getDescription(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		String text = "";
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_FLOOD);
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2471 = (ids).iterator(); _j14it2471.hasNext(); ) {
			  String id = (String) _j14it2471.next();
				GameObject tile = data.getGameObject(new Long(id));
				text = text + tile.getNameWithNumber() + ", ";
			}
		}
		else {
			return description;
		}
		if ((text.length() == 0)) return description;
		text = "In " + text.substring(0,text.length()-2) + " Flood is cast in the first round of combat.";
		return text;
	}
}