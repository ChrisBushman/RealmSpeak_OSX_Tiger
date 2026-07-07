package com.robin.magic_realm.components.effect;

public class FinalChitSpeedEffect implements ISpellEffect {

	public void apply(SpellEffectContext context) {
		context.Spell.getCaster().updateChitEffects();
	}

	public void unapply(SpellEffectContext context) {
	}

}
