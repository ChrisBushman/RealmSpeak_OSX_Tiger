package com.robin.magic_realm.components.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import com.robin.magic_realm.components.attribute.TileLocation;

public class DieRule {
	private static final int RULE_TYPE_MINUS_ONE = 1;
	private static final int RULE_TYPE_ONE_DIE = 2;
	private static final int RULE_TYPE_PLUS_ONE = 3;
	private static final int RULE_TYPE_MINUS_TWO = 4;
	
	private TileLocation tl;
	private int type;
	private boolean allKeys = false;
	private ArrayList keyList;
	private boolean allLocations = false;
	private ArrayList locationList;
	private ArrayList twt = new ArrayList(Arrays.asList(new String[]{"crypt of the knight","enchanted meadow","toadstool circle","circle of stones","ethereal abbey","fairy grove","haunted grave","mage library"}));
	
	public DieRule(TileLocation tl,String rule) {
		this.tl = tl;
		rule = rule.toLowerCase();
		if (rule.startsWith("-1")) {
			type = RULE_TYPE_MINUS_ONE;
		}
		else if (rule.startsWith("1d")) {
			type = RULE_TYPE_ONE_DIE;
		}
		else if (rule.startsWith("+1")) {
			type = RULE_TYPE_PLUS_ONE;
		}
		else if (rule.startsWith("-2")) {
			type = RULE_TYPE_MINUS_TWO;
		}
		else {
			throw new IllegalArgumentException("Illegal Rule: "+rule);
		}
		StringTokenizer tokens = new StringTokenizer(rule.substring(3),":");
		String keyListString = tokens.nextToken();
		if ("all".equals(keyListString)) {
			allKeys = true;
		}
		else {
			keyList = makeList(keyListString);
		}
		String locationListString = tokens.nextToken();
		if ("all".equals(locationListString)) {
			allLocations = true;
		}
		else {
			locationList = makeList(locationListString);
		}
	}
	public boolean conditionsMet(String key,ArrayList chitDescList) {
		if (key.indexOf(',')>0) {
			StringTokenizer tokens = new StringTokenizer(key,",");
			while(tokens.hasMoreTokens()) {
				if (conditionsMet(tokens.nextToken(),chitDescList)) {
					return true;
				}
			}
			return false;
		}
		boolean validKey = allKeys || keyList.contains(key) || (keyList.contains("twt") && twt.contains(key));
		boolean validLocation = allLocations || locationMatches(chitDescList);
		return validKey && validLocation;
	}
	private boolean locationMatches(ArrayList chitDescList) {
		for (java.util.Iterator _j14it2804 = (locationList).iterator(); _j14it2804.hasNext(); ) {
		  String loc = (String) _j14it2804.next();
			if (loc.startsWith(">")) {
				// test clearing
				if (tl.hasClearing() && tl.clearing.getType().equalsIgnoreCase(loc.substring(1))) {
					return true;
				}
			}
			else if (loc.startsWith("%") && loc.endsWith("%")) {
				loc = loc.substring(1,loc.length()-1);
				for (java.util.Iterator _j14it2805 = (chitDescList).iterator(); _j14it2805.hasNext(); ) {
				  String test = (String) _j14it2805.next();
					if (test.indexOf(loc)>=0) {
						return true;
					}
				}
			}
			else if (loc.startsWith("%")) {
				loc = loc.substring(1);
				for (java.util.Iterator _j14it2806 = (chitDescList).iterator(); _j14it2806.hasNext(); ) {
				  String test = (String) _j14it2806.next();
					if (test.endsWith(loc)) {
						return true;
					}
				}
			}
			else if (loc.endsWith("%")) {
				loc = loc.substring(0,loc.length()-1);
				for (java.util.Iterator _j14it2807 = (chitDescList).iterator(); _j14it2807.hasNext(); ) {
				  String test = (String) _j14it2807.next();
					if (test.startsWith(loc)) {
						return true;
					}
				}
			}
			else if (loc.startsWith("'")) {
				loc = loc.substring(1);
				if(tl.tile.getGameObject().hasThisAttribute(loc)) {
					return true;
				}
			}
			else if (chitDescList.contains(loc)) {
				return true;
			}
		}
		return false;
	}
	private static ArrayList makeList(String input) {
		ArrayList list = new ArrayList();
		StringTokenizer tokens = new StringTokenizer(input,",");
		while(tokens.hasMoreTokens()) {
			list.add(tokens.nextToken());
		}
		return list;
	}
	public boolean isMinusOne() {
		return type==RULE_TYPE_MINUS_ONE;
	}
	public boolean isOneDie() {
		return type==RULE_TYPE_ONE_DIE;
	}
	public boolean isPlusOne() {
		return type==RULE_TYPE_PLUS_ONE;
	}
	public boolean isMinusTwo() {
		return type==RULE_TYPE_MINUS_TWO;
	}
}