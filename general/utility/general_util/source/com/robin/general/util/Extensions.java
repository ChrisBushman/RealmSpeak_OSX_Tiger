package com.robin.general.util;

public class Extensions {
	public static <T> T coalesce(T obj1, T obj2){
		T result = obj1;
		if(result == null) result = obj2;
		return result;
	}

	public static boolean hasFlag(int value, int flag){
		return(value & flag) == flag;
	}
}
