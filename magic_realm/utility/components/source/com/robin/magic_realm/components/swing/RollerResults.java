package com.robin.magic_realm.components.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.robin.general.swing.ComponentTools;
import com.robin.magic_realm.components.attribute.RollerResult;
import com.robin.magic_realm.components.utility.Constants;

public class RollerResults extends JPanel {
	public static final int ROLLER_OFFSET = 58;
	public static final Color ALTERNATING_COLOR1 = new Color(120,120,255,50);
	public static final Color ALTERNATING_COLOR2 = new Color(120,255,120,100);
	
	private int width;
	private ArrayList battleRolls;
	
	public RollerResults() {
		setOpaque(true);
		setBackground(Color.white);
	}
	public boolean isEmpty() {
		return battleRolls==null || battleRolls.isEmpty();
	}
	
	/**
	 * @param in		Collection of RollerResult objects
	 */
	public void setBattleRolls(ArrayList in) {
		battleRolls = new ArrayList(in);
		ComponentTools.lockComponentSize(this,Constants.COMBAT_SIDEBAR_WIDTH,battleRolls.size()*ROLLER_OFFSET);
		repaint();
	}
	public void paint(Graphics g) {
		super.paint(g);
		Dimension size = getSize();
		width = size.width;
		if (battleRolls!=null && !battleRolls.isEmpty()) {
			int offset = 0;
			boolean altColor = false;
			int px = (width>>1);
			int py = (ROLLER_OFFSET>>1);
			for (java.util.Iterator _j14it2015 = (battleRolls).iterator(); _j14it2015.hasNext(); ) {
			  RollerResult rr = (RollerResult) _j14it2015.next();
				rr.draw(this,g,px,py+offset,true,altColor);
				offset+=ROLLER_OFFSET;
				altColor = !altColor;
			}
		}
	}
}