package com.robin.magic_realm.components.effect;
import com.robin.magic_realm.components.TileComponent;
import com.robin.magic_realm.components.utility.Constants;

public class FrozenWaterEffect implements ISpellEffect {	
	
	public void apply(SpellEffectContext context) {
		TileComponent targetTile = context.getTileTarget();
		if(!targetTile.getGameObject().hasThisAttribute(Constants.FROZEN_WATER)){
			targetTile.getGameObject().setThisAttribute(Constants.FROZEN_WATER);
		}
	}

	public void unapply(SpellEffectContext context) {
		TileComponent targetTile = context.getTileTarget();
		if(targetTile.getGameObject().hasThisAttribute(Constants.FROZEN_WATER)){
			targetTile.getGameObject().removeThisAttribute(Constants.FROZEN_WATER);
		}
	}

}
