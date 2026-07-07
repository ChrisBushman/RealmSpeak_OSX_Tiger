package com.robin.magic_realm.RealmBattle.targeting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.RealmBattle.BattleGroup;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.RealmBattle.CombatSheet;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.swing.RealmComponentOptionChooser;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.CombatWrapper;
import com.robin.magic_realm.components.wrapper.HostPrefWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public abstract class SpellTargetingSingle extends SpellTargeting {
	
	protected ArrayList identifiers = new ArrayList();
	protected Hashtable secondaryTargets = new Hashtable(); // A hash of lists by identifier
	protected String secondaryTargetChoiceString = "";

	protected SpellTargetingSingle(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}
	public boolean hasTargets() {
		return !(gameObjects.isEmpty() && secondaryTargets.isEmpty());
	}
	public boolean assign(HostPrefWrapper hostPrefs, CharacterWrapper activeCharacter) {
		RealmComponentOptionChooser chooser = new RealmComponentOptionChooser(combatFrame,"Select a Target for "+spell.getName()+":",true);
		if (identifiers.isEmpty()) {
			if (gameObjects.isEmpty()) {
				ArrayList list = new ArrayList(secondaryTargets.keySet());
				Collections.sort(list);
				chooser.addStrings(list);
			}
			else {
				// Rather than just dump them in, there should be a small icon indicating which sheet they are on
				Hashtable hash = new Hashtable();
				ArrayList combatSheets = combatFrame.getAllCombatSheets();
				for (java.util.Iterator _j14it806 = (combatSheets).iterator(); _j14it806.hasNext(); ) {
				  CombatSheet sheet = (CombatSheet) _j14it806.next();
					RealmComponent aSheetOwner = sheet.getSheetOwner();
					Collection c = sheet.getAllParticipantsOnSheet();
					for (java.util.Iterator _j14it807 = (c).iterator(); _j14it807.hasNext(); ) {
					  RealmComponent sp = (RealmComponent) _j14it807.next();
						hash.put(sp,aSheetOwner);
					}
				}
				
				for (java.util.Iterator _j14it808 = (gameObjects).iterator(); _j14it808.hasNext(); ) {
				  GameObject gameObject = (GameObject) _j14it808.next();
					RealmComponent rc = RealmComponent.getRealmComponent(gameObject);
					RealmComponent aSheetOwner = (RealmComponent) hash.get(rc);
					String option = chooser.generateOption();
					if (aSheetOwner!=null) {
						chooser.addRealmComponentToOption(option,aSheetOwner,RealmComponentOptionChooser.DisplayOption.MediumIcon);
					}
					if (rc.isSpell()) {
						updateChooserWithContent(chooser,option,rc);
					}
					chooser.addRealmComponentToOption(option,rc);
				}
			}
		}
		else {
			for (int i=0;i<identifiers.size();i++) {
				String identifier = (String) identifiers.get(i);
				GameObject pick = (GameObject) gameObjects.get(i);
				RealmComponent rc = RealmComponent.getRealmComponent(pick);
				String option = identifier+i;
				chooser.addOption(option,identifier);
				if (rc.isSpell()) {
					updateChooserWithContent(chooser,option,rc);
				}
				chooser.addRealmComponentToOption(option,rc);
			}
		}
		chooser.setVisible(true);
		String selText = chooser.getSelectedText();
		if (selText!=null) {
			RealmComponent theTarget = chooser.getLastSelectedComponent();
			
			if (hostPrefs.hasPref(Constants.SR_ADV_PROTECTED_LEADERS_TARGETING)) {
				if (theTarget.isNativeLeader() && !theTarget.isHiredOrControlled() && !theTarget.getGameObject().hasThisAttribute(Constants.DEAD)) {
					CombatWrapper combatWrapperTarget = new CombatWrapper(theTarget.getGameObject());
					if (combatWrapperTarget.getKilledBy()==null) {
						String groupName = theTarget.getGameObject().getThisAttribute(RealmComponent.NATIVE).toLowerCase();
						BattleGroup group = combatFrame.getBattleModel().getDenizenBattleGroup();
						for (java.util.Iterator _j14it809 = (group.getBattleParticipants()).iterator(); _j14it809.hasNext(); ) {
						  RealmComponent member = (RealmComponent) _j14it809.next();
							if (!member.isNativeLeader() && member.isNative() && member.getGameObject().getThisAttribute(RealmComponent.NATIVE).toLowerCase().matches(groupName) && !member.isHiredOrControlled()) {
								CombatWrapper combatWrapper = new CombatWrapper(member.getGameObject());
								if (combatWrapper.getAttackerCount()==0) {
									JOptionPane.showMessageDialog(combatFrame,"Cannot attack Native Leader unless all other unhired natives of the same group are also targeted.","Protected Leader",JOptionPane.INFORMATION_MESSAGE);
									return false;
								}
							}
						}
					}
				}
			}
			
			updateSecondaryTargetsAfterSelection(activeCharacter.getCurrentLocation(), theTarget);
			if (theTarget==null) {
				CombatFrame.broadcastMessage(activeCharacter.getGameObject().getName(),"Targets the "+selText+" with "+spell.getGameObject().getName());
				ArrayList list = (ArrayList) secondaryTargets.get(selText);
				for (java.util.Iterator _j14it810 = (list).iterator(); _j14it810.hasNext(); ) {
				  RealmComponent rc = (RealmComponent) _j14it810.next();
					spell.addTarget(hostPrefs,rc.getGameObject());
					combatFrame.makeWatchfulNatives(rc,true);
				}
			}
			else {
				spell.addTarget(hostPrefs,theTarget.getGameObject());
				combatFrame.makeWatchfulNatives(theTarget,true);
				String message = "Targets the "+theTarget.getGameObject().getNameWithNumber();
				if (selText != "") {
					message = message + " ("+selText+")";
				}
				message = message + " with "+spell.getGameObject().getName();
				CombatFrame.broadcastMessage(activeCharacter.getGameObject().getName(),message);
				if (selText.trim().length()>0) {
					spell.setExtraIdentifier(selText);
				}
				if (!secondaryTargets.isEmpty()) {
					chooser = new RealmComponentOptionChooser(combatFrame,secondaryTargetChoiceString,false);
					Hashtable hash = new Hashtable();
					ArrayList list = (ArrayList) secondaryTargets.get(selText);
					if (list.isEmpty()) {
						return true;
					}
					for (java.util.Iterator _j14it811 = (list).iterator(); _j14it811.hasNext(); ) {
					  GameObject st = (GameObject) _j14it811.next();
						String name = st.getName();
						chooser.addOption(name,name);
						hash.put(name,st);
					}
					chooser.setVisible(true);
					selText = chooser.getSelectedText();
					if (selText!=null) {
						GameObject st = (GameObject) hash.get(selText);
						spell.setSecondaryTarget(st);
					} // shouldn't ever be null with no cancel button!
				}
			}
			return true;
		}
		return false;
	}
	private static void updateChooserWithContent(RealmComponentOptionChooser chooser,String option,RealmComponent rc) {
		for (java.util.Iterator _j14it812 = (rc.getGameObject().getHold()).iterator(); _j14it812.hasNext(); ) {
		  GameObject hgo = (GameObject) _j14it812.next();
			chooser.addGameObjectToOption(option,hgo);
		}
	}
	public void updateSecondaryTargetsAfterSelection(TileLocation battleLocation, RealmComponent theTarget) {
		// can be overwritten
	}
}