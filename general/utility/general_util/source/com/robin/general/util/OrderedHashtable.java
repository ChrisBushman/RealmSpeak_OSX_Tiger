package com.robin.general.util;

import java.util.*;

/**
 * A Hashtable class that guarantees the order of the keys and values added
 * to it.
 */
public class OrderedHashtable extends Hashtable {
	protected ArrayList orderedKeys = new ArrayList();

	// overrides
	public void clear() {
		super.clear();
		orderedKeys.clear();
	}
	public Object put(Object key, Object value) {
		Object ret = super.put(key, value);
		if (orderedKeys==null) {
			orderedKeys = new ArrayList();
		}
		if (!orderedKeys.contains(key)) {
			orderedKeys.add(key);
		}
		return ret;
	}
	public void putAll(Map map) {
		for (int i=0;i<orderedKeys.size();i++) {
			Object key = orderedKeys.get(i);
			Object val = map.get(key);
			put(key,val);
		}
	}
	public Set keySet() {
		return new LinkedHashSet(orderedKeys);
	}
	public Collection values() {
		ArrayList vals = new ArrayList();
		if (orderedKeys==null) {
			orderedKeys = new ArrayList();
		}
		for (java.util.Iterator _j14it44 = (orderedKeys).iterator(); _j14it44.hasNext(); ) {
		  Object key = _j14it44.next();
			vals.add(get(key));
		}
		return vals;
	}
	public Object remove(Object key) {
		Object ret = super.remove(key);
		orderedKeys.remove(key);
		return ret;
	}

	// custom
	public Object remove(int index) {
		String key = (String)orderedKeys.get(index);
		return remove(key);
	}
	public Object getKey(int index) {
		return orderedKeys.get(index);
	}
	public Object getValue(int index) {
		return get(getKey(index));
	}
	public int indexOf(Object key) {
		return orderedKeys.indexOf(key);
	}
	public ArrayList orderedKeys() {
		return orderedKeys;
	}
	public Object insert(int index, Object key, Object val) {
		ArrayList newOrderedKeys = new ArrayList();
		for (int i=0;i<orderedKeys.size();i++) {
			if (i==index) {
				newOrderedKeys.add(key);
			}
			newOrderedKeys.add(orderedKeys.get(i));
		}
		orderedKeys = newOrderedKeys;
		return this.put(key,val);
	}
	public Object replace(int index, Object key, Object val) {
		ArrayList newOrderedKeys = new ArrayList();
		for (int i=0;i<orderedKeys.size();i++) {
			String currentKey = (String)orderedKeys.get(i);
			if (i==index) {
				newOrderedKeys.add(key);
				remove(currentKey);
			}
			else {
				newOrderedKeys.add(orderedKeys.get(i));
			}
		}
		orderedKeys = newOrderedKeys;
		return this.put(key,val);
	}
	public void sortKeys(Comparator comparator) {
		Collections.sort(orderedKeys, comparator);
	}
}
