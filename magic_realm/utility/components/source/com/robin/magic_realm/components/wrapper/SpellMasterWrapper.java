package com.robin.magic_realm.components.wrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JFrame;

import com.robin.game.objects.*;
import com.robin.general.util.HashLists;
import com.robin.general.util.RandomNumber;
import com.robin.magic_realm.components.attribute.ColorMagic;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.events.RealmEvents;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.RealmCalendar;

/**
 * A Class to encapsulate permanent/day spell handling
 */
public class SpellMasterWrapper extends GameObjectWrapper {
	
	/*
	 * Durations:
	 * 
	 * Continuous
	 * 	Combat - 42.7/1: remains in effect for the rest of the day; it expires at Midnight.
	 * 	Day - 42.7/2: remains in effect until the end of the next Daylight period; it expires
	 * 				at Sunset of the day after it is cast.
	 * 	Permanent - 42.8: never expire, but they do not continuously affect their targets. A Permanent
	 * 				spell affects its target only when it is "energized" . When it does not affect
	 * 				its target, it is "inert".
	 * 
	 * Delayed
	 * 	Move
	 * 	Phase
	 * 
	 * Instant
	 * 	Attack
	 * 	Instant
	 */
	
	private static final String PERMANENT_SPELLS = "permanent";
	private static final String DAY_SPELLS = "day";
	private static final String COMBAT_SPELLS = "combat";
	private static final String PHASE_SPELLS = "phase";
	private static final String MOVE_SPELLS = "move";
	private static final String MIDNIGHT_SPELLS = "midnight";
	
	public SpellMasterWrapper(GameObject gm) {
		super(gm);
	}
	public String getBlockName() {
		return "this";
	}
	/**
	 * @param target	The target to test
	 * 
	 * @return		List of SpellWrapper objects that are currently bewitching the target
	 */
	public ArrayList getAffectingSpells(GameObject target) {
		ArrayList ret = new ArrayList();
		for (java.util.Iterator _j14it1490 = (getSpells(null)).iterator(); _j14it1490.hasNext(); ) {
		  SpellWrapper spell = (SpellWrapper) _j14it1490.next();
			if (spell.targetsGameObject(target)) {
				ret.add(spell);
			}
		}
		return ret;
	}
	/**
	 * @return		The single SpellWrapper object that was cast by this incantation object (if any)
	 */
	public SpellWrapper getIncantedSpell(GameObject incantation) {
		for (java.util.Iterator _j14it1491 = (getSpells(null)).iterator(); _j14it1491.hasNext(); ) {
		  SpellWrapper s = (SpellWrapper) _j14it1491.next();
			GameObject inc = s.getIncantationObject();
			if (inc != null && inc.equals(incantation)) {
				return new SpellWrapper(inc);
			}
		}
		return null;
	}
	
	public ArrayList getList(String key) {
		ArrayList list = super.getList(key);
		if (list==null) {
			return new ArrayList();
		}
		return list;
	}
	/**
	 * @param location			The location to search
	 * @param needForCancel		If true, excludes noCancel spells
	 * 
	 * @return				A list of ALL breakable spells (currently Combat,Day,Permanent) that are in the clearing.
	 */
	public ArrayList getAllSpellsInClearing(TileLocation location,boolean needForCancel) {
		ArrayList ret = new ArrayList();
		for (java.util.Iterator _j14it1492 = (getSpells(null)).iterator(); _j14it1492.hasNext(); ) {
		  SpellWrapper spell = (SpellWrapper) _j14it1492.next();
			if (!needForCancel || !spell.isNoCancelSpell()) {
				TileLocation test = spell.getCurrentLocation();
				if (test!=null) { // test might be null if the spell is targeting a treasure that hasn't yet been seen
					if (test.equals(location)) {
						ret.add(spell);
					}
					else if (test.isTileOnly()) {
						ret.add(spell);
					}
				}
			}
		}
		return ret;
	}
	private ArrayList getSpells(String duration) {
		GameData data = getGameObject().getGameData();
		ArrayList ids = new ArrayList();
		if (duration==null) {
			ids.addAll(getList(PERMANENT_SPELLS));
			ids.addAll(getList(DAY_SPELLS));
			ids.addAll(getList(COMBAT_SPELLS));
			ids.addAll(getList(PHASE_SPELLS));
			ids.addAll(getList(MOVE_SPELLS));
		}
		else {
			ids.addAll(getList(duration));
		}
		ArrayList ret = new ArrayList();
		for (java.util.Iterator _j14it1493 = (ids).iterator(); _j14it1493.hasNext(); ) {
		  String id = (String) _j14it1493.next();
			GameObject go = data.getGameObject(Long.valueOf(id));
			ret.add(new SpellWrapper(go));
		}
		return ret;
	}
	public void breakAllIncantations(boolean markIncantationChitsAsUsed) {
		for (java.util.Iterator _j14it1494 = (getSpells(null)).iterator(); _j14it1494.hasNext(); ) {
		  SpellWrapper spell = (SpellWrapper) _j14it1494.next();
			spell.breakIncantation(markIncantationChitsAsUsed);
		}
	}
	public void expireAllSpells() {
		// Expire the spells, one at a time
		for (java.util.Iterator _j14it1495 = (getSpells(null)).iterator(); _j14it1495.hasNext(); ) {
		  SpellWrapper spell = (SpellWrapper) _j14it1495.next();
			spell.expireSpell();
		}
		
		// Clear all spell lists
		removeAttribute(DAY_SPELLS);
		removeAttribute(COMBAT_SPELLS);
		removeAttribute(PERMANENT_SPELLS);
		removeAttribute(PHASE_SPELLS);
		removeAttribute(MOVE_SPELLS);
	}
	public void expireAllSpellsBut(String[] spells) {
		// Expire the spells, one at a time
		for (java.util.Iterator _j14it1496 = (getSpells(null)).iterator(); _j14it1496.hasNext(); ) {
		  SpellWrapper spell = (SpellWrapper) _j14it1496.next();
			boolean ignoreSpell=false;
			for (int _j14i1497 = 0; _j14i1497 < spells.length; _j14i1497++) {
			  String spellName = spells[_j14i1497];
				if (spellName.toLowerCase().matches(spell.getName().toLowerCase())) {
					ignoreSpell = true;
					break;
				}
			}
			if (!ignoreSpell) {
				spell.expireSpell();
			}
		}
	}
	/**
	 * Causes all day spells to expire
	 */
	public void expireDaySpells() {
		// Expire the spells, one at a time
		for (java.util.Iterator _j14it1498 = (getSpells(DAY_SPELLS)).iterator(); _j14it1498.hasNext(); ) {
		  SpellWrapper spell = (SpellWrapper) _j14it1498.next();
			spell.expireSpell();
		}
		
		// Clear the day spell list
		removeAttribute(DAY_SPELLS);
	}
	
	/**
	 * Causes all combat spells to expire
	 */
	public void expireCombatSpells() {
		// Expire the spells, one at a time
		for (java.util.Iterator _j14it1499 = (getSpells(COMBAT_SPELLS)).iterator(); _j14it1499.hasNext(); ) {
		  SpellWrapper spell = (SpellWrapper) _j14it1499.next();
			spell.expireSpell();
		}

		// Clear the combat spell list
		removeAttribute(COMBAT_SPELLS);
	}
	/**
	 * Causes all phase spells to expire
	 * 
	 * @return true when spells were expired
	 */
	public boolean expirePhaseSpells() {
		boolean ret = false;
		// Expire the spells, one at a time
		for (java.util.Iterator _j14it1500 = (getSpells(PHASE_SPELLS)).iterator(); _j14it1500.hasNext(); ) {
		  SpellWrapper spell = (SpellWrapper) _j14it1500.next();
			spell.expireSpell();
			ret = true;
		}

		// Clear the phase spell list
		removeAttribute(PHASE_SPELLS);
		return ret;
	}
	/**
	 * Calculates spell locations, and figures out the colors (infinite sources only here)
	 */
	public void energizePermanentSpells(JFrame frame,GameWrapper game) {
		HashLists conflicts = new HashLists();
		ArrayList affectingSpells = new ArrayList();
		for (java.util.Iterator _j14it1501 = (getSpells(PERMANENT_SPELLS)).iterator(); _j14it1501.hasNext(); ) {
		  SpellWrapper spell = (SpellWrapper) _j14it1501.next();
			if (spell.isInert()) { // no point energizing non-inert spells!
				TileLocation loc = spell.getCurrentLocation();
				if (loc==null) {
					// This means the spell was lost somehow
					spell.expireSpell();
				}
				else {
					if (spellCanEnergize(game,loc,spell,true)) {
						if (spell.canConflict()) {
							// Before we can add this, need to make sure that the affected target isn't already afflicted with a STRONGER spell
							boolean addSpell = true;
							
							int str = spell.getConflictStrength();
							GameObject at = spell.getAffectedTarget().getGameObject();
							ArrayList affSpells = getAffectingSpells(at);
							for (java.util.Iterator _j14it1502 = (affSpells).iterator(); _j14it1502.hasNext(); ) {
							  SpellWrapper affSpell = (SpellWrapper) _j14it1502.next();
								if (!affSpell.isInert() && affSpell.canConflict() && !affSpell.equals(spell)) {
									int aStr = affSpell.getConflictStrength();
									if (aStr>=str) {
										// The active spell gets priority because it is equal to or greater in strength than this spell
										addSpell = false;
										break;
									}
								}
							}
							
							if (addSpell) {
								conflicts.put(at,spell);
							}
						}
						else {
							affectingSpells.add(spell);
						}
					}
				}
			}
		}
		for (java.util.Iterator _j14it1503 = (affectingSpells).iterator(); _j14it1503.hasNext(); ) {
		  SpellWrapper energizedSpell = (SpellWrapper) _j14it1503.next();
			energizedSpell.affectTargets(frame,game,false,affectingSpells);
		}
		
		// Resolve conflicts per target (if any)
		for (java.util.Iterator _j14it1504 = (conflicts.keySet()).iterator(); _j14it1504.hasNext(); ) {
		  GameObject target = (GameObject) _j14it1504.next();
			SpellWrapper strongest = null;
			ArrayList list = conflicts.getList(target);
			if (list.size()==1) {
				// No conflict!
				strongest = (SpellWrapper) list.get(0);
			}
			else {
				// Multiple spells affecting target - find the strongest one
				ArrayList strongGroup = new ArrayList();
				int bestStrength = 0;
				for (java.util.Iterator _j14it1505 = (list).iterator(); _j14it1505.hasNext(); ) {
				  SpellWrapper spell = (SpellWrapper) _j14it1505.next();
					int strength = spell.getConflictStrength();
					if (strength > bestStrength) {
						strongGroup.clear();
						bestStrength = strength;
					}
					if (strength == bestStrength) {
						strongGroup.add(spell);
					}
				}
				if (strongGroup.size()==1) {
					// Found the strongest spell
					strongest = (SpellWrapper) strongGroup.get(0);
				}
				else {
					// uh-oh, this means there are two spells with equal strength affecting the same target
					// In this case, it is up to the spellcaster to decide which spell goes into effect
					
					// Make sure its all the same caster
					CharacterWrapper commonCaster = null;
					for (java.util.Iterator _j14it1506 = (strongGroup).iterator(); _j14it1506.hasNext(); ) {
					  SpellWrapper spell = (SpellWrapper) _j14it1506.next();
						CharacterWrapper caster = spell.getCaster();
						if (commonCaster==null) {
							commonCaster = caster;
						}
						if (!commonCaster.equals(caster)) {
							commonCaster = null;
							break;
						}
					}
					if (commonCaster!=null) {
						// Found a common caster
						commonCaster.setSpellConflicts(strongGroup);
					}
					else {
						// No common caster?  No choice here, but to pick one at random!
						int r = RandomNumber.getRandom(strongGroup.size());
						strongest = (SpellWrapper) strongGroup.get(r);
					}
				}
			}
			if (strongest!=null) {
				strongest.affectTargets(frame,game,false,affectingSpells);
			}
		}
	}
	private static boolean spellCanEnergize(GameWrapper game,TileLocation loc,SpellWrapper spell,boolean includeCalendar) {
		RealmCalendar cal = RealmCalendar.getCalendar(game.getGameObject().getGameData());
		ArrayList infiniteSources = new ArrayList();
		if (loc!=null) {
			if (loc.isInClearing()) {
				infiniteSources.addAll(loc.clearing.getAllSourcesOfColor(true));
			}
			else if (loc.isBetweenClearings()) {
				infiniteSources.addAll(loc.clearing.getAllSourcesOfColor(true));
				infiniteSources.addAll(loc.getOther().clearing.getAllSourcesOfColor(true));
			}
			else if (loc.isTileOnly()) {
				infiniteSources.addAll(loc.tile.getAllSourcesOfColor());
			}
			else if (loc.isBetweenTiles()) {
				infiniteSources.addAll(loc.tile.getAllSourcesOfColor());
				infiniteSources.addAll(loc.getOther().tile.getAllSourcesOfColor());
			}
		}
		if (includeCalendar) {
			// 7th day color magic!
			infiniteSources.addAll(cal.getColorMagic(game.getMonth(),game.getDay()));
		}

		// Events
		infiniteSources.addAll(RealmEvents.getInfiniteColorMagicSources(game.getGameObject().getGameData()));
		
		if (infiniteSources.size()>0) {
			ColorMagic spellColor = spell.getRequiredColorMagic();
			if (spellColor==null || infiniteSources.contains(spellColor)) {
				return true;
			}
		}
		return false;
	}
	public void deenergizePermanentSpells() {
		// Make each permanent spell "inert", one at a time
		boolean didDeenergize = false;
		ArrayList denergizeSpells = new ArrayList();
		for (java.util.Iterator _j14it1507 = (getSpells(PERMANENT_SPELLS)).iterator(); _j14it1507.hasNext(); ) {
		  SpellWrapper spell = (SpellWrapper) _j14it1507.next();
			if (!spell.isInert()) { // If spell is already inert, then don't deenergize it!
				// Don't deenergize spells that have an automatic supply of color magic
				if (spellCanEnergize(GameWrapper.findGame(spell.getGameData()),spell.getCurrentLocation(),spell,false)) {
					continue;
				}
				
				if(!spell.isAlwaysActive()){
					denergizeSpells.add(spell);
					didDeenergize = true;
				}
			}
		}
		Collections.sort(denergizeSpells,new Comparator() {
			public int compare(Object o1,Object o2) {
				SpellWrapper s1 = (SpellWrapper) o1;
				SpellWrapper s2 = (SpellWrapper) o2;
				int pos1 = s1.getGameObject().getThisInt("spell_strength");
				int pos2 = s2.getGameObject().getThisInt("spell_strength");
				return pos1-pos2;
			}
		});
		for (java.util.Iterator _j14it1508 = (denergizeSpells).iterator(); _j14it1508.hasNext(); ) {
		  SpellWrapper spell = (SpellWrapper) _j14it1508.next();
			if (spell.getGameObject().hasThisAttribute(Constants.BREAK_IF_INERT)) {
				spell.expireSpell();
			} else {
				spell.unaffectTargets();
				spell.makeInert();
			}
		}
		
		if (didDeenergize) {
			// Yes, this is a dangerous recursion, but necessary I think.  If you Absorb Essence, and then activate a Transform
			// spell, you end up two layers deep in spell wizardry, and this is the only way to guarantee that both are deenergized.
			deenergizePermanentSpells();
		}
	}
	public void uneffectTargetsForMidnightSpells(GameWrapper game) {
		for (java.util.Iterator _j14it1509 = (getSpells(MIDNIGHT_SPELLS)).iterator(); _j14it1509.hasNext(); ) {
		  SpellWrapper spell = (SpellWrapper) _j14it1509.next();
			spell.expireSpell();
		}
		this.removeAttribute(MIDNIGHT_SPELLS);
	}
	
	/**
	 * Adds a spell to the master list.  Organizes into three bins:  permanent, day, combat.  All other spell
	 * duration types (instant,attack,phase,move) are ignored here.
	 */
	public void addSpell(SpellWrapper spell) {
		String duration = spell.getGameObject().getThisAttribute("duration");
		if (PERMANENT_SPELLS.equals(duration)) {
			addPermanentSpell(spell);
		}
		else if (DAY_SPELLS.equals(duration)) {
			addDaySpell(spell);
		}
		else if (COMBAT_SPELLS.equals(duration)) {
			addCombatSpell(spell);
		}
		else if (PHASE_SPELLS.equals(duration)) {
			addPhaseSpell(spell);
		}
		else if (MOVE_SPELLS.equals(duration)) {
			addMoveSpell(spell);
		}
		if (spell.getGameObject().hasThisAttribute(Constants.UNEFFECT_AT_MIDNIGHT)) {
			addMidnightSpell(spell);
		}
	}
	private void addPermanentSpell(SpellWrapper spell) {
		addListItem(PERMANENT_SPELLS,spell.getGameObject().getStringId());
	}
	private void addDaySpell(SpellWrapper spell) {
		addListItem(DAY_SPELLS,spell.getGameObject().getStringId());
	}
	private void addCombatSpell(SpellWrapper spell) {
		addListItem(COMBAT_SPELLS,spell.getGameObject().getStringId());
	}
	private void addMoveSpell(SpellWrapper spell) {
		addListItem(MOVE_SPELLS,spell.getGameObject().getStringId());
	}
	private void addPhaseSpell(SpellWrapper spell) {
		// only add a phase spell that has been activated (has a chit)
		if (spell.hasPhaseChit()) {
			addListItem(PHASE_SPELLS,spell.getGameObject().getStringId());
		}
	}
	private void addMidnightSpell(SpellWrapper spell) {
		addListItem(MIDNIGHT_SPELLS,spell.getGameObject().getStringId());
	}
	

	public void removeSpell(SpellWrapper spell) {
		String duration = spell.getGameObject().getThisAttribute("duration");
		ArrayList list = getList(duration);
		if (list!=null && list.contains(spell.getGameObject().getStringId())) {
			list = new ArrayList(list);
			list.remove(spell.getGameObject().getStringId());
			setList(duration,list);
		}
		list = getList(MIDNIGHT_SPELLS);
		if (list!=null && list.contains(spell.getGameObject().getStringId())) {
			list = new ArrayList(list);
			list.remove(spell.getGameObject().getStringId());
			setList(MIDNIGHT_SPELLS,list);
		}
	}
	public void expireIncantationSpell(GameObject incantation) {
		SpellWrapper spell = getIncantedSpell(incantation);
		if (spell!=null) {
			spell.expireSpell();
		}
	}
	public void expireBewitchingSpells(GameObject target) {
		expireBewitchingSpells(target,null);
	}
	public void expireBewitchingSpells(GameObject target,SpellWrapper exclude) {
		for (java.util.Iterator _j14it1510 = (getAffectingSpells(target)).iterator(); _j14it1510.hasNext(); ) {
		  SpellWrapper spell = (SpellWrapper) _j14it1510.next();
			if (exclude!=null && exclude.getGameObject().equals(spell.getGameObject())) continue;
			spell.removeTarget(target);
			if (spell.getTargetCount()==0) {
				spell.expireSpell();
			}
		}
	}
	public void restoreBewitchingNullifiedSpells(GameObject target,SpellWrapper exclude) {
		for (java.util.Iterator _j14it1511 = (getAffectingSpells(target)).iterator(); _j14it1511.hasNext(); ) {
		  SpellWrapper spell = (SpellWrapper) _j14it1511.next();
			if (exclude==null || !exclude.getGameObject().equals(spell.getGameObject())) {
				if (spell.isNullified()) {
					spell.restoreSpell();
				}
			}
		}
	}
	
	private static final String SPELL_MASTER_KEY = "__RealmSpellMaster_";
	public static Long MASTER_ID = null;
	public static SpellMasterWrapper getSpellMaster(GameData data) {
		if (MASTER_ID==null) {
			GamePool pool = new GamePool(data.getGameObjects());
			ArrayList list = pool.find(SPELL_MASTER_KEY);
			GameObject gm = null;
			if (list!=null && list.size()==1) {
				gm = (GameObject) list.iterator().next();
			}
			if (gm==null) {
				gm = data.createNewObject();
				gm.setName(SPELL_MASTER_KEY);
				gm.setThisAttribute(SPELL_MASTER_KEY);
			}
			MASTER_ID = Long.valueOf(gm.getId());
			return new SpellMasterWrapper(gm);
		}
		return new SpellMasterWrapper(data.getGameObject(MASTER_ID));
	}
}