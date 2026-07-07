package com.robin.magic_realm.components.effect;

import com.robin.magic_realm.components.utility.RealmLogging;
import com.robin.magic_realm.components.wrapper.SpellMasterWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class NullifyEffect implements ISpellEffect {

	public void apply(SpellEffectContext context) {
		if (context.Target.isCharacter()) {
			context.getCharacterTarget().nullifyCurses();
		}
		
		SpellMasterWrapper sm = SpellMasterWrapper.getSpellMaster(context.Spell.getGameObject().getGameData());
		for (java.util.Iterator _j14it2050 = (sm.getAffectingSpells(context.Target.getGameObject())).iterator(); _j14it2050.hasNext(); ) {
		  SpellWrapper spell = (SpellWrapper) _j14it2050.next();
			if (context.Spell.getGameObject().equals(spell.getGameObject())) continue;
			if (spell.isNullified()) continue;
			if (spell.isActive() && spell.hasAffectedTargets()) {
				spell.nullifySpell(false);
				RealmLogging.logMessage(context.getCharacterCaster().getName(),"Spell effect nullified "+spell.getName() + " bewitching "+context.Target+".");
			}
			else {
				spell.cancelSpell();
				RealmLogging.logMessage(context.getCharacterCaster().getName(),"Spell effect canceled "+spell.getName() + " (cast by "+spell.getCaster().getName()+", targeting " +context.Target+").");
			}
		}
	}

	public void unapply(SpellEffectContext context) {	
		if (context.Target.isCharacter()) {
			context.getCharacterTarget().restoreCurses();
		}
		
		SpellMasterWrapper sm = SpellMasterWrapper.getSpellMaster(context.Spell.getGameObject().getGameData());
		sm.restoreBewitchingNullifiedSpells(context.Target.getGameObject(),context.Spell);
	}
}
