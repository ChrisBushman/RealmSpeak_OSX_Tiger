package com.robin.magic_realm.components.quest.reward;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.quest.SpellCreator;
import com.robin.magic_realm.components.utility.SpellUtility;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class QuestRewardSpellEffectSummon extends QuestReward {
	
	public static final String SUMMON_TYPE = "_type";
	public static final String REMOVE = "_unsommon";
		
	public QuestRewardSpellEffectSummon(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame,CharacterWrapper character) {
		String spell;
		SpellUtility.SummonType _st = getSummonType();
		if (_st == SpellUtility.SummonType.animal) {
			spell = "Summon Animal";
		} else if (_st == SpellUtility.SummonType.elemental) {
			spell = "Summon Elemental";
		} else if (_st == SpellUtility.SummonType.undead) {
			spell = "Raise Dead";
		} else if (_st == SpellUtility.SummonType.demon) {
			spell = "Summon Demon";
		} else {
			return;
		}
		SpellWrapper spellWrapper = SpellCreator.CreateSpellWrapper(spell, character);
		if (spellWrapper == null) return;
		
		if (remove()) {
			SpellUtility.unsummonCompanions(spellWrapper);
			return;
		}
		SpellUtility.summonRandomCompanion(frame, character.getGameObject(), character, spellWrapper, getSummonType().toString());
	}
	
	private SpellUtility.SummonType getSummonType() {
		return SpellUtility.SummonType.valueOf(getString(SUMMON_TYPE));
	}
	private boolean remove() {
		return getBoolean(REMOVE);
	}
	
	public String getDescription() {
		if (remove()) {
			return "Removes the creatures from the character.";
		}
		return "Summons creatures for the character (demons are evil).";
	}

	public RewardType getRewardType() {
		return RewardType.SpellEffectSummon;
	}
}