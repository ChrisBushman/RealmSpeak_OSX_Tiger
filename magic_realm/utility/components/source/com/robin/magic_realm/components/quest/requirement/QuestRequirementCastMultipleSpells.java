package com.robin.magic_realm.components.quest.requirement;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.quest.QuestStep;
import com.robin.magic_realm.components.quest.TargetValueType;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.DayKey;

public class QuestRequirementCastMultipleSpells extends QuestRequirement {
	public static final String NUMBER_OF_SPELLS = "_nos";
	public static String UNIQUE = "_unique";
	public static final String TARGET_VALUE_TYPE = "_tvt";

	public QuestRequirementCastMultipleSpells(GameObject go) {
		super(go);
	}

	protected boolean testFulfillsRequirement(JFrame frame, CharacterWrapper character, QuestRequirementParams reqParams) {
		QuestStep step = getParentStep();
		DayKey earliestTime = new DayKey(1,1);
		TargetValueType tvt = getTargetValueType();
		if (tvt == TargetValueType.Game) {
			earliestTime = new DayKey(1,1);
		} else if (tvt == TargetValueType.Quest) {
			earliestTime = step.getQuestStartTime();
		} else if (tvt == TargetValueType.Step) {
			earliestTime = step.getQuestStepStartTime();
		} else if (tvt == TargetValueType.Day) {
			earliestTime = new DayKey(character.getCurrentDayKey());
		}
		
		List spellsCasted = new ArrayList();
		ArrayList allDayKeys = character.getAllDayKeys();
		if (allDayKeys==null) {
			return false;
		}
		for (java.util.Iterator _j14it2342 = (allDayKeys).iterator(); _j14it2342.hasNext(); ) {
		  String dayKeyString = (String) _j14it2342.next();
			DayKey dayKey = new DayKey(dayKeyString);
			if (dayKey.before(earliestTime)) continue; // ignore spells on days before the earliest allowable date
			for (java.util.Iterator _j14it2343 = (character.getCastedSpells(dayKeyString)).iterator(); _j14it2343.hasNext(); ) {
			  GameObject spell = (GameObject) _j14it2343.next();
				if (!getUnique() || !spellsCasted.contains(spell.getName())) {
					spellsCasted.add(spell.getName());
				}
			}
		}

		return spellsCasted.size() >= getAmount();
	}

	public RequirementType getRequirementType() {
		return RequirementType.CastMultipleSpells;
	}

	protected String buildDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("Must cast "+getAmount());
		if (getUnique()) {
			sb.append(" different");
		}
		sb.append(" spell(s).");
		return sb.toString();
	}

	public int getAmount() {
		return getInt(NUMBER_OF_SPELLS);
	}
	public boolean getUnique() {
		return getBoolean(UNIQUE);
	}
	public TargetValueType getTargetValueType() {
		if (getString(TARGET_VALUE_TYPE) == null) { // compatibility for old quests
			return TargetValueType.Game;
		}
		return TargetValueType.valueOf(getString(TARGET_VALUE_TYPE));
	}
}