package com.robin.magic_realm.components.quest;

public final class CharacterActionType {
	private final String _name;
	private final int _ordinal;
	private CharacterActionType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final CharacterActionType Unknown = new CharacterActionType("Unknown", 0);
	public static final CharacterActionType ActivatingItem = new CharacterActionType("ActivatingItem", 1);
	public static final CharacterActionType DeactivatingItem = new CharacterActionType("DeactivatingItem", 2);
	public static final CharacterActionType Alert = new CharacterActionType("Alert", 3);
	public static final CharacterActionType Cache = new CharacterActionType("Cache", 4);
	public static final CharacterActionType CastSpell = new CharacterActionType("CastSpell", 5);
	public static final CharacterActionType Enchant = new CharacterActionType("Enchant", 6);
	public static final CharacterActionType Fly = new CharacterActionType("Fly", 7);
	public static final CharacterActionType Fortify = new CharacterActionType("Fortify", 8);
	public static final CharacterActionType Heal = new CharacterActionType("Heal", 9);
	public static final CharacterActionType Hide = new CharacterActionType("Hide", 10);
	public static final CharacterActionType Hire = new CharacterActionType("Hire", 11);
	public static final CharacterActionType Move = new CharacterActionType("Move", 12);
	public static final CharacterActionType Open = new CharacterActionType("Open", 13);
	public static final CharacterActionType Repair = new CharacterActionType("Repair", 14);
	public static final CharacterActionType Rest = new CharacterActionType("Rest", 15);
	public static final CharacterActionType SearchTable = new CharacterActionType("SearchTable", 16);
	public static final CharacterActionType Teleport = new CharacterActionType("Teleport", 17);
	public static final CharacterActionType Trading = new CharacterActionType("Trading", 18);
	public static final CharacterActionType Stealing = new CharacterActionType("Stealing", 19);
	public static final CharacterActionType AbandonMissionCampaign = new CharacterActionType("AbandonMissionCampaign", 20);
	public static final CharacterActionType CompleteMissionCampaign = new CharacterActionType("CompleteMissionCampaign", 21);
	public static final CharacterActionType FailMissionCampaign = new CharacterActionType("FailMissionCampaign", 22);
	public static final CharacterActionType PickUpMissionCampaign = new CharacterActionType("PickUpMissionCampaign", 23);
	public static final CharacterActionType CompleteBounty = new CharacterActionType("CompleteBounty", 24);

	private static final CharacterActionType[] _VALUES = { Unknown, ActivatingItem, DeactivatingItem, Alert, Cache, CastSpell, Enchant, Fly, Fortify, Heal, Hide, Hire, Move, Open, Repair, Rest, SearchTable, Teleport, Trading, Stealing, AbandonMissionCampaign, CompleteMissionCampaign, FailMissionCampaign, PickUpMissionCampaign, CompleteBounty };
	public static CharacterActionType[] values() { CharacterActionType[] r = new CharacterActionType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static CharacterActionType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}

	// --- enum methods ---
	public String getDescriptor() {
		if (this == PickUpMissionCampaign) {
				return "Must pick up ";
			}
			else if (this == AbandonMissionCampaign) {
				return "Must abandon ";
			}
			else if (this == FailMissionCampaign) {
				return "Must fail ";
			}
			else if (this == CompleteMissionCampaign) {
				return "Must complete ";
			}
			else {
				return "?";
			}
	}
	public static CharacterActionType[] mcValues() {
		CharacterActionType[] mc = new CharacterActionType[4];
		mc[0] = PickUpMissionCampaign;
		mc[1] = AbandonMissionCampaign;
		mc[2] = FailMissionCampaign;
		mc[3] = CompleteMissionCampaign;
		return mc;
	}
}