package com.robin.magic_realm.components.quest.requirement;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.game.objects.GamePool;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.quest.ArmoredType;
import com.robin.magic_realm.components.quest.QuestConstants;
import com.robin.magic_realm.components.quest.QuestStep;
import com.robin.magic_realm.components.quest.TargetValueType;
import com.robin.magic_realm.components.quest.VulnerabilityType;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.DayKey;

public class QuestRequirementKill extends QuestRequirement {
	
	private static Logger logger = Logger.getLogger(QuestRequirementKill.class.getName());

	public static final String REGEX_FILTER = "_regex";
	public static final String REQUIRE_MARK = "_rqm";
	public static final String TARGET_VALUE_TYPE = "_tvt";
	public static final String VALUE = "_rq";
	public static final String VULNERABILITY = "_vy";
	public static final String ARMORED = "_arm";
	private static final String STEP_ONLY_KILLS = "_sok"; // compatibility for old quests
	public static final String REGEX_DESCRIPTION = "_regex_description";
	public static final String KILL_CHARACTERS = "_kill_characters";
	
	public QuestRequirementKill(GameObject go) {
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
		
		boolean requireMark = getRequireMark();
		String questId = getParentQuest().getGameObject().getStringId();
		String regex = getRegExFilter().trim();
		Pattern pattern = regex.length()==0?null:Pattern.compile(regex);
		
		int numberOfKillsNeeded = getValue();
		if (numberOfKillsNeeded==QuestConstants.ALL_VALUE) {
			GamePool pool = new GamePool(getGameData().getGameObjects());
			for (java.util.Iterator _j14it2334 = (pool.find("vulnerability,!weight")).iterator(); _j14it2334.hasNext(); ) {
			  GameObject go = (GameObject) _j14it2334.next(); // stuff that can be killed has a vulnerability, including characters!
				if (!killCharacters() && !go.hasThisAttribute(Constants.DENIZEN)) continue;
				if (killCharacters() && !go.hasThisAttribute(RealmComponent.CHARACTER)) continue;
				if (pattern!=null && !pattern.matcher(go.getName()).find()) continue;
				if (requireMark) {
					String mark = go.getThisAttribute(QuestConstants.QUEST_MARK);
					if (mark==null || !mark.equals(questId)) continue;
				}
				if (getVulnerability()!=VulnerabilityType.Any && VulnerabilityType.valueOf(go.getThisAttribute("vulnerability"))!=getVulnerability()) continue;
				if ((getArmored() == ArmoredType.Armored && !go.hasThisAttribute("armored"))|| (getArmored() == ArmoredType.Unarmored && go.hasThisAttribute("armored"))) continue;
				if (!go.hasThisAttribute(Constants.DEAD)) {
					logger.fine(go.getName()+" is still alive.");
					return false;
				}
			}
			return true;
		}
		
		ArrayList validKills = new ArrayList();
		ArrayList allDayKeys = character.getAllDayKeys();
		if (allDayKeys==null) {
			logger.fine("Character hasn't had a turn yet.");
			return false;
		}
		for (java.util.Iterator _j14it2335 = (allDayKeys).iterator(); _j14it2335.hasNext(); ) {
		  String dayKeyString = (String) _j14it2335.next();
			DayKey dayKey = new DayKey(dayKeyString);
			if (dayKey.before(earliestTime)) continue; // ignore kills on days before the earliest allowable date
			
			ArrayList kills = character.getKills(dayKeyString);
			for (java.util.Iterator _j14it2336 = (kills).iterator(); _j14it2336.hasNext(); ) {
			  GameObject kill = (GameObject) _j14it2336.next();
				if (!killCharacters() && !kill.hasThisAttribute(Constants.DENIZEN)) continue;
				if (killCharacters() && !kill.hasThisAttribute(RealmComponent.CHARACTER)) continue;
				if (pattern!=null && !pattern.matcher(kill.getName()).find()) continue;
				if (requireMark) {
					String mark = kill.getThisAttribute(QuestConstants.QUEST_MARK);
					if (mark==null || !mark.equals(questId)) continue;
				}
				if (getVulnerability()!=VulnerabilityType.Any && VulnerabilityType.valueOf(kill.getThisAttribute("vulnerability"))!=getVulnerability()) continue;
				if ((getArmored() == ArmoredType.Armored && !kill.hasThisAttribute("armored")) || (getArmored() == ArmoredType.Unarmored && kill.hasThisAttribute("armored"))) continue;
				
				validKills.add(kill);
			}
		}
		
		boolean ret = validKills.size()>=numberOfKillsNeeded;
		if (!ret) {
			logger.fine(validKills.size()+" is not enough kills.  Expecting at least "+numberOfKillsNeeded+".");
		}
		return ret;
	}
	
	protected String buildDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("Must kill ");
		int val = getValue();
		boolean mark = getRequireMark();
		sb.append(mark?"":"any ");
		sb.append(val==QuestConstants.ALL_VALUE?"ALL":""+val);
		sb.append(mark?" marked":"");
		if (getArmored() != ArmoredType.Any) {
			sb.append(" "+getArmored().toString().toLowerCase());
		}
		if (killCharacters()) {
			sb.append(" character");
		}
		else {
			sb.append(" denizen");
		}
		sb.append(val==1?"":"s");
		if(getRegExFilter().length() != 0) {
			sb.append(" that match");
			sb.append(val==1?"es ":" ");
			if (getRegExDescription().length() > 0) {
				sb.append(getRegExDescription());
			} else {
				sb.append(getRegExFilter());
			}
		}
		sb.append(getVulnerability()!=VulnerabilityType.Any?" with vulnerability "+getVulnerability():"");
		sb.append(".");
		return sb.toString();
	}

	public RequirementType getRequirementType() {
		return RequirementType.Kill;
	}
	
	private String getRegExFilter() {
		return getString(REGEX_FILTER);
	}
	public TargetValueType getTargetValueType() {
		if (getString(TARGET_VALUE_TYPE) == null) { // compatibility for old quests
			if (getBoolean(STEP_ONLY_KILLS)) { 
				return TargetValueType.Step;
			}
			return TargetValueType.Quest;
		}
		return TargetValueType.valueOf(getString(TARGET_VALUE_TYPE));
	}
	private boolean getRequireMark() {
		return getBoolean(REQUIRE_MARK);
	}
	private int getValue() {
		return getInt(VALUE);
	}
	private VulnerabilityType getVulnerability() {
		String vulnerability = getString(VULNERABILITY);
		if (vulnerability == null || vulnerability.matches("undefined")) { // compatibility for old quests
			return VulnerabilityType.Any;
		}
		return VulnerabilityType.valueOf(getString(VULNERABILITY));
	}
	private ArmoredType getArmored() {
		String armor = getString(ARMORED);
		if (armor == null) { // compatibility for old quests
			return ArmoredType.Any;
		}
		return ArmoredType.valueOf(getString(ARMORED));
	}
	
	public String getRegExDescription() {
		if (getGameObject().hasAttribute(getBlockName(),REGEX_DESCRIPTION)) {
			return getString(REGEX_DESCRIPTION);
		}
		return "";
	}
	private boolean killCharacters() {
		return getBoolean(KILL_CHARACTERS);
	}
}