package com.robin.magic_realm.RealmBattle.targeting;

import java.util.*;

import javax.swing.JOptionPane;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.*;

public class SpellTargetingClearing extends SpellTargetingSpecial {

	public SpellTargetingClearing(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}
	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		
		// Assume that activeParticipant IS character
		CharacterWrapper character = new CharacterWrapper(activeParticipant.getGameObject());
	
		ArrayList clearingTargetType = spell.getGameObject().getThisAttributeList("target_clearing");
		if (clearingTargetType.contains("combatants")) {
			ArrayList allBattleParticipants = battleModel.getAllBattleParticipants(true); // clearing affects everything, including hidden!!!
			for (java.util.Iterator _j14it868 = (allBattleParticipants).iterator(); _j14it868.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it868.next();
				gameObjects.add(rc.getGameObject());
			}
		}
		if (clearingTargetType.contains("characters")) {
			ArrayList allBattleParticipants = battleModel.getAllBattleParticipants(true); // clearing affects everything, including hidden!!!
			for (java.util.Iterator _j14it869 = (allBattleParticipants).iterator(); _j14it869.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it869.next();
				if (rc.isCharacter() && !gameObjects.contains(rc.getGameObject())) {
					gameObjects.add(rc.getGameObject());
				}
			}
		}
		if (clearingTargetType.contains("monsters")) {
			ArrayList allBattleParticipants = battleModel.getAllBattleParticipants(true); // clearing affects everything, including hidden!!!
			for (java.util.Iterator _j14it870 = (allBattleParticipants).iterator(); _j14it870.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it870.next();
				if (rc.isMonster() && !gameObjects.contains(rc.getGameObject())) {
					gameObjects.add(rc.getGameObject());
				}
			}
		}
		if (clearingTargetType.contains("demons")) {
			ArrayList allBattleParticipants = battleModel.getAllBattleParticipants(true); // clearing affects everything, including hidden!!!
			for (java.util.Iterator _j14it871 = (allBattleParticipants).iterator(); _j14it871.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it871.next();
				if (rc.getGameObject().hasThisAttribute(Constants.DEMON) && !gameObjects.contains(rc.getGameObject())) {
					gameObjects.add(rc.getGameObject());
				}
			}
		}
		if (clearingTargetType.contains("imps")) {
			ArrayList allBattleParticipants = battleModel.getAllBattleParticipants(true); // clearing affects everything, including hidden!!!
			for (java.util.Iterator _j14it872 = (allBattleParticipants).iterator(); _j14it872.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it872.next();
				if (rc.getGameObject().hasThisAttribute(Constants.IMP) && !gameObjects.contains(rc.getGameObject())) {
					gameObjects.add(rc.getGameObject());
				}
			}
		}
		if (clearingTargetType.contains("ghosts")) {
			ArrayList allBattleParticipants = battleModel.getAllBattleParticipants(true); // clearing affects everything, including hidden!!!
			for (java.util.Iterator _j14it873 = (allBattleParticipants).iterator(); _j14it873.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it873.next();
				if (rc.getGameObject().hasThisAttribute(Constants.GHOST) && !gameObjects.contains(rc.getGameObject())) {
					gameObjects.add(rc.getGameObject());
				}
			}
		}
		if (clearingTargetType.contains("skeletons")) {
			ArrayList allBattleParticipants = battleModel.getAllBattleParticipants(true); // clearing affects everything, including hidden!!!
			for (java.util.Iterator _j14it874 = (allBattleParticipants).iterator(); _j14it874.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it874.next();
				if (rc.getGameObject().hasThisAttribute(Constants.SKELETON) && !gameObjects.contains(rc.getGameObject())) {
					gameObjects.add(rc.getGameObject());
				}
			}
		}
		if (clearingTargetType.contains("wraiths")) {
			ArrayList allBattleParticipants = battleModel.getAllBattleParticipants(true); // clearing affects everything, including hidden!!!
			for (java.util.Iterator _j14it875 = (allBattleParticipants).iterator(); _j14it875.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it875.next();
				if (rc.getGameObject().hasThisAttribute(Constants.WRAITH) && !gameObjects.contains(rc.getGameObject())) {
					gameObjects.add(rc.getGameObject());
				}
			}
		}
		if (clearingTargetType.contains("ghouls")) {
			ArrayList allBattleParticipants = battleModel.getAllBattleParticipants(true); // clearing affects everything, including hidden!!!
			for (java.util.Iterator _j14it876 = (allBattleParticipants).iterator(); _j14it876.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it876.next();
				if (rc.getGameObject().hasThisAttribute(Constants.GHOUL) && !gameObjects.contains(rc.getGameObject())) {
					gameObjects.add(rc.getGameObject());
				}
			}
		}
		if (clearingTargetType.contains("vampires")) {
			ArrayList allBattleParticipants = battleModel.getAllBattleParticipants(true); // clearing affects everything, including hidden!!!
			for (java.util.Iterator _j14it877 = (allBattleParticipants).iterator(); _j14it877.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it877.next();
				if (rc.getGameObject().hasThisAttribute(Constants.VAMPIRE) && !gameObjects.contains(rc.getGameObject())) {
					gameObjects.add(rc.getGameObject());
				}
			}
		}
		if (clearingTargetType.contains("succubi")) {
			ArrayList allBattleParticipants = battleModel.getAllBattleParticipants(true); // clearing affects everything, including hidden!!!
			for (java.util.Iterator _j14it878 = (allBattleParticipants).iterator(); _j14it878.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it878.next();
				if (rc.getGameObject().hasThisAttribute(Constants.SUCCUBUS) && !gameObjects.contains(rc.getGameObject())) {
					gameObjects.add(rc.getGameObject());
				}
			}
		}
		if (clearingTargetType.contains("devils")) {
			ArrayList allBattleParticipants = battleModel.getAllBattleParticipants(true); // clearing affects everything, including hidden!!!
			for (java.util.Iterator _j14it879 = (allBattleParticipants).iterator(); _j14it879.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it879.next();
				if (rc.getGameObject().hasThisAttribute(Constants.DEVIL) && !gameObjects.contains(rc.getGameObject())) {
					gameObjects.add(rc.getGameObject());
				}
			}
		}
		if (clearingTargetType.contains("undead")) {
			ArrayList allBattleParticipants = battleModel.getAllBattleParticipants(true); // clearing affects everything, including hidden!!!
			for (java.util.Iterator _j14it880 = (allBattleParticipants).iterator(); _j14it880.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it880.next();
				if ((rc.getGameObject().hasThisAttribute(Constants.UNDEAD) || rc.getGameObject().hasThisAttribute(Constants.UNDEAD_SUMMONED)) && !gameObjects.contains(rc.getGameObject())) {
					gameObjects.add(rc.getGameObject());
				}
			}
		}
		if (clearingTargetType.contains("spells")) {
			SpellMasterWrapper sm = SpellMasterWrapper.getSpellMaster(spell.getGameObject().getGameData());
			for (java.util.Iterator _j14it881 = (sm.getAllSpellsInClearing(battleModel.getBattleLocation(),true)).iterator(); _j14it881.hasNext(); ) {
			  SpellWrapper sw = (SpellWrapper) _j14it881.next();
				gameObjects.add(sw.getGameObject());
			}
		}
		if (clearingTargetType.contains("curses")) {
			for (java.util.Iterator _j14it882 = (battleModel.getAllParticipatingCharacters()).iterator(); _j14it882.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it882.next();
				CharacterWrapper thisCharacter = new CharacterWrapper(rc.getGameObject());
				Collection curses = thisCharacter.getAllCurses();
				if (curses.size()>0) {
					gameObjects.add(rc.getGameObject());
				}
			}
		}
		if (clearingTargetType.contains("horses")) {
			ArrayList allBattleParticipants = battleModel.getAllBattleParticipants(true); // clearing affects everything, including hidden!!!
			for (java.util.Iterator _j14it883 = (allBattleParticipants).iterator(); _j14it883.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it883.next();
				if ((rc.getGameObject().hasThisAttribute("horse") || rc.getGameObject().hasThisAttribute(RealmComponent.MONSTER_STEED)) && !gameObjects.contains(rc.getGameObject())) {
					gameObjects.add(rc.getGameObject());
				}
				for (java.util.Iterator _j14it884 = (rc.getHold()).iterator(); _j14it884.hasNext(); ) {
				  GameObject go = (GameObject) _j14it884.next();
					if ((go.hasThisAttribute("horse") || go.hasThisAttribute(RealmComponent.MONSTER_STEED)) && !gameObjects.contains(go)) {
						gameObjects.add(go);
					}
				}
			}
		}
		if (clearingTargetType.contains("sites")) {
			TileLocation loc = battleModel.getBattleLocation();
			for (java.util.Iterator _j14it885 = (loc.clearing.getClearingComponents()).iterator(); _j14it885.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it885.next();
				if (rc.isTreasureLocation()) {
					gameObjects.add(rc.getGameObject());
				}
			}
		}
		boolean ignorebattle = spell.getGameObject().hasThisAttribute("nobattle");
		for (java.util.Iterator _j14it886 = (gameObjects).iterator(); _j14it886.hasNext(); ) {
		  GameObject theTarget = (GameObject) _j14it886.next();
			spell.addTarget(combatFrame.getHostPrefs(),theTarget,ignorebattle);
			if (!ignorebattle) {
				combatFrame.makeWatchfulNatives(RealmComponent.getRealmComponent(theTarget),true);
			}
			CombatFrame.broadcastMessage(character.getGameObject().getName(),"Targets the "+theTarget.getNameWithNumber()+" with "+spell.getGameObject().getName());
		}
		if (!gameObjects.isEmpty()) {
			JOptionPane.showMessageDialog(combatFrame,"All valid targets are selected.",spell.getName(),JOptionPane.INFORMATION_MESSAGE);
		}
		if (clearingTargetType.contains("clearing")) {
			// Affects the clearing itself
			TileLocation loc = battleModel.getBattleLocation();
			
			spell.addTarget(combatFrame.getHostPrefs(),loc.tile.getGameObject(),true);
			spell.setExtraIdentifier(String.valueOf(loc.clearing.getNum()));
			
			JOptionPane.showMessageDialog(combatFrame,"The current clearing is selected.",spell.getName(),JOptionPane.INFORMATION_MESSAGE);
			CombatFrame.broadcastMessage(character.getGameObject().getName(),"Targets clearing "
					+loc.clearing.getNum()
					+" of the "+loc.tile.getGameObject().getName()+".");
		}
		return true;
	}
}