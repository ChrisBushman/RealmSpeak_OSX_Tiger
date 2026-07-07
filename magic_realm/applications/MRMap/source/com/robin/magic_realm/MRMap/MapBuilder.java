package com.robin.magic_realm.MRMap;

import java.awt.Point;
import java.util.*;

import com.robin.game.objects.*;
import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.RealmLoader;
import com.robin.magic_realm.components.utility.RealmObjectMaster;
import com.robin.magic_realm.components.wrapper.HostPrefWrapper;
import com.robin.magic_realm.map.Tile;

public class MapBuilder {
	
	public static ArrayList startTileList(GameData data,Collection keyVals) {
		ArrayList tiles = new ArrayList();
		Collection c = RealmObjectMaster.getRealmObjectMaster(data).getTileObjects();
		for (java.util.Iterator _j14it958 = (c).iterator(); _j14it958.hasNext(); ) {
		  GameObject obj = (GameObject) _j14it958.next();
			tiles.add(new Tile(obj));
		}
		return tiles;
	}
	public static Tile findAnchorTile(Collection tiles) {
		// Find the Borderland tile, and start it at position 0,0 with a random rotation
		for (java.util.Iterator _j14it959 = (tiles).iterator(); _j14it959.hasNext(); ) {
		  Tile tile = (Tile) _j14it959.next();
			if (tile.getGameObject().hasThisAttribute(Constants.ANCHOR_TILE)) {
				return tile;
			}
		}
		throw new IllegalStateException("Borderland or other staring tile is missing from tiles!!");
	}

	public static boolean autoBuildMap(GameData data,Collection keyVals) {
		return autoBuildMap(data,keyVals,null);
	}
	public static boolean autoBuildMap(GameData data,Collection keyVals,MapProgressReportable reporter) {
		boolean autoBuildRiver = true;
		ArrayList tiles = startTileList(data,keyVals);
		
		// Find the Borderland tile, and start it at position 0,0 with a random rotation
		Hashtable mapGrid = new Hashtable();
		Tile anchor = findAnchorTile(tiles);
		mapGrid.put(new Point(0,0),anchor);
		anchor.setMapPosition(new Point(0,0));
		anchor.setRotation(RandomNumber.getRandom(6));
		
		if (reporter!=null) {
			reporter.setProgress(1,tiles.size());
		}
		
		HostPrefWrapper hostPrefs = HostPrefWrapper.findHostPrefs(data);
		
		// Cycle until the mapGrid has all the tiles
		while(mapGrid.size()<tiles.size()) {
			if (reporter!=null) {
				reporter.setProgress(mapGrid.size(),tiles.size());
			}
			// First, identify all connectable map placement locations
			//		- Have paths leading to them
			//		- Adjacent to at least two tiles (unless only one tile on map)
			ArrayList availableMapPositions = Tile.findAvailableMapPositions(mapGrid,anchor.getGameObject().getName(),autoBuildRiver,hostPrefs.hasPref(Constants.MAP_BUILDING_HILL_TILES));
			
			// Cycle through every available (unplaced) tile
			ArrayList allTileResults = new ArrayList();
			ArrayList tileResultsPrio1 = new ArrayList();
			ArrayList tileResultsPrio2 = new ArrayList();
			for (java.util.Iterator _j14it960 = (tiles).iterator(); _j14it960.hasNext(); ) {
			  Tile tile = (Tile) _j14it960.next();
				addPossibleTilePlacements(mapGrid,tile,anchor,hostPrefs,availableMapPositions,allTileResults,tileResultsPrio1,tileResultsPrio2);
			}
			if ((hostPrefs.hasPref(Constants.MAP_BUILDING_RANGE_SETUP) || hostPrefs.hasPref(Constants.MAP_BUILDING_RANGE_SETUP_VARIANT)) && allTileResults.size()==0 && tileResultsPrio1.size()==0 && tileResultsPrio2.size()==0) {
				for (java.util.Iterator _j14it961 = (tiles).iterator(); _j14it961.hasNext(); ) {
				  Tile tile = (Tile) _j14it961.next();
					addPossibleTilePlacements(mapGrid,tile,anchor,hostPrefs,availableMapPositions,allTileResults,tileResultsPrio1,tileResultsPrio2,false,false);
				}
			}
			if (allTileResults.size()>0 || tileResultsPrio1.size()>0 || tileResultsPrio2.size()>0) {
				// First, pick a random tile result set
				ArrayList tileResults = null;
				if (tileResultsPrio1.size()>0) {
					tileResults = (ArrayList) tileResultsPrio1.get(RandomNumber.getRandom(tileResultsPrio1.size()));
				} else if (tileResultsPrio2.size()>0) {
					tileResults = (ArrayList) tileResultsPrio2.get(RandomNumber.getRandom(tileResultsPrio2.size()));
				} else {
					tileResults = (ArrayList) allTileResults.get(RandomNumber.getRandom(allTileResults.size()));
				}

				// Then pick a random MappingResult from the set for this tile
				TileMappingPossibility tmp = (TileMappingPossibility) tileResults.get(RandomNumber.getRandom(tileResults.size()));
				
				// Add it to the grid
				Tile tile = tmp.getTile();
				if (tile.getClearingCount()==6 && !tile.hasRiverPaths(0)) {
					/*
					 * This is a TOTAL hack, but should improve the speed of map building...
					 * 
					 * Basically by renaming all successfully placed Tile objects with 6 clearings as the anchor (Borderland),
					 * any logic that searches for connections to the anchor will stop when one of these tiles is located,
					 * shortening EVERY search.
					 * 
					 * Note that by renaming the Tile object, the GameObject is unaffected, so there is no harm.  Might be
					 * confusing if someone were to try to debug this code (How many friggen Borderlands are there!!) but
					 * I'm guessing that will never happen.  Famous last words....?
					 * 
					 * This hack didn't work for at least one Super Realm River tile
					 */
					tile.changeName(anchor.getGameObject().getName());
				}
				Point pos = tmp.getPosition();
				int rot = tmp.getRotation();
				
				tile.setRotation(rot);
				tile.setMapPosition(pos);
				
				mapGrid.put(pos,tile);
			}
			else {
				// It is bad if the allTileResults collection is empty at this point - means that none
				// of the remaining tiles can be placed at all!  We have a dead map, so return false.
				System.out.println(" - no more tile placement options!");
				return false;
			}
		}
		
		if (!validateAdjacentTiles(mapGrid)) return false;
		if (!validateLakeWoodsTile(hostPrefs, mapGrid, anchor)) return false;
		if (!validateRiver(hostPrefs, mapGrid)) return false;
		
		for (java.util.Iterator _j14it962 = (mapGrid.values()).iterator(); _j14it962.hasNext(); ) {
		  Tile tile = (Tile) _j14it962.next();
			tile.writeToGameObject();
		}
		System.out.println();
		return true;
	}
	private static void addPossibleTilePlacements(Hashtable mapGrid, Tile tile, Tile anchor, HostPrefWrapper hostPrefs, ArrayList availableMapPositions, ArrayList allTileResults, ArrayList tileResultsPrio1, ArrayList tileResultsPrio2) {
		addPossibleTilePlacements(mapGrid,tile,anchor,hostPrefs,availableMapPositions,allTileResults,tileResultsPrio1,tileResultsPrio2,hostPrefs.hasPref(Constants.MAP_BUILDING_RANGE_SETUP),hostPrefs.hasPref(Constants.MAP_BUILDING_RANGE_SETUP_VARIANT));
	}
	private static void addPossibleTilePlacements(Hashtable mapGrid, Tile tile, Tile anchor, HostPrefWrapper hostPrefs, ArrayList availableMapPositions, ArrayList allTileResults, ArrayList tileResultsPrio1, ArrayList tileResultsPrio2, boolean rangeSetup, boolean rangeSetupVariant) {
		// Only use unmapped tiles
		if (!mapGrid.contains(tile)) {
			ArrayList tileResults = new ArrayList();					
			// Try the tile in every available position
			for (java.util.Iterator _j14it963 = (availableMapPositions).iterator(); _j14it963.hasNext(); ) {
			  Point pos = (Point) _j14it963.next();						
				// Try every rotation
				for (int rot=0;rot<6;rot++) {
					// Test the tile at pos, with rotation rot
					if (Tile.isMappingPossibility(mapGrid,tile,pos,rot,anchor.getGameObject().getName(),hostPrefs.hasPref(Constants.MAP_BUILDING_HILL_TILES),rangeSetup,rangeSetupVariant)) {
						tileResults.add(new TileMappingPossibility(tile,pos,rot));
						if (hostPrefs.hasPref(Constants.MAP_BUILDING_INCREASED_PRIO_TILE_PLACEMENT) && Tile.isMappingNextToPrioritizedTile(mapGrid,tile,pos,rot)) {
							tileResults.add(new TileMappingPossibility(tile,pos,rot));
							tileResults.add(new TileMappingPossibility(tile,pos,rot));
						}
					}
				}
			}
			if (tileResults.size()>0) {
				// Adding the tile results in by tile prevents unfair weighting per tile
				if (tile.getGameObject().hasThisAttribute(Constants.MAP_BUILDING_PRIO)) {
					if (tile.getGameObject().getThisAttribute(Constants.MAP_BUILDING_PRIO).matches("1")) {
						tileResultsPrio1.add(tileResults);
					} else {
						tileResultsPrio2.add(tileResults);
					}
				} else {
					allTileResults.add(tileResults);
				}
			}
		}
	}
	public static boolean validateAdjacentTiles(Hashtable mapGrid) {
		int neededCount = 2;
		for (java.util.Iterator _j14it964 = (mapGrid.values()).iterator(); _j14it964.hasNext(); ) {
		  Tile tile = (Tile) _j14it964.next();
			Point pos = tile.getMapPosition();
			int adjCount = 0;
			for (int edge=0;edge<6;edge++) {
				Point adjPos = Tile.getAdjacentPosition(pos,edge);
				Tile adjTile = (Tile) mapGrid.get(adjPos);
				if (adjTile!=null) {
					adjCount++;
				}
				if (adjCount>=neededCount) continue;
				if (edge==5) return false;
			}
		}
		return true;
	}
	public static boolean validateLakeWoodsTile(HostPrefWrapper hostPrefs, Hashtable mapGrid, Tile anchor) {
		if (hostPrefs.hasPref(Constants.MAP_BUILDING_LAKE_WOODS_MUST_CONNECT)) {
			for (java.util.Iterator _j14it965 = (mapGrid.values()).iterator(); _j14it965.hasNext(); ) {
			  Tile tile = (Tile) _j14it965.next();
				if (tile.getGameObject().getName().matches("Lake Woods")) {
					for (java.util.Iterator _j14it966 = (tile.getClearings()).iterator(); _j14it966.hasNext(); ) {
					  String clearing = (String) _j14it966.next();
						if (!tile.connectsToTilename(mapGrid,clearing,anchor.getGameObject().getName())) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	public static boolean validateRiver(HostPrefWrapper hostPrefs, Hashtable mapGrid) {
		if (hostPrefs.hasPref(Constants.MAP_BUILDING_NON_RIVER_TILES_ADJACENT_TO_RIVER) || hostPrefs.hasPref(Constants.MAP_BUILDING_2_NON_RIVER_TILES_ADJACENT_TO_RIVER)) {
			int neededCount = 1;
			if (hostPrefs.hasPref(Constants.MAP_BUILDING_2_NON_RIVER_TILES_ADJACENT_TO_RIVER)) {
				neededCount = 2;
			}
			for (java.util.Iterator _j14it967 = (mapGrid.values()).iterator(); _j14it967.hasNext(); ) {
			  Tile tile = (Tile) _j14it967.next();
				if (tile.hasRiverPaths(0)) {
					Point pos = tile.getMapPosition();
					int adjCount = 0;
					for (int edge=0;edge<6;edge++) {
						Point adjPos = Tile.getAdjacentPosition(pos,edge);
						Tile adjTile = (Tile) mapGrid.get(adjPos);
						if (adjTile!=null && !adjTile.hasRiverPaths(0)) {
							adjCount++;
						}
						if (adjCount>=neededCount) continue;
						if (edge==5) return false;
					}
				}
			}
		}
		return true;
	}
	public static Hashtable getMapGrid(GameData data, HostPrefWrapper hostPrefs) {
		Hashtable mapGrid = new Hashtable();
		Collection keyVals = GamePool.makeKeyVals(hostPrefs.getGameKeyVals());
		ArrayList tiles = startTileList(data,keyVals);
		for (java.util.Iterator _j14it968 = (tiles).iterator(); _j14it968.hasNext(); ) {
		  Tile tile = (Tile) _j14it968.next();
			tile.readFromGameObject();
			mapGrid.put(Tile.getPositionFromGameObject(tile.getGameObject()),tile);
		}
		return mapGrid;
	}
	public static void main(String[]args) {
	    RealmLoader loader = new RealmLoader();
		GameData data = loader.getData();
		System.out.println("loaded "+data.getGameObjects().size());
		ArrayList keyVals = new ArrayList();
		keyVals.add("super_realm");
		while(!MapBuilder.autoBuildMap(data,keyVals))
		for (java.util.Iterator _j14it969 = (data.getGameObjects()).iterator(); _j14it969.hasNext(); ) {
		  GameObject obj = (GameObject) _j14it969.next();
			if (obj.hasKey("tile")) {
				System.out.println(obj+":   "+obj.getAttributeBlock("mapGrid"));
			}
		}
	}
}