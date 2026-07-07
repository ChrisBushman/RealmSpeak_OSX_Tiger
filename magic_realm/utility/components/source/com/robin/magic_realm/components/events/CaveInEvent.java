package com.robin.magic_realm.components.events;

import java.util.ArrayList;
import java.util.Arrays;

import com.robin.game.objects.GameData;
import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.ClearingDetail;
import com.robin.magic_realm.components.TileComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.RealmLogging;

public class CaveInEvent implements IEvent {
	private static final String title = "Cave In";
	private static final String description = "In a random caves clearing Roof Collapses is cast in the first round of combat.";
	
	public void applyBirdsong(GameData data) {
	}
	public void applySunset(GameData data) {
		ArrayList clearingTypes = new ArrayList(Arrays.asList(new String[]{"caves"}));
		ClearingDetail clearing = RealmEvents.chooseRandomClearing(data,clearingTypes);
		if (clearing!=null) {
			TileComponent tile = clearing.getTileLocation().tile;
			GameObject config = RealmEvents.findEventsConfig(data);
			tile.getGameObject().addThisAttributeListItem(Constants.EVENT_CAVE_IN,clearing.getNumString());
			RealmEvents.addEffectForTile(config,Constants.EVENT_CAVE_IN,tile.getGameObject().getStringId());
			RealmLogging.logMessage("Event","Cave In: Cast Roof Collapses in the first round of combat in "+clearing.getNumString()+" of "+tile.getGameObject().getNameWithNumber()+".");
		}
	}
	public void expire(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_CAVE_IN);
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2465 = (ids).iterator(); _j14it2465.hasNext(); ) {
			  String id = (String) _j14it2465.next();
				GameObject tile = data.getGameObject(new Long(id));
				tile.removeThisAttribute(Constants.EVENT_CAVE_IN);
				RealmEvents.removeEffectForTile(config,Constants.EVENT_CAVE_IN,id);
			}
		}
	}
	public String getTitle() {
		return title;
	}
	public String getDescription(GameData data) {
		GameObject config = RealmEvents.findEventsConfig(data);
		String text = "";
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_CAVE_IN);
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2466 = (ids).iterator(); _j14it2466.hasNext(); ) {
			  String id = (String) _j14it2466.next();
				GameObject tile = data.getGameObject(new Long(id));
				for (java.util.Iterator _j14it2467 = (tile.getThisAttributeList(Constants.EVENT_CAVE_IN)).iterator(); _j14it2467.hasNext(); ) {
				  String cl = (String) _j14it2467.next();;
					text = text + tile.getNameWithNumber() +" ("+cl+")"+ ", ";
				}
			}
		}
		else {
			return description;
		}
		if ((text.length() == 0)) return description;
		text = "In "+text.substring(0,text.length()-2) + " Roof Collapses is cast in the first round of combat.";
		return text;
	}
}