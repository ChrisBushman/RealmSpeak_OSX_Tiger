package com.robin.general.swing;

import java.util.ArrayList;

public class SingleButtonManager {
	
	private boolean oneShowing;
	private boolean mandatoryShowing;
	private ArrayList buttons;
	
	public SingleButtonManager() {
		buttons = new ArrayList();
	}
	public void addButton(SingleButton button) {
		buttons.add(button);
	}
	public void updateButtonVisibility() {
		oneShowing = false;
		mandatoryShowing = false;
		for (java.util.Iterator _j14it10 = (buttons).iterator(); _j14it10.hasNext(); ) {
		  SingleButton button = (SingleButton) _j14it10.next();
			if (!oneShowing && button.needsShow()) {
				button.setVisible(true);
				oneShowing = true;
				mandatoryShowing = button.isMandatory();
			}
			else {
				button.setVisible(false);
			}
		}
	}
//	public boolean hasOneShowing() {
//		return oneShowing;
//	}
	public boolean hasMandatoryShowing() {
		return mandatoryShowing;
		
	}
}