package com.robin.magic_realm.components.quest.reward;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.ClearingDetail;
import com.robin.magic_realm.components.PathDetail;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.quest.QuestConstants;
import com.robin.magic_realm.components.quest.QuestLocation;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRewardMoveDenizen extends QuestReward {
	
	public static final class MoveOption {
		private final String _name;
		private final int _ordinal;
		private MoveOption(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final MoveOption CharactersTile = new MoveOption("CharactersTile", 0);
		public static final MoveOption CharactersClearing = new MoveOption("CharactersClearing", 1);
		public static final MoveOption Location = new MoveOption("Location", 2);

		private static final MoveOption[] _VALUES = { CharactersTile, CharactersClearing, Location };
		public static MoveOption[] values() { MoveOption[] r = new MoveOption[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static MoveOption valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	
	public static final class MoveFromOption {
		private final String _name;
		private final int _ordinal;
		private MoveFromOption(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final MoveFromOption Everywhere = new MoveFromOption("Everywhere", 0);
		public static final MoveFromOption CharactersTile = new MoveFromOption("CharactersTile", 1);
		public static final MoveFromOption CharactersClearing = new MoveFromOption("CharactersClearing", 2);

		private static final MoveFromOption[] _VALUES = { Everywhere, CharactersTile, CharactersClearing };
		public static MoveFromOption[] values() { MoveFromOption[] r = new MoveFromOption[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static MoveFromOption valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	
	public static final class ClearingSelection {
		private final String _name;
		private final int _ordinal;
		private ClearingSelection(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final ClearingSelection Random = new ClearingSelection("Random", 0);
		public static final ClearingSelection One = new ClearingSelection("One", 1);
		public static final ClearingSelection Two = new ClearingSelection("Two", 2);
		public static final ClearingSelection Three = new ClearingSelection("Three", 3);
		public static final ClearingSelection Four = new ClearingSelection("Four", 4);
		public static final ClearingSelection Five = new ClearingSelection("Five", 5);
		public static final ClearingSelection Six = new ClearingSelection("Six", 6);
		public static final ClearingSelection RandomForEachDenizen = new ClearingSelection("RandomForEachDenizen", 7);

		private static final ClearingSelection[] _VALUES = { Random, One, Two, Three, Four, Five, Six, RandomForEachDenizen };
		public static ClearingSelection[] values() { ClearingSelection[] r = new ClearingSelection[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static ClearingSelection valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	
	public static final String DENIZEN_REGEX = "_drx";
	public static final String MOVE_FROM_OPTION = "_mfo";
	public static final String MOVE_OPTION = "_mo";
	public static final String CLEARING = "_cl";
	public static final String LOCATION = "_loc";
	public static final String AMOUNT = "_amnt";
	public static final String MARK = "_mk";
	public static final String MOVE_HIRELINGS = "_mh";
	public static final String MOVE_COMPANIONS = "_mc";
	public static final String MOVE_SUMMONED = "_ms";
	public static final String MOVE_TRAVELERS = "_mt";
	public static final String MOVE_LIMITED = "_ml";
	
	public QuestRewardMoveDenizen(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame,CharacterWrapper character) {
		ArrayList denizens = character.getGameData().getGameObjectsByNameRegex(getDenizenNameRegex());
		QuestLocation loc = getQuestLocation();
		TileLocation charactersLoc= character.getCurrentLocation();
		ArrayList denizensToMove = new ArrayList();
		String questId = getParentQuest().getGameObject().getStringId();
		for (java.util.Iterator _j14it2394 = (denizens).iterator(); _j14it2394.hasNext(); ) {
		  GameObject denizen = (GameObject) _j14it2394.next();
			RealmComponent denizenRc = RealmComponent.getRealmComponent(denizen);
			if (denizenRc == null || (!denizenRc.isDenizen() && !denizenRc.isTraveler())) continue;
			TileLocation denizenLoc = denizenRc.getCurrentLocation();
			if(getMoveFromOption() == MoveFromOption.CharactersClearing
					&& (denizenLoc == null || charactersLoc == null || denizenLoc.tile != charactersLoc.tile || denizenLoc.clearing != charactersLoc.clearing)) {
				continue;
			}
			if(getMoveFromOption() == MoveFromOption.CharactersTile && (denizenLoc == null || charactersLoc == null || denizenLoc.tile != charactersLoc.tile)) {
				continue;
			}
			if (!moveHirelings() && denizen.hasThisAttribute(Constants.HIRELING)) {
				continue;
			}
			if (!moveCompanions() && denizen.hasThisAttribute(Constants.COMPANION)) {
				continue;
			}
			if (!moveSummoned() && denizen.hasThisAttribute(Constants.SUMMONED)) {
				continue;
			}
			if (!moveTravelers() && denizen.hasThisAttribute(Constants.TRAVELER)) {
				continue;
			}
			if (moveOnlySpecificOnes() && !denizen.hasThisAttribute(Constants.HIRELING) && !denizen.hasThisAttribute(Constants.COMPANION) && !denizen.hasThisAttribute(Constants.SUMMONED) && !denizen.hasThisAttribute(Constants.TRAVELER)) {
				continue;
			}
			if (needsMark()) {
				String mark = denizen.getThisAttribute(QuestConstants.QUEST_MARK);
				if (mark==null || !mark.equals(questId)) continue;
			}
			denizensToMove.add(denizen);
		}
		
		if (numberOfDenizens()!=0) {
			Collections.shuffle(denizensToMove);
		}
		
		MoveOption _mo = getMoveOption();
		if (_mo == MoveOption.CharactersClearing) {
			if(character.getCurrentLocation() == null || character.getCurrentLocation().clearing == null) return;
			TileLocation charactersClearing = character.getCurrentLocation();
			moveDenizens(charactersClearing, denizensToMove, numberOfDenizens());
			return;
		} else if (_mo == MoveOption.CharactersTile) {
			if(character.getCurrentLocation() == null) return;
			TileLocation charactersTile = character.getCurrentLocation();
			moveDenizensToTile(charactersTile, denizensToMove, numberOfDenizens());
			return;
		} else if (_mo == MoveOption.Location) {
			ArrayList locations = loc.fetchAllLocations(frame, character, character.getGameData());
			moveDenizensToLocation(locations, denizensToMove);
		}
	}
	
	private static void moveDenizens(TileLocation locationToMove, ArrayList denizensToMove, int numberOfDenizensToMove) {
		int movedDenizens = 0;
		for (java.util.Iterator _j14it2395 = (denizensToMove).iterator(); _j14it2395.hasNext(); ) {
		  GameObject denizen = (GameObject) _j14it2395.next();
			if(!locationToMove.clearing.isEdge()) {
				locationToMove.clearing.add(denizen,null);
			} else {
				ArrayList path = locationToMove.clearing.getConnectedPaths();
				ClearingDetail connectedClearing = ((PathDetail) path.get(0)).findConnection(locationToMove.clearing);
				connectedClearing.add(denizen,null);
			}
			movedDenizens++;
			if (numberOfDenizensToMove!=0 && movedDenizens>=numberOfDenizensToMove) {
				return;
			}
		}
	}
	private static void moveDenizensToClearing(TileLocation locationToMove, int clearingNumber, ArrayList denizensToMove, int numberOfDenizensToMove) {
		locationToMove.clearing = locationToMove.tile.getClearing(clearingNumber);
		if (locationToMove.clearing != null) {
			moveDenizens(locationToMove, denizensToMove, numberOfDenizensToMove);
		}
	}
	private void moveDenizensToTile(TileLocation tileToMove, ArrayList denizensToMove, int numberOfDenizensToMove) {
		ClearingSelection _cs = getClearing();
		if (_cs == ClearingSelection.Random) {
			int random = RandomNumber.getRandom(tileToMove.tile.getClearingCount());
			tileToMove.clearing = (ClearingDetail) tileToMove.tile.getClearings().get(random);
			moveDenizens(tileToMove, denizensToMove, numberOfDenizensToMove);
			return;
		} else if (_cs == ClearingSelection.One) {
			moveDenizensToClearing(tileToMove, 1, denizensToMove, numberOfDenizensToMove);
		} else if (_cs == ClearingSelection.Two) {
			moveDenizensToClearing(tileToMove, 2, denizensToMove, numberOfDenizensToMove);
		} else if (_cs == ClearingSelection.Three) {
			moveDenizensToClearing(tileToMove, 3, denizensToMove, numberOfDenizensToMove);
		} else if (_cs == ClearingSelection.Four) {
			moveDenizensToClearing(tileToMove, 4, denizensToMove, numberOfDenizensToMove);
		} else if (_cs == ClearingSelection.Five) {
			moveDenizensToClearing(tileToMove, 5, denizensToMove, numberOfDenizensToMove);
		} else if (_cs == ClearingSelection.Six) {
			moveDenizensToClearing(tileToMove, 6, denizensToMove, numberOfDenizensToMove);
		} else if (_cs == ClearingSelection.RandomForEachDenizen) {
			int movedDenizens = 0;
			for (java.util.Iterator _j14it2396 = (denizensToMove).iterator(); _j14it2396.hasNext(); ) {
			  GameObject denizen = (GameObject) _j14it2396.next();
				int randomForDenizen = RandomNumber.getRandom(tileToMove.tile.getClearingCount());
				tileToMove.clearing = (ClearingDetail) tileToMove.tile.getClearings().get(randomForDenizen);
				tileToMove.clearing.add(denizen, null);
				movedDenizens++;
				if (numberOfDenizens() != 0 && movedDenizens>=numberOfDenizens()) {
					break;
				}
			}
		}
	}
	private void moveDenizensToLocation(ArrayList locations, ArrayList denizensToMove) {
		int movedDenizens = 0;
		int numberOfDenizensToMove = numberOfDenizens();
		ClearingSelection _cs2 = getClearing();
		if (_cs2 == ClearingSelection.Random) {
			int random = RandomNumber.getRandom(locations.size());
			TileLocation locationToMove = (TileLocation) locations.get(random);
			for (java.util.Iterator _j14it2397 = (denizensToMove).iterator(); _j14it2397.hasNext(); ) {
			  GameObject denizen = (GameObject) _j14it2397.next();
				locationToMove.clearing.add(denizen, null);
				movedDenizens++;
				if (numberOfDenizensToMove != 0 && movedDenizens>=numberOfDenizensToMove) {
					break;
				}
			}
		} else if (_cs2 == ClearingSelection.One) {
			moveDenizensToLocationToClearing(locations, 1, denizensToMove, numberOfDenizensToMove);
		} else if (_cs2 == ClearingSelection.Two) {
			moveDenizensToLocationToClearing(locations, 2, denizensToMove, numberOfDenizensToMove);
		} else if (_cs2 == ClearingSelection.Three) {
			moveDenizensToLocationToClearing(locations, 3, denizensToMove, numberOfDenizensToMove);
		} else if (_cs2 == ClearingSelection.Four) {
			moveDenizensToLocationToClearing(locations, 4, denizensToMove, numberOfDenizensToMove);
		} else if (_cs2 == ClearingSelection.Five) {
			moveDenizensToLocationToClearing(locations, 5, denizensToMove, numberOfDenizensToMove);
		} else if (_cs2 == ClearingSelection.Six) {
			moveDenizensToLocationToClearing(locations, 6, denizensToMove, numberOfDenizensToMove);
		} else if (_cs2 == ClearingSelection.RandomForEachDenizen) {
			for (java.util.Iterator _j14it2398 = (denizensToMove).iterator(); _j14it2398.hasNext(); ) {
			  GameObject denizen = (GameObject) _j14it2398.next();
				int randomForDenizen = RandomNumber.getRandom(locations.size());
				TileLocation locationToMoveForDenizen = (TileLocation) locations.get(randomForDenizen);
				locationToMoveForDenizen.clearing.add(denizen, null);
				movedDenizens++;
				if (numberOfDenizensToMove != 0 && movedDenizens>=numberOfDenizensToMove) {
					break;
				}
			}
		}
	}
	private static void moveDenizensToLocationToClearing(ArrayList locations, int clearingNumber, ArrayList denizensToMove, int numberOfDenizensToMove) {
		ArrayList validLocations = new ArrayList();
		for (java.util.Iterator _j14it2399 = (locations).iterator(); _j14it2399.hasNext(); ) {
		  TileLocation loc = (TileLocation) _j14it2399.next();
			if (loc.clearing.getNum() == clearingNumber) {
				validLocations.add(loc);
			}
		}
		if (!validLocations.isEmpty()) {
			int random = RandomNumber.getRandom(validLocations.size());
			TileLocation selectedLocation = (TileLocation) validLocations.get(random);
			moveDenizens(selectedLocation, denizensToMove, numberOfDenizensToMove);
		}
	}
	
	public String getDescription() {
		StringBuffer sb = new StringBuffer();
		if (numberOfDenizens()!=0) {
			sb.append("Up to "+numberOfDenizens()+" ");
		} else {
			sb.append("Denizens");
		}
		sb.append(getDenizenNameRegex() +" are moved to ");
		if (getMoveOption() == MoveOption.Location && getQuestLocation() != null) {
			sb.append(getQuestLocation().getName());
		}
		else {
			sb.append(getMoveOption());
		}
		sb.append(".");
		return sb.toString();
	}
	private String getDenizenNameRegex() {
		return getString(DENIZEN_REGEX);
	}
	private MoveFromOption getMoveFromOption() {
		String moveFromOption = getString(MOVE_FROM_OPTION);
		if (moveFromOption == null) return MoveFromOption.Everywhere;
		return MoveFromOption.valueOf(moveFromOption);
	}
	private MoveOption getMoveOption() {
		return MoveOption.valueOf(getString(MOVE_OPTION));
	}
	private ClearingSelection getClearing() {
		return ClearingSelection.valueOf(getString(CLEARING));
	}
	private int numberOfDenizens() {
		return getInt(AMOUNT);
	}
	private boolean needsMark() {
		return getBoolean(MARK);
	}
	private boolean moveHirelings() {
		return getBoolean(MOVE_HIRELINGS);
	}
	private boolean moveCompanions() {
		return getBoolean(MOVE_COMPANIONS);
	}
	private boolean moveSummoned() {
		return getBoolean(MOVE_SUMMONED);
	}
	private boolean moveTravelers() {
		return getBoolean(MOVE_TRAVELERS);
	}
	private boolean moveOnlySpecificOnes() {
		return getBoolean(MOVE_LIMITED);
	}
	public RewardType getRewardType() {
		return RewardType.MoveDenizen;
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
	
}