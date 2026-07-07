package com.robin.magic_realm.components.effect;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class FreeSpellEffect implements ISpellEffect {
	
	public void apply(SpellEffectContext context) {
		for (java.util.Iterator _j14it2061 = (context.Target.getHold()).iterator(); _j14it2061.hasNext(); ) {
		  GameObject held = (GameObject) _j14it2061.next();
			if (held.hasThisAttribute(RealmComponent.SPELL)) {
				SpellWrapper targetSpell = new SpellWrapper(held);
				context.Spell.getGameObject().setThisAttribute(Constants.FREED_SPELL,held.getStringId());
				GameObject incantationObject = context.Spell.getIncantationObject();
				if (incantationObject!=null) {
					incantationObject.setThisAttribute(Constants.MAGIC_CHANGE_BY_FREE_SPELL, targetSpell.getCastMagicType());
				}
				return;
			}
		}		
	}

	public void unapply(SpellEffectContext context) {
		context.Spell.getGameObject().removeThisAttribute(Constants.FREED_SPELL);
		GameObject incantationObject = context.Spell.getIncantationObject();
		if (incantationObject!=null) {
			incantationObject.removeThisAttribute(Constants.MAGIC_CHANGE_BY_FREE_SPELL);
		}
	}

}
