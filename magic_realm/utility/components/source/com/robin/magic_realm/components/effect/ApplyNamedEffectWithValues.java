package com.robin.magic_realm.components.effect;

import com.robin.magic_realm.components.utility.SpellUtility;

public class ApplyNamedEffectWithValues implements ISpellEffect {
	String _effectName;
	
	public ApplyNamedEffectWithValues(String effectName){
		_effectName = effectName;
	}
	
	public void apply(SpellEffectContext context) {
		SpellUtility.ApplyNamedSpellEffectWithValuesToTarget(_effectName, context.Target.getGameObject(), context.Spell, context.Spell.getGameObject().getThisAttributeList(_effectName));
	}

	public void unapply(SpellEffectContext context) {
		if(context.Target.getGameObject().hasThisAttribute(_effectName)){
			for (java.util.Iterator _j14it2051 = (context.Spell.getGameObject().getThisAttributeList(_effectName)).iterator(); _j14it2051.hasNext(); ) {
			  String value = (String) _j14it2051.next();
				context.Target.getGameObject().removeThisAttributeListItem(_effectName, value);
			}
			if (context.Target.getGameObject().getThisAttributeList(_effectName).isEmpty()) {
				context.Target.getGameObject().removeThisAttribute(_effectName);
			}
		}
	}

}
