package com.robin.magic_realm.components.effect;
import javax.swing.JFrame;

import com.robin.magic_realm.components.ClearingDetail;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.utility.RealmLogging;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class ViolentWindsEffect implements ISpellEffect {	
	
	public void apply(SpellEffectContext context) {
		ClearingDetail clearing = context.getClearingTarget();
		if(!clearing.isAffectedByViolentWinds()){
			clearing.setAffectedByViolentWinds(true);
		}
		else{
			context.Spell.cancelSpell();
			RealmLogging.logMessage(context.Spell.getCaster().getGameObject().getName(),"Spell cancelled, because the targeted clearing already affected by Violent Winds.");
			return;
		}
		
		for (java.util.Iterator _j14it2038 = (clearing.getTileLocation().tile.getRealmComponentsBetweenClearing(clearing.getNum())).iterator(); _j14it2038.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it2038.next();
			if (!rc.isCharacter()) continue;
			CharacterWrapper character = new CharacterWrapper(rc.getGameObject());
			if (character.getCurrentLocation().isFlying()) {
				character.moveToLocation(new JFrame(), clearing.getTileLocation());
			}
		}
		
		moveFlyingCharactersBackToClearings(clearing);
	}

	public void unapply(SpellEffectContext context) {
		ClearingDetail clearing = context.getClearingTarget();
		if(clearing.isAffectedByViolentWinds()){
			clearing.setAffectedByViolentWinds(false);
		}
	}

	private static void moveFlyingCharactersBackToClearings(ClearingDetail clearing) {
		for (java.util.Iterator _j14it2039 = (clearing.getTileLocation().tile.getRealmComponentsBetweenClearing(clearing.getNum())).iterator(); _j14it2039.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it2039.next();
			if (!rc.isCharacter()) continue;
			CharacterWrapper character = new CharacterWrapper(rc.getGameObject());
			if (character.getRunAwayLastUsedChit().matches("FLY")) {
				character.moveToLocation(new JFrame(), clearing.getTileLocation());
			}
		}
	}	
}
