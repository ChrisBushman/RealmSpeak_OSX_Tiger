package com.robin.magic_realm.components.quest.reward;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.ClearingDetail;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.quest.QuestLocation;
import com.robin.magic_realm.components.quest.QuestStep;
import com.robin.magic_realm.components.table.RaiseDead;
import com.robin.magic_realm.components.table.SummonAnimal;
import com.robin.magic_realm.components.table.SummonElemental;
import com.robin.magic_realm.components.utility.MonsterCreator;
import com.robin.magic_realm.components.utility.SetupCardUtility;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestRewardSummonGeneratedMonster extends QuestReward {
	private static Logger logger = Logger.getLogger(QuestStep.class.getName());
	public static final String MONSTER_TYPE = "_monster";
	public static final String AMOUNT = "_amount";
	public static final String RANDOM_CLEARING = "_rc";
	public static final String SUMMON_TO_LOCATION = "_summon_loc";
	public static final String RANDOM_LOCATION = "_rnd_loc";
	public static final String LOCATION = "_loc";
	public static final String CUSTOM_STATS = "__custom_stats";
	public static final String FAME = "_fame";
	public static final String NOTORIETY = "_not";
		
	public static final class MonsterType {
		private final String _name;
		private final int _ordinal;
		private MonsterType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final MonsterType Basilisk = new MonsterType("Basilisk", 0);
		public static final MonsterType Eagle = new MonsterType("Eagle", 1);
		public static final MonsterType Bear = new MonsterType("Bear", 2);
		public static final MonsterType Wolf = new MonsterType("Wolf", 3);
		public static final MonsterType Hawk = new MonsterType("Hawk", 4);
		public static final MonsterType Squirrel = new MonsterType("Squirrel", 5);
		public static final MonsterType Skeleton = new MonsterType("Skeleton", 6);
		public static final MonsterType SkeletonArcher = new MonsterType("SkeletonArcher", 7);
		public static final MonsterType SkeletonSwordsman = new MonsterType("SkeletonSwordsman", 8);
		public static final MonsterType ZombieM5H6 = new MonsterType("ZombieM5H6", 9);
		public static final MonsterType ZombieM5T5 = new MonsterType("ZombieM5T5", 10);
		public static final MonsterType ZombieM4M6 = new MonsterType("ZombieM4M6", 11);
		public static final MonsterType Blob = new MonsterType("Blob", 12);
		public static final MonsterType Wasp = new MonsterType("Wasp", 13);
		public static final MonsterType AirElemental = new MonsterType("AirElemental", 14);
		public static final MonsterType EarthElemental = new MonsterType("EarthElemental", 15);
		public static final MonsterType FireElemental = new MonsterType("FireElemental", 16);
		public static final MonsterType WaterElemental = new MonsterType("WaterElemental", 17);

		private static final MonsterType[] _VALUES = { Basilisk, Eagle, Bear, Wolf, Hawk, Squirrel, Skeleton, SkeletonArcher, SkeletonSwordsman, ZombieM5H6, ZombieM5T5, ZombieM4M6, Blob, Wasp, AirElemental, EarthElemental, FireElemental, WaterElemental };
		public static MonsterType[] values() { MonsterType[] r = new MonsterType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static MonsterType valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	
	public QuestRewardSummonGeneratedMonster(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame,CharacterWrapper character) {
		ArrayList monsters = new ArrayList();
		
		MonsterCreator mc = null;
		MonsterType _mt = getMonsterType();
		if (_mt == MonsterType.Skeleton || _mt == MonsterType.SkeletonArcher || _mt == MonsterType.SkeletonSwordsman
				|| _mt == MonsterType.ZombieM5H6 || _mt == MonsterType.ZombieM5T5 || _mt == MonsterType.ZombieM4M6
				|| _mt == MonsterType.Blob || _mt == MonsterType.Wasp) {
			mc = new MonsterCreator("QuestRewardSummonGeneratedMonster");
		}

		for (int i = 0; i < getAmount(); i++) {
			if (_mt == MonsterType.Basilisk) {
				monsters.add((new SummonAnimal(frame)).createAnimal(getGameData(), SummonAnimal.AnimalType.Basilisk));
			} else if (_mt == MonsterType.Eagle) {
				monsters.add((new SummonAnimal(frame)).createAnimal(getGameData(), SummonAnimal.AnimalType.Basilisk));
			} else if (_mt == MonsterType.Bear) {
				monsters.add((new SummonAnimal(frame)).createAnimal(getGameData(), SummonAnimal.AnimalType.Basilisk));
			} else if (_mt == MonsterType.Wolf) {
				monsters.add((new SummonAnimal(frame)).createAnimal(getGameData(), SummonAnimal.AnimalType.Basilisk));
			} else if (_mt == MonsterType.Hawk) {
				monsters.add((new SummonAnimal(frame)).createAnimal(getGameData(), SummonAnimal.AnimalType.Basilisk));
			} else if (_mt == MonsterType.Squirrel) {
				monsters.add((new SummonAnimal(frame)).createAnimal(getGameData(), SummonAnimal.AnimalType.Basilisk));
			} else if (_mt == MonsterType.Skeleton) {
				monsters.add(RaiseDead.createSkeleton(mc, getGameData()));
			} else if (_mt == MonsterType.SkeletonArcher) {
				monsters.add(RaiseDead.createSkeletonArcher(mc, getGameData()));
			} else if (_mt == MonsterType.SkeletonSwordsman) {
				monsters.add(RaiseDead.createSkeletonSwordsman(mc, getGameData()));
			} else if (_mt == MonsterType.ZombieM5H6) {
				monsters.add(RaiseDead.createZombie(mc, getGameData(),0));
			} else if (_mt == MonsterType.ZombieM5T5) {
				monsters.add(RaiseDead.createZombie(mc, getGameData(),1));
			} else if (_mt == MonsterType.ZombieM4M6) {
				monsters.add(RaiseDead.createZombie(mc, getGameData(),2));
			} else if (_mt == MonsterType.Blob) {
				monsters.add(SetupCardUtility.createBlob(mc, getGameData()));
			} else if (_mt == MonsterType.Wasp) {
				monsters.add(SetupCardUtility.createWasp(mc, getGameData()));
			} else if (_mt == MonsterType.AirElemental) {
				monsters.add((new SummonElemental(frame)).createElemental(getGameData(), SummonElemental.ElementalType.Water));
			} else if (_mt == MonsterType.EarthElemental) {
				monsters.add((new SummonElemental(frame)).createElemental(getGameData(), SummonElemental.ElementalType.Water));
			} else if (_mt == MonsterType.FireElemental) {
				monsters.add((new SummonElemental(frame)).createElemental(getGameData(), SummonElemental.ElementalType.Fire));
			} else if (_mt == MonsterType.WaterElemental) {
				monsters.add((new SummonElemental(frame)).createElemental(getGameData(), SummonElemental.ElementalType.Water));
			}
		}
		
		if (customStats()) {
			for (java.util.Iterator _j14it2358 = (monsters).iterator(); _j14it2358.hasNext(); ) {
			  GameObject monster = (GameObject) _j14it2358.next();
				monster.setThisAttribute("notoriety",getNotoriety());
				monster.setThisAttribute("fame",getFame());
			}
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
			if (randomLocation()) {
				int random = RandomNumber.getRandom(validLocations.size());
				TileLocation tileLocation = (TileLocation) validLocations.get(random);
				for (java.util.Iterator _j14it2359 = (monsters).iterator(); _j14it2359.hasNext(); ) {
				  GameObject monster = (GameObject) _j14it2359.next();
					tileLocation.clearing.add(monster,null);
				}
			}
			else {
				for (java.util.Iterator _j14it2360 = (validLocations).iterator(); _j14it2360.hasNext(); ) {
				  TileLocation location = (TileLocation) _j14it2360.next();
					for (java.util.Iterator _j14it2361 = (monsters).iterator(); _j14it2361.hasNext(); ) {
					  GameObject monster = (GameObject) _j14it2361.next();
						GameObject summonMonster = monster.copy();
						location.clearing.add(summonMonster, null);
					}
				}
			}
			return;
		}
		
		if (randomClearing()) {
			ArrayList clearings = character.getCurrentLocation().tile.getClearings();
			int random = RandomNumber.getRandom(clearings.size());
			for (java.util.Iterator _j14it2362 = (monsters).iterator(); _j14it2362.hasNext(); ) {
			  GameObject monster = (GameObject) _j14it2362.next();
				((ClearingDetail) clearings.get(random)).add(monster,null);
			}
			return;
		}
		
		for (java.util.Iterator _j14it2363 = (monsters).iterator(); _j14it2363.hasNext(); ) {
		  GameObject monster = (GameObject) _j14it2363.next();
			character.getCurrentLocation().clearing.add(monster,null);
		}
	}
		
	public String getDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append(getAmount()+" ");
		sb.append(getMonsterType());
		if (getAmount()>1) {
			sb.append("s are");
		}
		else {
			sb.append(" is");
		}
		if (locationOnly() && getQuestLocation() != null) {
			sb.append(" summoned in ");
			if (randomLocation()) {
				sb.append("a random clearing of ");
			}
			sb.append(getQuestLocation().getName());
			return sb.toString();
		}
		if (randomClearing()) {
			sb.append(" summoned in a random clearing of characters tile.");
			return sb.toString();
		}
		sb.append(" summoned in the characters clearing.");
		return sb.toString();
	}

	public RewardType getRewardType() {
		return RewardType.SummonGeneratedMonster;
	}
	
	private MonsterType getMonsterType() {
		return MonsterType.valueOf(getString(MONSTER_TYPE));
	}
	
	private int getAmount() {
		return getInt(AMOUNT);
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
	
	private boolean customStats() {
		return getBoolean(CUSTOM_STATS);
	}
	private int getFame() {
		return getInt(FAME);
	}
	private int getNotoriety() {
		return getInt(FAME);
	}
}