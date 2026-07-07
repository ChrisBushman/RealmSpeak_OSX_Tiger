package com.robin.magic_realm.components.quest.reward;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.game.objects.GamePool;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.effect.SpellEffectContext;
import com.robin.magic_realm.components.effect.TransmorphEffect;
import com.robin.magic_realm.components.quest.DieRollType;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.GameWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class QuestRewardTransmorph extends QuestReward {
	
	public static final String TRANSMORPH_TYPE = "_type";
	public static final String DIE_ROLL = "_dr";
	public static final String REVERT_TRANSFORMATION = "_revert";
	
	public static final class TransmorphType {
		private final String _name;
		private final int _ordinal;
		private TransmorphType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final TransmorphType Animal = new TransmorphType("Animal", 0);
		public static final TransmorphType Statue = new TransmorphType("Statue", 1);
		public static final TransmorphType Mist = new TransmorphType("Mist", 2);

		private static final TransmorphType[] _VALUES = { Animal, Statue, Mist };
		public static TransmorphType[] values() { TransmorphType[] r = new TransmorphType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static TransmorphType valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	
	public QuestRewardTransmorph(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame, CharacterWrapper character) {
		GameWrapper gameWrapper = GameWrapper.findGame(getGameData());
		RealmComponent target = RealmComponent.getRealmComponent(character.getGameObject());
		GamePool pool = new GamePool(getGameData().getGameObjects());
		
		TransmorphEffect transmorph;
		String transformBlock;
		GameObject sp;	
		TransmorphType _tt = getTransmorphType();
		if (_tt == TransmorphType.Mist) {
			transmorph = new TransmorphEffect("mist");
			transformBlock = "mist";
			sp = pool.findFirst("name=Melt Into Mist");
		} else if (_tt == TransmorphType.Statue) {
			transmorph = new TransmorphEffect("statue");
			transformBlock = "statue";
			sp = pool.findFirst("name=Stone Gaze");
		} else { // Animal (default)
			transmorph = new TransmorphEffect("roll");
			transformBlock = "roll"+getDieRoll();
			sp = pool.findFirst("name=Transform");
		}
	
		if (sp == null) return;
		SpellWrapper spell = new SpellWrapper(sp);
		spell.setString(SpellWrapper.CASTER_ID, String.valueOf(character.getGameObject().getId()));
		SpellEffectContext spellEffectContext = new SpellEffectContext(frame, gameWrapper, target, spell, character.getGameObject());
		
		if (revert()) {
			transmorph.unapply(spellEffectContext);
			return;
		}
	
		GameObject transform = TransmorphEffect.prepareTransformation(transformBlock, target, spell, frame);
		character.setTransmorph(transform);
	}
	
	public RewardType getRewardType() {
		return RewardType.Transmorph;
	}
	public String getDescription() {
		if (revert()) {
			return "Character is tranformed back.";
		}
		return "Transmorphs the character into "+getTransmorphType()+".";
	}
	private TransmorphType getTransmorphType() {
		return TransmorphType.valueOf(getString(TRANSMORPH_TYPE));
	}
	private boolean revert() {
		return getBoolean(REVERT_TRANSFORMATION);
	}
	private int getDieRoll() {
		String dieRoll = getString(DIE_ROLL);
		return getDieRoll(DieRollType.valueOf(dieRoll));
	}
}