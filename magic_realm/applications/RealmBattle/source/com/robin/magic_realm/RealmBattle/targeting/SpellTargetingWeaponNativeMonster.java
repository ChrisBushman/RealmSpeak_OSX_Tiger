package com.robin.magic_realm.RealmBattle.targeting;

import java.util.ArrayList;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.RealmBattle.*;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingWeaponNativeMonster extends SpellTargetingSingle {

	protected SpellTargetingWeaponNativeMonster(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		ArrayList potentialTargets = combatFrame.findCanBeSeen(battleModel.getAllBattleParticipants(true),true);
		potentialTargets = CombatSheet.filterNativeFriendly(activeParticipant, potentialTargets);
		for (java.util.Iterator _j14it889 = (potentialTargets).iterator(); _j14it889.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it889.next();
			if ((rc.isNative() || rc.isMonster())
			 && !rc.hasMagicProtection() && !rc.hasMagicColorImmunity(spell)
			 && !rc.getGameObject().hasThisAttribute(Constants.ANOMALY) && !rc.getGameObject().hasThisAttribute(Constants.TITAN)) {
				gameObjects.add(rc.getGameObject());
			}
			if (rc.isCharacter()) {
				for (java.util.Iterator _j14it890 = (rc.getHold()).iterator(); _j14it890.hasNext(); ) {
				  GameObject go = (GameObject) _j14it890.next();
					if (RealmComponent.getRealmComponent(go).isWeapon() || (RealmComponent.getRealmComponent(go).isTreasure() && go.hasThisAttribute(RealmComponent.WEAPON) && !spell.getGameObject().hasThisAttribute(NON_TREASURE_WEAPON))) {
						gameObjects.add(go);
					}
				}
			}
		}
		return true;
	}
}