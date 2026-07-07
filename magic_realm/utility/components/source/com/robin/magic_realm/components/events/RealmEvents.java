package com.robin.magic_realm.components.events;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.robin.game.objects.GameData;
import com.robin.game.objects.GameObject;
import com.robin.game.objects.GamePool;
import com.robin.game.server.GameHost;
import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.ClearingDetail;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.TileComponent;
import com.robin.magic_realm.components.attribute.ColorMagic;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.utility.ClearingUtility;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.RealmObjectMaster;
import com.robin.magic_realm.components.utility.RealmUtility;
import com.robin.magic_realm.components.utility.SetupCardUtility;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class RealmEvents {
	
	private static final String eventsConfiguration = "RealmEvents";
	private static final String eventsForTheWeek = "events";
	private static final String activeEvents = "events_active";
	private static final String infiniteColorMagicSource = "infinite_colorMagic_Source";
	public static final int firstEventDay = 8;
	public static final int firstEventMonth = 2;
	public static final int blankEventsPerWeek = 7;
	public static final int normalEventsPerWeek = 3;
	
	private static final class Events {
		private final String _name;
		private final int _ordinal;
		private Events(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final Events Blank = new Events("Blank", 0);
		public static final Events ViolentStorm = new Events("ViolentStorm", 1);
		public static final Events Fog = new Events("Fog", 2);
		public static final Events Illusion = new Events("Illusion", 3);
		public static final Events Lost = new Events("Lost", 4);
		public static final Events NightOfTheDemon = new Events("NightOfTheDemon", 5);
		public static final Events Migrate = new Events("Migrate", 6);
		public static final Events HorseWhisper = new Events("HorseWhisper", 7);
		public static final Events FrozenRiver = new Events("FrozenRiver", 8);
		public static final Events CaveIn = new Events("CaveIn", 9);
		public static final Events HurricaneWinds = new Events("HurricaneWinds", 10);
		public static final Events ProwlI = new Events("ProwlI", 11);
		public static final Events ProwlII = new Events("ProwlII", 12);
		public static final Events ProwlIII = new Events("ProwlIII", 13);
		public static final Events Regenerate = new Events("Regenerate", 14);
		public static final Events Enchant = new Events("Enchant", 15);
		public static final Events Break = new Events("Break", 16);
		public static final Events White = new Events("White", 17);
		public static final Events Grey = new Events("Grey", 18);
		public static final Events Gold = new Events("Gold", 19);
		public static final Events Purple = new Events("Purple", 20);
		public static final Events Black = new Events("Black", 21);
		public static final Events ViolentWinds = new Events("ViolentWinds", 22);
		public static final Events Thorns = new Events("Thorns", 23);
		public static final Events Flood = new Events("Flood", 24);
		public static final Events NegativeAura = new Events("NegativeAura", 25);
		public static final Events FlutterMigrate = new Events("FlutterMigrate", 26);
		public static final Events PatterMigrate = new Events("PatterMigrate", 27);
		public static final Events SlitherMigrate = new Events("SlitherMigrate", 28);
		public static final Events HowlMigrate = new Events("HowlMigrate", 29);
		public static final Events RoarMigrate = new Events("RoarMigrate", 30);
		public static final Events MountainSurge = new Events("MountainSurge", 31);
		public static final Events PeacefulDay = new Events("PeacefulDay", 32);
		public static final Events Prism = new Events("Prism", 33);

		private static final Events[] _VALUES = { Blank, ViolentStorm, Fog, Illusion, Lost, NightOfTheDemon, Migrate, HorseWhisper, FrozenRiver, CaveIn, HurricaneWinds, ProwlI, ProwlII, ProwlIII, Regenerate, Enchant, Break, White, Grey, Gold, Purple, Black, ViolentWinds, Thorns, Flood, NegativeAura, FlutterMigrate, PatterMigrate, SlitherMigrate, HowlMigrate, RoarMigrate, MountainSurge, PeacefulDay, Prism };
		public static Events[] values() { Events[] r = new Events[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static Events valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}

	public RealmEvents() {
	}
	
	public static void drawEvent(GameHost host) {
		GameObject config = findEventsConfig(host.getGameData());
		ArrayList events = config.getThisAttributeList(eventsForTheWeek);
		if (events == null || events.isEmpty()) return;
		int i = RandomNumber.getRandom(events.size());
		String eventString = (String) events.remove(i);
		config.addThisAttributeListItem(activeEvents,eventString);
		IEvent event = createEvent(Events.valueOf(eventString));
		host.broadcast("host","Event: "+event.getTitle());
	}
	
	public static void applyBirdsong(GameHost host) {
		GameObject config = findEventsConfig(host.getGameData());
		ArrayList events = config.getThisAttributeList(activeEvents);
		if (events == null || events.isEmpty()) return;
		
		for (java.util.Iterator _j14it2491 = (events).iterator(); _j14it2491.hasNext(); ) {
		  String eventString = (String) _j14it2491.next();
			IEvent event = createEvent(Events.valueOf(eventString));
			event.applyBirdsong(host.getGameData());
		}
	}
	
	public static void applySunset(GameHost host) {
		GameObject config = findEventsConfig(host.getGameData());
		ArrayList events = config.getThisAttributeList(activeEvents);
		if (events == null || events.isEmpty()) return;
		
		for (java.util.Iterator _j14it2492 = (events).iterator(); _j14it2492.hasNext(); ) {
		  String eventString = (String) _j14it2492.next();
			IEvent event = createEvent(Events.valueOf(eventString));
			event.applySunset(host.getGameData());
		}
	}
	
	public static void expireEvents(GameHost host) {
		GameObject config = findEventsConfig(host.getGameData());
		ArrayList events = config.getThisAttributeList(activeEvents);
		if (events==null) return;
		
		for (java.util.Iterator _j14it2493 = (events).iterator(); _j14it2493.hasNext(); ) {
		  String eventString = (String) _j14it2493.next();
			IEvent event = createEvent(Events.valueOf(eventString));
			event.expire(host.getGameData());
		}
		config.removeThisAttribute(activeEvents);
	}
	
	public static ArrayList getCurrentEvents(GameData gameData) {
		GameObject config = findEventsConfig(gameData);
		ArrayList events = config.getThisAttributeList(activeEvents);
		if (events==null) return null;
		
		ArrayList list = new ArrayList();
		for (java.util.Iterator _j14it2494 = (events).iterator(); _j14it2494.hasNext(); ) {
		  String eventString = (String) _j14it2494.next();
			IEvent event = createEvent(Events.valueOf(eventString));
			list.add(event);
		}
		
		return list;
	}
	
	public static void shuffleEvents(GameHost host) {
		GameObject config = findEventsConfig(host.getGameData());
		ArrayList list = new ArrayList();
		for (int i=0;i<blankEventsPerWeek;i++) {
			list.add(Events.Blank.toString());
		}
		ArrayList possibleEvents = new ArrayList();
		Events[] _vals2495 = Events.values();
		for (int _i2495 = 0; _i2495 < _vals2495.length; _i2495++) {
			Events event = _vals2495[_i2495];
			possibleEvents.add(event.toString());
		}
		possibleEvents.remove(Events.Blank.toString());
		for (int i=0;i<normalEventsPerWeek;i++) {
			int j = RandomNumber.getRandom(possibleEvents.size());
			list.add(possibleEvents.remove(j).toString());
		}
		config.setThisAttributeList(eventsForTheWeek,list);
		host.broadcast("host","Events shuffled");
	}
	
	public static GameObject findEventsConfig(GameData data) {
		GamePool pool = new GamePool(data.getGameObjects());
		ArrayList config = pool.find("name="+eventsConfiguration);
		if (config==null || config.isEmpty()) {
			return createNewConfigGameObject(data);
		}
		return (GameObject) config.get(0);
	}
	
	private static GameObject createNewConfigGameObject(GameData data) {
		GameObject config = data.createNewObject();
		config.setName(eventsConfiguration);
		return config;
	}
	
	public static IEvent createEvent(Events eventName){
		if (eventName == Events.ViolentStorm) return new ViolentStormEvent();
		else if (eventName == Events.Fog) return new FogEvent();
		else if (eventName == Events.Illusion) return new IllusionEvent();
		else if (eventName == Events.Lost) return new LostEvent();
		else if (eventName == Events.NightOfTheDemon) return new NightOfTheDemonEvent();
		else if (eventName == Events.Migrate) return new MigrateEvent();
		else if (eventName == Events.HorseWhisper) return new HorseWhisperEvent();
		else if (eventName == Events.FrozenRiver) return new FrozenRiverEvent();
		else if (eventName == Events.CaveIn) return new CaveInEvent();
		else if (eventName == Events.HurricaneWinds) return new HurricaneWindsEvent();
		else if (eventName == Events.ProwlI) return new Prowl1Event();
		else if (eventName == Events.ProwlII) return new Prowl2Event();
		else if (eventName == Events.ProwlIII) return new Prowl3Event();
		else if (eventName == Events.Regenerate) return new RegenerateEvent();
		else if (eventName == Events.Enchant) return new EnchantEvent();
		else if (eventName == Events.Break) return new BreakEvent();
		else if (eventName == Events.White) return new WhiteEvent();
		else if (eventName == Events.Grey) return new GreyEvent();
		else if (eventName == Events.Gold) return new GoldEvent();
		else if (eventName == Events.Purple) return new PurpleEvent();
		else if (eventName == Events.Black) return new BlackEvent();
		else if (eventName == Events.ViolentWinds) return new ViolentWindsEvent();
		else if (eventName == Events.Thorns) return new ThornsEvent();
		else if (eventName == Events.Flood) return new FloodEvent();
		else if (eventName == Events.NegativeAura) return new NegativeAuraEvent();
		else if (eventName == Events.FlutterMigrate) return new FlutterMigrateEvent();
		else if (eventName == Events.PatterMigrate) return new PatterMigrateEvent();
		else if (eventName == Events.SlitherMigrate) return new SlitherMigrateEvent();
		else if (eventName == Events.HowlMigrate) return new HowlMigrateEvent();
		else if (eventName == Events.RoarMigrate) return new RoarMigrateEvent();
		else if (eventName == Events.MountainSurge) return new MountainSurgeEvent();
		else if (eventName == Events.PeacefulDay) return new PeacefulDayEvent();
		else if (eventName == Events.Prism) return new PrismEvent();
		else return new BlankEvent();
	}
	
	public static void addInfiniteColorMagicSource(GameData data,String color) {
		GameObject config = findEventsConfig(data);
		config.addThisAttributeListItem(infiniteColorMagicSource, color);
	}
	
	public static void removeInfiniteColorMagicSource(GameData data,String color) {
		GameObject config = findEventsConfig(data);
		config.removeThisAttributeListItem(infiniteColorMagicSource, color);
	}
	
	public static ArrayList getInfiniteColorMagicSources(GameData data) {
		GameObject config = findEventsConfig(data);
		ArrayList list = new ArrayList();
		if (config.hasThisAttribute(infiniteColorMagicSource)) {
			for (java.util.Iterator _j14it2496 = (config.getThisAttributeList(infiniteColorMagicSource)).iterator(); _j14it2496.hasNext(); ) {
			  String color = (String) _j14it2496.next();
				list.add(ColorMagic.makeColorMagic(color,true));
			}
		}
		return list;
	}
	
	public static void addEffectForTile(GameObject config,String effect,String id) {
		config.addThisAttributeListItem(effect, id);
	}
	
	public static void removeEffectForTile(GameObject config,String effect,String id) {
		config.removeThisAttributeListItem(effect, id);
	}
	
	public static ArrayList getTileIdsForEffect(GameObject config,String effect) {
		return config.getThisAttributeList(effect);
	}
	
	public static ArrayList getLivingCharacters(GameData gameData) {
		GamePool pool = new GamePool(RealmObjectMaster.getRealmObjectMaster(gameData).getPlayerCharacterObjects());
		ArrayList list = pool.find(CharacterWrapper.NAME_KEY);
		ArrayList active = new ArrayList();
		for (java.util.Iterator _j14it2497 = (list).iterator(); _j14it2497.hasNext(); ) {
		  GameObject characterGo = (GameObject) _j14it2497.next();
			CharacterWrapper character = new CharacterWrapper(characterGo);
			if (!character.isDead()) {
				active.add(character);
			}
		}
		return active;
	}
	
	public static TileComponent chooseRandomTile(GameData data) {
		return chooseRandomTile(data,null,false);
	}
	public static TileComponent chooseRandomTileWithSummonChit(GameData data) {
		return chooseRandomTile(data,null,true);
	}
	public static TileComponent chooseRandomTile(GameData data,ArrayList tileTypes) {
		return chooseRandomTile(data,tileTypes,false);
	}
	public static TileComponent chooseRandomTile(GameData data,ArrayList tileTypes, boolean onlyWithSummonChit) {
		ArrayList list = new ArrayList();
		for (java.util.Iterator _j14it2498 = (getLivingCharacters(data)).iterator(); _j14it2498.hasNext(); ) {
		  CharacterWrapper character = (CharacterWrapper) _j14it2498.next();
			TileLocation loc = character.getCurrentLocation();
			if (loc!=null && loc.tile!=null && !list.contains(loc.tile)) {
				if (onlyWithSummonChit) {
					ArrayList hold = loc.tile.getGameObject().getHold();
					boolean foundChit = false;
					for (java.util.Iterator _j14it2499 = (hold).iterator(); _j14it2499.hasNext(); ) {
					  GameObject go = (GameObject) _j14it2499.next();
						RealmComponent rc = RealmComponent.getRealmComponent(go);
						if (rc.isWarning() || rc.isSound()) {
							foundChit = true;
							break;
						}
					}
					if (!foundChit) {
						continue;
					}
				}
				if (tileTypes!=null) {
					for (java.util.Iterator _j14it2500 = (tileTypes).iterator(); _j14it2500.hasNext(); ) {
					  String type = (String) _j14it2500.next();
						if (loc.tile.getTileType().matches(type)) {
							list.add(loc.tile);
							break;
						}
					}
				}
				else {
					list.add(loc.tile);
				}
			}
			for (java.util.Iterator _j14it2501 = (character.getAllHirelings()).iterator(); _j14it2501.hasNext(); ) {
			  RealmComponent hireling = (RealmComponent) _j14it2501.next();
				TileLocation locHireling = hireling.getCurrentLocation();
				if (locHireling!=null && locHireling.tile!=null && !list.contains(locHireling.tile)) {
					if (onlyWithSummonChit) {
						ArrayList hold = loc.tile.getGameObject().getHold();
						boolean foundChit = false;
						for (java.util.Iterator _j14it2502 = (hold).iterator(); _j14it2502.hasNext(); ) {
						  GameObject go = (GameObject) _j14it2502.next();
							RealmComponent rc = RealmComponent.getRealmComponent(go);
							if (rc.isWarning() || rc.isSound()) {
								foundChit = true;
								break;
							}
						}
						if (!foundChit) {
							continue;
						}
					}
					if (tileTypes!=null) {
						for (java.util.Iterator _j14it2503 = (tileTypes).iterator(); _j14it2503.hasNext(); ) {
						  String type = (String) _j14it2503.next();
							if (locHireling.tile.getTileType().matches(type)) {
								list.add(locHireling.tile);
								break;
							}
						}
					}
					else {
						list.add(locHireling.tile);
					}
				}
			}
		}
		if (list.isEmpty()) return null;
		return (TileComponent) list.get(RandomNumber.getRandom(list.size()));
	}
	public static GameObject chooseRandomTileWithUnhiredNatives(GameData data) {
		ArrayList list = new ArrayList();
		GamePool pool = new GamePool(data.getGameObjects());
		ArrayList denizens = pool.find(RealmComponent.NATIVE+",!treasure");
		for (java.util.Iterator _j14it2504 = (denizens).iterator(); _j14it2504.hasNext(); ) {
		  GameObject denizen = (GameObject) _j14it2504.next();
			RealmComponent rc = RealmComponent.getRealmComponent(denizen);
			if (!rc.isHiredOrControlled()) {
				GameObject heldBy = denizen.getHeldBy();
				if (heldBy!=null && heldBy.hasThisAttribute(RealmComponent.TILE)) {
					list.add(heldBy);
				}
			}
		}
		if (list.isEmpty()) return null;
		return (GameObject) list.get(RandomNumber.getRandom(list.size()));
	}
	public static ArrayList chooseRandomAndAdjacentTiles(GameData data) {
		ClearingUtility.initAdjacentTiles(data);
		
		ArrayList allTiles = new ArrayList();
		TileComponent tile = RealmEvents.chooseRandomTile(data);
		allTiles.add(tile);
		allTiles.addAll(tile.getAllAdjacentTiles());
		return allTiles;
	}
	public static ClearingDetail chooseRandomClearing(GameData data,ArrayList clearingTypes) {
		ArrayList list = new ArrayList();
		for (java.util.Iterator _j14it2505 = (getLivingCharacters(data)).iterator(); _j14it2505.hasNext(); ) {
		  CharacterWrapper character = (CharacterWrapper) _j14it2505.next();
			TileLocation loc = character.getCurrentLocation();
			if (loc!=null && loc.clearing!=null && !list.contains(loc.clearing)) {
				if (clearingTypes!=null) {
					for (java.util.Iterator _j14it2506 = (clearingTypes).iterator(); _j14it2506.hasNext(); ) {
					  String type = (String) _j14it2506.next();
						if (loc.clearing.getType().matches(type)) {
							list.add(loc.clearing);
							break;
						}
					}
				}
				else {
					list.add(loc.clearing);
				}
			}
			for (java.util.Iterator _j14it2507 = (character.getAllHirelings()).iterator(); _j14it2507.hasNext(); ) {
			  RealmComponent hireling = (RealmComponent) _j14it2507.next();
				TileLocation locHireling = hireling.getCurrentLocation();
				if (locHireling!=null && locHireling.clearing!=null && !list.contains(locHireling.clearing)) {
					if (clearingTypes!=null) {
						for (java.util.Iterator _j14it2508 = (clearingTypes).iterator(); _j14it2508.hasNext(); ) {
						  String type = (String) _j14it2508.next();
							if (locHireling.clearing.getType().matches(type)) {
								list.add(locHireling.clearing);
								break;
							}
						}
					}
					else {
						list.add(locHireling.clearing);
					}
				}
			}
		}
		if (list.isEmpty()) return null;
		return (ClearingDetail) list.get(RandomNumber.getRandom(list.size()));
	}
	public static ArrayList chooseAllTiles(GameData data) {
		GamePool pool = new GamePool(data.getGameObjects());
		return pool.find("tile");
	}
	public static ArrayList chooseRandomWaterAndAdjacentTiles(GameData data) {
		ArrayList allTiles = RealmEvents.chooseAllTiles(data);
		ArrayList waterTiles = new ArrayList();
		ArrayList chosenTiles = new ArrayList();
		for (java.util.Iterator _j14it2509 = (allTiles).iterator(); _j14it2509.hasNext(); ) {
		  GameObject tile = (GameObject) _j14it2509.next();
			TileComponent tileComponent = new TileComponent(tile);
			for (java.util.Iterator _j14it2510 = (tileComponent.getClearings()).iterator(); _j14it2510.hasNext(); ) {
			  ClearingDetail cl = (ClearingDetail) _j14it2510.next();
				if (cl.isWater() || cl.isFrozenWater()) {
					waterTiles.add(tileComponent);
					break;
				}
			}
		}
		
		if (!waterTiles.isEmpty()) {
			TileComponent chosenTile = (TileComponent) waterTiles.remove(RandomNumber.getRandom(waterTiles.size()));
			chosenTiles.add(chosenTile);
			Point basePosition = RealmUtility.getTilePositionFromGameObject(chosenTile.getGameObject());
			for (java.util.Iterator _j14it2511 = (waterTiles).iterator(); _j14it2511.hasNext(); ) {
			  TileComponent tile = (TileComponent) _j14it2511.next();
				Point position = RealmUtility.getTilePositionFromGameObject(tile.getGameObject());
				if ((position.x==basePosition.x || position.x==basePosition.x-1 || position.x==basePosition.x+1)
						&& (position.y==basePosition.y || position.y==basePosition.y-1 || position.y==basePosition.y+1)) {
					chosenTiles.add(tile);
				}
			}
		}
		return chosenTiles;
	}
	public static ArrayList getAllAdjacentTiles(GameObject chosenTile,GameData data) {
		ArrayList allTiles = chooseAllTiles(data);
		ArrayList adjacentTiles = new ArrayList();
		Point basePosition = RealmUtility.getTilePositionFromGameObject(chosenTile);
		allTiles.remove(chosenTile);
		for (java.util.Iterator _j14it2512 = (allTiles).iterator(); _j14it2512.hasNext(); ) {
		  GameObject tile = (GameObject) _j14it2512.next();
			Point position = RealmUtility.getTilePositionFromGameObject(tile);
			if ((position.x==basePosition.x || position.x==basePosition.x-1 || position.x==basePosition.x+1)
					&& (position.y==basePosition.y || position.y==basePosition.y-1 || position.y==basePosition.y+1)) {
				adjacentTiles.add(tile);
			}
		}
		return adjacentTiles;
	}
	public static void summonMonstersForSoundChit(GameObject sound, GameObject tile, GameData data) {
		GamePool pool = new GamePool(data.getGameObjects());
		ArrayList summons = pool.find("summon");
		String boardNum = sound.getThisAttribute(Constants.BOARD_NUMBER);
		ArrayList otherLocations = new ArrayList();
		for (java.util.Iterator _j14it2513 = (summons).iterator(); _j14it2513.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2513.next();
			if(!SetupCardUtility.GameObjectMatchesBoardNumber(go,boardNum)) continue;
			if (go.hasKey("gold_special_target")) {
				continue;
			}
			otherLocations.add(go);
		}
		Collections.sort(otherLocations,new Comparator() {
			public int compare(Object o1,Object o2) {
				GameObject go1 = (GameObject) o1;
				GameObject go2 = (GameObject) o2;
				int n1 = go1.getInt("this","box_num");
				int n2 = go2.getInt("this","box_num");
				return n1-n2;
			}
		});
		ArrayList redChitNames = new ArrayList();
		for (java.util.Iterator _j14it2514 = (tile.getHold()).iterator(); _j14it2514.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2514.next();
			if (!go.hasKey("red_special")) continue;
			redChitNames.add(go.getName());
		}
		String soundName = sound.getThisAttribute("sound");
		int soundClearing = sound.getThisInt("clearing");

		GameObject loc = SetupCardUtility.getFirstLocationWithSummonName(otherLocations,soundName,null,boardNum,redChitNames);
		if (loc!=null) {
			ClearingUtility.dumpHoldToTile(tile,loc,soundClearing);
		}
	}
}