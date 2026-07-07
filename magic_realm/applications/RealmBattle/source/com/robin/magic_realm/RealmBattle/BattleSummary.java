package com.robin.magic_realm.RealmBattle;

import java.util.*;

import com.robin.game.objects.GameObject;

public class BattleSummary {
	
	private ArrayList orderedAttackers;
	private Hashtable hash;
	
	public BattleSummary() {
		orderedAttackers = new ArrayList();
		hash = new Hashtable();
	}
	public void addAttackerTarget(GameObject attacker,GameObject target) {
		ArrayList targets = (ArrayList) hash.get(attacker);
		if (targets==null) {
			targets = new ArrayList();
			hash.put(attacker,targets);
			orderedAttackers.add(attacker);
		}
		targets.add(target);
	}
	public ArrayList getSummaryRows() {
		ArrayList list =  new ArrayList();
		int n=0;
		for (java.util.Iterator _j14it386 = (orderedAttackers).iterator(); _j14it386.hasNext(); ) {
		  GameObject attacker = (GameObject) _j14it386.next();
			for (java.util.Iterator _j14it387 = ((ArrayList) hash.get(attacker)).iterator(); _j14it387.hasNext(); ) {
			  GameObject target = (GameObject) _j14it387.next();
				list.add(new BattleSummaryRow(attacker,target,n++));
			}
		}
		Collections.sort(list);
		return list;
	}
}