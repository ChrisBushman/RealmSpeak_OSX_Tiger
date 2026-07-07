package com.robin.magic_realm.components.utility;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.*;

public class DieRuleTest extends TestBaseWithLoader {
	
	@Test
	public void testOneDie() {
		DieRule dr = new DieRule(null,"1d:hide:all");
		Assert.assertTrue(dr.isOneDie());
	}

	@Test
	public void testMinusOne() {
		DieRule dr = new DieRule(null,"-1:hide:all");
		Assert.assertTrue(dr.isMinusOne());
	}

	@Test
	public void testPlusOne() {
		DieRule dr = new DieRule(null,"+1:hide:all");
		Assert.assertTrue(dr.isPlusOne());
	}
	
	@Test
	public void testMinusTwo() {
		DieRule dr = new DieRule(null,"-2:peer:all");
		Assert.assertTrue(dr.isMinusTwo());
	}
	
	@Test
	public void testRuinsTileName() {
		GameObject go = findGameObject("Ruins");
		TileComponent tile = (TileComponent)RealmComponent.getRealmComponent(go);
		ArrayList list = tile.getChitDescriptionList();
		DieRule dr = new DieRule(null,"-1:locate:%ruins%");
		Assert.assertTrue(dr.conditionsMet("locate",list));
	}
	@Test
	public void testRuinsChit() {
		ArrayList list = new ArrayList();
		list.add("cliff");
		list.add("ruins m");
		list.add("flutter");
		DieRule dr = new DieRule(null,"-1:locate:%ruins%");
		Assert.assertTrue(dr.conditionsMet("locate",list));
		
	}
	@Test
	public void testLostCityChit() {
		ArrayList list = new ArrayList();
		list.add("cliff");
		list.add("lost city b");
		list.add("flutter");
		DieRule dr = new DieRule(null,"-1:locate:lost city%");
		Assert.assertTrue(dr.conditionsMet("locate",list));
	}
	@Test
	public void testWoodsTile() {
		GameObject go = findGameObject("Deep Woods");
		TileComponent tile = (TileComponent)RealmComponent.getRealmComponent(go);
		ArrayList list = tile.getChitDescriptionList();
		DieRule dr = new DieRule(null,"-1:locate:% woods");
		Assert.assertTrue(dr.conditionsMet("locate",list));
	}
	@Test
	public void testNotWoods() {
		ArrayList list = new ArrayList();
		list.add("cliff");
		list.add("lost city b");
		list.add("flutter");
		list.add("woodsgirl's cache");
		DieRule dr = new DieRule(null,"-1:locate:% woods");
		Assert.assertTrue(!dr.conditionsMet("locate",list));
	}
	@Test
	public void testAllDieModsLists() {
		ArrayList query = new ArrayList();
		query.add(Constants.DIEMOD);
		ArrayList dieModObjs = findGameObjects(query);
		for (java.util.Iterator _j14it1305 = (dieModObjs).iterator(); _j14it1305.hasNext(); ) {
		  GameObject go = (GameObject) _j14it1305.next();
			for (java.util.Iterator _j14it1306 = (go.getAttributeBlockNames()).iterator(); _j14it1306.hasNext(); ) {
			  String blockName = (String) _j14it1306.next();
				if (!go.hasAttribute(blockName,Constants.DIEMOD)) continue;
				go.getAttributeList(blockName,Constants.DIEMOD);
			}
		}
	}
}