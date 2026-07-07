package com.robin.magic_realm.components.effect;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.table.Loot;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.DieRollReporter;
import com.robin.magic_realm.components.utility.RollResult;
import com.robin.magic_realm.components.utility.SpellUtility;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class FilcherEffect implements ISpellEffect {
	private boolean oneTime;

	public void apply(SpellEffectContext context) {
		if(oneTime)return;

		String nativeGroup = context.Target.getGameObject().getThisAttribute("native");
		String dwellingName = context.Target.getGameObject().getThisAttribute("setup_start");

		CharacterWrapper cc = new CharacterWrapper(context.Caster);
		String msg;

		RollResult result = SpellUtility.rollResult(context, "Filcher");

		switch(result.roll){
			case 1:
			case 2:
			case 3:
			case 4:
				stealFromDwelling(context, nativeGroup, dwellingName, cc, result, false);
				break;
			case 5:
				cc.changeRelationship(Constants.GAME_RELATIONSHIP, nativeGroup, -1, false);
				stealFromDwelling(context, nativeGroup, dwellingName, cc, result, true);
				break;
			case 6:
				cc.changeRelationship(Constants.GAME_RELATIONSHIP, nativeGroup, 0, true);
				msg = "You are caught red-handed by the " + nativeGroup + " and they are now your enemy!";
				DieRollReporter.showMessageDialog(result.roller, context.Parent, "Filcher", msg, JOptionPane.INFORMATION_MESSAGE);
				for (java.util.Iterator _j14it2030 = (context.Spell.getTargets()).iterator(); _j14it2030.hasNext(); ) {
				  RealmComponent n = (RealmComponent) _j14it2030.next();
					cc.addBattlingNative(n.getGameObject());
				}
				break;
		}

		oneTime = true;
	}

	private static void stealFromDwelling(SpellEffectContext context, String nativeGroup, String dwellingName, CharacterWrapper cc, RollResult result, boolean suspicious) {
		String msg;
		GameObject dwelling = context.Game.getGameData().getGameObjectByName(dwellingName);

		ArrayList stuff = new ArrayList();
		for (java.util.Iterator _j14it2031 = (dwelling.getHold()).iterator(); _j14it2031.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2031.next();
			RealmComponent rc = RealmComponent.getRealmComponent(go);
			if (rc.isItem() && !rc.isHorse()) {
				stuff.add(go);
			}
		}

		RollResult stealRoll = SpellUtility.rollResult(context, "Steal");
		int stealIndex = stealRoll.roll - 1;
		GameObject stolenItem = (stealIndex < stuff.size()) ? (GameObject) stuff.get(stealIndex) : null;

		String suspiciousMsg = suspicious ? ", but they become suspicious" : "";

		if(stolenItem != null){
			msg = "You stole the " + stolenItem.getName() + " from the " + nativeGroup + suspiciousMsg + ".";
			Loot.addItemToCharacter(context.Parent, null, cc, stolenItem);
		} else {
			msg = "You stole 5 gold from the " + nativeGroup + suspiciousMsg + ".";
			cc.addGold(5);
		}

		DieRollReporter.showMessageDialog(result.roller, context.Parent, "Filcher", msg, JOptionPane.INFORMATION_MESSAGE);
	}

	public void unapply(SpellEffectContext context) {
	}
}
