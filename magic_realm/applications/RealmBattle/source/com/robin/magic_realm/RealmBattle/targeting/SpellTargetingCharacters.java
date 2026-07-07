package com.robin.magic_realm.RealmBattle.targeting;

import java.util.ArrayList;

import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.attribute.Strength;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingCharacters extends SpellTargetingMultiple {
	
	private boolean lightOnly;

	public SpellTargetingCharacters(CombatFrame combatFrame, SpellWrapper spell,boolean lightOnly) {
		super(combatFrame, spell);
		this.lightOnly = lightOnly;
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		ArrayList allCharacters = combatFrame.findCanBeSeen(battleModel.getAllParticipatingCharactersAsRc(),true);
		for (java.util.Iterator _j14it802 = (allCharacters).iterator(); _j14it802.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it802.next();
			CharacterWrapper character = new CharacterWrapper(rc.getGameObject());
			if (!character.hasMagicProtection() && (!lightOnly || !character.getVulnerability().strongerThan(new Strength("L")))) {
				gameObjects.add(rc.getGameObject());
			}
		}
		return true;
	}
}