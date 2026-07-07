package com.robin.magic_realm.RealmBattle.targeting;

import com.robin.magic_realm.RealmBattle.BattleModel;
import com.robin.magic_realm.RealmBattle.CombatFrame;
import com.robin.magic_realm.components.*;
import com.robin.magic_realm.components.attribute.ColorMagic;
import com.robin.magic_realm.components.attribute.ColorMod;
import com.robin.magic_realm.components.utility.SpellUtility;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class SpellTargetingColor extends SpellTargetingSingle {

	protected SpellTargetingColor(CombatFrame combatFrame, SpellWrapper spell) {
		super(combatFrame, spell);
	}

	public boolean populate(BattleModel battleModel,RealmComponent activeParticipant) {
		ColorMod colorMod = ColorMod.createColorMod(spell.getGameObject());
		
		// Character Chits
		for (java.util.Iterator _j14it836 = (battleModel.getAllParticipatingCharacters()).iterator(); _j14it836.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it836.next();
			CharacterWrapper character = new CharacterWrapper(rc.getGameObject());
			for (java.util.Iterator _j14it837 = (character.getColorChits()).iterator(); _j14it837.hasNext(); ) {
			  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it837.next();
				if (colorMod.willAffect(chit.getColorMagic())) {
					gameObjects.add(chit.getGameObject());
				}
			}
		}
		
		// Enchanted Tile
		if (battleModel.getBattleLocation().tile.isEnchanted()) {
			TileComponent tile = battleModel.getBattleLocation().tile;
			gameObjects.add(tile.getGameObject());
		}
		
		// Permanent sources (color_source)
		for (java.util.Iterator _j14it838 = (battleModel.getBattleLocation().clearing.getDeepClearingComponents()).iterator(); _j14it838.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it838.next();
			ColorMagic cm = SpellUtility.getColorMagicFor(rc);
			if (cm!=null && colorMod.willAffect(cm)) {
				gameObjects.add(rc.getGameObject());
			}
		}
		
		return true;
	}
}