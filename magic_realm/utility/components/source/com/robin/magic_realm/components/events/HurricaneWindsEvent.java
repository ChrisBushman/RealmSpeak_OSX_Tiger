package com.robin.magic_realm.components.events;

import java.util.ArrayList;
import java.util.Arrays;

import com.robin.game.objects.GameData;
import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.ClearingDetail;
import com.robin.magic_realm.components.TileComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.RealmLogging;

public class HurricaneWindsEvent implements IEvent {
	private static final String title = "Hurricane Winds";
	private static final String description = "In a random mountain clearing Hurricane Winds is cast in the first round of combat.";
	
	public void applyBirdsong(GameData data) {
	}
	public void applySunset(GameData data) {
		ArrayList clearingTypes = new ArrayList(Arrays.asList(new String[]{"mountain"}));
		ClearingDetail clearing = RealmEvents.chooseRandomClearing(data,clearingTypes);
		if (clearing!=null) {
			TileComponent tile = clearing.getTileLocation().tile;
			GameObject config = RealmEvents.findEventsConfig(data);
			tile.getGameObject().addThisAttributeListItem(Constants.EVENT_HURRICANE_WINDS,clearing.getNumString());
			RealmEvents.addEffectForTile(config,Constants.EVENT_HURRICANE_WINDS,tile.getGameObject().getStringId());
			RealmLogging.logMessage("Event","Hurricane Winds: Cast Hurricane Winds in the first round of combat in "+clearing.getNumString()+" of "+tile.getGameObject().getNameWithNumber()+".");
		}
	}
	public void expire(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_HURRICANE_WINDS);
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2517 = (ids).iterator(); _j14it2517.hasNext(); ) {
			  String id = (String) _j14it2517.next();
				GameObject tile = data.getGameObject(new Long(id));
				tile.removeThisAttribute(Constants.EVENT_HURRICANE_WINDS);
				RealmEvents.removeEffectForTile(config,Constants.EVENT_HURRICANE_WINDS,id);
			}
		}
	}
	public String getTitle() {
		return title;
	}
	public String getDescription(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		String text = "";
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_HURRICANE_WINDS);
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2518 = (ids).iterator(); _j14it2518.hasNext(); ) {
			  String id = (String) _j14it2518.next();
				GameObject tile = data.getGameObject(new Long(id));
				for (java.util.Iterator _j14it2519 = (tile.getThisAttributeList(Constants.EVENT_HURRICANE_WINDS)).iterator(); _j14it2519.hasNext(); ) {
				  String cl = (String) _j14it2519.next();;
					text = text + tile.getNameWithNumber() +" ("+cl+")"+ ", ";
				}
			}
		}
		else {
			return description;
		}
		if ((text.length() == 0)) return description;
		text = "In " + text.substring(0,text.length()-2) + " Hurricane Winds is cast in the first round of combat.";
		return text;
	}
}