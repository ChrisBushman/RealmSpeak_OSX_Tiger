package com.robin.magic_realm.RealmQuestBuilder;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.game.objects.GameObjectBlockManager;
import com.robin.general.swing.ComponentTools;
import com.robin.magic_realm.RealmCharacterBuilder.EditPanel.*;
import com.robin.magic_realm.components.CharacterActionChitComponent;
import com.robin.magic_realm.components.quest.QuestMinorCharacter;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;

public class AbilityEditor extends GenericEditor {
	public static final class AbilityType {
		private final String _name;
		private final int _ordinal;
		private AbilityType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final AbilityType ColorBlocking = new AbilityType("ColorBlocking", 0);
		public static final AbilityType ColorSource = new AbilityType("ColorSource", 1);
		public static final AbilityType DieModification = new AbilityType("DieModification", 2);
		public static final AbilityType ExtraAction = new AbilityType("ExtraAction", 3);
		public static final AbilityType ExtraChit = new AbilityType("ExtraChit", 4);
		public static final AbilityType MiscellaneousAbilities = new AbilityType("MiscellaneousAbilities", 5);
		public static final AbilityType MonsterInteraction = new AbilityType("MonsterInteraction", 6);
		public static final AbilityType MonsterImmunity = new AbilityType("MonsterImmunity", 7);
		public static final AbilityType MonsterControl = new AbilityType("MonsterControl", 8);
		public static final AbilityType MonsterFear = new AbilityType("MonsterFear", 9);
		public static final AbilityType MonsterFriendliness = new AbilityType("MonsterFriendliness", 10);
		public static final AbilityType TreasureLocationFear = new AbilityType("TreasureLocationFear", 11);
		public static final AbilityType SpecialAction = new AbilityType("SpecialAction", 12);
		public static final AbilityType TacticsChange = new AbilityType("TacticsChange", 13);

		private static final AbilityType[] _VALUES = { ColorBlocking, ColorSource, DieModification, ExtraAction, ExtraChit, MiscellaneousAbilities, MonsterInteraction, MonsterImmunity, MonsterControl, MonsterFear, MonsterFriendliness, TreasureLocationFear, SpecialAction, TacticsChange };
		public static AbilityType[] values() { AbilityType[] r = new AbilityType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static AbilityType valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}

	public static final String TEMPLATE_ABILITY_BLOCK = "design";
	private CharacterWrapper template;
	private AbilityType type;
	private AdvantageEditPanel editPanel;

	public AbilityEditor(JFrame frame,String title,AbilityType type,CharacterWrapper template) {
		super(frame,null);
		this.setTitle(title);
		this.type = type;
		this.template = template;//new CharacterWrapper(GameObject.createEmptyGameObject());
		initComponents();
	}
	public void update(GameObject target,String targetBlock) {
		target.copyAttributeBlockFrom(template.getGameObject(),TEMPLATE_ABILITY_BLOCK);
		target.removeAttributeBlock(targetBlock);
		target.copyAttributeBlock(TEMPLATE_ABILITY_BLOCK,targetBlock);
		target.removeAttributeBlock(TEMPLATE_ABILITY_BLOCK);
		target.setAttribute(targetBlock,QuestMinorCharacter.ABILITY_DESCRIPTION,editPanel.getSuggestedDescription());
		target.setAttribute(targetBlock,QuestMinorCharacter.ABILITY_TYPE,type.toString());
		
		GameObjectBlockManager manTarget = new GameObjectBlockManager(target);
		manTarget.clearBlocks(Constants.BONUS_CHIT+TEMPLATE_ABILITY_BLOCK);
		GameObjectBlockManager manTemplate = new GameObjectBlockManager(template.getGameObject());
		GameObject go = manTemplate.extractGameObjectFromBlocks(Constants.BONUS_CHIT+TEMPLATE_ABILITY_BLOCK,true);
		if (go != null) {
			CharacterActionChitComponent extraChit = new CharacterActionChitComponent(go);
			manTarget.storeGameObjectInBlocks(extraChit.getGameObject(),Constants.BONUS_CHIT+TEMPLATE_ABILITY_BLOCK);
		}
	}
	protected boolean isValidForm() {
		return true;
	}
	protected void save() {
		editPanel.apply();
	}
	private void initComponents() {
		setSize(800,1000);
		setLayout(new BorderLayout());
		editPanel = null;
		JDialog frame = new JDialog();
		AbilityType _at = type;
		if (_at == AbilityType.ColorBlocking) {
			editPanel = new ColorBlockingEditPanel(template,TEMPLATE_ABILITY_BLOCK);
		} else if (_at == AbilityType.ColorSource) {
			editPanel = new ColorSourceEditPanel(template,TEMPLATE_ABILITY_BLOCK);
		} else if (_at == AbilityType.DieModification) {
			editPanel = new DieModEditPanel(template,TEMPLATE_ABILITY_BLOCK);
		} else if (_at == AbilityType.ExtraAction) {
			editPanel = new ExtraActionEditPanel(template,TEMPLATE_ABILITY_BLOCK);
		} else if (_at == AbilityType.ExtraChit) {
			editPanel = new ExtraChitEditPanel(frame, template,TEMPLATE_ABILITY_BLOCK);
		} else if (_at == AbilityType.MiscellaneousAbilities) {
			editPanel = new MiscellaneousEditPanel(template,TEMPLATE_ABILITY_BLOCK);
		} else if (_at == AbilityType.MonsterInteraction || _at == AbilityType.MonsterImmunity) {
			editPanel = new MonsterInteractionEditPanel(template,TEMPLATE_ABILITY_BLOCK,Constants.MONSTER_IMMUNITY);
		} else if (_at == AbilityType.MonsterControl) {
			editPanel = new MonsterInteractionEditPanel(template,TEMPLATE_ABILITY_BLOCK,Constants.MONSTER_CONTROL);
		} else if (_at == AbilityType.MonsterFear) {
			editPanel = new MonsterInteractionEditPanel(template,TEMPLATE_ABILITY_BLOCK,Constants.MONSTER_FEAR);
		} else if (_at == AbilityType.MonsterFriendliness) {
			editPanel = new MonsterInteractionEditPanel(template,TEMPLATE_ABILITY_BLOCK,Constants.MONSTER_FRIENDLINESS);
		} else if (_at == AbilityType.TreasureLocationFear) {
			editPanel = new TreasureLocationEditPanel(template,TEMPLATE_ABILITY_BLOCK);
		} else if (_at == AbilityType.SpecialAction) {
			editPanel = new SpecialActionEditPanel(template,TEMPLATE_ABILITY_BLOCK);
		} else if (_at == AbilityType.TacticsChange) {
			editPanel = new TacticsChangeEditPanel(template,TEMPLATE_ABILITY_BLOCK);
		}
		if (editPanel==null) {
			throw new IllegalStateException("Unsupported AbilityType "+type.toString());
		}
		add(editPanel);
		add(buildOkCancelLine(),BorderLayout.SOUTH);
	}
	public static void main(String[] args) {
		ComponentTools.setSystemLookAndFeel();
		AbilityEditor editor = new AbilityEditor(new JFrame(),"Ability Editor Test",AbilityType.DieModification,new CharacterWrapper(GameObject.createEmptyGameObject()));
		editor.setLocationRelativeTo(null);
		editor.setVisible(true);
		if (!editor.getCanceledEdit()) {
			// Do something
		}
		System.exit(0);
	}
}