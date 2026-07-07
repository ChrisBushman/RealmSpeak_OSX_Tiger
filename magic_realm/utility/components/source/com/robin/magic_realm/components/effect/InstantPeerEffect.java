package com.robin.magic_realm.components.effect;

public class InstantPeerEffect implements ISpellEffect {

	public void apply(SpellEffectContext context) {
		context.getCharacterTarget().setDoInstantPeer(true);
	}

	public void unapply(SpellEffectContext context) {
	}

}
