package com.robin.game.GameSetupEncoder;

import java.io.*;
import java.util.*;

import com.robin.game.objects.*;
import com.robin.general.util.*;

public class Encoder {

	protected GameData data;
	protected ArrayList printGroupings;
	protected ArrayList codings;
	protected Collection filter = null;
	
	protected static final String LINE_END = "\r\n";
	
	public Encoder() {
		printGroupings = new ArrayList();
		codings = new ArrayList();
	}
	public GameData getGameData() {
		return data;
	}
	public void loadFromPath(String xmlPathname) {
		data = new GameData();
		File xmlFile = new File(xmlPathname);
		System.out.println("Loading "+xmlFile.getPath());
		data.loadFromFile(xmlFile);
		System.out.println("Loaded "+data.getGameObjects().size()+" objects.");
	}
	public void loadFromStream(InputStream stream) {
		data = new GameData();
		data.loadFromStream(stream);
		System.out.println("Loaded "+data.getGameObjects().size()+" objects.");
	}
	public void addPrintGrouping(PrintGrouping grouping) {
		printGroupings.add(grouping);
	}
	public void addCoding(Coding coding) {
		codings.add(coding);
	}
	public void setFilter(Collection keyVals) {
		filter = keyVals;
	}
	public boolean writeFile(String setupName,String filename) {
		StringBuffer printString = new StringBuffer();

		ArrayList query = new ArrayList();
		query.add("original_game");
	
		ArrayList list = data.doSetup(setupName,query);
		System.out.println("Finished setup");
		if (list!=null) {
			GamePool pool = new GamePool(list);
			if (filter!=null) {
				pool = new GamePool(pool.extract(filter));
			}
			
			printString.append(StringUtilities.getRepeatString("*",79));
			printString.append(LINE_END);
			printString.append("**  Result of game setup \""+setupName+"\":"+LINE_END);
			printString.append(StringUtilities.getRepeatString("*",79));
			printString.append(LINE_END);
			printString.append(LINE_END);
			
			// Encode objects
			StringBuffer codingResult = new StringBuffer();
			for (java.util.Iterator _j14it73 = (codings).iterator(); _j14it73.hasNext(); ) {
			  Coding coding = (Coding) _j14it73.next();
				codingResult.append(coding.encode(pool));
			}
			
			// Lay out groups
			for (java.util.Iterator _j14it74 = (printGroupings).iterator(); _j14it74.hasNext(); ) {
			  PrintGrouping grouping = (PrintGrouping) _j14it74.next();
				printString.append(grouping.print(pool));
			}
			
			printString.append(LINE_END);
			printString.append(StringUtilities.getRepeatString("*",79));
			printString.append(LINE_END);
			printString.append("**  Code translations:"+LINE_END);
			printString.append(StringUtilities.getRepeatString("*",79));
			printString.append(LINE_END);
			printString.append(LINE_END);
			
			// Print coding
			printString.append(codingResult.toString());
		}
		
		// Save to file
		try {
			PrintStream stream = new PrintStream(new FileOutputStream(new File(filename)));
			stream.print(printString);
			stream.close();
			return true;
		}
		catch(FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	public static void main(String[]args) {
	}
}