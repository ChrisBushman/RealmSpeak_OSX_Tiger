package com.robin.magic_realm.components.effect;

import java.util.ArrayList;

import com.robin.game.objects.GameData;
import com.robin.game.objects.GameObject;
import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.BattleHorse;
import com.robin.magic_realm.components.CharacterActionChitComponent;
import com.robin.magic_realm.components.MonsterChitComponent;
import com.robin.magic_realm.components.MonsterPartChitComponent;
import com.robin.magic_realm.components.NativeChitComponent;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.RealmLogging;
import com.robin.magic_realm.components.utility.RealmUtility;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.CombatWrapper;
import com.robin.magic_realm.components.wrapper.HostPrefWrapper;

public class SpiderWebEffect implements ISpellEffect {

	public void apply(SpellEffectContext context) {
		RealmComponent target = context.Target;
		GameObject caster = context.Caster;
		GameData gameData = context.getGameData();
		applySpiderWebEffect(target,caster,gameData);
	}
	public static void applySpiderWebEffect(RealmComponent target, GameObject caster, GameData gameData) {
		CombatWrapper cw = new CombatWrapper(target.getGameObject());
		ArrayList attackBoxes = new ArrayList();
		ArrayList defenseBoxes = new ArrayList();
		attackBoxes.add(new Integer(cw.getCombatBoxAttack()));
		defenseBoxes.add(new Integer(cw.getCombatBoxDefense()));
		if (target.isDenizen()) {
			if (target.isMonster()) {
				MonsterChitComponent monster = ((MonsterChitComponent)target);
				MonsterPartChitComponent weapon = monster.getWeapon();
				if (weapon!=null) {
					CombatWrapper cwWeapon = new CombatWrapper(weapon.getGameObject());
					if (!attackBoxes.contains(new Integer(cwWeapon.getCombatBoxAttack()))) {
						attackBoxes.add(new Integer(cwWeapon.getCombatBoxAttack()));
					}
					if (!defenseBoxes.contains(new Integer(cwWeapon.getCombatBoxDefense()))) {
						defenseBoxes.add(new Integer(cwWeapon.getCombatBoxDefense()));
					}
				}
				BattleHorse horse = monster.getHorse();
				if (horse!=null) {
					if (!attackBoxes.contains(new Integer(horse.getAttackCombatBox()))) {
						attackBoxes.add(new Integer(horse.getAttackCombatBox()));
					}
					if (!defenseBoxes.contains(new Integer(horse.getManeuverCombatBox()))) {
						defenseBoxes.add(new Integer(horse.getManeuverCombatBox()));
					}
				}
			}
			else if (target.isNative()) {
				NativeChitComponent nativeDenizen = ((NativeChitComponent)target);
				BattleHorse horse = nativeDenizen.getHorse();
				if (horse!=null) {
					if (!attackBoxes.contains(new Integer(horse.getAttackCombatBox()))) {
						attackBoxes.add(new Integer(horse.getAttackCombatBox()));
					}
					if (!defenseBoxes.contains(new Integer(horse.getManeuverCombatBox()))) {
						defenseBoxes.add(new Integer(horse.getManeuverCombatBox()));
					}
				}
			}
		}
		else if (target.isCharacter()) {
			CharacterWrapper character = new CharacterWrapper(target.getGameObject());
			for (java.util.Iterator _j14it2040 = (character.getActiveChits()).iterator(); _j14it2040.hasNext(); ) {
			  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it2040.next();
				CombatWrapper cwChit = new CombatWrapper(chit.getGameObject());
				if (!attackBoxes.contains(new Integer(cwChit.getCombatBoxAttack()))) {
					attackBoxes.add(new Integer(cwChit.getCombatBoxAttack()));
				}
				if (!defenseBoxes.contains(new Integer(cwChit.getCombatBoxDefense()))) {
					defenseBoxes.add(new Integer(cwChit.getCombatBoxDefense()));
				}
			}
			for (java.util.Iterator _j14it2041 = (character.getActiveInventory()).iterator(); _j14it2041.hasNext(); ) {
			  GameObject item = (GameObject) _j14it2041.next();
				RealmComponent rc = RealmComponent.getRealmComponent(item);
				if (rc.isHorse() || rc.isNativeHorse()) {
					CombatWrapper itemChit = new CombatWrapper(item);
					if (!attackBoxes.contains(new Integer(itemChit.getCombatBoxAttack()))) {
						attackBoxes.add(new Integer(itemChit.getCombatBoxAttack()));
					}
					if (!defenseBoxes.contains(new Integer(itemChit.getCombatBoxDefense()))) {
						defenseBoxes.add(new Integer(itemChit.getCombatBoxDefense()));
					}
				}
			}
		}
		
		HostPrefWrapper hostPref = HostPrefWrapper.findHostPrefs(gameData);
		
		int randomBox = RandomNumber.getRandom(3)+1;
		if (attackBoxes.isEmpty() || (attackBoxes.size()==1 && new Integer(0).equals(attackBoxes.get(0)))) {
			target.getGameObject().addThisAttributeListItem(Constants.SPIDER_WEB_BOXES_ATTACK,Integer.toString(randomBox));
		}
		else {
			for (java.util.Iterator _j14it2042 = (attackBoxes).iterator(); _j14it2042.hasNext(); ) {
			  Integer box = (Integer) _j14it2042.next();
				if (box.intValue()!=0) {
					target.getGameObject().addThisAttributeListItem(Constants.SPIDER_WEB_BOXES_ATTACK,box.toString());
				}
			}
		}
		if (defenseBoxes.isEmpty() || (defenseBoxes.size()==1 && new Integer(0).equals(defenseBoxes.get(0)))) {
			if (hostPref.hasPref(Constants.SR_COMBAT)) {
				int randomBoxDefense = RandomNumber.getRandom(3)+1;
				target.getGameObject().addThisAttributeListItem(Constants.SPIDER_WEB_BOXES_DEFENSE,Integer.toString(randomBoxDefense));
			}
			else {
				target.getGameObject().addThisAttributeListItem(Constants.SPIDER_WEB_BOXES_DEFENSE,Integer.toString(randomBox));
			}
		}
		else {
			for (java.util.Iterator _j14it2043 = (defenseBoxes).iterator(); _j14it2043.hasNext(); ) {
			  Integer box = (Integer) _j14it2043.next();
				if (box.intValue()!=0) {
					target.getGameObject().addThisAttributeListItem(Constants.SPIDER_WEB_BOXES_DEFENSE,box.toString());
				}
			}
		}
		
		StringBuffer sb = new StringBuffer();
		for (java.util.Iterator _j14it2044 = (target.getGameObject().getThisAttributeList(Constants.SPIDER_WEB_BOXES_ATTACK)).iterator(); _j14it2044.hasNext(); ) {
		  String box = (String) _j14it2044.next();
			sb.append(RealmUtility.getNameForAttackBox(Integer.parseInt(box))+" ");
		}
		for (java.util.Iterator _j14it2045 = (target.getGameObject().getThisAttributeList(Constants.SPIDER_WEB_BOXES_DEFENSE)).iterator(); _j14it2045.hasNext(); ) {
		  String box = (String) _j14it2045.next();
			sb.append(RealmUtility.getNameForDefensekBox(Integer.parseInt(box))+" ");
		}
		RealmLogging.logMessage(caster.getName(),target.getGameObject().getNameWithNumber()+" was hit by Spider Web and can only use the following combat boxes: "+sb);
	}

	public void unapply(SpellEffectContext context) {
	}

}
