package com.robin.magic_realm.components.quest;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.robin.game.objects.GameData;
import com.robin.general.graphics.GraphicsUtil;
import com.robin.general.swing.ComponentTools;
import com.robin.general.swing.ImageCache;
import com.robin.magic_realm.components.utility.Constants;

public class QuestStepView extends JComponent {
	
	private static int BORDER = 5;
	private static int SQUARE = 30;
	
	private static ImageIcon selection = ImageCache.getIcon("quests/tokenselect",QuestStepState.tokenSizePercent);
	
	private static Font idFont = new Font("Dialog",Font.BOLD,12);
	
	ArrayList tokens;
	private int orientation = SwingConstants.HORIZONTAL;
	private int maxRank;
	private int maxDisplayOrder;
	
	private ArrayList changeListeners;
	
	public QuestStepView(){
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent ev) {
				QuestStep step = getStepAtPoint(ev.getPoint());
				if (step!=null) {
					setSelectedStep(step);
					fireStateChanged();
				}
			}
		});		
	}
	public void addChangeListener(ChangeListener listener) {
		if (changeListeners==null) changeListeners = new ArrayList();
		if (!changeListeners.contains(listener)) {
			changeListeners.add(listener);
		}
	}
	public void removeChangeListener(ChangeListener listener) {
		if (changeListeners==null) return;
		changeListeners.remove(listener);
		if (changeListeners.size()==0) changeListeners=null;
	}
	protected void fireStateChanged() {
		if (changeListeners==null) return;
		ChangeEvent ev = new ChangeEvent(this);
		for (java.util.Iterator _j14it2252 = (changeListeners).iterator(); _j14it2252.hasNext(); ) {
		  ChangeListener listener = (ChangeListener) _j14it2252.next();
			listener.stateChanged(ev);
		}
	}
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	public void setSelectedStep(QuestStep step) {
		if (tokens==null) return;
		for (java.util.Iterator _j14it2253 = (tokens).iterator(); _j14it2253.hasNext(); ) {
		  QuestStepToken token = (QuestStepToken) _j14it2253.next();
			token.setSelected(token.getStep()==step);
		}
		repaint();
	}
	public void setFirstSelectedStep() {
		if (tokens==null || tokens.size()==0) return;
		setSelectedStep(((QuestStepToken) tokens.get(0)).getStep());
		fireStateChanged();
	}
	public QuestStep getSelectedStep() {
		for (java.util.Iterator _j14it2254 = (tokens).iterator(); _j14it2254.hasNext(); ) {
		  QuestStepToken token = (QuestStepToken) _j14it2254.next();
			if (token.isSelected()) return token.getStep();
		}
		return null;
	}
	public void updateSteps(ArrayList steps) {
		rebuildTokens(steps);
		layoutTokens();
		repaint();
	}
	public QuestStep getStepAtPoint(Point p) {
		if (tokens!=null) {
			for (java.util.Iterator _j14it2255 = (tokens).iterator(); _j14it2255.hasNext(); ) {
			  QuestStepToken token = (QuestStepToken) _j14it2255.next();
				Rectangle r = new Rectangle(token.drawX,token.drawY,SQUARE,SQUARE);
				if (r.contains(p)) {
					return token.getStep();
				}
			}
		}
		return null;
	}
	public void paintComponent(Graphics g1) {
		super.paintComponent(g1);
		if (tokens==null) return;
		Graphics2D g = (Graphics2D)g1;
		
		Stroke old = g.getStroke();
		
		// Draw connectors
		g.setColor(Color.black);
		g.setStroke(Constants.THICK_STROKE);
		for (java.util.Iterator _j14it2256 = (tokens).iterator(); _j14it2256.hasNext(); ) {
		  QuestStepToken token = (QuestStepToken) _j14it2256.next();
			if (token.getStep().isOrigin()) continue;
			int x1 = token.getDrawX()+(SQUARE>>1); 
			int y1 = token.getDrawY()+(SQUARE>>1); 
			for (java.util.Iterator _j14it2257 = (tokens).iterator(); _j14it2257.hasNext(); ) {
			  QuestStepToken otherToken = (QuestStepToken) _j14it2257.next();
				if (token==otherToken) continue;
				if (token.getViewRank()!=otherToken.viewRank+1) continue;
				boolean required = token.getStep().requires(otherToken.getStep());
				boolean onfail = token.getStep().requiresFail(otherToken.getStep());
				boolean bothVirtual = token.isVirtual() && otherToken.isVirtual();
				if (((required || onfail) && !bothVirtual)
						|| token.getStep()==otherToken.getStep()) { // virtual match
					// draw a line...
					Color c = onfail ? Color.red : token.getStep().getLogicType().getColor();
					g.setColor(c);
					int x2 = otherToken.getDrawX()+(SQUARE>>1); 
					int y2 = otherToken.getDrawY()+(SQUARE>>1);
					g.drawLine(x1,y1,x2,y2);
				}
			}
		}
		g.setStroke(old);
		
		// Draw tokens
		g.setFont(idFont);
		for (java.util.Iterator _j14it2258 = (tokens).iterator(); _j14it2258.hasNext(); ) {
		  QuestStepToken token = (QuestStepToken) _j14it2258.next();
			if (token.isVirtual()) {
				if (SHOW_VIRTUAL) {
					g.setColor(Color.white);
					g.fillOval(token.getDrawX()+2,token.getDrawY()-1,SQUARE,SQUARE);
					GraphicsUtil.drawCenteredString(g1,token.getDrawX()+2,token.getDrawY()-1,SQUARE,SQUARE,""+token.getStep().getId());
					g.setColor(Color.blue);
					GraphicsUtil.drawCenteredString(g1,token.getDrawX()+1,token.getDrawY()-2,SQUARE,SQUARE,""+token.getStep().getId());
				}
				continue;
			}
			QuestStepState state = token.getStep().getState();
			g.drawImage(state.getIcon().getImage(),token.getDrawX(),token.getDrawY(),this);
			if (token.isSelected()) {
				g.drawImage(selection.getImage(),token.getDrawX(),token.getDrawY(),this);
			}
			if (state == QuestStepState.None) {
				g.setColor(Color.white);
				GraphicsUtil.drawCenteredString(g1,token.getDrawX()+2,token.getDrawY()-1,SQUARE,SQUARE,""+token.getStep().getId());
				g.setColor(Color.blue);
				GraphicsUtil.drawCenteredString(g1,token.getDrawX()+1,token.getDrawY()-2,SQUARE,SQUARE,""+token.getStep().getId());
			}
		}
	}
	protected void layoutTokens() {
		int rankSize = (maxRank+1)*SQUARE+(BORDER<<1) - SQUARE;
		int displayOrderSize = ((maxDisplayOrder+1)*SQUARE)+(BORDER<<2);
		int width = orientation==SwingConstants.HORIZONTAL?rankSize:displayOrderSize;
		int height = orientation==SwingConstants.HORIZONTAL?displayOrderSize:rankSize;
		ComponentTools.lockComponentSize(this,width,height);
		setBorder(BorderFactory.createEtchedBorder());
		Hashtable hash = new Hashtable();
		for (java.util.Iterator _j14it2259 = (tokens).iterator(); _j14it2259.hasNext(); ) {
		  QuestStepToken token = (QuestStepToken) _j14it2259.next();
			int viewRank = token.getViewRank();
			if (hash.containsKey(Integer.valueOf(viewRank))) {
				hash.put(Integer.valueOf(viewRank), Integer.valueOf(((Integer) hash.get(Integer.valueOf(viewRank))).intValue()+1));
			}
			else {
				hash.put(Integer.valueOf(viewRank), Integer.valueOf(1));
			}
		}
		for (java.util.Iterator _j14it2260 = (tokens).iterator(); _j14it2260.hasNext(); ) {
		  QuestStepToken token = (QuestStepToken) _j14it2260.next();
			token.initOrientation(orientation,BORDER,SQUARE,displayOrderSize,((Integer) hash.get(Integer.valueOf(token.getViewRank()))).intValue());
		}
	}
	protected void rebuildTokens(ArrayList steps) {
		maxRank = 0;
		maxDisplayOrder = 0;
		tokens = new ArrayList();
		if (steps==null) return;
		steps = new ArrayList(steps); // make a copy that we can modify
		ArrayList origins = new ArrayList();
		int displayOrder=0;
		ArrayList toRemove = new ArrayList();
		for (java.util.Iterator _j14it2261 = (steps).iterator(); _j14it2261.hasNext(); ) {
		  QuestStep step = (QuestStep) _j14it2261.next();
			if (step.isOrigin()) {
				QuestStepToken token = new QuestStepToken(step);
				token.setViewRank(0);
				maxDisplayOrder = Math.max(displayOrder,maxDisplayOrder);
				token.setDisplayOrder(displayOrder++);
				origins.add(token);
				toRemove.add(step);
			}
		}
		steps.removeAll(toRemove);
		int currentRank = 1;
		while(origins.size()>0) {
			tokens.addAll(origins);
			boolean allVirtual = true;
			for (java.util.Iterator _j14it2262 = (origins).iterator(); _j14it2262.hasNext(); ) {
			  QuestStepToken test = (QuestStepToken) _j14it2262.next();
				if (!test.isVirtual()) {
					allVirtual = false;
					break;
				}
			}
			if (allVirtual) break;
			displayOrder = 0;
			ArrayList virtualCovered = new ArrayList();
			ArrayList newTokens = new ArrayList();
			for (java.util.Iterator _j14it2263 = (origins).iterator(); _j14it2263.hasNext(); ) {
			  QuestStepToken token = (QuestStepToken) _j14it2263.next();
				//if (token.isVirtual()) continue;
				toRemove.clear();
				for (java.util.Iterator _j14it2264 = (steps).iterator(); _j14it2264.hasNext(); ) {
				  QuestStep step = (QuestStep) _j14it2264.next();
					boolean sameAsToken = step.getId()==token.getStep().getId();
					boolean tokenIsRequiredByStep = step.requires(token.getStep()) || step.requiresFail(token.getStep());
					if (!sameAsToken && !tokenIsRequiredByStep) continue;
					if (tokenIsRequiredByStep && token.isVirtual()) continue;
					if (virtualCovered.contains(step)) continue;
					maxDisplayOrder = Math.max(displayOrder,maxDisplayOrder);
					QuestStepToken newToken = new QuestStepToken(step);
					newToken.setViewRank(currentRank);
					newToken.setDisplayOrder(displayOrder++);
					newToken.setVirtual(!newToken.allRequiredPresent(tokens));
					newTokens.add(newToken);
					maxRank = currentRank;
					if (newToken.isVirtual()) {
						virtualCovered.add(step);
					}
					else {
						toRemove.add(step);
					}
				}
				steps.removeAll(toRemove);
			}
			origins = newTokens;
			currentRank++;
		}
//		System.out.println("------------------");
//		System.out.println(maxRank);
//		for (java.util.Iterator _j14it2265 = (tokens).iterator(); _j14it2265.hasNext(); ) {
//		  QuestStepToken token = (QuestStepToken) _j14it2265.next();
//			System.out.println(token);
//		}
	}
	private static ArrayList getTestSteps1() {
		GameData data = new GameData();
		ArrayList steps = new ArrayList();
		QuestStep step1 = new QuestStep(data.createNewObject());
		step1.setName("Step 1");
		step1.setId(1);
		QuestStep step2 = new QuestStep(data.createNewObject());
		step2.setName("Step 2");
		step2.setId(2);
		QuestStep step3 = new QuestStep(data.createNewObject());
		step3.setName("Step 3");
		step3.setId(3);
		QuestStep step4 = new QuestStep(data.createNewObject());
		step4.setName("Step 4");
		step4.setId(4);
		QuestStep step5 = new QuestStep(data.createNewObject());
		step5.setName("Step 5");
		step5.setId(5);
		QuestStep step6 = new QuestStep(data.createNewObject());
		step6.setName("Step 6");
		step6.setId(6);
		step2.addRequiredStep(step1);
		step3.addRequiredStep(step1);
		step4.addRequiredStep(step1);
		step5.addRequiredStep(step3);
		step6.addRequiredStep(step2);
		step6.addRequiredStep(step4);
		step6.addRequiredStep(step5);
		steps.add(step1);
		steps.add(step2);
		steps.add(step3);
		steps.add(step4);
		steps.add(step5);
		steps.add(step6);
		return steps;
	}
	private static ArrayList getTestSteps2() {
		GameData data = new GameData();
		ArrayList steps = new ArrayList();
		QuestStep step1 = new QuestStep(data.createNewObject());
		step1.setName("Step 1");
		step1.setId(1);
		QuestStep step2 = new QuestStep(data.createNewObject());
		step2.setName("Step 2");
		step2.setId(2);
		QuestStep step3 = new QuestStep(data.createNewObject());
		step3.setName("Step 3");
		step3.setId(3);
		QuestStep step4 = new QuestStep(data.createNewObject());
		step4.setName("Step 4");
		step4.setId(4);
		QuestStep step5 = new QuestStep(data.createNewObject());
		step5.setName("Step 5");
		step5.setId(5);
		QuestStep step6 = new QuestStep(data.createNewObject());
		step6.setName("Step 6");
		step6.setId(6);
		step3.addRequiredStep(step1);
		step4.addRequiredStep(step2);
		step5.addRequiredStep(step3);
		step5.addRequiredStep(step4);
		step6.addRequiredStep(step5);
		steps.add(step1);
		steps.add(step2);
		steps.add(step3);
		steps.add(step4);
		steps.add(step5);
		steps.add(step6);
		return steps;
	}
	private static ArrayList getTestSteps3() {
		GameData data = new GameData();
		ArrayList steps = new ArrayList();
		QuestStep step1 = new QuestStep(data.createNewObject());
		step1.setName("Step 1");
		step1.setId(1);
		QuestStep step2 = new QuestStep(data.createNewObject());
		step2.setName("Step 2");
		step2.setId(2);
		QuestStep step3 = new QuestStep(data.createNewObject());
		step3.setName("Step 3");
		step3.setId(3);
		QuestStep step4 = new QuestStep(data.createNewObject());
		step4.setName("Step 4");
		step4.setId(4);
		QuestStep step5 = new QuestStep(data.createNewObject());
		step5.setName("Step 5");
		step5.setId(5);
		QuestStep step6 = new QuestStep(data.createNewObject());
		step6.setName("Step 6");
		step6.setId(6);
		step3.addRequiredStep(step1);
		step4.addRequiredStep(step1);
		step4.addRequiredStep(step2);
		step5.addRequiredStep(step3);
		step5.addRequiredStep(step2);
		step6.addRequiredStep(step4);
		step6.addRequiredStep(step5);
		steps.add(step1);
		steps.add(step2);
		steps.add(step3);
		steps.add(step4);
		steps.add(step5);
		steps.add(step6);
		return steps;
	}
	private static ArrayList getTestSteps4() {
		GameData data = new GameData();
		ArrayList steps = new ArrayList();
		QuestStep step1 = new QuestStep(data.createNewObject());
		step1.setName("Step 1");
		step1.setId(1);
		QuestStep step2 = new QuestStep(data.createNewObject());
		step2.setName("Step 2");
		step2.setId(2);
		QuestStep step3 = new QuestStep(data.createNewObject());
		step3.setName("Step 3");
		step3.setId(3);
		QuestStep step4 = new QuestStep(data.createNewObject());
		step4.setName("Step 4");
		step4.setId(4);
		QuestStep step5 = new QuestStep(data.createNewObject());
		step5.setName("Step 5");
		step5.setId(5);
		QuestStep step6 = new QuestStep(data.createNewObject());
		step6.setName("Step 6");
		step6.setId(6);
		step2.addRequiredStep(step1);
		step3.addRequiredStep(step1);
		step4.addRequiredStep(step2);
		step5.addRequiredStep(step4);
		step6.addRequiredStep(step1);
		step6.addRequiredStep(step3);
		step6.addRequiredStep(step5);
		steps.add(step1);
		steps.add(step2);
		steps.add(step3);
		steps.add(step4);
		steps.add(step5);
		steps.add(step6);
		return steps;
	}
	private static ArrayList getTestSteps5() {
		GameData data = new GameData();
		ArrayList steps = new ArrayList();
		QuestStep step1 = new QuestStep(data.createNewObject());
		step1.setName("Step 1");
		step1.setId(1);
		QuestStep step2 = new QuestStep(data.createNewObject());
		step2.setName("Step 2");
		step2.setId(2);
		QuestStep step3 = new QuestStep(data.createNewObject());
		step3.setName("Step 3");
		step3.setId(3);
		QuestStep step4 = new QuestStep(data.createNewObject());
		step4.setName("Step 4");
		step4.setId(4);
		QuestStep step5 = new QuestStep(data.createNewObject());
		step5.setName("Step 5");
		step5.setId(5);
		QuestStep step6 = new QuestStep(data.createNewObject());
		step6.setName("Step 6");
		step6.setId(6);
		step2.addRequiredStep(step1);
		step3.addRequiredStep(step2);
		step4.addRequiredStep(step3);
		step5.addRequiredStep(step2);
		step5.addRequiredStep(step4);
		step6.addRequiredStep(step5);
		steps.add(step1);
		steps.add(step2);
		steps.add(step3);
		steps.add(step4);
		steps.add(step5);
		steps.add(step6);
		return steps;
	}
	private static ArrayList getTestSteps6() {
		GameData data = new GameData();
		ArrayList steps = new ArrayList();
		QuestStep step1 = new QuestStep(data.createNewObject());
		step1.setName("Step 1");
		step1.setId(1);
		QuestStep step2 = new QuestStep(data.createNewObject());
		step2.setName("Step 2");
		step2.setId(2);
		QuestStep step3 = new QuestStep(data.createNewObject());
		step3.setName("Step 3");
		step3.setId(3);
		step2.addRequiredStep(step1);
		step2.addRequiredStep(step3);
		step3.addRequiredStep(step2);
		steps.add(step1);
		steps.add(step2);
		steps.add(step3);
		return steps;
	}
	private static boolean SHOW_VIRTUAL = false;
	public static void main(String[] args) {
		QuestStepView view = new QuestStepView();
		Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalGlue());
		box.add(view);
		box.add(Box.createHorizontalGlue());
		
		view.updateSteps(getTestSteps1());
		view.setPreferredSize(new Dimension(400,400));
		JOptionPane.showMessageDialog(null,box);
		
		view.updateSteps(getTestSteps2());
		view.setPreferredSize(new Dimension(400,400));
		JOptionPane.showMessageDialog(null,box);
		
		view.updateSteps(getTestSteps3());
		view.setPreferredSize(new Dimension(400,400));
		JOptionPane.showMessageDialog(null,box);
		
		view.updateSteps(getTestSteps4());
		view.setPreferredSize(new Dimension(400,400));
		JOptionPane.showMessageDialog(null,box);
		
		view.updateSteps(getTestSteps5());
		view.setPreferredSize(new Dimension(400,400));
		JOptionPane.showMessageDialog(null,box);
		
		view.updateSteps(getTestSteps6());
		view.setPreferredSize(new Dimension(400,400));
		JOptionPane.showMessageDialog(null,box);
		
		System.exit(0);
	}
}