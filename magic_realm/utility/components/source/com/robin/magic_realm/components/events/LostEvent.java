package com.robin.magic_realm.components.events;

import java.util.ArrayList;

import com.robin.game.objects.GameData;
import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.TileComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.RealmLogging;

public class LostEvent implements IEvent {
	private static final String title = "Lost";
	private static final String description = "A random hex and all adjacent hexes are affected by Lost.";
	public void applyBirdsong(GameData data) {
		ArrayList tiles = RealmEvents.chooseRandomAndAdjacentTiles(data);
		if (tiles!=null && !tiles.isEmpty()) {
			GameObject config = RealmEvents.findEventsConfig(data);
			for (java.util.Iterator _j14it2476 = (tiles).iterator(); _j14it2476.hasNext(); ) {
			  TileComponent tile = (TileComponent) _j14it2476.next();
				RealmEvents.addEffectForTile(config,Constants.EVENT_LOST,tile.getGameObject().getStringId());
				RealmLogging.logMessage("Event","Lost: Each affected individual moves randomly in "+tile.getGameObject().getNameWithNumber());
			}
		}
	}
	public void applySunset(GameData data) {
	}
	public void expire(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_LOST);
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2477 = (ids).iterator(); _j14it2477.hasNext(); ) {
			  String id = (String) _j14it2477.next();
				GameObject tile = data.getGameObject(Long.valueOf(id));
				tile.removeThisAttribute(Constants.EVENT_LOST);
				RealmEvents.removeEffectForTile(config,Constants.EVENT_LOST,id);
			}
		}
	}
	public String getTitle() {
		return title;
	}
	public String getDescription(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		String text = "";
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_LOST);
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2478 = (ids).iterator(); _j14it2478.hasNext(); ) {
			  String id = (String) _j14it2478.next();
				GameObject tile = data.getGameObject(Long.valueOf(id));
				text = text + tile.getNameWithNumber() + ", ";
			}
		}
		text = text.substring(0,text.length()-2) + " are affected by Lost.";
		return text;
	}
}