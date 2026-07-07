package com.robin.magic_realm.RealmBattle.targeting;

import java.util.ArrayList;

import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.components.CharacterActionChitComponent;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingHurtChit extends SpellTargetingMultiple {
	protected SpellTargetingHurtChit(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		// Assume that activeParticipant IS character
		CharacterWrapper character = new CharacterWrapper(activeParticipant.getGameObject());
		ArrayList hurtChits = new ArrayList();
		hurtChits.addAll(character.getFatiguedChits());
		hurtChits.addAll(character.getWoundedChits());
		for (java.util.Iterator _j14it829 = (hurtChits).iterator(); _j14it829.hasNext(); ) {
		  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it829.next();
			gameObjects.add(chit.getGameObject());
		}
		return true;
	}
}