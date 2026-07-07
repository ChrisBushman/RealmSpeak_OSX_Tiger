package com.robin.general.util;

import java.util.*;

/**
 * A utility class for handling and populating lists of objects hashed by key.  Multiple objects can be added
 * for a single key.  Note that the lists are uniqued by default, unless specified otherwise.
 */
public class HashLists implements Map {

	private boolean forceUnique;
    private Hashtable hash;

	public HashLists() {
		this(true);
	}
	public HashLists(boolean forceUnique) {
		this.forceUnique = forceUnique;
	    hash = new Hashtable();
	}
	public Object put(Object key,Object val) {
	    ArrayList list = getList(key);
	    if (list==null) {
	        list = new ArrayList();
	        hash.put(key,list);
	    }
	    if (!forceUnique || !list.contains(val)) {
		    list.add(val);
	    }
	    return null;
	}
	public void putList(Object key,ArrayList list) {
		hash.put(key,list);
	}
	public Object get(Object key) {
	    return hash.get(key);
	}
	public ArrayList getList(Object key) {
	    Object val = hash.get(key);
	    return val == null ? null : (ArrayList) val;
	}
	public ArrayList getListAsNew(Object key) {
		ArrayList list = getList(key);
		if (list!=null) {
			return new ArrayList(list);
		}
		return null;
	}
	public int size() {
	    return hash.size();
	}
	public void clear() {
	    hash.clear();
	}
	public boolean containsKey(Object key) {
	    return hash.containsKey(key);
	}
	public boolean containsValue(Object val) {
	    for (java.util.Iterator _j14it51 = (hash.values()).iterator(); _j14it51.hasNext(); ) {
	      ArrayList list = (ArrayList) _j14it51.next();
	        if (list.contains(val)) {
	            return true;
	        }
	    }
	    return false;
	}
	public boolean isEmpty() {
	    return hash.isEmpty();
	}
	public Set keySet() {
	    return hash.keySet();
	}
	public void putAll(Map map) {
	    for (java.util.Iterator _j14it52 = (map.keySet()).iterator(); _j14it52.hasNext(); ) {
	      Object key = _j14it52.next();
	        ArrayList list = getList(key);
	        Object val = map.get(key);
	        if (val instanceof Collection) {
	            list.addAll((Collection)val);
	        }
	        else {
	            list.add(val);
	        }
	    }
	}
	public Set entrySet() {
	    return hash.entrySet();
	}
	public Object remove(Object key) {
	    return hash.remove(key);
	}
	public void removeKeyValue(Object key,Object val) {
		if (key!=null) {
			ArrayList list = getList(key);
			if (list!=null && list.contains(val)) {
				list.remove(val);
				if (list.isEmpty()) {
					remove(key);
				}
			}
		}
	}
	public void removeValue(Object val) {
	    for (java.util.Iterator _j14it53 = (hash.values()).iterator(); _j14it53.hasNext(); ) {
	      ArrayList list = (ArrayList) _j14it53.next();
	        if (list.contains(val)) {
	            list.remove(val);
	        }
	    }
	}
	public Collection values() {
	    return hash.values();
	}
}
