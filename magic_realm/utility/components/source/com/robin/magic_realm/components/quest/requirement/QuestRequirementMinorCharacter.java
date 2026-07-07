package com.robin.magic_realm.components.quest.requirement;

import java.util.logging.Logger;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRequirementMinorCharacter extends QuestRequirement {
	private static Logger logger = Logger.getLogger(QuestRequirementMinorCharacter.class.getName());
	
	public static final String MINOR_CHARACTER = "_mcn";

	public QuestRequirementMinorCharacter(GameObject go) {
		super(go);
	}

	protected boolean testFulfillsRequirement(JFrame frame, CharacterWrapper character, QuestRequirementParams reqParams) {
		String test = getMinorCharacterName();
		for (java.util.Iterator _j14it2303 = (character.getMinorCharacters()).iterator(); _j14it2303.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2303.next();
			if (test.equals(go.getName())) {
				return true;
			}
		}
		logger.fine(test+" is not present.");
		return false;
	}

	public RequirementType getRequirementType() {
		return RequirementType.MinorCharacter;
	}

	protected String buildDescription() {
		return "Only when "+getMinorCharacterName()+" is present.";
	}
	
	public String getMinorCharacterName() {
		return getString(MINOR_CHARACTER);
	}
}