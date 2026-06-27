package com.robin.magic_realm.components.effect;

import java.util.Iterator;

import com.robin.magic_realm.components.PathDetail;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class DiscoverRoadEffect implements ISpellEffect {

	@Override
	public void apply(SpellEffectContext context) {
		CharacterWrapper character = context.getCharacterTarget();
		for (Iterator i = character.getCurrentClearing().getConnectedPaths().iterator(); i.hasNext();) {
			character.updatePathKnowledge((PathDetail) i.next());
		}
	}

	@Override
	public void unapply(SpellEffectContext context) {
		// TODO Auto-generated method stub

	}

}
