package com.robin.magic_realm.RealmBattle.targeting;

import java.util.ArrayList;

import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingSpiderOctopusScorpion extends SpellTargetingSingle {

	public SpellTargetingSpiderOctopusScorpion(CombatFrame combatFrame,SpellWrapper spell) {
		super(combatFrame, spell);
	}
	
	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		ArrayList allDenizens = combatFrame.findCanBeSeen(battleModel.getAllBattleParticipants(true),true);
		ArrayList allParticipantsSansDenizens = combatFrame.findCanBeSeen(battleModel.getAllBattleParticipants(false),true);
		allDenizens.removeAll(allParticipantsSansDenizens);
		for (java.util.Iterator _j14it887 = (allDenizens).iterator(); _j14it887.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it887.next();
			if (rc.isMonster() && !rc.isPlayerControlledLeader() && !rc.hasMagicProtection() && !rc.hasMagicColorImmunity(spell)) {
				if (rc.getGameObject().hasThisAttribute(Constants.SPIDER) || rc.getGameObject().hasThisAttribute(Constants.OCTOPUS) || rc.getGameObject().hasThisAttribute(Constants.SCORPION)) {
					gameObjects.add(rc.getGameObject());
				}
			}
		}
		return true;
	}
}