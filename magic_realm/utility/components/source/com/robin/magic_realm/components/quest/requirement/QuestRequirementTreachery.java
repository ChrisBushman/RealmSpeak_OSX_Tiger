package com.robin.magic_realm.components.quest.requirement;

import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.CombatWrapper;

public class QuestRequirementTreachery extends QuestRequirement {
	public static final String REGEX_FILTER = "_regex";
	public static final String KILL_IN_COMBAT = "kill_in_combat";
	public static final String NATIVE_ONLY = "native_only";

	public QuestRequirementTreachery(GameObject go) {
		super(go);
	}

	protected boolean testFulfillsRequirement(JFrame frame, CharacterWrapper character, QuestRequirementParams reqParams) {
		CombatWrapper combatCharacter = new CombatWrapper(character.getGameObject());
		ArrayList ids = combatCharacter.getBetrayedIds();
		if (ids==null || ids.isEmpty()) {
			return false;
		}
		Pattern pattern = Pattern.compile(getRegExFilter());
		for (java.util.Iterator _j14it2319 = (combatCharacter.getBetrayedIds()).iterator(); _j14it2319.hasNext(); ) {
		  String id = (String) _j14it2319.next();
			GameObject victim = character.getGameData().getGameObject(id);
			if (getRegExFilter().length() == 0 || pattern.matcher(victim.getName()).find()) {
				if (killInCombat()) {
					boolean killedVictimInCombat = false;
					ArrayList kills = character.getKills(character.getCurrentDayKey());
					for (java.util.Iterator _j14it2320 = (kills).iterator(); _j14it2320.hasNext(); ) {
					  GameObject kill = (GameObject) _j14it2320.next();
						if (id.matches(kill.getStringId())) {
							killedVictimInCombat = true;
							break;
						}
					}
					if (!killedVictimInCombat) {
						continue;
					}
				}
				
				CombatWrapper combatVictim = new CombatWrapper(victim);
				ArrayList traitors = combatVictim.getBetrayedByIds();
				if (traitors == null || traitors.isEmpty()) {
					continue;
				}
				for (java.util.Iterator _j14it2321 = (traitors).iterator(); _j14it2321.hasNext(); ) {
				  String traitorId = (String) _j14it2321.next();
					if (traitorId.matches(character.getGameObject().getStringId())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public RequirementType getRequirementType() {
		return RequirementType.Treachery;
	}

	protected String buildDescription() {
		String regex = getRegExFilter();
		StringBuffer sb = new StringBuffer();
		sb.append("Must commit treachery");
		if (nativeOnly() && (regex == null || regex.trim().length()==0)) {
			sb.append(" to a native");
		}
		if (regex != null && regex.trim().length() > 0) {
			sb.append(" to ");
			sb.append(regex);
		}
		if (killInCombat()) {
			sb.append(" and kill it");
		}
		sb.append(".");
		return sb.toString();
	}

	public boolean killInCombat() {
		return getBoolean(KILL_IN_COMBAT);
	}
	public boolean nativeOnly() {
		return getBoolean(NATIVE_ONLY);
	}
	public String getRegExFilter() {
		return getString(REGEX_FILTER);
	}
}