package com.robin.magic_realm.RealmSpeak;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

import com.robin.game.objects.GameObject;
import com.robin.game.objects.GamePool;
import com.robin.general.swing.ComponentTools;
import com.robin.magic_realm.components.swing.RelationshipTable;
import com.robin.magic_realm.components.utility.Constants;

public class CharacterRelationshipPanel extends CharacterFramePanel {

	protected RelationshipTable relationshipTable;
	protected Hashtable charIdBoxHash; // id:JCheckBox hash for characters
	protected Hashtable charNameObjectHash; // name:GameObject hash for characters
	
	public CharacterRelationshipPanel(CharacterFrame parent) {
		super(parent);
		init();
	}
	private void init() {
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;
		setLayout(gridBag);
		
		relationshipTable = new RelationshipTable(getGameHandler().getRelationshipNames(),getCharacter());
		JScrollPane sp = new JScrollPane(relationshipTable);
		
		gbc.weightx = 1.0;
		gridBag.setConstraints(sp,gbc);
		add(sp);
		
		// one checkbox for every character
		charIdBoxHash = new Hashtable();
		charNameObjectHash = new Hashtable();
		GamePool pool = getGameHandler().getGamePool();
		ArrayList allChars = pool.find("character");
		Collections.sort(allChars,new Comparator() {
			public int compare(Object obj1,Object obj2) {
				GameObject go1 = (GameObject) obj1;
				GameObject go2 = (GameObject) obj2;
				return go1.getName().compareTo(go2.getName());
			}
		});
		
		boolean customCharacters = getHostPrefs().hasPref(Constants.EXP_CUSTOM_CHARS);
		JPanel enemyPanel = new JPanel(new GridLayout(allChars.size()+1,1));
		JLabel panelHeader = new JLabel("ENEMIES",SwingConstants.CENTER);
		panelHeader.setBackground(Color.red);
		panelHeader.setForeground(Color.white);
		panelHeader.setOpaque(true);
		enemyPanel.add(panelHeader);
		ComponentTools.lockComponentSize(enemyPanel,100,allChars.size()*18);
		JButton noneButton = new JButton("none");
		noneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				for (java.util.Iterator _j14it989 = (charNameObjectHash.values()).iterator(); _j14it989.hasNext(); ) {
				  GameObject otherCharacter = (GameObject) _j14it989.next();
					if(!otherCharacter.equals(getCharacter().getGameObject())) {
						getCharacter().setEnemyCharacter(otherCharacter,false);
					}
				}
				updatePanel();
			}
		});
		enemyPanel.add(noneButton);
		JButton allButton = new JButton("all");
		allButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				for (java.util.Iterator _j14it990 = (charNameObjectHash.values()).iterator(); _j14it990.hasNext(); ) {
				  GameObject otherCharacter = (GameObject) _j14it990.next();
					if(!otherCharacter.equals(getCharacter().getGameObject())) {
						getCharacter().setEnemyCharacter(otherCharacter,true);
					}
				}
				updatePanel();
			}
		});
		enemyPanel.add(allButton);
		for (java.util.Iterator _j14it991 = (allChars).iterator(); _j14it991.hasNext(); ) {
		  GameObject aChar = (GameObject) _j14it991.next();
			if (!aChar.equals(getCharacter().getGameObject())) { // no checkbox option for self
				if (aChar.hasThisAttribute(Constants.CUSTOM_CHARACTER) && !customCharacters) {
					continue;
				}
				JCheckBox cb = new JCheckBox(aChar.getName(),false);
				charIdBoxHash.put(aChar.getStringId(),cb);
				charNameObjectHash.put(aChar.getName(),aChar);
				cb.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						JCheckBox thisCb = (JCheckBox)ev.getSource();
						boolean enemy = thisCb.isSelected();
						GameObject theChar = (GameObject) charNameObjectHash.get(thisCb.getText());
						getCharacter().setEnemyCharacter(theChar,enemy);
					}
				});
				enemyPanel.add(cb);
			}
		}
		sp = new JScrollPane(enemyPanel);
		
		gbc.weightx = 0.2;
		gridBag.setConstraints(sp,gbc);
		add(sp);
	}
	public void updatePanel() {
		for (java.util.Iterator _j14it992 = (charNameObjectHash.values()).iterator(); _j14it992.hasNext(); ) {
		  GameObject aChar = (GameObject) _j14it992.next();
			JCheckBox cb = (JCheckBox) charIdBoxHash.get(aChar.getStringId());
			cb.setSelected(getCharacter().isEnemy(aChar));
		}
		relationshipTable.fireTableDataChanged();
	}
}