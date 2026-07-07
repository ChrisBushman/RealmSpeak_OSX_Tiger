package com.robin.magic_realm.components.quest.requirement;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.quest.TargetValueType;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.DayKey;

public class QuestRequirementPath extends QuestRequirement {
	private static Logger logger = Logger.getLogger(QuestRequirementPath.class.getName());

	public static final String PATH = "_path";
	public static final String TIME_RESTRICTION = "_tr";
	public static final String CHECK_REVERSE = "_cr";
	public static final String ALLOW_TRANSPORT = "_at";

	public QuestRequirementPath(GameObject go) {
		super(go);
	}

	protected boolean testFulfillsRequirement(JFrame frame, CharacterWrapper character, QuestRequirementParams reqParams) {
		String path = getPathString();
		if (path==null || path.trim().length()==0) {
			logger.fine("QUEST ERROR:  Invalid path defined in Quest file.");
			return false;
		}
		path = path.trim();
		
		ArrayList history = character.getMoveHistory();
		if (history==null || history.size()==0) {
			logger.fine("Character hasn't gone anywhere yet.");
			return false;
		}
		ArrayList historyDays = character.getMoveHistoryDayKeys();
		if (history.size()!=historyDays.size()) {
			logger.fine("QUEST ERROR:  history is different size than historyDays.");
			return false;
		}
		
		DayKey startKey = null;
		TargetValueType _tr = getTimeRestriction();
		if (_tr == TargetValueType.Quest) {
			startKey = getParentStep().getQuestStartTime();
		} else if (_tr == TargetValueType.Step) {
			startKey = getParentStep().getQuestStepStartTime();
		} else if (_tr == TargetValueType.Game) {
			startKey = new DayKey(1,1);
		} else {
			startKey = new DayKey(character.getCurrentDayKey());
		}
		
		boolean ignoreJumps = isAllowTransport();
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<history.size();i++) {
			if (startKey!=null) {
				DayKey dayKey = new DayKey((String) historyDays.get(i));
				if (dayKey.before(startKey)) continue;
			}
			String location = (String) history.get(i);
			if (CharacterWrapper.MOVE_HISTORY_DAY.equals(location)) continue; // always ignore the days
			if (ignoreJumps && CharacterWrapper.MOVE_HISTORY_JUMP.equals(location)) continue; // ignore the jumps only if transport is allowed
			if (sb.length()>0) sb.append(" ");
			sb.append(location);
		}
		
		String charPath = sb.toString();
		
		boolean matchForward = testPath(charPath,path);
		boolean matchReverse = !matchForward && isCheckReverse() && testPath(charPath,getReversePath());
		
		if (!matchForward && !matchReverse) {
			logger.fine("Character path ("+charPath+") doesn't contain specified path ("+path+")");
			return false;
		}
		return true;
	}
	
	public static boolean testPath(String charPath,String testPath) {
		
		charPath = " "+charPath;
		String[] each = testPath.split(" "); // "1 2 3 4 5"
		String[] pathSections = new String[each.length - 1]; // "1 2" "2 3" "3 4" "4 5"
		if (each.length == 1) {
			String clearing = (String) Array.get(each, 0);
			return charPath.indexOf(" "+clearing) >= 0;
		}
		
		for (int i=0;i<pathSections.length;i++) {
			StringBuffer sb = new StringBuffer(each[i]);
			sb.append(' ');
			sb.append(each[i+1]);
			pathSections[i] = sb.toString();
		}
		
		int lastIndex = -1;
		for (int _j14i2318 = 0; _j14i2318 < pathSections.length; _j14i2318++) {
		  String section = pathSections[_j14i2318];
			int index = charPath.lastIndexOf(" "+section);
			if (index<0 || index<=lastIndex) return false;
			lastIndex = index;
		}
		return true;
	}

	public RequirementType getRequirementType() {
		return RequirementType.Path;
	}

	protected String buildDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("Must follow path");
		TargetValueType _tr2 = getTimeRestriction();
		if (_tr2 == TargetValueType.Quest) {
			sb.append(" during the quest");
		} else if (_tr2 == TargetValueType.Step) {
			sb.append(" during the step");
		} else {
			sb.append(" during the current day");
		}
		if (!isAllowTransport()) {
			sb.append(" without teleporting");
		}
		if (isCheckReverse()) {
			sb.append(" in either direction");
		}
		sb.append(": ");
		sb.append(getPathString());
		return sb.toString();
	}
	
	public String getPathString() {
		return getString(PATH);
	}
	
	public String getReversePath() {
		String path = getPathString();
		if (path==null) return null;
		String[] ret = path.split(" ");
		StringBuffer sb = new StringBuffer();
		for(int i=ret.length-1;i>=0;i--) {
			sb.append(ret[i]);
			sb.append(' ');
		}
		return sb.toString().trim();
	}
	
	public TargetValueType getTimeRestriction() {
		return TargetValueType.valueOf(getString(TIME_RESTRICTION));
	}
	
	public boolean isCheckReverse() {
		return getBoolean(CHECK_REVERSE);
	}
	
	public boolean isAllowTransport() {
		return getBoolean(ALLOW_TRANSPORT);
	}
}