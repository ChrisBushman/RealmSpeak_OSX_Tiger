package com.robin.magic_realm.RealmQuestBuilder;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import com.robin.general.swing.*;
import com.robin.magic_realm.components.quest.*;
import com.robin.magic_realm.components.utility.Constants;

public class QuestDeckViewer extends AggressiveDialog {	
	JTable table;
	private Constants.QuestDeckMode mode;
	private ArrayList quests;
	private int totalVPs=0;
	private int deckCards=0;
	private Quest selectedQuest;
	public QuestDeckViewer(JFrame frame, ArrayList input, Constants.QuestDeckMode mode) {
		super(frame, "Quest Deck", true);
		this.quests = input;
		this.mode = mode;
		setLayout(new BorderLayout());
		setSize(800,600);
		
		int allPlayCount = 0;
		int eventCount = 0;
		totalVPs=0;
		deckCards=0;
		Constants.QuestDeckMode _qdm = mode;
		if (_qdm == Constants.QuestDeckMode.QtR || _qdm == Constants.QuestDeckMode.SR || _qdm == Constants.QuestDeckMode.GQ) {
			for (java.util.Iterator _j14it302 = (quests).iterator(); _j14it302.hasNext(); ) {
			  Quest quest = (Quest) _j14it302.next();
				int count = quest.getInt(QuestConstants.CARD_COUNT);
				if (quest.isAllPlay()) {
					allPlayCount += count;
				}
				else {
					deckCards += count;
				}
				totalVPs += (quest.getInt(QuestConstants.VP_REWARD)*count);
			}
		} else if (_qdm == Constants.QuestDeckMode.BoQ) {
			for (java.util.Iterator _j14it303 = (quests).iterator(); _j14it303.hasNext(); ) {
			  Quest quest = (Quest) _j14it303.next();
				if (quest.isEvent()) {
					eventCount ++;
				}
				else {
					deckCards ++;
				}
			}
		}

		table = new JTable(new DeckTableModel());
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent ev) {
				if (ev.getClickCount()!=2) return;
				int row = table.getSelectedRow();
				int lastColumn = table.getColumnCount()-1;
				String filePath= (String) table.getValueAt(row, lastColumn);
				
				for (java.util.Iterator _j14it304 = (quests).iterator(); _j14it304.hasNext(); ) {
				  Quest quest = (Quest) _j14it304.next();
					if (quest.filepath == filePath) {
						selectedQuest = quest;
						break;
					}
				}
				
				setVisible(false);
				dispose();
			}
		});
		TableSorter.makeSortable(table);
		ComponentTools.lockColumnWidth(table,0,40);
		ComponentTools.lockColumnWidth(table,1,40);
		ComponentTools.lockColumnWidth(table,2,50);
		ComponentTools.lockColumnWidth(table,3,40);
		ComponentTools.lockColumnWidth(table,5,200);
		if (mode == Constants.QuestDeckMode.QtR || mode == Constants.QuestDeckMode.SR) {
			ComponentTools.lockColumnWidth(table,6,50);
			ComponentTools.lockColumnWidth(table,7,40);
			ComponentTools.lockColumnWidth(table,8,60);
		}
		add(new JScrollPane(table),BorderLayout.CENTER);
		
		Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalGlue());
		box.add(new JLabel("Deck Cards: "+deckCards));
		box.add(Box.createHorizontalGlue());
		{
			Constants.QuestDeckMode _qdm2 = mode;
			if (_qdm2 == Constants.QuestDeckMode.QtR || _qdm2 == Constants.QuestDeckMode.SR) {
				box.add(new JLabel("All Play Cards: "+allPlayCount));
				box.add(Box.createHorizontalGlue());
				box.add(new JLabel("Total VPs: "+totalVPs));
				box.add(Box.createHorizontalGlue());
			} else if (_qdm2 == Constants.QuestDeckMode.BoQ) {
				box.add(new JLabel("Events: "+eventCount));
				box.add(Box.createHorizontalGlue());
			}
		}
		JButton close = new JButton("Close");
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				setVisible(false);
				dispose();
			}
		});
		box.add(close);
		add(box,BorderLayout.SOUTH);
		getRootPane().setDefaultButton(close);
	}
	public Quest getSelectedQuest() {
		return selectedQuest;
	}
	private static String[] HEADER_QtR = {
		"TEST",
		"BAD",
		"ALL",
		"ACT",
		"Name",
		"Minor Characters",
		"Count",
		"VPs",
		"% Draw",
		"FilePath"
	};
	private static String[] HEADER_BoQ = {
			"TEST",
			"BAD",
			"EVENT",
			"ACT",
			"Name",
			"Minor Characters",
			"FilePath"
		};
	private static Class[] CLASS = {
		ImageIcon.class,
		ImageIcon.class,
		ImageIcon.class,
		ImageIcon.class,
		String.class,
		String.class,
		Integer.class,
		Integer.class,
		Integer.class,
		String.class
	};
	static ImageIcon test = IconFactory.findIcon("icons/search.gif");
	static ImageIcon cross = IconFactory.findIcon("icons/cross.gif");
	static ImageIcon check = IconFactory.findIcon("icons/check.gif");
	static ImageIcon plus = IconFactory.findIcon("icons/plus.gif");
	private class DeckTableModel extends AbstractTableModel {
		public DeckTableModel() {
		}

		public int getColumnCount() {
			Constants.QuestDeckMode _qdm3 = mode;
			if (_qdm3 == Constants.QuestDeckMode.GQ || _qdm3 == Constants.QuestDeckMode.BoQ) {
				return HEADER_BoQ.length;
			}
			return HEADER_QtR.length;
		}
		
		public Class getColumnClass(int col) {
			return CLASS[col];
		}
		
		public String getColumnName(int col) {
			Constants.QuestDeckMode _qdm4 = mode;
			if (_qdm4 == Constants.QuestDeckMode.GQ || _qdm4 == Constants.QuestDeckMode.BoQ) {
				return HEADER_BoQ[col];
			}
			return HEADER_QtR[col];
		}

		public int getRowCount() {
			return quests.size();
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowIndex<getRowCount()) {
				Quest quest = (Quest) quests.get(rowIndex);
				switch(columnIndex) {
					case 0:			return quest.isTesting()?test:null;
					case 1:			return quest.isBroken()?cross:null;
					case 2:			{
						Constants.QuestDeckMode _qdm5 = mode;
						if (_qdm5 == Constants.QuestDeckMode.QtR) {
							return quest.isAllPlay()?check:null;
						} else if (_qdm5 == Constants.QuestDeckMode.GQ || _qdm5 == Constants.QuestDeckMode.BoQ) {
							return quest.isEvent()?check:null;
						} else {
							return null;
						}
					}
					case 3:			return quest.isActivateable()?plus:null;
					case 4:			return quest.getName();
					case 5:			return getMinorCharacters(quest);
					case 6:			return new Integer(quest.getInt(QuestConstants.CARD_COUNT));
					case 7:			return new Integer(quest.getInt(QuestConstants.VP_REWARD));
					case 8:			return new Integer((int)Math.round(quest.getInt(QuestConstants.CARD_COUNT)*100.0/deckCards));
					case 9:			return quest.filepath;
				}
			}
			return null;
		}
		private String getMinorCharacters(Quest quest) {
			StringBuffer sb = new StringBuffer();
			for (java.util.Iterator _j14it305 = (quest.getMinorCharacters()).iterator(); _j14it305.hasNext(); ) {
			  QuestMinorCharacter mc = (QuestMinorCharacter) _j14it305.next();
				if (sb.length()>0) sb.append(',');
				sb.append(mc.getName());
			}
			return sb.toString();
		}
	}
}