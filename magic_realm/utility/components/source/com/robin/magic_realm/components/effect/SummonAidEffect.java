package com.robin.magic_realm.components.effect;

import java.util.ArrayList;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.attribute.RelationshipType;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.GameObjectFilter;
import com.robin.magic_realm.components.utility.SetupCardUtility;
import com.robin.magic_realm.components.utility.SpellUtility;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class SummonAidEffect implements ISpellEffect {

	@Override
	public void apply(SpellEffectContext context) {
		CharacterWrapper character = context.getCharacterTarget();

		if(character.getGold() < 1){
			context.Spell.expireSpell();
			return;
		}

		character.setGold(character.getGold() - 1);

		ArrayList<String> friends = character.getRelationshipList(Constants.GAME_RELATIONSHIP, RelationshipType.FRIENDLY);
		ArrayList<String> allies  = character.getRelationshipList(Constants.GAME_RELATIONSHIP, RelationshipType.ALLY);

		GameObjectFilter notdead = new GameObjectFilter() {
			public boolean test(GameObject go) {
				CharacterWrapper cw = new CharacterWrapper(go);
				return !cw.isDead() && cw.getCurrentTile() != null;
			}
		};

		GameObject buddy = SpellUtility.findNativeFromTheseGroups(allies, notdead, context.Game);
		if (buddy == null) {
			buddy = SpellUtility.findNativeFromTheseGroups(friends, notdead, context.Game);
		}

		if(buddy == null){
			context.Spell.cancelSpell();
			return;
		}

		context.Spell.getGameObject().setThisAttribute("SummonedNative", buddy.getStringId());
		SpellUtility.bringSummonToClearing(character, buddy, context.Spell, null);
	}

	@Override
	public void unapply(SpellEffectContext context) {
		long id = Long.parseLong(context.Spell.getGameObject().getThisAttribute("SummonedNative"));

		GameObject buddy = null;
		for (GameObject go : context.getGameData().getGameObjects()) {
			if (go.equalsId(id)) {
				buddy = go;
				break;
			}
		}
		if (buddy == null) return;

		String nativeGroup = buddy.getThisAttribute("native");
		CharacterWrapper cw = new CharacterWrapper(buddy);
		CharacterWrapper casterCharacter = new CharacterWrapper(context.Caster);

		if(cw.isDead()){
			casterCharacter.changeRelationship(Constants.GAME_RELATIONSHIP, nativeGroup, -1, false);
		}
		else {
			casterCharacter.removeHireling(buddy);
			SetupCardUtility.resetDenizen(buddy);
		}
	}

}
