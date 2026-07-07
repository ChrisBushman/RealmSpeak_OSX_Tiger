package com.robin.magic_realm.RealmQuestBuilder;

import java.util.ArrayList;

import javax.swing.JComponent;

public class QuestPropertyBlock {

	public static final class FieldType {
		private final String _name;
		private final int _ordinal;
		private FieldType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final FieldType Boolean = new FieldType("Boolean", 0);
		public static final FieldType ChitType = new FieldType("ChitType", 1);
		public static final FieldType GameObjectWrapperSelector = new FieldType("GameObjectWrapperSelector", 2);
		public static final FieldType NoSpacesTextLine = new FieldType("NoSpacesTextLine", 3);
		public static final FieldType Number = new FieldType("Number", 4);
		public static final FieldType NumberAll = new FieldType("NumberAll", 5);
		public static final FieldType Regex = new FieldType("Regex", 6);
		public static final FieldType RegexIgnoreChitTypes = new FieldType("RegexIgnoreChitTypes", 7);
		public static final FieldType SmartTextLine = new FieldType("SmartTextLine", 8);
		public static final FieldType SmartTextArea = new FieldType("SmartTextArea", 9);
		public static final FieldType StringSelector = new FieldType("StringSelector", 10);
		public static final FieldType TextArea = new FieldType("TextArea", 11);
		public static final FieldType TextLine = new FieldType("TextLine", 12);
		public static final FieldType CompanionSelector = new FieldType("CompanionSelector", 13);

		private static final FieldType[] _VALUES = { Boolean, ChitType, GameObjectWrapperSelector, NoSpacesTextLine, Number, NumberAll, Regex, RegexIgnoreChitTypes, SmartTextLine, SmartTextArea, StringSelector, TextArea, TextLine, CompanionSelector };
		public static FieldType[] values() { FieldType[] r = new FieldType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static FieldType valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}

	private String keyName;
	private String fieldName;
	private FieldType fieldType;
	private Object[] selections;
	private String[] keyVals;
	
	private JComponent component;
	
	public QuestPropertyBlock(String keyName, String fieldName, FieldType fieldType) {
		this(keyName, fieldName, fieldType, null);
	}

	public QuestPropertyBlock(String keyName, String fieldName, FieldType fieldType, Object[] selections) {
		this(keyName, fieldName, fieldType, selections, null);
	}

	public QuestPropertyBlock(String keyName, String fieldName, FieldType fieldType, Object[] selections, String[] keyVals) {
		this.keyName = keyName;
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.selections = selections;
		this.keyVals = keyVals;
	}

	public String getKeyName() {
		return keyName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public Object[] getSelections() {
		return selections;
	}
	public ArrayList getSelectionsAsStrings() {
		ArrayList list = new ArrayList();
		if (selections!=null) {
			for (int _j14i340 = 0; _j14i340 < selections.length; _j14i340++) {
			  Object val = selections[_j14i340];
				list.add(val.toString());
			}
		}
		return list;
	}
	
	public String[] getKeyVals() {
		return keyVals;
	}

	public JComponent getComponent() {
		return component;
	}

	public void setComponent(JComponent component) {
		this.component = component;
	}
}