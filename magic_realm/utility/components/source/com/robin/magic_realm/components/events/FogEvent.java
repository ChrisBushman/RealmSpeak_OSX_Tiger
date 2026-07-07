package com.robin.magic_realm.components.events;

import java.util.ArrayList;

import com.robin.game.objects.GameData;
import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.TileComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.RealmLogging;

public class FogEvent implements IEvent {
	private static final String title = "Fog";
	private static final String description = "A random hex and all adjacent hexes are affected by Fog.";
	public void applyBirdsong(GameData data) {
		ArrayList tiles = RealmEvents.chooseRandomAndAdjacentTiles(data);
		if (tiles!=null && !tiles.isEmpty()) {
			GameObject config = RealmEvents.findEventsConfig(data);
			for (java.util.Iterator _j14it2472 = (tiles).iterator(); _j14it2472.hasNext(); ) {
			  TileComponent tile = (TileComponent) _j14it2472.next();
				tile.getGameObject().setThisAttribute(Constants.EVENT_FOG);
				RealmEvents.addEffectForTile(config,Constants.EVENT_FOG,tile.getGameObject().getStringId());
				RealmLogging.logMessage("Event","Fog: Cannot peer in "+tile.getGameObject().getNameWithNumber());
			}
		}
	}
	public void applySunset(GameData data) {
	}
	public void expire(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_FOG);
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2473 = (ids).iterator(); _j14it2473.hasNext(); ) {
			  String id = (String) _j14it2473.next();
				GameObject tile = data.getGameObject(new Long(id));
				tile.removeThisAttribute(Constants.EVENT_FOG);
				RealmEvents.removeEffectForTile(config,Constants.EVENT_FOG,id);
			}
		}
	}
	public String getTitle() {
		return title;
	}
	public String getDescription(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		String text = "";
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_FOG);
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2474 = (ids).iterator(); _j14it2474.hasNext(); ) {
			  String id = (String) _j14it2474.next();
				GameObject tile = data.getGameObject(new Long(id));
				text = text + tile.getNameWithNumber() + ", ";
			}
		}
		text = text.substring(0,text.length()-2) + " are affected by Fog.";
		return text;
	}
}