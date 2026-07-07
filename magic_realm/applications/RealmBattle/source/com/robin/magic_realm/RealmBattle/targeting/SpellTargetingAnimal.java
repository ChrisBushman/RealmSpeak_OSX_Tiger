package com.robin.magic_realm.RealmBattle.targeting;

import java.util.ArrayList;

import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.RealmBattle.CombatSheet;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingAnimal extends SpellTargetingSingle {

	public SpellTargetingAnimal(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		ArrayList potentialTargets = combatFrame.findCanBeSeen(battleModel.getAllBattleParticipants(true),true);
		potentialTargets = CombatSheet.filterNativeFriendly(activeParticipant, potentialTargets);
		for (java.util.Iterator _j14it833 = (potentialTargets).iterator(); _j14it833.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it833.next();
			if (!rc.hasMagicProtection() && rc.getGameObject().hasThisAttribute("animal") && !rc.hasMagicColorImmunity(spell)) {
				gameObjects.add(rc.getGameObject());
			}
		}
		return true;
	}
}