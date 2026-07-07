package com.robin.general.util;

public class Extensions {
	public static Object coalesce(Object obj1, Object obj2){
		Object result = obj1;
		if(result == null) result = obj2;
		return result;
	}

	public static boolean hasFlag(int value, int flag){
		return(value & flag) == flag;
	}
}
