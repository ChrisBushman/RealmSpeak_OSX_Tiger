package com.robin.magic_realm.RealmBattle.targeting;

import java.util.ArrayList;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingArtifact extends SpellTargetingSingle {

	protected SpellTargetingArtifact(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		// Assume that activeParticipant IS character
		CharacterWrapper character = new CharacterWrapper(activeParticipant.getGameObject());
		secondaryTargetChoiceString = "Select a spell to enchant artifact with:";
		ArrayList spellPossibilities = new ArrayList(character.getAllSpells());
		// Eliminate the casting spell?  Maybe not...
		if (spellPossibilities.size()>0) { // can't enchant an artifact with a recorded spell, if you have none!
			for (java.util.Iterator _j14it841 = (character.getInventory()).iterator(); _j14it841.hasNext(); ) {
			  GameObject item = (GameObject) _j14it841.next();
				if (item.hasThisAttribute("artifact") || item.hasThisAttribute("book")) {
					RealmComponent rc = RealmComponent.getRealmComponent(item);
					if (!rc.isEnchanted()) {
						identifiers.add("");
						gameObjects.add(item);
						secondaryTargets.put("",spellPossibilities); // the spell possibilities are the same each time
					}
				}
			}
		}
		return true;
	}
}