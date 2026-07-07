package com.robin.magic_realm.components.quest;

import javax.swing.ImageIcon;

import com.robin.general.swing.ImageCache;

public final class QuestStepState {
	private final String _name;
	private final int _ordinal;
	private QuestStepState(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final QuestStepState None = new QuestStepState("None", 0);
	public static final QuestStepState Pending = new QuestStepState("Pending", 1);
	public static final QuestStepState Ready = new QuestStepState("Ready", 2);
	public static final QuestStepState Finished = new QuestStepState("Finished", 3);
	public static final QuestStepState Failed = new QuestStepState("Failed", 4);

	private static final QuestStepState[] _VALUES = { None, Pending, Ready, Finished, Failed };
	public static QuestStepState[] values() { QuestStepState[] r = new QuestStepState[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static QuestStepState valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}

	// --- enum methods ---
	public static int tokenSizePercent = 60;
	private static ImageIcon readyIcon = ImageCache.getIcon("quests/token",tokenSizePercent);
	private static ImageIcon pendingIcon = ImageCache.getIcon("quests/tokenpending",tokenSizePercent);
	private static ImageIcon finishedIcon = ImageCache.getIcon("quests/tokendone",tokenSizePercent);
	private static ImageIcon failedIcon = ImageCache.getIcon("quests/tokenfail",tokenSizePercent);

	public ImageIcon getIcon() {
		if (this == None) {
				return null;
			}
			else if (this == Pending) {
				return pendingIcon;
			}
			else if (this == Ready) {
				return readyIcon;
			}
			else if (this == Finished) {
				return finishedIcon;
			}
			else if (this == Failed) {
				return failedIcon;
			}
		return null;
	}
	public String getTooltip() {
		if (this == None) {
				return null;
			}
			else if (this == Pending) {
				return "Quest Pending";
			}
			else if (this == Ready) {
				return "Quest Ready";
			}
			else if (this == Finished) {
				return "Quest Step Finished";
			}
			else if (this == Failed) {
				return "Quest Step Failed";
			}
		return null;
	}
}
