package com.robin.magic_realm.RealmBattle;

import java.util.Comparator;

import com.robin.magic_realm.components.RealmComponent;

public class TargetIndexComparator implements Comparator {
	public int compare(Object o1,Object o2) {
		RealmComponent rc1 = (RealmComponent) o1;
		RealmComponent rc2 = (RealmComponent) o2;
		int ret = 0;
		ret = rc1.getTargetIndex()-rc2.getTargetIndex();
		return ret;
	}
}