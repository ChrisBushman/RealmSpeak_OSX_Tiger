package com.robin.magic_realm.RealmBattle.targeting;

import java.util.ArrayList;

import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingUncontrolledMonsters extends SpellTargetingSingle {

	public SpellTargetingUncontrolledMonsters(CombatFrame combatFrame,SpellWrapper spell) {
		super(combatFrame, spell);
	}
	
	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		ArrayList allDenizens = combatFrame.findCanBeSeen(battleModel.getAllBattleParticipants(true),true);
		ArrayList allParticipantsSansDenizens = combatFrame.findCanBeSeen(battleModel.getAllBattleParticipants(false),true);
		allDenizens.removeAll(allParticipantsSansDenizens);
		String validTargets = spell.getGameObject().getThisAttribute("targeted_monsters");
		String[] targetNames = validTargets.split(",");
		for (java.util.Iterator _j14it783 = (allDenizens).iterator(); _j14it783.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it783.next();
			if (rc.isMonster() && !rc.isPlayerControlledLeader() && !rc.hasMagicProtection() && !rc.hasMagicColorImmunity(spell)) {
				String name = rc.getGameObject().getName().toLowerCase();
				for (int _j14i784 = 0; _j14i784 < targetNames.length; _j14i784++) {
				  String targetName = targetNames[_j14i784];
					if (name.indexOf(targetName.trim()) >= 0 || rc.getGameObject().hasThisAttribute(targetName)) {
						gameObjects.add(rc.getGameObject());
						continue;
					}
				}
			}
		}
		return true;
	}
}