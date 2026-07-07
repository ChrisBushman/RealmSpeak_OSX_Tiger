package com.robin.game.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GameCommandOrderSetup extends GameCommand {

	public static String NAME = "OrderSetup";
	
	public GameCommandOrderSetup(GameSetup gameSetup) {
		super(gameSetup);
	}
	public String getTypeName() {
		return NAME;
	}
	protected String process(ArrayList allGameObjects) {
		return orderSetup(allGameObjects);
	}
	public static String orderSetup(ArrayList allGameObjects) {
		ArrayList sortedObjects = new ArrayList();
		for (java.util.Iterator _j14it75 = (allGameObjects).iterator(); _j14it75.hasNext(); ) {
		  GameObject go = (GameObject) _j14it75.next();
			if (go.hasThisAttribute("dwelling") || go.hasThisAttribute("treasure_location")) {
				ArrayList hold = new ArrayList();
				hold.addAll(go.getHold());
				if (hold==null || hold.isEmpty()) continue;
				go.clearHold();
				
				Collections.shuffle(hold);
				Collections.shuffle(hold);
				Collections.sort(hold,new Comparator() {
					public int compare(Object o1,Object o2) {
						return getOrderNumber((GameObject)o1)-getOrderNumber((GameObject)o2);
					}
				});
				
				for (java.util.Iterator _j14it76 = (hold).iterator(); _j14it76.hasNext(); ) {
				  GameObject obj = (GameObject) _j14it76.next();
					go.add(obj);
				}
				sortedObjects.add(go);
			}
		}
		return "Objects sorted:  "+sortedObjects.toString();
	}
	
	private static int getOrderNumber(GameObject go) {
		if (go.hasThisAttribute("treasure")) {
			if (go.getThisAttribute("treasure").matches("large")) return 1;
			if (go.getThisAttribute("treasure").matches("small")) return 2;
		}
		if (go.hasThisAttribute("spell")) return 3;
		if (go.hasThisAttribute("item")) {
			if (go.hasThisAttribute("horse")) return 4;
			if (go.hasThisAttribute("armor") && go.hasThisAttribute("armor_thrust") && go.hasThisAttribute("armor_swing") && go.hasThisAttribute("armor_smash")) return 5;
			if (go.hasThisAttribute("weapon")) return 6;
			if (go.hasThisAttribute("armor") && go.hasThisAttribute("armor_smash")) return 7;
			if (go.hasThisAttribute("armor") && (go.hasThisAttribute("shield") || go.hasThisAttribute("armor_choice"))) return 8;
			if (go.hasThisAttribute("armor") && go.hasThisAttribute("armor_thrust") && go.hasThisAttribute("armor_swing")) return 9;
		}
		if (go.hasThisAttribute("denizen")) return 10;
		
		return 0;
	}
}