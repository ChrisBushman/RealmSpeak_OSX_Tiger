package com.robin.magic_realm.components;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.attribute.Harm;
import com.robin.magic_realm.components.attribute.Speed;
import com.robin.magic_realm.components.attribute.Strength;
import com.robin.magic_realm.components.wrapper.CombatWrapper;
import com.robin.magic_realm.components.wrapper.GameWrapper;
import com.robin.magic_realm.components.wrapper.HostPrefWrapper;

public class EventSpellCardComponent extends SpellCardComponent implements BattleChit {

	public EventSpellCardComponent(GameObject obj) {
		super(obj);
	}

	public void changeWeaponState(HostPrefWrapper hostPrefs) {		
	}

	public Integer getLength() {
		if (getGameObject().hasThisAttribute("length")) {
			int len = getGameObject().getThisInt("length");
			return new Integer(len);
		}
		return null;
	}

	public Speed getMoveSpeed() {
		return null;
	}

	public Speed getFlySpeed() {
		return null;
	}

	public Speed getAttackSpeed() {
		Speed speed = new Speed();
		if (getGameObject().hasThisAttribute("attack_speed")) {
			speed = new Speed(getGameObject().getThisInt("attack_speed"));
		}
		return speed;
	}

	public Harm getHarm() {
		Strength strength = new Strength(getGameObject().getThisAttribute("strength"));
		int sharpness = getGameObject().getThisInt("sharpness");
		return new Harm(strength,sharpness);
	}

	public String getMagicType() {
		return getGameObject().getThisAttribute("magic_type");
	}

	public String getAttackSpell() {
		return null;
	}

	public int getManeuverCombatBox() {
		CombatWrapper combat = new CombatWrapper(getGameObject());
		return combat.getCombatBoxDefense();
	}

	public int getAttackCombatBox() {
		CombatWrapper combat = new CombatWrapper(getGameObject());
		return combat.getCombatBoxAttack();
	}

	public boolean isMissile() {
		return getGameObject().hasThisAttribute("missile");
	}

	public String getMissileType() {
		return getGameObject().getThisAttribute("missile");
	}

	public boolean hitsOnTie() {
		return false;
	}

	public boolean hasAnAttack() {
		return getAttackCombatBox()>0;
	}

	public boolean applyHit(GameWrapper game, HostPrefWrapper hostPrefs, BattleChit attacker, int box,Harm attackerHarm, int attackOrderPos) {
		return false;
	}	
}