package com.robin.magic_realm.components.quest.requirement;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.quest.QuestStep;
import com.robin.magic_realm.components.quest.TargetValueType;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.DayKey;

public class QuestRequirementKillGuardian extends QuestRequirement {
	
	private static Logger logger = Logger.getLogger(QuestRequirementKillGuardian.class.getName());

	public static final String GUARDIAN_AND_SITE = "_guardian_site";
	public static final String TARGET_VALUE_TYPE = "_tvt";
	
	public QuestRequirementKillGuardian(GameObject go) {
		super(go);
	}

	protected boolean testFulfillsRequirement(JFrame frame,CharacterWrapper character,QuestRequirementParams reqParams) {
		logger.fine(buildDescription());
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
		ArrayList allDayKeys = character.getAllDayKeys();
		if (allDayKeys==null) {
			logger.fine("Character hasn't had a turn yet.");
			return false;
		}
		for (java.util.Iterator _j14it2331 = (allDayKeys).iterator(); _j14it2331.hasNext(); ) {
		  String dayKeyString = (String) _j14it2331.next();
			DayKey dayKey = new DayKey(dayKeyString);
			if (dayKey.before(earliestTime)) continue; // ignore kills on days before the earliest allowable date
			ArrayList kills = character.getKills(dayKeyString);
			for (java.util.Iterator _j14it2332 = (kills).iterator(); _j14it2332.hasNext(); ) {
			  GameObject kill = (GameObject) _j14it2332.next();
				if (kill.getName().toLowerCase().matches(getGuardian().trim().toLowerCase())
						&& kill.getThisAttribute("setup_start").toLowerCase().matches(getSite().toLowerCase())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	protected String buildDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("Must kill ");
		sb.append(getGuardian()+"("+getSite()+")");
		sb.append(".");
		return sb.toString();
	}

	public RequirementType getRequirementType() {
		return RequirementType.KillGuardian;
	}
	
	public String getGuardian() {
		StringTokenizer tokenizer = new StringTokenizer(getString(GUARDIAN_AND_SITE), "(");
		return tokenizer.nextToken();
	}
	
	public String getSite() {
		StringTokenizer tokenizer = new StringTokenizer(getString(GUARDIAN_AND_SITE), "(");
		tokenizer.nextToken();
		String siteWithBracket = tokenizer.nextToken();
		return siteWithBracket.replaceAll("\\(","").replaceAll("\\)","");
	}
	
	public TargetValueType getTargetValueType() {
		return TargetValueType.valueOf(getString(TARGET_VALUE_TYPE));
	}

}