package com.robin.magic_realm.RealmBattle;

import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.robin.game.objects.GameObject;
import com.robin.general.swing.DieRoller;
import com.robin.magic_realm.components.*;
import com.robin.magic_realm.components.attribute.*;
import com.robin.magic_realm.components.swing.RealmComponentOptionChooser;
import com.robin.magic_realm.components.utility.*;
import com.robin.magic_realm.components.wrapper.*;

public class MoveActivator {
	
	public static final class MoveActionResult {
		private final String _name;
		private final int _ordinal;
		private MoveActionResult(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final MoveActionResult NO_MOVE_POSSIBLE = new MoveActionResult("NO_MOVE_POSSIBLE", 0);
		public static final MoveActionResult SUCCESSFUL = new MoveActionResult("SUCCESSFUL", 1);
		public static final MoveActionResult UNSUCCESSFUL = new MoveActionResult("UNSUCCESSFUL", 2);

		private static final MoveActionResult[] _VALUES = { NO_MOVE_POSSIBLE, SUCCESSFUL, UNSUCCESSFUL };
		public static MoveActionResult[] values() { MoveActionResult[] r = new MoveActionResult[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static MoveActionResult valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	
	public static final String FLIP_SIDE_TEXT = "(back)";
	
	private CombatFrame combatFrame;
	private BattleModel battleModel;
	private RealmComponent activeParticipant;
	private CharacterWrapper activeCharacter;
	private HostPrefWrapper hostPrefs;
	
	private RealmComponent selectedMoveChit;
	
	private Fly fly = null;
	private ArrayList attackers;
	
	public MoveActivator(CombatFrame combatFrame) {
		this.combatFrame = combatFrame;
		battleModel = combatFrame.getBattleModel();
		activeParticipant = combatFrame.getActiveParticipant();
		activeCharacter = combatFrame.getActiveCharacter();
		hostPrefs = combatFrame.getHostPrefs();
	}
	public ArrayList getAttackers() {
		return attackers;
	}
	public RealmComponent getSelectedMoveChit() {
		return selectedMoveChit;
	}
	public Fly getFly() {
		return fly;
	}
	public boolean isFly() {
		return fly!=null;
	}
	/**
	 * Takes a collection of BattleChits, and returns only the flyers.
	 */
	private Collection filterFlyers(Collection in) {
		ArrayList list = new ArrayList();
		for (java.util.Iterator _j14it375 = (in).iterator(); _j14it375.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it375.next();
			RealmComponent target = rc.getTarget();
			if (target!=null && target.equals(activeParticipant)) {
				// As long as the character is not immune to this monster type, include it
				if (!activeParticipant.isImmuneTo(rc)) {
					BattleChit chit = (BattleChit)rc;
					Speed speed = chit.getFlySpeed();
					if (speed!=null) {
						list.add(rc);
					}
				}
			}
		}
		return list;
	}
	private Collection getFlyingAttackersOnActive() {
		return filterFlyers(battleModel.getAllBattleParticipants(true));
	}
	private Speed getFastestAttackerFlySpeed() {
		Collection c = getFlyingAttackersOnActive();
		// Find fastest attacker fly speed on your sheet
		Speed fastest = new Speed(); // infinitely slow
		for (java.util.Iterator _j14it376 = (c).iterator(); _j14it376.hasNext(); ) {
		  RealmComponent i = (RealmComponent) _j14it376.next();
			BattleChit chit = (BattleChit)i;
			Speed speed = chit.getFlySpeed();
			if (speed!=null && speed.fasterThan(fastest)) {
				fastest = speed;
			}
		}
		return fastest;
	}
	public Speed getFastestAttackerMoveSpeed() {
		// Find fastest attacker move speed on your sheet
		Speed fastest = new Speed(); // infinitely slow
		for (java.util.Iterator _j14it377 = (battleModel.getAllBattleParticipants(true)).iterator(); _j14it377.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it377.next();
			RealmComponent target = rc.getTarget();
			if (target!=null && target.equals(activeParticipant)) {
				String magicImmunity = rc.getGameObject().getThisAttribute(Constants.MAGIC_IMMUNITY);
				// As long as the character is not immune to this monster type, include it
				if (!activeParticipant.isImmuneTo(rc) && (!activeParticipant.getGameObject().hasThisAttribute(Constants.BLINDING_LIGHT) || (magicImmunity!=null && (magicImmunity.matches("prism") || magicImmunity.matches("purple"))))) {
					BattleChit chit = (BattleChit)rc;
					Speed speed = chit.getMoveSpeed();
					if (speed.fasterThan(fastest)) {
						fastest = speed;
					}
				}
			}
		}
		
		// Don't forget to check charge chits!!
		if (activeParticipant.isCharacter() && !activeParticipant.getGameObject().hasThisAttribute(Constants.BLINDING_LIGHT)) {
			CombatWrapper combat = new CombatWrapper(activeParticipant.getGameObject());
			for (java.util.Iterator _j14it378 = (combat.getChargeChits()).iterator(); _j14it378.hasNext(); ) {
			  GameObject go = (GameObject) _j14it378.next();
				RealmComponent rc = RealmComponent.getRealmComponent(go);
				if (rc.isActionChit()) {
					CharacterActionChitComponent chit = (CharacterActionChitComponent)rc;
					Speed moveSpeed = chit.getMoveSpeed();
					if (moveSpeed.fasterThan(fastest)) {
						fastest = moveSpeed;
					}
				}
				else if (rc.isFlyChit()) {
					FlyChitComponent flyChit = (FlyChitComponent)rc;
					Speed flySpeed = flyChit.getSpeed();
					if (flySpeed.fasterThan(fastest)) {
						fastest = flySpeed;
					}
				}
			}
		}
		return fastest;
	}
	public MoveActionResult playedValidMoveChit(String title,String noMoveMessage) {
		return playedValidMoveChit(title,noMoveMessage,true);
	}
	public MoveActionResult playedValidMoveChit(String title,String noMoveMessage,boolean checkStumble) {
		// Find fastest attacker move speed on your sheet
		attackers = battleModel.getAttackersFor(activeParticipant);
		Speed fastest = getFastestAttackerMoveSpeed();
		
		// Also check charge chits (if any)
		CombatWrapper chargeCombat = new CombatWrapper(activeParticipant.getGameObject());
		Collection chargeChits = chargeCombat.getChargeChits();
		for (java.util.Iterator _j14it379 = (chargeChits).iterator(); _j14it379.hasNext(); ) {
		  GameObject go = (GameObject) _j14it379.next();		
			RealmComponent rc = RealmComponent.getRealmComponent(go);
			attackers.add(rc);
			if (rc.isActionChit()) {
				CharacterActionChitComponent chit = (CharacterActionChitComponent)rc;
				Speed moveSpeed = chit.getMoveSpeed();
				if (moveSpeed.fasterThan(fastest)) {
					fastest = moveSpeed;
				}
			}
			else if (rc.isFlyChit()) {
				FlyChitComponent flyChit = (FlyChitComponent)rc;
				Speed flySpeed = flyChit.getSpeed();
				if (flySpeed.fasterThan(fastest)) {
					fastest = flySpeed;
				}
			}
		}
		
		// Find all playable options
		Speed speedToBeat = hostPrefs.hasPref(Constants.OPT_STUMBLE)?new Speed():fastest; // Stumble allows any move chit
		Collection moveSpeedOptions = activeCharacter.getMoveSpeedOptions(speedToBeat,true,true);
		Collection availableManeuverOptions = combatFrame.getAvailableManeuverOptions(0,true); // if running away, then the red-side-up check has already been done
		moveSpeedOptions.retainAll(availableManeuverOptions); // Intersection between the two
		
		TileLocation currentCombatLocation = battleModel.getBattleLocation();
		//Remove flying chits if affected by Violent Winds
		if (currentCombatLocation.clearing.isAffectedByViolentWinds()) {
			Collection nonFlyingOptions = new ArrayList();
			for (java.util.Iterator _j14it380 = (moveSpeedOptions).iterator(); _j14it380.hasNext(); ) {
			  RealmComponent option = (RealmComponent) _j14it380.next();	
				if (!option.isFlyChit()) {
					nonFlyingOptions.add(option);
				}
			}
			moveSpeedOptions.retainAll(nonFlyingOptions);
		}
		
		// Check for flying possibilities
		ArrayList flyChits = activeCharacter.getFlyStrengthChits(false);;
		
		for (java.util.Iterator _j14it381 = (activeCharacter.getActiveInventory()).iterator(); _j14it381.hasNext(); ) {
		  GameObject item = (GameObject) _j14it381.next();
			RealmComponent itemRc = RealmComponent.getRealmComponent(item);
			if ((itemRc.isHorse() && ((SteedChitComponent)itemRc).flies()) || (itemRc.isNativeHorse() && ((NativeSteedChitComponent)itemRc).flies())) {
				flyChits.add(new StrengthChit(
						item,
						new Strength(item.getThisAttribute("vulnerability")),
						BattleUtility.getMoveSpeed(itemRc)));
			}
		}
		
		Speed fastestFlyer = null;
		if (flyChits!=null && !flyChits.isEmpty()) {
			fastestFlyer = getFastestAttackerFlySpeed();
			Speed flyingSpeedToBeat = hostPrefs.hasPref(Constants.OPT_STUMBLE)?new Speed():fastestFlyer;
			Strength needed = activeCharacter.getNeededSupportWeight();
			if (currentCombatLocation.hasClearing() && !currentCombatLocation.clearing.isAffectedByViolentWinds()) {
				// Filter out those flyChits that aren't strong enough or fast enough
				for (java.util.Iterator _j14it382 = (flyChits).iterator(); _j14it382.hasNext(); ) {
				  StrengthChit sc = (StrengthChit) _j14it382.next();
					if (sc.getSpeed().fasterThan(flyingSpeedToBeat) && sc.getStrength().strongerOrEqualTo(needed)) {
						RealmComponent rc = sc.getRealmComponent();
						if (rc.isMonster()) {
							rc = ((MonsterChitComponent)rc).getMoveChit();
						}
						if (!moveSpeedOptions.contains(rc)) {
							moveSpeedOptions.add(rc);
						}
					}
				}
			}
		}
		
		if (moveSpeedOptions.size()>0) {
			if (hostPrefs.hasPref(Constants.OPT_RIDING_HORSES)) {
				// Check for a horse in the move options.  If there is one, that's the ONLY option!
				for (java.util.Iterator _j14it383 = (moveSpeedOptions).iterator(); _j14it383.hasNext(); ) {
				  RealmComponent rc = (RealmComponent) _j14it383.next();
					if (rc.isHorse()) {
						moveSpeedOptions = new ArrayList();
						moveSpeedOptions.add(rc);
						break;
					}
				}
			}
			
			// Choose one
			RealmComponentOptionChooser chooser = getChooserForMoveOptions(combatFrame,activeCharacter,moveSpeedOptions,true);
			chooser.setVisible(true);
			if (chooser.getSelectedText()!=null) {
				selectedMoveChit = chooser.getFirstSelectedComponent();
				CombatWrapper combat = new CombatWrapper(activeCharacter.getGameObject());
				
				String key = chooser.getSelectedOptionKey();
				if (selectedMoveChit.isHorse()) {
					SteedChitComponent horse = (SteedChitComponent)selectedMoveChit;
					if (key.endsWith("F")) {
						horse.flip();
					}
					if (horse.isGalloping()) {
						combat.setGalloped(true);
					}
				}
				
				fly = null;
				if (Fly.valid(selectedMoveChit)) {
					fly = new Fly(selectedMoveChit);
				}
				
				if (!selectedMoveChit.isHorse()) {
					combat.addUsedChit(selectedMoveChit.getGameObject());
				}
				
				if (fly!=null) {
					// Flying away?  Make some adjustments here...
					fastest = fastestFlyer;
					attackers = new ArrayList(filterFlyers(attackers));
				}
					
				if (checkStumble && !fastest.isInfinitelySlow() && hostPrefs.hasPref(Constants.OPT_STUMBLE)) {
					// Running might NOT be a success...
					Speed speed = fly!=null?fly.getSpeed():BattleUtility.getMoveSpeed(selectedMoveChit);
					
					int stumbleModifier = speed.getNum()-fastest.getNum();
					
					// Include all attackers, EXCEPT monster weapons
					for (java.util.Iterator _j14it384 = (attackers).iterator(); _j14it384.hasNext(); ) {
					  RealmComponent attacker = (RealmComponent) _j14it384.next();
						if (!attacker.isMonsterPart()) {
							stumbleModifier++;
						}
					}
					
					DieRoller runAwayRoll = DieRollBuilder.getDieRollBuilder(combatFrame,activeCharacter).createRoller("stumble");
					runAwayRoll.addModifier(stumbleModifier);
					CombatFrame.setRunAwayRoll(runAwayRoll);
					combatFrame.madeChanges();
					boolean success = runAwayRoll.getHighDieResult()<7;
					if (!success) {
						return MoveActionResult.UNSUCCESSFUL;
					}
				}
				activeCharacter.setRunAwayLastUsedChit(selectedMoveChit.isFlyChit()?"FLY":"MOVE");
				return MoveActionResult.SUCCESSFUL;
			}
		}
		else {
			JOptionPane.showMessageDialog(combatFrame,noMoveMessage,title,JOptionPane.INFORMATION_MESSAGE);
		}
		return MoveActionResult.NO_MOVE_POSSIBLE;
	}
	public void prepareFatigue() {
		// Prepare fatigue
		Effort effortUsed = BattleUtility.getEffortUsed(activeCharacter);
		int free = activeCharacter.getEffortFreeAsterisks();
		int runAwayFatigue = effortUsed.getNeedToFatigue(free);
		CombatFrame.setRunAwayFatigue(runAwayFatigue);
		if (runAwayFatigue>0) {
			// Make sure that the tile is marked, so that combat can be extended if necessary
			CombatWrapper tileCombat = new CombatWrapper(battleModel.getBattleLocation().tile.getGameObject());
			tileCombat.setWasFatigue(true);
		}
	}
	public static RealmComponentOptionChooser getChooserForMoveOptions(JFrame frame,CharacterWrapper activeCharacter,Collection moveOptions,boolean includeHorseFlip) {
		CombatWrapper combat = new CombatWrapper(activeCharacter.getGameObject());
		boolean canGallop = !combat.hasGalloped();
		TileLocation loc = activeCharacter.getCurrentLocation();
		if (loc !=null && loc.tile!=null && loc.tile.getGameObject().hasThisAttribute(Constants.EVENT_HORSE_WHISPER)) {
			canGallop = false;
		}
		
		Strength heaviestInv = activeCharacter.getNeededSupportWeight();
		RealmComponentOptionChooser chooser = new RealmComponentOptionChooser(frame,"Select Maneuver:",true);
		int keyN = 0;
		for (java.util.Iterator _j14it385 = (moveOptions).iterator(); _j14it385.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it385.next();
			String key = "C"+(keyN++);
			if (includeHorseFlip && rc.isHorse()) {
				SteedChitComponent horse = (SteedChitComponent)rc;
				String flipKey = key+"F";
				
				// This complication is to make sure they appear in the same order, and that they are strong enough
				if (horse.isTrotting()) {
					if (horse.getTrotStrength().strongerOrEqualTo(heaviestInv)) {
						chooser.addOption(key,"");
						chooser.addRealmComponentToOption(key,rc);
					}
					if (canGallop && !horse.getGameObject().hasThisAttribute(Constants.HORSE_WHISPER) && horse.getGallopStrength().strongerOrEqualTo(heaviestInv)) {
						chooser.addOption(flipKey,FLIP_SIDE_TEXT);
						chooser.addRealmComponentToOption(flipKey,rc,RealmComponentOptionChooser.DisplayOption.Flipside);
					}
				}
				else {
					if (horse.getTrotStrength().strongerOrEqualTo(heaviestInv)) {
						chooser.addOption(flipKey,FLIP_SIDE_TEXT);
						chooser.addRealmComponentToOption(flipKey,rc,RealmComponentOptionChooser.DisplayOption.Flipside);
					}
					if (canGallop && !horse.getGameObject().hasThisAttribute(Constants.HORSE_WHISPER) && horse.getGallopStrength().strongerOrEqualTo(heaviestInv)) {
						chooser.addOption(key,"");
						chooser.addRealmComponentToOption(key,rc);
					}
				}
			}
			else if (includeHorseFlip && rc.isNativeHorse()) {
				NativeSteedChitComponent horse = (NativeSteedChitComponent)rc;
				String flipKey = key+"F";
				
				// This complication is to make sure they appear in the same order, and that they are strong enough
				if (horse.isTrotting()) {
					if (horse.getTrotStrength().strongerOrEqualTo(heaviestInv)) {
						chooser.addOption(key,"");
						chooser.addRealmComponentToOption(key,rc);
					}
					if (canGallop && !horse.getGameObject().hasThisAttribute(Constants.HORSE_WHISPER) && horse.getGallopStrength().strongerOrEqualTo(heaviestInv)) {
						chooser.addOption(flipKey,FLIP_SIDE_TEXT);
						chooser.addRealmComponentToOption(flipKey,rc,RealmComponentOptionChooser.DisplayOption.Flipside);
					}
				}
				else {
					if (horse.getTrotStrength().strongerOrEqualTo(heaviestInv)) {
						chooser.addOption(flipKey,FLIP_SIDE_TEXT);
						chooser.addRealmComponentToOption(flipKey,rc,RealmComponentOptionChooser.DisplayOption.Flipside);
					}
					if (canGallop && !horse.getGameObject().hasThisAttribute(Constants.HORSE_WHISPER) && horse.getGallopStrength().strongerOrEqualTo(heaviestInv)) {
						chooser.addOption(key,"");
						chooser.addRealmComponentToOption(key,rc);
					}
				}
			}
			else {
				chooser.addOption(key,"");
				chooser.addRealmComponentToOption(key,rc);
			}
		}
		return chooser;
	}
}