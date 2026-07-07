package com.robin.magic_realm.RealmBattle.targeting;

import java.util.Collection;

import com.robin.game.objects.GameData;
import com.robin.game.objects.GameObject;
import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.SpellMasterWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingSpellOrCurse extends SpellTargetingSingle {

	public SpellTargetingSpellOrCurse(CombatFrame combatFrame,SpellWrapper spell) {
		super(combatFrame, spell);
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		String targetType = spell.getGameObject().getThisAttribute("target");
		GameData gameData = spell.getGameObject().getGameData();
		if (targetType.indexOf("spell")>=0) {
			SpellMasterWrapper sm = SpellMasterWrapper.getSpellMaster(gameData);
			for (java.util.Iterator _j14it859 = (sm.getAllSpellsInClearing(battleModel.getBattleLocation(),true)).iterator(); _j14it859.hasNext(); ) {
			  SpellWrapper targetSpell = (SpellWrapper) _j14it859.next();
				if (targetSpell.isAlive()) {
					identifiers.add(targetSpell.getTargetsName());
					gameObjects.add(targetSpell.getGameObject());
				}
			}
		}
		if (targetType.indexOf("curse")>=0) {
			for (java.util.Iterator _j14it860 = (battleModel.getAllParticipatingCharacters()).iterator(); _j14it860.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it860.next();
				CharacterWrapper character = new CharacterWrapper(rc.getGameObject());
				Collection curses = character.getAllCurses();
				if (curses.size()>0) {
					for (java.util.Iterator _j14it861 = (curses).iterator(); _j14it861.hasNext(); ) {
					  String curse = (String) _j14it861.next();
						identifiers.add(curse);
						gameObjects.add(rc.getGameObject());
					}
				}
			}
		}
		if (spell.getGameObject().hasThisAttribute(Constants.TARGETS_SITES_FREED_SPELL)) {
			for (java.util.Iterator _j14it862 = (battleModel.getBattleLocation().clearing.getClearingComponents()).iterator(); _j14it862.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it862.next();
				if (rc.isTreasureLocation() && spell.getCaster().hasTreasureLocationDiscovery(rc.toString())) {
					for (java.util.Iterator _j14it863 = (rc.getHold()).iterator(); _j14it863.hasNext(); ) {
					  GameObject held = (GameObject) _j14it863.next();
						if (held.hasThisAttribute(RealmComponent.SPELL)) {
							gameObjects.add(rc.getGameObject());
							break;
						}
					}
				}
			}
		}
		return true;
	}
}