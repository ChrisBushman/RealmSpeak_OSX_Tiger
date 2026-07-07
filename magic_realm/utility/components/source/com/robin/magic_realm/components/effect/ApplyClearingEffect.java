package com.robin.magic_realm.components.effect;

public class ApplyClearingEffect implements ISpellEffect {
	String _effect;
	
	public ApplyClearingEffect(String effect){
		_effect = effect;
	}
	
	public void apply(SpellEffectContext context) {
		context.getClearingTarget().addSpellEffect(_effect);
	}

	public void unapply(SpellEffectContext context) {
		context.getClearingTarget().removeSpellEffect(_effect);
	}

}
