package com.robin.magic_realm.components.effect;

import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class DiscoverRoadEffect implements ISpellEffect {

	public void apply(SpellEffectContext context) {
		CharacterWrapper character = context.getCharacterTarget();
	
		for (java.util.Iterator _j14it2037 = (character.getCurrentClearing().getConnectedPaths()).iterator(); _j14it2037.hasNext(); ) {
		  com.robin.magic_realm.components.PathDetail path = (com.robin.magic_realm.components.PathDetail) _j14it2037.next();
			character.addPathKnowledge(path);
		}
	}

	public void unapply(SpellEffectContext context) {
	}

}
