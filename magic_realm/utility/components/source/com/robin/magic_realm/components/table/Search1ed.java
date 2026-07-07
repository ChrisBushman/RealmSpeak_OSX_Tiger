package com.robin.magic_realm.components.table;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.robin.magic_realm.components.*;
import com.robin.magic_realm.components.swing.PathIcon;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class Search1ed extends Search {
	public Search1ed(JFrame frame) {
		this(frame,null);
	}
	public Search1ed(JFrame frame,ClearingDetail clearing) {
		super(frame,clearing);
	}
	public String getTableName(boolean longDescription) {
		return "Search"+(longDescription?"\n(Glimpse, Secret Passages, Hidden Paths, Hidden Enemies)":"");
	}
	public String getTableKey() {
		return "Search1ed";
	}
	public String applyOne(CharacterWrapper character) {
		return doChoice1ed(character);
	}

	public String applyTwo(CharacterWrapper character) {
		return doGlimpse(character);
	}

	public String applyThree(CharacterWrapper character) {
		return doPassages(character);
	}

	public String applyFour(CharacterWrapper character) {
		return doPaths(character);
	}

	public String applyFive(CharacterWrapper character) {
		if (character.affectedByKey(Constants.ADVENTURE_GUIDE)) {
			doPassages(character);
		}
		if (character.affectedByKey(Constants.TRAVELERS_GUIDE)) {
			doPaths(character);
		}
		return doHiddenEnemies(character);
	}

	public String applySix(CharacterWrapper character) {
		return "Nothing";
	}
	
	protected ArrayList getHintIcons(CharacterWrapper character) {
		ArrayList list = new ArrayList();
		for (java.util.Iterator _j14it2071 = (getAllUndiscoveredPassages(character)).iterator(); _j14it2071.hasNext(); ) {
		  PathDetail path = (PathDetail) _j14it2071.next();
			list.add(new PathIcon(path));
		}
		for (java.util.Iterator _j14it2072 = (getAllUndiscoveredPaths(character)).iterator(); _j14it2072.hasNext(); ) {
		  PathDetail path = (PathDetail) _j14it2072.next();
			list.add(new PathIcon(path));
		}
		for (java.util.Iterator _j14it2073 = (getAllDiscoverableChits(character,true)).iterator(); _j14it2073.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it2073.next();
			list.add(getIconForSearch(rc));
		}
		return list;
	}
}