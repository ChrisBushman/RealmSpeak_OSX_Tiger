package com.robin.magic_realm.RealmBattle.targeting;

import java.util.ArrayList;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.TileComponent;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.utility.RealmObjectMaster;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingIndividualPlusHex extends SpellTargetingIndividual {
	public SpellTargetingIndividualPlusHex(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}
	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		super.populate(battleModel,activeParticipant);
		
		secondaryTargetChoiceString = "Select a tile to FLY the target to:";
		TileLocation here = battleModel.getBattleLocation();
		
		ArrayList adjTiles = new ArrayList();
		for (java.util.Iterator _j14it822 = (here.tile.getAllAdjacentTiles()).iterator(); _j14it822.hasNext(); ) {
		  TileComponent tile = (TileComponent) _j14it822.next();
			adjTiles.add(tile.getGameObject());
		}
		
		if (adjTiles.isEmpty()) { // this only happens during battle simulator
			RealmObjectMaster rom = RealmObjectMaster.getRealmObjectMaster(battleModel.getGameData());
			adjTiles.addAll(rom.getTileObjects());
		}
		
		for (java.util.Iterator _j14it823 = (gameObjects).iterator(); _j14it823.hasNext(); ) {
		  GameObject go = (GameObject) _j14it823.next();
			if (!RealmComponent.getRealmComponent(go).hasMagicProtection() && !RealmComponent.getRealmComponent(go).hasMagicColorImmunity(spell)) {
				identifiers.add(go.getName());
				secondaryTargets.put(go.getName(),adjTiles);
			}
		}
		return true;
	}
}