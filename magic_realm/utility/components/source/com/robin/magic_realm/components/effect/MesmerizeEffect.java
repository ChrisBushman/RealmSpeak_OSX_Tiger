package com.robin.magic_realm.components.effect;

import com.robin.magic_realm.components.table.Mesmerize;

public class MesmerizeEffect implements ISpellEffect {

	public void apply(SpellEffectContext context) {
		int d = context.Spell.getRedDieLock();
		Mesmerize.doNow(context.Parent,context.Spell.getCaster().getGameObject(),context.Target.getGameObject(),true,d);
	}

	public void unapply(SpellEffectContext context) {
	}

}
