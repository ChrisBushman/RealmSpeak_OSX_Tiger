package com.robin.magic_realm.components.quest.reward;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.quest.SpellCreator;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.GameWrapper;
import com.robin.magic_realm.components.wrapper.HostPrefWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class QuestRewardSpellEffectOnTile extends QuestReward {
	
	public static final String SPELL = "_spell";
	public static final String REMOVE = "_remove";
	
	public static final class EffectOnTile {
		private final String _name;
		private final int _ordinal;
		private EffectOnTile(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final EffectOnTile Fog = new EffectOnTile("Fog", 0);
		public static final EffectOnTile FrozenWater = new EffectOnTile("FrozenWater", 1);
		public static final EffectOnTile ViolentStorm = new EffectOnTile("ViolentStorm", 2);

		private static final EffectOnTile[] _VALUES = { Fog, FrozenWater, ViolentStorm };
		public static EffectOnTile[] values() { EffectOnTile[] r = new EffectOnTile[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static EffectOnTile valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	
	public QuestRewardSpellEffectOnTile(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame,CharacterWrapper character) {
		String spell;
		EffectOnTile _eot = getSpell();
		if (_eot == EffectOnTile.Fog) {
			spell = "fog";
		} else if (_eot == EffectOnTile.FrozenWater) {
			spell = "frozen water";
		} else if (_eot == EffectOnTile.ViolentStorm) {
			spell = "violent storm";
		} else {
			return;
		}
		
		GameWrapper gameWrapper = GameWrapper.findGame(getGameObject().getGameData());
		HostPrefWrapper hostPrefs = HostPrefWrapper.findHostPrefs(getGameData());
		SpellWrapper spellWrapper = SpellCreator.CreateSpellWrapper(spell, character);
		
		TileLocation charactersLocation = character.getCurrentLocation();
		if (charactersLocation != null && charactersLocation.tile != null) {
			spellWrapper.addTarget(hostPrefs, charactersLocation.tile.getGameObject());
		}
		
		if (remove()) {
			spellWrapper.unaffectTargets();
			return;
		}
		spellWrapper.affectTargets(frame, gameWrapper, false, null);
	}
	
	private EffectOnTile getSpell() {
		return EffectOnTile.valueOf(getString(SPELL));
	}
	
	private boolean remove() {
		return getBoolean(REMOVE);
	}
	
	public String getDescription() {
		if (remove()) {
			return("Remove the spell "+getSpell()+" from the characters tile.");
		}
		return "Cast the spell "+getSpell()+" on the characters tile";
	}
	public RewardType getRewardType() {
		return RewardType.SpellEffectOnTile;
	}
}