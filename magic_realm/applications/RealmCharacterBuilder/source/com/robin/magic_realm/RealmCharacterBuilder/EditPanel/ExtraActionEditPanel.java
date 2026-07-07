package com.robin.magic_realm.RealmCharacterBuilder.EditPanel;

import java.awt.GridLayout;
import java.util.*;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class ExtraActionEditPanel extends AdvantageEditPanel {
	
	private ButtonGroup group;
	private Hashtable actionHash;

	public ExtraActionEditPanel(CharacterWrapper pChar,String levelKey) {
		super(pChar,levelKey);
		setLayout(new GridLayout(12,1));
		
		group = new ButtonGroup();
		actionHash = new Hashtable();
		addOption("HIDE","H",true); // default
		addOption("MOVE","M");
		addOption("SEARCH","S");
		addOption("TRADE","T");
		addOption("REST","R");
		addOption("ALERT","A");
		addOption("HIRE","HR");
		addOption("ENCHANT","E");
		addOption("PEER","P");
		addOption("FLY","FLY");
		addOption("REMOTE SP","RS");
		addOption("CACHE","C");
		addOption("not-MOVE","!M");
		addOption("OFFROAD","O");
		
		// Initialize, if you can
		ArrayList extra = getAttributeList(Constants.EXTRA_ACTIONS);
		if (extra!=null) {
			for (java.util.Iterator _j14it936 = (extra).iterator(); _j14it936.hasNext(); ) {
			  String extraAction = (String) _j14it936.next();
				JRadioButton button = (JRadioButton) actionHash.get(extraAction);
				button.setSelected(true);
				break; // assume only ONE per list
			}
		}
	}
	private void addOption(String name,String action) {
		addOption(name,action,false);
	}
	private void addOption(String name,String action,boolean selected) {
		JRadioButton button = new JRadioButton(name,selected);
		group.add(button);
		add(button);
		actionHash.put(action,button);
	}
	public String toString() {
		return "Extra Action";
	}
	public boolean isCurrent() {
		return hasAttribute(Constants.EXTRA_ACTIONS);
	}
	protected void applyAdvantage() {
		for (java.util.Iterator _j14it937 = (actionHash.keySet()).iterator(); _j14it937.hasNext(); ) {
		  String action = (String) _j14it937.next();
			JRadioButton button = (JRadioButton) actionHash.get(action);
			if (button.isSelected()) {
				ArrayList list = new ArrayList();
				list.add(action);
				setAttributeList(Constants.EXTRA_ACTIONS,list);
				break;
			}
		}
	}
	public String getSuggestedDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("Gets an extra ");
		for (java.util.Iterator _j14it938 = (actionHash.keySet()).iterator(); _j14it938.hasNext(); ) {
		  String action = (String) _j14it938.next();
			JRadioButton button = (JRadioButton) actionHash.get(action);
			if (button.isSelected()) {
				sb.append(button.getText().toUpperCase());
			}
		}
		sb.append(" phase.");
		
		return sb.toString();
	}
}