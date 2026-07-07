package com.robin.magic_realm.components.effect;

import com.robin.magic_realm.components.ClearingDetail;

public class LightEffect implements ISpellEffect {

	public void apply(SpellEffectContext context) {
		ClearingDetail clearing = context.getClearingTarget();
		clearing.setLighted(true);
	}

	public void unapply(SpellEffectContext context) {
		ClearingDetail clearing = context.getClearingTarget();
		clearing.setLighted(false);
	}
	
}
