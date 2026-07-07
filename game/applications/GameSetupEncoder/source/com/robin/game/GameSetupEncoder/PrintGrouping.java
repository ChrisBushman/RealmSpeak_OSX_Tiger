package com.robin.game.GameSetupEncoder;

import java.util.*;

import com.robin.general.util.*;
import com.robin.game.objects.*;

public class PrintGrouping extends Properties {
	
	protected String groupName;

	public PrintGrouping(String groupName) {
		this.groupName = groupName;
	}
	public String getName() {
		return groupName;
	}
	public Collection getKeyVals() {
		ArrayList keyVals = new ArrayList();
		for (Enumeration e=keys();e.hasMoreElements();) {
			String key = (String)e.nextElement();
			String val = getProperty(key);
			if (val.trim().length()==0) {
				keyVals.add(key);
			}
			else {
				keyVals.add(key+"="+val);
			}
		}
		return keyVals;
	}
	
	public String print(GamePool pool) {
		StringBuffer sb = new StringBuffer(groupName);
		sb.append(Encoder.LINE_END);
		sb.append(StringUtilities.getRepeatString("-",79));
		sb.append(Encoder.LINE_END);
		
		ArrayList toPrint = pool.find(getKeyVals());
		// Print objects
		for (java.util.Iterator _j14it71 = (toPrint).iterator(); _j14it71.hasNext(); ) {
		  GameObject obj = (GameObject) _j14it71.next();
			sb.append(obj+":  ");
			// Print hold codes
			for (java.util.Iterator _j14it72 = (obj.getHold()).iterator(); _j14it72.hasNext(); ) {
			  GameObject held = (GameObject) _j14it72.next();
				sb.append(Coding.getCode(held));
			}
			sb.append(Encoder.LINE_END);
		}
		sb.append(Encoder.LINE_END);
		return sb.toString();
	}
}