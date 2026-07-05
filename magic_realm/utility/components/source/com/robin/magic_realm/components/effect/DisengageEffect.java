package com.robin.magic_realm.components.effect;

import java.util.ArrayList;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.wrapper.CombatWrapper;

public class DisengageEffect implements ISpellEffect {

	@Override
	public void apply(SpellEffectContext context) {
		CombatWrapper combat = context.getCombatTarget();

		// Remove all attackers and targets
		ArrayList<GameObject> attackers = combat.getAttackers();

		for (GameObject a : attackers) {
			RealmComponent.getRealmComponent(a).clearTargets();
		}

		for (GameObject a : attackers) {
			CombatWrapper cw = new CombatWrapper(a);
			if (cw.getAttackerCount() > 0) {
				cw.setSheetOwner(true);
			}
		}

		combat.removeAllAttackers();
	}

	@Override
	public void unapply(SpellEffectContext context) {
	}

}
