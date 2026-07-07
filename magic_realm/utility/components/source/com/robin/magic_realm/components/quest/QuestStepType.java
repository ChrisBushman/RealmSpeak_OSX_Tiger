package com.robin.magic_realm.components.quest;

import java.awt.Color;

public final class QuestStepType {
	private final String _name;
	private final int _ordinal;
	private QuestStepType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final QuestStepType And = new QuestStepType("And", 0);
	public static final QuestStepType Or = new QuestStepType("Or", 1);

	private static final QuestStepType[] _VALUES = { And, Or };
	public static QuestStepType[] values() { QuestStepType[] r = new QuestStepType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static QuestStepType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}

	// --- enum methods ---
	public Color getColor() {
		if (this == And) {
				return Color.black;
			}
			else if (this == Or) {
				return Color.blue;
			}
		throw new IllegalStateException();
	}
}
