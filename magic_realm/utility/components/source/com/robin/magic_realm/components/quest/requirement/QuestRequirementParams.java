package com.robin.magic_realm.components.quest.requirement;

import java.util.ArrayList;

import com.robin.game.objects.GameData;
import com.robin.game.objects.GameObject;
import com.robin.general.util.StringUtilities;
import com.robin.magic_realm.components.attribute.ColorMagic;
import com.robin.magic_realm.components.quest.*;

public class QuestRequirementParams {
	// General
	public GamePhaseType timeOfCall = GamePhaseType.Unspecified; // default
	public String actionName;
	public CharacterActionType actionType = CharacterActionType.Unknown; // default
	public String dayKey;
	public ArrayList objectList =  new ArrayList();
	public ColorMagic burnedColor;
	
	// Search stuff
	public int dieResult;
	public SearchResultType searchType = SearchResultType.Any;
	public GameObject targetOfSearch;
	public boolean searchHadAnEffect;
	
	public QuestRequirementParams copy(GameData gameData) {
		return valueOf(asString(),gameData);
	}
	
	public void clearTables() {
		actionName = null;
		actionType = CharacterActionType.Unknown;
		dieResult = -1;
		searchType = null;
		objectList.clear();
		searchHadAnEffect = false;
		targetOfSearch = null;
	}
	public String asString() {
		ArrayList list = new ArrayList();
		list.add(timeOfCall.toString());
		list.add(actionName);
		list.add(dayKey);
		list.add(actionType.toString());
		list.add(String.valueOf(dieResult));
		list.add(searchType == null ? SearchResultType.Any.toString() : searchType.toString());
		list.add(searchHadAnEffect?"T":"F");
		list.add(targetOfSearch==null?"null":targetOfSearch.getStringId());
		if (objectList!=null) {
			for (java.util.Iterator _j14it2340 = (objectList).iterator(); _j14it2340.hasNext(); ) {
			  GameObject res = (GameObject) _j14it2340.next();
				if (res!=null) list.add(res.getStringId());
			}
		}
		return StringUtilities.collectionToString(list,"@");
	}
	public static QuestRequirementParams valueOf(String s,GameData gameData) {
		ArrayList list = StringUtilities.stringToCollection(s,"@",true);
		QuestRequirementParams qp = new QuestRequirementParams();
		if (list.size()>=8) {
			qp.timeOfCall = GamePhaseType.valueOf((String) list.get(0));
			qp.actionName = list.get(1)==null?null:(String) list.get(1);
			qp.dayKey = list.get(2)==null?null:(String) list.get(2);
			qp.actionType = CharacterActionType.valueOf((String) list.get(3));
			qp.dieResult = Integer.parseInt((String) list.get(4));
			qp.searchType = SearchResultType.valueOf((String) list.get(5));
			qp.searchHadAnEffect = "T".equals(list.get(6));
			qp.targetOfSearch = readGameObject((String) list.get(7),gameData);
		}
		if (list.size()>8) {
			for (java.util.Iterator _j14it2341 = (list.subList(8,list.size())).iterator(); _j14it2341.hasNext(); ) {
			  String val = (String) _j14it2341.next();
				qp.objectList.add(readGameObject(val,gameData));
			}
		}
		return qp;
	}
	private static GameObject readGameObject(String val,GameData gameData) {
		if (val==null) return null;
		return gameData.getGameObject(Long.valueOf(val));
	}
}