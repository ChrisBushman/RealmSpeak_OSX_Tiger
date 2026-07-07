package com.robin.magic_realm.RealmBattle.targeting;

import java.util.ArrayList;

import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingMonster extends SpellTargetingSingle {

	public SpellTargetingMonster(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		ArrayList allBattleParticipants = combatFrame.findCanBeSeen(battleModel.getAllBattleParticipants(true),true);
		for (java.util.Iterator _j14it891 = (allBattleParticipants).iterator(); _j14it891.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it891.next();
			if (rc.isMonster() && !rc.hasMagicProtection() && !rc.hasMagicColorImmunity(spell)) {
				gameObjects.add(rc.getGameObject());
			}
		}
		return true;
	}
}