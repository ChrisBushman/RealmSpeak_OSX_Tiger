package com.robin.magic_realm.components.quest.requirement;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.general.swing.DieRoller;
import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.quest.DieRollType;
import com.robin.magic_realm.components.utility.DieRollBuilder;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRequirementHideResult extends QuestRequirement {

	public static final String DIE_ROLL = "_dr";
	
	public QuestRequirementHideResult(GameObject go) {
		super(go);
	}

	protected boolean testFulfillsRequirement(JFrame frame, CharacterWrapper character, QuestRequirementParams reqParams) {
		DieRoller roller = DieRollBuilder.getDieRollBuilder(frame,character).createHideRoller();
		if (roller.getHighDieResult() < getDieRoll()) { 
			return true;
		}
		return false;
	}

	protected String buildDescription() {
		if (getString(DIE_ROLL)!=DieRollType.Random.toString()) {
			return "Requires a successful hide roll below or equal to "+getDieRoll()+".";
		}
		return "Requires a successful hide roll below or equal to a random value (1-6).";
	}

	public RequirementType getRequirementType() {
		return RequirementType.HideResult;
	}
	
	public int getDieRoll() {
		String dieRoll = getString(DIE_ROLL);
		DieRollType _drt = DieRollType.valueOf(dieRoll);
		if (_drt == DieRollType.One) {
			return 1;
		} else if (_drt == DieRollType.Two) {
			return 2;
		} else if (_drt == DieRollType.Three) {
			return 3;
		} else if (_drt == DieRollType.Four) {
			return 4;
		} else if (_drt == DieRollType.Five) {
			return 5;
		} else if (_drt == DieRollType.Six) {
			return 6;
		} else {
			return RandomNumber.getDieRoll(6);
		}
	}
}