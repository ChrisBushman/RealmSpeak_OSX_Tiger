package com.robin.game.objects;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

import com.robin.general.swing.DieRoller;
import com.robin.general.swing.DieRollerLoggable;
import com.robin.general.util.RandomNumber;

public class DieRollerLog implements DieRollerLoggable {
	
	private static final String ROLL_LIST = "_rl";
	private static final String REASON_LIST = "_rs";
	
	private boolean dirty = true;
	
	private ArrayList cache;
	
	private GameObject gameObject;
	
	public DieRollerLog(GameObject go) {
		this.gameObject = go;
	}
	public void addDieRoll(DieRoller roll,String reason) {
		dirty = true;
		gameObject.addThisAttributeListItem(ROLL_LIST,roll.getStringResult());
		gameObject.addThisAttributeListItem(REASON_LIST,reason==null?"":reason);
	}
	public int getTotalRolls() {
		ArrayList rolls = gameObject.getThisAttributeList(ROLL_LIST);
		return rolls==null?0:rolls.size();
	}
	public Integer[] getDieMultiples() {
		ArrayList multiples = new ArrayList();
		for (java.util.Iterator _j14it117 = (getDieRollers()).iterator(); _j14it117.hasNext(); ) {
		  DieRoller roller = (DieRoller) _j14it117.next();
			int nod = roller.getNumberOfDice();
			if (!multiples.contains(Integer.valueOf(nod))) {
				multiples.add(Integer.valueOf(nod));
			}
		}
		Collections.sort(multiples);
		return (Integer[]) multiples.toArray(new Integer[multiples.size()]);
	}
	public int getTotalRolls(int numberOfDice) {
		int count=0;
		for (java.util.Iterator _j14it118 = (getDieRollers()).iterator(); _j14it118.hasNext(); ) {
		  DieRoller roller = (DieRoller) _j14it118.next();
			if (roller.getNumberOfDice()==numberOfDice) count++;
		}
		return count;
	}
	public int getTotalDiceRolled() {
		int dice = 0;
		for (java.util.Iterator _j14it119 = (getDieRollers()).iterator(); _j14it119.hasNext(); ) {
		  DieRoller roller = (DieRoller) _j14it119.next();
			dice += roller.getNumberOfDice();
		}
		return dice;
	}
	public int getFrequencyOfDieResult(int result) {
		int frequency = 0;
		for (java.util.Iterator _j14it120 = (getDieRollers()).iterator(); _j14it120.hasNext(); ) {
		  DieRoller roller = (DieRoller) _j14it120.next();
			frequency += roller.getDieResultCount(result);
		}
		return frequency;
	}
	public int getFrequencyOfTotal(int numberOfDice,int result,boolean includeModifier) {
		int frequency = 0;
		for (java.util.Iterator _j14it121 = (getDieRollers()).iterator(); _j14it121.hasNext(); ) {
		  DieRoller roller = (DieRoller) _j14it121.next();
			if (roller.getNumberOfDice()==numberOfDice) {
				int total = roller.getTotal();
				if (!includeModifier) {
					total -= roller.getModifier();
				}
				if (total==result) {
					frequency++;
				}
			}
		}
		return frequency;
	}
	public int getFrequencyOfHighDie(int numberOfDice,int result) {
		int frequency = 0;
		for (java.util.Iterator _j14it122 = (getDieRollers()).iterator(); _j14it122.hasNext(); ) {
		  DieRoller roller = (DieRoller) _j14it122.next();
			if (roller.getNumberOfDice()==numberOfDice) {
				if (roller.getHighDieResult()==result) {
					frequency++;
				}
			}
		}
		return frequency;
	}
	public ArrayList getReasons() {
		return gameObject.getThisAttributeList(REASON_LIST);
	}
	public ArrayList getDieRollers() {
		if (cache==null || dirty) {
			cache = new ArrayList();
			ArrayList rolls = gameObject.getThisAttributeList(ROLL_LIST);
			if (rolls!=null) {
				for (java.util.Iterator _j14it123 = (rolls).iterator(); _j14it123.hasNext(); ) {
				  String roll = (String) _j14it123.next();
					cache.add(new DieRoller(roll));
				}
			}
			dirty = false;
		}
		return cache;
	}
	public String getStandardReport(boolean includeTotals,boolean includeHighDie) {
		NumberFormat percentFormat = NumberFormat.getPercentInstance();
		percentFormat.setMaximumFractionDigits(2);
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("-------------------------\n");
		sb.append("-- DIE ROLL STATISTICS --\n");
		sb.append("-------------------------\n");
		sb.append("Random Number Generator:\n");
		sb.append("      ");
		sb.append(RandomNumber.getRandomNumberGenerator().toString());
		sb.append("\n");
		sb.append("-------------------------\n");
		sb.append("Total rolls: "+getTotalRolls()+"\n");
		sb.append("Total dice: "+getTotalDiceRolled()+"\n");
		sb.append("-------------------------\n");
		for (int i=1;i<=6;i++) {
			int freq = getFrequencyOfDieResult(i);
			double percent = (double)freq/(double)getTotalDiceRolled();
			sb.append(i+"s rolled: "+freq+" or "+percentFormat.format(percent)+" of total dice rolled.\n");
		}
		sb.append("-------------------------\n");
		if (includeTotals) {
			Integer[] _dm124 = getDieMultiples(); for (int _i124=0;_i124<_dm124.length;_i124++) {
			  int nod = _dm124[_i124].intValue();
				if (nod==1) continue;
				int totalRolls = getTotalRolls(nod);
				sb.append("For "+totalRolls+" rolls with "+nod+" dice:\n");
				for (int i=nod;i<=(nod*6);i++) {
					int freq = getFrequencyOfTotal(nod,i,false);
					double percent = (double)freq/(double)totalRolls;
					sb.append(i+"s rolled: "+freq+" or "+percentFormat.format(percent)+" of total rolls.\n");
				}
				sb.append("-------------------------\n");
			}
		}
		if (includeHighDie) {
			Integer[] _dm125 = getDieMultiples(); for (int _i125=0;_i125<_dm125.length;_i125++) {
			  int nod = _dm125[_i125].intValue();
				if (nod==1) continue;
				int totalRolls = getTotalRolls(nod);
				sb.append("For "+totalRolls+" rolls with "+nod+" dice:\n");
				for (int i=1;i<=6;i++) {
					int freq = getFrequencyOfHighDie(nod,i);
					double percent = (double)freq/(double)totalRolls;
					sb.append(i+" was the high die "+percentFormat.format(percent)+" of the time ("+freq+" of "+totalRolls+" rolls)\n");
				}
				sb.append("-------------------------\n");
			}
		}
		return sb.toString();
	}
	public String getAllDieRolls() {
		ArrayList rollers = getDieRollers();
		ArrayList reasons = getReasons();
		if (reasons==null) return "No die rolls have been recorded yet.";
		String defaultReason = "<none>";
		
		int maxReasonLength = defaultReason.length();
		for (int i=0;i<reasons.size();i++){
			maxReasonLength = Math.max(maxReasonLength,((String)reasons.get(i)).length());
		}
		maxReasonLength+=2;
		
		StringBuffer sb = new StringBuffer();
		appendField(sb,"Reason",maxReasonLength);
		sb.append("Total  High\n");
		appendField(sb,"------",maxReasonLength-2,'-');
		sb.append("  -----  -----\n");
		for (int i=0;i<rollers.size();i++){
			DieRoller roller = (DieRoller) rollers.get(i);
			String reason = (String) reasons.get(i);
			appendField(sb,reason.length()==0?"<none>":reason,maxReasonLength);
			appendField(sb,String.valueOf(roller.getTotal()-roller.getModifier()),7);
			appendField(sb,String.valueOf(roller.getHighDieResult()),7);
			for (int n=0;n<roller.getNumberOfDice();n++) {
				sb.append(roller.getValue(n));
				sb.append(" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	private static void appendField(StringBuffer sb,String field,int length) {
		appendField(sb,field,length,' ');
	}
	private static void appendField(StringBuffer sb,String field,int length,char c) {
		sb.append(field);
		while(length-field.length()>0) {
			sb.append(c);
			length--;
		}
	}
	
	public static void main(String[] args) {
		GameObject log = GameObject.createEmptyGameObject();
		DieRollerLog logger = new DieRollerLog(log);
		DieRoller.setDieRollerLog(logger);
		for (int i=0;i<10000;i++) {
			DieRoller roller = new DieRoller();
			roller.addWhiteDie();
			roller.addRedDie();
			roller.rollDice("double");
		}
		for (int i=0;i<100;i++) {
			DieRoller roller = new DieRoller();
			roller.addWhiteDie();
			roller.addRedDie();
			roller.addRedDie();
			roller.rollDice("triple");
		}
		System.out.println(logger.getAllDieRolls());
		System.out.println(logger.getStandardReport(true,true));
	}
}