package com.robin.magic_realm.components;

import java.awt.Color;
import java.awt.Point;
import java.util.*;

import com.robin.game.objects.GameObject;
import com.robin.game.objects.GamePool;
import com.robin.general.swing.DieRoller;
import com.robin.magic_realm.components.attribute.*;
import com.robin.magic_realm.components.utility.*;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.GameWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;
import com.robin.magic_realm.map.Tile;

public class ClearingDetail {
	
	private static final int EDGE_NUM = -1;

	public static final int MAGIC_WHITE		= 0;
	public static final int MAGIC_GRAY		= 1;
	public static final int MAGIC_GOLD		= 2;
	public static final int MAGIC_PURPLE	= 3;
	public static final int MAGIC_BLACK		= 4;
	public static final int MAGIC_VARIED	= 5;
	
	private static Color DEFAULT_MARK_COLOR = Color.green;
	
	public static final char[] MAGIC_CHAR = {'W','R','G','P','B','V'};

	protected TileComponent parent;
	protected int num;
	protected String type;
	protected Point position; // Position within the tile
	protected Point absolutePosition; // Position on the map
	protected boolean[] magic;
	protected int side;
	
	private boolean marked = false;
	protected Color markColor = DEFAULT_MARK_COLOR;
	
	private ArrayList extras;
	
	/**
	 * Use this constructor for edges
	 */
	public ClearingDetail(TileComponent parent,String edge,Point position,int side) {
		this(parent,EDGE_NUM,edge,position,side);
	}
	public ClearingDetail(TileComponent parent,int num,String type,Point position,int side) {
		this.parent = parent;
		this.num = num;
		this.type = type;
		this.position = position;
		this.magic = new boolean[6];
		this.side = side;
		Arrays.fill(magic,false);
		extras = new ArrayList();
	}
	public TileLocation getTileLocation() {
		return new TileLocation(this);
	}
	public void addExtra(String val) {
		extras.add(val.trim().toLowerCase());
	}
	public boolean hasExtra(String val) {
		return extras.contains(val.trim().toLowerCase());
	}
	public boolean equals(Object o1) {
		if (o1 instanceof ClearingDetail) {
			ClearingDetail other = (ClearingDetail)o1;
			// this doesn't discriminate clearings on opposite sides of the same tile, but that's not a really important check
			return (parent.getGameObject().equals(other.parent.getGameObject()) && num==other.num && type.equals(other.type));
		}
		return false;
	}
	/**
	 * Convenience method for adding something to this clearing
	 */
	public void add(GameObject thing,CharacterWrapper character) {
		parent.getGameObject().add(thing);
		thing.setThisAttribute("clearing",getNumString());
		if (character!=null) {
			thing.setThisAttribute(Constants.PLAIN_SIGHT);
			thing.setThisAttribute(Constants.DROPPED_BY,character.getGameObject().getStringId());
		}
	}
	public void remove(GameObject thing) {
		parent.getGameObject().remove(thing);
		thing.removeThisAttribute("clearing");
	}
	public boolean isEdge() {
		return num==EDGE_NUM;
	}
	public void setPosition(Point p) {
		position = p;
	}
	public String getName() {
		return "clearing_"+num;
	}
	public int getNum() {
		return num;
	}
	public String getNumString() {
		return isEdge()?("."+type):String.valueOf(num);
	}
	public Point getPosition() {
		return position;
	}
	public String getType() {
		return type;
	}
	public String getTypeCode() {
		return isEdge()?"E":type.toUpperCase().substring(0,1);
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setMagic(int colorId,boolean val) {
		magic[colorId]=val;
	}
	public boolean getMagic(int colorId) {
		return magic[colorId];
	}
	public Color getColor() {
		if (magic[MAGIC_WHITE]) {
			return Color.white;
		}
		else if (magic[MAGIC_GRAY]) {
			return Color.darkGray;
		}
		else if (magic[MAGIC_GOLD]) {
			return MagicRealmColor.GOLD;
		}
		else if (magic[MAGIC_PURPLE]) {
			return MagicRealmColor.PURPLE;
		}
		else if (magic[MAGIC_BLACK]) {
			return Color.black;
		}
		else if (magic[MAGIC_VARIED]) {
			return MagicRealmColor.LIGHTGREEN;
		}
		return null;
	}
	public boolean isNormal() {
		return type.equals("normal") && !hasSpellEffect(Constants.MOUNTAIN_SURGE) && !hasEventEffect(Constants.EVENT_MOUNTAIN_SURGE);
	}
	public boolean isCave() {
		return type.equals("caves") && !hasSpellEffect(Constants.MOUNTAIN_SURGE) && !hasEventEffect(Constants.EVENT_MOUNTAIN_SURGE);
	}
	public boolean isWater() {
		return type.equals("water") && !parent.getGameObject().hasThisAttribute(Constants.FROZEN_WATER) && !hasSpellEffect(Constants.MOUNTAIN_SURGE) && !hasEventEffect(Constants.EVENT_MOUNTAIN_SURGE);
	}
	public boolean isFrozenWater() {
		return (type.equals(Constants.FROZEN_WATER) || parent.getGameObject().hasThisAttribute(Constants.FROZEN_WATER) || parent.getGameObject().hasThisAttribute(Constants.EVENT_FROZEN_WATER)) && !hasSpellEffect(Constants.MOUNTAIN_SURGE) && !hasEventEffect(Constants.EVENT_MOUNTAIN_SURGE);
	}
	public boolean isLighted() {
		if (!parent.getGameObject().hasThisAttribute(Constants.LIGHTED)) return false;
		for (java.util.Iterator _j14it1429 = (parent.getGameObject().getThisAttributeList(Constants.LIGHTED)).iterator(); _j14it1429.hasNext(); ) {
		  String clearing = (String) _j14it1429.next();
			if (clearing.matches(String.valueOf(num))) return true;
		}
		return false;
	}
	public void setLighted(boolean light) {
		if (light && !isLighted()) {
			parent.getGameObject().addThisAttributeListItem(Constants.LIGHTED, String.valueOf(num));
		}
		if (!light && isLighted()) {
			parent.getGameObject().removeThisAttributeListItem(Constants.LIGHTED, String.valueOf(num));
		}
	}
	public boolean isMountain() {
		return type.equals("mountain") || hasSpellEffect(Constants.MOUNTAIN_SURGE) || hasEventEffect(Constants.EVENT_MOUNTAIN_SURGE);
	}
	public boolean isWoods() {
		return (type.equals("woods") || type.equals(Constants.FROZEN_WATER) || parent.getGameObject().hasThisAttribute(Constants.FROZEN_WATER) || parent.getGameObject().hasThisAttribute(Constants.EVENT_FROZEN_WATER)) && !hasSpellEffect(Constants.MOUNTAIN_SURGE) && !hasEventEffect(Constants.EVENT_MOUNTAIN_SURGE); //treat frozen water clearings as woods clearings
	}
	public int moveCost(CharacterWrapper character,TileLocation currentLocation) {
		int val = 1;
		if (isMountain()) {
			val = character.getMountainMoveCost();
		}
		if (isWater() && !character.affectedByKey(Constants.SEAFARING) && !character.canWaterRun(currentLocation.clearing,this)) {
			if (!character.isTransmorphed()) val++;
			if (character.affectedByKey(Constants.WATER_MOVE_ADJ)) val--;
			if (!character.isTransmorphed() && currentLocation.clearing!=null && currentLocation.clearing.isWater()) {
				GamePool pool = new GamePool(this.parent.getGameObject().getGameData().getGameObjects());
				ArrayList waterSources = pool.find("tile,water_source_clearing");
				if (!waterSources.isEmpty()) {
					if (this.distanceToWaterSource(waterSources)>=currentLocation.clearing.distanceToWaterSource(waterSources)) {
						if (currentLocation.isBetweenClearings()) {
							if (this.distanceToWaterSource(waterSources)>=currentLocation.getOther().clearing.distanceToWaterSource(waterSources)) {
								val--;
							}
						}
						else {
							val--;
						}
					}
				}
			}
		}
		if (!isCave() && character.addsOneToMoveExceptCaves()) {
			val++;
		}
		return val;
	}
	public String toString() {
		if (num==-1) {
			return type;
		}
		return "clearing_"+num;
	}
	public String parentToString() { // allows me to see the pointer info
		return super.toString();
	}
	public String shortString() {
		StringBuffer sb = new StringBuffer();
		sb.append(parent.getTileName());
		sb.append(" ");
		sb.append(num);
		return sb.toString();
	}
	public String fullString() {
		StringBuffer sb = new StringBuffer();
		sb.append(parent.getTileName());
		sb.append(" ");
		sb.append(num);
		if (!type.equals("normal")) {
			sb.append(" (");
			sb.append(type);
			sb.append(")");
		}
		Collection c = getClearingColorMagic();
		if (c.size()>0) {
			for (java.util.Iterator _j14it1430 = (c).iterator(); _j14it1430.hasNext(); ) {
			  ColorMagic cm = (ColorMagic) _j14it1430.next();
				sb.append(" ");
				sb.append(cm.getColorName());
			}
		}
		return sb.toString();
	}
	/**
	 * @return Returns the marked flag.  A clearing that is marked will be highlighted.
	 */
	public boolean isMarked() {
		return marked;
	}
	/**
	 * @param marked The marked to set.
	 */
	public void setMarked(boolean marked) {
		this.marked = marked;
		setMarkColor(DEFAULT_MARK_COLOR);
	}
	public void setMarkColor(Color c) {
		markColor = c;
	}
	public Color getMarkColor() {
		return markColor;
	}
	/**
	 * @return Returns the parent.
	 */
	public TileComponent getParent() {
		return parent;
	}
	public String getDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append(parent.getTileName());
		sb.append(" ");
		if (isEdge()) {
			sb.append(Tile.convertEdge(type, parent.getRotation()));
			sb.append(" edge");
		}
		else {
			sb.append(num);
			if (!"normal".equals(type)) {
				sb.append(" ("+type+")");
			}
		}
		return sb.toString();
	}
	private int distanceToWaterSource(Collection waterSources) {
		int distance = 0;
		ArrayList touchedWaterClearings = new ArrayList();
		touchedWaterClearings.add(this);
		if (this.parent.getGameObject().hasThisAttribute("water_source_clearing") && this.parent.getGameObject().getThisAttribute("water_source_clearing").matches(this.getNumString())) {
			return distance;
		}
		
		boolean foundNewClearings = true;
		ArrayList newWaterClearings = new ArrayList();
		newWaterClearings.add(this);
		ArrayList waterClearingsToCheck = new ArrayList();
		while (foundNewClearings) {
			foundNewClearings = false;
			distance++;
			waterClearingsToCheck.clear();
			waterClearingsToCheck.addAll(newWaterClearings);
			newWaterClearings.clear();
			for (java.util.Iterator _j14it1431 = (waterClearingsToCheck).iterator(); _j14it1431.hasNext(); ) {
			  ClearingDetail clearing = (ClearingDetail) _j14it1431.next();
				Collection c = clearing.getConnectedPathsWithDirection();
				if (c!=null) {
					for (java.util.Iterator _j14it1432 = (c).iterator(); _j14it1432.hasNext(); ) {
					  PathDetail path = (PathDetail) _j14it1432.next();
						if (!path.connectsToAnEdge()) {
							if (path.getTo().isWater() && path.getType().matches("river") && !touchedWaterClearings.contains(path.getTo())) {
								if (path.getTo().parent.getGameObject().hasThisAttribute("water_source_clearing") && path.getTo().parent.getGameObject().getThisAttribute("water_source_clearing").matches(path.getTo().getNumString())) {
									return distance;
								}
								foundNewClearings = true;
								touchedWaterClearings.add(path.getTo());
								newWaterClearings.add(path.getTo());
							}
						} else {
							ClearingDetail connectedClearing = path.findConnection(path.getFrom());
							if (connectedClearing.isWater() && path.getType().matches("river") && !touchedWaterClearings.contains(connectedClearing)) {
								if (connectedClearing.parent.getGameObject().hasThisAttribute("water_source_clearing") && connectedClearing.parent.getGameObject().getThisAttribute("water_source_clearing").matches(connectedClearing.getNumString())) {
									return distance;
								}
								foundNewClearings = true;
								touchedWaterClearings.add(connectedClearing);
								newWaterClearings.add(connectedClearing);
							}
						}
					}
				}
			}
		}
		
		return distance;
	}
	/**
	 * Returns a PathDetail that connects two clearings, or null if none.
	 */
	public PathDetail getConnectingPath(ClearingDetail other) {
		if (other.isEdge()) {
			return parent.getEdgePath(Tile.convertEdge(other.getType(),parent.getRotation()));
		}
		ArrayList paths = getConnectedPaths();
		if (paths!=null) { // might be null if this clearing is not connected to any other
			for (java.util.Iterator _j14it1433 = (paths).iterator(); _j14it1433.hasNext(); ) {
			  PathDetail path = (PathDetail) _j14it1433.next();
				if (path.findConnection(this)==other) {
					return path;
				}
			}
		}
		return null;
	}
	public ArrayList getConnectedPathsWithDirection() {
		ArrayList paths = parent.findConnections(this);
		if (paths==null) return null;
		ArrayList pathsDirected = new ArrayList(); 
		for (java.util.Iterator _j14it1434 = (paths).iterator(); _j14it1434.hasNext(); ) {
		  PathDetail path = (PathDetail) _j14it1434.next();
			if (path.getFrom().equals(this)) pathsDirected.add(path);
			else {
				PathDetail newPath = new PathDetail(path.parent,path.num,path.to,path.from,path.c2,path.c1,path.arc,path.type,path.tileSideName);
				pathsDirected.add(newPath);
			}
		}
		return pathsDirected;
	}
	public ArrayList getConnectedPaths() {
		return parent.findConnections(this);
	}
	public ArrayList getConnectedMapEdges() {
		return parent.findConnectedMapEdges(this);
	}
	public ArrayList getAllConnectedPaths() {
		ArrayList p;
		ArrayList allPaths = new ArrayList();
		p = getConnectedPaths();
		if (p!=null) allPaths.addAll(p);
		p = getConnectedMapEdges();
		if (p!=null) allPaths.addAll(p);
		return allPaths;
	}
	public int getSide() {
		return side;
	}
	/**
	 * Returns this object if on the correctSide, or the other object if not
	 */
	public ClearingDetail correctSide() {
		if (getParent().getFacingIndex()!=side) {
			return getParent().getClearing(getNumString());
		}
		return this;
	}
	public ArrayList getClearingComponentsInPlainSight(CharacterWrapper character) {
		boolean hidden = character.isHidden();
		ArrayList plainSight = new ArrayList();
		ArrayList list = getClearingComponents(false);
		for (java.util.Iterator _j14it1435 = (list).iterator(); _j14it1435.hasNext(); ) {
		  RealmComponent item = (RealmComponent) _j14it1435.next();
			if (item.isPlainSight()) {
				if (!hidden || item.isAtYourFeet(character)) {
					plainSight.add(item);
				}
			}
		}
		return plainSight;
	}
	/**
	 * Returns a collection of all RealmComponents in this clearing.  It does not directly return objects contained
	 * by other objects.  This includes face-up site cards which are in the clearing.
	 */
	public ArrayList getClearingComponents() {
		return getClearingComponents(true);
	}
	/**
	 * Returns a collection of all RealmComponents in this clearing.  It does not directly return objects contained
	 * by other objects.
	 * 
	 * @param includeSites		If true, then all treasure locations in the clearing are searched for face-up site cards, which are included.
	 */
	public ArrayList getClearingComponents(boolean includeSites) {
		ArrayList c = getParent().getRealmComponentsAt(getNum());
		if (includeSites) {
			ArrayList more = new ArrayList();
			for (java.util.Iterator _j14it1436 = (c).iterator(); _j14it1436.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it1436.next();
				if (rc.isTreasureLocation() && !rc.isCacheChit()) {
					// Check TLs for face up SITE CARDS, cuz those should be painted too
					for (java.util.Iterator _j14it1437 = (rc.getGameObject().getHold()).iterator(); _j14it1437.hasNext(); ) {
					  GameObject thing = (GameObject) _j14it1437.next();
						RealmComponent trc = RealmComponent.getRealmComponent(thing);
						if (trc.isTreasure()) {
							TreasureCardComponent treasure = (TreasureCardComponent)trc;
							if (treasure.isFaceUp()) {
								more.add(treasure);
							}
						}
					}
				}
				else if (rc.isPlayerControlledLeader()) {
					CharacterWrapper leader = new CharacterWrapper(rc.getGameObject());
					more.addAll(leader.getFollowingHirelings());
				}
			}
			c.addAll(more);
		}
		return c;
	}
	public ArrayList getTreasureLocations() {
		ArrayList c = getParent().getRealmComponentsAt(getNum());
		ArrayList sites = new ArrayList();
		for (java.util.Iterator _j14it1438 = (c).iterator(); _j14it1438.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it1438.next();
			if (rc.isTreasureLocation() && !rc.isCacheChit()) {
				sites.add(rc);
			}
		}
		return sites;
	}
	public ArrayList getTreasureLocationsAndRedSpecialsAndDwellings() {
		ArrayList c = getParent().getRealmComponentsAt(getNum());
		ArrayList sites = new ArrayList();
		for (java.util.Iterator _j14it1439 = (c).iterator(); _j14it1439.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it1439.next();
			if ((rc.isTreasureLocation() || rc.isRedSpecial() || rc.isDwelling()) && !rc.isCacheChit()) {
				sites.add(rc);
			}
		}
		return sites;
	}
	public ArrayList getSounds() {
		ArrayList c = getParent().getRealmComponentsAt(getNum());
		ArrayList sounds = new ArrayList();
		for (java.util.Iterator _j14it1440 = (c).iterator(); _j14it1440.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it1440.next();
			if (rc.isSound()) {
				sounds.add(rc);
			}
		}
		return sounds;
	}
	public ArrayList getSoundsAndWarnings() {
		ArrayList c = getParent().getRealmComponentsAt(getNum());
		ArrayList chits = new ArrayList();
		for (java.util.Iterator _j14it1441 = (c).iterator(); _j14it1441.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it1441.next();
			if (rc.isSound() || rc.isWarning()) {
				chits.add(rc);
			}
		}
		return chits;
	}
	/**
	 * Returns a complete collection of all RealmComponents in the clearing, including those that are held by
	 * other objects.  In fact, this will get all objects, regardless of depth.
	 */
	public ArrayList getDeepClearingComponents() {
		ArrayList found = new ArrayList();
		for (java.util.Iterator _j14it1442 = (getParent().getRealmComponentsAt(getNum())).iterator(); _j14it1442.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it1442.next();
			found.add(rc);
			Collection gos = RealmUtility.getAllGameObjectsIn(rc.getGameObject(),true);
			for (java.util.Iterator _j14it1443 = (gos).iterator(); _j14it1443.hasNext(); ) {
			  GameObject go = (GameObject) _j14it1443.next();
				RealmComponent inrc = RealmComponent.getRealmComponent(go);
				if (!found.contains(inrc)) {
					found.add(inrc);
				}
			}
		}
		return found;
	}
	
	/**
	 * @return		The ColorMagic for this clearing NOT including items/chits/characters/treasures/etc.  (JUST the
	 * 				clearing's own color magic)
	 */
	public ArrayList getClearingColorMagic() {
		ArrayList list = new ArrayList();
		if (magic[MAGIC_WHITE]) {
			list.add(new ColorMagic(ColorMagic.WHITE,true));
		}
		if (magic[MAGIC_GRAY]) {
			list.add(new ColorMagic(ColorMagic.GRAY,true));
		}
		if (magic[MAGIC_GOLD]) {
			list.add(new ColorMagic(ColorMagic.GOLD,true));
		}
		if (magic[MAGIC_PURPLE]) {
			list.add(new ColorMagic(ColorMagic.PURPLE,true));
		}
		if (magic[MAGIC_BLACK]) {
			list.add(new ColorMagic(ColorMagic.BLACK,true));
		}
		if (magic[MAGIC_VARIED]) {
			GameWrapper gameWrapper = GameWrapper.findGame(this.parent.getGameObject().getGameData());
			DieRoller monsterDie = gameWrapper.getMonsterDie();
			if (monsterDie != null) {
				int number = monsterDie.getValue(0);
				switch (number) {
				case 1:
				case 4:
					list.add(new ColorMagic(ColorMagic.GRAY,true));
					break;
				case 2:
				case 5:
					list.add(new ColorMagic(ColorMagic.GOLD,true));
					break;
				case 3:
				case 6:
					list.add(new ColorMagic(ColorMagic.PURPLE,true));
					break;
				default:
					break;
				}
			}
			DieRoller nativeDie = gameWrapper.getNativeDie();
			if (nativeDie != null) {
				int number = nativeDie.getValue(0);
				switch (number) {
				case 1:
				case 4:
					list.add(new ColorMagic(ColorMagic.GRAY,true));
					break;
				case 2:
				case 5:
					list.add(new ColorMagic(ColorMagic.GOLD,true));
					break;
				case 3:
				case 6:
					list.add(new ColorMagic(ColorMagic.PURPLE,true));
					break;
				default:
					break;
				}
			}
		}
		if (parent.getGameObject().hasThisAttribute(Constants.MOD_COLOR_SOURCE)) {
			ColorMod colorMod = ColorMod.createColorMod(parent.getGameObject().getThisAttribute(Constants.MOD_COLOR_SOURCE));
			list = colorMod.getModifiedColors(list);
		}
		return list;
	}
	/**
	 * Returns all sources of magic in this clearing, available to everyone.
	 */
	public ArrayList getAllSourcesOfColor(boolean checkForColorMods) {
		ArrayList list = new ArrayList();
		for (java.util.Iterator _j14it1444 = (getClearingComponents()).iterator(); _j14it1444.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it1444.next();
			list.addAll(SpellUtility.getSourcesOfColor(rc));
		}
		list.addAll(getClearingColorMagic());
		
		ArrayList uniqueList = new ArrayList();
		for (java.util.Iterator _j14it1445 = (list).iterator(); _j14it1445.hasNext(); ) {
		  ColorMagic cm = (ColorMagic) _j14it1445.next();
			if (!uniqueList.contains(cm)) {
				uniqueList.add(cm);
			}
		}
		
		if (checkForColorMods) {
			uniqueList = ColorMod.getConvertedColorsForThings(getAllActivatedStuff(),uniqueList);
		}
		
		Collections.sort(uniqueList);
		
		return uniqueList;
	}
	public ArrayList getAllActivatedStuff() {
		ArrayList stuff = new ArrayList();
		for (java.util.Iterator _j14it1446 = (getClearingComponents()).iterator(); _j14it1446.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it1446.next();
			for (java.util.Iterator _j14it1447 = (ClearingUtility.dissolveIntoSeenStuff(rc)).iterator(); _j14it1447.hasNext(); ) {
			  RealmComponent seen = (RealmComponent) _j14it1447.next();
				GameObject thing = seen.getGameObject();
				if (thing.hasThisAttribute(Constants.ACTIVATED)) {
					stuff.add(thing);
				}
			}
		}
		return stuff;
	}
	public String getShorthand() {
		return getParent().getTileCode()+getNumString();
	}
	public Point getAbsolutePosition() {
		return absolutePosition;
	}
	public void setAbsolutePosition(Point absolutePosition) {
		this.absolutePosition = absolutePosition;
	}
	public RealmComponent getDwellingWitShelter() {
		for (java.util.Iterator _j14it1448 = (getClearingComponents()).iterator(); _j14it1448.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it1448.next();
			if (rc.isDwelling() && !rc.getGameObject().hasThisAttribute(Constants.NO_SHELTER)) {
				return rc;
			}
		}
		return null;
	}
	public RealmComponent getDwelling() {
		for (java.util.Iterator _j14it1449 = (getClearingComponents()).iterator(); _j14it1449.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it1449.next();
			if (rc.isDwelling()) {
				return rc;
			}
		}
		return null;
	}
	public RealmComponent getGuild() {
		for (java.util.Iterator _j14it1450 = (getClearingComponents()).iterator(); _j14it1450.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it1450.next();
			if (rc.isGuild()) {
				return rc;
			}
		}
		return null;
	}
	public ArrayList getRedSpecials() {
		ArrayList reds = new ArrayList();
		for (java.util.Iterator _j14it1451 = (getClearingComponents()).iterator(); _j14it1451.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it1451.next();
			if (rc.isRedSpecial()) {
				reds.add(rc);
			}
		}
		return reds;
	}
	public boolean holdsDwelling() {
		return getDwelling()!=null;
	}
	public boolean holdsDwellingWithShelter() {
		return getDwellingWitShelter()!=null;
	}
	public boolean holdsGuild() {
		return getGuild()!=null;
	}
	public boolean holdsRedSpecial() {
		for (java.util.Iterator _j14it1452 = (getClearingComponents()).iterator(); _j14it1452.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it1452.next();
			if (rc.isRedSpecial()) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Returns true if this clearing holds a gold special chit for pickup.  Returns false if the chit is a 
	 * Visitor, or if the chit is a 2nd campaign chit (characters can only carry ONE campaign chit at a time)
	 */
	public boolean holdsGoldSpecial(String currentCampaign) {
		for (java.util.Iterator _j14it1453 = (getClearingComponents()).iterator(); _j14it1453.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it1453.next();
			if (rc.isGoldSpecial() && !rc.getGameObject().hasThisAttribute(Constants.VISITOR) && !rc.getGameObject().hasThisAttribute(Constants.NOMAD) && !rc.getGameObject().hasThisAttribute(Constants.DRAW_BACKSIDE)) {
				if (currentCampaign==null || !rc.getGameObject().hasThisAttribute(Constants.CAMPAIGN)) {
					return true;
				}
			}
		}
		return false;
	}
	private String freeActionKey() {
		return "fa_cl"+num;
	}
	private String freeActionObjectKey() {
		return "fao_cl"+num;
	}
	public void addFreeAction(String action,GameObject go) {
		getParent().getGameObject().addThisAttributeListItem(freeActionKey(),action);
		getParent().getGameObject().addThisAttributeListItem(freeActionObjectKey(),go.getStringId());
	}
	public boolean removeFreeAction(String action) {
		ArrayList list = getParent().getGameObject().getThisAttributeList(freeActionKey());
		if (list!=null) {
			int index = list.indexOf(action);
			if (index>=0) {
				list.remove(index);
				ArrayList objectList = getParent().getGameObject().getThisAttributeList(freeActionObjectKey());
				objectList.remove(index);
				if (list.isEmpty()) {
					getParent().getGameObject().removeThisAttribute(freeActionKey());
					getParent().getGameObject().removeThisAttribute(freeActionObjectKey());
				}
				else {
					getParent().getGameObject().setThisAttributeList(freeActionKey(),list);
					getParent().getGameObject().setThisAttributeList(freeActionObjectKey(),objectList);
				}
				return true;
			}
		}
		return false;
	}
	public ArrayList getFreeActions() {
		return getParent().getGameObject().getThisAttributeList(freeActionKey());
	}
	public GameObject getFreeActionObject(String action) {
		ArrayList list = getParent().getGameObject().getThisAttributeList(freeActionKey());
		if (list!=null) {
			int index = list.indexOf(action);
			if (index>=0) {
				ArrayList objectList = getParent().getGameObject().getThisAttributeList(freeActionObjectKey());
				String id = (String) objectList.get(index);
				return parent.getGameObject().getGameData().getGameObject(Long.valueOf(id));
			}
		}
		return null;
	}
	private String spellEffectKey() {
		return "se_cl"+num;
	}
	public void addSpellEffect(String effect) {
		getParent().getGameObject().addThisAttributeListItem(spellEffectKey(),effect);
	}
	public boolean removeSpellEffect(String effect) {
		ArrayList list = getParent().getGameObject().getThisAttributeList(spellEffectKey());
		if (list!=null) {
			int index = list.indexOf(effect);
			if (index>=0) {
				list.remove(index);
				if (list.isEmpty()) {
					getParent().getGameObject().removeThisAttribute(spellEffectKey());
				}
				else {
					getParent().getGameObject().setThisAttributeList(spellEffectKey(),list);
				}
				return true;
			}
		}
		return false;
	}
	public boolean hasSpellEffect(String effect) {
		return getParent().getGameObject().hasThisAttributeListItem(spellEffectKey(),effect);
	}
	public boolean hasEventEffect(String effect) {
		return getParent().getGameObject().hasThisAttributeListItem(effect,this.getNumString());
	}
	public boolean hasKnownGate(CharacterWrapper character) {
		boolean usableGate = false;
		for (java.util.Iterator _j14it1454 = (getDeepClearingComponents()).iterator(); _j14it1454.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it1454.next();
			if (rc.isGate()) {
				if (character.hasOtherChitDiscovery(rc.getGameObject().getName()) || character.affectedByKey(Constants.ALL_GATE)) {
					usableGate = true;
				}
			}
			else if (rc.getGameObject().hasThisAttribute(Constants.NO_GATE)) {
				usableGate = false;
				break;
			}
		}
		return usableGate;
	}
	public static String BL_CONNECT = "bl_con";
	public void setConnectsToBorderland(boolean val) {
		if (val) {
			if (parent.getGameObject().hasThisAttributeListItem(BL_CONNECT,getNumString())) return;
			parent.getGameObject().addThisAttributeListItem(BL_CONNECT,getNumString());
		}
		else {
			parent.getGameObject().removeThisAttributeListItem(BL_CONNECT,getNumString());
		}
	}
	public boolean isConnectsToBorderland() {
		return parent.getGameObject().hasThisAttributeListItem(BL_CONNECT,getNumString());
	}
	
	public void energizeItems() {
		ArrayList colors = getAllSourcesOfColor(true);
		for (java.util.Iterator _j14it1455 = (getClearingComponents()).iterator(); _j14it1455.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it1455.next();
			if (rc.isItem()) {
				energizeItem(rc.getGameObject(),colors);
			}
			if (rc.isCharacter()) {
				for (java.util.Iterator _j14it1456 = ((new CharacterWrapper(rc.getGameObject()).getInventory())).iterator(); _j14it1456.hasNext(); ) {
				  GameObject go = (GameObject) _j14it1456.next();
					energizeItem(go,colors);
				}
			}
		}
	}
	private static void energizeItem(GameObject item, Collection colors) {
		if (item.hasThisAttribute(Constants.MAGIC_COLOR_BONUS)) {
			ColorMagic requiredColor = ColorMagic.makeColorMagic(item.getThisAttribute(Constants.MAGIC_COLOR_BONUS),true);
			for (java.util.Iterator _j14it1457 = (colors).iterator(); _j14it1457.hasNext(); ) {
			  ColorMagic c = (ColorMagic) _j14it1457.next();
				if (c.sameColorAs(requiredColor)) item.setThisAttribute(Constants.MAGIC_COLOR_BONUS_ACTIVE);
				break;
			}
		}
		if (item.hasThisAttribute(Constants.REENERGIZE)) {
			boolean energized = false;
			ColorMagic requiredColor = ColorMagic.makeColorMagic(item.getThisAttribute("magic_color"),true);
			for (java.util.Iterator _j14it1458 = (colors).iterator(); _j14it1458.hasNext(); ) {
			  ColorMagic c = (ColorMagic) _j14it1458.next();
				if (c.sameColorAs(requiredColor)) {
					energized = true;
					break;
				}
			}

			for (java.util.Iterator _j14it1459 = (item.getHold()).iterator(); _j14it1459.hasNext(); ) {
			  GameObject spell = (GameObject) _j14it1459.next();
				if (spell.hasThisAttribute("spell")) {
					SpellWrapper spellWrapper = new SpellWrapper(spell);
					if (energized) {
						GameWrapper game = GameWrapper.findGame(item.getGameData());
						spellWrapper.affectTargets(null,game,false,null);
					}
					if (!energized) {
						spellWrapper.unaffectTargets();
						spellWrapper.makeInert();
					}
				}
			}
		}
	}
	
	public boolean connectionHasThorns(TileLocation other) {
		return this.connectionHasThorns(other.clearing);
	}
	public boolean connectionHasThorns(ClearingDetail other) {
		if (this.getTileLocation().tile==null || other==null || other.getTileLocation().tile==null) return false;
		TileComponent tile = this.getTileLocation().tile;
		TileComponent otherTile = other.getTileLocation().tile;
		if (testThorns(tile,otherTile,other)) return true;
		if (testThorns(otherTile,tile,other)) return true;
		return false;
	}
	private boolean testThorns(TileComponent tile, TileComponent otherTile, ClearingDetail other) {
		if (tile==null||otherTile==null||other==null) return false;
		if ((tile!=null && tile.getGameObject().hasThisAttribute(Constants.EVENT_THORNS)) || (otherTile!=null && otherTile.getGameObject().hasThisAttribute(Constants.EVENT_THORNS))) {
			return true;
		}
		if (tile.equals(otherTile)) {
			if (tile.getGameObject().hasThisAttribute(Constants.THORNS)) {
				ArrayList allThorns = tile.getGameObject().getThisAttributeList(Constants.THORNS);
				String num1 = this.isEdge()?this.toString():this.getNumString();
				String num2 = other.isEdge()?other.toString():other.getNumString();
				for (java.util.Iterator _j14it1460 = (allThorns).iterator(); _j14it1460.hasNext(); ) {
				  String thorns = (String) _j14it1460.next();
					if (thorns.matches(num1+"_"+num2)) return true;
					if (thorns.matches(num2+"_"+num1)) return true;
				}
			}
		}
		else {
			ClearingDetail clearing1 = this;
			ClearingDetail clearing2 = other;
			String edgeName1 = ClearingUtility.getEdgeNameBetweenClearings(clearing1,clearing2);
			String edgeName2 = ClearingUtility.getEdgeNameBetweenClearings(clearing2,clearing1);
			String string1 = clearing1.getNum()+"_"+edgeName1;
			String string2 = clearing2.getNum()+"_"+edgeName2;
			ArrayList list1 = tile.getGameObject().getThisAttributeList(Constants.THORNS);
			ArrayList list2 = otherTile.getGameObject().getThisAttributeList(Constants.THORNS);
			if (list1!=null && list1.contains(string1)) return true;
			if (list2!=null && list2.contains(string2)) return true;
		}
		return false;
	}
	public boolean isAffectedByViolentWinds() {
		GameObject tile = getTileLocation().tile.getGameObject();
		return tile.hasThisAttributeListItem(Constants.VIOLENT_WINDS, String.valueOf(getNum())) || tile.hasThisAttribute(Constants.EVENT_VIOLENT_WINDS);
	}
	public boolean isAffectedByViolentWindsSpell() {
		GameObject tile = getTileLocation().tile.getGameObject();
		return tile.hasThisAttributeListItem(Constants.VIOLENT_WINDS, String.valueOf(getNum()));
	}
	public void setAffectedByViolentWinds(boolean val) {
		GameObject tile = getTileLocation().tile.getGameObject();
		if (val && !tile.hasThisAttributeListItem(Constants.VIOLENT_WINDS, String.valueOf(getNum()))) {
			tile.addThisAttributeListItem(Constants.VIOLENT_WINDS, String.valueOf(getNum()));
			return;
		}
		if (!val && tile.hasThisAttributeListItem(Constants.VIOLENT_WINDS, String.valueOf(getNum()))) {
			tile.removeThisAttributeListItem(Constants.VIOLENT_WINDS, String.valueOf(getNum()));
			return;
		}
	}
}