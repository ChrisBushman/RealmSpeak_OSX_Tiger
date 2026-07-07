package com.robin.game.server;

import java.util.ArrayList;

public class InfoObject {
	private boolean forHost;
	private String destClientName;
	private ArrayList info; // list of Strings

	public InfoObject(String destClientName, ArrayList info) {
		this.destClientName = destClientName;
		this.info = new ArrayList(info);
		this.forHost = destClientName==null;
	}
	public boolean isForHost() {
		return forHost;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("destClientName=");
		sb.append(destClientName);
		sb.append(", info=");
		sb.append(info);
		return sb.toString();
	}

	public String getDestClientName() {
		return destClientName;
	}

	public ArrayList getInfo() {
		return info;
	}
}