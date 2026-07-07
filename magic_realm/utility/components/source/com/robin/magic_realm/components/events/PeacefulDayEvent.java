package com.robin.magic_realm.components.events;

import java.util.ArrayList;

import com.robin.game.objects.GameData;
import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.TileComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.RealmLogging;

public class PeacefulDayEvent implements IEvent {
	private static final String title = "Peaceful Day";
	private static final String description = "A random hex won't summon monsters this day.";
	public void applyBirdsong(GameData data) {
		TileComponent tile = RealmEvents.chooseRandomTileWithSummonChit(data);
		if (tile!=null) {
			GameObject config = RealmEvents.findEventsConfig(data);
			tile.getGameObject().setThisAttribute(Constants.EVENT_PEACEFUL_DAY);
			RealmEvents.addEffectForTile(config,Constants.EVENT_PEACEFUL_DAY,tile.getGameObject().getStringId());
			RealmLogging.logMessage("Event","Peaceful Day: "+tile.getGameObject().getNameWithNumber()+" won't summon monsters.");
		}
	}
	public void applySunset(GameData data) {
	}
	public void expire(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_PEACEFUL_DAY);
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2462 = (ids).iterator(); _j14it2462.hasNext(); ) {
			  String id = (String) _j14it2462.next();
				GameObject tile = data.getGameObject(new Long(id));
				tile.removeThisAttribute(Constants.EVENT_PEACEFUL_DAY);
				RealmEvents.removeEffectForTile(config,Constants.EVENT_PEACEFUL_DAY,id);
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