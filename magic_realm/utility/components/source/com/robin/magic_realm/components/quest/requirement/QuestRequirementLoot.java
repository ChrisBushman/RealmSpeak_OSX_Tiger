package com.robin.magic_realm.components.quest.requirement;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.game.objects.GamePool;
import com.robin.magic_realm.components.quest.QuestConstants;
import com.robin.magic_realm.components.quest.TreasureType;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRequirementLoot extends QuestRequirement {
	private static Logger logger = Logger.getLogger(QuestRequirementLoot.class.getName());
	
	public static final String TREASURE_TYPE = "_tt";
	public static final String REGEX_FILTER = "_regex";
	public static final String REQ_MARK = "_req_mark";
	public static final String ADD_MARK = "_add_mark";
	public static final String REQ_ABILITY = "_req_ability";
	
	public QuestRequirementLoot(GameObject go) {
		super(go);
	}
	
	protected boolean testFulfillsRequirement(JFrame frame,CharacterWrapper character,QuestRequirementParams reqParams) {
		if (reqParams!=null && "Loot".equals(reqParams.actionName)) {
			ArrayList matches = filterObjectsForRequirement(character,reqParams.objectList,logger);
			if (markItems() && !matches.isEmpty() ) {
				for (java.util.Iterator _j14it2292 = (matches).iterator(); _j14it2292.hasNext(); ) {
				  GameObject item = (GameObject) _j14it2292.next();
					item.setThisAttribute(QuestConstants.QUEST_MARK,getParentQuest().getGameObject().getStringId());
				}
			}
			return !matches.isEmpty();
		}
		logger.fine(character.getName()+" did not Loot.");
		return false;
	}
	
	protected ArrayList filterObjectsForRequirement(CharacterWrapper character,ArrayList objects,Logger myLogger) {
		ArrayList matches = new ArrayList();
		if (objects.isEmpty()) {
			myLogger.fine("No items to test.");
			return matches;
		}
		GamePool pool = new GamePool(objects);
		String query = null;
		TreasureType tt = getTreasureType();
		if (tt == TreasureType.Any) {
			query = "";
		} else if (tt == TreasureType.Artifact || tt == TreasureType.Book || tt == TreasureType.Boots
				|| tt == TreasureType.Gloves || tt == TreasureType.Great || tt == TreasureType.Scroll) {
			query = tt.toString().toLowerCase();
		} else if (tt == TreasureType.Large) {
			query="treasure=large";
		} else if (tt == TreasureType.MagicArmor) {
			query="armor,magic";
		} else if (tt == TreasureType.MagicWeapon) {
			query="weapon,magic";
		} else if (tt == TreasureType.Small) {
			query="treasure=small";
		} else if (tt == TreasureType.Treasure) {
			query="treasure";
		} else if (tt == TreasureType.TWT) {
			query="treasure_within_treasure";
		} else if (tt == TreasureType.Armor) {
			query="armor,!character";
		} else if (tt == TreasureType.Weapon) {
			query="weapon,!character";
		}
		ArrayList typeMatches = query==null?objects:pool.find(query);
		if (!typeMatches.isEmpty()) {
			String regex = getRegExFilter();
			Pattern pattern = regex==null || regex.trim().length()==0?null:Pattern.compile(regex);
			String questId = getParentQuest().getGameObject().getStringId();
			for (java.util.Iterator _j14it2293 = (typeMatches).iterator(); _j14it2293.hasNext(); ) {
			  GameObject go = (GameObject) _j14it2293.next();
				if (pattern==null || pattern.matcher(go.getName()).find()) {
					if (requiresMark()) {
						String mark = go.getThisAttribute(QuestConstants.QUEST_MARK);
						if (mark==null || !mark.equals(questId)) continue;
					}
					if (getRequiredAbility()!=null && getRequiredAbility().length() > 0) {
						if (!go.hasAllKeyVals(getRequiredAbility())) {
							continue;
						}
					}
					matches.add(go);
				}
			}
			if (matches.isEmpty()) {
				myLogger.fine("None of the treasures tested had the required ability/mark and matched the regex: "+regex);
			}
		}
		else {
			myLogger.fine("None of the treasures tested were the correct treasure type: "+tt.toString());
		}
		return matches;
	}
	
	public RequirementType getRequirementType() {
		return RequirementType.Loot;
	}
	
	protected String buildDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("Must loot a");
		TreasureType tt = getTreasureType();
		if (tt!=TreasureType.Any) {
			sb.append(" ");
			sb.append(getTreasureType().toString().toLowerCase());
		}
		sb.append(" treasure");
		String regex = getRegExFilter();
		if (regex!=null && regex.trim().length()>0) {
			sb.append(", matching /");
			sb.append(regex);
			sb.append("/");
		}
		if (getRequiredAbility()!=null && getRequiredAbility().length() > 0) {
			sb.append(" with the ability "+getRequiredAbility());
		}
		sb.append(".");
		return sb.toString();
	}
	
	public TreasureType getTreasureType() {
		return TreasureType.valueOf(getString(TREASURE_TYPE));
	}
	public String getRegExFilter() {
		return getString(REGEX_FILTER);
	}
	public boolean requiresMark() {
		return getBoolean(REQ_MARK);
	}
	public String getRequiredAbility() {
		return getString(REQ_ABILITY);
	}
	public boolean markItems() {
		return getBoolean(ADD_MARK);
	}
}