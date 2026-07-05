package com.robin.magic_realm.RealmBattle;

import java.util.*;

import com.robin.game.objects.GameObject;

public class BattleSummary {
	
	private ArrayList<GameObject> orderedAttackers;
	private Hashtable<GameObject,ArrayList<GameObject>> hash;
	
	public BattleSummary() {
		orderedAttackers = new ArrayList<GameObject>();
		hash = new Hashtable<GameObject,ArrayList<GameObject>>();
	}
	public void addAttackerTarget(GameObject attacker,GameObject target) {
		ArrayList<GameObject> targets = hash.get(attacker);
		if (targets==null) {
			targets = new ArrayList<GameObject>();
			hash.put(attacker,targets);
			orderedAttackers.add(attacker);
		}
		targets.add(target);
	}
	public ArrayList<BattleSummaryRow> getSummaryRows() {
		ArrayList<BattleSummaryRow> list =  new ArrayList<BattleSummaryRow>();
		int n=0;
		for (GameObject attacker:orderedAttackers) {
			for (GameObject target:hash.get(attacker)) {
				list.add(new BattleSummaryRow(attacker,target,n++));
			}
		}
		Collections.sort(list);
		return list;
	}
}