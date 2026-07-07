package com.robin.magic_realm.components.table;

import javax.swing.JFrame;

import com.robin.game.objects.GameData;
import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.MonsterCreator;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.CombatWrapper;

public class SummonDemon extends MonsterTable {

	public static final String KEY = "SummonDemon";
	
	public static final class DemonType {
		private final String _name;
		private final int _ordinal;
		private DemonType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final DemonType Devil = new DemonType("Devil", 0);
		public static final DemonType WingedDemon = new DemonType("WingedDemon", 1);
		public static final DemonType Demon = new DemonType("Demon", 2);
		public static final DemonType Ghoul = new DemonType("Ghoul", 3);
		public static final DemonType Zombie = new DemonType("Zombie", 4);
		public static final DemonType Ghost = new DemonType("Ghost", 5);

		private static final DemonType[] _VALUES = { Devil, WingedDemon, Demon, Ghoul, Zombie, Ghost };
		public static DemonType[] values() { DemonType[] r = new DemonType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static DemonType valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	
	public SummonDemon(JFrame frame) {
		super(frame);
	}
	public String getTableKey() {
		return KEY;
	}
	public String getTableName(boolean longDescription) {
		return "Summon Demon";
	}
	public String getMonsterKey() {
		return "summoned_demon";
	}
	public String applyOne(CharacterWrapper character) {
		summon(character,DemonType.Devil);
		return "Devil Summoned";
	}
	public String applyTwo(CharacterWrapper character) {
		summon(character,DemonType.WingedDemon);
		return "Winged Demon Summoned";
	}
	public String applyThree(CharacterWrapper character) {
		summon(character,DemonType.Demon);
		return "Demon Summoned";
	}
	public String applyFour(CharacterWrapper character) {
		summon(character,DemonType.Ghoul);
		return "Ghoul Summoned";
	}
	public String applyFive(CharacterWrapper character) {
		summon(character,DemonType.Zombie);
		return "Zombie Summoned";
	}
	public String applySix(CharacterWrapper character) {
		summon(character,DemonType.Ghost);
		return "Ghost Summoned";
	}
	public GameObject createDemon(GameData data, DemonType type) {
		GameObject demon = getMonsterCreator().createMonster(data);
		DemonType _dt = type;
		if (_dt == DemonType.Devil) {
			getMonsterCreator().setupGameObject(demon,"Devil","demon","X",true,false,false,"wesnoth/units");
			MonsterCreator.setupSide(demon,"light","",0,0,18,-1,"gray");
			MonsterCreator.setupSide(demon,"dark","RED",0,0,18,-1,"darkgray");
			demon.setAttribute("light", "magic_type", "V");
			demon.setAttribute("light", "attack_spell", Constants.DEVILS_SPELL);
			demon.setThisAttribute(Constants.NO_CHANGE_TACTICS);
			demon.setThisAttribute(Constants.DEVIL);
			demon.setThisAttribute(Constants.ICON_SIZE,"0.9");
		}
		else if (_dt == DemonType.WingedDemon) {
			getMonsterCreator().setupGameObject(demon,"Winged Demon","galerunner","T",false,true,false,"wesnoth/units");
			MonsterCreator.setupSide(demon,"light","",0,3,17,3,"gray");
			MonsterCreator.setupSide(demon,"dark","M",0,3,17,3,"darkgray");
			demon.setAttribute("light", "magic_type", "V");
			demon.setAttribute("light", "attack_spell", Constants.POWER_OF_THE_PIT);
			demon.setThisAttribute(Constants.DEMON);
			demon.setThisAttribute(Constants.ICON_SIZE,"0.9");
		}
		else if (_dt == DemonType.Demon) {
			getMonsterCreator().setupGameObject(demon,"Demon","yuureNightmare","T",false,false,false,"wesnoth/units");
			MonsterCreator.setupSide(demon,"light","",0,2,17,4,"gray");
			MonsterCreator.setupSide(demon,"dark","H",0,2,17,4,"darkgray");
			demon.setAttribute("light", "magic_type", "V");
			demon.setAttribute("light", "attack_spell", Constants.POWER_OF_THE_PIT);
			demon.setThisAttribute(Constants.DEMON);
			demon.setThisAttribute(Constants.ICON_SIZE,"0.9");
		}
		else if (_dt == DemonType.Ghoul) {
			getMonsterCreator().setupGameObject(demon,"Ghoul","ghoul","H",false,false,false,"wesnoth/units/undead");
			MonsterCreator.setupSide(demon,"light","M",0,4,0,4,"gray");
			MonsterCreator.setupSide(demon,"dark","M",0,4,0,4,"darkgray");
			demon.setThisAttribute(Constants.GHOUL);
			demon.setThisAttribute(Constants.UNDEAD);
			demon.setThisAttribute(Constants.UNDEAD_SUMMONED);
			demon.setThisAttribute(Constants.ICON_SIZE,"0.9");
		}
		else if (_dt == DemonType.Zombie) {
			getMonsterCreator().setupGameObject(demon,"Zombie","zombie","H",false,false,false,"wesnoth/units/undead");
			MonsterCreator.setupSide(demon,"light","L",0,5,0,5,"gray");
			MonsterCreator.setupSide(demon,"dark","L",0,5,0,5,"darkgray");
			demon.setThisAttribute(Constants.ZOMBIE);
			demon.setThisAttribute(Constants.UNDEAD);
			demon.setThisAttribute(Constants.UNDEAD_SUMMONED);
			demon.setThisAttribute(Constants.ICON_SIZE,"0.9");
		}
		else if (_dt == DemonType.Ghost) {
			getMonsterCreator().setupGameObject(demon,"Ghost","ghost","M",true,false,false,"wesnoth/units");
			MonsterCreator.setupSide(demon,"light","M",0,2,0,2,"gray");
			MonsterCreator.setupSide(demon,"dark","M",0,2,0,2,"darkgray");
			demon.setThisAttribute(Constants.GHOST);
			demon.setThisAttribute(Constants.UNDEAD);
			demon.setThisAttribute(Constants.UNDEAD_SUMMONED);
			demon.setThisAttribute(Constants.ICON_SIZE,"0.9");
		}
		demon.setThisAttribute(Constants.SPOILS_NONE);
		demon.setThisAttribute(Constants.SUPER_REALM);
		return demon;
	}
	private void summon(CharacterWrapper character, DemonType type) {
		GameData data = character.getGameObject().getGameData();
		GameObject demon = createDemon(data, type);
		CombatWrapper combat = new CombatWrapper(demon);
		combat.setSheetOwner(false);
		TileLocation tl = character.getCurrentLocation();
		if (tl!=null && tl.isInClearing()) {
			tl.clearing.add(demon,null);
		}
	}
	public void summon(GameData data, DemonType type, TileLocation tl) {
		GameObject demon = createDemon(data, type);
		CombatWrapper combat = new CombatWrapper(demon);
		combat.setSheetOwner(false);
		if (tl!=null && tl.isInClearing()) {
			tl.clearing.add(demon,null);
		}
	}
}