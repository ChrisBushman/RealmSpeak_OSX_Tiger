package com.robin.magic_realm.RealmSpeak;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import com.robin.game.objects.GameObject;
import com.robin.game.objects.GamePool;
import com.robin.magic_realm.components.*;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.TreasureUtility;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class CharacterDiscoveriesPanel extends CharacterFramePanel {
	protected TreasureLocationDiscoveryModel treasureLocationDiscoveryModel;
	protected JTable treasureLocationTable;
	protected JTable hiddenPathTable;
	protected JTable secretPassageTable;
	public CharacterDiscoveriesPanel(CharacterFrame parent) {
		super(parent);
		init();
	}
	private void init() {
		// Build the discovery lists
		GamePool pool = getGameHandler().getGamePool();
		ArrayList treasureLocations = new ArrayList();
		treasureLocations.addAll(pool.find("treasure_location,discovery"));
		
		// If questing is turn on, show lost city and castle?
		if (getHostPrefs().hasPref(Constants.QST_BOOK_OF_QUESTS) || getHostPrefs().hasPref(Constants.QST_QUEST_CARDS) || getHostPrefs().hasPref(Constants.QST_SR_QUESTS) || getHostPrefs().hasPref(Constants.QST_GUILD_QUESTS)) {
			treasureLocations.addAll(pool.find("red_special"));
		}
		
		ArrayList hiddenPathList = new ArrayList();
		ArrayList secretPassageList =  new ArrayList();
		for (java.util.Iterator _j14it1268 = (pool.find("tile")).iterator(); _j14it1268.hasNext(); ) {
		  GameObject go = (GameObject) _j14it1268.next();
			TileComponent tile = (TileComponent)RealmComponent.getRealmComponent(go);
			hiddenPathList.addAll(tile.getHiddenPaths());
			secretPassageList.addAll(tile.getSecretPassages());
		}
		Collections.sort(hiddenPathList);
		Collections.sort(secretPassageList);
		
		// Now construct the panels
		setLayout(new BorderLayout(5,5));
		
		JPanel innerPanel = new JPanel(new GridLayout(1,3));
			JPanel treasureLocs = new JPanel(new BorderLayout());
			treasureLocs.add(new JLabel("Treasure Locations"),"North");
			treasureLocationDiscoveryModel = new TreasureLocationDiscoveryModel(treasureLocations);
			treasureLocationTable = new JTable(treasureLocationDiscoveryModel);
			treasureLocationTable.setDefaultRenderer(String.class,new TreasureLocationDiscoveryRenderer(treasureLocations));
			treasureLocationTable.getColumnModel().getColumn(1).setMaxWidth(60);
			treasureLocs.add(new JScrollPane(treasureLocationTable));
		innerPanel.add(treasureLocs);
			JPanel hiddenPaths = new JPanel(new BorderLayout());
			hiddenPaths.add(new JLabel("Hidden Paths"),"North");
			hiddenPathTable = new JTable(new PathDiscoveryTableModel(hiddenPathList));
			hiddenPathTable.setDefaultRenderer(String.class,new PathDiscoveryRenderer(CharacterWrapper.DISC_HIDDEN_PATHS,hiddenPathList));
			hiddenPathTable.getColumnModel().getColumn(2).setMaxWidth(30);
			hiddenPaths.add(new JScrollPane(hiddenPathTable));
		innerPanel.add(hiddenPaths);
			JPanel secretPassages = new JPanel(new BorderLayout());
			secretPassages.add(new JLabel("Secret Passages"),"North");
			secretPassageTable = new JTable(new PathDiscoveryTableModel(secretPassageList));
			secretPassageTable.setDefaultRenderer(String.class,new PathDiscoveryRenderer(CharacterWrapper.DISC_SECRET_PASSAGES,secretPassageList));
			secretPassageTable.getColumnModel().getColumn(2).setMaxWidth(30);
			secretPassages.add(new JScrollPane(secretPassageTable));
		innerPanel.add(secretPassages);
		add(innerPanel,"Center");
	}
	public void updatePanel() {
		// Check to see if any caches were discovered or emptied
		ArrayList list = getCharacter().getTreasureLocationDiscoveries();
		if (list!=null) {
			treasureLocationDiscoveryModel.update(list);
		}
	}
	private class TreasureLocationDiscoveryModel extends AbstractTableModel {
		private ArrayList list;
		private ArrayList discoveryNamesList;
		public TreasureLocationDiscoveryModel(ArrayList list) {
			this.list = list;
			discoveryNamesList = new ArrayList();
			for (java.util.Iterator _j14it1269 = (list).iterator(); _j14it1269.hasNext(); ) {
			  GameObject go = (GameObject) _j14it1269.next();
				discoveryNamesList.add(go.getName());
			}
		}
		public void update(ArrayList allDiscoveries) {
			for (java.util.Iterator _j14it1270 = (allDiscoveries).iterator(); _j14it1270.hasNext(); ) {
			  String name = (String) _j14it1270.next();
				if (discoveryNamesList.indexOf(name) < 0) {
					GameObject go = getGameHandler().getClient().getGameData().getGameObjectByName(name);
					list.add(go);
					discoveryNamesList.add(name);
				}
			}
		}
		public int getRowCount() {
			return list.size();
		}
		public int getColumnCount() {
			return 2;
		}
		public String getColumnName(int column) {
			return "";
		}
		public Class getColumnClass(int column) {
			return String.class;
		}
		public Object getValueAt(int row, int column) {
			if (row<list.size()) {
				GameObject tl = (GameObject) list.get(row);
				switch(column) {
					case 0:
						String name = tl.getName();
						boolean tableRoll = tl.hasAttributeBlock("table");
						int clearing = tl.getThisInt("clearing");
						return name+(clearing==0?"":(" "+clearing))+(tableRoll?" (see table)":"");
					case 1:
						if (tl.hasThisAttribute(RealmComponent.RED_SPECIAL)) return "n/a";
						int count = TreasureUtility.getTreasureCount(tl,getCharacter());
						return count==0?"---":(count+" left");
				}
			}
			return null;
		}
	}
	private class TreasureLocationDiscoveryRenderer extends DefaultTableCellRenderer {
		private ArrayList list;
		public TreasureLocationDiscoveryRenderer(ArrayList list) {
			this.list = list;
		}
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSel, boolean hasFocus, int row, int column) {
			setForeground(Color.black);
			setBackground(Color.white);
			setText((String)value);
			if (row<list.size()) {
				GameObject tl = (GameObject) list.get(row);
				if (tl.hasThisAttribute(RealmComponent.RED_SPECIAL) && getCharacter().hasOtherChitDiscovery(tl.getName())) {
					setBackground(MagicRealmColor.DISCOVERY_HIGHLIGHT_COLOR);
				}
				else if (getCharacter().hasTreasureLocationDiscovery(tl.getName())) {
					setBackground(MagicRealmColor.DISCOVERY_HIGHLIGHT_COLOR);
				}
			}
			if (column==0) setHorizontalAlignment(SwingConstants.LEFT);
			else setHorizontalAlignment(SwingConstants.CENTER);
			return this;
		}
	}
	private class PathDiscoveryTableModel extends AbstractTableModel {
		private ArrayList list;
		public PathDiscoveryTableModel(ArrayList list) {
			this.list = list;
		}
		public int getRowCount() {
			return list.size();
		}
		public int getColumnCount() {
			return 3;
		}
		public String getColumnName(int column) {
			return "";
		}
		public Class getColumnClass(int column) {
			return String.class;
		}
		public Object getValueAt(int row, int column) {
			if (row<list.size()) {
				PathDetail path = (PathDetail) list.get(row);
				switch(column) {
					case 0:	
						return path.getParent().getTileName();
					case 1:
						return path.getTileSideName();
					case 2:
						return path.getClearingConnectionString();
				}
			}
			return null;
		}
	}
	private class PathDiscoveryRenderer extends DefaultTableCellRenderer {
		private String key;
		private ArrayList list;
		public PathDiscoveryRenderer(String key,ArrayList list) {
			this.key = key;
			this.list = list;
		}
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSel, boolean hasFocus, int row, int column) {
			setForeground(Color.black);
			setBackground(Color.white);
			setText((String)value);
			if (row<list.size()) {
				PathDetail path = (PathDetail) list.get(row);
				if (hasDiscovery(path.getFullPathKey())) {
					setBackground(MagicRealmColor.DISCOVERY_HIGHLIGHT_COLOR);
				}
			}
			return this;
		}
		private boolean hasDiscovery(String name) {
			if (CharacterWrapper.DISC_HIDDEN_PATHS.equals(key)) {
				return getCharacter().hasHiddenPathDiscovery(name);
			}
			return getCharacter().hasSecretPassageDiscovery(name);
		}
	}
}