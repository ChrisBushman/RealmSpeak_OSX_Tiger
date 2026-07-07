package com.robin.magic_realm.components.events;

import java.util.ArrayList;

import com.robin.game.objects.GameData;
import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.TileComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.RealmLogging;

public class IllusionEvent implements IEvent {
	private static final String title = "Illusion";
	private static final String description = "A random hex and all adjacent hexes are affected by Illusion.";
	private static final String dieMod = "+1:locate,loot,peer,readrunes,magicsight,event_illusion:all";
	public void applyBirdsong(GameData data) {
		ArrayList tiles = RealmEvents.chooseRandomAndAdjacentTiles(data);
		if (tiles!=null && !tiles.isEmpty()) {
			GameObject config = RealmEvents.findEventsConfig(data);
			for (java.util.Iterator _j14it2455 = (tiles).iterator(); _j14it2455.hasNext(); ) {
			  TileComponent tile = (TileComponent) _j14it2455.next();
				tile.getGameObject().addThisAttributeListItem(Constants.DIEMOD,dieMod);
				RealmEvents.addEffectForTile(config,Constants.EVENT_ILLUSION,tile.getGameObject().getStringId());
				RealmLogging.logMessage("Event","Illusion: Add one to all SEARCH die rolls in "+tile.getGameObject().getNameWithNumber());
			}
		}
	}
	public void applySunset(GameData data) {
	}
	public void expire(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_ILLUSION);
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2456 = (ids).iterator(); _j14it2456.hasNext(); ) {
			  String id = (String) _j14it2456.next();
				GameObject tile = data.getGameObject(Long.valueOf(id));
				tile.removeThisAttributeListItem(Constants.DIEMOD,dieMod);
				tile.removeThisAttribute(Constants.EVENT_ILLUSION);
				RealmEvents.removeEffectForTile(config,Constants.EVENT_ILLUSION,id);
			}
		}
	}
	public String getTitle() {
		return title;
	}
	public String getDescription(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		String text = "";
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_ILLUSION);
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2457 = (ids).iterator(); _j14it2457.hasNext(); ) {
			  String id = (String) _j14it2457.next();
				GameObject tile = data.getGameObject(Long.valueOf(id));
				text = text + tile.getNameWithNumber() + ", ";
			}
		}
		text = text.substring(0,text.length()-2) + " are affected by Illusion.";
		return text;
	}
}