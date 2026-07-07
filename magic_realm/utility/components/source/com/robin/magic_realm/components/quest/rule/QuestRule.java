package com.robin.magic_realm.components.quest.rule;

import java.util.Hashtable;

import com.robin.game.objects.GameObject;

/**
 * Quest rules are active as soon as the quest is taken.
 */
public class QuestRule {
	
	public static final class RuleType {
		private final String _name;
		private final int _ordinal;
		private RuleType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final RuleType ActiveMonster = new RuleType("ActiveMonster", 0);
		public static final RuleType MovementRestricted = new RuleType("MovementRestricted", 1);

		private static final RuleType[] _VALUES = { ActiveMonster, MovementRestricted };
		public static RuleType[] values() { RuleType[] r = new RuleType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static RuleType valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
		// --- enum methods ---
		public boolean affectsAllPlayers() {
			return this==ActiveMonster; // eventually others here...
		}
	}
	
	public void updateIds(Hashtable lookup) {
		// override if IDs need to be updated!
	}
}