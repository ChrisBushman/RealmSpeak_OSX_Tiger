package com.robin.general.util;

public final class RandomNumberType {
	private final String _name;
	private final int _ordinal;
	private RandomNumberType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final RandomNumberType System = new RandomNumberType("System", 0);
	public static final RandomNumberType R250_521 = new RandomNumberType("R250_521", 1);
	public static final RandomNumberType MersenneTwister = new RandomNumberType("MersenneTwister", 2);
	public static final RandomNumberType RandomOnTheFly = new RandomNumberType("RandomOnTheFly", 3);

	private static final RandomNumberType[] _VALUES = { System, R250_521, MersenneTwister, RandomOnTheFly };
	public static RandomNumberType[] values() { RandomNumberType[] r = new RandomNumberType[_VALUES.length]; java.lang.System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static RandomNumberType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}
}