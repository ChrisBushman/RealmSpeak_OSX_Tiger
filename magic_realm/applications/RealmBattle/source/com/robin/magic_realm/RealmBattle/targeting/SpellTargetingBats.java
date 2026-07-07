package com.robin.magic_realm.RealmBattle.targeting;

import java.util.ArrayList;

import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingBats extends SpellTargetingAll {

	public SpellTargetingBats(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}
	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		ArrayList allBattleParticipants = combatFrame.findCanBeSeen(battleModel.getAllBattleParticipants(true),true);
		for (java.util.Iterator _j14it803 = (allBattleParticipants).iterator(); _j14it803.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it803.next();
			if (rc.isMonster() && !rc.isPlayerControlledLeader() && !rc.hasMagicProtection() && !rc.hasMagicColorImmunity(spell)) {
				if (rc.getGameObject().hasThisAttribute(Constants.BAT)) {
					gameObjects.add(rc.getGameObject());
				}
			}
		}
		return true;
	}
}