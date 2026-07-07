package com.robin.magic_realm.components.quest.reward;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.game.objects.GamePool;
import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.ClearingDetail;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.quest.QuestConstants;
import com.robin.magic_realm.components.quest.QuestLocation;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.SetupCardUtility;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRewardSummonFromAppearanceToChit extends QuestReward {
	public static final String CHIT = "_chit";
	public static final String DENIZEN = "_denizen";
	public static final String SUMMON_LIVING_DENIZENS = "_summon_living_denizens";
	public static final String MAX_DENIZENS = "_max_denizens";
	public static final String MAX_DENIZEN_HOLDERS = "_max_denizen_holders";
	public static final String SUMMON_TO = "_summon_to";
	public static final String LOCATION = "_loc";
	public static final String MARK = "_mark";
	
	public static final class SummonTo {
		private final String _name;
		private final int _ordinal;
		private SummonTo(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final SummonTo Anywhere = new SummonTo("Anywhere", 0);
		public static final SummonTo CharactersClearing = new SummonTo("CharactersClearing", 1);
		public static final SummonTo CharactersTile = new SummonTo("CharactersTile", 2);
		public static final SummonTo QuestLocationClearings = new SummonTo("QuestLocationClearings", 3);
		public static final SummonTo QuestLocationTiles = new SummonTo("QuestLocationTiles", 4);

		private static final SummonTo[] _VALUES = { Anywhere, CharactersClearing, CharactersTile, QuestLocationClearings, QuestLocationTiles };
		public static SummonTo[] values() { SummonTo[] r = new SummonTo[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static SummonTo valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	
	public QuestRewardSummonFromAppearanceToChit(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame,CharacterWrapper character) {
		int summonedDenizens = 0;
		ArrayList summonedDenizenHolders = new ArrayList();
		ArrayList allQuestLocations = new ArrayList();
		if (toLocation()) {
			QuestLocation loc = getQuestLocation();
			if (loc == null) return;
			allQuestLocations = loc.fetchAllLocations(character.getGameData());
		}
		
		ArrayList validChits = new ArrayList();
		if (getChit().length() > 0) {
			ArrayList chits = character.getGameData().getGameObjectsByNameRegex(getChit());
			for (java.util.Iterator _j14it2425 = (chits).iterator(); _j14it2425.hasNext(); ) {
			  GameObject chit = (GameObject) _j14it2425.next();
				RealmComponent rc = RealmComponent.getRealmComponent(chit);
				if (!chit.hasThisAttribute("dwelling") && (rc == null || (!rc.isWarning() && !rc.isSound() && !rc.isTreasureLocation()))) continue; 
				
				SummonTo _st = summonTo();
				if (_st == SummonTo.CharactersClearing) {
					if (rc.getCurrentLocation() == character.getCurrentLocation()) {
						validChits.add(chit);
					}
				} else if (_st == SummonTo.CharactersTile) {
					if (rc.getCurrentLocation().tile == character.getCurrentLocation().tile) {
						validChits.add(chit);
					}
				} else if (_st == SummonTo.QuestLocationClearings) {
					for (java.util.Iterator _j14it2426 = allQuestLocations.iterator(); _j14it2426.hasNext(); ) {
						TileLocation tl = (TileLocation) _j14it2426.next();
						if (rc.getCurrentLocation() == tl) {
							validChits.add(chit);
							break;
						}
					}
				} else if (_st == SummonTo.QuestLocationTiles) {
					for (java.util.Iterator _j14it2427 = allQuestLocations.iterator(); _j14it2427.hasNext(); ) {
						TileLocation tl = (TileLocation) _j14it2427.next();
						if (rc.getCurrentLocation().tile == tl.tile) {
							validChits.add(chit);
							break;
						}
					}
				} else { // Anywhere + default
					validChits.add(chit);
				}
			}
		}
		else {
			if (toLocation()) {
				SummonTo _st2 = summonTo();
				if (_st2 == SummonTo.QuestLocationClearings) {
					for (java.util.Iterator _j14it2428 = (allQuestLocations).iterator(); _j14it2428.hasNext(); ) {
					  TileLocation tl = (TileLocation) _j14it2428.next();
						ArrayList clearingComponents = tl.clearing.getClearingComponents();
						for (java.util.Iterator _j14it2429 = (clearingComponents).iterator(); _j14it2429.hasNext(); ) {
						  RealmComponent rc = (RealmComponent) _j14it2429.next();
							if (rc.isWarning() || rc.isSound() || rc.isTreasureLocation() || rc.isDwelling()) {
								validChits.add(rc.getGameObject());
							}
						}
					}
				} else if (_st2 == SummonTo.QuestLocationTiles) {
					for (java.util.Iterator _j14it2430 = (allQuestLocations).iterator(); _j14it2430.hasNext(); ) {
					  TileLocation tl = (TileLocation) _j14it2430.next();
						ArrayList clearingComponents = tl.tile.getAllClearingComponents();
						for (java.util.Iterator _j14it2431 = (clearingComponents).iterator(); _j14it2431.hasNext(); ) {
						  RealmComponent rc = (RealmComponent) _j14it2431.next();
							if (rc.isWarning() || rc.isSound() || rc.isTreasureLocation() || rc.isDwelling()) {
								validChits.add(rc.getGameObject());
							}
						}
					}
				}
			}
			else {
				GamePool pool = new GamePool(getGameData().getGameObjects());
				ArrayList query = new ArrayList();
				query.add("warning");
				validChits.addAll(pool.find(query));
				query.clear();
				query.add("sound");
				validChits.addAll(pool.find(query));
				query.clear();
				query.add("treasure_location");
				validChits.addAll(pool.find(query));
				query.clear();
				query.add("dwelling");
				validChits.addAll(pool.find(query));
			}
		}

		ArrayList validDenizens = new ArrayList();
		if (getDenizenName().length() > 0) {
			ArrayList possibleDenizens = character.getGameData().getGameObjectsByNameRegex(getDenizenName());
			for (java.util.Iterator _j14it2432 = (possibleDenizens).iterator(); _j14it2432.hasNext(); ) {
			  GameObject denizen = (GameObject) _j14it2432.next();
				if (denizen.hasThisAttribute("vulnerability") && denizen.hasThisAttribute("setup_start")) {
					validDenizens.add(denizen);
				}
			}
		}
		else {
			GamePool pool = new GamePool(getGameData().getGameObjects());
			ArrayList query = new ArrayList();
			query.add("vulnerability");
			query.add("setup_start");
			validDenizens.addAll(pool.find(query));
		}
		
		for (java.util.Iterator _j14it2433 = (validChits).iterator(); _j14it2433.hasNext(); ) {
		  GameObject chit = (GameObject) _j14it2433.next();
			RealmComponent rcChit = RealmComponent.getRealmComponent(chit);	
			for (java.util.Iterator _j14it2434 = (validDenizens).iterator(); _j14it2434.hasNext(); ) {
			  GameObject denizen = (GameObject) _j14it2434.next();
				if (markDenizens()) {
					denizen.setThisAttribute(QuestConstants.QUEST_MARK,getParentQuest().getGameObject().getStringId());
				}				
				GameObject denizenHolder = SetupCardUtility.getDenizenHolder(denizen);
				RealmComponent rcDenizenHolder = RealmComponent.getRealmComponent(denizenHolder);
				String bn = denizenHolder.getThisAttribute(Constants.BOARD_NUMBER);
				if (bn != null && denizen.getThisAttribute(Constants.BOARD_NUMBER) != bn) continue;
				ClearingDetail clearingSummonTo = null;
				if (chit.hasThisAttribute("dwelling") && denizen.getThisAttribute("setup_start").toLowerCase().matches(chit.getName().toLowerCase())) {
					clearingSummonTo = rcDenizenHolder.getCurrentLocation().clearing;
				}
				if (rcChit.isTreasureLocation() && denizen.getThisAttribute("setup_start").toLowerCase().matches(chit.getName().toLowerCase())) {
					clearingSummonTo = rcDenizenHolder.getCurrentLocation().clearing;
				}
				if (rcChit.isSound()) {
					String soundsList = denizenHolder.getThisAttribute("summon");
					if (soundsList == null) continue;
					List sounds = Arrays.asList(soundsList.split("\\s*,\\s*"));
					String sound = chit.getThisAttribute("sound").toLowerCase();
					for (java.util.Iterator _j14it2435 = (sounds).iterator(); _j14it2435.hasNext(); ) {
					  String soundName = (String) _j14it2435.next();
						if (soundName.toLowerCase().trim().matches(sound)) {
							int clearingNumber = chit.getThisInt("clearing");
							clearingSummonTo = rcChit.getCurrentLocation().tile.getClearing(clearingNumber);
							break;
						}
					}
				}
				if (rcChit.isWarning()) {
					String warningsList = denizenHolder.getThisAttribute("summon");
					if (warningsList == null) continue;
					List warnings = Arrays.asList(warningsList.split("\\s*,\\s*"));
					String warning = chit.getName().toLowerCase();
					for (java.util.Iterator _j14it2436 = (warnings).iterator(); _j14it2436.hasNext(); ) {
					  String warningName = (String) _j14it2436.next();
						if (warningName.toLowerCase().trim().matches(warning)) {
							ArrayList clearings = rcChit.getCurrentLocation().tile.getClearings();
							int random = RandomNumber.getRandom(clearings.size());
							clearingSummonTo = (ClearingDetail) clearings.get(random);
						}
					}
				}

				if (clearingSummonTo != null) {
					if (summonLivingDenizens()) {
						SetupCardUtility.resetDenizen(denizen);
					}
					if (denizen.getHeldBy() == denizenHolder) {
						clearingSummonTo.add(denizen, null);
					}
					summonedDenizens = summonedDenizens + 1;
					if (summonedDenizens >= maxDenizens()) return;
					if (!summonedDenizenHolders.contains(denizen)) {
						summonedDenizenHolders.add(denizenHolder);
					}
					if (summonedDenizenHolders.size() >= maxDenizenHolders()) return;
				}
			}
		}
	}
	
	public String getDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("Summons ");
		if (getDenizenName().length() > 0) {
			sb.append(getDenizenName()+"(s) ");
		}
		if (getChit().length() > 0) {
			sb.append("to "+getChit());
		}
		if (summonTo() != SummonTo.Anywhere && getChit().length() > 0) {
			sb.append(" of ");
		}
		if (summonTo() != SummonTo.Anywhere && getChit().length() == 0) {
			sb.append(" to ");
		}
		if (summonTo() != SummonTo.Anywhere) {
			if (toLocation() && getQuestLocation() != null) {
				sb.append(getQuestLocation().getName());
			}
			else {
				sb.append(summonTo());
			}
		}
		sb.append(".");
		return sb.toString();
	}

	private String getChit() {
		return getString(CHIT) != null ?  getString(CHIT) : new String();
	}
	private String getDenizenName() {
		return getString(DENIZEN) != null ?  getString(DENIZEN) : new String();
	}
	private boolean summonLivingDenizens() {
		return getBoolean(SUMMON_LIVING_DENIZENS);
	}
	private int maxDenizens() {
		return getInt(MAX_DENIZENS);
	}
	private int maxDenizenHolders() {
		return getInt(MAX_DENIZEN_HOLDERS);
	}
	
	private SummonTo summonTo() {
		return SummonTo.valueOf(getString(SUMMON_TO));
	}
	private boolean toLocation() {
		return summonTo() == SummonTo.QuestLocationClearings || summonTo() == SummonTo.QuestLocationTiles;
	}
	
	public RewardType getRewardType() {
		return RewardType.SummonFromAppearanceToChit;
	}
	
	public boolean usesLocationTag(String tag) {
		QuestLocation loc = getQuestLocation();
		return loc!=null && tag.equals(loc.getName());
	}
	public QuestLocation getQuestLocation() {
		String id = getString(LOCATION);
		if (id!=null) {
			GameObject go = getGameData().getGameObject(Long.valueOf(id));
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
	
	private boolean markDenizens() {
		return getBoolean(MARK);
	}
}