package com.robin.magic_realm.components.quest.reward;

import java.util.ArrayList;
import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.TileComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class QuestRewardSpellEffectOnCharacter extends QuestReward {
	
	public static final String SPELL = "_spell";
	public static final String REMOVE = "_remove";
	
	public static final class EffectOnCharacter {
		private final String _name;
		private final int _ordinal;
		private EffectOnCharacter(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final EffectOnCharacter Barkskin = new EffectOnCharacter("Barkskin", 0);
		public static final EffectOnCharacter BlazingLightX = new EffectOnCharacter("BlazingLightX", 1);
		public static final EffectOnCharacter Blunt = new EffectOnCharacter("Blunt", 2);
		public static final EffectOnCharacter Camouflage = new EffectOnCharacter("Camouflage", 3);
		public static final EffectOnCharacter DivineMight = new EffectOnCharacter("DivineMight", 4);
		public static final EffectOnCharacter DivineProtection = new EffectOnCharacter("DivineProtection", 5);
		public static final EffectOnCharacter DivineShield = new EffectOnCharacter("DivineShield", 6);
		public static final EffectOnCharacter GrowWings = new EffectOnCharacter("GrowWings", 7);
		public static final EffectOnCharacter HurricaneWinds = new EffectOnCharacter("HurricaneWinds", 8);
		public static final EffectOnCharacter Lost = new EffectOnCharacter("Lost", 9);
		public static final EffectOnCharacter NegativeAura = new EffectOnCharacter("NegativeAura", 10);
		public static final EffectOnCharacter PeaceWithNature = new EffectOnCharacter("PeaceWithNature", 11);
		public static final EffectOnCharacter Premonition = new EffectOnCharacter("Premonition", 12);
		public static final EffectOnCharacter Prophecy = new EffectOnCharacter("Prophecy", 13);
		public static final EffectOnCharacter ProtectionFromMagic = new EffectOnCharacter("ProtectionFromMagic", 14);
		public static final EffectOnCharacter Shrink = new EffectOnCharacter("Shrink", 15);
		public static final EffectOnCharacter Slowed = new EffectOnCharacter("Slowed", 16);
		public static final EffectOnCharacter SpiritGuide = new EffectOnCharacter("SpiritGuide", 17);
		public static final EffectOnCharacter TrackersSense = new EffectOnCharacter("TrackersSense", 18);
		public static final EffectOnCharacter ValeWalker = new EffectOnCharacter("ValeWalker", 19);
		public static final EffectOnCharacter WaterRun = new EffectOnCharacter("WaterRun", 20);

		private static final EffectOnCharacter[] _VALUES = { Barkskin, BlazingLightX, Blunt, Camouflage, DivineMight, DivineProtection, DivineShield, GrowWings, HurricaneWinds, Lost, NegativeAura, PeaceWithNature, Premonition, Prophecy, ProtectionFromMagic, Shrink, Slowed, SpiritGuide, TrackersSense, ValeWalker, WaterRun };
		public static EffectOnCharacter[] values() { EffectOnCharacter[] r = new EffectOnCharacter[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static EffectOnCharacter valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	
	public QuestRewardSpellEffectOnCharacter(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame,CharacterWrapper character) {
		String effect;
		EffectOnCharacter _eoc = getSpell();
		if (_eoc == EffectOnCharacter.Barkskin) {
			effect = Constants.BARKSKIN;
		} else if (_eoc == EffectOnCharacter.BlazingLightX) {
			effect = Constants.TORCH_BEARER;
		} else if (_eoc == EffectOnCharacter.Blunt) {
			effect = Constants.BLUNT;
		} else if (_eoc == EffectOnCharacter.Camouflage) {
			effect = Constants.CAMOUFLAGE;
		} else if (_eoc == EffectOnCharacter.DivineMight) {
			effect = Constants.STRONG_MF;
		} else if (_eoc == EffectOnCharacter.DivineProtection) {
			effect = Constants.STRENGTHENED_VULNERABILITY;
		} else if (_eoc == EffectOnCharacter.DivineShield) {
			effect = Constants.ADDS_ARMOR;
		} else if (_eoc == EffectOnCharacter.GrowWings) {
			effect = Constants.GROW_WINGS;
		} else if (_eoc == EffectOnCharacter.HurricaneWinds) {
			GameObject hurricanWindsOriginal = character.getGameData().getGameObjectByNameIgnoreCase("hurricane winds");
			if (hurricanWindsOriginal == null) return;
			GameObject hurricanWinds = hurricanWindsOriginal.copy();
			if (remove()) {
				character.getGameObject().removeThisAttribute(Constants.BLOWS_TARGET);
				hurricanWinds.removeAttribute(SpellWrapper.SPELL_BLOCK_NAME,SpellWrapper.SECONDARY_TARGET);
				return;
			}
			if(character.getCurrentLocation() == null || character.getCurrentLocation().tile == null) return;
			ArrayList adjacentTiles = new ArrayList(character.getCurrentLocation().tile.getAllAdjacentTiles());
			TileComponent targetTile = (TileComponent) adjacentTiles.get(RandomNumber.getRandom(adjacentTiles.size()));
			SpellWrapper spell = new SpellWrapper(hurricanWinds);
			spell.setSecondaryTarget(targetTile.getGameObject());
			character.getGameObject().setThisAttribute(Constants.BLOWS_TARGET,hurricanWinds.getStringId());
			return;
		} else if (_eoc == EffectOnCharacter.Lost) {
			effect = Constants.SP_MOVE_IS_RANDOM;
		} else if (_eoc == EffectOnCharacter.NegativeAura) {
			effect = Constants.NEGATIVE_AURA;
		} else if (_eoc == EffectOnCharacter.PeaceWithNature) {
			effect = Constants.PEACE_WITH_NATURE;
		} else if (_eoc == EffectOnCharacter.Premonition) {
			effect = Constants.CHOOSE_TURN;
		} else if (_eoc == EffectOnCharacter.Prophecy) {
			effect = Constants.DAYTIME_ACTIONS;
		} else if (_eoc == EffectOnCharacter.ProtectionFromMagic) {
			effect = Constants.MAGIC_PROTECTION;
		} else if (_eoc == EffectOnCharacter.Shrink) {
			effect = Constants.SHRINK;
		} else if (_eoc == EffectOnCharacter.Slowed) {
			effect = Constants.SLOWED;
		} else if (_eoc == EffectOnCharacter.SpiritGuide) {
			effect = Constants.SPIRIT_GUIDE;
		} else if (_eoc == EffectOnCharacter.TrackersSense) {
			effect = Constants.TRACKERS_SENSE;
		} else if (_eoc == EffectOnCharacter.ValeWalker) {
			effect = Constants.VALE_WALKER;
		} else if (_eoc == EffectOnCharacter.WaterRun) {
			effect = Constants.WATER_RUN;
		} else {
			return;
		}
		
		if (remove()) {
			character.getGameObject().removeThisAttribute(effect);
			return;
		}
		character.getGameObject().setThisAttribute(effect);
	}
	
	private EffectOnCharacter getSpell() {
		return EffectOnCharacter.valueOf(getString(SPELL));
	}
	
	private boolean remove() {
		return getBoolean(REMOVE);
	}
	
	public String getDescription() {
		if (remove()) {
			return "Removes the spell effect "+getSpell()+" from the character.";
		}
		return "Applies the spell effect "+getSpell()+" on the character (with unlimited duration).";
	}
	public RewardType getRewardType() {
		return RewardType.SpellEffectOnCharacter;
	}

}