package com.robin.magic_realm.components.utility;

import java.util.Comparator;
import com.robin.game.objects.GameObject;

public class NativeHireOrder implements Comparator {

	public int compare(Object o1, Object o2) {
		GameObject n1 = (GameObject) o1;
		GameObject n2 = (GameObject) o2;
		String rs1 = n1.getThisAttribute("rank");
		if (rs1==null) rs1 = "0";
		int rank1 = "HQ".equals(rs1)?0:Integer.parseInt(rs1);

		String rs2 = n2.getThisAttribute("rank");
		if (rs2==null) rs2 = "0";
		int rank2 = "HQ".equals(rs2)?0:Integer.parseInt(rs2);

		return rank2 - rank1;
	}

}
