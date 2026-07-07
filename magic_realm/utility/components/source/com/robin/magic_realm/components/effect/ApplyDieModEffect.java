package com.robin.magic_realm.components.effect;

import java.util.ArrayList;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.utility.Constants;

public class ApplyDieModEffect implements ISpellEffect {
	String _dieModString;
	
	public ApplyDieModEffect(){
	}
	
	public void apply(SpellEffectContext context) {
		ArrayList dieMods = getDieMods(context);
		for (java.util.Iterator _j14it2035 = (dieMods).iterator(); _j14it2035.hasNext(); ) {
		  String dieMod = (String) _j14it2035.next();
			context.Target.getGameObject().addThisAttributeListItem(Constants.DIEMOD,dieMod);
		}
	}

	public void unapply(SpellEffectContext context) {
		ArrayList dieMods = getDieMods(context);
		for (java.util.Iterator _j14it2036 = (dieMods).iterator(); _j14it2036.hasNext(); ) {
		  String dieMod = (String) _j14it2036.next();
			if(context.Target.getGameObject().hasThisAttributeListItem(Constants.DIEMOD,dieMod)) {
				context.Target.getGameObject().removeThisAttributeListItem(Constants.DIEMOD,dieMod);
			}
		}
	}

	private static ArrayList getDieMods(SpellEffectContext context) {
		GameObject spell = context.Spell.getGameObject();
		if (spell.hasAttributeBlock(Constants.DIEMOD)) {
			return spell.getAttributeList(Constants.DIEMOD,Constants.DIEMOD);
		}
		return new ArrayList();
	}
}
