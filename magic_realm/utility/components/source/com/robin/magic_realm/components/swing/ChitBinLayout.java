package com.robin.magic_realm.components.swing;

import java.awt.Point;
import java.util.*;

import com.robin.general.util.HashLists;
import com.robin.magic_realm.components.CharacterActionChitComponent;
import com.robin.magic_realm.components.ChitComponent;
import com.robin.magic_realm.components.StateChitComponent;

public class ChitBinLayout {
	public static final int INNER_CELL_SPACE = 3;
	private static final String[] GROUP = {
		"FLY",
		"OTHER",
		"M/F",
		"MOVE",
		"FIGHT",
		"MAGIC",
	};
	
	private ArrayList groups;
	private ArrayList chitBins;
	private HashLists hashLists;
	
	public ChitBinLayout(ArrayList chits) {
		Collections.sort(chits);
		groups = new ArrayList();
		chitBins = new ArrayList();
		hashLists = new HashLists();
		for (java.util.Iterator _j14it2002 = (chits).iterator(); _j14it2002.hasNext(); ) {
		  ChitComponent chit = (ChitComponent) _j14it2002.next();
			if (chit.isActionChit()) {
				CharacterActionChitComponent achit = (CharacterActionChitComponent)chit;
				if (achit.isMoveFight()) {
					addChit("M/F",achit);
				}
				else if (achit.isMove()) {
					addChit("MOVE",achit);
				}
				else if (achit.isFight() || achit.isFightAlert() || achit.isReflex()) {
					addChit("FIGHT",achit);
				}
				else if (achit.isFly()) {
					addChit("FLY",achit);
				}
				else if (achit.isMagic()) {
					addChit("MAGIC",achit);
				}
				else {
					addChit("OTHER",achit);
				}
			}
			else {
				// Spell Fly chit
				addChit("FLY",null);
			}
		}
		
		ArrayList sorted = new ArrayList(Arrays.asList(GROUP));
		sorted.retainAll(groups);
		groups = sorted;
	}
	public ArrayList getGroups() {
		return groups;
	}
	public ArrayList getBins(String group) {
		return hashLists.getList(group);
	}
	public ChitComponent getChit(int index) {
		if (index<0 || index>=chitBins.size()) {
			throw new IllegalStateException("No chit bin at position " + index);
		}
		ChitBin bin = (ChitBin) chitBins.get(index);
		return bin.getChit();
	}
	public void setChit(int index,ChitComponent chit) {
		if (index<0 || index>=chitBins.size()) {
			throw new IllegalStateException("No chit bin at position " + index);
		}
		ChitBin bin = (ChitBin) chitBins.get(index);
		bin.setChit(chit);
	}
	public ArrayList getAllChits() {
		ArrayList list = new ArrayList();
		for (java.util.Iterator _j14it2003 = (chitBins).iterator(); _j14it2003.hasNext(); ) {
		  ChitBin bin = (ChitBin) _j14it2003.next();
			ChitComponent chit = bin.getChit();
			if (chit!=null) {
				list.add(chit);
			}
		}
		return list;
	}
	public ArrayList getColorChits() {
		ArrayList list = new ArrayList();
		for (java.util.Iterator _j14it2004 = (chitBins).iterator(); _j14it2004.hasNext(); ) {
		  ChitBin bin = (ChitBin) _j14it2004.next();
			ChitComponent chit = bin.getChit();
			if (chit!=null && chit instanceof CharacterActionChitComponent && ((CharacterActionChitComponent)chit).isColorOnlyChit()) {
				list.add(chit);
			}
		}
		return list;
	}
	private void addChit(String type,CharacterActionChitComponent chit) {
		ChitBin bin = new ChitBin();
		if (chit!=null && (chit.isMagic() || chit.isColorOnlyChit())) {
			bin.setColorMagic(chit.getEnchantedColorMagic());
		}
		hashLists.put(type, bin);
		chitBins.add(bin);
		if (!groups.contains(type)) {
			groups.add(type);
		}
	}
	public void reset() {
		for (java.util.Iterator _j14it2005 = (chitBins).iterator(); _j14it2005.hasNext(); ) {
		  ChitBin bin = (ChitBin) _j14it2005.next();
			bin.setChit(null);
		}
	}
	public ChitComponent getChitAt(Point p) {
		for (java.util.Iterator _j14it2006 = (chitBins).iterator(); _j14it2006.hasNext(); ) {
		  ChitBin bin = (ChitBin) _j14it2006.next();
			if (bin.getRectangle().contains(p)) {
				return bin.getChit();
			}
		}
		return null;
	}
	public int getChitIndex(ChitComponent chit) {
		for (int i=0;i<chitBins.size();i++) {
			ChitBin bin = (ChitBin) chitBins.get(i);
			if (chit==bin.getChit()) { // testing pointer equality is good enough for here
				return i;
			}
		}
		return -1;
	}
}