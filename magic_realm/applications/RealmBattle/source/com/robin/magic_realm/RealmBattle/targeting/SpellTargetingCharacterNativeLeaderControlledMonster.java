package com.robin.magic_realm.RealmBattle.targeting;

import java.util.ArrayList;

import com.robin.magic_realm.RealmBattle.*;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingCharacterNativeLeaderControlledMonster extends SpellTargetingSingle {

	protected SpellTargetingCharacterNativeLeaderControlledMonster(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		ArrayList potentialTargets = combatFrame.findCanBeSeen(battleModel.getAllBattleParticipants(true),true);
		potentialTargets = CombatSheet.filterNativeFriendly(activeParticipant, potentialTargets);
		for (java.util.Iterator _j14it866 = (potentialTargets).iterator(); _j14it866.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it866.next();
			if ((rc.isCharacter() || rc.isNativeLeader() || rc.isControlledMonster())
			 && !rc.hasMagicProtection() && !rc.hasMagicColorImmunity(spell)) {
				gameObjects.add(rc.getGameObject());
			}
		}
		return true;
	}
}