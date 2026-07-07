package com.robin.magic_realm.components.events;

import java.util.ArrayList;

import com.robin.game.objects.GameData;
import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.RealmLogging;

public class ViolentWindsEvent implements IEvent {
	private static final String title = "Violent Winds";
	private static final String description = "No one is able to Fly today (but first Fly activity for landing). Flyers +1 to maneuver.";
	public void applyBirdsong(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		for (java.util.Iterator _j14it2463 = (RealmEvents.chooseAllTiles(data)).iterator(); _j14it2463.hasNext(); ) {
		  GameObject tile = (GameObject) _j14it2463.next();
			tile.setThisAttribute(Constants.EVENT_VIOLENT_WINDS);
			RealmEvents.addEffectForTile(config,Constants.EVENT_VIOLENT_WINDS,tile.getStringId());
		}
		RealmLogging.logMessage("Event","Violent Winds: No one is able to Fly today (but first Fly activity for landing). Flyers +1 to maneuver.");
	}
	public void applySunset(GameData data) {
	}
	public void expire(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_VIOLENT_WINDS);
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2464 = (ids).iterator(); _j14it2464.hasNext(); ) {
			  String id = (String) _j14it2464.next();
				GameObject tile = data.getGameObject(new Long(id));
				tile.removeThisAttribute(Constants.EVENT_VIOLENT_WINDS);
				RealmEvents.removeEffectForTile(config,Constants.EVENT_VIOLENT_WINDS,id);
			}
		}
	}
	public String getTitle() {
		return title;
	}
	public String getDescription(GameData data) {
		return description;
	}
}