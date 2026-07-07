package com.robin.magic_realm.RealmGm;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class RockerSwitch extends JPanel {
	
	private JToggleButton offButton;
	private JToggleButton onButton;
	
	private ArrayList actionListeners;
	
	public RockerSwitch() {
		this(false);
	}
	public boolean isOn() {
		return onButton.isSelected();
	}
	public void doClickOn() {
		onButton.doClick();
	}
	public void doClickOff() {
		offButton.doClick();
	}
	public RockerSwitch(boolean defaultOn) {
		initComponents(defaultOn);
		actionListeners = new ArrayList();
	}
	public void addActionListener(ActionListener listener) {
		actionListeners.add(listener);
	}
	public void removeActionListener(ActionListener listener) {
		actionListeners.remove(listener);
	}
	protected void fireActionPerformed() {
		ActionEvent ev = new ActionEvent(this,0,"");
		for (java.util.Iterator _j14it212 = (actionListeners).iterator(); _j14it212.hasNext(); ) {
		  ActionListener listener = (ActionListener) _j14it212.next();
			listener.actionPerformed(ev);
		}
	}
	private void initComponents(boolean defaultOn) {
		setLayout(new GridLayout(1,2));
		offButton = new JToggleButton("OFF",!defaultOn);
		offButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				onButton.setSelected(!offButton.isSelected());
				fireActionPerformed();
			}
		});
		add(offButton);
		onButton = new JToggleButton("ON",defaultOn);
		onButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				offButton.setSelected(!onButton.isSelected());
				fireActionPerformed();
			}
		});
		add(onButton);
	}
	public static void main(String[] args) {
		JOptionPane.showMessageDialog(new JFrame(),new RockerSwitch(true));
	}
}