package com.robin.magic_realm.components.utility;

import java.io.*;
import java.util.*;
import javax.swing.*;

import com.robin.game.objects.*;
import com.robin.general.io.ResourceFinder;
import com.robin.general.swing.*;
import com.robin.general.util.RandomNumber;
import com.robin.general.util.StringUtilities;
import com.robin.magic_realm.components.*;
import com.robin.magic_realm.components.attribute.*;
import com.robin.magic_realm.components.effect.SpellEffectContext;
import com.robin.magic_realm.components.quest.CharacterActionType;
import com.robin.magic_realm.components.quest.requirement.QuestRequirementParams;
import com.robin.magic_realm.components.swing.CenteredMapView;
import com.robin.magic_realm.components.swing.TileLocationChooser;
import com.robin.magic_realm.components.table.*;
import com.robin.magic_realm.components.wrapper.*;

public class SpellUtility {
	public static final class TeleportType {
		private final String _name;
		private final int _ordinal;
		private TeleportType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final TeleportType ChooseAny = new TeleportType("ChooseAny", 0);
		public static final TeleportType ChooseTileTwo = new TeleportType("ChooseTileTwo", 1);
		public static final TeleportType RandomClearing = new TeleportType("RandomClearing", 2);
		public static final TeleportType KnownGate = new TeleportType("KnownGate", 3);
		public static final TeleportType ClearingInSameTile = new TeleportType("ClearingInSameTile", 4);
		public static final TeleportType Location = new TeleportType("Location", 5);

		private static final TeleportType[] _VALUES = { ChooseAny, ChooseTileTwo, RandomClearing, KnownGate, ClearingInSameTile, Location };
		public static TeleportType[] values() { TeleportType[] r = new TeleportType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static TeleportType valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	
	public static void heal(CharacterWrapper character) {
		// Heal all fatigue and wounds - cancels wither curse
		character.removeCurse(Constants.WITHER);
		for (java.util.Iterator _j14it2613 = (character.getWoundedChits()).iterator(); _j14it2613.hasNext(); ) {
		  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it2613.next();
			chit.makeActive();
		}
		for (java.util.Iterator _j14it2614 = (character.getFatiguedChits()).iterator(); _j14it2614.hasNext(); ) {
		  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it2614.next();
			chit.makeActive();
		}
	}
	
	public static void repair(CharacterWrapper character){
		for (java.util.Iterator _j14it2615 = (character.getInventory()).iterator(); _j14it2615.hasNext(); ) {
		  GameObject obj = (GameObject) _j14it2615.next();
			RealmComponent rc = RealmComponent.getRealmComponent(obj);
			if (rc.isArmor()) {
				ArmorChitComponent armor = (ArmorChitComponent) rc;
				if (armor.isDamaged()) {
					armor.setIntact(true);
				}
			}
		}
	}
	
	public static ArrayList getBewitchingSpells(GameObject go) {
		SpellMasterWrapper spellMaster = SpellMasterWrapper.getSpellMaster(go.getGameData());
		return spellMaster.getAffectingSpells(go);
	}
	
	public static ArrayList getBewitchingSpellsWithKey(GameObject target, String key){
		ArrayList result = new ArrayList();
		for (java.util.Iterator _j14it2616 = (getBewitchingSpells(target)).iterator(); _j14it2616.hasNext(); ) {
		  SpellWrapper spell = (SpellWrapper) _j14it2616.next();
			if(spell.isActive() && spell.getGameObject().hasThisAttribute(key)){
				result.add(spell);
			}
		}
		return result;
	}
	
	public static boolean affectedByBewitchingSpellKey(GameObject go,String key) {
		GameData gameData = go.getGameData();
		if (gameData!=null) { // can be null in the character builder tool
			SpellMasterWrapper spellMaster = SpellMasterWrapper.getSpellMaster(gameData);
			for (java.util.Iterator _j14it2617 = (spellMaster.getAffectingSpells(go)).iterator(); _j14it2617.hasNext(); ) {
			  SpellWrapper spell = (SpellWrapper) _j14it2617.next();
				if (spell.isActive() && spell.getGameObject().hasThisAttribute(key)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static void doTeleport(JFrame frame,String reason,CharacterWrapper character,TeleportType teleportType,int teleportSpeed) {
		doTeleport(frame,reason,character,teleportType,teleportSpeed,null);
	}
	
	public static void doTeleport(JFrame frame,String reason,CharacterWrapper character,TeleportType teleportType,int teleportSpeed,String location) {
		if (character.getGameData().getDataName().matches(Constants.DATA_NAME_COMBAT_FRAME)) {
			JOptionPane.showMessageDialog(frame,"There is no way to escape this combat!",reason,JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		// Get the map to pop to the forefront, centered on the clearing, and the move possibilities marked
		TileLocation chosen;
		TileLocation planned = character.getPlannedLocation();
		if (teleportType==TeleportType.RandomClearing) {
			ArrayList clearings = planned.tile.getClearings();
			clearings.remove(planned.clearing); // Any clearing EXCEPT this one
			int r = RandomNumber.getRandom(clearings.size());
			chosen = ((ClearingDetail) clearings.get(r)).getTileLocation();
			JOptionPane.showMessageDialog(frame,"The "+character.getGameObject().getName()+" teleports to "+chosen,reason,JOptionPane.INFORMATION_MESSAGE);
		}
		else if (teleportType==TeleportType.Location) {
			ArrayList clearings = new ArrayList();
			GamePool pool = new GamePool(character.getGameData().getGameObjects());
			ArrayList destinations = pool.find("name="+location);
			for (java.util.Iterator _j14it2618 = (destinations).iterator(); _j14it2618.hasNext(); ) {
			  GameObject destination = (GameObject) _j14it2618.next();
				TileLocation loc = ClearingUtility.getTileLocation(destination);
				if (loc!=null && loc.clearing!=null) {
					clearings.add(loc.clearing);
				}
			}
			int r = RandomNumber.getRandom(clearings.size());
			chosen = ((ClearingDetail) clearings.get(r)).getTileLocation();
			JOptionPane.showMessageDialog(frame,"The "+character.getGameObject().getName()+" teleports to "+chosen,reason,JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			TeleportType _tt = teleportType;
			if (_tt == TeleportType.ChooseTileTwo) {
				CenteredMapView.getSingleton().setMarkClearingAlertText("Teleport "+character.getGameObject().getName()+" to which tile?");
				CenteredMapView.getSingleton().markAdjacentTiles(planned.tile,true,1); // recurse once to pick up the second set!
			}
			else if (_tt == TeleportType.KnownGate) {
				ArrayList knownGates = findKnownGatesForCharacter(character);
				if (!knownGates.isEmpty()) {
					CenteredMapView.getSingleton().setMarkClearingAlertText("Which known gate?");
					for (java.util.Iterator _j14it2619 = (knownGates).iterator(); _j14it2619.hasNext(); ) {
					  GateChitComponent gate = (GateChitComponent) _j14it2619.next();
						ClearingDetail clearing = gate.getCurrentLocation().clearing;
						clearing.setMarked(true);
					}
				}
				else {
					JOptionPane.showMessageDialog(frame,"The "+character.getGameObject().getName()+" has not discovered any gates!  Spell fails.",reason,JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
			else if (_tt == TeleportType.ClearingInSameTile) {
				CenteredMapView.getSingleton().setMarkClearingAlertText("Teleport "+character.getGameObject().getName()+" to which clearing?");
				CenteredMapView.getSingleton().markAllClearings(false);
				for (java.util.Iterator _j14it2620 = (planned.tile.getClearings()).iterator(); _j14it2620.hasNext(); ) {
				  ClearingDetail clearing = (ClearingDetail) _j14it2620.next();
					clearing.setMarked(true);
				}
			}
			else { // default / ChooseAny
				CenteredMapView.getSingleton().setMarkClearingAlertText("Teleport "+character.getGameObject().getName()+" to which clearing?");
				CenteredMapView.getSingleton().markAllClearings(true);
				if (planned.isInClearing()) {
					planned.clearing.setMarked(false);
				}
			}
			TileLocationChooser chooser = new TileLocationChooser(frame,CenteredMapView.getSingleton(),planned);
			chooser.setVisible(true);
			chosen = chooser.getSelectedLocation();
		}
		
		character.jumpMoveHistory(); // because we didn't walk here
		character.moveToLocation(null,chosen);
		RealmLogging.logMessage(character.getGameObject().getName(),"Teleported to "+chosen);
		if (teleportType!=TeleportType.RandomClearing && teleportType!=TeleportType.Location) {
			CenteredMapView.getSingleton().markAllClearings(false);
			CenteredMapView.getSingleton().markAllTiles(false);
		}
		if (CenteredMapView.isFollowEnabled()) {
			CenteredMapView.getSingleton().centerOn(chosen);
		}

		// Followers should stay behind!
		for (java.util.Iterator _j14it2621 = (character.getFollowingHirelings()).iterator(); _j14it2621.hasNext(); ) {
		  RealmComponent h = (RealmComponent) _j14it2621.next();
			ClearingUtility.moveToLocation(h.getGameObject(), planned);
			if (h.getGameObject().hasThisAttribute(Constants.CAPTURE)) {
				character.removeHireling(h.getGameObject());
				RealmLogging.logMessage(character.getGameObject().getName(),"The "+h.getGameObject().getName()+" escaped!");
			}
		}
		
		//CJM -- leaving this for a moment in case I break something 
//		for (Iterator i=character.getFollowingHirelings().iterator();i.hasNext();) {
//			RealmComponent hireling = (RealmComponent)i.next();
//			ClearingUtility.moveToLocation(hireling.getGameObject(),planned);
//			if (hireling.getGameObject().hasThisAttribute(Constants.CAPTURE)) {
//				// A captured traveler is immediately freed!
//				character.removeHireling(hireling.getGameObject());
//				RealmLogging.logMessage(character.getGameObject().getName(),"The "+hireling.getGameObject().getName()+" escaped!");
//			}
//		}
		
		// Be sure to clear out combat...
		character.clearCombat();
		CombatWrapper.clearAllCombatInfo(character.getGameObject());
		SpellMasterWrapper sm = SpellMasterWrapper.getSpellMaster(character.getGameData());
		for (java.util.Iterator _j14it2622 = (sm.getAffectingSpells(character.getGameObject())).iterator(); _j14it2622.hasNext(); ) {
		  SpellWrapper spell = (SpellWrapper) _j14it2622.next();
			if (spell.isActive() && !spell.hasAffectedTargets() && spell.getAttackSpeed().getNum() > teleportSpeed) {
				spell.removeTarget(character.getGameObject());
				if (spell.getTargetCount() == 0) {
					spell.cancelSpell();
					RealmLogging.logMessage(character.getName(), "Targeted spell "+spell.getName() + " canceled, as "+character.getName()+" teleported.");
				}
			}
		}
		
		QuestRequirementParams params = new QuestRequirementParams();
		params.actionType = CharacterActionType.Teleport;
		params.actionName = teleportType.toString();
		character.testQuestRequirements(frame,params);
	}
	
	private static ArrayList findKnownGatesForCharacter(CharacterWrapper character) {
		GameData gameData = character.getGameObject().getGameData();
		ArrayList knownGates = new ArrayList();
		
		for (java.util.Iterator _j14it2623 = (character.getOtherChitDiscoveries()).iterator(); _j14it2623.hasNext(); ) {
		  String d = (String) _j14it2623.next();
			RealmComponent rc = RealmComponent.getRealmComponent(gameData.getGameObjectByName(d));
			if (rc.isGate()) {
				knownGates.add((GateChitComponent) rc);
			}
		}

		//CJM -- leaving this for a moment in case I break something 
//		ArrayList list = character.getOtherChitDiscoveries();
//		
//		if (list!=null) {
//			for (Iterator i=character.getOtherChitDiscoveries().iterator();i.hasNext();) {
//				String discovery = (String)i.next();
//				GameObject go = gameData.getGameObjectByName(discovery);
//				RealmComponent rc = RealmComponent.getRealmComponent(go);
//				if (rc.isGate()) {
//					knownGates.add((GateChitComponent)rc);
//				}
//			}
//		}
		return knownGates;
	}
	
	/*
	 * b.1) When a character Teleports due to a Wish, he and all
of his horses and items (regardless of their weight) instantly
move to whatever clearing he chooses. If an individual teleports
to the clearing where he is already located, he does not move.
When a denizen teleports, it goes to the place where it started the
game: a Ghost or Garrison native goes to the clearing where it
started the game, and any other monster or native goes to its box
on the Appearance Chart. Note: If a hired native is teleported to
the Appearance Chart, he instantly becomes unhired.
	 */
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

		public static final SummonType undead = new SummonType("undead", 0);
		public static final SummonType animal = new SummonType("animal", 1);
		public static final SummonType elemental = new SummonType("elemental", 2);
		public static final SummonType demon = new SummonType("demon", 3);

		private static final SummonType[] _VALUES = { undead, animal, elemental, demon };
		public static SummonType[] values() { SummonType[] r = new SummonType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static SummonType valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	private static MonsterTable getMonsterTableFor(JFrame parent,String summonType) {
		MonsterTable monsterTable = null;
		SummonType _st = SummonType.valueOf(summonType);
		if (_st == SummonType.undead) {
			monsterTable = new RaiseDead(parent);
		}
		else if (_st == SummonType.elemental) {
			monsterTable = new SummonElemental(parent);
		}
		else if (_st == SummonType.animal) {
			monsterTable = new SummonAnimal(parent);
		}
		else if (_st == SummonType.demon) {
			monsterTable = new SummonDemon(parent);
		}
		return monsterTable;
	}
	public static void summonRandomCompanion(JFrame parent,GameObject caster,CharacterWrapper character,SpellWrapper spell,String summonType) {
		summonCompanion(parent,caster,character,spell,summonType,0);
	}
	
	public static void summonCompanion(JFrame parent,GameObject caster,CharacterWrapper character,SpellWrapper spell,String summonType,int dieRoll) {
		MonsterTable monsterTable = getMonsterTableFor(parent,summonType);
		if (summonType.matches(SummonType.demon.toString())) {
			character = new CharacterWrapper(caster);
		}
		DieRoller roller = DieRollBuilder.getDieRollBuilder(parent,character).createRoller(monsterTable);
		if (dieRoll>0&&dieRoll<7) {
			roller.setDice(dieRoll);
		}
		else {
			roller.rollDice(summonType);
		}
		String result = monsterTable.apply(character,roller);
		RealmLogging.logMessage(caster.getName(),monsterTable.getTableName(true)+" roll: "+roller.getDescription());
		RealmLogging.logMessage(caster.getName(),monsterTable.getTableName(true)+" result: "+result);
		ArrayList list = spell.getGameObject().getThisAttributeList("created");
		if (list==null) {
			list = new ArrayList();
		}
		for (java.util.Iterator _j14it2624 = (monsterTable.getMonsterCreator().getMonstersCreated()).iterator(); _j14it2624.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2624.next();
			list.add(go.getStringId());
		}
		spell.getGameObject().setThisAttributeList("created",list);
	}
	
	public static ArrayList getCreatedCompanions(SpellWrapper spell) {
		GameData gameData = spell.getGameObject().getGameData();
		ArrayList created = new ArrayList();
		ArrayList list = spell.getGameObject().getThisAttributeList("created");
		if (list!=null) {
			for (java.util.Iterator _j14it2625 = (list).iterator(); _j14it2625.hasNext(); ) {
			  String id = (String) _j14it2625.next();
				GameObject go = gameData.getGameObject(Long.valueOf(id));
				created.add(go);
			}
		}
		return created;
	}
	public static void unsummonCompanions(SpellWrapper spell) {
		CharacterWrapper caster = spell.getCaster();
		ArrayList created = getCreatedCompanions(spell);
		for (java.util.Iterator _j14it2626 = (created).iterator(); _j14it2626.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2626.next();
			go.removeThisAttribute("clearing");
			go.setThisAttribute(Constants.DEAD);
			caster.removeHireling(go);
			GameObject heldBy = go.getHeldBy();
			if (heldBy!=null) {
				heldBy.remove(go);
			}
		}
	}
	public static int getSpellCount(GameObject spellLocation,Boolean awakened,boolean excludeAsteriskType) {
		return getSpells(spellLocation,awakened,excludeAsteriskType,false).size();
	}
	public static ArrayList getSpells(GameObject spellLocation,Boolean awakened,boolean excludeAsteriskType,boolean ignoreEnchanted) {
		ArrayList list = new ArrayList();
		
		RealmComponent sl = RealmComponent.getRealmComponent(spellLocation);
		if (ignoreEnchanted || !sl.isEnchanted()) { // enchanted artifacts/books cannot have active spells!
			for (java.util.Iterator _j14it2627 = (spellLocation.getHold()).iterator(); _j14it2627.hasNext(); ) {
			  GameObject obj = (GameObject) _j14it2627.next();
				RealmComponent rc = RealmComponent.getRealmComponent(obj);
				if (rc.isSpell()) {
					String spellType = obj.getThisAttribute("spell");
					if (spellType!=null && spellType.trim().length()>0) {
						if (!excludeAsteriskType || !spellType.trim().equals("*")) {
							if (awakened==null || obj.hasThisAttribute(Constants.SPELL_AWAKENED)==awakened.booleanValue()) {
								list.add(obj);
							}
						}
					}
				}
			}
		}
		return list;
	}
	
	public static String getColorSourceName(RealmComponent rc) {
		String colorName;
		if (rc.getGameObject().hasThisAttribute(Constants.MOD_COLOR_SOURCE)) {
			colorName = rc.getGameObject().getThisAttribute(Constants.MOD_COLOR_SOURCE);
		}
		else {
			colorName = rc.getGameObject().getThisAttribute("color_source");
		}
		return colorName;
	}
	
	public static ArrayList getSourcesOfColor(RealmComponent test) {
		ArrayList colors = new ArrayList();
		ArrayList seen = ClearingUtility.dissolveIntoSeenStuff(test);
		for (java.util.Iterator _j14it2628 = (seen).iterator(); _j14it2628.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it2628.next();
			String colorName = getColorSourceName(rc);
			ColorMagic cm = ColorMagic.makeColorMagic(colorName,true);
			if (cm!=null) {
				colors.add(cm);
			}
			if (colorName!=null && colorName.matches("prism")) {
				colors.add(new ColorMagic(ColorMagic.GRAY,true));
				colors.add(new ColorMagic(ColorMagic.PURPLE,true));
				colors.add(new ColorMagic(ColorMagic.GOLD,true));
			}
		}
		return colors;
	}
	public static ColorMagic getColorMagicFor(RealmComponent rc) {
		return ColorMagic.makeColorMagic(getColorSourceName(rc),true);
	}
	public static int chooseRedDie(JFrame parent,String spellKey,CharacterWrapper character) {
		String table = null;
		if (Wish.KEY.equalsIgnoreCase(spellKey)) {
			table = "smallblessing";
		}
		else if (Curse.KEY.equalsIgnoreCase(spellKey)) {
			table = "curse";
		}
		else if (PowerOfThePit.KEY.equalsIgnoreCase(spellKey)) {
			table = "powerofthepit";
		}
		else if ("violentstorm".equalsIgnoreCase(spellKey)) {
			table = "violentstorm";
		}
		else if ("transform".equalsIgnoreCase(spellKey)) {
			table = "transform";
		}
		return DieFaceChooser.getRedDieFace(parent,StringUtilities.capitalize(spellKey),character.getGameObject().getName()+" has the ability to control the RED die.  Choose a result:",table);
	}
	private static String getSpellReferenceName(GameObject spell) {
		String name = spell.getName().toLowerCase();
		name = StringUtilities.findAndReplace(name, " ", "");
		name = StringUtilities.findAndReplace(name, "'", "");
		return name;
	}
	public static String getSpellName(GameObject spell) {
		HostPrefWrapper hostPrefs = HostPrefWrapper.findHostPrefs(spell.getGameData());
		if (hostPrefs.hasPref(Constants.HOUSE1_NO_SECRETS)) {
			return spell.getName();
		}
		return "##a Spell|"+spell.getName()+"##";
	}
	public static String getSpellDetail(GameObject spell) {
		String name = getSpellReferenceName(spell);
		String resource = "text/"+name+".rtf";
		StringBuffer sb = new StringBuffer();
		try {
			InputStream stream = ResourceFinder.getInputStream(resource);
			if (stream==null) {
				throw new IOException("");
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String line;
			while((line=reader.readLine())!=null) {
				sb.append(line);
			}
		}
		catch(IOException ex) {
			sb.append("Resource not found: "+resource);
		}
		return sb.toString();
	}
	public static ImageIcon getSpellDetailTable(GameObject spell) {
		String name = getSpellReferenceName(spell);
		String tableResource = "images/tables/"+name+".gif";
		ImageIcon table = null;
		if (tableResource!=null) {
			table = IconFactory.findIcon(tableResource);
		}
		return table;
	}

	public static void ApplyNamedSpellEffectToTarget(String effect, GameObject target, SpellWrapper spellWrapper) {
		ApplyNamedSpellEffectWithValueToTarget(effect, target, spellWrapper, "");
	}
	
	public static void ApplyNamedSpellEffectWithValueToTarget(String effect, GameObject target, SpellWrapper spellWrapper, String value) {
		if(!target.hasThisAttribute(effect)){
			target.setThisAttribute(effect,value);
		}
		else{
			spellWrapper.cancelSpell();
			RealmLogging.logMessage(spellWrapper.getCaster().getGameObject().getName(),"Spell cancelled, because the targeted character already has this ability.");
		}
	}
	
	public static void ApplyNamedSpellEffectWithValuesToTarget(String effect, GameObject target, SpellWrapper spellWrapper, ArrayList values) {
		if(!target.hasThisAttribute(effect)){
			target.setThisAttributeList(effect, values);
		}
		else {
			for (java.util.Iterator _j14it2629 = (values).iterator(); _j14it2629.hasNext(); ) {
			  String value = (String) _j14it2629.next();
				target.addThisAttributeListItem(effect, value);
			}
		}
	}

	public static boolean ApplyNamedSpellEffectToTargetAndReturn(String effect, GameObject target, SpellWrapper spellWrapper) {
		if(!target.hasThisAttribute(effect)){
			target.setThisAttribute(effect);
			return true;
		}
		spellWrapper.cancelSpell();
		RealmLogging.logMessage(spellWrapper.getCaster().getGameObject().getName(),"Spell cancelled, because the targeted character already has this ability.");
		return false;
	}
	
	public static void setAlteredSpeed(RealmComponent chit, String attributeName, SpellWrapper spellWrapper) {	
		String attributeValue = chit.getGameObject().getThisAttribute(attributeName);
		int newspeed = spellWrapper.getGameObject().getThisInt(attributeValue);
		chit.getGameObject().setThisAttribute("move_speed_change", newspeed);
	}

	public static boolean targetsAreBeingAttackedByHirelings(ArrayList attackers, GameObject caster) {
		for (java.util.Iterator _j14it2630 = (attackers).iterator(); _j14it2630.hasNext(); ) {
		  GameObject atk = (GameObject) _j14it2630.next();
			RealmComponent rc = RealmComponent.getRealmComponent(atk);
			if (!rc.getGameObject().equals(caster)) {
				RealmComponent owner = rc.getOwner();
				if (owner != null && owner.getGameObject().equals(caster)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static GameObject findNativeFromTheseGroups(ArrayList groups, GameObjectFilter filter, GameWrapper game) {
		ArrayList lowerCaseGroups = new ArrayList();
		for (java.util.Iterator _j14it2631 = (groups).iterator(); _j14it2631.hasNext(); ) {
		  String g = (String) _j14it2631.next();
			lowerCaseGroups.add(g.toLowerCase());
		}
		ArrayList candidates = new ArrayList();
		for (java.util.Iterator _j14it2632 = (game.getGameData().getGameObjects()).iterator(); _j14it2632.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2632.next();
			if (go.hasThisAttribute("native") && go.hasThisAttribute("denizen")
					&& lowerCaseGroups.contains(go.getThisAttribute("native").toLowerCase())
					&& filter.test(go)) {
				candidates.add(go);
			}
		}
		if (candidates.isEmpty()) return null;
		Collections.sort(candidates, new NativeHireOrder());
		return (GameObject) candidates.get(0);
	}

	public static GameObject findNativeFromTheseGroups(String group, GameObjectFilter filter, GameWrapper game) {
		ArrayList candidates = new ArrayList();
		for (java.util.Iterator _j14it2633 = (game.getGameData().getGameObjects()).iterator(); _j14it2633.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2633.next();
			if (go.hasThisAttribute("native") && go.hasThisAttribute("denizen")
					&& go.getThisAttribute("native").toLowerCase().equals(group.toLowerCase())
					&& filter.test(go)) {
				candidates.add(go);
			}
		}
		if (candidates.isEmpty()) return null;
		Collections.sort(candidates, new NativeHireOrder());
		return (GameObject) candidates.get(0);
	}

	public static void bringSummonToClearing(CharacterWrapper character, GameObject summon, SpellWrapper spell, ArrayList createdMonsters){
		TileLocation tl = character.getCurrentLocation();
		character.addHireling(summon);
		CombatWrapper combat = new CombatWrapper(summon);
		combat.setSheetOwner(true);
		if (tl!=null && tl.isInClearing()) {
			tl.clearing.add(summon,null);
		}
		character.getGameObject().add(summon); // so that you don't have to assign as a follower right away
		
		ArrayList list = spell.getGameObject().getThisAttributeList("created");
		if (list==null) {
			list = new ArrayList();
		}
		
		if(createdMonsters == null){
			list.add(summon.getStringId());
		} else {
			for (java.util.Iterator _j14it2634 = (createdMonsters).iterator(); _j14it2634.hasNext(); ) {
			  GameObject go = (GameObject) _j14it2634.next();
				list.add(go.getStringId());
			}
		}
	}
	
	public static RollResult rollResult(SpellEffectContext context, String rollType){
		DieRoller roller = DieRollBuilder
				.getDieRollBuilder(context.Parent, context.Spell.getCaster(),context.Spell.getRedDieLock())
				.createRoller(rollType.toLowerCase());
		
		int die = roller.getHighDieResult();
		int mod = context.Spell.getGameObject().getThisInt(Constants.SPELL_MOD);
		
		die += mod;
		if (die>=6) die=6;
		if (die<1) die=1;

		
		RealmLogging.logMessage(context.Spell.getCaster().getGameObject().getName(), rollType + " roll: "+ roller.getDescription());
		return new RollResult(roller, roller.getStringResult(), die);
	}
	
	public static TileLocation chooseTileLocation(JFrame parent, String title) {
		CenteredMapView cmap = CenteredMapView.getSingleton();
		cmap.markAllTiles(true);
		cmap.setMapAttentionMessage(title);
		TileLocationChooser chooser = new TileLocationChooser(parent,cmap,null);
		chooser.setLocationRelativeTo(parent);
		chooser.setVisible(true);
		cmap.markAllTiles(false);
		return chooser.getSelectedLocation();
	}
}