package com.robin.magic_realm.RealmBattle.targeting;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.RealmBattle.*;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingWeaponOrDenizenWeapon extends SpellTargetingSingle {

	protected SpellTargetingWeaponOrDenizenWeapon(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		for (java.util.Iterator _j14it849 = (combatFrame.findCanBeSeen(battleModel.getAllBattleParticipants(true),true)).iterator(); _j14it849.hasNext(); ) {
		  RealmComponent participant = (RealmComponent) _j14it849.next();
			if (participant.isCharacter()) {
				CharacterWrapper character = new CharacterWrapper(participant.getGameObject());
				if (character.isMistLike()) continue;
				for (java.util.Iterator _j14it850 = (character.getActiveInventory()).iterator(); _j14it850.hasNext(); ) {
				  GameObject go = (GameObject) _j14it850.next();
					RealmComponent itemRc = RealmComponent.getRealmComponent(go);
					if (itemRc.isWeapon()) {
						gameObjects.add(go);
					}
				}
			} else if (participant.isMonster() || participant.isNative()) {
				if (participant.getGameObject().hasThisAttribute(Constants.WEAPON_USE) && !participant.getGameObject().hasThisAttribute(Constants.NO_WEAPON_USAGE)) {
					gameObjects.add(participant.getGameObject());
				}
				for (java.util.Iterator _j14it851 = (participant.getHold()).iterator(); _j14it851.hasNext(); ) {
				  GameObject held = (GameObject) _j14it851.next();
					if (held.hasThisAttribute(Constants.MONSTER_WEAPON)
							|| held.hasThisAttribute(Constants.GIANT_CLUB)
							|| held.hasThisAttribute(Constants.GIANT_AXE)) {
						gameObjects.add(held);
					}
				}
			}
		}
		return true;
	}
}