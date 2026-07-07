package com.robin.magic_realm.components.store;

import java.util.ArrayList;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.general.swing.ButtonOptionDialog;
import com.robin.magic_realm.components.*;
import com.robin.magic_realm.components.attribute.Strength;
import com.robin.magic_realm.components.swing.RealmComponentOptionChooser;
import com.robin.magic_realm.components.utility.*;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.HostPrefWrapper;

public class ThievesGuild extends GuildStore {
	
	private static int GOLD_PRICE = 100;
	
	private static String MAP_SERVICE_1 = "Learn all hidden paths on a tile (both sides) for 5 gold.";
	private static String MAP_SERVICE_2 = "Learn all secret passages on a tile (both sides) for 10 gold.";
	private static String UNLOCK_SERVICE = "Unlock the Chest for 50 gold.";
	private static String ADVANCEMENT_SERVICE = "Pay "+GOLD_PRICE+" gold to advance to next level.";
	public static String JOIN_GUILD_TEXT = "You have fulfilled the join requirement for the Thieves Guild. You are now a member of the Thieves Guild.";
	public static String JOIN_GUILD_TITLE = "Joining Thieves Guild";
	
	private ArrayList tilesWithUnknownPaths;
	private ArrayList tilesWithUnknownPassages;
	private ArrayList openable;

	public ThievesGuild(GuildChitComponent guild, CharacterWrapper character) {
		super(guild, character);
	}
	protected void setupGuildSpecific() {
		if (character.hasCurse(Constants.ASHES)) {
			reasonStoreNotAvailable = "The "+getTraderName()+" does not like your ASHES curse!";
			return;
		}
		
		tilesWithUnknownPaths = new ArrayList();
		tilesWithUnknownPassages = new ArrayList();
		RealmObjectMaster rom = RealmObjectMaster.getRealmObjectMaster(character.getGameData());
		for (java.util.Iterator _j14it2547 = (rom.getTileObjects()).iterator(); _j14it2547.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2547.next();
			TileComponent tile = (TileComponent)RealmComponent.getRealmComponent(go);
			if (pathsToDiscover(tile.getHiddenPaths(true)).size()>0) {
				tilesWithUnknownPaths.add(tile);
			}
			if (pathsToDiscover(tile.getSecretPassages(true)).size()>0) {
				tilesWithUnknownPassages.add(tile);
			}
		}
		
		openable = new ArrayList();
		for (java.util.Iterator _j14it2548 = (character.getInventory()).iterator(); _j14it2548.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2548.next();
			if (go.getName().startsWith("Chest") && go.hasThisAttribute(Constants.NEEDS_OPEN)) {
				openable.add(go);
			}
		}
	}
	protected ArrayList pathsToDiscover(ArrayList paths) {
		ArrayList toDiscover = new ArrayList();
		for (java.util.Iterator _j14it2549 = (paths).iterator(); _j14it2549.hasNext(); ) {
		  PathDetail path = (PathDetail) _j14it2549.next();
			String pathKey = path.getFullPathKey();
			if ((path.isHidden() && !character.hasHiddenPathDiscovery(pathKey))
					|| (path.isSecret() && !character.hasSecretPassageDiscovery(pathKey))) {
				toDiscover.add(pathKey);
			}
		}
		return toDiscover;
	}
	private String revealHidden(JFrame frame,ArrayList list) {
		return reveal(frame,list,true);
	}
	private String revealSecret(JFrame frame,ArrayList list) {
		return reveal(frame,list,false);
	}
	private String reveal(JFrame frame,ArrayList list,boolean hidden) {
		RealmComponentOptionChooser chooser = new RealmComponentOptionChooser(frame,"Which tile to reveal "+(hidden?"hidden paths":"secret passages")+"?",true);
		for (java.util.Iterator _j14it2550 = (list).iterator(); _j14it2550.hasNext(); ) {
		  TileComponent tile = (TileComponent) _j14it2550.next();
			chooser.addRealmComponent(tile,true);
		}
		chooser.setVisible(true);
		if (chooser.getSelectedText()!=null) {
			TileComponent tile = (TileComponent)chooser.getFirstSelectedComponent();
			reveal(tile,hidden);
			return tile.getGameObject().getName();
		}
		return null;
	}
	private void reveal(TileComponent tile,boolean hidden) {
		if (hidden) {
			for (java.util.Iterator _j14it2551 = (pathsToDiscover(tile.getHiddenPaths(true))).iterator(); _j14it2551.hasNext(); ) {
			  String pathKey = (String) _j14it2551.next();
				if (!character.hasHiddenPathDiscovery(pathKey)) {
					character.addHiddenPathDiscovery(pathKey);
				}
			}
		}
		else {
			for (java.util.Iterator _j14it2552 = (pathsToDiscover(tile.getSecretPassages(true))).iterator(); _j14it2552.hasNext(); ) {
			  String pathKey = (String) _j14it2552.next();
				if (!character.hasSecretPassageDiscovery(pathKey)) {
					character.addSecretPassageDiscovery(pathKey);
				}
			}
		}
	}
	protected String doGuildService(JFrame frame,int level) {
		int gold = (int)character.getGold();
		HostPrefWrapper hostPrefs = HostPrefWrapper.findHostPrefs(character.getGameData());
		ButtonOptionDialog chooser = new ButtonOptionDialog(frame,trader.getIcon(),"Which service?",getTraderName()+" Services",true);
		if (!hostPrefs.hasPref(Constants.GUILDS_NO_ADVANCEMENT_SERVICE)) {
			if (level<3) chooser.addSelectionObject(ADVANCEMENT_SERVICE,gold>=GOLD_PRICE);
			updateButtonChooser(chooser,level);
		}
		if (level>=1) chooser.addSelectionObject(MAP_SERVICE_1,(gold>=5) && !tilesWithUnknownPaths.isEmpty());
		if (level>=2) chooser.addSelectionObject(MAP_SERVICE_2,(gold>=10) && !tilesWithUnknownPassages.isEmpty());
		if (level==3) chooser.addSelectionObject(UNLOCK_SERVICE,(gold>=50) && !openable.isEmpty());
		chooser.setVisible(true);
		
		String selected = (String)chooser.getSelectedObject();
		if (selected!=null) {
			boolean freeAdvancement = isFreeAdvancement(selected);
			if (MAP_SERVICE_1.equals(selected)) {
				String tileName = revealHidden(frame,tilesWithUnknownPaths);
				if (tileName!=null) {
					character.addGold(-5);
					return "Learned all hidden paths in the "+tileName;
				}
			}
			else if (MAP_SERVICE_2.equals(selected)) {
				String tileName = revealSecret(frame,tilesWithUnknownPassages);
				if (tileName!=null) {
					character.addGold(-10);
					return "Learned all secret passages in the "+tileName;
				}
			}
			else if (UNLOCK_SERVICE.equals(selected)) {
				character.addGold(-50);
				GameObject opened = TreasureUtility.openOneObject(frame,character,openable,null,true);
				return "Opened the "+opened.getName();
			}
			else if (freeAdvancement || ADVANCEMENT_SERVICE.equals(selected)) {
				if (!freeAdvancement) character.addGold(-GOLD_PRICE);
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
			character.getGameObject().addThisAttributeListItem(Constants.EXTRA_ACTIONS,"H");
			character.getGameObject().setThisAttribute(Constants.GUILD_BENEFIT+"_1");
		}
	}
	public void unapplyGuildBenefit1(JFrame frame, CharacterWrapper character) {
		if (character.getGameObject().hasThisAttribute(Constants.GUILD_BENEFIT+"_1")) {
			character.getGameObject().removeThisAttributeListItem(Constants.EXTRA_ACTIONS,"H");
			character.getGameObject().removeThisAttribute(Constants.GUILD_BENEFIT+"_1");
		}
	}
	public void applyGuildBenefit2(JFrame frame, CharacterWrapper character) {
		if (!character.getGameObject().hasThisAttribute(Constants.GUILD_BENEFIT+"_2")) {
			character.getGameObject().addThisAttributeListItem(Constants.DIEMOD,"-1:loot:all");
			character.getGameObject().setThisAttribute(Constants.GUILD_BENEFIT+"_2");
		}
	}
	public void unapplyGuildBenefit2(JFrame frame, CharacterWrapper character) {
		if (character.getGameObject().hasThisAttribute(Constants.GUILD_BENEFIT+"_2")) {
			character.getGameObject().removeThisAttribute(Constants.GUILD_BENEFIT+"_2");
		}
	}
	public void applyGuildBenefit3(JFrame frame, CharacterWrapper character) {
		if (!character.getGameObject().hasThisAttribute(Constants.GUILD_BENEFIT+"_3")) {
			HostPrefWrapper hostPrefs = HostPrefWrapper.findHostPrefs(character.getGameData());
			if (hostPrefs.hasPref(Constants.GUILDS_FINAL_BENEFIT)) {
				for (java.util.Iterator _j14it2553 = (RealmUtility.getLivingCharacters(character.getGameData())).iterator(); _j14it2553.hasNext(); ) {
				  GameObject livingCharacter = (GameObject) _j14it2553.next();
					String guildLivingCharacter = new CharacterWrapper(livingCharacter).getCurrentGuild();
					if (guildLivingCharacter!=null && guildLivingCharacter.matches(THIEVES_GUILD)) {
						if (livingCharacter.getId()!=character.getGameObject().getId() &&  (livingCharacter.hasThisAttribute(Constants.GUILD_BENEFIT+"_3") || livingCharacter.hasThisAttribute(Constants.GUILD_BENEFIT_SUCESSOR))) {
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
			go.setThisAttribute("action","move");
			go.setThisAttribute("speed","2");
			go.setThisAttribute("strength",vul.toString());
			go.setThisAttribute("effort","2");
			go.setName(character.getCharacterLevelName(4)+" MOVE "+vul.toString()+"2**");
			go.setThisAttribute(Constants.GUILD_BENEFIT+"_3");
			RealmLogging.logMessage(character.getGameObject().getName(),"Gained a "+go.getName()+" chit.");
		}
	}
	public void unapplyGuildBenefit3(JFrame frame, CharacterWrapper character) {
		character.getGameObject().removeThisAttribute(Constants.GUILD_BENEFIT+"_3");
		for (java.util.Iterator _j14it2554 = (character.getAllChits()).iterator(); _j14it2554.hasNext(); ) {
		  CharacterActionChitComponent chit = (CharacterActionChitComponent) _j14it2554.next();
			if (chit.getGameObject().hasThisAttribute(Constants.GUILD_BENEFIT+"_3")) {
				character.getGameObject().remove(chit.getGameObject());
				chit.getGameObject().clearAllAttributes();
			}
		}
	}
	
	public boolean validateRequirementAndJoin(CharacterWrapper character, GameObject loot) {
		HostPrefWrapper hostPrefs = HostPrefWrapper.findHostPrefs(character.getGameData());
		if (character.hasGuildJoinRequirement() && RealmComponent.getRealmComponent(loot).isTreasure()) {
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