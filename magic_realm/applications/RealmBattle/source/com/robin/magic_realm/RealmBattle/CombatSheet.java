package com.robin.magic_realm.RealmBattle;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import com.robin.game.objects.GameObject;
import com.robin.general.graphics.GraphicsUtil;
import com.robin.general.graphics.TextType;
import com.robin.general.graphics.TextType.Alignment;
import com.robin.general.swing.DieRoller;
import com.robin.general.util.HashLists;
import com.robin.magic_realm.components.*;
import com.robin.magic_realm.components.attribute.RelationshipType;
import com.robin.magic_realm.components.attribute.RollerResult;
import com.robin.magic_realm.components.attribute.TileLocation;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.*;

public abstract class CombatSheet extends JLabel implements Scrollable {
	
	private static final int CHIT_OFFSET = 5;
	protected static final String[] horseRiderSplit = {"w/ Horse","Separate"};
	
	public abstract boolean hasUnpositionedDenizens();
	public abstract boolean usesMaxCombatBoxes();
	public abstract boolean usesCombatBoxesEqually();
	public abstract boolean needsTargetAssignment();
	
	protected abstract Point[] getPositions(HostPrefWrapper hostPrefs);
	protected abstract ImageIcon getImageIcon();
	protected abstract void updateHotSpots();
	protected abstract String[] splitHotSpot(int index);
	protected abstract void updateLayout();
	protected abstract void handleClick(int index,int swingConstant);
	protected abstract void drawRollers(Graphics g);
	protected abstract void drawOther(Graphics g);
	protected abstract int getDeadBoxIndex();
	protected abstract int getBoxIndexFromCombatBoxes(int boxA, int boxD);
	protected abstract int getBoxIndexFromCombatBoxesForDefender(int boxA, int boxD);
	protected abstract int getBoxIndexFromCombatBoxesForDefenderTarget(int boxA, int boxD);
	protected abstract int getBoxIndexFromCombatBoxesForAttacker(int boxA, int boxD);
	protected abstract int getHotSpotSize();
	
	private Point[] positions;		// Position of every hotspot
	private int[] offset;			// Token draw offset for every hotspot
	protected CombatFrame combatFrame;
	protected BattleModel model;
	protected RealmComponent sheetOwner;
	protected Collection sheetParticipants;
	protected HashLists layoutHash;
	HostPrefWrapper hostPrefs;
	
	protected Integer mouseHoverIndex;
	protected boolean mouseHoverShift = false;
	protected ArrayList battleChitsWithRolls;
	
	protected Hashtable hotspotHash;
	
	protected RollerGroup redGroup;
	protected RollerGroup squareGroup;
	protected RollerGroup circleGroup;
	
	protected boolean interactiveFrame;
	
	public boolean alwaysSecret = false;
	
	/**
	 * Testing constructor ONLY!!!
	 */
	protected CombatSheet() {
		battleChitsWithRolls = new ArrayList();
		hotspotHash = new Hashtable();
		layoutHash = new HashLists();
		mouseHoverIndex = null;
		positions = getPositions(hostPrefs);
		offset = new int[positions.length];
		setIcon(getImageIcon());
	}
	protected CombatSheet(CombatFrame frame,BattleModel model,RealmComponent participant,boolean interactiveFrame, HostPrefWrapper hostPrefs) {
		super("");
		this.combatFrame = frame;
		this.model = model;
		this.sheetOwner = participant;
		this.interactiveFrame = interactiveFrame;
		this.hostPrefs = hostPrefs;
		battleChitsWithRolls = new ArrayList();
		hotspotHash = new Hashtable();
		layoutHash = new HashLists();
		mouseHoverIndex = null;
		positions = getPositions(hostPrefs);
		offset = new int[positions.length];
		setIcon(getImageIcon());
		updateRollers();
		addKeyListener(shiftKeyListener);
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT,0),"dummy"); // this reserves the SHIFT key, somehow!?
		// There should really be an action mapping, but for some reason, its not used anyway!!!
	}
	public boolean hasHotspots() {
		return hotspotHash.size()>0;
	}
	public RealmComponent getSheetOwner() {
		return sheetOwner;
	}
	public TileLocation getBattleLocation() {
		return combatFrame.getBattleModel().getBattleLocation();
	}
	/**
	 * Gets a list of all RCs in the three boxes.  Excludes any character attacks, horses, or parts
	 */
	protected ArrayList getAllBoxListFromLayout(int box1) {
		ArrayList all = new ArrayList();
		for (int i=0;i<3;i++) {
			ArrayList list = layoutHash.getList(new Integer(box1+i));
			if (list!=null) {
				for (java.util.Iterator _j14it730 = (list).iterator(); _j14it730.hasNext(); ) {
				  RealmComponent rc = (RealmComponent) _j14it730.next();
					if (!rc.isNativeHorse() && !rc.isMonsterPart() && !rc.isActionChit()) {
						all.add(rc);
					}
				}
			}
		}
		return all;
	}
	protected ArrayList getAllFromSingleBoxListFromLayout(int box) {
		ArrayList all = new ArrayList();
		ArrayList list = layoutHash.getList(new Integer(box));
		if (list!=null) {
			for (java.util.Iterator _j14it731 = (list).iterator(); _j14it731.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it731.next();
				if (!rc.isNativeHorse() && !rc.isMonsterPart() && !rc.isActionChit()) {
					all.add(rc);
				}
			}
		}
		return all;
	}
	protected ArrayList getAllBoxListsFromLayout(int[] boxes) {
		ArrayList all = new ArrayList();
		for (int _j14i732 = 0; _j14i732 < boxes.length; _j14i732++) {
			all.addAll(getAllFromSingleBoxListFromLayout(boxes[_j14i732]));
		}
		return all;
	}
	protected static DieRoller makeRoller(String val) {
		return new DieRoller(val,25,6);
	}
	protected boolean usesMaxCombatBoxes(int index) {
		int total = 0;
		int boxCount = 0;
		
		for (int i=index;i<index+3;i++) {
			int count = countAttacks(i);
			if (count>0) boxCount++;
			total += count;
		}
		
		return total>2?boxCount==3:boxCount==total;
	}
	protected boolean usesCombatBoxesEqually(int index) {
		int total = 0;
		int boxCount = 0;
		for (int i=index;i<index+3;i++) {
			int count = countAttacks(i, true);
			boxCount++;
			total += count;
		}
		
		int minAttackers = total/boxCount;
		int maxAttackers = (total + boxCount - 1) / boxCount;
		
		for (int i=index;i<index+3;i++) {
			int attacksInBox = countAttacks(i, true);
			if (attacksInBox < minAttackers || attacksInBox > maxAttackers) {
				return false;
			}
		}
		return true;
	}
	/**
	 * @return		The total number of attacks in the box
	 */
	protected int countAttacks(int index, boolean includeHorses) {
		int count = 0;
		ArrayList list = layoutHash.getList(new Integer(index));
		if (list!=null) {
			for (java.util.Iterator _j14it733 = (list).iterator(); _j14it733.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it733.next();
				if (rc.isWeapon() && this instanceof DenizenCombatSheet) {
					RealmComponent holder = rc.getHeldBy();
					if (holder!=null) {
						RealmComponent target1 = holder.getTarget();
						RealmComponent target2 = holder.get2ndTarget();
						if (target1!=sheetOwner && target2!=sheetOwner) continue;
					}
				}
				if ((includeHorses || !rc.isNativeHorse()) && !rc.isActionChit()) {
					count++;
				}
			}
		}
		return count;
	}
	/**
	 * @return		The total number of attacks in the box:  doesn't include horses!
	 */
	protected int countAttacks(int index) {
		return countAttacks(index, false);
	}
	protected void updateBattleChitsWithRolls(CombatWrapper combat) {
		if ((combat.getMissileRolls()!=null && combat.getMissileRolls().size()>0)
				|| (combat.getFumbleRolls()!=null && combat.getFumbleRolls().size()>0)) {
			BattleChit bc = (BattleChit)RealmComponent.getRealmComponent(combat.getGameObject());
			battleChitsWithRolls.add(bc);
		}
	}
	private ArrayList getBattleRolls() {
		ArrayList battleRolls = new ArrayList();
		if (!battleChitsWithRolls.isEmpty()) {
			// Deduce and build all fumble/stumble rolls (in order)
			if (combatFrame.getCurrentRound()==1) {
				Collections.sort(battleChitsWithRolls,new BattleChitLengthComparator());
			}
			else {
				Collections.sort(battleChitsWithRolls,new BattleChitSpeedComparator());
			}
			for (java.util.Iterator _j14it734 = (battleChitsWithRolls).iterator(); _j14it734.hasNext(); ) {
			  BattleChit bc = (BattleChit) _j14it734.next();
				
				RealmComponent rc = (RealmComponent)bc;
				CombatWrapper combat = new CombatWrapper(bc.getGameObject());
				
				ArrayList missileResults = combat.getMissileRolls();
				ArrayList fumbleResults = combat.getFumbleRolls();
				
				String type;
				ArrayList rs,ss;
				if (fumbleResults!=null) {
					type = " Fumble";
					rs = fumbleResults;
					ss = combat.getFumbleRollSubtitles();
				}
				else {
					type = " Missile";
					rs = missileResults;
					ss = combat.getMissileRollSubtitles();
				}
				
				String prefix = (rc.isCharacter()?"":("B"+combat.getCombatBoxAttack()+","+combat.getCombatBoxDefense()+" "));
				Iterator r=rs.iterator();
				Iterator s=ss.iterator();
				while(r.hasNext()) {
					RollerResult rr = new RollerResult(
							prefix+combat.getGameObject().getName()+type,
							(String) r.next(),
							(String) s.next());
					battleRolls.add(rr);
				}
			}
		}
			
		// lastly, add any serious wounds results
		CharacterWrapper character = combatFrame.getActiveCharacter();
		if (character!=null) {
			CombatWrapper combat = new CombatWrapper(character.getGameObject());
			ArrayList list = combat.getSeriousWoundRolls();
			if (list!=null) {
				for (java.util.Iterator _j14it735 = (list).iterator(); _j14it735.hasNext(); ) {
				  String result = (String) _j14it735.next();
					RollerResult rr = new RollerResult(character.getCharacterName()+" Serious Wound",result,"");
					battleRolls.add(rr);
				}
			}
		}
		return battleRolls;
	}
	public boolean hasBattleRolls() {
		return !getBattleRolls().isEmpty();
	}
	protected void updateRollerResults() {
		if (combatFrame.getRollerResults()!=null) {
			combatFrame.getRollerResults().setBattleRolls(getBattleRolls());
		}
	}
	public void updateMouseHover(Point p) {
		updateMouseHover(p,false);
	}
	public void updateMouseHover(Point p,boolean isShiftDown) {
		Integer newIndex = null;
		if (p!=null) {
			for (java.util.Iterator _j14it736 = (layoutHash.keySet()).iterator(); _j14it736.hasNext(); ) {
			  Integer index = (Integer) _j14it736.next();
				int range = HOTSPOT_SIZE>>1;
				Point test = positions[index.intValue()];
				if (test!=null) {
					int dx = Math.abs(test.x-p.x);
					int dy = Math.abs(test.y-p.y);
					if (dx<range && dy<range) {
						newIndex = index;
						break;
					}
				}
			}
		}
		boolean sameIndex = mouseHoverIndex==null?newIndex==null:mouseHoverIndex.equals(newIndex);
		if (!sameIndex || mouseHoverShift!=isShiftDown) {
			mouseHoverIndex = newIndex;
			mouseHoverShift = isShiftDown;
			repaint();
		}
	}
	public void handleClick(Point p) {
		for (java.util.Iterator _j14it737 = (hotspotHash.keySet()).iterator(); _j14it737.hasNext(); ) {
		  Integer index = (Integer) _j14it737.next();
			int range = HOTSPOT_SIZE>>1;
			Point test = positions[index.intValue()];
			int dx = Math.abs(test.x-p.x);
			int dy = Math.abs(test.y-p.y);
			if (dx<range && dy<range) {
				int side = p.x<test.x?SwingConstants.LEFT:SwingConstants.RIGHT;
				handleClick(index.intValue(),side);
				combatFrame.updateHotspotIndicators();
				break;
			}
		}
	}
	public Collection getAllParticipantsOnSheet() {
		ArrayList list = new ArrayList();
		for (java.util.Iterator _j14it738 = (layoutHash.values()).iterator(); _j14it738.hasNext(); ) {
		  ArrayList in = (ArrayList) _j14it738.next();
			for (java.util.Iterator _j14it739 = (in).iterator(); _j14it739.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it739.next();
				if (rc.isMonster() || rc.isCharacter() || rc.isNative()) {
					list.add(rc);
				}
			}
		}
		return list;
	}
	public void paint(Graphics g) {
		//super.paint(g);
		g.drawImage(getImageIcon().getImage(),0,0,null);
		
		// Draw components
		Arrays.fill(offset,0);
		for (java.util.Iterator _j14it740 = (layoutHash.keySet()).iterator(); _j14it740.hasNext(); ) {
		  Integer index = (Integer) _j14it740.next();
			ArrayList list = layoutHash.getList(index);
			Collections.sort(list);
			for (java.util.Iterator _j14it741 = (list).iterator(); _j14it741.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it741.next();
				paintRealmComponent(g,rc,index.intValue());
			}
		}
		
		// Draw hotspots
		ArrayList hotspotKeys = new ArrayList(hotspotHash.keySet());
		Collections.sort(hotspotKeys);
		for (java.util.Iterator _j14it742 = (hotspotKeys).iterator(); _j14it742.hasNext(); ) {
		  Integer index = (Integer) _j14it742.next();
			String name = (String) hotspotHash.get(index);
			paintHotSpot(g,name,index.intValue());
		}
		
		// Draw Rollers (if any)
		drawRollers(g);
		
		// Other info
		drawOther(g);
		
		// Draw mouse hover
		if (mouseHoverIndex!=null) {
			Dimension maxSize = getSize();
			int contentsX = 5;
			int contentsY = 5;
			
			ArrayList c = layoutHash.getList(mouseHoverIndex);
			
			if (c!=null) {
				Rectangle[] plot = new Rectangle[c.size()];
				int n=0;
				for (java.util.Iterator _j14it743 = (c).iterator(); _j14it743.hasNext(); ) {
				  RealmComponent rc = (RealmComponent) _j14it743.next();
					Dimension d = rc.getSize();
					plot[n++] = new Rectangle(contentsX,contentsY,d.width,d.height);
					contentsX += d.width;
					contentsX += 5;
				}
				// Resize if the contents run off the edge
				Rectangle test = plot[plot.length-1];
				if ((test.x+test.width)>maxSize.width) {
					double scaling = ((double)(maxSize.width-test.width))/((double)(test.x));
					for (int i=0;i<plot.length;i++) {
						plot[i].x = (int)(plot[i].x*scaling);
					}
				}
				
				// Finally, draw them
				n=0;
				for (java.util.Iterator _j14it744 = (c).iterator(); _j14it744.hasNext(); ) {
				  RealmComponent rc = (RealmComponent) _j14it744.next();
					Rectangle r = plot[n++];
					if (mouseHoverShift && rc.isChit()) {
						// draw flipside
						ChitComponent cc = (ChitComponent)rc;
						g.drawImage(cc.getFlipSideImage(),r.x,r.y,null);
					}
					else {
						rc.paint(g.create(r.x,r.y,r.width,r.height));
					}
				}
			}
		}
		
		// Draw consecutive rounds of combat without damage/fatigue/spellcasting
		if (combatFrame!=null) {
			CombatWrapper tile = new CombatWrapper(combatFrame.getBattleModel().getBattleLocation().tile.getGameObject());
			int n = tile.getRoundsOfMissing();
			if (combatFrame.getActionState()>=Constants.COMBAT_RESOLVING) n--;
			if (n>0) {
				g.setColor(Color.red);
				g.setFont(Constants.HOTSPOT_FONT);
				String text = "";
				HostPrefWrapper hostPrefs = HostPrefWrapper.findHostPrefs(tile.getGameData());
				if (hostPrefs.hasPref(Constants.SR_ENDING_COMBAT)) {
					text = "There was no klling, wounds, fatigue, or spellcasting last round.";
				} else {
					text = "There was no damage, fatigue, or spellcasting last round.";
				}
				g.drawString(text,5,15);
			}
		}
	}
	protected static String getSubtitleForReposition(DieRoller roller) {
		if (roller!=null) {
			switch(roller.getHighDieResult()) {
				case 1:		return "Switch Box 2 & 3";
				case 2:		return "Switch Box 1 & 3";
				case 3:		return "Switch Box 1 & 2";
				case 4:		return "No Change";
				case 5:		return "Move Down & Right";
				case 6:		return "Move Up & Left";
			}
		}
		return null;
	}
	protected static String getSubtitleForTactics(DieRoller roller) {
		String ret = null;
		if (roller!=null) {
			ret = roller.getHighDieResult()==6?"Flipped!":null;
		}
		return ret;
	}
	protected void paintRoller(Graphics g,String title,String subtitle,DieRoller roller,int index,int xoff,int yoff) {
		if (roller!=null) {
			Dimension size = roller.getPreferredSize();
			
			Point p = positions[index];
			int x = p.x - (size.width>>1) + xoff;
			int y = p.y - (size.height>>1) + yoff;
			
			roller.paintComponent(g.create(x,y,size.width,size.height));
			
			Border lineBorder = new LineBorder(Color.black,1);
			Border titleBorder = BorderFactory.createTitledBorder(
					lineBorder,
					title,
					TitledBorder.CENTER,
					TitledBorder.TOP,
					Constants.HOTSPOT_FONT,
					Color.black);
			titleBorder.paintBorder(this,g,x,y-10,size.width,size.height+10);
			
			if (subtitle!=null) {
				g.setFont(Constants.RESULT_FONT);
				g.setColor(Color.yellow);
				GraphicsUtil.drawCenteredString(g,x+1,y+16,size.width,size.height+10,subtitle);
				g.setColor(Color.blue);
				GraphicsUtil.drawCenteredString(g,x,y+15,size.width,size.height+10,subtitle);
			}
		}
	}
	protected void placeParticipantAttacker(RealmComponent participant) {
		placeParticipant(participant,false,false,false,false,true);
	}
	protected void placeParticipantDefenderTarget(RealmComponent participant) {
		placeParticipant(participant,false,false,false,true,false);
	}
	protected void placeParticipantDefender(RealmComponent participant) {
		placeParticipantDefender(participant,false,false);
	}
	protected void placeParticipantDefender(RealmComponent participant,boolean secrecy,boolean horseSameBox) {
		placeParticipant(participant,secrecy,horseSameBox,true,false,false);
	}
	protected void placeParticipant(RealmComponent participant) {
		placeParticipant(participant,false,false,false,false,false);
	}
	private void placeParticipant(RealmComponent participant,boolean secrecy,boolean horseSameBox,boolean defender,boolean defenderTarget,boolean attacker) {
		CombatWrapper combat;
		// Place horse (if any) first
		RealmComponent horse = (RealmComponent)participant.getHorse();
		if (horse!=null) {
			combat = new CombatWrapper(horse.getGameObject());
			int boxD = combat.getCombatBoxDefense();
			int boxA = combat.getCombatBoxAttack();
			if (boxD==0) {
				if (horseSameBox) {
					CombatFrame.placeInFirstCombatBox(combat);
					boxD = combat.getCombatBoxDefense();
					boxA = combat.getCombatBoxAttack();
				}
				else {
					CombatFrame.placeInCombatBoxAvailable(combat,2);
					boxD = combat.getCombatBoxDefense();
					boxA = combat.getCombatBoxAttack();
				}
			}
			if (secrecy) {
				boxA = 0;
			}
			if (defender) {
				layoutHash.put(new Integer(getBoxIndexFromCombatBoxesForDefender(boxA,boxD)),horse);
			} else if (defenderTarget) {
				layoutHash.put(new Integer(getBoxIndexFromCombatBoxesForDefenderTarget(boxA,boxD)),horse);
			} else if (attacker) {
				layoutHash.put(new Integer(getBoxIndexFromCombatBoxesForAttacker(boxA,boxD)),horse);
			} else {
				layoutHash.put(new Integer(getBoxIndexFromCombatBoxes(boxA,boxD)),horse);
			}
		}
		
		// Place participant
		combat = new CombatWrapper(participant.getGameObject());
		updateBattleChitsWithRolls(combat);
		
		int boxA = combat.getCombatBoxAttack();
		int boxD = combat.getCombatBoxDefense();
		if (boxA==0 || boxD==0) {
			CombatFrame.placeInFirstCombatBox(combat);
			boxD = combat.getCombatBoxDefense();
			boxA = combat.getCombatBoxAttack();
		}
		if (secrecy) {
			boxA = 0;
		}
		if (defender) {
			layoutHash.put(new Integer(getBoxIndexFromCombatBoxesForDefender(boxA,boxD)),participant);
		} else if (defenderTarget) {
			layoutHash.put(new Integer(getBoxIndexFromCombatBoxesForDefenderTarget(boxA,boxD)),participant);
		} else if (attacker) {
			layoutHash.put(new Integer(getBoxIndexFromCombatBoxesForAttacker(boxA,boxD)),participant);
		} else {
			layoutHash.put(new Integer(getBoxIndexFromCombatBoxes(boxA,boxD)),participant);
		}
		
		// Place weapon (if any)
		if (participant.isMonster()) {
			MonsterChitComponent monster = (MonsterChitComponent)participant;
			MonsterPartChitComponent weapon = monster.getWeapon();
			if (weapon!=null) {
				combat = new CombatWrapper(weapon.getGameObject());
				updateBattleChitsWithRolls(combat);
				boxA = combat.getCombatBoxAttack();
				boxD = combat.getCombatBoxDefense();
				if (boxA==0) {
					CombatFrame.placeInCombatBoxAvailable(combat,2);
					boxD = combat.getCombatBoxDefense();
					boxA = combat.getCombatBoxAttack();
				}
				if (secrecy) {
					boxA = 0;
				}
				if (defender) {
					layoutHash.put(new Integer(getBoxIndexFromCombatBoxesForDefender(boxA,boxD)),weapon);
				} else if (defenderTarget) {
					layoutHash.put(new Integer(getBoxIndexFromCombatBoxesForDefenderTarget(boxA,boxD)),weapon);
				} else if (attacker) {
					layoutHash.put(new Integer(getBoxIndexFromCombatBoxesForAttacker(boxA,boxD)),weapon);
				} else {
					layoutHash.put(new Integer(getBoxIndexFromCombatBoxes(boxA,boxD)),weapon);
				}
			}
		}
	}
	protected boolean addedToDead(RealmComponent rc) {
		CombatWrapper combat = new CombatWrapper(rc.getGameObject());
		if (combat.isDead()) {
			if (combatFrame.getActionState()<Constants.COMBAT_RESOLVING) {
				layoutHash.put(new Integer(getDeadBoxIndex()),rc);
				return true;
			}
			else if (combatFrame.getActionState()==Constants.COMBAT_RESOLVING) {
				if (!combat.hasCombatBox()) {
					layoutHash.put(new Integer(getDeadBoxIndex()),rc);
					return true;
				}
			}
		}
		return false;
	}
	protected void placeAllAttacks(int attackBox1,int weaponBox1,Collection excludeList) {
		boolean reveal = combatFrame.getActionState()>=Constants.COMBAT_RESOLVING;
		
		ArrayList all = new ArrayList(model.getAllBattleParticipants(true));
		
		// Sort by target index (lower first), to keep stack ordering correct
		Collections.sort(all,new TargetIndexComparator());
		
		/*
		 * Cycle through all participants that are in the model, that are targeting one of the monsters or
		 * natives in the target boxes.  (go into the attack boxes)
		 */
		for (java.util.Iterator _j14it745 = (all).iterator(); _j14it745.hasNext(); ) {
		  RealmComponent rc = (RealmComponent) _j14it745.next();
			CombatWrapper rcCombat = new CombatWrapper(rc.getGameObject());
			RealmComponent target = rc.getTarget();
			RealmComponent target2 = rc.get2ndTarget();
			GameObject spell = rcCombat.getCastSpell();
			
			// If targeting any of the sheetOwners targets, put them on the sheet in the attacker boxes
			boolean isInactiveSheetOwner = target==null && target2==null && spell==null && sheetOwner.equals(rc);
			boolean castingASpell = spell!=null;
			boolean battleMage = false;
			if (rc.isCharacter()) {
				GameObject chararacterGo = rc.getGameObject();
				CharacterWrapper activeCharacter = new CharacterWrapper(chararacterGo);
				HostPrefWrapper hostPrefs = HostPrefWrapper.findHostPrefs(chararacterGo.getGameData());
				if (activeCharacter.affectedByKey(Constants.BATTLE_MAGE) || hostPrefs.hasPref(Constants.SR_ADV_STEEL_AGAINST_MAGIC)) {
					if (activeCharacter.hasOnlyStaffAsActivatedWeapon() && !activeCharacter.hasActiveArmorChits()) {
						battleMage = true;
					}
				}
			}
					
			boolean targetingSomeoneOnThisSheet = (target!=null && (sheetParticipants.contains(target) || sheetOwner.equals(target))) || (target2!=null && (sheetParticipants.contains(target2) || sheetOwner.equals(target2)));
			if (!targetingSomeoneOnThisSheet && target!=null) {
				RealmComponent targetsTarget = target.getTarget();
				RealmComponent targetsTarget2 = target.get2ndTarget();
				if (targetsTarget!=null && sheetParticipants.contains(targetsTarget)) {
					// This is sloppy, but will work
					targetingSomeoneOnThisSheet = true;
				}
				else if (targetsTarget2!=null && sheetParticipants.contains(targetsTarget2)) {
					// This is sloppy, but will work
					targetingSomeoneOnThisSheet = true;
				}
			}
			if (!targetingSomeoneOnThisSheet && target2!=null) {
				RealmComponent targetsTarget = target2.getTarget();
				RealmComponent targetsTarget2 = target2.get2ndTarget();
				if (targetsTarget!=null && sheetParticipants.contains(targetsTarget)) {
					// This is sloppy, but will work
					targetingSomeoneOnThisSheet = true;
				}
				else if (targetsTarget2!=null && sheetParticipants.contains(targetsTarget2)) {
					// This is sloppy, but will work
					targetingSomeoneOnThisSheet = true;
				}
			}
			if (isInactiveSheetOwner || targetingSomeoneOnThisSheet || castingASpell) {
				if (excludeList==null || !excludeList.contains(rc)) {
					if (!rc.isCharacter() && (sheetParticipants.contains(target) || sheetParticipants.contains(target2))) {
						if (!addedToDead(rc)) {
							placeParticipantAttacker(rc);
							sheetParticipants.add(rc);
						}
					}
					else if (rc.isCharacter()) {
						updateBattleChitsWithRolls(rcCombat);
						CharacterWrapper character = new CharacterWrapper(rc.getGameObject());
						CharacterChitComponent characterChit = (CharacterChitComponent)rc;
						if (spell==null || battleMage) {
							// Only show attacks if attacking a non-owned target, OR the attacker is the activeParticipant
							if (reveal 
									|| (target==null && target2==null)
									|| (target!=null && target.getOwnerId()==null)
									|| (target2!=null && target2.getOwnerId()==null)
									|| ((target!=null || target2!=null) && rc.equals(combatFrame.getActiveParticipant()))) {
								
								MonsterChitComponent transmorph = characterChit.getTransmorphedComponent();
								if (transmorph==null) {
									// Cycle through character fight chits and weapons for attack
									ArrayList weapons = character.getActiveWeapons();
									if (weapons!=null) {
										for (java.util.Iterator _j14it746 = (weapons).iterator(); _j14it746.hasNext(); ) {
										  WeaponChitComponent weapon = (WeaponChitComponent) _j14it746.next();
											CombatWrapper combat = new CombatWrapper(weapon.getGameObject());
											int box = combat.getCombatBoxAttack();
											if (box>0 && !combat.getPlacedAsParry() && !combat.getPlacedAsParryShield() && this.sheetOwner.getGameObject().getStringId().equals(combat.getSheetOwnerId())) {
												layoutHash.put(new Integer(weaponBox1+box-1),weapon);
											}
										}
									}
									for (java.util.Iterator _j14it747 = (character.getActiveFightChits()).iterator(); _j14it747.hasNext(); ) {
									  RealmComponent chit = (RealmComponent) _j14it747.next();
										CombatWrapper combat = new CombatWrapper(chit.getGameObject());
										int box = combat.getCombatBoxAttack();
										if (box>0 && combat.getPlacedAsFight() && this.sheetOwner.getGameObject().getStringId().equals(combat.getSheetOwnerId())) {
											layoutHash.put(new Integer(attackBox1+box-1),chit);
										}
									}
									
									// Look for gloves, and/or weapon inventory
									for (java.util.Iterator _j14it748 = (character.getActiveInventory()).iterator(); _j14it748.hasNext(); ) {
									  GameObject go = (GameObject) _j14it748.next();
										RealmComponent item = RealmComponent.getRealmComponent(go);
										if (item.getGameObject().hasThisAttribute("gloves")) {
											CombatWrapper combat = new CombatWrapper(item.getGameObject());
											int box = combat.getCombatBoxAttack();
											if (box>0 && this.sheetOwner.getGameObject().getStringId().equals(combat.getSheetOwnerId())) {
												layoutHash.put(new Integer(attackBox1+box-1),item);
											}
										}
										else if (item.getGameObject().hasThisAttribute("attack")) {
											CombatWrapper combat = new CombatWrapper(item.getGameObject());
											int box = combat.getCombatBoxAttack();
											if (box>0 && this.sheetOwner.getGameObject().getStringId().equals(combat.getSheetOwnerId())) {
												layoutHash.put(new Integer(weaponBox1+box-1),item);
											}
										}
									}
								}
								else {
									CombatWrapper combat = new CombatWrapper(transmorph.getGameObject());
									updateBattleChitsWithRolls(combat);
									int box = combat.getCombatBoxAttack();
									if (box>0) {
										layoutHash.put(new Integer(attackBox1+box-1),transmorph.getFightChit());
									}
									
									// Add monster weapon here
									MonsterPartChitComponent monsterWeapon = transmorph.getWeapon();
									if (monsterWeapon!=null) {
										combat = new CombatWrapper(monsterWeapon.getGameObject());
										updateBattleChitsWithRolls(combat);
										box = combat.getCombatBoxAttack();
										if (box>0) {
											layoutHash.put(new Integer(attackBox1+box-1),monsterWeapon);
										}
									}
								}
							}
						}
						if (spell!=null) {
							// Attack spells are placed here
							ArrayList targetTest = new ArrayList();
							targetTest.addAll(sheetParticipants);
							targetTest.add(sheetOwner);
							SpellWrapper sw = new SpellWrapper(spell);
							if (sw.isAttackSpell() && sw.isAlive()) {
								ArrayList targeted = sw.getTargetedRealmComponents(targetTest);
								boolean showAttack = targeted.size()>0 || sw.noTargeting();
								
								// If the attacker is NOT the active participant and...
								if (!rc.equals(combatFrame.getActiveParticipant())) {
									// ... any of the targeted are owned by the activeParticipant, then don't show attack (maintains secrecy)
									for (java.util.Iterator _j14it749 = (targeted).iterator(); _j14it749.hasNext(); ) {
									  RealmComponent test = (RealmComponent) _j14it749.next();
										if (test.getOwnerId()!=null && test.getOwner().equals(combatFrame.getActiveParticipant())) {
											showAttack = false;
											break;
										}
									}
								}
								if (reveal || showAttack) {
									GameObject incObj = sw.getIncantationObject();
									CombatWrapper combat = new CombatWrapper(incObj);
									int box = combat.getCombatBoxAttack();
									if (box>0) {
										layoutHash.put(new Integer(weaponBox1+box-1),RealmComponent.getRealmComponent(spell));
									}
								}
							}
						}
					}
				}
			}
		}
	}
	private static final int HOTSPOT_SIZE = 98;
	private static final Color HOTSPOT_TITLE_COLOR = Color.blue;
	private static final Color HOTSPOT_LINE_COLOR = Color.green;//new Color(0,0,255,120);
	private static final Color HOTSPOT_BACKING = new Color(0,255,0,40);
	private static final Color HOTSPOT_SPLIT_BACKING = new Color(255,255,255,190);
	private void paintHotSpot(Graphics g,String string,int index) {
		int HOTSPOT_SIZE = getHotSpotSize();
		Point p = positions[index];
		int x = p.x - (HOTSPOT_SIZE>>1);
		int y = p.y - (HOTSPOT_SIZE>>1);
		
		g.setColor(HOTSPOT_BACKING);
		g.fillRect(x,y+5,HOTSPOT_SIZE,HOTSPOT_SIZE-5);
		Border lineBorder = new LineBorder(HOTSPOT_LINE_COLOR,4,true);
		Border titleBorder = BorderFactory.createTitledBorder(
				lineBorder,
				string,
				TitledBorder.CENTER,
				TitledBorder.TOP,
				Constants.HOTSPOT_FONT,
				HOTSPOT_TITLE_COLOR);
		titleBorder.paintBorder(this,g,x,y,HOTSPOT_SIZE,HOTSPOT_SIZE+5);
		
		if (hostPrefs.hasPref(Constants.HOUSE3_HORSE_WEAPON_SAME_BOX)) {
			String[] split = splitHotSpot(index);
			if (split!=null) {
				int half = HOTSPOT_SIZE>>1;
				g.setColor(HOTSPOT_LINE_COLOR);
				g.fillRect(x+half-2,y+16,4,HOTSPOT_SIZE-16);
				
				g.setColor(HOTSPOT_SPLIT_BACKING);
				g.fillRect(x-2,y+15,12,HOTSPOT_SIZE-20);
				g.fillRect(x+90,y+15,12,HOTSPOT_SIZE-20);
				
				g.setColor(Color.black);
				g.setFont(Constants.RESULT_FONT);
				TextType.drawText(g,split[0],x,y+5,HOTSPOT_SIZE,90,Alignment.Center);
				TextType.drawText(g,split[1],x+93,y+5,HOTSPOT_SIZE,90,Alignment.Center);
			}
		}
	}
	private void paintRealmComponent(Graphics g,RealmComponent rc,int index) {
		ImageIcon icon = rc.getIcon();
		Point p = positions[index];
		int x = p.x - (icon.getIconWidth()>>1) - offset[index];
		int y = p.y - (icon.getIconHeight()>>1) - offset[index];
		g.drawImage(icon.getImage(),x,y,null);
		offset[index] += CHIT_OFFSET;
	}
	// Scrollable interface
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}
	public Dimension getPreferredScrollableViewportSize() {
		return null;
	}
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 300;
	}
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 20;
	}
	protected void updateRollers() {
		CombatWrapper combat = new CombatWrapper(sheetOwner.getGameObject());
		redGroup = buildRollerGroup(CombatWrapper.GROUP_RED,combat);
		circleGroup = buildRollerGroup(CombatWrapper.GROUP_CIRCLE,combat);
		squareGroup = buildRollerGroup(CombatWrapper.GROUP_SQUARE,combat);
	}
	protected static RollerGroup buildRollerGroup(String prefix,CombatWrapper combat) {
		RollerGroup rg = null;
		int reposition = combat.getRepositionResult(prefix);
		if (reposition>0) {
			rg = new RollerGroup();
			rg.repositionRoller = new DieRoller();
			rg.repositionRoller.adjustDieSize(25, 6);
			rg.repositionRoller.addRedDie();
			rg.repositionRoller.setValue(0,reposition);
			
			String changeTacs1 = combat.getChangeTacticsResult(prefix,1);
			if (changeTacs1!=null) {
				rg.changeTacticsRoller1 = makeRoller(changeTacs1);
			}
			String changeTacs2 = combat.getChangeTacticsResult(prefix,2);
			if (changeTacs2!=null) {
				rg.changeTacticsRoller2 = makeRoller(changeTacs2);
			}
			String changeTacs3 = combat.getChangeTacticsResult(prefix,3);
			if (changeTacs3!=null) {
				rg.changeTacticsRoller3 = makeRoller(changeTacs3);
			}
		}
		return rg;
	}
	protected void drawRollerGroup(Graphics g,RollerGroup rg,int repRoll,int ctRoll) {
		paintRoller(g," ",getSubtitleForReposition(rg.repositionRoller),rg.repositionRoller,repRoll,0,0);
		paintRoller(g,"Tactics",getSubtitleForTactics(rg.changeTacticsRoller1),rg.changeTacticsRoller1,ctRoll,0,-50);
		paintRoller(g,"Tactics",getSubtitleForTactics(rg.changeTacticsRoller2),rg.changeTacticsRoller2,ctRoll+1,0,-50);
		paintRoller(g,"Tactics",getSubtitleForTactics(rg.changeTacticsRoller3),rg.changeTacticsRoller3,ctRoll+2,0,-50);
	}
	
	/**
	 * @return		true if attacked by another character (hirelings are ignored for this check, however)
	 */
	protected boolean isAttackedByCharacter() {
		for (java.util.Iterator _j14it750 = (model.getAttackersFor(sheetOwner)).iterator(); _j14it750.hasNext(); ) {
		  RealmComponent attacker = (RealmComponent) _j14it750.next();
			if (attacker.isCharacter()) {
				return true;
			}
		}
		return false;
	}
	
	protected static class RollerGroup {
		public DieRoller repositionRoller;
		public DieRoller changeTacticsRoller1;
		public DieRoller changeTacticsRoller2;
		public DieRoller changeTacticsRoller3;
	}
	
	public static boolean containsHorse(ArrayList list) {
		if (list!=null) {
			for (java.util.Iterator _j14it751 = (list).iterator(); _j14it751.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it751.next();
				if (rc.hasHorse()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean containsEnemy(RealmComponent attacker,ArrayList list) {
		if (list!=null) {
			for (java.util.Iterator _j14it752 = (list).iterator(); _j14it752.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it752.next();
				if (combatFrame.allowsTreachery() || !attacker.equals(rc.getOwner())) {
					return true;
				}
			}
		}
		return false;
	}
	public static boolean containsFriend(RealmComponent attacker,ArrayList list) {
		if (list!=null) {
			for (java.util.Iterator _j14it753 = (list).iterator(); _j14it753.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it753.next();
				if (attacker.equals(rc.getOwner())) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Returns true, if the list contains at least one friend, or one unhired denizen.
	 */
	public static boolean containsFriendOrDenizen(RealmComponent attacker,ArrayList list) {
		if (list!=null) {
			for (java.util.Iterator _j14it754 = (list).iterator(); _j14it754.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it754.next();
				if (rc.isNative() || rc.isMonster() || rc.isCharacter() || rc.isActionChit()) {
					RealmComponent owner = rc.getOwner();
					if (owner==null || attacker.equals(rc.getOwner())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public static ArrayList filterEnemies(RealmComponent attacker,ArrayList list) {
		if (list!=null) {
			ArrayList ret = new ArrayList();
			for (java.util.Iterator _j14it755 = (list).iterator(); _j14it755.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it755.next();
				if (!attacker.equals(rc.getOwner())) {
					ret.add(rc);
				}
			}
			return ret;
		}
		return null;
	}
	public static ArrayList filterFriends(RealmComponent attacker,ArrayList list) {
		if (list!=null) {
			ArrayList ret = new ArrayList();
			for (java.util.Iterator _j14it756 = (list).iterator(); _j14it756.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it756.next();
				if (attacker.equals(rc.getOwner())) {
					ret.add(rc);
				}
			}
			return ret;
		}
		return null;
	}
	public static ArrayList filterFriendsAndDenizens(RealmComponent attacker,ArrayList list) {
		if (list!=null) {
			ArrayList ret = new ArrayList();
			for (java.util.Iterator _j14it757 = (list).iterator(); _j14it757.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it757.next();
				RealmComponent owner = rc.getOwner();
				if (owner==null || attacker.equals(rc.getOwner())) {
					ret.add(rc);
				}
			}
			return ret;
		}
		return null;
	}
	public static ArrayList filterNativeFriendly(RealmComponent attacker,Collection list) {
		if (attacker == null) return (ArrayList) list;
		if (list!=null) {
			ArrayList ret = new ArrayList();
			HostPrefWrapper hostPrefs = HostPrefWrapper.findHostPrefs(attacker.getGameObject().getGameData());
			for (java.util.Iterator _j14it758 = (list).iterator(); _j14it758.hasNext(); ) {
			  RealmComponent rc = (RealmComponent) _j14it758.next();
				if (!attackerIsFriendlyToDenizen(attacker,rc,hostPrefs)) {
					ret.add(rc);
				}
			}
			return ret;
		}
		return null;
	}
	public static boolean attackerIsFriendlyToDenizen(RealmComponent attacker, RealmComponent rc, HostPrefWrapper hostPrefs) {
		if (rc.isNative() && !rc.isHiredOrControlled()) {
			RealmComponent owner = attacker;
			while (owner.isHiredOrControlled()) {
				owner = owner.getOwner();
			}
			CharacterWrapper ownerCharacter = new CharacterWrapper(owner.getGameObject());
			boolean ownerIsNativeFriendly = ownerCharacter.affectedByKey(Constants.NATIVE_FRIENDLY);
			boolean ownerIsBattlingNativeGroup = false;
			Collection battlingNativeGroups = ownerCharacter.getBattlingNativeGroups();
			String nativeGroup = rc.getGameObject().getThisAttribute("native");
			for (java.util.Iterator _j14it759 = (battlingNativeGroups).iterator(); _j14it759.hasNext(); ) {
			  String group = (String) _j14it759.next();
				if (group.toLowerCase().matches(nativeGroup.toLowerCase())) {
					ownerIsBattlingNativeGroup = true;
					break;
				}
			}
			if (!ownerIsBattlingNativeGroup && (ownerIsNativeFriendly || hostPrefs.hasPref(Constants.OPT_NATIVES_FRIENDLY))) {
				int relationship = ownerCharacter.getRelationship(rc.getGameObject());
				boolean groupIsFriendlyAllied = relationship == RelationshipType.FRIENDLY || relationship == RelationshipType.ALLY;
				if (groupIsFriendlyAllied) {
					return true;
				}
				return false;
			}
			return false;
		}
		return false;
	}
	public static CombatSheet createCombatSheet(CombatFrame frame,BattleModel currentBattleModel,RealmComponent rc,boolean interactiveFrame, HostPrefWrapper hostPrefs) {
		if (rc.isCharacter()) {
			return new CharacterCombatSheet(frame,currentBattleModel,rc,interactiveFrame,hostPrefs);
		}
		return new DenizenCombatSheet(frame,currentBattleModel,rc,interactiveFrame,hostPrefs);
	}
	private KeyListener shiftKeyListener = new KeyListener() {
		public void keyTyped(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode()==KeyEvent.VK_SHIFT) {
				mouseHoverShift = true;
				repaint();
			}
		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode()==KeyEvent.VK_SHIFT) {
				mouseHoverShift = false;
				repaint();
			}
		}
	};
}