package com.robin.magic_realm.components.store;

import java.util.ArrayList;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.general.swing.ButtonOptionDialog;
import com.robin.magic_realm.components.*;
import com.robin.magic_realm.components.attribute.Strength;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.RealmLogging;
import com.robin.magic_realm.components.utility.RealmUtility;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.HostPrefWrapper;

public class FightersGuild extends GuildStore {
	
	private static int FAME_PRICE = 60;
	
	private static String REST_SERVICE = "Rest all fatigued FIGHT/MOVE chits for 5 gold.";
	private static String REPAIR_SERVICE = "Repair all active armor for 10 gold.";
	private static String ADVANCEMENT_SERVICE = "Pay "+FAME_PRICE+" FAME to advance to next level.";
	public static String JOIN_GUILD_LOG_MESSAGE = "Fulfilled the join requirement for the Fighters Guild.";
	
	private ArrayList restableChits;
	private ArrayList repairableArmor;
	
	public FightersGuild(GuildChitComponent guild, CharacterWrapper character) {
		super(guild, character);
	}
	protected void setupGuildSpecific() {
		if (character.hasCurse(Constants.ASHES)) {
			reasonStoreNotAvailable = "The "+getTraderName()+" does not like your ASHES curse!";
			return;
		}
		if (character.hasCurse(Constants.DISGUST)) {
			reasonStoreNotAvailable = "The "+getTraderName()+" does not like your DISGUST curse!";
			return;
		}
		
		restableChits = new ArrayList();
		if (!character.hasCurse(Constants.WITHER)) {
			restableChits.addAll(character.getFatiguedChits());
		}
		
		repairableArmor = new ArrayList();
		for (java.util.Iterator _j14it2528 = (character.getActiveInventory()).iterator(); _j14it2528.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2528.next();
			RealmComponent rc = RealmComponent.getRealmComponent(go);
			if (rc.isArmor()) {
				repairableArmor.add((ArmorChitComponent)rc);
			}
		}
	}
	protected String doGuildService(JFrame frame,int level) {
		int gold = (int)character.getGold();
		int fame = (int)character.getFame();
		HostPrefWrapper hostPrefs = HostPrefWrapper.findHostPrefs(character.getGameData());
		
		ButtonOptionDialog chooser = new ButtonOptionDialog(frame,trader.getIcon(),"Which service?",getTraderName()+" Services",true);
		if (!hostPrefs.hasPref(Constants.GUILDS_NO_ADVANCEMENT_SERVICE)) {
			if (level<3) chooser.addSelectionObject(ADVANCEMENT_SERVICE,fame>=FAME_PRICE);
			updateButtonChooser(chooser,level);
		}
		if (level>=1) chooser.addSelectionObject(REST_SERVICE,(gold>=5) && !restableChits.isEmpty());
		if (level>=2) chooser.addSelectionObject(REPAIR_SERVICE,(gold>=10) && !repairableArmor.isEmpty());
		chooser.setVisible(true);
		
		String selected = (String)chooser.getSelectedObject();
		if (selected!=null) {
			boolean freeAdvancement = isFreeAdvancement(selected);
			if (REST_SERVICE.equals(selected)) {
				character.addGold(-5);
				for (java.util.Iterator _j14it2529 = (restableChits).iterator(); _j14it2529.hasNext(); ) {
				  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it2529.next();
					chit.makeActive();
				}
				return "Rested all MOVE/FIGHT chits.";
			}
			else if (REPAIR_SERVICE.equals(selected)) {
				character.addGold(-10);
				for (java.util.Iterator _j14it2530 = (repairableArmor).iterator(); _j14it2530.hasNext(); ) {
				  ArmorChitComponent armor = (ArmorChitComponent) _j14it2530.next();
					armor.setIntact(true);
				}
				return "Repaired all active armor.";
			}
			else if (freeAdvancement || ADVANCEMENT_SERVICE.equals(selected)) {
				if (!freeAdvancement) character.addFame(-FAME_PRICE);
				int newLevel = character.getCurrentGuildLevel()+1;
				character.setCurrentGuildLevel(newLevel);
				chooseFriendlinessGain(frame);
				if (newLevel!=3 && hostPrefs.hasPref(Constants.GUILDS_BENEFITS)) {
					applyGuildBenefit(frame,character,newLevel);
				}
				if (newLevel==3) {
					applyGuildBenefit3(frame,character);
				}
				return "Advanced to "+character.getCurrentGuildLevelName()+"!";
			}
		}
		
		return null;
	}
	public void applyGuildBenefit1(JFrame frame, CharacterWrapper character) {
		if (!character.getGameObject().hasThisAttribute(Constants.GUILD_BENEFIT+"_1")) {
			character.getGameObject().addThisAttributeListItem(Constants.EXTRA_ACTIONS,"R");
			character.getGameObject().setThisAttribute(Constants.GUILD_BENEFIT+"_1");
		}
	}
	public void unapplyGuildBenefit1(JFrame frame, CharacterWrapper character) {
		if (character.getGameObject().hasThisAttribute(Constants.GUILD_BENEFIT+"_1")) {
			character.getGameObject().removeThisAttributeListItem(Constants.EXTRA_ACTIONS,"R");
			character.getGameObject().removeThisAttribute(Constants.GUILD_BENEFIT+"_1");
		}
	}
	public void applyGuildBenefit2(JFrame frame, CharacterWrapper character) {
		if (!character.getGameObject().hasThisAttribute(Constants.GUILD_BENEFIT+"_2")) {
			character.getGameObject().setThisAttribute(Constants.ARMOR_PIERCING);
			character.getGameObject().setThisAttribute(Constants.GUILD_BENEFIT+"_2");
		}
	}
	public void unapplyGuildBenefit2(JFrame frame, CharacterWrapper character) {
		if (character.getGameObject().hasThisAttribute(Constants.GUILD_BENEFIT+"_2")) {
			character.getGameObject().removeThisAttribute(Constants.ARMOR_PIERCING);
			character.getGameObject().removeThisAttribute(Constants.GUILD_BENEFIT+"_2");
		}
	}
	public void applyGuildBenefit3(JFrame frame, CharacterWrapper character) {
		if (!character.getGameObject().hasThisAttribute(Constants.GUILD_BENEFIT+"_3")) {
			HostPrefWrapper hostPrefs = HostPrefWrapper.findHostPrefs(character.getGameData());
			if (hostPrefs.hasPref(Constants.GUILDS_FINAL_BENEFIT)) {
				for (java.util.Iterator _j14it2531 = (RealmUtility.getLivingCharacters(character.getGameData())).iterator(); _j14it2531.hasNext(); ) {
				  GameObject livingCharacter = (GameObject) _j14it2531.next();
					String guildLivingCharacter = new CharacterWrapper(livingCharacter).getCurrentGuild();
					if (guildLivingCharacter!=null && guildLivingCharacter.matches(FIGHTERS_GUILD)) {
						if (livingCharacter.getId()!=character.getGameObject().getId() && (livingCharacter.hasThisAttribute(Constants.GUILD_BENEFIT+"_3") || livingCharacter.hasThisAttribute(Constants.GUILD_BENEFIT_SUCESSOR))) {
							return;
						}
					}
				}
			}
			character.getGameObject().setThisAttribute(Constants.GUILD_BENEFIT+"_3");
			GameObject go = getNewCharacterChit();
			Strength vul = new Strength(character.getGameObject().getThisAttribute("vulnerability"));
			if (!vul.isTremendous()) {
				vul = vul.addStrength(1);
			}
			go.setThisAttribute("action","fight");
			go.setThisAttribute("speed","3");
			go.setThisAttribute("strength",vul.toString());
			go.setThisAttribute("effort","2");
			go.setName(character.getCharacterLevelName(4)+" FIGHT "+vul.toString()+"3**");
			go.setThisAttribute(Constants.GUILD_BENEFIT+"_3");
			RealmLogging.logMessage(character.getGameObject().getName(),"Gained a "+go.getName()+" chit.");
		}
	}
	public void unapplyGuildBenefit3(JFrame frame, CharacterWrapper character) {
		character.getGameObject().removeThisAttribute(Constants.GUILD_BENEFIT+"_3");
		for (java.util.Iterator _j14it2532 = (character.getAllChits()).iterator(); _j14it2532.hasNext(); ) {
		  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it2532.next();
			if (chit.getGameObject().hasThisAttribute(Constants.GUILD_BENEFIT+"_3")) {
				character.getGameObject().remove(chit.getGameObject());
				chit.getGameObject().clearAllAttributes();
			}
		}
	}
	
	public boolean validateRequirementAndJoin(CharacterWrapper character, RealmComponent victim, boolean spellUsed) {
		if (spellUsed) return false;
		HostPrefWrapper hostPrefs = HostPrefWrapper.findHostPrefs(character.getGameData());
		if (character.hasGuildJoinRequirement() && victim.isMonster() && ((MonsterChitComponent)victim).getVulnerability().equalTo(new Strength("H"))) {
			character.setGuildJoinRequirement(false);
			if (hostPrefs.hasPref(Constants.GUILDS_START_LEVEL)) {
				character.setCurrentGuildLevel(0);
			}
			else {
				character.setCurrentGuildLevel(1);
				if (hostPrefs.hasPref(Constants.GUILDS_BENEFITS)) {
					character.getCurrentGuildStore().applyGuildBenefit1(null, character);
				}
			}
			return true;
		}
		return false;
	}
}