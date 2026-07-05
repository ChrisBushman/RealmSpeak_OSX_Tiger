package com.robin.magic_realm.components.effect;

import com.robin.magic_realm.components.utility.Constants;

public class PhaseChitEffectFactory {	
	public static ISpellEffect[] create(String effect){
		if ("nullify".equals(effect.toLowerCase())) return new ISpellEffect[]{new NullifyEffect()};
		if ("magic_protection".equals(effect.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.MAGIC_PROTECTION)};
		if ("magic_protection_extended".equals(effect.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.MAGIC_PROTECTION_EXTENDED)};
		if ("blinding_light".equals(effect.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.BLINDING_LIGHT),new RemovePotentialBlocksEffect()};
		if ("holy_shield".equals(effect.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.HOLY_SHIELD)};
		if ("reserve".equals(effect.toLowerCase())) return new ISpellEffect[]{new MagicChitEffect()};
		if ("dark_favor".equals(effect.toLowerCase())) return new ISpellEffect[]{new DarkFavorEffect(), new ApplyNamedEffect(Constants.DARK_FAVOR)};
		if ("dazzle".equals(effect.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.DAZZLE)};
		if ("luck".equals(effect.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.LUCK)};
			
		return null;
	}
}