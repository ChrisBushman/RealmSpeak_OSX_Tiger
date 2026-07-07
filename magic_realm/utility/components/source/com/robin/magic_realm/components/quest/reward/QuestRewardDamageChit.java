package com.robin.magic_realm.components.quest.reward;

import java.util.ArrayList;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.CharacterActionChitComponent;
import com.robin.magic_realm.components.attribute.Strength;
import com.robin.magic_realm.components.quest.DamageType;
import com.robin.magic_realm.components.quest.VulnerabilityType;
import com.robin.magic_realm.components.swing.RealmComponentOptionChooser;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRewardDamageChit extends QuestReward {	
	public static final String DAMAGE_TYPE = "_dt";
	public static final String TYPE = "_type";
	public static final String STRENGTH = "_strength";
	public static final String SPEED = "_speed";
	public static final String MAGIC_COLOR = "_magic_color";
	public static final String MAGIC_TYPE = "_magic_type";
	public static final String ONLY_ACTIVE = "_only_active";
	
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
		
	public QuestRewardDamageChit(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame, CharacterWrapper character) {
		ArrayList chitsToCheck = character.getAllChits();
		ArrayList chits = new ArrayList();
		ChitType _ct = getType();
		if (_ct == ChitType.Move) {
			for (java.util.Iterator _j14it2388 = (chitsToCheck).iterator(); _j14it2388.hasNext(); ) {
			  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it2388.next();
				if (chit.isMove()) chits.add(chit);
			}
		} else if (_ct == ChitType.Fight) {
			for (java.util.Iterator _j14it2389 = (chitsToCheck).iterator(); _j14it2389.hasNext(); ) {
			  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it2389.next();
				if (chit.isFight()) chits.add(chit);
			}
		} else if (_ct == ChitType.Magic) {
			for (java.util.Iterator _j14it2390 = (chitsToCheck).iterator(); _j14it2390.hasNext(); ) {
			  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it2390.next();
				if (chit.isMagic() && chit.getMagicNumber()==getMagicType() && (getMagicColor().matches("Any") || chit.getColorMagic().getColorName().matches(getMagicColor()))) {
					chits.add(chit);
				}
			}
		} else if (_ct == ChitType.Fly) {
			for (java.util.Iterator _j14it2391 = (chitsToCheck).iterator(); _j14it2391.hasNext(); ) {
			  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it2391.next();
				if (chit.isFly()) chits.add(chit);
			}
		} else {
			chits.addAll(chitsToCheck);
		}
		chitsToCheck.clear();
		chitsToCheck.addAll(chits);
		chits.clear();
		for (java.util.Iterator _j14it2392 = (chitsToCheck).iterator(); _j14it2392.hasNext(); ) {
		  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it2392.next();
				if (getStrength() != VulnerabilityType.Any && chit.getStrength().weakerTo(new Strength(getStrength().toString()))) continue;
				if (getSpeed() != 0 && chit.getSpeed().getNum()>getSpeed()) continue;
				if (onlyActive() && !chit.isActive()) continue;
				if (getDamageType() == DamageType.Wounds && chit.isWounded()) continue;
				chits.add(chit);
		}
		
		String dmgType = "";
		DamageType _dt = getDamageType();
		if (_dt == DamageType.WeatherFatigue) {
			dmgType = "fatigue";
		} else if (_dt == DamageType.Wounds) {
			dmgType = "wound";
		}
		if (chits.isEmpty()) return;
		
		RealmComponentOptionChooser chooser = new RealmComponentOptionChooser(frame,"Choose chit to "+dmgType+":",false);
		chooser.addRealmComponents(chits,false);
		chooser.setVisible(true);
		CharacterActionChitComponent chit = (CharacterActionChitComponent)chooser.getFirstSelectedComponent();
		DamageType _dt2 = getDamageType();
		if (_dt2 == DamageType.WeatherFatigue) {
			chit.makeFatigued();
		} else if (_dt2 == DamageType.Wounds) {
			chit.makeWounded();
		}
	}

	public String getDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("Must damage a");
		if (getType() != ChitType.Any) {
			sb.append(getType()+" ");
		}
		sb.append("chit.");
		return sb.toString();
	}

	public RewardType getRewardType() {
		return RewardType.DamageChit;
	}
	private DamageType getDamageType() {
		return DamageType.valueOf(getString(DAMAGE_TYPE));
	}
	public ChitType getType() {
		return ChitType.valueOf(getString(TYPE));
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
}