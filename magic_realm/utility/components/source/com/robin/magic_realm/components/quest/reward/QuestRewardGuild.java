package com.robin.magic_realm.components.quest.reward;

import javax.swing.JFrame;

import com.robin.game.objects.GameObject;
import com.robin.magic_realm.components.quest.QuestConstants;
import com.robin.magic_realm.components.store.GuildStore;
import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.wrapper.CharacterWrapper;
import com.robin.magic_realm.components.wrapper.HostPrefWrapper;

public class QuestRewardGuild extends QuestReward {
	public static final String GUILD = "_guild";
	public static final String GUILD_CHANGE = "_guild_change";
	public static final String GUILD_LEVEL = "_guild_lvl";
	public static final String GUILD_RESET = "_guild_reset";
	
	public static final class GuildGainType {
		private final String _name;
		private final int _ordinal;
		private GuildGainType(String name, int ordinal) { this._name = name; this._ordinal = ordinal; }
		public String toString() { return _name; }
		public String name() { return _name; }
		public int ordinal() { return _ordinal; }
		public boolean equals(Object o) { return this == o; }
		public int hashCode() { return _ordinal; }
		private int _thisOrdinal() { return _ordinal; }

		public static final GuildGainType Increase = new GuildGainType("Increase", 0);
		public static final GuildGainType Decrease = new GuildGainType("Decrease", 1);
		public static final GuildGainType Set = new GuildGainType("Set", 2);
		public static final GuildGainType NoChange = new GuildGainType("NoChange", 3);

		private static final GuildGainType[] _VALUES = { Increase, Decrease, Set, NoChange };
		public static GuildGainType[] values() { GuildGainType[] r = new GuildGainType[_VALUES.length]; System.arraycopy(_VALUES,0,r,0,_VALUES.length); return r; }
		public static GuildGainType valueOf(String s) {
			for (int i=0;i<_VALUES.length;i++) if (_VALUES[i]._name.equals(s)) return _VALUES[i];
			throw new IllegalArgumentException(s);
		}
	}
	
	public QuestRewardGuild(GameObject go) {
		super(go);
	}

	public void processReward(JFrame frame,CharacterWrapper character) {
		HostPrefWrapper hostPrefs = HostPrefWrapper.findHostPrefs(character.getGameData());
		if (getGuildName().matches(QuestConstants.REMOVE)) {
			character.clearGuild();
			if (hostPrefs.hasPref(Constants.GUILDS_LOOSE_BENEFITS)) {
				GuildStore guild = character.getCurrentGuildStore();
				guild.unapplyAllGuildBenefits(frame, character);
			}
			return;
		}
		if (!getGuildName().matches(QuestConstants.CURRENT)) {
			if (resetGuildLevelForGuildChange() && !getGuildName().matches(character.getCurrentGuild())) {
				if (hostPrefs.hasPref(Constants.GUILDS_START_LEVEL)) {
					character.setCurrentGuildLevel(0);
				} else {
					character.setCurrentGuildLevel(1);
				}
				if (hostPrefs.hasPref(Constants.GUILDS_LOOSE_BENEFITS)) {
					GuildStore guild = character.getCurrentGuildStore();
					guild.unapplyAllGuildBenefits(frame, character);
				}
			}
			character.setCurrentGuild(getGuildName());
		}
		
		GuildGainType _gc = getGuildChange();
		if (_gc == GuildGainType.Increase) {
			if (hostPrefs.hasPref(Constants.GUILDS_MAX_LVL_3) && character.getCurrentGuildLevel()>=3) {
				return;
			}
			character.setCurrentGuildLevel(character.getCurrentGuildLevel()+1);
			if (hostPrefs.hasPref(Constants.GUILDS_BENEFITS) || character.getCurrentGuildLevel()==3) {
				GuildStore guild = character.getCurrentGuildStore();
				guild.applyGuildBenefit(frame,character,character.getCurrentGuildLevel());
			}
		} else if (_gc == GuildGainType.Decrease) {
			if (hostPrefs.hasPref(Constants.GUILDS_MIN_LVL_1) && character.getCurrentGuildLevel()<=1) {
				return;
			}
			if (hostPrefs.hasPref(Constants.GUILDS_MIN_LVL_0) && character.getCurrentGuildLevel()<=0) {
				return;
			}
			character.setCurrentGuildLevel(character.getCurrentGuildLevel()-1);
			if (hostPrefs.hasPref(Constants.GUILDS_LOOSE_BENEFITS)) {
				GuildStore guild = character.getCurrentGuildStore();
				guild.unapplyGuildBenefit(frame,character,character.getCurrentGuildLevel()+1);
			}
		} else if (_gc == GuildGainType.Set) {
			int lvlBefore = character.getCurrentGuildLevel();
			if (hostPrefs.hasPref(Constants.GUILDS_MAX_LVL_3) && getGuildLevel()>=3) {
				character.setCurrentGuildLevel(3);
			} else if (hostPrefs.hasPref(Constants.GUILDS_MIN_LVL_1) && getGuildLevel()<=1) {
				character.setCurrentGuildLevel(1);
			} else if (hostPrefs.hasPref(Constants.GUILDS_MIN_LVL_0) && getGuildLevel()<=0) {
				character.setCurrentGuildLevel(0);
			} else {
				character.setCurrentGuildLevel(getGuildLevel());
			}
			if (hostPrefs.hasPref(Constants.GUILDS_LOOSE_BENEFITS) || character.getCurrentGuildLevel()==3) {
				if (lvlBefore>character.getCurrentGuildLevel()) {
					GuildStore guild = character.getCurrentGuildStore();
					while (lvlBefore>character.getCurrentGuildLevel()) {
						guild.unapplyGuildBenefit(frame,character,lvlBefore);
						lvlBefore--;
					}
				}
			}
			if (hostPrefs.hasPref(Constants.GUILDS_BENEFITS) || character.getCurrentGuildLevel()==3) {
				if (lvlBefore<character.getCurrentGuildLevel()) {
					GuildStore guild = character.getCurrentGuildStore();
					while (lvlBefore<character.getCurrentGuildLevel()) {
						guild.applyGuildBenefit(frame,character,lvlBefore+1);
						lvlBefore++;
					}
				}
			}
		}
		// NoChange: do nothing
	}	
		
	public String getDescription() {
		StringBuffer sb = new StringBuffer();
		if (getGuildName().matches(QuestConstants.REMOVE)) {
			return "Removes the characters guild membership.";
		}
		GuildGainType _gc2 = getGuildChange();
		if (_gc2 == GuildGainType.Increase) {
			sb.append("Increase ");
		} else if (_gc2 == GuildGainType.Decrease) {
			sb.append("Deacrease ");
		} else if (_gc2 == GuildGainType.Set || _gc2 == GuildGainType.NoChange) {
			return "Set characters guild to "+getGuildName();
		}
		if (getGuildName().matches(QuestConstants.CURRENT)) {
			sb.append("characters current guild level");
		}
		else {
			sb.append("characters guild level of "+getGuildName());
		}
		if (getGuildChange() == GuildGainType.Set) {
			sb.append(" to "+getGuildLevel());
		}
		
		sb.append(".");
		return sb.toString();
	}
	public RewardType getRewardType() {
		return RewardType.Guild;
	}
	private GuildGainType getGuildChange() {
		return GuildGainType.valueOf(getString(GUILD_CHANGE));
	}
	private int getGuildLevel() {
		return getInt(GUILD_LEVEL);
	}
	private String getGuildName() {
		return getString(GUILD);
	}
	private boolean resetGuildLevelForGuildChange() {
		return getBoolean(GUILD_RESET);
	}
}