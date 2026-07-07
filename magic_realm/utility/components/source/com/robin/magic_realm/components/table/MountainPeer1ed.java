package com.robin.magic_realm.components.table;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.robin.general.swing.DieRoller;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class MountainPeer1ed extends Peer1ed {
	
	public MountainPeer1ed(JFrame frame) {
		super(frame);
	}
	public String getTableName(boolean longDescription) {
		return "Peer Neighboring Clearing";
	}
	public String apply(CharacterWrapper character, DieRoller inRoller) {
		TileLocation loc = character.getCurrentLocation();
		if (loc!=null && loc.clearing!=null) {
			for (java.util.Iterator _j14it2133 = (loc.clearing.getDeepClearingComponents()).iterator(); _j14it2133.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it2133.next();
				if (rc.getGameObject().hasThisAttribute(Constants.MIST_CRYSTAL)) {
					return loc.toString()+": Affected by Mist Crystal, Peering not possible";
				}
			}
		}
		TileLocation tl = PeerClearingChooser.chooseClearingFromMountain(getParentFrame(), character);
		targetClearing = tl.clearing;
		return tl.toString()+": "+super.apply(character,inRoller);
	}
	protected ArrayList getHintIcons(CharacterWrapper character) {
		return new ArrayList();
	}
}