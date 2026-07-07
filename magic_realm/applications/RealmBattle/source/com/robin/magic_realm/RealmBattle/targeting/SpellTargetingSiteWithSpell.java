package com.robin.magic_realm.RealmBattle.targeting;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingSiteWithSpell extends SpellTargetingSingle {
	
	public SpellTargetingSiteWithSpell(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		TileLocation loc = battleModel.getBattleLocation();
		CharacterWrapper caster = spell.getCaster();
		
		for (java.util.Iterator _j14it797 = (loc.clearing.getClearingComponents()).iterator(); _j14it797.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it797.next();
			if (rc.isTreasureLocation() && caster.hasTreasureLocationDiscovery(rc.toString())) {
				for (java.util.Iterator _j14it798 = (rc.getHold()).iterator(); _j14it798.hasNext(); ) {
				  GameObject held = (GameObject) _j14it798.next();
					if (held.hasThisAttribute(RealmComponent.SPELL)) {
						gameObjects.add(rc.getGameObject());
						break;
					}
				}
			}
		}
		return true;
	}
}