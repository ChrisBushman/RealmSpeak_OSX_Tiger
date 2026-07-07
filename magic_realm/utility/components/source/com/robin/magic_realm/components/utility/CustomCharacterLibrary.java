package com.robin.magic_realm.components.utility;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ImageIcon;

import com.robin.game.objects.GameObject;
import com.robin.game.objects.GamePool;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class CustomCharacterLibrary {
	private static CustomCharacterLibrary singleton = null;
	public static CustomCharacterLibrary getSingleton() {
		if (singleton==null) {
			singleton = new CustomCharacterLibrary();
		}
		return singleton;
	}
	
	private Hashtable customCharacterHash;
	private Hashtable customCharacterImageHash;
	private CustomCharacterLibrary() {
		customCharacterHash = new Hashtable();
		customCharacterImageHash = new Hashtable();
	}
	public void addCustomCharacterTemplate(GameObject character,ImageIcon detailImage) {
		customCharacterHash.put(character.getName(),character);
		customCharacterImageHash.put(character.getName(),detailImage);
	}
	public ArrayList getCharacterTemplateNameList() {
		return new ArrayList(customCharacterHash.keySet());
	}
	public GameObject getCharacterTemplate(String name) {
		return (GameObject) customCharacterHash.get(name);
	}
	private static String getNameFor(GameObject go) {
		return go.getAttribute("level_4","name");
	}
	public String getCharacterUniqueKey(GameObject go) {
		return getCharacterUniqueKey(getNameFor(go));
	}
	public String getCharacterUniqueKey(String name) {
		GameObject go = getCharacterTemplate(name);
		if (go!=null) {
			CharacterWrapper character = new CharacterWrapper(go);
			return character.getCharacterLevelName(4)+":"+go.getGameData().getCheckSum();
		}
		return null;
	}
	public ArrayList getCharacterTemplateList() {
		return new ArrayList(customCharacterHash.values());
	}
	public ImageIcon getCharacterImage(String name) {
		return (ImageIcon) customCharacterImageHash.get(name);
	}
	public ArrayList getCharacterWeapons(GameObject go) {
		GameObject character = getCharacterTemplate(getNameFor(go));
		GamePool pool = new GamePool(character.getGameData().getGameObjects());
		return pool.find("weapon,!character,!magic");
	}
	public ArrayList getCharacterArmor(GameObject go) {
		GameObject character = getCharacterTemplate(getNameFor(go));
		GamePool pool = new GamePool(character.getGameData().getGameObjects());
		return pool.find("armor,!character,!magic");
	}
	public ArrayList getCharacterCompanions(GameObject go) {
		GameObject character = getCharacterTemplate(getNameFor(go));
		GamePool pool = new GamePool(character.getGameData().getGameObjects());
		return pool.find("companion");
	}
	private ArrayList getAllUniqueKeys() {
		ArrayList list =  new ArrayList();
		for (java.util.Iterator _j14it2686 = (getCharacterTemplateNameList()).iterator(); _j14it2686.hasNext(); ) {
		  String name = (String) _j14it2686.next();
			list.add(getCharacterUniqueKey(name));
		}
		return list;
	}
	public ArrayList getMissingCharacterNames(ArrayList expectedList) {
		ArrayList allUniqueKeys = getAllUniqueKeys();
		ArrayList list =  new ArrayList();
		for (java.util.Iterator _j14it2687 = (expectedList).iterator(); _j14it2687.hasNext(); ) {
		  String val = (String) _j14it2687.next();
			if (!allUniqueKeys.contains(val)) {
				int col = val.lastIndexOf(':');
				list.add(val.substring(0,col));
			}
		}
		return list;
	}
}