package com.robin.magic_realm.components.utility;

import java.util.*;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.*;
import com.robin.magic_realm.components.attribute.Speed;
import com.robin.magic_realm.components.attribute.Strength;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.CombatWrapper;

public class DemonsEffects {	
	public static String getDestClientName(GameObject attacker,GameObject target) {
		RealmComponent attackerRc = RealmComponent.getRealmComponent(attacker);
		RealmComponent targetRc = RealmComponent.getRealmComponent(target);
		// Determine the destination client
		RealmComponent destOwner = attackerRc.getOwner();
		if (destOwner==null) {
			destOwner = targetRc.getOwner();
		}
		// destOwner should NOT be null at this point!  One or the other HAS to be owned // if monster attacks another monster it is null (e.g. duel spell)
		if (destOwner==null) {
			return attackerRc.getName();
		}
		CharacterWrapper destCharacter = new CharacterWrapper(destOwner.getGameObject());
		return destCharacter.getPlayerName();
	}
	public static String getKilledString(ArrayList killed) {
		StringBuffer string = new StringBuffer();
		if (!killed.isEmpty()) {
			string.append("\n\n");
			for (java.util.Iterator _j14it2688 = (killed).iterator(); _j14it2688.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it2688.next();
				string.append("    ");
				string.append(rc.getGameObject().getName());
				string.append(" was killed.\n");
			}
		}
		return string.toString();
	}
	public static ArrayList killEverythingInClearing(CharacterWrapper character,Strength power,boolean hiddenAreSafe,boolean charactersAreSafe,Speed speed,GameObject caster, boolean makeDeadWhenKilled, ArrayList kills) {
		ArrayList killed = new ArrayList();
		TileLocation tl = character.getCurrentLocation();
		if (tl.isInClearing()) {
			HashSet livingThings = new HashSet();
			for (java.util.Iterator _j14it2689 = (tl.clearing.getClearingComponents()).iterator(); _j14it2689.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it2689.next();
				if (rc.isPlayerControlledLeader()) {
					livingThings.add(rc);
					CharacterWrapper aChar = new CharacterWrapper(rc.getGameObject());
					livingThings.addAll(aChar.getFollowingHirelings());
				}
				if (rc.isNative() || rc.isHorse() || rc.isMonster()) {
					livingThings.add(rc);
				}
			}
			for (java.util.Iterator _j14it2690 = (livingThings).iterator(); _j14it2690.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it2690.next();
				if (!rc.isMistLike()) {
					if (!hiddenAreSafe || !rc.isHidden()) {
						Strength strength = new Strength(rc.getGameObject().getThisAttribute("vulnerability"));
						if (rc.isCharacter()) {
							CharacterChitComponent achar = (CharacterChitComponent)rc;
							MonsterChitComponent transform = achar.getTransmorphedComponent();
							if (transform!=null) {
								strength = new Strength(transform.getGameObject().getThisAttribute("vulnerability"));
							}
							else if (charactersAreSafe) {
								strength = new Strength("X");
							}
						}
						if (power.strongerThan(strength)) {
							kill(rc.getGameObject(),speed,caster,makeDeadWhenKilled,kills);
							killed.add(rc);
						}
					}
				}
			}
		}
		
		return killed;
	}
	public static void kill(GameObject go,Speed speed, GameObject caster, boolean makeDeadWhenKilled, ArrayList kills) {
		RealmComponent attacker = RealmComponent.getRealmComponent(caster); 
		RealmComponent victim = RealmComponent.getRealmComponent(go);
		BattleUtility.handleSpoilsOfWar(attacker,victim);
		
		kills.add(go);
		
		if (makeDeadWhenKilled) {
			RealmUtility.makeDead(RealmComponent.getRealmComponent(go),speed.getNum());
		}
		else {
			CombatWrapper combat = new CombatWrapper(go);
			combat.setKilledBy(caster);
			combat.setKilledLength(new Integer(17));
			combat.setKilledSpeed(speed);
		}
	}
}