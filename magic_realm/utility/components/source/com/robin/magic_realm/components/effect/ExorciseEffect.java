package com.robin.magic_realm.components.effect;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.CombatWrapper;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class ExorciseEffect implements ISpellEffect {

	public void apply(SpellEffectContext context) {
		CombatWrapper combat = context.getCombatTarget();
		
		if (context.Target.getGameObject().hasThisAttribute(Constants.MAGIC_PROTECTION_EXTENDED)) {
			System.out.println("No effect on target.");
			return;
		}
		if (context.Target.getGameObject().hasThisAttribute(Constants.DEMON)||context.Target.getGameObject().hasThisAttribute(Constants.IMP)||context.Target.getGameObject().hasThisAttribute(Constants.DEVIL)||context.Target.getGameObject().hasThisAttribute(Constants.VAMPIRE)||context.Target.getGameObject().hasThisAttribute(Constants.SUCCUBUS)) {
			combat.setKilledBy(context.Caster);
			combat.setKilledLength(new Integer(18));
			combat.setKilledSpeed(context.Spell.getAttackSpeed());
		}
		else if (context.Target.isDenizen()) {
			combat.setAffectedByExorcise(true);
		}
		else if (context.Target.isCharacter()) {
			CharacterWrapper targChar = new CharacterWrapper(context.Target.getGameObject());
			
			// Cancel Spellcasting (do NOT include this spell!!)
			GameObject castSpell = combat.getCastSpell();
			if (castSpell!=null && !castSpell.equals(context.Spell.getGameObject())) {
				SpellWrapper otherSpell = new SpellWrapper(castSpell);
				otherSpell.expireSpell();
			}
			
			// Cancel curses
			targChar.removeAllCurses();
			
			// Fatigue Color Chits
			for (java.util.Iterator _j14it2069 = (targChar.getColorChits()).iterator(); _j14it2069.hasNext(); ) {
			  com.robin.magic_realm.components.CharacterActionChitComponent chit = (com.robin.magic_realm.components.CharacterActionChitComponent) _j14it2069.next();
					chit.makeFatigued();
				}
		}
		else if (context.Target.isSpell()) {
			SpellWrapper otherSpell = new SpellWrapper(context.Target.getGameObject());
			otherSpell.expireSpell();
		}
		else if (context.Target.isTreasureLocation()) {
			for (java.util.Iterator _j14it2070 = (context.Target.getHold()).iterator(); _j14it2070.hasNext(); ) {
			  GameObject held = (GameObject) _j14it2070.next();
				if (held.hasThisAttribute(RealmComponent.SPELL)) {
					SpellWrapper spellWrapper = new SpellWrapper(held);
					if (spellWrapper.isAlive() && spellWrapper.getGameObject().hasThisAttribute(Constants.FREED_SPELL)) {
						spellWrapper.expireSpell();
					}
				}
			}
		}
		else {
			System.out.println("No effect on target.");
		}
	}

	public void unapply(SpellEffectContext context) {
	}

}
