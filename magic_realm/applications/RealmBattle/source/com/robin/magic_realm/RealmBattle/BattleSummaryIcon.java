package com.robin.magic_realm.RealmBattle;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.Icon;

public class BattleSummaryIcon implements Icon {
	
	private static final Color color1 = Color.white;
	private static final Color color2 = new Color(220,255,255);
	
	private ArrayList rows;
	
	public BattleSummaryIcon() {
		rows = new ArrayList();
	}
	public BattleSummaryIcon(BattleSummary bs) {
		rows = bs.getSummaryRows(); 
	}
	public void addRow(BattleSummaryRow row) {
		rows.add(row);
	}

	public int getIconHeight() {
		return rows.size()*BattleSummaryRow.HEIGHT;
	}

	public int getIconWidth() {
		return BattleSummaryRow.WIDTH;
	}

	public void paintIcon(Component c, Graphics g1, int x, int y) {
		boolean white = true;
		int n=0;
		Graphics2D g = (Graphics2D)g1;
		for (java.util.Iterator _j14it695 = (rows).iterator(); _j14it695.hasNext(); ) {
		  BattleSummaryRow row = (BattleSummaryRow) _j14it695.next();
			row.draw(g,x,y+n,white?color1:color2);
			n += BattleSummaryRow.HEIGHT;
			white = !white;
		}
	}
}