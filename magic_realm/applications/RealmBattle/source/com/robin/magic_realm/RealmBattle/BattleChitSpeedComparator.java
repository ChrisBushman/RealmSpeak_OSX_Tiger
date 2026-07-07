package com.robin.magic_realm.RealmBattle;

import java.util.Comparator;

import com.robin.magic_realm.components.BattleChit;

public class BattleChitSpeedComparator implements Comparator {
	public int compare(Object o1,Object o2) {
		BattleChit b1 = (BattleChit) o1;
		BattleChit b2 = (BattleChit) o2;
		int ret = 0;
		ret = b1.getAttackSpeed().compareTo(b2.getAttackSpeed()); // smaller (faster) speeds should be first
		if (ret==0) {
			ret = b2.getLength().compareTo(b1.getLength()); // bigger lengths should be first
		}
		return ret;
	}
}