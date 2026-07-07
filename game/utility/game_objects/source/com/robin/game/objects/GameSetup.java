package com.robin.game.objects;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdom.Attribute;
import org.jdom.Element;

import com.robin.general.io.ModifyableObject;

public class GameSetup extends ModifyableObject implements Serializable {
	public static final String ALL = "ALL";

	protected String name="Untitled Setup";
	protected ArrayList gameCommands;
	
	protected GameData parent;
	
	protected Hashtable pools;
	
	public GameSetup(GameData parentData) {
		parent = parentData;
		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ev) {
				parent.setModified(true);
			}
		});
		gameCommands = new ArrayList();
		reset();
		setModified(true);
	}
	public GameData getGameData() {
		return parent;
	}
	public void reset() {
		gameCommands.clear();
	}
	public void setName(String val) {
		if (val!=null) {
			name = val;
			setModified(true);
		}
	}
	public String getName() {
		return name;
	}
	public String getFullTitle() {
		return parent.getGameName()+":  "+name;
	}
	public Element getXML() {
		Element element = new Element("GameSetup");
		element.setAttribute(new Attribute("name",name));
		
		// Add all commands
		for (java.util.Iterator _j14it127 = (gameCommands).iterator(); _j14it127.hasNext(); ) {
		  GameCommand command = (GameCommand) _j14it127.next();
			element.addContent(command.getXML());
		}
		
		// return
		return element;
	}
	public void setXML(Element element) {
		reset();
		Attribute nameAtt = element.getAttribute("name");
		if (nameAtt!=null) {
			setName(nameAtt.getValue());
		}
		
		// Read all commands
		Collection commands = element.getChildren();
		gameCommands.clear();
		for (java.util.Iterator _j14it128 = (commands).iterator(); _j14it128.hasNext(); ) {
		  Element command = (Element) _j14it128.next();
			GameCommand newCommand = GameCommand.createFromXML(this,command);
//			GameCommand newCommand = new GameCommand(this);
//			newCommand.setXML(command);
			gameCommands.add(newCommand);
		}
		
		setModified(true);
	}
	public String toString() {
		return name;
	}
	public ArrayList getGameCommands() {
		return gameCommands;
	}
	public int getCommandCount() {
		return gameCommands.size();
	}
	public void add(GameCommand command) {
		gameCommands.add(command);
	}
	public boolean removeCommand(GameCommand command) {
		return gameCommands.remove(command);
	}
	public boolean updateCommand(GameCommand original,GameCommand updated) {
		int index = gameCommands.indexOf(original);
		if (index>=0) {
			gameCommands.set(index,updated);
		}
		setModified(true);
		return false;
	}
	public GameCommand createNewCommand() {
		return createNewCommand(-1);
	}
	public GameCommand createNewCommand(int row) {
		GameCommand command = new GameCommandCreate(this);
		if (row==-1) {
			gameCommands.add(command);
		}
		else {
			gameCommands.add(row,command);
		}
		setModified(true);
		return command;
	}
	public void copyCommandsFrom(GameSetup setup) {
		ArrayList commands = setup.getGameCommands();
		for (java.util.Iterator _j14it129 = (commands).iterator(); _j14it129.hasNext(); ) {
		  GameCommand command = (GameCommand) _j14it129.next();
			GameCommand newCommand = GameCommand.getCommandForName(this,command.getTypeName());
			gameCommands.add(newCommand);
			newCommand.copyFrom(command);
		}
		setModified(true);
	}
	public ArrayList processSetup(StringBuffer result,ArrayList gameObjects) {
		pools = new Hashtable();
		pools.put(ALL,new GamePool(gameObjects));
		result.append("Pool ALL was created: "+gameObjects.size()+"\n");
		for (java.util.Iterator _j14it130 = (gameCommands).iterator(); _j14it130.hasNext(); ) {
		  GameCommand command = (GameCommand) _j14it130.next();
			result.append(command.doCommand(gameObjects));
		}
		result.append("\n");
		result.append("---DONE---");
		result.append("\n");
		ArrayList keys = new ArrayList(pools.keySet());
		Collections.sort(keys);
		for (java.util.Iterator _j14it131 = (keys).iterator(); _j14it131.hasNext(); ) {
		  String key = (String) _j14it131.next();
			GamePool pool = (GamePool) pools.get(key);
			result.append(key+": "+pool.size()+" left\n");
		}
		return gameObjects;
	}
	public void createPool(String poolName) {
		if (pools.get(poolName)==null) {
			pools.put(poolName,new GamePool());
		}
	}
	public GamePool getPool(String poolName) {
		return (GamePool) pools.get(poolName);
	}
	public void moveObjectsBefore(ArrayList objects,GameCommand indexObject) {
		moveObjects(objects,indexObject,true);
	}
	public void moveObjectsAfter(ArrayList objects,GameCommand indexObject) {
		moveObjects(objects,indexObject,false);
	}
	/**
	 * Moves the objects to the position BEFORE the GameCommand with an id==idPosition
	 */
	private void moveObjects(ArrayList objects,GameCommand indexObject,boolean before) {
		// First, verify ALL objects are in the list, and that the list is uniqued
		ArrayList validCommands = new ArrayList();
		for (java.util.Iterator _j14it132 = (objects).iterator(); _j14it132.hasNext(); ) {
		  GameCommand command = (GameCommand) _j14it132.next();
			if (command.parent==this && gameCommands.contains(command) && !validCommands.contains(command)) {
				validCommands.add(command);
			}
		}
		if (validCommands.size()!=objects.size()) {
			throw new IllegalStateException("Invalid object set to move!");
		}
		
		// Find the index of the specified id
		if (indexObject==null) {
			throw new IllegalStateException("Invalid indexObject!");
		}
		
		// Remove all valid objects
		gameCommands.removeAll(validCommands);
		
		int index = gameCommands.indexOf(indexObject);
		if (!before) index++;
		
		// Reinsert into specified position
		gameCommands.addAll(index,validCommands);
	}
	// Serializable interface
	private static void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}
	private static void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
	}
	
	public void expandSetup(ArrayList nameAppends) {
		expandSetup(nameAppends,new ArrayList(),null);
	}
	public void expandSetup(ArrayList nameAppends,ArrayList tiedPools,String tiedKey) { // Hey!  Tide Pools!
		if (tiedPools==null || (!tiedPools.isEmpty() && tiedKey==null)) {
			throw new IllegalArgumentException("Invalid use of expandSetup: "+tiedPools+","+tiedKey);
		}
		ArrayList expanded = new ArrayList();
		for (java.util.Iterator _j14it133 = (gameCommands).iterator(); _j14it133.hasNext(); ) {
		  GameCommand command = (GameCommand) _j14it133.next();
			expanded.add(command);
			if (command.usesTargetObject()) {
				// Duplicate for each append, but locate a new targetObject using nameAppend
				for (java.util.Iterator _j14it134 = (nameAppends).iterator(); _j14it134.hasNext(); ) {
				  String nameAppend = (String) _j14it134.next();
					GameCommand dupCommand = GameCommand.getCommandForName(this,command.getTypeName());
					dupCommand.copyFrom(command);
					GameObject targObj = command.getTargetObject();
					GameObject newTargObj = parent.getGameObjectByName(targObj.getName()+nameAppend);
					dupCommand.setTargetObject(newTargObj);
					if (dupCommand.usesFrom() && tiedPools.contains(dupCommand.getFrom())) {
						dupCommand.setFrom(dupCommand.getFrom()+nameAppend);
					}
					expanded.add(dupCommand);
				}
			}
			else if (command.isCreate() && tiedPools.contains(command.getNewPool())) {
				for (java.util.Iterator _j14it135 = (nameAppends).iterator(); _j14it135.hasNext(); ) {
				  String nameAppend = (String) _j14it135.next();
					GameCommand dupCommand = GameCommand.getCommandForName(this,command.getTypeName());
					dupCommand.copyFrom(command);
					dupCommand.setNewPool(command.getNewPool()+nameAppend);
					expanded.add(dupCommand);
				}
			}
			else if (command.isExtract() && tiedPools.contains(command.getTo())) {
				for (java.util.Iterator _j14it136 = (nameAppends).iterator(); _j14it136.hasNext(); ) {
				  String nameAppend = (String) _j14it136.next();
					GameCommand dupCommand = GameCommand.getCommandForName(this,command.getTypeName());
					dupCommand.copyFrom(command);
					dupCommand.setTo(command.getTo()+nameAppend);
					dupCommand.addKeyVal(tiedKey,nameAppend.trim());
					expanded.add(dupCommand);
				}
				command.addKeyVal("!"+tiedKey);
			}
			else if (command.usesCount()) {
				// Don't duplicate, but multiply the count
				command.setCount(command.getCount()*(nameAppends.size()+1));
			}
		}
		gameCommands.clear();
		gameCommands = expanded;
	}
}