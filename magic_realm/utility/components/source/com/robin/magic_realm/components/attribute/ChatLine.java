package com.robin.magic_realm.components.attribute;

import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class ChatLine {
	
	public static final class HeaderMode {
		private final String _name;
		private final int _ordinal;
		private HeaderMode(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final HeaderMode CharacterName = new HeaderMode("CharacterName", 0);
		public static final HeaderMode PlayerName = new HeaderMode("PlayerName", 1);
		public static final HeaderMode Both = new HeaderMode("Both", 2);

		private static final HeaderMode[] _VALUES = { CharacterName, PlayerName, Both };
		public static HeaderMode[] values() { HeaderMode[] r = new HeaderMode[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static HeaderMode valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	
	public static String BOLD_PREFIX = "b_";
	private static HeaderMode headerMode = HeaderMode.CharacterName;
	public static HeaderMode getHeaderMode() {
		return headerMode;
	}
	public static void setHeaderMode(HeaderMode mode) {
		headerMode = mode;
	}
	
	private CharacterWrapper character;
	private String text;
	public ChatLine(CharacterWrapper character,String text) {
		this.character = character;
		this.text = text;
	}
	public String getHeader() {
		if (headerMode == HeaderMode.CharacterName) {
			return character.getName();
		} else if (headerMode == HeaderMode.PlayerName) {
			return character.getPlayerName();
		}
		return character.getName()+" ("+character.getPlayerName()+")";
	}
	public String getHeaderStyleName() {
		return BOLD_PREFIX+character.getChatStyle();
	}
	public String getText() {
		return text;
	}
	public String getTextStyleName() {
		return character.getChatStyle();
	}
	public boolean isValid() {
		return character!=null && text.trim().length()>0;
	}
}