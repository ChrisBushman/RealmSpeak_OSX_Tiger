package com.robin.magic_realm.components.effect;

public class FlyStrengthEffect implements ISpellEffect {

	public void apply(SpellEffectContext context) {
		context.Target.getGameObject().setThisAttribute("fly_strength",context.Spell.getGameObject().getThisAttribute("fly_strength"));
		context.Target.getGameObject().setThisAttribute("fly_speed",context.Spell.getGameObject().getThisAttribute("fly_speed"));
	}

	public void unapply(SpellEffectContext context) {
		context.Target.getGameObject().removeThisAttribute("fly_strength");
		context.Target.getGameObject().removeThisAttribute("fly_speed");
	}

}
