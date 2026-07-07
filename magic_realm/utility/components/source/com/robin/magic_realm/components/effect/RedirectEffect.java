package com.robin.magic_realm.components.effect;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.utility.SpellUtility;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.GameWrapper;
import com.robin.magic_realm.components.wrapper.HostPrefWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class RedirectEffect implements ISpellEffect {

	public void apply(SpellEffectContext context) {
		RealmComponent primaryTarget = (RealmComponent) context.Spell.getTargets().get(0);
		GameObject secondaryTarget = context.Spell.getSecondaryTarget();
		String spellId = context.Spell.getExtraIdentifier();
		GameObject spell = context.getGameData().getGameObject(spellId);
		SpellWrapper spellWrapper = new SpellWrapper(spell);
		if (spell!=null && primaryTarget!=null && secondaryTarget!=null) {
			ArrayList bewitchingSpellsOnSecondaryTarget = SpellUtility.getBewitchingSpells(secondaryTarget);
			for (java.util.Iterator _j14it2052 = (bewitchingSpellsOnSecondaryTarget).iterator(); _j14it2052.hasNext(); ) {
			  SpellWrapper bewitchingSpell = (SpellWrapper) _j14it2052.next();
				if(bewitchingSpell.getGameObject().getName().matches(spell.getName())) {
					JOptionPane.showMessageDialog(context.Parent,"Redirect: Target already bewichted by the same spell",secondaryTarget.getNameWithNumber()+" already bewitched by "+spell.getName()+". Cannot redirect the spell.",JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			}
			
			HostPrefWrapper hostPrefs = HostPrefWrapper.findHostPrefs(context.getGameData());
			if (spellWrapper.isAbsorbEssence()) {
				spellWrapper.unaffectTargets();
				spellWrapper.setCaster(new CharacterWrapper(secondaryTarget));
				spellWrapper.affectTargets(new JFrame(),GameWrapper.findGame(context.getGameData()),false,null);
				return;
			}
			
			spellWrapper.unaffectTargets();
			spellWrapper.removeTarget(primaryTarget.getGameObject());
			spellWrapper.addTarget(hostPrefs, secondaryTarget);
			spellWrapper.affectTargets(new JFrame(),GameWrapper.findGame(context.getGameData()),false,null);
		}
	}

	public void unapply(SpellEffectContext context) {
	}

}
