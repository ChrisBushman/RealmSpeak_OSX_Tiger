package com.robin.magic_realm.components.effect;

import com.robin.magic_realm.components.table.PowerOfThePit;

public class PowerPitEffect implements ISpellEffect {

	public void apply(SpellEffectContext context) {
		int d = context.Spell.getRedDieLock();
		PowerOfThePit.doNow(context.Parent,context.Spell.getCaster().getGameObject(),context.Target.getGameObject(),true,d,context.Spell.getAttackSpeed());
	}

	public void unapply(SpellEffectContext context) {
	}

}
