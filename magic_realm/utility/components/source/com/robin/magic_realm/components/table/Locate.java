package com.robin.magic_realm.components.table;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.robin.magic_realm.components.*;
import com.robin.magic_realm.components.swing.PathIcon;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class Locate extends Search {
	public Locate(JFrame frame) {
		this(frame,null);
	}
	public Locate(JFrame frame,ClearingDetail clearing) {
		super(frame,clearing);
	}
	public String getTableName(boolean longDescription) {
		return "Locate"+(longDescription?"\n(Secret Passages, Treasure Locations)":"");
	}
	public String getTableKey() {
		return "Locate";
	}
	public String applyOne(CharacterWrapper character) {
		// Choice
		return doChoice(character);
	}

	public String applyTwo(CharacterWrapper character) {
		// Clues, Passages
		doClues(character);
		String passageRes = doPassages(character);
		if (passageRes!=null) {
			return "Clues and "+passageRes;
		}
		return "Clues";
	}

	public String applyThree(CharacterWrapper character) {
		// Passages
		return doPassages(character);
	}

	public String applyFour(CharacterWrapper character) {
		// Discover Chits
		return doDiscoverChits(character);
	}

	public String applyFive(CharacterWrapper character) {
		if (character.affectedByKey(Constants.ADVENTURE_GUIDE)) {
			doPassages(character);
		}
		if (character.affectedByKey(Constants.TRAVELERS_GUIDE)) {
			doPaths(character);
		}
		// Nothing
		return "Nothing";
	}

	public String applySix(CharacterWrapper character) {
		// Nothing
		return "Nothing";
	}
	
	protected ArrayList getHintIcons(CharacterWrapper character) {
		ArrayList list = new ArrayList();
		for (java.util.Iterator _j14it2176 = (getAllUndiscoveredPassages(character)).iterator(); _j14it2176.hasNext(); ) {
		  PathDetail path = (PathDetail) _j14it2176.next();
			list.add(new PathIcon(path));
		}
		for (java.util.Iterator _j14it2177 = (getAllDiscoverableChits(character,true)).iterator(); _j14it2177.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it2177.next();
			list.add(getIconForSearch(rc));
		}
		return list;
	}
}