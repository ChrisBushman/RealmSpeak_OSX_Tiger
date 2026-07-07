package com.robin.general.util;

import java.util.ArrayList;
import java.util.Collection;

public class StringBufferedList {
	private String comma;
	private String and;
	private ArrayList list;
	public StringBufferedList() {
		this(", ","and ");
	}
	public StringBufferedList(String comma,String and) {
		this.comma = comma;
		this.and = and;
		list = new ArrayList();
	}
	public int size() {
		return list.size();
	}
	public void append(String val) {
		list.add(val);
	}
	public void appendAll(Collection list) {
		for (java.util.Iterator _j14it46 = (list).iterator(); _j14it46.hasNext(); ) {
		  String val = (String) _j14it46.next();
			append(val);
		}
	}
	public void countIdenticalItems() {
		HashLists hash = new HashLists();
		ArrayList keys = new ArrayList();
		int n=0;
		for (java.util.Iterator _j14it47 = (list).iterator(); _j14it47.hasNext(); ) {
		  String string = (String) _j14it47.next();
			hash.put(string,"n"+(n++));
			if (!keys.contains(string)) {
				keys.add(string);
			}
		}
		list.clear();
		for (java.util.Iterator _j14it48 = (keys).iterator(); _j14it48.hasNext(); ) {
		  String string = (String) _j14it48.next();
			int count = hash.getList(string).size();
			if (count==1) {
				list.add(string);
			}
			else {
				list.add(count+" "+string+(count==1?"":"s"));
			}
		}
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<list.size();i++) {
			String val = (String) list.get(i);
			if (sb.length()>0) {
				sb.append(comma);
				if (i==(list.size()-1)) {
					sb.append(and);
				}
			}
			sb.append(val);
		}
		return sb.toString();
	}
}