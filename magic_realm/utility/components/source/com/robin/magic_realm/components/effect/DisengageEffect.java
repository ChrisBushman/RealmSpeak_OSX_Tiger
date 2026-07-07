package com.robin.magic_realm.components.effect;

import java.util.ArrayList;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.wrapper.CombatWrapper;

public class DisengageEffect implements ISpellEffect {

	public void apply(SpellEffectContext context) {
		CombatWrapper combat = context.getCombatTarget();

		// Remove all attackers and targets
		ArrayList attackers = combat.getAttackers();

		for (java.util.Iterator _j14it2046 = (attackers).iterator(); _j14it2046.hasNext(); ) {
		  GameObject a = (GameObject) _j14it2046.next();
			RealmComponent.getRealmComponent(a).clearTargets();
		}

		for (java.util.Iterator _j14it2047 = (attackers).iterator(); _j14it2047.hasNext(); ) {
		  GameObject a = (GameObject) _j14it2047.next();
			CombatWrapper cw = new CombatWrapper(a);
			if (cw.getAttackerCount() > 0) {
				cw.setSheetOwner(true);
			}
		}

		combat.removeAllAttackers();
	}

	public void unapply(SpellEffectContext context) {
	}

}
