package com.robin.magic_realm.components.events;

import java.util.ArrayList;

import com.robin.game.objects.GameData;
import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.TileComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.RealmLogging;

public class NightOfTheDemonEvent implements IEvent {
	private static final String title = "Night of the Demon";
	private static final String description = "Roll on Summon Demon effect for each combat in a random hex.";
	public void applyBirdsong(GameData data) {
		TileComponent tile = RealmEvents.chooseRandomTile(data);
		if (tile!=null) {
			GameObject config = RealmEvents.findEventsConfig(data);
			tile.getGameObject().setThisAttribute(Constants.EVENT_NIGHT_OF_THE_DEMON);
			RealmEvents.addEffectForTile(config,Constants.EVENT_NIGHT_OF_THE_DEMON,tile.getGameObject().getStringId());
			RealmLogging.logMessage("Event","Night of the Demon: Summon Demon for each combat in "+tile.getGameObject().getNameWithNumber());
		}
	}
	public void applySunset(GameData data) {
	}
	public void expire(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_NIGHT_OF_THE_DEMON);
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2482 = (ids).iterator(); _j14it2482.hasNext(); ) {
			  String id = (String) _j14it2482.next();
				GameObject tile = data.getGameObject(Long.valueOf(id));
				tile.removeThisAttribute(Constants.EVENT_NIGHT_OF_THE_DEMON);
				RealmEvents.removeEffectForTile(config,Constants.EVENT_NIGHT_OF_THE_DEMON,id);
			}
		}
	}
	public String getTitle() {
		return title;
	}
	public String getDescription(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		String text = "";
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_NIGHT_OF_THE_DEMON);
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2483 = (ids).iterator(); _j14it2483.hasNext(); ) {
			  String id = (String) _j14it2483.next();
				GameObject tile = data.getGameObject(Long.valueOf(id));
				text = text + tile.getNameWithNumber() + ", ";
			}
		}
		text = text.substring(0,text.length()-2) + " is affected by the Night of the Demon.";
		return text;
	}
}