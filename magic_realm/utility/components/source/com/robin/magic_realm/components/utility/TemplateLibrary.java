package com.robin.magic_realm.components.utility;

import java.util.*;

import com.robin.game.objects.*;
import com.robin.magic_realm.components.wrapper.SpellWrapper;

public class TemplateLibrary {
	
	public static final String WEAPON_QUERY = "weapon,!character,!magic";
	public static final String CHARACTER_QUERY = "character";
	public static final String ARMOR_QUERY = "armor,!character,!treasure,!magic";
	
	private static TemplateLibrary singleton = null;
	public static TemplateLibrary getSingleton() {
		if (singleton==null) {
			singleton = new TemplateLibrary();
		}
		return singleton;
	}
	
	private GamePool dataPool;
	private Hashtable templateHash;
	
	public static TemplateLibrary reinitSingleton() {
		singleton = new TemplateLibrary();
		return singleton;
	}
	private TemplateLibrary() {
		RealmLoader loader = new RealmLoader();
		dataPool = new GamePool(loader.getData().getGameObjects());
		templateHash = new Hashtable();
		addQuery(dataPool,WEAPON_QUERY);
		addQuery(dataPool,CHARACTER_QUERY);
		addQuery(dataPool,ARMOR_QUERY);
	}
	private void addQuery(GamePool pool,String query) {
		ArrayList items = pool.find(query);
		for (java.util.Iterator _j14it2794 = (items).iterator(); _j14it2794.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2794.next();
			String name = go.getName();
			if (!templateHash.containsKey(name)) {
				addTemplate(name,go);
			}
		}
	}
	public ArrayList getAllWeaponNames() {
		ArrayList names = new ArrayList();
		for (java.util.Iterator _j14it2795 = (getAllNames()).iterator(); _j14it2795.hasNext(); ) {
		  String name = (String) _j14it2795.next();
			GameObject go = getWeaponTemplate(name);
			if (go!=null) {
				names.add(name);
			}
		}
		return names;
	}
	public ArrayList getAllArmorNames() {
		ArrayList names = new ArrayList();
		for (java.util.Iterator _j14it2796 = (getAllNames()).iterator(); _j14it2796.hasNext(); ) {
		  String name = (String) _j14it2796.next();
			GameObject go = getArmorTemplate(name);
			if (go!=null) {
				names.add(name);
			}
		}
		return names;
	}
	public GameObject getWeaponTemplate(String name) {
		GameObject go = (GameObject) templateHash.get(name);
		if (go!=null && go.hasAllKeyVals(WEAPON_QUERY)) {
			return go;
		}
		return null;
	}
	public boolean hasWeaponTemplate(String name) {
		return getWeaponTemplate(name)!=null;
	}
	public GameObject getArmorTemplate(String name) {
		GameObject go = (GameObject) templateHash.get(name);
		if (go!=null && go.hasAllKeyVals(ARMOR_QUERY)) {
			return go;
		}
		return null;
	}
	public boolean hasArmorTemplate(String name) {
		return getArmorTemplate(name)!=null;
	}
	public GameObject getCharacterTemplate(String name) {
		GameObject go = (GameObject) templateHash.get(name);
		if (go!=null && go.hasAllKeyVals(CHARACTER_QUERY)) {
			return go;
		}
		return null;
	}
	public ArrayList getAllCharacterTemplateNames() {
		ArrayList names = new ArrayList();
		for (java.util.Iterator _j14it2797 = (getAllNames()).iterator(); _j14it2797.hasNext(); ) {
		  String name = (String) _j14it2797.next();
			GameObject go = getCharacterTemplate(name);
			if (go!=null) {
				names.add(name);
			}
		}
		return names;
	}
	public ArrayList getAllNames() {
		return new ArrayList(templateHash.keySet());
	}
	private void addTemplate(String name,GameObject go) {
		templateHash.put(name,go);
	}
	public GameObject getCompanionTemplate(String name,String query) {
		return getCompanionTemplate(name,query,true);
	}
	
	public GameObject getCompanionTemplate(String name,String query,boolean includeHorse) {
		GameObject template = (GameObject) templateHash.get(name);
		if (template==null) {
			if (query.startsWith("Transform|")) {
				ArrayList list = dataPool.find("name=Transform");
				if (list!=null && !list.isEmpty()) {
					GameObject go = (GameObject) list.get(0);
					String block = query.substring(10);
					template = GameObject.createEmptyGameObject();
					SpellWrapper.copyTransformToObject(go,block,template);
				}
			}
			else {
				ArrayList list = dataPool.find(query);
				if (list!=null && !list.isEmpty()) {
					GameObject go = (GameObject) list.get(0);
					template = GameObject.createEmptyGameObject();
					template.copyAttributesFrom(go);
					
					// Get steeds
					if(includeHorse) {
						for (java.util.Iterator _j14it2798 = (go.getHold()).iterator(); _j14it2798.hasNext(); ) {
						  GameObject held = (GameObject) _j14it2798.next();
							GameObject heldTemplate = GameObject.createEmptyGameObject();
							heldTemplate.copyAttributesFrom(held);
							heldTemplate.setAttribute("trot","chit_color","paleyellow");
							heldTemplate.setAttribute("gallop","chit_color","yellow");
							heldTemplate.setThisAttribute(Constants.COMPANION);
							template.add(heldTemplate);
						}
					}
					
					// update some specific attributes
					template.removeThisAttribute("setup_start");
					template.removeThisAttribute("monster_die");
					template.removeThisAttribute("monster_die2");
					template.removeThisAttribute("hire_type");
				}
			}
			if (template!=null) {
				template.setName(name);
				template.setAttribute("light","chit_color","paleyellow");
				template.setAttribute("dark","chit_color","yellow");
				template.setThisAttribute(Constants.COMPANION); // Makes these guys easy to find
				template.setThisAttribute("query",query);
				
				templateHash.put(name,template);
			}
		}
		return template;
	}
	public GameObject createCompanionFromTemplate(GameData gameData,GameObject go) {
		GameObject companion = gameData.createNewObject();
		companion.copyAttributesFrom(go);
		// Get steeds
		for (java.util.Iterator _j14it2799 = (go.getHold()).iterator(); _j14it2799.hasNext(); ) {
		  GameObject held = (GameObject) _j14it2799.next();
			GameObject heldTemplate = gameData.createNewObject();
			heldTemplate.copyAttributesFrom(held);
			companion.add(heldTemplate);
		}
		return companion;
	}
}