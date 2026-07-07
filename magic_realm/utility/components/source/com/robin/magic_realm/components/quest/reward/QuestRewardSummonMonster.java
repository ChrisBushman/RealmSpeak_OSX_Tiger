package com.robin.magic_realm.components.quest.reward;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.ClearingDetail;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.quest.QuestConstants;
import com.robin.magic_realm.components.quest.QuestLocation;
import com.robin.magic_realm.components.quest.QuestStep;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.SetupCardUtility;
import com.robin.magic_realm.components.utility.TemplateLibrary;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRewardSummonMonster extends QuestReward {
	private static Logger logger = Logger.getLogger(QuestStep.class.getName());
	public static final String MONSTER_NAME = "_mn";
	public static final String SUMMON_TYPE = "_summon_from_chart";
	public static final String RANDOM_CLEARING = "_rc";
	public static final String SUMMON_TO_LOCATION = "_summon_loc";
	public static final String RANDOM_LOCATION = "_rnd_loc";
	public static final String LOCATION = "_loc";
	public static final String MARK = "_mark";
	
	public static final class SummonType {
		private final String _name;
		private final int _ordinal;
		private SummonType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final SummonType NewMonster = new SummonType("NewMonster", 0);
		public static final SummonType SummonFromSetupCard = new SummonType("SummonFromSetupCard", 1);
		public static final SummonType SummonFromSetupCardOrMap = new SummonType("SummonFromSetupCardOrMap", 2);

		private static final SummonType[] _VALUES = { NewMonster, SummonFromSetupCard, SummonFromSetupCardOrMap };
		public static SummonType[] values() { SummonType[] r = new SummonType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static SummonType valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	
	public QuestRewardSummonMonster(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame,CharacterWrapper character) {
		GameObject monster = null;
		if (getSummonType() == SummonType.NewMonster) {
			GameObject template = TemplateLibrary.getSingleton().getCompanionTemplate(getMonsterKeyName(),getMonsterQuery());
			monster = TemplateLibrary.getSingleton().createCompanionFromTemplate(getGameData(),template);
			monster.removeThisAttribute(Constants.COMPANION);
			monster.setThisAttribute(Constants.SUMMONED);
		}
		else {
			ArrayList monsters = getGameData().getGameObjectsByName(getMonsterKeyName());
			for (java.util.Iterator _j14it2348 = (monsters).iterator(); _j14it2348.hasNext(); ) {
			  GameObject validMonster = (GameObject) _j14it2348.next();
				if (getSummonType() == SummonType.SummonFromSetupCard && validMonster.getHeldBy() != SetupCardUtility.getDenizenHolder(validMonster)) continue;
				monster = validMonster;
				SetupCardUtility.resetDenizen(monster);
				break;
			}
			if (monster == null) return;
		}
		
		if (locationOnly()) {
			QuestLocation loc = getQuestLocation();
			if (loc == null) return;
			ArrayList validLocations = new ArrayList();
			validLocations = loc.fetchAllLocations(frame, character, getGameData());
			if(validLocations.isEmpty()) {
				logger.fine("QuestLocation "+loc.getName()+" doesn't have any valid locations!");
				return;
			}
			if (markDenizens()) {
				monster.setThisAttribute(QuestConstants.QUEST_MARK,getParentQuest().getGameObject().getStringId());
			}
			if (randomLocation()) {
				int random = RandomNumber.getRandom(validLocations.size());
				TileLocation tileLocation = (TileLocation) validLocations.get(random);
				tileLocation.clearing.add(monster,null);
			}
			else {
				for (java.util.Iterator _j14it2349 = (validLocations).iterator(); _j14it2349.hasNext(); ) {
				  TileLocation location = (TileLocation) _j14it2349.next();
					GameObject summonMonster = monster.copy();
					location.clearing.add(summonMonster, null);
				}
			}
			return;
		}
		
		if (randomClearing()) {
			if (markDenizens()) {
				monster.setThisAttribute(QuestConstants.QUEST_MARK,getParentQuest().getGameObject().getStringId());
			}
			ArrayList clearings = character.getCurrentLocation().tile.getClearings();
			int random = RandomNumber.getRandom(clearings.size());
			((ClearingDetail) clearings.get(random)).add(monster,null);
			return;
		}
		character.getCurrentLocation().clearing.add(monster,null);
	}
	
	public ImageIcon getIcon() {
		GameObject template = TemplateLibrary.getSingleton().getCompanionTemplate(getMonsterKeyName(),getMonsterQuery());
		RealmComponent rc = RealmComponent.getRealmComponent(template);
		return rc.getIcon();
	}
	
	public String getDescription() {
		if (locationOnly() && getQuestLocation() != null) {
			StringBuffer sb = new StringBuffer();
			sb.append(getMonsterKeyName()+" is summoned in ");
			if (randomLocation()) {
				sb.append("a random clearing of ");
			}
			sb.append(getQuestLocation().getName());
			return sb.toString();
		}
		if (randomClearing()) {
			return getMonsterKeyName()+" is summoned in a random clearing of characters tile.";
		}
		return getMonsterKeyName()+" is summoned in the characters clearing.";
	}

	public RewardType getRewardType() {
		return RewardType.SummonMonster;
	}
	
	private String getMonsterKeyName() {
		return getString(QuestConstants.KEY_PREFIX+MONSTER_NAME).trim();
	}
	
	private String getMonsterQuery() {
		return getString(QuestConstants.VALUE_PREFIX+MONSTER_NAME).trim();
	}

	private SummonType getSummonType() {
		String summonType = getString(SUMMON_TYPE);
		if (summonType == null) { // compatibility for old quests
			return SummonType.NewMonster;
		}
		return SummonType.valueOf(getString(SUMMON_TYPE));
	}
	
	private boolean randomClearing() {
		return getBoolean(RANDOM_CLEARING);
	}
	
	private boolean locationOnly() {
		return getBoolean(SUMMON_TO_LOCATION);
	}
	
	private boolean randomLocation() {
		return getBoolean(RANDOM_LOCATION);
	}
	
	private boolean markDenizens() {
		return getBoolean(MARK);
	}
	
	public boolean usesLocationTag(String tag) {
		QuestLocation loc = getQuestLocation();
		return loc!=null && tag.equals(loc.getName());
	}
	
	public QuestLocation getQuestLocation() {
		String id = getString(LOCATION);
		if (id!=null) {
			GameObject go = getGameData().getGameObject(new Long(id));
			if (go!=null) {
				return new QuestLocation(go);
			}
		}
		return null;
	}

	public void setQuestLocation(QuestLocation location) {
		setString(LOCATION,location.getGameObject().getStringId());
	}
	
	public void updateIds(Hashtable lookup) {
		updateIdsForKey(lookup,LOCATION);
	}
}