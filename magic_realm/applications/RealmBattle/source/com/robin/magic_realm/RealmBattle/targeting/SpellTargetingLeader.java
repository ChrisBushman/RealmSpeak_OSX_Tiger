package com.robin.magic_realm.RealmBattle.targeting;

import java.util.ArrayList;

import com.robin.magic_realm.RealmBattle.*;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingLeader extends SpellTargetingSingle {

	public SpellTargetingLeader(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		ArrayList potentialTargets = combatFrame.findCanBeSeen(battleModel.getAllBattleParticipants(false),true);
		potentialTargets = CombatSheet.filterNativeFriendly(activeParticipant, potentialTargets);
		for (java.util.Iterator _j14it780 = (potentialTargets).iterator(); _j14it780.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it780.next();
			if (rc.isAnyLeader() && !rc.hasMagicProtection() && !rc.hasMagicColorImmunity(spell)) {
				gameObjects.add(rc.getGameObject());
			}
		}
		return true;
	}
}