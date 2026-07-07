package com.robin.magic_realm.components.events;

import java.util.ArrayList;

import com.robin.game.objects.GameData;
import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.TileComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.RealmCalendar;
import com.robin.magic_realm.components.utility.RealmLogging;
import com.robin.magic_realm.components.wrapper.GameWrapper;

public class FrozenRiverEvent implements IEvent {
	private static final String title = "Frozen River";
	private static final String description = "A random River hex and all adjacent hexes have their water clearings frozen until the end of the week.";
	public void applyBirdsong(GameData data) {
		ArrayList freezingTiles = RealmEvents.chooseRandomWaterAndAdjacentTiles(data);		
		if (!freezingTiles.isEmpty()) {
			GameObject config = RealmEvents.findEventsConfig(data);
			for (java.util.Iterator _j14it2479 = (freezingTiles).iterator(); _j14it2479.hasNext(); ) {
			  TileComponent tile = (TileComponent) _j14it2479.next();
				tile.getGameObject().setThisAttribute(Constants.EVENT_FROZEN_WATER);
				RealmEvents.addEffectForTile(config,Constants.EVENT_FROZEN_WATER,tile.getGameObject().getStringId());
				RealmLogging.logMessage("Event","Frozen Water: All water clreaings in "+tile.getGameObject().getNameWithNumber()+" are frozen until the end of the week.");
			}
		}
	}
	public void applySunset(GameData data) {
	}
	public void expire(GameData data) {
		GameWrapper game = GameWrapper.findGame(data);
		if (RealmCalendar.isSeventhDay(game.getDay())) {
			GameObject config = RealmEvents.findEventsConfig(data);
			ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_FROZEN_WATER);
			if (ids!=null && !ids.isEmpty()) {
				for (java.util.Iterator _j14it2480 = (ids).iterator(); _j14it2480.hasNext(); ) {
				  String id = (String) _j14it2480.next();
					GameObject tile = data.getGameObject(Long.valueOf(id));
					tile.removeThisAttribute(Constants.EVENT_FROZEN_WATER);
					RealmEvents.removeEffectForTile(config,Constants.EVENT_FROZEN_WATER,id);
				}
			}
		}
	}
	public String getTitle() {
		return title;
	}
	public String getDescription(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		String text = "";
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_FROZEN_WATER);
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2481 = (ids).iterator(); _j14it2481.hasNext(); ) {
			  String id = (String) _j14it2481.next();
				GameObject tile = data.getGameObject(Long.valueOf(id));
				text = text + tile.getNameWithNumber() + ", ";
			}
		}
		text = "All water clearings in "+text.substring(0,text.length()-2) + " are frozen until the end of the week.";
		return text;
	}
}