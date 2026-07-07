package com.robin.magic_realm.RealmBattle.targeting;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingDeadMonster extends SpellTargetingSingle {

	public SpellTargetingDeadMonster(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		for (java.util.Iterator _j14it855 = (battleModel.getAllParticipatingCharacters()).iterator(); _j14it855.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it855.next();
			CharacterWrapper character = new CharacterWrapper(rc.getGameObject());
			for (java.util.Iterator _j14it856 = (character.getKills(character.getCurrentDayKey())).iterator(); _j14it856.hasNext(); ) {
			  GameObject go = (GameObject) _j14it856.next();
				RealmComponent kill = RealmComponent.getRealmComponent(go);
				if (kill.isMonster() && !go.hasThisAttribute(Constants.DEAD_PERMANENT)) {
					gameObjects.add(go);
				}
			}
		}
		return true;
	}
}