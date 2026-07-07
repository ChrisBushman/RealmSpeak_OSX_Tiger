package com.robin.magic_realm.RealmBattle.targeting;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingOtherArtifact extends SpellTargetingSingle {

	protected SpellTargetingOtherArtifact(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {		
		for (java.util.Iterator _j14it804 = (combatFrame.findCanBeSeen(battleModel.getAllBattleParticipants(true),true)).iterator(); _j14it804.hasNext(); ) {
		  RealmComponent participant = (RealmComponent) _j14it804.next();
			if (!participant.isCharacter()) continue;
			CharacterWrapper character = new CharacterWrapper(participant.getGameObject());
			for (java.util.Iterator _j14it805 = (character.getInventory()).iterator(); _j14it805.hasNext(); ) {
			  GameObject item = (GameObject) _j14it805.next();
				if (item.hasThisAttribute("artifact") || item.hasThisAttribute("book")) {
					RealmComponent rc = RealmComponent.getRealmComponent(item);
					if (!rc.isEnchanted()) {
						gameObjects.add(item);
					}
				}
			}
		}
		return true;
	}
}