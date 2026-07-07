package com.robin.magic_realm.RealmBattle.targeting;

import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.components.CharacterActionChitComponent;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingChit extends SpellTargetingSingle {
	private String action;
	protected SpellTargetingChit(CombatFrame combatFrame, SpellWrapper spell, String action) {
		super(combatFrame, spell);
		this.action = action;
	}
	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		CharacterWrapper caster = spell.getCaster();
		for (java.util.Iterator _j14it848 = (caster.getAllChits()).iterator(); _j14it848.hasNext(); ) {
		  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it848.next();
			if (chit.getAction().equals(action)) {
				gameObjects.add(chit.getGameObject());
			}
		}
		return true;
	}
}