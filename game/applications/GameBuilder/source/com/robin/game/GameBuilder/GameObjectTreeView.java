package com.robin.game.GameBuilder;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

import com.robin.game.objects.*;

public class GameObjectTreeView extends JFrame {
	protected JTree tree;
	
	public GameObjectTreeView(Collection gameObjects) {
		init(gameObjects);
	}
	private void init(Collection gameObjects) {
		setSize(400,500);
		getContentPane().setLayout(new BorderLayout());
			DefaultMutableTreeNode top = new DefaultMutableTreeNode("top");
			
			// Add all base objects (not held by anything)
			Hashtable hash = new Hashtable();
			for (java.util.Iterator _j14it64 = (gameObjects).iterator(); _j14it64.hasNext(); ) {
			  GameObject object = (GameObject) _j14it64.next();
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(object);
				if (object.getHeldBy()==null) {
					top.add(node);
				}
				hash.put(object.toString(),node);
			}
			
			// Now use the hash to add all the branches
			for (java.util.Iterator _j14it65 = (gameObjects).iterator(); _j14it65.hasNext(); ) {
			  GameObject object = (GameObject) _j14it65.next();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) hash.get(object.toString());
				for (java.util.Iterator _j14it66 = (object.getHold()).iterator(); _j14it66.hasNext(); ) {
				  GameObject heldObject = (GameObject) _j14it66.next();
					DefaultMutableTreeNode child = (DefaultMutableTreeNode) hash.get(heldObject.toString());
					if (child!=null) {
						node.add(child);
					}
				}
			}
			
			tree = new JTree(top);
			tree.setRootVisible(false);
			tree.setShowsRootHandles(true);
		getContentPane().add(new JScrollPane(tree));
	}
}