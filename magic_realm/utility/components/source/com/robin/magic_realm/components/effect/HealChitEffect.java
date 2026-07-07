package com.robin.magic_realm.components.effect;

import com.robin.magic_realm.components.CharacterActionChitComponent;
import com.robin.magic_realm.components.RealmComponent;

public class HealChitEffect implements ISpellEffect {

	public void apply(SpellEffectContext context) {
		for (java.util.Iterator _j14it2027 = (context.Spell.getTargets()).iterator(); _j14it2027.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it2027.next();
			CharacterActionChitComponent chit = (CharacterActionChitComponent)rc;
			chit.makeActive();
		}
	}

	public void unapply(SpellEffectContext context) {
	}

}
