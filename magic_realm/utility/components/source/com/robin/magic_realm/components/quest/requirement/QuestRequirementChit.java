package com.robin.magic_realm.components.quest.requirement;

import java.util.ArrayList;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.CharacterActionChitComponent;
import com.robin.magic_realm.components.attribute.Strength;
import com.robin.magic_realm.components.quest.VulnerabilityType;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRequirementChit extends QuestRequirement {	
	public static final String TYPE = "_type";
	public static final String AMOUNT = "_amount";
	public static final String STRENGTH = "_strength";
	public static final String SPEED = "_speed";
	public static final String MAGIC_COLOR = "_magic_color";
	public static final String MAGIC_TYPE = "_magic_type";
	public static final String ONLY_ACTIVE = "_only_active";
	public static final String NOT_FATIGUED = "_not_fatigued";
	public static final String NOT_WOUNDED = "_not_wounded";
	
	public static final class ChitType {
		private final String _name;
		private final int _ordinal;
		private ChitType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final ChitType Any = new ChitType("Any", 0);
		public static final ChitType Move = new ChitType("Move", 1);
		public static final ChitType Fight = new ChitType("Fight", 2);
		public static final ChitType Magic = new ChitType("Magic", 3);
		public static final ChitType Fly = new ChitType("Fly", 4);

		private static final ChitType[] _VALUES = { Any, Move, Fight, Magic, Fly };
		public static ChitType[] values() { ChitType[] r = new ChitType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static ChitType valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
		
	public QuestRequirementChit(GameObject go) {
		super(go);
	}

	protected boolean testFulfillsRequirement(JFrame frame, CharacterWrapper character, QuestRequirementParams reqParams) {
		ArrayList chitsToCheck = character.getAllChits();
		ArrayList chits = new ArrayList();
		ChitType _ctype = getType();
		if (_ctype == ChitType.Move) {
			for (java.util.Iterator _j14it2304 = (chitsToCheck).iterator(); _j14it2304.hasNext(); ) {
			  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it2304.next();
				if (chit.isMove()) chits.add(chit);
			}
		} else if (_ctype == ChitType.Fight) {
			for (java.util.Iterator _j14it2305 = (chitsToCheck).iterator(); _j14it2305.hasNext(); ) {
			  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it2305.next();
				if (chit.isFight()) chits.add(chit);
			}
		} else if (_ctype == ChitType.Magic) {
			for (java.util.Iterator _j14it2306 = (chitsToCheck).iterator(); _j14it2306.hasNext(); ) {
			  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it2306.next();
				if (chit.isMagic() && chit.getMagicNumber()==getMagicType() && (getMagicColor().matches("Any") || chit.getColorMagic().getColorName().matches(getMagicColor()))) {
					chits.add(chit);
				}
			}
		} else if (_ctype == ChitType.Fly) {
			for (java.util.Iterator _j14it2307 = (chitsToCheck).iterator(); _j14it2307.hasNext(); ) {
			  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it2307.next();
				if (chit.isFly()) chits.add(chit);
			}
		} else {
			chits.addAll(chitsToCheck);
		}
		chitsToCheck.clear();
		chitsToCheck.addAll(chits);
		chits.clear();
		for (java.util.Iterator _j14it2308 = (chitsToCheck).iterator(); _j14it2308.hasNext(); ) {
		  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it2308.next();
				if (getStrength() != VulnerabilityType.Any && chit.getStrength().weakerTo(new Strength(getStrength().toString()))) continue;
				if (getSpeed() != 0 && chit.getSpeed().getNum()>getSpeed()) continue;
				if (onlyActive() && !chit.isActive()) continue;
				if (notFatigued() && chit.isFatigued()) continue;
				if (notWounded() && chit.isWounded()) continue;
				chits.add(chit);
		}
		
		if (chits.size()>=getAmount()) {
			return true;
		}
		return false;
	}

	protected String buildDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("Must have "+getAmount()+" ");
		if (getType() != ChitType.Any) {
			sb.append(getType()+" ");
		}
		sb.append("chit(s).");
		return sb.toString();
	}

	public RequirementType getRequirementType() {
		return RequirementType.Chit;
	}
	public ChitType getType() {
		return ChitType.valueOf(getString(TYPE));
	}
	private int getAmount() {
		return getInt(AMOUNT);
	}
	private VulnerabilityType getStrength() {
		return VulnerabilityType.valueOf(getString(STRENGTH));
	}
	private int getSpeed() {
		return getInt(SPEED);
	}
	private String getMagicColor() {
		return getString(MAGIC_COLOR);
	}
	private int getMagicType() {
		return getInt(MAGIC_TYPE);
	}
	private boolean onlyActive() {
		return getBoolean(ONLY_ACTIVE);
	}
	private boolean notFatigued() {
		return getBoolean(NOT_FATIGUED);
	}
	private boolean notWounded() {
		return getBoolean(NOT_WOUNDED);
	}
}