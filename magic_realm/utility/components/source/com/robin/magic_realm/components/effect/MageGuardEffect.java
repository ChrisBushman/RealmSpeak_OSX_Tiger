package com.robin.magic_realm.components.effect;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.ArmorChitComponent;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.utility.ArmorCreator;

public class MageGuardEffect implements ISpellEffect {

	public void apply(SpellEffectContext context) {
		int ARMOR_CHOICE = 8;

		RealmComponent cc = RealmComponent.getRealmComponent(context.Caster);

		//move staff to spell
		context.Spell.getGameObject().add(context.Target.getGameObject());

		//create mage guard
		ArmorCreator creator = new ArmorCreator("mageguard");
		GameObject guard = creator.createOrReuseArmor(context.Game.getGameData());
		creator.setupGameObject(guard, "Mage Guard", "staff", "H", "", 1, ARMOR_CHOICE);
		ArmorCreator.setupSide(guard, "intact", 0, "gray");
		ArmorCreator.setupSide(guard, "damaged", 0, "white");

		ArmorChitComponent armor = new ArmorChitComponent(guard);
		armor.setOwner(cc);
		armor.setActivated(true);

		context.Caster.add(guard);
	}

	public void unapply(SpellEffectContext context) {
		GameObject guard = null;
		for (java.util.Iterator _j14it2068 = (context.Caster.getHold()).iterator(); _j14it2068.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2068.next();
			if ("Mage Guard".equals(go.getName())) {
				guard = go;
				break;
			}
		}
		if (guard != null) context.Caster.remove(guard);
		context.Caster.add(context.Target.getGameObject());
	}

}
