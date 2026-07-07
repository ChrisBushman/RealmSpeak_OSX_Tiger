package com.robin.magic_realm.components.quest;

import java.util.ArrayList;

import javax.swing.JFrame;

import com.robin.game.objects.*;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class QuestBookEvents extends GameObjectWrapper {
	
	private static String QUEST_BOOK_KEY = "__qb_key_";
	private static String QUEST_EVENT_LIST = "_q_ev_lst";
	private static String QUEST_UNIQUE_ID_GENERATOR = "_uidg";
	private static String QUEST_EVENT_TEMPLATE = "_q_ev_template";
	
	public QuestBookEvents(GameObject go) {
		super(go);
	}
	public String getBlockName() {
		return "QuestBookEvents";
	}
	private int generateUniqueId() {
		int id = getInt(QUEST_UNIQUE_ID_GENERATOR);
		setInt(QUEST_UNIQUE_ID_GENERATOR,id+1);
		return id;
	}
	
	public void addEvent(Quest quest) {
		quest.setBoolean(QUEST_EVENT_TEMPLATE,true);
		quest.setInt(Quest.QUEST_UNIQUE_ID,generateUniqueId()); // All events get a unique id on entry.
		addListItem(QUEST_EVENT_LIST,quest.getGameObject().getStringId());
	}
	
	public void setupEvents(JFrame frame,CharacterWrapper character) {
		for (java.util.Iterator _j14it2288 = (getEvents()).iterator(); _j14it2288.hasNext(); ) {
		  Quest card = (Quest) _j14it2288.next();
			if (card.getState()!=QuestState.New && !card.isMultipleUse()) continue; // skip all play cards that are no longer new (completed or failed)
			Quest quest = card.copyQuestToGameData(getGameData());
			quest.setState(QuestState.Assigned, character.getCurrentDayKey(), character); // indicates when the quest was first assigned
			character.addQuest(frame,quest);
		}
	}
	private ArrayList getEventsAsObjects() {
		ArrayList allPlay = new ArrayList();
		ArrayList list = getList(QUEST_EVENT_LIST);
		if (list!=null && list.size()>0) {
			for (java.util.Iterator _j14it2289 = (list).iterator(); _j14it2289.hasNext(); ) {
			  String questId = (String) _j14it2289.next();
				GameObject go = getGameData().getGameObject(new Long(questId));
				allPlay.add(go);
			}
		}
		return allPlay;
	}
	private ArrayList getEvents() {
		ArrayList events = new ArrayList();
		for (java.util.Iterator _j14it2290 = (getEventsAsObjects()).iterator(); _j14it2290.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2290.next();
			Quest quest = new Quest(go);
			events.add(quest);
		}
		return events;
	}
	
	public ArrayList getAllEventNames() {
		ArrayList events = new ArrayList();
		for (java.util.Iterator _j14it2291 = (getEventsAsObjects()).iterator(); _j14it2291.hasNext(); ) {
		  GameObject go = (GameObject) _j14it2291.next();
			events.add(go.getName());
		}
		return events;
	}
	
	public static Long BOOK_ID = null;
	public static QuestBookEvents findBook(GameData data) {
		if (BOOK_ID==null) {
			GamePool pool = new GamePool(data.getGameObjects());
			GameObject go = pool.findFirst(QUEST_BOOK_KEY);
			if (go!=null) {
				BOOK_ID = new Long(go.getId());
				return new QuestBookEvents(go);
			}
		}
		else {
			return new QuestBookEvents(data.getGameObject(BOOK_ID));
		}
		
		// None found?  Better make one.
		GameObject go = data.createNewObject();
		go.setName("Created by QuestBookEvents");
		go.setThisAttribute(QUEST_BOOK_KEY);
		
		QuestBookEvents book = new QuestBookEvents(go);
		BOOK_ID = new Long(go.getId());
		
		return book;
	}
}