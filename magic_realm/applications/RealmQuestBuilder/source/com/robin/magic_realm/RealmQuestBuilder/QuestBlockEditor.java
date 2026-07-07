package com.robin.magic_realm.RealmQuestBuilder;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;
import javax.swing.text.*;

import com.robin.game.objects.*;
import com.robin.general.swing.*;
import com.robin.magic_realm.RealmQuestBuilder.QuestPropertyBlock.FieldType;
import com.robin.magic_realm.components.ChitComponent;
import com.robin.magic_realm.components.RealmComponent;
import com.robin.magic_realm.components.quest.QuestConstants;
import com.robin.magic_realm.components.quest.reward.QuestRewardItem;
import com.robin.magic_realm.components.swing.RealmObjectChooser;
import com.robin.magic_realm.components.utility.TemplateLibrary;

public abstract class QuestBlockEditor extends GenericEditor {
	
	protected abstract String getEditorTitle();
	protected abstract ArrayList createPropertyBlocks();
	
	private JFrame parent;
	private ArrayList propertyBlocks;
	private GameObjectWrapper go;
	private ChitTypePanel chitTypePanel; // important to remember this one, in case it is used
	
	public QuestBlockEditor(JFrame parent, GameData realmSpeakData, GameObjectWrapper go) {
		super(parent, realmSpeakData);
		this.parent = parent;
		this.go = go;
	}
	public void setVisible(boolean val) {
		if (!propertyBlocks.isEmpty()) {
			super.setVisible(val);
		}
	}
	public boolean needsEdit() {
		return !propertyBlocks.isEmpty();
	}
	protected void read() {
		propertyBlocks = createPropertyBlocks();
		initComponents();
	}
	protected boolean isValidForm() {
		for (java.util.Iterator _j14it354 = (propertyBlocks).iterator(); _j14it354.hasNext(); ) {
		  QuestPropertyBlock block = (QuestPropertyBlock) _j14it354.next();
			if (block.getFieldType()==FieldType.NoSpacesTextLine) {
				String val = ((JTextField)block.getComponent()).getText();
				if (val==null || val.trim().length()==0) {
					JOptionPane.showMessageDialog(parent,"Value required for field \""+block.getFieldName()+"\"");
					return false;
				}
			}
			else if (block.getFieldType()==FieldType.Number) {
				int val = ((IntegerField)block.getComponent()).getInt();
				if (val<=0) {
					JOptionPane.showMessageDialog(parent,"Field \""+block.getFieldName()+"\" must be greater than zero.");
					return false;
				}
			}
		}
		return true;
	}
	protected void save() {
		for (java.util.Iterator _j14it355 = (propertyBlocks).iterator(); _j14it355.hasNext(); ) {
		  QuestPropertyBlock block = (QuestPropertyBlock) _j14it355.next();
			{
				FieldType _ft = block.getFieldType();
				if (_ft == FieldType.TextLine || _ft == FieldType.NoSpacesTextLine || _ft == FieldType.SmartTextLine || _ft == FieldType.Regex || _ft == FieldType.RegexIgnoreChitTypes) {
					go.setString(block.getKeyName(),((JTextField)block.getComponent()).getText().trim());
				} else if (_ft == FieldType.SmartTextArea || _ft == FieldType.TextArea) {
					go.setString(block.getKeyName(),((JTextArea)block.getComponent()).getText().trim());
				} else if (_ft == FieldType.Number || _ft == FieldType.NumberAll) {
					go.setInt(block.getKeyName(),((IntegerField)block.getComponent()).getInt());
				} else if (_ft == FieldType.StringSelector || _ft == FieldType.GameObjectWrapperSelector) {
					saveSelection(block,(JComboBox)block.getComponent());
				} else if (_ft == FieldType.Boolean) {
					go.setBoolean(block.getKeyName(),((JCheckBox)block.getComponent()).isSelected());
				} else if (_ft == FieldType.ChitType) {
					go.setList(block.getKeyName(),((ChitTypePanel)block.getComponent()).getChitTypeList());
				} else if (_ft == FieldType.CompanionSelector) {
					go.setString(QuestConstants.KEY_PREFIX+block.getKeyName(),((JLabel)block.getComponent()).getText());
					go.setString(QuestConstants.VALUE_PREFIX+block.getKeyName(),block.getComponent().getToolTipText());
				}
			}
		}
	}
	private void initComponents() {
		setTitle(getEditorTitle());
		setSize(450,450);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(buildForm(),BorderLayout.CENTER);
		getContentPane().add(buildOkCancelLine(),BorderLayout.SOUTH);
	}
	private Box buildForm() {
		UniformLabelGroup group = new UniformLabelGroup();
		Box box = Box.createVerticalBox();
		for (java.util.Iterator _j14it356 = (propertyBlocks).iterator(); _j14it356.hasNext(); ) {
		  QuestPropertyBlock block = (QuestPropertyBlock) _j14it356.next();
			Box line = group.createLabelLine(block.getFieldName());
			JComponent component = null;
			JButton button = null;
			boolean useScrollPane = false;
			{
				FieldType _ft2 = block.getFieldType();
				if (_ft2 == FieldType.TextLine) {
					component = new JTextField(go.getString(block.getKeyName()));
					ComponentTools.lockComponentSize(component,150,25);
				} else if (_ft2 == FieldType.SmartTextLine) {
					SuggestionTextField sta = new SuggestionTextField();
					sta.setText(go.getString(block.getKeyName()));
					sta.setWords(block.getSelectionsAsStrings());
					sta.setFont(UIManager.getFont("TextArea.font"));
					sta.setLineModeOn(true);
					sta.setAutoSpace(false);
					component = sta;
					ComponentTools.lockComponentSize(component,150,25);
				} else if (_ft2 == FieldType.SmartTextArea) {
					SuggestionTextArea star = new SuggestionTextArea();
					star.setText(go.getString(block.getKeyName()));
					star.setWords(block.getSelectionsAsStrings());
					star.setFont(UIManager.getFont("TextArea.font"));
					star.setLineModeOn(false);
					star.setAutoSpace(false);
					star.setWrapStyleWord(true);
					star.setLineWrap(true);
					component = star;
					ComponentTools.lockComponentSize(component,200,100);
				} else if (_ft2 == FieldType.NoSpacesTextLine) {
					JTextField tf = new JTextField();
					component = tf;
					ComponentTools.lockComponentSize(component,150,25);
					((JTextField)component).setDocument(new PlainDocument() {
						public void insertString (int offset, String  str, AttributeSet attr) throws BadLocationException {
							if (str == null) return;
							StringBuffer temp=new StringBuffer();
							for (int i=0;i<str.length();i++) {
								if (!Character.isWhitespace(str.charAt(i))) {
									temp.append(str.charAt(i));
								}
							}
							if (temp.length()>0)
								super.insertString(offset,temp.toString(),attr);
						}
					});
					tf.setText(go.getString(block.getKeyName()));
				} else if (_ft2 == FieldType.TextArea) {
					JTextArea ta = new JTextArea(15,40);
					ta.setLineWrap(true);
					ta.setWrapStyleWord(true);
					ta.setText(go.getString(block.getKeyName()));
					useScrollPane = true;
					component = ta;
				} else if (_ft2 == FieldType.Number) {
					int n = go.getInt(block.getKeyName());
					component = new IntegerField(n==0?1:n);
					ComponentTools.lockComponentSize(component,150,25);
				} else if (_ft2 == FieldType.NumberAll) {
					int i = go.getInt(block.getKeyName());
					component = new IntegerField(i);
					ComponentTools.lockComponentSize(component,150,25);
				} else if (_ft2 == FieldType.StringSelector || _ft2 == FieldType.GameObjectWrapperSelector) {
					JComboBox cb = new JComboBox(block.getSelections());
					component = cb;
					readSelection(block,cb);
					ComponentTools.lockComponentSize(component,200,25);
				} else if (_ft2 == FieldType.Boolean) {
					JCheckBox yesBox = new JCheckBox("",go.getBoolean(block.getKeyName()));
					yesBox.setFocusable(false);
					component = yesBox;
				} else if (_ft2 == FieldType.Regex) {
					component = new JTextField(go.getString(block.getKeyName()));
					ComponentTools.lockComponentSize(component,150,25);
					button = new PropertyButton("...",component,block);
					button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ev) {
							PropertyButton me = (PropertyButton)ev.getSource();
							JTextField field = (JTextField)me.getMyComponent();
							String text = launchRegexHelper(field.getText(),me.getBlock());
							if (text!=null) {
								field.setText(text);
							}
						}
					});
				} else if (_ft2 == FieldType.RegexIgnoreChitTypes) {
					component = new JTextField(go.getString(block.getKeyName()));
					ComponentTools.lockComponentSize(component,150,25);
					button = new PropertyButton("...",component,block);
					button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ev) {
							PropertyButton me = (PropertyButton)ev.getSource();
							JTextField field = (JTextField)me.getMyComponent();
							String text = launchRegexHelper(field.getText(),me.getBlock(),true);
							if (text!=null) {
								field.setText(text);
							}
						}
					});
				} else if (_ft2 == FieldType.ChitType) {
					chitTypePanel = new ChitTypePanel(go.getList(block.getKeyName()));
					component = chitTypePanel;
				} else if (_ft2 == FieldType.CompanionSelector) {
					JLabel label = new JLabel();
					component = label;
					String current = go.getString(block.getKeyName());
					if (current==null) {
						current = go.getString(QuestConstants.KEY_PREFIX+block.getKeyName());
					}
					if (current!=null && current.length()>0) {
						KeyValuePair[] _j14arr357 = (KeyValuePair[]) block.getSelections();
						for (int _j14i357 = 0; _j14i357 < _j14arr357.length; _j14i357++) {
						  KeyValuePair kv = _j14arr357[_j14i357];
							if (!kv.getKey().equals(current)) continue;
							GameObject go = TemplateLibrary.getSingleton().getCompanionTemplate(kv.getKey(),kv.getValue());
							ChitComponent chit = (ChitComponent)RealmComponent.getRealmComponent(go);
							label.setIcon(chit.getIcon());
							label.setText(kv.getKey());
							label.setToolTipText(kv.getValue());
							break;
						}
					}
					button = new PropertyButton("...",component,block);
					button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ev) {
							PropertyButton me = (PropertyButton)ev.getSource();
							JLabel field = (JLabel)me.getMyComponent();

							ChitComponent chit = selectCompanion((KeyValuePair[])me.getBlock().getSelections());
							if (chit!=null) {
								field.setIcon(chit.getIcon());
								field.setText(chit.getGameObject().getName());
								field.setToolTipText(chit.getGameObject().getThisAttribute("query")); // just a hacky way to capture the info
							}
						}
					});
				}
			}
			block.setComponent(component);
			if (useScrollPane) {
				line.add(new JScrollPane(component));
			}
			else {
				line.add(component);
			}
			if (button!=null) {
				line.add(Box.createHorizontalStrut(10));
				line.add(button);
			}
			line.add(Box.createHorizontalGlue());
			box.add(line);
		}
		box.add(Box.createVerticalGlue());
		return box;
	}
	private ChitComponent selectCompanion(KeyValuePair[] keyVals) {
		ArrayList list = new ArrayList();
		for (int _j14i358 = 0; _j14i358 < keyVals.length; _j14i358++) {
		  KeyValuePair kv = keyVals[_j14i358];
			GameObject go = TemplateLibrary.getSingleton().getCompanionTemplate(kv.getKey(),kv.getValue());
			list.add(go);
		}
		
		RealmObjectChooser chooser = new RealmObjectChooser("Choose one:",realmSpeakData,true,true);
		chooser.setValidateChosenObjects(false);
		chooser.addObjectsToChoose(list);
		chooser.setVisible(true);
		GameObject go = chooser.getChosenObject();
		return go==null?null:(ChitComponent)RealmComponent.getRealmComponent(go);
	}
	private String launchRegexHelper(String text,QuestPropertyBlock block) {
		return launchRegexHelper(text,block,false);
	}
	private String launchRegexHelper(String text,QuestPropertyBlock block,boolean ignoreChitTypePanel) {
		ArrayList objectNames = new ArrayList();
		if (chitTypePanel!=null&&!ignoreChitTypePanel) {
			ArrayList objects = QuestRewardItem.getObjectList(realmSpeakData.getGameObjects(),chitTypePanel.getChitItemTypes(),null); 
			for (java.util.Iterator _j14it359 = (objects).iterator(); _j14it359.hasNext(); ) {
			  GameObject go = (GameObject) _j14it359.next();
				if (!objectNames.contains(go.getName())) {
					objectNames.add(go.getName());
				}
			}
		}
		else {
			String[] keyVals = block.getKeyVals();
			if (keyVals!=null && keyVals.length>0) {
				GamePool pool = new GamePool(realmSpeakData.getGameObjects());
				for (int _j14i360 = 0; _j14i360 < keyVals.length; _j14i360++) {
				  String keyVal = keyVals[_j14i360];
					for (java.util.Iterator _j14it361 = (pool.find(keyVal)).iterator(); _j14it361.hasNext(); ) {
					  GameObject go = (GameObject) _j14it361.next();
						if (objectNames.contains(go.getName())) continue;
						objectNames.add(go.getName());
					}
				}
			}
			else {
				objectNames = new ArrayList(realmSpeakData.getAllGameObjectNames());
			}
		}
		Collections.sort(objectNames);
		RealmRegexHelper helper = new RealmRegexHelper(parent,text,objectNames);
		helper.setLocationRelativeTo(this);
		helper.setVisible(true);
		return helper.getText();
	}
	private void readSelection(QuestPropertyBlock block,JComboBox cb) {
		Object[] selections =block.getSelections(); 
		if (selections==null || selections.length==0) return;
		boolean kvPair = selections[0] instanceof KeyValuePair;
		
		int selIndex = 0; // default
		String current = null;
		if (block.getFieldType()==FieldType.GameObjectWrapperSelector) {
			String id = go.getString(block.getKeyName());
			if (id!=null) {
				GameObject ref = go.getGameData().getGameObject(new Long(id));
				if (ref != null) {
					current = ref.getName();
				}
			}
		}
		else {
			current = kvPair?go.getString(QuestConstants.KEY_PREFIX+block.getKeyName()):go.getString(block.getKeyName());
		}
		if (current!=null) {
			for (int i=0;i<selections.length;i++) {
				Object selection = selections[i];
				if (selection==null) continue;
				if (selection.toString().equals(current)) {
					selIndex = i;
					break;
				}
			}
		}
		cb.setSelectedIndex(selIndex);
	}
	private void saveSelection(QuestPropertyBlock block,JComboBox cb) {
		if (block.getFieldType()==FieldType.GameObjectWrapperSelector) {
			GameObjectWrapper gow = (GameObjectWrapper)cb.getSelectedItem();
			if (gow==null) {
				go.clear(block.getKeyName());
			}
			else {
				go.setString(block.getKeyName(),gow.getGameObject().getStringId());
			}
		}
		else {
			Object selected = cb.getSelectedItem();
			if (selected instanceof KeyValuePair) {
				KeyValuePair kv = (KeyValuePair)selected;
				go.setString(QuestConstants.KEY_PREFIX+block.getKeyName(),kv.getKey());
				go.setString(QuestConstants.VALUE_PREFIX+block.getKeyName(),kv.getValue());
			}
			else {
				go.setString(block.getKeyName(),selected.toString());
			}
		}
	}
	
	private static class PropertyButton extends ComponentButton {
		private QuestPropertyBlock block;
		public PropertyButton(String text, JComponent component,QuestPropertyBlock block) {
			super(text, component);
			this.block = block;
		}
		public QuestPropertyBlock getBlock() {
			return block;
		}
	}
}