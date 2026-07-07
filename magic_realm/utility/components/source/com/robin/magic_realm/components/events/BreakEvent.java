package com.robin.magic_realm.components.events;

import com.robin.game.objects.GameData;
import com.robin.magic_realm.components.utility.RealmLogging;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.SpellMasterWrapper;

public class BreakEvent implements IEvent {
	private static final String title = "Break";
	private static final String description = "At midnight, all spells in effect anywhere are broken. This includes Curses and Mesmerizes.";
	public void applyBirdsong(GameData data) {
	}
	public void applySunset(GameData data) {
	}
	public void expire(GameData data) {
		SpellMasterWrapper sm = SpellMasterWrapper.getSpellMaster(data);
		sm.expireAllSpells();
		for (java.util.Iterator _j14it2461 = (RealmEvents.getLivingCharacters(data)).iterator(); _j14it2461.hasNext(); ) {
		  CharacterWrapper character = (CharacterWrapper) _j14it2461.next();
			character.removeAllCurses();
		}
		RealmLogging.logMessage("Break","All spells in effect anywhere are broken.");
	}
	public String getTitle() {
		return title;
	}
	public String getDescription(GameData data) {
		return description;
	}
}