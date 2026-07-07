package com.robin.magic_realm.components.effect;

import com.robin.magic_realm.components.wrapper.CombatWrapper;

public class UnassignEffect implements ISpellEffect {

	public void apply(SpellEffectContext context) {
		context.Target.clearTargets();
		CombatWrapper aCombat = new CombatWrapper(context.Target.getGameObject());
		aCombat.setSheetOwner(false);
	}

	public void unapply(SpellEffectContext context) {
	}

}
