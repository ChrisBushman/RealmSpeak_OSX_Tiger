package com.robin.general.util;

import java.util.Collection;

public class CollectionUtility {
	public static <T> boolean containsAny(Collection collection, Collection query) {
		if (collection == null || collection.isEmpty() || query == null || query.isEmpty())
			return false;
		for (java.util.Iterator _j14it45 = (query).iterator(); _j14it45.hasNext(); ) {
		  T val = (T) _j14it45.next();
			if (collection.contains(val))
				return true;
		}
		return false;
	}
}