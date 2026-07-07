package com.robin.magic_realm.components.quest;

public final class AttributeType {
	private final String _name;
	private final int _ordinal;
	private AttributeType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final AttributeType GreatTreasures = new AttributeType("GreatTreasures", 0);
	public static final AttributeType RecordedSpells = new AttributeType("RecordedSpells", 1);
	public static final AttributeType Fame = new AttributeType("Fame", 2);
	public static final AttributeType Notoriety = new AttributeType("Notoriety", 3);
	public static final AttributeType Gold = new AttributeType("Gold", 4);

	private static final AttributeType[] _VALUES = { GreatTreasures, RecordedSpells, Fame, Notoriety, Gold };
	public static AttributeType[] values() { AttributeType[] r = new AttributeType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static AttributeType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}

	// --- enum methods ---
	public String getDescription(boolean plural) {
		if (this == Fame) {
				return plural?"Fame points":"Fame point";
			}
			else if (this == Notoriety) {
				return plural?"Notoriety points":"Notoriety point";
			}
			else if (this == Gold) {
				return "Gold";
			}
			else if (this == RecordedSpells) {
				return plural?"new spells":"new spell";
			}
			else if (this == GreatTreasures) {
				return plural?"Great Treasures":"Great Treasure";
			}
		return "NO DESCRIPTION";
	}
}