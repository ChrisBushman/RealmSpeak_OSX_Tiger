package com.robin.magic_realm.components.events;

import java.util.ArrayList;

import com.robin.game.objects.GameData;
import com.robin.game.objects.GameObject;
import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.ClearingDetail;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.TileComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.RealmCalendar;
import com.robin.magic_realm.components.utility.RealmLogging;
import com.robin.magic_realm.components.wrapper.GameWrapper;

public class MountainSurgeEvent implements IEvent {
	private static final String title = "Mountain Surge";
	private static final String description = "At sunset in a random hex Mountain Surge is cast and lasts until the seventh day of the week.";
	public void applyBirdsong(GameData data) {
		TileComponent tile = RealmEvents.chooseRandomTile(data);
		ArrayList clearings = new ArrayList();
		if (tile!=null) {
			for (java.util.Iterator _j14it2486 = (tile.getClearings()).iterator(); _j14it2486.hasNext(); ) {
			  ClearingDetail cl = (ClearingDetail) _j14it2486.next();
				for (java.util.Iterator _j14it2487 = (cl.getClearingComponents()).iterator(); _j14it2487.hasNext(); ) {
				  RealmComponent rc = (RealmComponent) _j14it2487.next();
					if (rc.isCharacter() || rc.isHiredOrControlled()) {
						clearings.add(cl);
					}
				}
			}
			if (!clearings.isEmpty()) {
				ClearingDetail chosenClearing = (ClearingDetail) clearings.get(RandomNumber.getRandom(clearings.size()));
				GameObject config = RealmEvents.findEventsConfig(data);
				tile.getGameObject().addThisAttributeListItem(Constants.EVENT_MOUNTAIN_SURGE,chosenClearing.getNumString());
				RealmEvents.addEffectForTile(config,Constants.EVENT_MOUNTAIN_SURGE,tile.getGameObject().getStringId());
				RealmLogging.logMessage("Event","Mountain Surge: Cast Mountain Surge in "+chosenClearing.getNumString()+" of "+tile.getGameObject().getNameWithNumber()+".");
			}
		}
	}
	public void applySunset(GameData data) {
	}
	public void expire(GameData data) {
		GameWrapper game = GameWrapper.findGame(data);
		if (RealmCalendar.isSeventhDay(game.getDay())) {
			GameObject config = RealmEvents.findEventsConfig(data);
			ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_MOUNTAIN_SURGE);
			if (ids!=null && !ids.isEmpty()) {
				for (java.util.Iterator _j14it2488 = (ids).iterator(); _j14it2488.hasNext(); ) {
				  String id = (String) _j14it2488.next();
					GameObject tile = data.getGameObject(Long.valueOf(id));
					tile.removeThisAttribute(Constants.EVENT_MOUNTAIN_SURGE);
					RealmEvents.removeEffectForTile(config,Constants.EVENT_MOUNTAIN_SURGE,id);
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
		ArrayList ids = RealmEvents.getTileIdsForEffect(config,Constants.EVENT_MOUNTAIN_SURGE);
		if (ids!=null && !ids.isEmpty()) {
			for (java.util.Iterator _j14it2489 = (ids).iterator(); _j14it2489.hasNext(); ) {
			  String id = (String) _j14it2489.next();
				GameObject tile = data.getGameObject(Long.valueOf(id));
				for (java.util.Iterator _j14it2490 = (tile.getThisAttributeList(Constants.EVENT_MOUNTAIN_SURGE)).iterator(); _j14it2490.hasNext(); ) {
				  String cl = (String) _j14it2490.next();;
					text = text + tile.getNameWithNumber() +" ("+cl+")"+ ", ";
				}
			}
		}
		else {
			return description;
		}
		if ((text.length() == 0)) return description;
		text = "In "+text.substring(0,text.length()-2) + " Mountain Surge is cast and lasts until the seventh day of the week.";
		return text;
	}
}