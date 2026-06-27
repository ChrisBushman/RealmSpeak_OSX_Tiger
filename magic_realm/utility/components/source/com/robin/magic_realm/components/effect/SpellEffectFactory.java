package com.robin.magic_realm.components.effect;

import com.robin.magic_realm.components.utility.Constants;

public class SpellEffectFactory {
	public static ISpellEffect[] create(String spellName){
		String n = spellName.toLowerCase();
		if ("absorb essence".equals(n)) return new ISpellEffect[]{new TransmorphEffect("target")};
		if ("animate".equals(n)) return new ISpellEffect[]{new AnimateEffect()};
		if ("ask demon".equals(n)) return new ISpellEffect[]{new AskDemonEffect()};

		//if ("bad luck".equals(n)) I AM NOT SURE WHAT GOES HERE
		if ("bewilder".equals(n)) return new ISpellEffect[]{new ApplyClearingEffect("bewildered")};
		if ("blazing light".equals(n)) return new ISpellEffect[]{new ExtraCavePhaseEffect()};
		if ("blazing light x".equals(n)) return new ISpellEffect[]{new ApplyNamedEffect(Constants.TORCH_BEARER)};

		if ("blend into background".equals(n)) return new ISpellEffect[]{new ExtraActionEffect("H")};
		if ("blend into background x".equals(n)) return new ISpellEffect[]{new ExtraActionEffect("H")};
		if ("blunting".equals(n)) return new ISpellEffect[]{new ApplyClearingEffect("blunted")};
		if ("blur".equals(n)) return new ISpellEffect[]{new FinalChitSpeedEffect()};

		if ("broomstick".equals(n)) return new ISpellEffect[]{new FlyChitEffect()};

		if ("control bats".equals(n)) return new ISpellEffect[]{new ControlEffect()};
		if ("curse".equals(n)) return new ISpellEffect[]{new CurseEffect()};

		if ("exorcise".equals(n)) return new ISpellEffect[]{new ExorciseEffect()};

		if ("deal with goblins".equals(n)) return new ISpellEffect[]{new PacifyEffect(0)};
		if ("disguise".equals(n)) return new ISpellEffect[]{new PacifyEffect(0)};
		if ("disjunction".equals(n)) return new ISpellEffect[]{new ApplyNamedEffect("no_w_fat"),new ApplyNamedEffect("no_ter_harm")};

		if ("dissolve spell".equals(n)) return new ISpellEffect[]{new CancelEffect()};
		if ("divine might".equals(n)) return new ISpellEffect[]{new ApplyNamedEffect(Constants.STRONG_MF)};
		if ("divine protection".equals(n)) return new ISpellEffect[]{new ApplyNamedEffect("adds_armor")};

		if ("elemental power".equals(n)) return new ISpellEffect[]{new ForcedEnchantEffect()};
		if ("elemental spirit".equals(n)) return new ISpellEffect[]{new ChitChangeEffect()};
		if ("elven grace".equals(n)) return new ISpellEffect[]{new MoveSpeedChangeEffect()};

		if ("enchant artifact".equals(n)) return new ISpellEffect[]{new EnchantEffect()};
		if ("eternal servant".equals(n)) return new ISpellEffect[]{new NoWeightEffect()};

		if ("fae guard".equals(n)) return new ISpellEffect[]{new SummonFairyEffect()};
		if ("faerie lights".equals(n)) return new ISpellEffect[]{new ChitChangeEffect()};
		if ("filcher".equals(n)) return new ISpellEffect[]{new FilcherEffect()};

		if ("flame staff".equals(n)) return new ISpellEffect[]{new AddSharpnessEffect(2)};
		if ("fog".equals(n)) return new ISpellEffect[]{new ApplyNamedEffect(Constants.SP_NO_PEER)};

		if ("gravity".equals(n)) return new ISpellEffect[]{new ApplyClearingEffect("heavied")};
		if ("guide spider or octopus".equals(n)) return new ISpellEffect[]{new ControlEffect()};

		if ("heal".equals(n)) return new ISpellEffect[]{new HealChitEffect()};
		if ("hop".equals(n)) return new ISpellEffect[]{new TeleportEffect("RandomClearing")};
		if ("hurricane winds".equals(n)) return new ISpellEffect[]{new HurricaneWindsEffect()};
		if ("hypnotize".equals(n)) return new ISpellEffect[]{new ControlEffect()};

		if ("levitate".equals(n)) return new ISpellEffect[]{new NoWeightEffect()};
		if ("lost".equals(n)) return new ISpellEffect[]{new ApplyNamedEffect(Constants.SP_MOVE_IS_RANDOM)};
		if ("mage guard".equals(n)) return new ISpellEffect[]{new MageGuardEffect()};

		if ("make whole".equals(n)) return new ISpellEffect[]{new MakeWholeEffect()};
		if ("melt into mist".equals(n)) return new ISpellEffect[]{new NullifyEffect(),new DisengageEffect(), new TransmorphEffect("mist")};
		if ("miracle".equals(n)) return new ISpellEffect[]{new MiracleEffect()};
		if ("open gate".equals(n)) return new ISpellEffect[]{new TeleportEffect("KnownGate")};

		if ("peace".equals(n)) return new ISpellEffect[]{new PeaceEffect()};
		//if ("peace with nature".equals(n)) return new ISpellEffect[]{new ApplyNamedEffect(Constants.PEACE_WITH_NATURE)};

		if ("pentangle".equals(n)) return new ISpellEffect[]{new NullifyEffect()};
		if ("persuade".equals(n)) return new ISpellEffect[]{new PacifyEffect(1)};

		if ("phantasm".equals(n)) return new ISpellEffect[]{new PhantasmEffect()};
		if ("poison".equals(n)) return new ISpellEffect[]{new AddSharpnessEffect(1)};
		if ("power of the pit".equals(n)) return new ISpellEffect[]{new PowerPitEffect()};

		if ("prayer".equals(n)) return new ISpellEffect[]{new ExtraActionEffect("R")};
		if ("premonition".equals(n)) return new ISpellEffect[]{new ApplyNamedEffect(Constants.CHOOSE_TURN)};
		if ("prophecy".equals(n)) return new ISpellEffect[]{new ApplyNamedEffect(Constants.DAYTIME_ACTIONS)};
		if ("protection from magic".equals(n)) return new ISpellEffect[]{new PhaseChitEffect(), new NullifyEffect()};

		if ("raise dead".equals(n)) return new ISpellEffect[]{new SummonEffect("undead")};
		if ("remedy".equals(n)) return new ISpellEffect[]{new CancelEffect()};
		if ("repair armor".equals(n)) return new ISpellEffect[]{new RepairEffect()};
		if ("reverse power".equals(n)) return new ISpellEffect[]{new ColorModEffect()};

		if ("see/change weather".equals(n)) return new ISpellEffect[]{new SeeChangeWeatherEffect()};
		if ("see hidden signs".equals(n)) return new ISpellEffect[]{new ExtraActionEffect("S")};
		if ("see hidden signs x".equals(n)) return new ISpellEffect[]{new ExtraActionEffect("S")};
		if ("send".equals(n)) return new ISpellEffect[]{new ControlEffect()};

		if ("sense danger".equals(n)) return new ISpellEffect[]{new ExtraActionEffect("A")};
		if ("serpent tongue".equals(n)) return new ISpellEffect[]{new ControlEffect()};
		if ("shrink".equals(n)) return new ISpellEffect[]{new ApplyNamedEffect("shrink")};

		if ("slow monster".equals(n)) return new ISpellEffect[]{new ApplyNamedEffect("slowed")};
		if ("small blessing".equals(n)) return new ISpellEffect[]{new SmallBlessingEffect()};
		if ("sparkle".equals(n)) return new ISpellEffect[]{new UnassignEffect()};

		if ("spirit guide".equals(n)) return new ISpellEffect[]{new ApplyNamedEffect(Constants.SPIRIT_GUIDE)};
		if ("staff to snake".equals(n)) return new ISpellEffect[]{new ChangeToCompanionEffect()};
		if ("stone gaze".equals(n)) return new ISpellEffect[]{new PetrifyEffect()};

		if ("summon aid".equals(n)) return new ISpellEffect[]{new SummonAidEffect()};
		if ("summon animal".equals(n)) return new ISpellEffect[]{new SummonEffect("animal")};
		if ("summon elemental".equals(n)) return new ISpellEffect[]{new SummonEffect("elemental")};
		if ("sword song".equals(n)) return new ISpellEffect[]{new ApplyNamedEffect("alerted_weapon"), new AlertWeaponEffect()};

		if ("talk to wise bird".equals(n)) return new ISpellEffect[]{new InstantPeerEffect()};
		if ("teleport".equals(n)) return new ISpellEffect[]{new TeleportEffect("ChooseTileTwo")};

		if ("transform".equals(n)) return new ISpellEffect[]{new TransmorphEffect("roll")};

		if ("unleash power".equals(n)) return new ISpellEffect[]{new ActionChangeEffect()};

		if ("vale walker".equals(n)) return new ISpellEffect[]{new ApplyNamedEffect(Constants.VALE_WALKER)};
		if ("violent storm".equals(n)) return new ISpellEffect[]{new ViolentStormEffect()};
		if ("vision".equals(n)) return new ISpellEffect[]{new DiscoverRoadEffect()};

		if ("whistle for monsters".equals(n)) return new ISpellEffect[]{new MoveSoundEffect()};
		if ("witch's brew".equals(n)) return new ISpellEffect[]{new ChitChangeEffect()};

		return null;
	}
}
