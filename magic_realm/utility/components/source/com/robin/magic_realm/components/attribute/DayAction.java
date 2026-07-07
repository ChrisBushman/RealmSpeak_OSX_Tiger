package com.robin.magic_realm.components.attribute;

public class DayAction {
	
	public static final class ActionId {
		private final String _name;
		private final int _ordinal;
		private ActionId(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final ActionId NoAction = new ActionId("NoAction", 0);
		public static final ActionId Hide = new ActionId("Hide", 1);
		public static final ActionId Move = new ActionId("Move", 2);
		public static final ActionId Search = new ActionId("Search", 3);
		public static final ActionId Trade = new ActionId("Trade", 4);
		public static final ActionId Rest = new ActionId("Rest", 5);
		public static final ActionId Alert = new ActionId("Alert", 6);
		public static final ActionId Hire = new ActionId("Hire", 7);
		public static final ActionId Follow = new ActionId("Follow", 8);
		public static final ActionId Spell = new ActionId("Spell", 9);
		public static final ActionId SpellPrep = new ActionId("SpellPrep", 10);
		public static final ActionId EnhPeer = new ActionId("EnhPeer", 11);
		public static final ActionId Fly = new ActionId("Fly", 12);
		public static final ActionId RemSpell = new ActionId("RemSpell", 13);
		public static final ActionId Cache = new ActionId("Cache", 14);
		public static final ActionId Heal = new ActionId("Heal", 15);
		public static final ActionId Repair = new ActionId("Repair", 16);
		public static final ActionId Fortify = new ActionId("Fortify", 17);
		public static final ActionId Steal = new ActionId("Steal", 18);
		public static final ActionId Offroad = new ActionId("Offroad", 19);

		private static final ActionId[] _VALUES = { NoAction, Hide, Move, Search, Trade, Rest, Alert, Hire, Follow, Spell, SpellPrep, EnhPeer, Fly, RemSpell, Cache, Heal, Repair, Fortify, Steal, Offroad };
		public static ActionId[] values() { ActionId[] r = new ActionId[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static ActionId valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	
	public static DayAction HIDE_ACTION = new DayAction(ActionId.Hide,"Hide","H","hide");
	public static DayAction MOVE_ACTION = new DayAction(ActionId.Move,"Move","M","move"); // (clearing)
	public static DayAction SEARCH_ACTION = new DayAction(ActionId.Search,"Search","S","search");
	public static DayAction TRADE_ACTION = new DayAction(ActionId.Trade,"Trade","T","trade");
	public static DayAction REST_ACTION = new DayAction(ActionId.Rest,"Rest","R","rest");
	public static DayAction ALERT_ACTION = new DayAction(ActionId.Alert,"Alert","A","alert");
	public static DayAction HIRE_ACTION = new DayAction(ActionId.Hire,"Hire","HR","hire");
	public static DayAction FOLLOW_ACTION = new DayAction(ActionId.Follow,"Follow","F","follow");  // (individual)
	public static DayAction SPELL_ACTION = new DayAction(ActionId.Spell,"Enchant","E","spell");
	public static DayAction SPELL_PREP_ACTION = new DayAction(ActionId.SpellPrep,"Enchant Meditation","EM","spell");
	public static DayAction ENH_PEER_ACTION = new DayAction(ActionId.EnhPeer,"Enh.Peer","P","peer"); // (clearing) 
	public static DayAction FLY_ACTION = new DayAction(ActionId.Fly,"Fly","FLY","fly"); // (tile) 
	public static DayAction REMOTE_SPELL_ACTION = new DayAction(ActionId.RemSpell,"Rem.Spell","RS","remotespell"); // (clearing)
	public static DayAction CACHE_ACTION = new DayAction(ActionId.Cache,"Cache","C","cache");
	public static DayAction STEAL_ACTION = new DayAction(ActionId.Steal,"Steal","ST","steal");
	public static DayAction OFFROAD_TRAVEL_ACTION = new DayAction(ActionId.Offroad,"Offroad","O","offroad");
	
	// Special Actions (custom characters)
	public static DayAction HEAL_ACTION = new DayAction(ActionId.Heal,"Heal","HL","heal");				// Mostly like REST
	public static DayAction REPAIR_ACTION = new DayAction(ActionId.Repair,"Repair","RP","repair");		// Mostly like ALERT
	public static DayAction FORTIFY_ACTION = new DayAction(ActionId.Fortify,"Fortify","FT","fortify");	// Mostly like HIDE
	
	public static DayAction[] dayAction = {
		HIDE_ACTION,
		MOVE_ACTION,
		SEARCH_ACTION,
		TRADE_ACTION,
		REST_ACTION,
		ALERT_ACTION,
		HIRE_ACTION,
		FOLLOW_ACTION,
		SPELL_ACTION,
		SPELL_PREP_ACTION,
		ENH_PEER_ACTION,
		FLY_ACTION,
		REMOTE_SPELL_ACTION,
		CACHE_ACTION,
		STEAL_ACTION,
		OFFROAD_TRAVEL_ACTION,
		
		HEAL_ACTION,
		REPAIR_ACTION,
		FORTIFY_ACTION,
	};
	
	public static DayAction getDayAction(ActionId id) {
		for (int i=0;i<dayAction.length;i++) {
			if (dayAction[i].id==id) {
				return dayAction[i];
			}
		}
		return null; // no action
	}
	
	private ActionId id;
	private String name;
	private String code;
	private String iconName;
	public DayAction(ActionId id) {
		this(id,null,null,null);
	}
	public DayAction(ActionId id,String name,String code,String iconName) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.iconName = iconName;
	}
	public String getCode() {
		return code;
	}
	public ActionId getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getIconName() {
		return iconName;
	}
	/**
	 * @deprecated bob
	 */
	public boolean equals(Object o1) {
		return false;
	}
}