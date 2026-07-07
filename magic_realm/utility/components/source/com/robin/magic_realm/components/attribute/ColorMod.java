package com.robin.magic_realm.components.attribute;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.robin.game.objects.GameObject;
import com.robin.general.util.HashLists;

public class ColorMod {
	
	private HashLists conversions;
	
	private ColorMod(String mod) {	
		setMod(mod);
	}
	public boolean willAffect(ColorMagic cm) {
		return conversions.containsKey(Integer.valueOf(cm.getColorNumber()));
	}
	private void setMod(String mod) {
		// Like 1.2;1.3 (means white->grey, white->gold)
		boolean error = false;
		conversions = new HashLists();
		StringTokenizer changes = new StringTokenizer(mod,";");
		try {
			while(changes.hasMoreTokens()) {
				String change = changes.nextToken();
				int dot = change.indexOf('.');
				int fromColor = Integer.parseInt(change.substring(0,dot));
				int toColor = Integer.parseInt(change.substring(dot+1));
				conversions.put(Integer.valueOf(fromColor),Integer.valueOf(toColor));
			}
		}
		catch(IndexOutOfBoundsException ex) {
			error = true;
		}
		catch(NumberFormatException ex){
			error = true;
		}
		if (error || conversions.isEmpty()) {
			throw new IllegalArgumentException("Cannot parse argument mod: "+mod);
		}
	}
	
	public ColorMagic convertColor(ColorMagic cm) {
		if (cm!=null && conversions.containsKey(Integer.valueOf(cm.getColorNumber()))) {
			for (java.util.Iterator _j14it1461 = (conversions.getList(Integer.valueOf(cm.getColorNumber()))).iterator(); _j14it1461.hasNext(); ) {
			  int toColorNumber = ((Integer) _j14it1461.next()).intValue();
				return new ColorMagic(toColorNumber,cm.isInfinite()); // Just return the first in this case...
			}
		}
		return null;
	}
	
	public ArrayList getModifiedColors(ArrayList colors) {
		ArrayList modColors = new ArrayList();
		
		for (java.util.Iterator _j14it1462 = (colors).iterator(); _j14it1462.hasNext(); ) {
		  ColorMagic fromColor = (ColorMagic) _j14it1462.next();
			if (conversions.containsKey(Integer.valueOf(fromColor.getColorNumber()))) {
				for (java.util.Iterator _j14it1463 = (conversions.getList(Integer.valueOf(fromColor.getColorNumber()))).iterator(); _j14it1463.hasNext(); ) {
				  int toColorNumber = ((Integer) _j14it1463.next()).intValue();
					modColors.add(new ColorMagic(toColorNumber,fromColor.isInfinite()));
				}
			}
		}
		return modColors;
	}

	private ArrayList stripConvertedColors(ArrayList colors) {
		ArrayList filteredColors = new ArrayList();
		for (java.util.Iterator _j14it1464 = (colors).iterator(); _j14it1464.hasNext(); ) {
		  ColorMagic magic = (ColorMagic) _j14it1464.next();
			if (!conversions.containsKey(Integer.valueOf(magic.getColorNumber()))) {
				filteredColors.add(magic);
			}
		}
		return filteredColors;
	}
	
	public static ArrayList getConvertedColorsForThings(ArrayList things,ArrayList colors) {
		ArrayList list = createColorMods(things);
		if (!list.isEmpty()) {
			ArrayList modified = new ArrayList();
			for (java.util.Iterator _j14it1465 = (list).iterator(); _j14it1465.hasNext(); ) {
			  ColorMod mod = (ColorMod) _j14it1465.next();
				for (java.util.Iterator _j14it1466 = (mod.getModifiedColors(colors)).iterator(); _j14it1466.hasNext(); ) {
				  ColorMagic magic = (ColorMagic) _j14it1466.next();
					if (!magic.isInfinite() || !modified.contains(magic)) {
						modified.add(magic);
					}
				}
			}
			for (java.util.Iterator _j14it1467 = (list).iterator(); _j14it1467.hasNext(); ) {
			  ColorMod mod = (ColorMod) _j14it1467.next();
				colors = mod.stripConvertedColors(colors);
			}
			colors.addAll(modified);
		}
		return colors;
	}
	private static ArrayList createColorMods(ArrayList things) {
		ArrayList list = new ArrayList();
		for (java.util.Iterator _j14it1468 = (things).iterator(); _j14it1468.hasNext(); ) {
		  GameObject thing = (GameObject) _j14it1468.next();
			ColorMod mod = createColorMod(thing);
			if (mod!=null) {
				list.add(mod);
			}
		}
		return list;
	}
	public static ColorMod createColorMod(GameObject thing) {
		ColorMod ret = null;
		if (thing.hasThisAttribute("color_mod")) {
			ret = new ColorMod(thing.getThisAttribute("color_mod"));
		}
		return ret;
	}
	public static ColorMod createColorMod(String colorMod) {
		return new ColorMod(colorMod);
	}
}