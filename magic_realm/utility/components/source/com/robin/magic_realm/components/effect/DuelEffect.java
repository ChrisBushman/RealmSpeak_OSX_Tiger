package com.robin.magic_realm.components.effect;

import java.util.ArrayList;

import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.MonsterChitComponent;
import com.robin.magic_realm.components.MonsterPartChitComponent;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.CombatWrapper;
import com.robin.magic_realm.components.wrapper.HostPrefWrapper;

public class DuelEffect implements ISpellEffect {

	private static String DUELLING = "duelling";
	
	public void apply(SpellEffectContext context) {
		HostPrefWrapper hostPrefs = HostPrefWrapper.findHostPrefs(context.getGameData());
		
		ArrayList targets = context.Spell.getTargets();
		if (targets.size() != 2 || context.Spell.getExtraIdentifier() == DUELLING) return;
		RealmComponent t0 = (RealmComponent) targets.get(0);
		RealmComponent t1 = (RealmComponent) targets.get(1);

		t0.clearTargets();
		t1.clearTargets();
		t0.setTarget(t1);
		t1.setTarget(t0);

		CombatWrapper combat0 = new CombatWrapper(t0.getGameObject());
		combat0.setSheetOwner(true);
		CombatWrapper combat1 = new CombatWrapper(t1.getGameObject());
		combat1.setSheetOwnerId(t0);
		if (hostPrefs.hasPref(Constants.SR_COMBAT)) {
			if (combat0.getGameObject().hasThisAttribute(Constants.SPIDER_WEB_BOXES_ATTACK)) {
				ArrayList boxes = combat0.getGameObject().getThisAttributeList(Constants.SPIDER_WEB_BOXES_ATTACK);
				String box = (String) boxes.get(RandomNumber.getRandom(boxes.size()));
				combat0.setCombatBoxAttack(Integer.parseInt(box));
			}
			else {
				combat0.setCombatBoxAttack(RandomNumber.getRandom(3)+1);
			}
			if (combat0.getGameObject().hasThisAttribute(Constants.SPIDER_WEB_BOXES_DEFENSE)) {
				ArrayList boxes = combat0.getGameObject().getThisAttributeList(Constants.SPIDER_WEB_BOXES_DEFENSE);
				String box = (String) boxes.get(RandomNumber.getRandom(boxes.size()));
				combat0.setCombatBoxAttack(Integer.parseInt(box));
			} else {
				combat0.setCombatBoxDefense(RandomNumber.getRandom(3)+1);
			}
			if (combat1.getGameObject().hasThisAttribute(Constants.SPIDER_WEB_BOXES_ATTACK)) {
				ArrayList boxes = combat1.getGameObject().getThisAttributeList(Constants.SPIDER_WEB_BOXES_ATTACK);
				String box = (String) boxes.get(RandomNumber.getRandom(boxes.size()));
				combat1.setCombatBoxAttack(Integer.parseInt(box));
			}
			else {
				combat1.setCombatBoxAttack(RandomNumber.getRandom(3)+1);
			}
			if (combat1.getGameObject().hasThisAttribute(Constants.SPIDER_WEB_BOXES_DEFENSE)) {
				ArrayList boxes = combat1.getGameObject().getThisAttributeList(Constants.SPIDER_WEB_BOXES_DEFENSE);
				String box = (String) boxes.get(RandomNumber.getRandom(boxes.size()));
				combat1.setCombatBoxAttack(Integer.parseInt(box));
			} else {
				combat1.setCombatBoxDefense(RandomNumber.getRandom(3)+1);
			}
		}
		else {
			if (combat0.getGameObject().hasThisAttribute(Constants.SPIDER_WEB_BOXES_ATTACK) && combat0.getGameObject().hasThisAttribute(Constants.SPIDER_WEB_BOXES_DEFENSE)) {
				ArrayList boxesA = combat0.getGameObject().getThisAttributeList(Constants.SPIDER_WEB_BOXES_ATTACK);
				ArrayList boxesD = combat0.getGameObject().getThisAttributeList(Constants.SPIDER_WEB_BOXES_ATTACK);
				ArrayList result = new ArrayList();
				for (java.util.Iterator _j14it2063 = (boxesA).iterator(); _j14it2063.hasNext(); ) {
				  String b = (String) _j14it2063.next(); if (boxesD.contains(b) && !result.contains(b)) result.add(b); }
				String box = (String) result.get(RandomNumber.getRandom(result.size()));
				combat0.setCombatBoxAttack(Integer.parseInt(box));
				combat0.setCombatBoxDefense(Integer.parseInt(box));
			}
			else {
				int random1 = RandomNumber.getRandom(3)+1;
				combat0.setCombatBoxAttack(random1);
				combat0.setCombatBoxDefense(random1);
			}
			if (combat1.getGameObject().hasThisAttribute(Constants.SPIDER_WEB_BOXES_ATTACK) && combat1.getGameObject().hasThisAttribute(Constants.SPIDER_WEB_BOXES_DEFENSE)) {
				ArrayList boxesA = combat1.getGameObject().getThisAttributeList(Constants.SPIDER_WEB_BOXES_ATTACK);
				ArrayList boxesD = combat1.getGameObject().getThisAttributeList(Constants.SPIDER_WEB_BOXES_ATTACK);
				ArrayList result = new ArrayList();
				for (java.util.Iterator _j14it2064 = (boxesA).iterator(); _j14it2064.hasNext(); ) {
				  String b = (String) _j14it2064.next(); if (boxesD.contains(b) && !result.contains(b)) result.add(b); }
				String box = (String) result.get(RandomNumber.getRandom(result.size()));
				combat1.setCombatBoxAttack(Integer.parseInt(box));
				combat1.setCombatBoxDefense(Integer.parseInt(box));
			} else {
				int random2 = RandomNumber.getRandom(3)+1;
				combat1.setCombatBoxAttack(random2);
				combat1.setCombatBoxDefense(random2);
			}
		}
		
		for (java.util.Iterator _j14it2065 = (targets).iterator(); _j14it2065.hasNext(); ) {
		  RealmComponent target = (RealmComponent) _j14it2065.next();
			if (target instanceof MonsterChitComponent) {
				MonsterPartChitComponent weapon = ((MonsterChitComponent) target).getWeapon();
				if (weapon != null) {
					CombatWrapper combat = new CombatWrapper(target.getGameObject());
					CombatWrapper combatWeapon = new CombatWrapper(weapon.getGameObject());
					combatWeapon.setCombatBoxAttack(RandomNumber.getRandom(3)+1);
					while (combat.getCombatBoxAttack() == combatWeapon.getCombatBoxAttack()) {
						combatWeapon.setCombatBoxAttack(RandomNumber.getRandom(3)+1);
					}
				}
			}
		}
		context.Spell.setExtraIdentifier(DUELLING);
	}

	public void unapply(SpellEffectContext context) {
	}

}
