package com.robin.magic_realm.RealmBattle.targeting;

import java.util.ArrayList;

import com.robin.magic_realm.RealmBattle.BattleGroup;
import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.RealmBattle.CombatSheet;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingAttacker extends SpellTargetingSingle {

	public SpellTargetingAttacker(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		BattleGroup bg = battleModel.getParticipantsBattleGroup(activeParticipant);
		ArrayList otherOpponents = combatFrame.findCanBeSeen(battleModel.getAllOtherBattleParticipants(bg,true,combatFrame.allowsTreachery()),true);
		otherOpponents = CombatSheet.filterNativeFriendly(activeParticipant, otherOpponents);
		for (java.util.Iterator _j14it795 = (otherOpponents).iterator(); _j14it795.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it795.next();
			if (!rc.hasMagicProtection() && !rc.hasMagicColorImmunity(spell)) {
				gameObjects.add(rc.getGameObject());
			}
		}
		return true;
	}
}