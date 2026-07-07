package com.robin.game.objects;

import java.util.ArrayList;

public class GameQuery {
	
	private String blockName;
	
	public GameQuery() {
	}
	public GameQuery(String blockName) {
		this.blockName = blockName;
	}
	public boolean hasGameObjectWithKey(ArrayList list,String key) {
		return firstGameObjectWithKey(list,key)!=null;
	}
	public boolean hasGameObjectWithKeyAndValue(ArrayList list,String key,String value) {
		return firstGameObjectWithKeyAndValue(list,key,value)!=null;
	}
	public GameObject firstGameObjectWithKey(ArrayList list,String key) {
		ArrayList ret = query(list,key,null,true);
		return ret.isEmpty()?null:(GameObject) ret.get(0);
	}
	public GameObject firstGameObjectWithKeyAndValue(ArrayList list,String key,String value) {
		ArrayList ret = query(list,key,value,true);
		return ret.isEmpty()?null:(GameObject) ret.get(0);
	}
	public ArrayList allGameObjectsWithKey(ArrayList list,String key) {
		return query(list,key,null,false);
	}
	public ArrayList allGameObjectsWithKeyAndValue(ArrayList list,String key,String value) {
		return query(list,key,value,false);
	}
	private ArrayList query(ArrayList list,String key,String value,boolean stopAtFirst) {
		ArrayList ret = new ArrayList();
		for (java.util.Iterator _j14it115 = (list).iterator(); _j14it115.hasNext(); ) {
		  GameObject go = (GameObject) _j14it115.next();
			ArrayList blockNames = new ArrayList();
			if (blockName!=null) {
				blockNames.add(blockName);
			}
			else {
				blockNames.addAll(go.getAttributeBlockNames());
			}
			for (java.util.Iterator _j14it116 = (blockNames).iterator(); _j14it116.hasNext(); ) {
			  String bn = (String) _j14it116.next();
				if (value==null && go.hasAttribute(bn,key)) {
					ret.add(go);
				}
				else if (value!=null) {
					Object val = go.getObject(bn,key);
					boolean found = false;
					if (val instanceof ArrayList) {
						found = ((ArrayList)val).contains(value);
					}
					else {
						found = value.equals(val); 
					}
					if (found) {
						ret.add(go);
					}
				}
				if (stopAtFirst && !ret.isEmpty()) {
					return ret;
				}
			}
		}
		return ret;
	}
}