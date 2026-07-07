package com.robin.magic_realm.components.effect;

import com.robin.magic_realm.components.ClearingDetail;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class RemovePotentialBlocksEffect implements ISpellEffect {
	
	public RemovePotentialBlocksEffect(){
	}
	
	public void apply(SpellEffectContext context) {
		ClearingDetail cl = context.Target.getCurrentLocation().clearing;
		if (cl==null) return;
		for (java.util.Iterator _j14it2062 = (cl.getClearingComponents()).iterator(); _j14it2062.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it2062.next();
			if (!rc.isAnyLeader()) continue;
			CharacterWrapper leader = new CharacterWrapper(rc.getGameObject());
			if (leader.hasBlockDecision(context.Target.getGameObject())) {
				leader.removeBlockDecision(context.Target.getGameObject());
			}
		}
	}

	public void unapply(SpellEffectContext context) {
	}

}
