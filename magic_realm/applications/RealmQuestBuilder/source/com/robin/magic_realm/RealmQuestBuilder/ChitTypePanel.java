package com.robin.magic_realm.RealmQuestBuilder;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.*;

import com.robin.magic_realm.components.quest.ChitItemType;

public class ChitTypePanel extends JPanel {
	
	JCheckBox treasureType;
	JCheckBox weaponType;
	JCheckBox armorType;
	JCheckBox sizeType;
	JCheckBox horseType;
	
	public ChitTypePanel(ArrayList types) {
		initComponents();
		if (types==null) return;
		for (java.util.Iterator _j14it338 = (types).iterator(); _j14it338.hasNext(); ) {
		  String type = (String) _j14it338.next();
			ChitItemType cit = ChitItemType.valueOf(type);
			if (cit == ChitItemType.None) {
				treasureType.setSelected(false);
				weaponType.setSelected(false);
				armorType.setSelected(false);
				sizeType.setSelected(false);
				horseType.setSelected(false);
			} else if (cit == ChitItemType.Treasure) {
				treasureType.setSelected(true);
			} else if (cit == ChitItemType.Weapon) {
				weaponType.setSelected(true);
			} else if (cit == ChitItemType.Armor) {
				armorType.setSelected(true);
			} else if (cit == ChitItemType.Great) {
				sizeType.setSelected(true);
			} else if (cit == ChitItemType.Horse) {
				horseType.setSelected(true);
			}
		}
	}
	private void initComponents() {
		setLayout(new GridLayout(2,2));
		treasureType = new JCheckBox("Treasure");
		add(treasureType);
		horseType = new JCheckBox("Horse");
		add(horseType);
		weaponType = new JCheckBox("Weapon");
		add(weaponType);
		armorType = new JCheckBox("Armor");
		add(armorType);
		sizeType = new JCheckBox("Great");
		add(sizeType);
		setBorder(BorderFactory.createEtchedBorder());
	}
	public ArrayList getChitItemTypes() {
		boolean allUnchecked = !treasureType.isSelected() && !weaponType.isSelected() && !armorType.isSelected( )&& !sizeType.isSelected() && !horseType.isSelected();
		ArrayList types = new ArrayList();
		if (allUnchecked) {
			types.add(ChitItemType.None);
		}
		else {
			if (treasureType.isSelected()) types.add(ChitItemType.Treasure);
			if (weaponType.isSelected()) types.add(ChitItemType.Weapon);
			if (armorType.isSelected()) types.add(ChitItemType.Armor);
			if (sizeType.isSelected()) types.add(ChitItemType.Great);
			if (horseType.isSelected()) types.add(ChitItemType.Horse);
		}
		return types;
	}
	public ArrayList getChitTypeList() {
		return ChitItemType.listToStrings(getChitItemTypes());
	}
}