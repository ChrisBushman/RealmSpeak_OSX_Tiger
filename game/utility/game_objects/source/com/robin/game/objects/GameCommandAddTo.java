package com.robin.game.objects;

import java.util.ArrayList;

public class GameCommandAddTo extends GameCommand {

	public static String NAME = "Add";
	
	public GameCommandAddTo(GameSetup gameSetup) {
		super(gameSetup);
	}
	public String getTypeName() {
		return NAME;
	}
	protected String process(ArrayList allGameObjects) {
		GamePool fromPool = parent.getPool(from);
		return addTo(fromPool,allGameObjects);
	}
	public String addTo(GamePool fromPool,ArrayList allGameObjects) {
		// First find the targetObject copy
		GameObject targetObjectCopy = null;
		for (java.util.Iterator _j14it82 = (allGameObjects).iterator(); _j14it82.hasNext(); ) {
		  GameObject copyObject = (GameObject) _j14it82.next();
			if (copyObject.equalsId(targetObject.getId())) {
				targetObjectCopy = copyObject;
				break;
			}
		}
		
		// Now, populate the contains of the copy
		ArrayList picked = fromPool.pick(count,transferType);
		if (picked!=null && targetObjectCopy!=null) {
			for (java.util.Iterator _j14it83 = (picked).iterator(); _j14it83.hasNext(); ) {
			  GameObject obj = (GameObject) _j14it83.next();
				targetObjectCopy.add(obj);
			}
			return "Picked:  "+picked.size()+":  "+from+"="+fromPool.size()+"\n";
		}
		return "Picked nothing";
	}
	public boolean usesFrom() {
		return true;
	}
	public boolean usesTargetObject() {
		return true;
	}
	public boolean usesCount() {
		return true;
	}
	public boolean usesTransferType() {
		return true;
	}
}