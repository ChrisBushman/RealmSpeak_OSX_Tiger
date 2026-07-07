package com.robin.magic_realm.RealmBattle.targeting;

import java.util.ArrayList;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.RealmBattle.CombatSheet;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingIndividualHorseHound extends SpellTargetingSingle {	
	public SpellTargetingIndividualHorseHound(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		ArrayList potentialTargets = combatFrame.findCanBeSeen(battleModel.getAllBattleParticipants(true),true);
		potentialTargets = CombatSheet.filterNativeFriendly(activeParticipant, potentialTargets);
		for (java.util.Iterator _j14it834 = (potentialTargets).iterator(); _j14it834.hasNext(); ) {
		  RealmComponent participant = (RealmComponent) _j14it834.next();
			if (participant.getGameObject().hasThisAttribute(Constants.HOUND) && !participant.hasMagicColorImmunity(spell)) {
				gameObjects.add(participant.getGameObject());
				continue;
			}

			if (participant.isCharacter()) {
				CharacterWrapper character = new CharacterWrapper(participant.getGameObject());
				if (character.isMistLike()) continue;
				if (!participant.hasMagicProtection() && !participant.hasMagicColorImmunity(spell)) {
					gameObjects.add(participant.getGameObject());
				}
				for (java.util.Iterator _j14it835 = (character.getInventory()).iterator(); _j14it835.hasNext(); ) {
				  GameObject go = (GameObject) _j14it835.next();
					RealmComponent itemRc = RealmComponent.getRealmComponent(go);
					if ((itemRc.isHorse() || itemRc.isNativeHorse()) && itemRc.isActivated()) {
						gameObjects.add(go);
					}
				}
			} else if (participant.isMonster() || participant.isNative() || participant.isNativeHorse()) {
				if (!participant.hasMagicProtection() && !participant.hasMagicColorImmunity(spell)) {
					gameObjects.add(participant.getGameObject());
				}
			}
		}
		return true;
	}
}