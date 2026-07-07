package com.robin.magic_realm.RealmBattle.targeting;

import java.util.ArrayList;

import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.RealmBattle.CombatSheet;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingHumanGroup extends SpellTargetingSingle {

	public SpellTargetingHumanGroup(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		// Giants, or Ogres, or Native Group
		ArrayList potentialTargets = battleModel.getAllBattleParticipants(true);
		potentialTargets = CombatSheet.filterNativeFriendly(activeParticipant, potentialTargets);
		String ownerId = activeParticipant.getGameObject().getStringId();
		for (java.util.Iterator _j14it793 = (potentialTargets).iterator(); _j14it793.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it793.next();
			if (!rc.isCharacter() && !rc.hasMagicProtection() && !rc.hasMagicColorImmunity(spell) && (rc.getOwnerId()==null || rc.getOwnerId().equals(ownerId))) {
				String groupName = null;
				if (rc.isMonster() && !rc.isPlayerControlledLeader()) {
					if (rc.getGameObject().hasThisAttribute(Constants.OGRE)) {
						groupName="Ogres";
					}
					if (rc.getGameObject().hasThisAttribute(Constants.GIANT)) {
						groupName="Giants";
					}
					if (rc.getGameObject().hasThisAttribute(Constants.FROST_GIANT)) {
						groupName="Frost Giants";
					}
				}
				else if (rc.isNative()) {
					groupName = rc.getGameObject().getAttribute(rc.getThisBlock(),"native");
				}
				
				if (groupName!=null) {
					ArrayList list = (ArrayList) secondaryTargets.get(groupName);
					if (list==null) {
						list = new ArrayList();
						secondaryTargets.put(groupName,list);
					}
					list.add(rc);
				}
			}
		}
		return true;
	}
}