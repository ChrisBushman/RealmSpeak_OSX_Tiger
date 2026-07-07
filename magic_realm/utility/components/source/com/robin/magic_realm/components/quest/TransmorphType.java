package com.robin.magic_realm.components.quest;

public final class TransmorphType {
	private final String _name;
	private final int _ordinal;
	private TransmorphType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final TransmorphType Any = new TransmorphType("Any", 0);
	public static final TransmorphType Denizen = new TransmorphType("Denizen", 1);
	public static final TransmorphType Native = new TransmorphType("Native", 2);
	public static final TransmorphType Monster = new TransmorphType("Monster", 3);
	public static final TransmorphType Animal = new TransmorphType("Animal", 4);
	public static final TransmorphType Mist = new TransmorphType("Mist", 5);
	public static final TransmorphType Statue = new TransmorphType("Statue", 6);

	private static final TransmorphType[] _VALUES = { Any, Denizen, Native, Monster, Animal, Mist, Statue };
	public static TransmorphType[] values() { TransmorphType[] r = new TransmorphType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static TransmorphType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}
}