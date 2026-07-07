package com.robin.magic_realm.components.quest.reward;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.quest.SpellCreator;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.GameWrapper;
import com.robin.magic_realm.components.wrapper.HostPrefWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class QuestRewardSpellEffectOnClearing extends QuestReward {
	
	public static final String SPELL = "_spell";
	public static final String REMOVE = "_remove";
	
	public static final class EffectOnClearing {
		private final String _name;
		private final int _ordinal;
		private EffectOnClearing(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final EffectOnClearing Bewilder = new EffectOnClearing("Bewilder", 0);
		public static final EffectOnClearing Blunting = new EffectOnClearing("Blunting", 1);
		public static final EffectOnClearing Gravity = new EffectOnClearing("Gravity", 2);
		public static final EffectOnClearing MountainSurge = new EffectOnClearing("MountainSurge", 3);
		public static final EffectOnClearing RocksGlow = new EffectOnClearing("RocksGlow", 4);
		public static final EffectOnClearing Sleep = new EffectOnClearing("Sleep", 5);
		public static final EffectOnClearing ViolentWinds = new EffectOnClearing("ViolentWinds", 6);

		private static final EffectOnClearing[] _VALUES = { Bewilder, Blunting, Gravity, MountainSurge, RocksGlow, Sleep, ViolentWinds };
		public static EffectOnClearing[] values() { EffectOnClearing[] r = new EffectOnClearing[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static EffectOnClearing valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	
	public QuestRewardSpellEffectOnClearing(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame,CharacterWrapper character) {
		String spell;
		EffectOnClearing _eoc = getSpell();
		if (_eoc == EffectOnClearing.Bewilder) {
			spell = "bewilder";
		} else if (_eoc == EffectOnClearing.Blunting) {
			spell = "blunting";
		} else if (_eoc == EffectOnClearing.Gravity) {
			spell = "gravity";
		} else if (_eoc == EffectOnClearing.MountainSurge) {
			spell = "mountain surge";
		} else if (_eoc == EffectOnClearing.RocksGlow) {
			spell = "rocks glow";
		} else if (_eoc == EffectOnClearing.Sleep) {
			spell = "sleep";
		} else if (_eoc == EffectOnClearing.ViolentWinds) {
			spell = "violent winds";
		} else {
			return;
		}
		
		GameWrapper gameWrapper = GameWrapper.findGame(getGameObject().getGameData());
		HostPrefWrapper hostPrefs = HostPrefWrapper.findHostPrefs(getGameData());
		SpellWrapper spellWrapper = SpellCreator.CreateSpellWrapper(spell, character);
		
		TileLocation charactersLocation = character.getCurrentLocation();
		if (charactersLocation != null && charactersLocation.tile != null && charactersLocation.clearing != null) {
			spellWrapper.addTarget(hostPrefs, charactersLocation.tile.getGameObject());
			spellWrapper.setExtraIdentifier(charactersLocation.clearing.getNumString());
		}
		
		if (remove()) {
			spellWrapper.unaffectTargets();
			return;
		}
		spellWrapper.affectTargets(frame, gameWrapper, false, null);
	}
	
	private EffectOnClearing getSpell() {
		return EffectOnClearing.valueOf(getString(SPELL));
	}
	
	private boolean remove() {
		return getBoolean(REMOVE);
	}
	
	public String getDescription() {
		if (remove()) {
			return("Remove the spell "+getSpell()+" from the characters clearing.");
		}
		return "Cast the spell "+getSpell()+" on the characters clearing";
	}
	public RewardType getRewardType() {
		return RewardType.SpellEffectOnClearing;
	}
}