package com.robin.magic_realm.components.effect;

import com.robin.magic_realm.components.wrapper.CombatWrapper;

public class SleepEffect implements ISpellEffect {

	public void apply(SpellEffectContext context) {
		CombatWrapper tile = context.getCombatTarget();
		String clearing = context.Spell.getExtraIdentifier();
		tile.addSleepClearing(Integer.parseInt(clearing));
	}

	public void unapply(SpellEffectContext context) {
	}

}
