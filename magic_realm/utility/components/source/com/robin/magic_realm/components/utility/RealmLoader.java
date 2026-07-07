package com.robin.magic_realm.components.utility;

import java.util.ArrayList;

import com.robin.game.objects.*;

public class RealmLoader {

	public static String DATA_PATH = "data/MagicRealmData.xml";

	private GameData master; // needed to determine changes
	private GameData data;

	public RealmLoader() {
		master = new GameData();
		master.loadFromPath(DATA_PATH);

		data = new GameData();
		data.loadFromPath(DATA_PATH);
	}
	
	public RealmLoader(GameData data) {
		this.master = data.copy();
		this.data = data.copy();
	}
	
	public void cleanupData(String keyVals) {
		long maxid = master.getMaxId();
		GamePool pool = new GamePool(data.getGameObjects());
		ArrayList found = pool.find(keyVals);
		ArrayList toDelete = new ArrayList();
		for (java.util.Iterator _j14it2683 = (data.getGameObjects()).iterator(); _j14it2683.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2683.next();
			if (go.getId()<=maxid) { // only consider objects in the master
				if (!found.contains(go)) {
					// Make sure it isn't held by...
					GameObject hb = go;
					while(hb.getHeldBy()!=null) {
						hb = hb.getHeldBy();
					}
					if (!found.contains(hb)) {
						toDelete.add(go);
					}
				}
			}
		}
		for (java.util.Iterator _j14it2684 = (toDelete).iterator(); _j14it2684.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2684.next();
			data.removeObject(go);
		}
	}

	public GameData getMaster() {
		return master;
	}

	public GameData getData() {
		return data;
	}
	
	public static void main(String[] args) {
		RealmLoader loader = new RealmLoader();
		GamePool pool = new GamePool(loader.getData().getGameObjects());
		ArrayList query = new ArrayList();
		query.add("rw_expansion_1");
		query.add("treasure");
		String tab = "\t";
		System.out.println(
				"Name"
				+tab+"Great"
				+tab+"Large"
				+tab+"Discard"
				+tab+"Weight"
				+tab+"Fame Reward"
				+tab+"Fame"
				+tab+"Notoriety"
				+tab+"Gold"
				+tab+"Text"
				);
		for (java.util.Iterator _j14it2685 = (pool.find(query)).iterator(); _j14it2685.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2685.next();
			int twt = go.getThisInt("treasure_within_treasure");
			String great = go.hasThisAttribute("great")?"Great":" ";
			String large = twt==0?(go.getThisAttribute("treasure").equals("large")?"Large":" "):("P"+twt);
			String discard = go.getThisAttribute("discard");
			if (discard == null) discard = " ";
			String nat = go.getThisAttribute("native");
			if (nat==null) nat = " ";
			System.out.println(go.getName()
					+tab+great
					+tab+large
					+tab+discard
					+tab+go.getThisAttribute(Constants.WEIGHT)
					+tab+nat
					+tab+go.getThisInt("fame")
					+tab+go.getThisInt("notoriety")
					+tab+go.getThisInt("base_price")
					+tab+go.getThisAttribute("text"));
		}
	}
}