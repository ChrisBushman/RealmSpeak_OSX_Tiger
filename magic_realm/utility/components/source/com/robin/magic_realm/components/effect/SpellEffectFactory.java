package com.robin.magic_realm.components.effect;

import com.robin.magic_realm.components.utility.Constants;
import com.robin.magic_realm.components.utility.SpellUtility;

public class SpellEffectFactory {	
	public static ISpellEffect[] create(String spellName,String alternativeSpellEffect){
		if ("absorb essence".equals(spellName.toLowerCase())) return new ISpellEffect[]{new TransmorphEffect("target")};
		if ("animate".equals(spellName.toLowerCase())) return new ISpellEffect[]{new AnimateEffect()};
		if ("ask demon".equals(spellName.toLowerCase())) return new ISpellEffect[]{new AskDemonEffect()};
		
		if ("bewilder".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyClearingEffect(Constants.BEWILDERED)};
		if ("blazing light".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ExtraCavePhaseEffect()};
		if ("blazing light x".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.TORCH_BEARER)};
		if ("body double".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.BODY_DOUBLE)};
		
		if ("blend into background".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ExtraActionEffect("H")};
		if ("blend into background x".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ExtraActionEffect("H")};
		if ("blunting".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyClearingEffect(Constants.BLUNTED)};
		if ("blur".equals(spellName.toLowerCase())) return new ISpellEffect[]{new FinalChitSpeedEffect()};
		
		if ("broomstick".equals(spellName.toLowerCase())) return new ISpellEffect[]{new FlyChitEffect()};
		
		if ("control bats".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ControlEffect()};
		if ("control dragon".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ControlEffect()};
		if ("curse".equals(spellName.toLowerCase())) return new ISpellEffect[]{new CurseEffect()};
	
		if ("exorcise".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ExorciseEffect()};
		
		if ("deal with goblins".equals(spellName.toLowerCase())) return new ISpellEffect[]{new PacifyEffect(0)};
		if ("disguise".equals(spellName.toLowerCase())) return new ISpellEffect[]{new PacifyEffect(0)};
		if ("disjunction".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.NO_WEATHER_FATIGUE),new ApplyNamedEffect(Constants.NO_TERRAIN_HARM)};
		
		if ("dissolve spell".equals(spellName.toLowerCase())) return new ISpellEffect[]{new CancelEffect()};
		if ("divine might".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.STRONG_MF)};
		if ("divine shield".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.ADDS_ARMOR)};
		
		if ("elemental power".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ForcedEnchantEffect()};
		if ("elemental spirit".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ChitChangeEffect()};
		if ("elven grace".equals(spellName.toLowerCase())) return new ISpellEffect[]{new MoveSpeedChangeEffect()};
		
		if ("enchant artifact".equals(spellName.toLowerCase())) return new ISpellEffect[]{new EnchantEffect()};
		if ("eternal servant".equals(spellName.toLowerCase())) return new ISpellEffect[]{new NoWeightEffect()};
		
		if ("fae guard".equals(spellName.toLowerCase())) return new ISpellEffect[]{new SummonFairyEffect()};
		if ("faerie lights".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ChitChangeEffect()};
		if ("filcher".equals(spellName.toLowerCase())) return new ISpellEffect[]{new FilcherEffect()};
			
		if ("flame staff".equals(spellName.toLowerCase())) return new ISpellEffect[]{new AddSharpnessEffect(2)};
			
		if ("flying carpet spell".equals(spellName.toLowerCase())) return new ISpellEffect[]{new FlyStrengthEffect()};
			
		if ("fog".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.SP_NO_PEER)};
		
		if ("gravity".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyClearingEffect(Constants.HEAVIED)};
		if ("guide spider or octopus".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ControlEffect()};
		
		if ("heal".equals(spellName.toLowerCase())) return new ISpellEffect[]{new HealChitEffect()};
		if ("hop".equals(spellName.toLowerCase())) return new ISpellEffect[]{new TeleportEffect(SpellUtility.TeleportType.RandomClearing)};
		if ("hurricane winds".equals(spellName.toLowerCase())) return new ISpellEffect[]{new HurricaneWindsEffect()};
		if ("hypnotize".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ControlEffect()};
		
		if ("levitate".equals(spellName.toLowerCase())) return new ISpellEffect[]{new NoWeightEffect()};
		if ("lost".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.SP_MOVE_IS_RANDOM), new MazeCheckEffect()};
		if ("mage guard".equals(spellName.toLowerCase())) return new ISpellEffect[]{new MageGuardEffect()};
		if ("make whole".equals(spellName.toLowerCase())) return new ISpellEffect[]{new MakeWholeEffect()};
		if ("melt into mist".equals(spellName.toLowerCase())) return new ISpellEffect[]{new NullifyEffect(),new DisengageEffect(), new TransmorphEffect("mist")};
		if ("miracle".equals(spellName.toLowerCase())) return new ISpellEffect[]{new MiracleEffect()};
		if ("open gate".equals(spellName.toLowerCase())) return new ISpellEffect[]{new TeleportEffect(SpellUtility.TeleportType.KnownGate)};
		
		if ("peace".equals(spellName.toLowerCase())) return new ISpellEffect[]{new PeaceEffect()};
		if ("peace with nature".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.PEACE_WITH_NATURE)};
		
		if ("pentangle".equals(spellName.toLowerCase())) return new ISpellEffect[]{new NullifyEffect()};
		if ("persuade".equals(spellName.toLowerCase())) return new ISpellEffect[]{new PacifyEffect(1)};
		
		if ("phantasm".equals(spellName.toLowerCase())) return new ISpellEffect[]{new PhantasmEffect()};
		if ("poison".equals(spellName.toLowerCase())) return new ISpellEffect[]{new AddSharpnessEffect(1),new ApplyNamedEffect(Constants.POISON)};
		if ("power of the pit".equals(spellName.toLowerCase())) return new ISpellEffect[]{new PowerPitEffect()};
		
		if ("prayer".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ExtraActionEffect("R")};
		if ("premonition".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.CHOOSE_TURN)};
		if ("prophecy".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.DAYTIME_ACTIONS)};
		if ("protection from magic".equals(spellName.toLowerCase())) return new ISpellEffect[]{new PhaseChitEffect()};

		if ("raise dead".equals(spellName.toLowerCase())) return new ISpellEffect[]{new SummonEffect(SpellUtility.SummonType.undead.toString())};
		if ("remedy".equals(spellName.toLowerCase())) return new ISpellEffect[]{new CancelEffect()};
		if ("repair armor".equals(spellName.toLowerCase())) return new ISpellEffect[]{new RepairEffect()};
		if ("reverse power".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ColorModEffect()};
		
		if ("see/change weather".equals(spellName.toLowerCase())) return new ISpellEffect[]{new SeeChangeWeatherEffect()};
		if ("see hidden signs".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ExtraActionEffect("S")};
		if ("see hidden signs x".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ExtraActionEffect("S")};
		if ("send".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ControlEffect()};
		
		if ("sense danger".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ExtraActionEffect("A")};
		if ("serpent tongue".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ControlEffect()};
		if ("shrink".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.SHRINK)};
		
		if ("slow monster".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.SLOWED)};
		if ("small blessing".equals(spellName.toLowerCase())) return new ISpellEffect[]{new SmallBlessingEffect()};
		if ("sparkle".equals(spellName.toLowerCase())) return new ISpellEffect[]{new UnassignEffect()};
		
		if ("spirit guide".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.SPIRIT_GUIDE)};
		if ("staff to snake".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ChangeToCompanionEffect()};
		if ("stone gaze".equals(spellName.toLowerCase())) return new ISpellEffect[]{new PetrifyEffect()};
		
		if ("summon aid".equals(spellName.toLowerCase())) return new ISpellEffect[]{new SummonAidEffect()};
		if ("summon animal".equals(spellName.toLowerCase())) return new ISpellEffect[]{new SummonEffect(SpellUtility.SummonType.animal.toString())};
		if ("summon elemental".equals(spellName.toLowerCase())) return new ISpellEffect[]{new SummonEffect(SpellUtility.SummonType.elemental.toString())};
		if ("sword song".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.ALERTED_WEAPON), new AlertWeaponEffect()};
		
		if ("talk to wise bird".equals(spellName.toLowerCase())) return new ISpellEffect[]{new InstantPeerEffect()};
		if ("teleport".equals(spellName.toLowerCase())) return new ISpellEffect[]{new TeleportEffect(SpellUtility.TeleportType.ChooseTileTwo)};
		
		if ("transform".equals(spellName.toLowerCase())) return new ISpellEffect[]{new TransmorphEffect("roll")};
		
		if ("unleash power".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ActionChangeEffect()};
		
		if ("vale walker".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.VALE_WALKER)};
		if ("violent storm".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ViolentStormEffect()};
		if ("vision".equals(spellName.toLowerCase())) return new ISpellEffect[]{new DiscoverRoadEffect()};
		
		if ("whistle for monsters".equals(spellName.toLowerCase())) return new ISpellEffect[]{new MoveSoundEffect()};
		if ("witch's brew".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ChitChangeEffect()};
			
			// new spells
		if ("duel".equals(spellName.toLowerCase())) return new ISpellEffect[]{new DuelEffect()};
		if ("fighting hands".equals(spellName.toLowerCase())) return new ISpellEffect[]{new FightChitEffect()};
		if ("flame sword".equals(spellName.toLowerCase())) return new ISpellEffect[]{new MagicWeaponEffect()};
		if ("lucky blow".equals(spellName.toLowerCase())) return new ISpellEffect[]{new FinalChitHarmEffect()};
		if ("magic shield".equals(spellName.toLowerCase())) return new ISpellEffect[]{new MagicShieldEffect()};
		if ("mystic boots".equals(spellName.toLowerCase())) return new ISpellEffect[]{new MoveChitEffect()};
		if ("rocks glow".equals(spellName.toLowerCase())) return new ISpellEffect[]{new LightEffect()};
		if ("sleep".equals(spellName.toLowerCase())) return new ISpellEffect[]{new SleepEffect()};
		if ("spider web".equals(spellName.toLowerCase())) return new ISpellEffect[]{new SpiderWebEffect()};
			
			// super realm
		if ("alter object".equals(spellName.toLowerCase())) return new ISpellEffect[]{new AlterObjectEffect()};
		if ("alter size".equals(spellName.toLowerCase())) return new ISpellEffect[]{new AlterSizeEffect()};
		if ("barkskin".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.BARKSKIN)};
		if ("blinding light".equals(spellName.toLowerCase())) return new ISpellEffect[]{new PhaseChitEffect()};
		if ("blunt".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.BLUNT)};
		if ("camouflage".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.CAMOUFLAGE)};
		if ("charm snake".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ControlEffect()};
		if ("control horse".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ControlHorseEffect()};
		if ("control element".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ControlEffect()};
		if ("dazzle".equals(spellName.toLowerCase())) return new ISpellEffect[]{new PhaseChitEffect()};
		if ("deal with orcs and goblins".equals(spellName.toLowerCase())) return new ISpellEffect[]{new PacifyEffect(0)};
		if ("dark favor".equals(spellName.toLowerCase())) return new ISpellEffect[]{new PhaseChitEffect()};
		if ("divine protection".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.STRENGTHENED_VULNERABILITY)};
		if ("enchant key".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.KEY)};
		if ("enchant weapon".equals(spellName.toLowerCase())) return new ISpellEffect[]{new EnchantWeaponEffect(),new ApplyNamedEffect(Constants.IGNORE_MIST_LIKE)};
		if ("free the soul".equals(spellName.toLowerCase())) return new ISpellEffect[]{new FreeTheSoulEffect()};
		if ("free spell".equals(spellName.toLowerCase())) return new ISpellEffect[]{new FreeSpellEffect()};
		if ("frozen water".equals(spellName.toLowerCase())) return new ISpellEffect[]{new FrozenWaterEffect()};
		if ("guide beast".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ControlEffect()};
		if ("grow wings".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.GROW_WINGS)};
		if ("holy shield".equals(spellName.toLowerCase())) return new ISpellEffect[]{new PhaseChitEffect()};
		if ("horse whisper".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.HORSE_WHISPER),new TurnLightSideUpEffect()};
		if ("lift object".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.WEIGHT_NEGLIGIBLE)};
		if ("luck".equals(spellName.toLowerCase())) return new ISpellEffect[]{new PhaseChitEffect()};
		if ("meditate".equals(spellName.toLowerCase())) return new ISpellEffect[]{new MeditateEffect()};
		if ("mesmerize".equals(spellName.toLowerCase())) return new ISpellEffect[]{new MesmerizeEffect()};
		if ("migration".equals(spellName.toLowerCase())) return new ISpellEffect[]{new MigrationEffect()};
		if ("mountain surge".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyClearingEffect(Constants.MOUNTAIN_SURGE)};
		if ("negative aura".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.NEGATIVE_AURA)};
		if ("pacify skeletons".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffectWithValues(Constants.PACIFY_MONSTER),new ApplyNamedEffectWithValue(Constants.PACIFY_TYPE)};
		if ("redirect".equals(spellName.toLowerCase())) return new ISpellEffect[]{new RedirectEffect()};
		if ("reanimate".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ReanimateEffect()};
		if ("reinvigorate".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ExtraActionEffect("R")};
		if ("reserve".equals(spellName.toLowerCase())) return new ISpellEffect[]{new PhaseChitEffect()};
		if ("summon demon".equals(spellName.toLowerCase())) return new ISpellEffect[]{new SummonEffect(SpellUtility.SummonType.demon.toString())};
		if ("thorns".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ThornsEffect()};
		if ("tracker's sense".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyNamedEffect(Constants.TRACKERS_SENSE)};
		if ("violent winds".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ViolentWindsEffect()};
		if ("water run".equals(spellName.toLowerCase())) return new ISpellEffect[]{new MoveChitEffect()};
		if ("white feathered wing spell".equals(spellName.toLowerCase())) return new ISpellEffect[]{new ApplyDieModEffect()};
			
		if (alternativeSpellEffect!=null) {
			return create(alternativeSpellEffect,null);
		}
		return null;
	}
}