package com.robin.magic_realm.components.quest;

import javax.swing.ImageIcon;

import com.robin.general.swing.ImageCache;

public final class QuestState {
	private final String _name;
	private final int _ordinal;
	private QuestState(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
	public String toString() { return _name; }
	public String name() { return _name; }
	public int ordinal() { return _ordinal; }
	public boolean equals(Object o) { return this == o; }
	public int hashCode() { return _ordinal; }
	private int _thisOrdinal() { return _ordinal; }

	public static final QuestState New = new QuestState("New", 0);
	public static final QuestState Assigned = new QuestState("Assigned", 1);
	public static final QuestState Active = new QuestState("Active", 2);
	public static final QuestState Failed = new QuestState("Failed", 3);
	public static final QuestState Complete = new QuestState("Complete", 4);

	private static final QuestState[] _VALUES = { New, Assigned, Active, Failed, Complete };
	public static QuestState[] values() { QuestState[] r = new QuestState[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
	public static QuestState valueOf(String s) {
		for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
		throw new IllegalArgumentException(s);
	}

	// --- enum methods ---
	public static int tokenSizePercent = 80;
	private static ImageIcon assignedIcon = ImageCache.getIcon("quests/tokenpending",tokenSizePercent);
	private static ImageIcon activeIcon = ImageCache.getIcon("quests/token",tokenSizePercent);
	private static ImageIcon failedIcon = ImageCache.getIcon("quests/tokenfail",tokenSizePercent);
	private static ImageIcon completeIcon = ImageCache.getIcon("quests/tokendone",tokenSizePercent);
	
	private static ImageIcon smallFailedIcon = ImageCache.getIcon("quests/tokenfail",30);
	private static ImageIcon smallCompleteIcon = ImageCache.getIcon("quests/tokendone",30);
	
	public ImageIcon getIcon() {
		if (this == New) {
				return assignedIcon; // not really used anyway
			}
			else if (this == Assigned) {
				return assignedIcon;
			}
			else if (this == Active) {
				return activeIcon;
			}
			else if (this == Failed) {
				return failedIcon;
			}
			else if (this == Complete) {
				return completeIcon;
			}
		return null;
	}
	public ImageIcon getSmallIcon() {
		if (this == Failed) {
				return smallFailedIcon;
			}
			else if (this == Complete) {
				return smallCompleteIcon;
			}
			else {
				return null;
			}
	}
	public boolean isFinished() {
		return this==Failed || this==Complete;
	}
}