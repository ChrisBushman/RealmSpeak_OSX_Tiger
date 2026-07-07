package com.robin.magic_realm.components.effect;

import java.util.ArrayList;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.utility.RealmLogging;
import com.robin.magic_realm.components.wrapper.CombatWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class PeaceEffect implements ISpellEffect {

	public void apply(SpellEffectContext context) {
		CombatWrapper combat = context.getCombatTarget();
		
		boolean attacked = false;
		ArrayList attackers = combat.getAttackers();
		for (java.util.Iterator _j14it2032 = (attackers).iterator(); _j14it2032.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2032.next();
			if (!go.equals(context.Caster)) {
				attacked = true;
			}
		}
		if (attacked) {
			RealmLogging.logMessage(
					context.Caster.getName(),
					context.Spell.getGameObject().getName()+" was cancelled because the "
					+ context.Target.getGameObject().getName()
					+" is being attacked by someone other than the "+ context.Caster.getName()+"!");
		}
		else {
			combat.setPeace(true);
			context.Target.clearTargets();
			if (context.Target.isCharacter()) {
				// Cancel any cast spells
				GameObject go = combat.getCastSpell();
				if (go!=null) {
					SpellWrapper spell = new SpellWrapper(go);
					spell.cancelSpell();
					RealmLogging.logMessage(
							spell.getCaster().getGameObject().getName(),
							spell.getGameObject().getName()+" was cancelled because of PEACE spell!");
				}
			}
		}
	}

	public void unapply(SpellEffectContext context) {
		//Rulebook: This 'peace' cannot be broken before it expires.
	}

}
