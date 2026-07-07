package com.robin.magic_realm.components.attribute;

public class GuildLevelType {
	public static final class GuildLevel {
		private final String _name;
		private final int _ordinal;
		private GuildLevel(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final GuildLevel Apprentice = new GuildLevel("Apprentice", 0);
		public static final GuildLevel Journeyman = new GuildLevel("Journeyman", 1);
		public static final GuildLevel Master = new GuildLevel("Master", 2);

		private static final GuildLevel[] _VALUES = { Apprentice, Journeyman, Master };
		public static GuildLevel[] values() { GuildLevel[] r = new GuildLevel[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static GuildLevel valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	
	public static int getIntFor(GuildLevel level) {
		if (level == GuildLevel.Apprentice) return 1;
		if (level == GuildLevel.Journeyman) return 2;
		if (level == GuildLevel.Master) return 3;
		return 0;
	}
}