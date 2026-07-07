package com.robin.magic_realm.RealmBattle.targeting;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingKilledDenizen extends SpellTargetingSingle {

	public SpellTargetingKilledDenizen(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		for (java.util.Iterator _j14it854 = (battleModel.getKilledObjects()).iterator(); _j14it854.hasNext(); ) {
		  GameObject go = (GameObject) _j14it854.next();
			if (RealmComponent.getRealmComponent(go).isDenizen() && !go.hasThisAttribute(Constants.DEAD_PERMANENT)) {
				gameObjects.add(go);
			}
		}
		return true;
	}
}