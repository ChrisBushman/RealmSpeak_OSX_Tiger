package com.robin.magic_realm.components.events;

import java.util.ArrayList;

import com.robin.game.objects.GameData;
import com.robin.game.objects.GameObject;
import com.robin.general.swing.DieRoller;
import com.robin.magic_realm.components.TileComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.RealmLogging;

public class ViolentStormEvent implements IEvent {
	private static final String title = "Violent Storm";
	private static final String description = "A random hex and all adjacent hexes are affected by the Violent Storm.";
	public void applyBirdsong(GameData data) {
		ArrayList tiles = RealmEvents.chooseRandomAndAdjacentTiles(data);
		if (tiles!=null && !tiles.isEmpty()) {
			GameObject config = RealmEvents.findEventsConfig(data);
			DieRoller dieRoller = new DieRoller();
			dieRoller.rollDice();
			int result = dieRoller.getHighDieResult();
			int phasesLost;
			if (result<=1) {
				phasesLost = 4;
			}
			else if (result<=3) {
				phasesLost = 3;
			}
			else if (result<=5) {
				phasesLost = 2;
			}
			else {
				phasesLost = 1;
			}
			for (java.util.Iterator _j14it2458 = (tiles).iterator(); _j14it2458.hasNext(); ) {
			  TileComponent tile = (TileComponent) _j14it2458.next();
				tile.getGameObject().setThisAttribute(Constants.EVENT_VIOLENT_STORM,phasesLost);
				RealmEvents.addEffectForTile(config,Constants.EVENT_VIOLENT_STORM,tile.getGameObject().getStringId());
				RealmLogging.logMessage("Event","Violent Storm: "+phasesLost+" phase"+(phasesLost==1?"":"s")+" lost on entry on tile "+tile.getGameObject().getNameWithNumber());
			}
		}
	}
	public void applySunset(GameData data) {
	}
	public void expire(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_VIOLENT_STORM);
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2459 = (ids).iterator(); _j14it2459.hasNext(); ) {
			  String id = (String) _j14it2459.next();
				GameObject tile = data.getGameObject(new Long(id));
				tile.removeThisAttribute(Constants.EVENT_VIOLENT_STORM);
				RealmEvents.removeEffectForTile(config,Constants.EVENT_VIOLENT_STORM,id);
			}
		}
	}
	public String getTitle() {
		return title;
	}
	public String getDescription(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		String text = "";
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_VIOLENT_STORM);
		int phasesLost = 0;
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2460 = (ids).iterator(); _j14it2460.hasNext(); ) {
			  String id = (String) _j14it2460.next();
				GameObject tile = data.getGameObject(new Long(id));
				phasesLost = tile.getThisInt(Constants.EVENT_VIOLENT_STORM);
				text = text + tile.getNameWithNumber() + ", ";
			}
		}
		text = text.substring(0,text.length()-2) + " are affected by the Violent Storm ("+phasesLost+" phase"+(phasesLost==1?"":"s")+").";
		return text;
	}
}