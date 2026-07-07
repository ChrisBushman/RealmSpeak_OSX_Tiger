package com.robin.magic_realm.RealmBattle.targeting;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.StateChitComponent;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.swing.RealmComponentOptionChooser;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.SpellUtility;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingBewitched extends SpellTargetingSingle {
	
	private ArrayList possibleSecondaryTargets = new ArrayList();
	private BattleModel battleModel = null;
	
	public SpellTargetingBewitched(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}
	
	public boolean isAddableItem(RealmComponent item) {
		return (item.isWeapon() || item.isArmor() || item.isTreasure() || item.getGameObject().hasThisAttribute(Constants.BROOMSTICK));
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		this.battleModel = battleModel;
		for (java.util.Iterator _j14it785 = (combatFrame.findCanBeSeen(battleModel.getAllBattleParticipants(true),true,true)).iterator(); _j14it785.hasNext(); ) {
		  RealmComponent participant = (RealmComponent) _j14it785.next();
			ArrayList bewitchingSpells = SpellUtility.getBewitchingSpells(participant.getGameObject());
			if (!participant.hasMagicProtection() && !participant.hasMagicColorImmunity(spell)) {
				possibleSecondaryTargets.add(participant.getGameObject());
				if (bewitchingSpells!=null && !bewitchingSpells.isEmpty()) {
					gameObjects.add(participant.getGameObject());
				}
			}
			if (participant.isCharacter()) {
				CharacterWrapper character = new CharacterWrapper(participant.getGameObject());
				for (java.util.Iterator _j14it786 = (character.getInventory()).iterator(); _j14it786.hasNext(); ) {
				  GameObject go = (GameObject) _j14it786.next();
					RealmComponent itemRc = RealmComponent.getRealmComponent(go);
					if (isAddableItem(itemRc)) {
						possibleSecondaryTargets.add(go);
						ArrayList spells = SpellUtility.getBewitchingSpells(go);
						if (spells!=null && !spells.isEmpty()) {
							gameObjects.add(go);
						}
					}
					else if (go.hasThisAttribute(Constants.PHASE_CHIT) && !gameObjects.contains(participant.getGameObject())) {
						gameObjects.add(participant.getGameObject());
					}
				}
				for (java.util.Iterator _j14it787 = (character.getFlyChits()).iterator(); _j14it787.hasNext(); ) {
				  StateChitComponent chit = (StateChitComponent) _j14it787.next();
					if (chit.getGameObject().hasThisAttribute(Constants.BROOMSTICK)) {
						possibleSecondaryTargets.add(chit.getGameObject());
						ArrayList spells = SpellUtility.getBewitchingSpells(chit.getGameObject());
						if (spells!=null && !spells.isEmpty()) {
							gameObjects.add(chit.getGameObject());
						}
					}
				}
			} else if (participant.isMonster() || participant.isNative()) {				
				for (java.util.Iterator _j14it788 = (participant.getHold()).iterator(); _j14it788.hasNext(); ) {
				  GameObject held = (GameObject) _j14it788.next();
					if (held.hasThisAttribute(Constants.MONSTER_WEAPON)
							|| held.hasThisAttribute(Constants.SHIELD)
							|| held.hasThisAttribute(Constants.GIANT_CLUB)
							|| held.hasThisAttribute(Constants.GIANT_AXE)) {
						possibleSecondaryTargets.add(held);
						ArrayList spells = SpellUtility.getBewitchingSpells(held);
						if (spells!=null && !spells.isEmpty()) {
							gameObjects.add(held);
						}
					}
				}
			}
		}
		
		return true;
	}
	
	public void updateSecondaryTargetsAfterSelection(TileLocation battleLocation, RealmComponent theTarget) {
		ArrayList bewitchingSpells = SpellUtility.getBewitchingSpells(theTarget.getGameObject());
		RealmComponentOptionChooser spellChooser = new RealmComponentOptionChooser(combatFrame,"Select a spell for "+spell.getName()+":",false);
		for (java.util.Iterator _j14it789 = (bewitchingSpells).iterator(); _j14it789.hasNext(); ) {
		  SpellWrapper spell = (SpellWrapper) _j14it789.next();
			if (!spell.isCurse() && !spell.isMesmerize()) {
				spellChooser.addRealmComponent(RealmComponent.getRealmComponent(spell.getGameObject()));
			}
		}
		CharacterWrapper character = new CharacterWrapper(theTarget.getGameObject());
		for (java.util.Iterator _j14it790 = (character.getInventory()).iterator(); _j14it790.hasNext(); ) {
		  GameObject go = (GameObject) _j14it790.next();
			if (go.hasThisAttribute(Constants.PHASE_CHIT)) {
				GameObject spellGo = theTarget.getGameObject().getGameData().getGameObject(Long.valueOf(go.getThisAttribute(Constants.SPELL_ID)));
				spellChooser.addRealmComponent(RealmComponent.getRealmComponent(spellGo));
			}
		}
		RealmComponent selectedSpell = null;
		if (spellChooser.hasOptions()) {
			spellChooser.setVisible(true);
			selectedSpell = spellChooser.getFirstSelectedComponent();
			if (selectedSpell!=null) {
				spell.setExtraIdentifier(selectedSpell.getGameObject().getStringId());
			}
			else {
				return;
			}
		}
		else {
			JOptionPane.showMessageDialog(combatFrame,"No spell target.",spell.getName()+" : No spell target available.",JOptionPane.INFORMATION_MESSAGE);
		}
		
		SpellWrapper selectedSpellWrapper = new SpellWrapper(selectedSpell.getGameObject());
		RealmComponentOptionChooser secondaryTargetChooser = new RealmComponentOptionChooser(combatFrame,"Select secondary target for "+spell.getName()+":",false);
		
		if (selectedSpellWrapper.isAbsorbEssence() || SpellTargeting.targetingCharacterOrTile(selectedSpellWrapper.getGameObject().getThisAttribute("target"))) {
			for (java.util.Iterator _j14it791 = (possibleSecondaryTargets).iterator(); _j14it791.hasNext(); ) {
			  GameObject go = (GameObject) _j14it791.next();
				if (RealmComponent.getRealmComponent(go).isCharacter()) {
					secondaryTargetChooser.addRealmComponent(RealmComponent.getRealmComponent(go));
				}
			}
		}
		else {
			SpellTargeting spellTargeting = SpellTargeting.getTargeting(combatFrame,selectedSpellWrapper);
			if (spellTargeting instanceof SpellTargetingCaster) {
				spellTargeting = new SpellTargetingCharacter(combatFrame,spell,spell.getGameObject().hasThisAttribute("targetLightOnly"));
			}
			else if (spellTargeting instanceof SpellTargetingMyItem) {
				spellTargeting = new SpellTargetingItem(combatFrame,spell,true,true);
			}
			else if (spellTargeting instanceof SpellTargetingMyArmor) {
				spellTargeting = new SpellTargetingArmor(combatFrame,spell);
			}
			else if (spellTargeting instanceof SpellTargetingMyWeapon) {
				spellTargeting = new SpellTargetingWeapon(combatFrame,spell);
			}
			else if (spellTargeting instanceof SpellTargetingMyStaff) {
				spellTargeting = new SpellTargetingStaff(combatFrame,spell);
			}
			else if (spellTargeting instanceof SpellTargetingAttacker) {
				spellTargeting = new SpellTargetingIndividual(combatFrame,spell);
			}
			else if (spellTargeting instanceof SpellTargetingArtifact) {
				spellTargeting = new SpellTargetingOtherArtifact(combatFrame,spell);
			}
		
			if (spellTargeting == null) {
				JOptionPane.showMessageDialog(combatFrame,"No secondary target.",spell.getName()+" : No secondary target available.",JOptionPane.INFORMATION_MESSAGE);
			}
			spellTargeting.populate(battleModel, selectedSpell);
			for (java.util.Iterator _j14it792 = (possibleSecondaryTargets).iterator(); _j14it792.hasNext(); ) {
			  GameObject go = (GameObject) _j14it792.next();
				if (spellTargeting.getPossibleTargets().contains(go)) {
					secondaryTargetChooser.addRealmComponent(RealmComponent.getRealmComponent(go));
				}
			}
		}
		if (secondaryTargetChooser.hasOptions()) {
			secondaryTargetChooser.setVisible(true);
			RealmComponent secondaryTarget = secondaryTargetChooser.getFirstSelectedComponent();
			if (secondaryTarget!=null) {
				spell.setSecondaryTarget(secondaryTarget.getGameObject());
			}
		}
		else {
			JOptionPane.showMessageDialog(combatFrame,"No secondary target.",spell.getName()+" : No secondary target available.",JOptionPane.INFORMATION_MESSAGE);
		}
	}
}