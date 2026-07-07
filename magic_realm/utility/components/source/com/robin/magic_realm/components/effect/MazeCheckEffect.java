package com.robin.magic_realm.components.effect;

import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class MazeCheckEffect implements ISpellEffect {

	public void apply(SpellEffectContext context) {
		if (context.Target.isCharacter()) {
			CharacterWrapper character = new CharacterWrapper(context.Target.getGameObject());
			character.checkForLostInTheMaze(character.getCurrentLocation());
		}
	}

	public void unapply(SpellEffectContext context) {
	}

}
