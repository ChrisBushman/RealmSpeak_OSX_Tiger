package com.robin.magic_realm.components.quest;

public final class LocationType {
	private final String _name;
	private final int _ordinal;
	private LocationType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final LocationType Any = new LocationType("Any", 0);
	public static final LocationType Lock = new LocationType("Lock", 1);
	public static final LocationType QuestChoice = new LocationType("QuestChoice", 2);
	public static final LocationType StepChoice = new LocationType("StepChoice", 3);
	public static final LocationType QuestRandom = new LocationType("QuestRandom", 4);
	public static final LocationType StepRandom = new LocationType("StepRandom", 5);

	private static final LocationType[] _VALUES = { Any, Lock, QuestChoice, StepChoice, QuestRandom, StepRandom };
	public static LocationType[] values() { LocationType[] r = new LocationType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static LocationType valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}

	// --- enum methods ---
	public String getDescriptionPrefix() {
		if (this == Any) {
				return "Any";
			}
			else if (this == Lock) {
				return "The first";
			}
			else if (this == QuestChoice) {
				return "At quest start, player selected";
			}
			else if (this == QuestRandom) {
				return "At quest start, randomly chosen";
			}
			else if (this == StepChoice) {
				return "At start of step, player selected";
			}
			else if (this == StepRandom) {
				return "At start of step, randomly chosen";
			}
		return "ERROR - No description!!";
	}
	
	public String getDescription() {
		if (this == Any) {
				return "Any location in the list is valid at any time during the quest.";
			}
			else if (this == Lock) {
				return "The first location in the list that a requirement is completed for becomes locked.";
			}
			else if (this == QuestChoice) {
				return "Player must pick a location from the list at the start of the quest.";
			}
			else if (this == QuestRandom) {
				return "A location is chosen at random from the list at the start of the quest.";
			}
			else if (this == StepChoice) {
				return "Player must pick a location from the list at the start of the first step that references it.";
			}
			else if (this == StepRandom) {
				return "A location is chosen at random from the list at the start of the first step that references it.";
			}
		return "ERROR - No description!!";
	}
}