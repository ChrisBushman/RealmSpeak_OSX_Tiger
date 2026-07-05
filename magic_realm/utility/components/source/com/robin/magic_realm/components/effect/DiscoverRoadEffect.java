package com.robin.magic_realm.components.effect;

import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class DiscoverRoadEffect implements ISpellEffect {

	@Override
	public void apply(SpellEffectContext context) {
		CharacterWrapper character = context.getCharacterTarget();
	
		for (com.robin.magic_realm.components.PathDetail path : character.getCurrentClearing().getConnectedPaths()) {
			character.addPathKnowledge(path);
		}
	}

	@Override
	public void unapply(SpellEffectContext context) {
	}

}
