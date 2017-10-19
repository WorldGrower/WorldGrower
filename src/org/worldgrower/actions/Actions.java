/*******************************************************************************
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.worldgrower.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.magic.AnimalFriendshipSpellAction;
import org.worldgrower.actions.magic.AnimateDeadAction;
import org.worldgrower.actions.magic.AnimateSuitOfArmorAction;
import org.worldgrower.actions.magic.BestowCurseAction;
import org.worldgrower.actions.magic.BurdenAction;
import org.worldgrower.actions.magic.CureDiseaseAction;
import org.worldgrower.actions.magic.CurePoisonAction;
import org.worldgrower.actions.magic.DarkVisionSpellAction;
import org.worldgrower.actions.magic.DetectMagicAction;
import org.worldgrower.actions.magic.DetectPoisonAndDiseaseAction;
import org.worldgrower.actions.magic.DimensionDoorAction;
import org.worldgrower.actions.magic.DisguiseMagicSpellAction;
import org.worldgrower.actions.magic.DisintegrateArmorAction;
import org.worldgrower.actions.magic.DisintegrateWeaponAction;
import org.worldgrower.actions.magic.DispelMagicAction;
import org.worldgrower.actions.magic.EnlargeAction;
import org.worldgrower.actions.magic.EntangleAction;
import org.worldgrower.actions.magic.FearMagicSpellAction;
import org.worldgrower.actions.magic.FeatherAction;
import org.worldgrower.actions.magic.FireBallAttackAction;
import org.worldgrower.actions.magic.FireBoltAttackAction;
import org.worldgrower.actions.magic.FireTrapAction;
import org.worldgrower.actions.magic.FreedomOfMovementAction;
import org.worldgrower.actions.magic.InflictWoundsAction;
import org.worldgrower.actions.magic.InvisibilityAction;
import org.worldgrower.actions.magic.LichTransformationAction;
import org.worldgrower.actions.magic.LightningBoltAttackAction;
import org.worldgrower.actions.magic.LockMagicSpellAction;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.actions.magic.MajorIllusionAction;
import org.worldgrower.actions.magic.MendAction;
import org.worldgrower.actions.magic.MinorHealAction;
import org.worldgrower.actions.magic.MinorIllusionAction;
import org.worldgrower.actions.magic.ParalyzeSpellAction;
import org.worldgrower.actions.magic.PlantGrowthAction;
import org.worldgrower.actions.magic.ProtectionFromFireAction;
import org.worldgrower.actions.magic.ProtectionFromIceAction;
import org.worldgrower.actions.magic.ProtectionFromLightningAction;
import org.worldgrower.actions.magic.RayOfFrostAttackAction;
import org.worldgrower.actions.magic.ReduceAction;
import org.worldgrower.actions.magic.RemoveCurseAction;
import org.worldgrower.actions.magic.ResearchSpellAction;
import org.worldgrower.actions.magic.SacredFlameAttackAction;
import org.worldgrower.actions.magic.ScribeMagicSpellAction;
import org.worldgrower.actions.magic.SecretChestAction;
import org.worldgrower.actions.magic.SilenceMagicAction;
import org.worldgrower.actions.magic.SleepMagicSpellAction;
import org.worldgrower.actions.magic.SoulTrapAction;
import org.worldgrower.actions.magic.TrapContainerMagicSpellAction;
import org.worldgrower.actions.magic.TurnUndeadAction;
import org.worldgrower.actions.magic.UnlockMagicSpellAction;
import org.worldgrower.actions.magic.WaterWalkAction;
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.goal.GatherFoodGoal;
import org.worldgrower.goal.Goals;
import org.worldgrower.gui.ImageIds;

public class Actions {

	public static final DoNothingAction DO_NOTHING_ACTION = new DoNothingAction();
	public static final MoveAction MOVE_ACTION = new MoveAction();
	public static final EatAction EAT_ACTION = new EatAction();
	public static final EatNightShadeAction EAT_NIGHT_SHADE_ACTION = new EatNightShadeAction();
	public static final EatFromInventoryAction EAT_FROM_INVENTORY_ACTION = new EatFromInventoryAction();
	public static final DrinkAction DRINK_ACTION = new DrinkAction();
	public static final CutWoodAction CUT_WOOD_ACTION = new CutWoodAction();
	public static final BuildShackAction BUILD_SHACK_ACTION = new BuildShackAction();
	public static final BuildWellAction BUILD_WELL_ACTION = new BuildWellAction();
	public static final TalkAction TALK_ACTION = new TalkAction();
	public static final MeleeAttackAction MELEE_ATTACK_ACTION = new MeleeAttackAction();
	public static final NonLethalMeleeAttackAction NON_LETHAL_MELEE_ATTACK_ACTION = new NonLethalMeleeAttackAction();
	public static final RangedAttackAction RANGED_ATTACK_ACTION = new RangedAttackAction();
	public static final MineStoneAction MINE_STONE_ACTION = new MineStoneAction();
	public static final MineOreAction MINE_ORE_ACTION = new MineOreAction();
	public static final MineGoldAction MINE_GOLD_ACTION = new MineGoldAction();
	public static final MintGoldAction MINT_GOLD_ACTION = new MintGoldAction();
	public static final BuildHouseAction BUILD_HOUSE_ACTION = new BuildHouseAction();
	public static final ChooseProfessionAction CHOOSE_PROFESSION_ACTION = new ChooseProfessionAction();
	public static final BuildSmithAction BUILD_SMITH_ACTION = new BuildSmithAction();
	public static final SellAction SELL_ACTION = new SellAction();
	public static final BuyAction BUY_ACTION = new BuyAction();
	public static final HarvestFoodAction HARVEST_FOOD_ACTION = new HarvestFoodAction();
	public static final HarvestGrapesAction HARVEST_GRAPES_ACTION = new HarvestGrapesAction();
	public static final HarvestNightShadeAction HARVEST_NIGHT_SHADE_ACTION = new HarvestNightShadeAction();
	public static final PlantBerryBushAction PLANT_BERRY_BUSH_ACTION = new PlantBerryBushAction();
	public static final PlantGrapeVineAction PLANT_GRAPE_VINE_ACTION = new PlantGrapeVineAction();
	public static final PlantTreeAction PLANT_TREE_ACTION = new PlantTreeAction();
	public static final SexAction SEX_ACTION = new SexAction();
	public static final CraftIronClaymoreAction CRAFT_IRON_CLAYMORE_ACTION = new CraftIronClaymoreAction();
	public static final CraftIronCuirassAction CRAFT_IRON_CUIRASS_ACTION = new CraftIronCuirassAction();
	public static final CraftIronHelmetAction CRAFT_IRON_HELMET_ACTION = new CraftIronHelmetAction();
	public static final CraftIronGauntletsAction CRAFT_IRON_GAUNTLETS_ACTION = new CraftIronGauntletsAction();
	public static final CraftIronGreavesAction CRAFT_IRON_GREAVES_ACTION = new CraftIronGreavesAction();
	public static final CraftIronBootsAction CRAFT_IRON_BOOTS_ACTION = new CraftIronBootsAction();
	public static final CraftIronShieldAction CRAFT_IRON_SHIELD_ACTION = new CraftIronShieldAction();
	public static final CraftIronGreatswordAction CRAFT_IRON_GREATSWORD_ACTION = new CraftIronGreatswordAction();
	public static final CraftIronAxeAction CRAFT_IRON_AXE_ACTION = new CraftIronAxeAction();
	public static final CraftIronGreataxeAction CRAFT_IRON_GREATAXE_ACTION = new CraftIronGreataxeAction();
	public static final CraftLongBowAction CRAFT_LONG_BOW_ACTION = new CraftLongBowAction();
	public static final ChooseDeityAction CHOOSE_DEITY_ACTION = new ChooseDeityAction();
	public static final WorshipDeityAction WORSHIP_DEITY_ACTION = new WorshipDeityAction();
	public static final BuildShrineAction BUILD_SHRINE_ACTION = new BuildShrineAction();
	public static final RestAction REST_ACTION = new RestAction();
	public static final SleepAction SLEEP_ACTION = new SleepAction();
	public static final StealAction STEAL_ACTION = new StealAction();
	public static final StealGoldAction STEAL_GOLD_ACTION = new StealGoldAction();
	public static final KissAction KISS_ACTION = new KissAction();
	public static final PoisonAttackAction POISON_ATTACK_ACTION = new PoisonAttackAction();
	public static final CocoonAction COCOON_ACTION = new CocoonAction();
	public static final ReadAction READ_ACTION = new ReadAction();
	public static final DisguiseAction DISGUISE_ACTION = new DisguiseAction();
	public static final FireBoltAttackAction FIRE_BOLT_ATTACK_ACTION = new FireBoltAttackAction();
	public static final RayOfFrostAttackAction RAY_OF_FROST_ATTACK_ACTION = new RayOfFrostAttackAction();
	public static final BrewWineAction BREW_WINE_ACTION = new BrewWineAction();
	public static final BrewPoisonAction BREW_POISON_ACTION = new BrewPoisonAction();
	public static final MinorIllusionAction MINOR_ILLUSION_ACTION = new MinorIllusionAction();
	public static final BuildPaperMillAction BUILD_PAPER_MILL_ACTION = new BuildPaperMillAction();
	public static final CreatePaperAction CREATE_PAPER_ACTION = new CreatePaperAction();
	public static final CollectWaterAction COLLECT_WATER_ACTION = new CollectWaterAction();
	public static final CreateProfessionOrganizationAction CREATE_PROFESSION_ORGANIZATION_ACTION = new CreateProfessionOrganizationAction();
	public static final CreateReligionOrganizationAction CREATE_RELIGION_ORGANIZATION_ACTION = new CreateReligionOrganizationAction();
	public static final MinorHealAction MINOR_HEAL_ACTION = new MinorHealAction();
	public static final CurePoisonAction CURE_POISON_ACTION = new CurePoisonAction();
	public static final CureDiseaseAction CURE_DISEASE_ACTION = new CureDiseaseAction();
	public static final InvisibilityAction INVISIBILITY_ACTION = new InvisibilityAction();
	public static final EnlargeAction ENLARGE_ACTION = new EnlargeAction();
	public static final ReduceAction REDUCE_ACTION = new ReduceAction();
	public static final InflictWoundsAction INFLICT_WOUNDS_ACTION = new InflictWoundsAction();
	public static final MendAction MEND_ACTION = new MendAction();
	public static final SleepMagicSpellAction SLEEP_MAGIC_SPELL_ACTION = new SleepMagicSpellAction();
	public static final ParalyzeSpellAction PARALYZE_SPELL_ACTION = new ParalyzeSpellAction();
	public static final DetectPoisonAndDiseaseAction DETECT_POISON_AND_DISEASE_ACTION = new DetectPoisonAndDiseaseAction();
	public static final UnlockMagicSpellAction UNLOCK_MAGIC_SPELL_ACTION = new UnlockMagicSpellAction();
	public static final LockMagicSpellAction LOCK_MAGIC_SPELL_ACTION = new LockMagicSpellAction();
	public static final WaterWalkAction WATER_WALK_ACTION = new WaterWalkAction();
	public static final DisguiseMagicSpellAction DISGUISE_MAGIC_SPELL_ACTION = new DisguiseMagicSpellAction();
	public static final LightningBoltAttackAction LIGHTNING_BOLT_ATTACK_ACTION = new LightningBoltAttackAction();
	public static final BurdenAction BURDEN_ACTION = new BurdenAction();
	public static final FeatherAction FEATHER_ACTION = new FeatherAction();
	public static final DisintegrateArmorAction DISINTEGRATE_ARMOR_ACTION = new DisintegrateArmorAction();
	public static final DisintegrateWeaponAction DISINTEGRATE_WEAPON_ACTION = new DisintegrateWeaponAction();
	public static final SoulTrapAction SOUL_TRAP_ACTION = new SoulTrapAction();
	public static final DispelMagicAction DISPEL_MAGIC_ACTION = new DispelMagicAction();
	public static final SilenceMagicAction SILENCE_MAGIC_ACTION = new SilenceMagicAction();
	public static final DetectMagicAction DETECT_MAGIC_ACTION = new DetectMagicAction();
	public static final FearMagicSpellAction FEAR_MAGIC_SPELL_ACTION = new FearMagicSpellAction();
	public static final EntangleAction ENTANGLE_ACTION = new EntangleAction();
	public static final DarkVisionSpellAction DARK_VISION_SPELL_ACTION = new DarkVisionSpellAction();
	public static final TrapContainerMagicSpellAction TRAP_CONTAINER_MAGIC_SPELL_ACTION = new TrapContainerMagicSpellAction();
	public static final DimensionDoorAction DIMENSION_DOOR_ACTION = new DimensionDoorAction();
	public static final SecretChestAction SECRET_CHEST_ACTION = new SecretChestAction();
	public static final SacredFlameAttackAction SACRED_FLAME_ATTACK_ACTION = new SacredFlameAttackAction();
	public static final TurnUndeadAction TURN_UNDEAD_ACTION = new TurnUndeadAction();
	public static final PlantGrowthAction PLANT_GROWTH_ACTION = new PlantGrowthAction();
	
	public static final BuildLibraryAction BUILD_LIBRARY_ACTION = new BuildLibraryAction();
	public static final ResearchRestorationSkillAction RESEARCH_RESTORATION_SKILL_ACTION = new ResearchRestorationSkillAction();
	public static final ResearchIllusionSkillAction RESEARCH_ILLUSION_SKILL_ACTION = new ResearchIllusionSkillAction();
	public static final ResearchEvocationSkillAction RESEARCH_EVOCATION_SKILL_ACTION = new ResearchEvocationSkillAction();
	public static final ResearchNecromancySkillAction RESEARCH_NECROMANCY_SKILL_ACTION = new ResearchNecromancySkillAction();
	public static final GetItemFromInventoryAction GET_ITEM_FROM_INVENTORY_ACTION = new GetItemFromInventoryAction();
	public static final PutItemIntoInventoryAction PUT_ITEM_INTO_INVENTORY_ACTION = new PutItemIntoInventoryAction();
	public static final PoisonAction POISON_ACTION = new PoisonAction();
	public static final DrinkFromInventoryAction DRINK_FROM_INVENTORY_ACTION = new DrinkFromInventoryAction();
	public static final AnimateSuitOfArmorAction ANIMATE_SUIT_OF_ARMOR_ACTION = new AnimateSuitOfArmorAction();
	
	public static final AnimateDeadAction ANIMATE_DEAD_ACTION = new AnimateDeadAction();
	public static final CommandAction COMMAND_GATHER_WOOD_ACTION = new CommandAction(Goals.CREATE_WOOD_GOAL, "gather wood");
	public static final CommandAction COMMAND_GATHER_FOOD_ACTION = new CommandAction(new GatherFoodGoal(Integer.MAX_VALUE), "gather food");
	public static final CommandAction COMMAND_GATHER_GOLD_ACTION = new CommandAction(Goals.MINE_GOLD_GOAL, "gather gold");
	public static final CommandAction COMMAND_GATHER_STONE_ACTION = new CommandAction(Goals.MINE_STONE_GOAL, "gather stone");
	public static final CommandAction COMMAND_GATHER_IRON_ORE_ACTION = new CommandAction(Goals.MINE_ORE_GOAL, "gather iron ore");
	public static final CommandAction COMMAND_GATHER_SOUL_GEMS_ACTION = new CommandAction(Goals.MINE_SOUL_GEMS_GOAL, "gather soulgems");
	public static final CommandAction COMMAND_PROTECT_CASTER_ACTION = new CommandAction(Goals.KILL_OUTSIDERS_GOAL, "protect me");
	public static final CommandAction COMMAND_GATHER_REMAINS_ACTION = new CommandAction(Goals.GATHER_REMAINS_GOAL, "gather remains");
	public static final CommandAction COMMAND_GATHER_NIGHT_SHADE_ACTION = new CommandAction(Goals.HARVEST_NIGHT_SHADE_GOAL, "gather night shade");
	public static final CommandAction COMMAND_GATHER_COTTON_ACTION = new CommandAction(Goals.HARVEST_COTTON_GOAL, "gather cotton");
	public static final CommandAction COMMAND_GATHER_GRAPES_ACTION = new CommandAction(Goals.HARVEST_GRAPES_GOAL, "gather grapes");
		
	public static final GatherRemainsAction GATHER_REMAINS_ACTION = new GatherRemainsAction();
	public static final CreateGraveAction CREATE_GRAVE_ACTION = new CreateGraveAction();
	public static final BecomeLeaderCandidateAction BECOME_LEADER_CANDIDATE_ACTION = new BecomeLeaderCandidateAction();
	public static final VoteForLeaderAction VOTE_FOR_LEADER_ACTION = new VoteForLeaderAction();
	public static final StartOrganizationVoteAction START_ORGANIZATION_VOTE_ACTION = new StartOrganizationVoteAction();
	public static final HandoverTaxesAction HANDOVER_TAXES_ACTION = new HandoverTaxesAction();
	public static final SetGovernanceAction SET_GOVERNANCE_ACTION = new SetGovernanceAction();
	
	public static final PlantCottonPlantAction PLANT_COTTON_PLANT_ACTION = new PlantCottonPlantAction();
	public static final HarvestCottonAction HARVEST_COTTON_ACTION = new HarvestCottonAction();
	public static final WeaveCottonShirtAction WEAVE_COTTON_SHIRT_ACTION = new WeaveCottonShirtAction();
	public static final WeaveCottonHatAction WEAVE_COTTON_HAT_ACTION = new WeaveCottonHatAction();
	public static final WeaveCottonBootsAction WEAVE_COTTON_BOOTS_ACTION = new WeaveCottonBootsAction();
	public static final WeaveCottonGlovesAction WEAVE_COTTON_GLOVES_ACTION = new WeaveCottonGlovesAction();
	public static final WeaveCottonPantsAction WEAVE_COTTON_PANTS_ACTION = new WeaveCottonPantsAction();
	
	public static final VampireBiteAction VAMPIRE_BITE_ACTION = new VampireBiteAction();
	public static final ConstructBedAction CONSTRUCT_BED_ACTION = new ConstructBedAction();
	public static final ConstructKitchenAction CONSTRUCT_KITCHEN_ACTION = new ConstructKitchenAction();
	public static final MarkAsSellableAction MARK_AS_SELLABLE_ACTION = new MarkAsSellableAction();
	public static final MarkAsNotSellableAction MARK_AS_NOT_SELLABLE_ACTION = new MarkAsNotSellableAction();
	public static final ConstructTrainingDummyAction CONSTRUCT_TRAINING_DUMMY_ACTION = new ConstructTrainingDummyAction();
	public static final DetermineDeathReasonAction DETERMINE_DEATH_REASON_ACTION = new DetermineDeathReasonAction();
	public static final PlantNightShadeAction PLANT_NIGHT_SHADE_ACTION = new PlantNightShadeAction();
	public static final CapturePersonAction CAPTURE_PERSON_ACTION = new CapturePersonAction();
	public static final BuildJailAction BUILD_JAIL_ACTION = new BuildJailAction();
	public static final UnlockJailDoorAction UNLOCK_JAIL_DOOR_ACTION = new UnlockJailDoorAction();
	public static final BuildSacrificalAltarAction BUILD_SACRIFICAL_ALTAR_ACTION = new BuildSacrificalAltarAction();
	public static final CapturePersonForSacrificeAction CAPTURE_PERSON_FOR_SACRIFICE_ACTION = new CapturePersonForSacrificeAction();
	public static final ExtractOilAction EXTRACT_OIL_ACTION = new ExtractOilAction();
	public static final ThrowOilAction THROW_OIL_ACTION = new ThrowOilAction();
	public static final ConstructFishingPoleAction CONSTRUCT_FISHING_POLE_ACTION = new ConstructFishingPoleAction();
	public static final CatchFishAction CATCH_FISH_ACTION = new CatchFishAction();
	public static final MarkInventoryItemAsSellableAction MARK_INVENTORY_ITEM_AS_SELLABLE_ACTION = new MarkInventoryItemAsSellableAction();
	public static final BuildArenaAction BUILD_ARENA_ACTION = new BuildArenaAction();
	public static final DonateMoneyAction DONATE_MONEY_ACTION = new DonateMoneyAction();
	public static final CraftRepairHammerAction CRAFT_REPAIR_HAMMER_ACTION = new CraftRepairHammerAction();
	public static final RepairEquipmentInInventoryAction REPAIR_EQUIPMENT_IN_INVENTORY_ACTION = new RepairEquipmentInInventoryAction();
	public static final PoisonInventoryWaterAction POISON_INVENTORY_WATER_ACTION = new PoisonInventoryWaterAction();
	public static final ButcherAction BUTCHER_ACTION = new ButcherAction();
	public static final CreateHumanMeatAction CREATE_HUMAN_MEAT_ACTION = new CreateHumanMeatAction();
	public static final MineSoulGemsAction MINE_SOUL_GEMS_ACTION = new MineSoulGemsAction();
	public static final EquipInventoryItemAction EQUIP_INVENTORY_ITEM_ACTION = new EquipInventoryItemAction();
	public static final BuildInnAction BUILD_INN_ACTION = new BuildInnAction();
	public static final CreateNewsPaperAction CREATE_NEWS_PAPER_ACTION = new CreateNewsPaperAction();
	public static final ReadItemInInventoryAction READ_ITEM_IN_INVENTORY_ACTION = new ReadItemInInventoryAction();
	public static final ObfuscateDeathReasonAction OBFUSCATE_DEATH_REASON_ACTION = new ObfuscateDeathReasonAction();
	public static final FireTrapAction FIRE_TRAP_ACTION = new FireTrapAction();
	public static final InvestigateAction INVESTIGATE_ACTION = new InvestigateAction();
	public static final CreateBloodAction CREATE_BLOOD_ACTION = new CreateBloodAction();
	public static final LichTransformationAction LICH_TRANSFORMATION_ACTION = new LichTransformationAction();
	public static final CraftIronMaceAction CRAFT_IRON_MACE_ACTION = new CraftIronMaceAction();
	public static final CraftIronKatarAction CRAFT_IRON_KATAR_ACTION = new CraftIronKatarAction();
	public static final BrewSleepingPotionAction BREW_SLEEPING_POTION_ACTION = new BrewSleepingPotionAction();
	public static final SleepingPoisonAction SLEEPING_POISON_ACTION = new SleepingPoisonAction();
	public static final PoisonInventoryWaterWithSleepingPotionAction POISON_INVENTORY_WATER_WITH_SLEEPING_POTION_ACTION = new PoisonInventoryWaterWithSleepingPotionAction();
	public static final PoisonWeaponAction POISON_WEAPON_ACTION = new PoisonWeaponAction();
	public static final StandStillToTalkAction STAND_STILL_TO_TALK_ACTION = new StandStillToTalkAction();
	public static final BuildWeaveryAction BUILD_WEAVERY_ACTION = new BuildWeaveryAction();
	public static final BuildWorkbenchAction BUILD_WORKBENCH_ACTION = new BuildWorkbenchAction();
	public static final BuildBreweryAction BUILD_BREWERY_ACTION = new BuildBreweryAction();
	public static final LifeStealAction LIFE_STEAL_ACTION = new LifeStealAction();
	public static final LeashAction LEASH_ACTION = new LeashAction();
	public static final UnleashAction UNLEASH_ACTION = new UnleashAction();
	public static final ClaimCattleAction CLAIM_CATTLE_ACTION = new ClaimCattleAction();
	public static final ClaimBuildingAction CLAIM_BUILDING_ACTION = new ClaimBuildingAction();
	public static final LearnMagicSpellFromBookAction LEARN_MAGIC_SPELL_FROM_BOOK_ACTION = new LearnMagicSpellFromBookAction();
	public static final BuildApothecaryAction BUILD_APOTHECARY_ACTION = new BuildApothecaryAction();
	public static final ConstructChestAction CONSTRUCT_CHEST_ACTION = new ConstructChestAction();
	public static final RecallSecretChestAction RECALL_SECRET_CHEST_ACTION = new RecallSecretChestAction();
	public static final DismissSecretChestAction DISMISS_SECRET_CHEST_ACTION = new DismissSecretChestAction();
	public static final AnimalFriendshipSpellAction ANIMAL_FRIENDSHIP_SPELL_ACTION = new AnimalFriendshipSpellAction();
	public static final EatRemainsAction EAT_REMAINS_ACTION = new EatRemainsAction();
	public static final FireBallAttackAction FIRE_BALL_ATTACK_ACTION = new FireBallAttackAction();
	public static final MajorIllusionAction MAJOR_ILLUSION_ACTION = new MajorIllusionAction();
	public static final SetPricesAction SET_PRICES_ACTION = new SetPricesAction();
	public static final ConstructPickAxeAction CONSTRUCT_PICK_AXE_ACTION = new ConstructPickAxeAction();
	public static final ConstructScytheAction CONSTRUCT_SCYTHE_ACTION = new ConstructScytheAction();
	public static final ConstructLockpickAction CONSTRUCT_LOCKPICK_ACTION = new ConstructLockpickAction();
	public static final OpenLockAction OPEN_LOCK_ACTION = new OpenLockAction();
	public static final ConstructButcherKnifeAction CONSTRUCT_BUTCHER_KNIFE_ACTION = new ConstructButcherKnifeAction();
	public static final FirePublicEmployeeAction FIRE_PUBLIC_EMPLOYEE_ACTION = new FirePublicEmployeeAction();
	public static final ProtectionFromFireAction PROTECTION_FROM_FIRE_ACTION = new ProtectionFromFireAction();
	public static final ProtectionFromIceAction PROTECTION_FROM_ICE_ACTION = new ProtectionFromIceAction();
	public static final ProtectionFromLightningAction PROTECTION_FROM_LIGHTNING_ACTION = new ProtectionFromLightningAction();
	public static final FreedomOfMovementAction FREEDOM_OF_MOVEMENT_ACTION = new FreedomOfMovementAction();
	public static final BrewHealingPotionAction BREW_HEALING_POTION_ACTION = new BrewHealingPotionAction();
	public static final BrewCurePoisonPotionAction BREW_CURE_POISON_POTION_ACTION = new BrewCurePoisonPotionAction();
	public static final BrewCureDiseasePotionAction BREW_CURE_DISEASE_POTION_ACTION = new BrewCureDiseasePotionAction();
	public static final ReversePickPocketAction REVERSE_PICK_POCKET_ACTION = new ReversePickPocketAction();
	public static final DropItemAction DROP_ITEM_ACTION = new DropItemAction();
	public static final WeaveLeatherShirtAction WEAVE_LEATHER_SHIRT_ACTION = new WeaveLeatherShirtAction();
	public static final WeaveLeatherHatAction WEAVE_LEATHER_HAT_ACTION = new WeaveLeatherHatAction();
	public static final WeaveLeatherBootsAction WEAVE_LEATHER_BOOTS_ACTION = new WeaveLeatherBootsAction();
	public static final WeaveLeatherGlovesAction WEAVE_LEATHER_GLOVES_ACTION = new WeaveLeatherGlovesAction();
	public static final WeaveLeatherPantsAction WEAVE_LEATHER_PANTS_ACTION = new WeaveLeatherPantsAction();
	public static final CreateSteelAction CREATE_STEEL_ACTION = new CreateSteelAction();
	public static final CraftSteelClaymoreAction CRAFT_STEEL_CLAYMORE_ACTION = new CraftSteelClaymoreAction();
	public static final CraftSteelGreatswordAction CRAFT_STEEL_GREATSWORD_ACTION = new CraftSteelGreatswordAction();
	public static final CraftSteelAxeAction CRAFT_STEEL_AXE_ACTION = new CraftSteelAxeAction();
	public static final CraftSteelGreataxeAction CRAFT_STEEL_GREATAXE_ACTION = new CraftSteelGreataxeAction();
	public static final CraftSteelCuirassAction CRAFT_STEEL_CUIRASS_ACTION = new CraftSteelCuirassAction();
	public static final CraftSteelHelmetAction CRAFT_STEEL_HELMET_ACTION = new CraftSteelHelmetAction();
	public static final CraftSteelGauntletsAction CRAFT_STEEL_GAUNTLETS_ACTION = new CraftSteelGauntletsAction();
	public static final CraftSteelGreavesAction CRAFT_STEEL_GREAVES_ACTION = new CraftSteelGreavesAction();
	public static final CraftSteelShieldAction CRAFT_STEEL_SHIELD_ACTION = new CraftSteelShieldAction();
	public static final CraftSteelBootsAction CRAFT_STEEL_BOOTS_ACTION = new CraftSteelBootsAction();
	public static final CraftSteelMaceAction CRAFT_STEEL_MACE_ACTION = new CraftSteelMaceAction();
	public static final CraftSteelKatarAction CRAFT_STEEL_KATAR_ACTION = new CraftSteelKatarAction();
	public static final CraftShortBowAction CRAFT_SHORT_BOW_ACTION = new CraftShortBowAction();
	public static final BrewChangeGenderPotionAction BREW_CHANGE_GENDER_POTION_ACTION = new BrewChangeGenderPotionAction();
	public static final BestowCurseAction BESTOW_CURSE_ACTION = new BestowCurseAction();
	public static final RemoveCurseAction REMOVE_CURSE_ACTION = new RemoveCurseAction();
	public static final BrewRemoveCursePotionAction BREW_REMOVE_CURSE_POTION_ACTION = new BrewRemoveCursePotionAction();
	public static final CraftIronDaggerAction CRAFT_IRON_DAGGER_ACTION = new CraftIronDaggerAction();
	public static final CraftSteelDaggerAction CRAFT_STEEL_DAGGER_ACTION = new CraftSteelDaggerAction();
	public static final CraftIronMorningstarAction CRAFT_IRON_MORNINGSTAR_ACTION = new CraftIronMorningstarAction();
	public static final CraftSteelMorningstarAction CRAFT_STEEL_MORNINGSTAR_ACTION = new CraftSteelMorningstarAction();
	public static final ConstructLampAction CONSTRUCT_LAMP_ACTION = new ConstructLampAction();
	public static final ConstructSawAction CONSTRUCT_SAW_ACTION = new ConstructSawAction();
	public static final PlantPalmTreeAction PLANT_PALM_TREE_ACTION = new PlantPalmTreeAction();
	public static final CraftIronTridentAction CRAFT_IRON_TRIDENT_ACTION = new CraftIronTridentAction();
	public static final CraftSteelTridentAction CRAFT_STEEL_TRIDENT_ACTION = new CraftSteelTridentAction();
	public static final CookAction COOK_ACTION = new CookAction();
	
	private static final List<MagicSpell> MAGIC_SPELLS = Arrays.asList(
			MINOR_HEAL_ACTION, 
			MINOR_ILLUSION_ACTION, 
			FIRE_BOLT_ATTACK_ACTION, 
			RAY_OF_FROST_ATTACK_ACTION, 
			CURE_POISON_ACTION,
			ANIMATE_DEAD_ACTION,
			CURE_DISEASE_ACTION,
			INVISIBILITY_ACTION,
			ENLARGE_ACTION,
			REDUCE_ACTION,
			INFLICT_WOUNDS_ACTION,
			MEND_ACTION,
			SLEEP_MAGIC_SPELL_ACTION,
			PARALYZE_SPELL_ACTION,
			DETECT_POISON_AND_DISEASE_ACTION,
			UNLOCK_MAGIC_SPELL_ACTION,
			LOCK_MAGIC_SPELL_ACTION,
			WATER_WALK_ACTION,
			DISGUISE_MAGIC_SPELL_ACTION,
			LIGHTNING_BOLT_ATTACK_ACTION,
			BURDEN_ACTION,
			FEATHER_ACTION,
			DISINTEGRATE_ARMOR_ACTION,
			DISINTEGRATE_WEAPON_ACTION,
			SOUL_TRAP_ACTION,
			DISPEL_MAGIC_ACTION,
			SILENCE_MAGIC_ACTION,
			DETECT_MAGIC_ACTION,
			FIRE_TRAP_ACTION,
			LICH_TRANSFORMATION_ACTION,
			FEAR_MAGIC_SPELL_ACTION,
			ENTANGLE_ACTION,
			DARK_VISION_SPELL_ACTION,
			TRAP_CONTAINER_MAGIC_SPELL_ACTION,
			DIMENSION_DOOR_ACTION,
			SECRET_CHEST_ACTION,
			ANIMAL_FRIENDSHIP_SPELL_ACTION,
			FIRE_BALL_ATTACK_ACTION,
			MAJOR_ILLUSION_ACTION,
			SACRED_FLAME_ATTACK_ACTION,
			TURN_UNDEAD_ACTION,
			PROTECTION_FROM_FIRE_ACTION,
			PROTECTION_FROM_ICE_ACTION,
			PROTECTION_FROM_LIGHTNING_ACTION,
			FREEDOM_OF_MOVEMENT_ACTION,
			BESTOW_CURSE_ACTION,
			REMOVE_CURSE_ACTION,
			PLANT_GROWTH_ACTION
			);
	
	private static final List<InventoryAction> INVENTORY_ACTIONS = Arrays.asList(
			EAT_FROM_INVENTORY_ACTION,
			DRINK_FROM_INVENTORY_ACTION,
			REPAIR_EQUIPMENT_IN_INVENTORY_ACTION,
			POISON_INVENTORY_WATER_ACTION,
			ANIMATE_SUIT_OF_ARMOR_ACTION,
			READ_ITEM_IN_INVENTORY_ACTION,
			POISON_INVENTORY_WATER_WITH_SLEEPING_POTION_ACTION,
			POISON_WEAPON_ACTION,
			LEARN_MAGIC_SPELL_FROM_BOOK_ACTION,
			RECALL_SECRET_CHEST_ACTION,
			MARK_INVENTORY_ITEM_AS_SELLABLE_ACTION,
			DROP_ITEM_ACTION
			);
	
	private static final List<ResearchKnowledgeSkillAction> RESEARCH_SKILL_ACTIONS = Arrays.asList(
			RESEARCH_RESTORATION_SKILL_ACTION,
			RESEARCH_ILLUSION_SKILL_ACTION,
			RESEARCH_EVOCATION_SKILL_ACTION,
			RESEARCH_NECROMANCY_SKILL_ACTION);
	
	public static final List<ManagedOperation> ALL_ACTIONS = new ArrayList<>(Arrays.asList(
		MOVE_ACTION,
		EAT_ACTION,
		EAT_NIGHT_SHADE_ACTION,
		DRINK_ACTION,
		CUT_WOOD_ACTION,
		BUILD_SHACK_ACTION,
		BUILD_WELL_ACTION,
		TALK_ACTION,
		MELEE_ATTACK_ACTION,
		RANGED_ATTACK_ACTION,
		MINE_STONE_ACTION,
		MINE_ORE_ACTION,
		MINE_GOLD_ACTION,
		BUILD_HOUSE_ACTION,
		CHOOSE_PROFESSION_ACTION,
		BUILD_SMITH_ACTION,
		SELL_ACTION,
		BUY_ACTION,
		HARVEST_FOOD_ACTION,
		HARVEST_GRAPES_ACTION,
		HARVEST_NIGHT_SHADE_ACTION,
		PLANT_BERRY_BUSH_ACTION,
		PLANT_GRAPE_VINE_ACTION,
		PLANT_TREE_ACTION,
		SEX_ACTION,
		CRAFT_IRON_CLAYMORE_ACTION,
		CRAFT_IRON_CUIRASS_ACTION,
		CRAFT_IRON_HELMET_ACTION,
		CRAFT_IRON_GAUNTLETS_ACTION,
		CRAFT_IRON_GREAVES_ACTION,
		CRAFT_IRON_BOOTS_ACTION,
		CRAFT_IRON_SHIELD_ACTION,
		CRAFT_IRON_GREATSWORD_ACTION,
		CRAFT_IRON_AXE_ACTION,
		CRAFT_IRON_GREATAXE_ACTION,
		CRAFT_LONG_BOW_ACTION,
		CHOOSE_DEITY_ACTION,
		WORSHIP_DEITY_ACTION,
		BUILD_SHRINE_ACTION,
		REST_ACTION,
		SLEEP_ACTION,
		STEAL_ACTION,
		STEAL_GOLD_ACTION,
		KISS_ACTION,
		POISON_ATTACK_ACTION,
		COCOON_ACTION,
		READ_ACTION,
		DISGUISE_ACTION,
		MINT_GOLD_ACTION,
		BREW_WINE_ACTION,
		CREATE_PAPER_ACTION,
		COLLECT_WATER_ACTION,
		CREATE_PROFESSION_ORGANIZATION_ACTION,
		CREATE_RELIGION_ORGANIZATION_ACTION,
		BUILD_LIBRARY_ACTION,
		GET_ITEM_FROM_INVENTORY_ACTION,
		PUT_ITEM_INTO_INVENTORY_ACTION,
		BREW_POISON_ACTION,
		POISON_ACTION,
		COMMAND_GATHER_WOOD_ACTION,
		COMMAND_GATHER_FOOD_ACTION,
		COMMAND_GATHER_GOLD_ACTION,
		COMMAND_GATHER_STONE_ACTION,
		COMMAND_GATHER_SOUL_GEMS_ACTION,
		COMMAND_GATHER_IRON_ORE_ACTION,
		COMMAND_PROTECT_CASTER_ACTION,
		COMMAND_GATHER_REMAINS_ACTION,
		COMMAND_GATHER_NIGHT_SHADE_ACTION,
		COMMAND_GATHER_COTTON_ACTION,
		GATHER_REMAINS_ACTION,
		CREATE_GRAVE_ACTION,
		BUILD_PAPER_MILL_ACTION,
		BECOME_LEADER_CANDIDATE_ACTION,
		VOTE_FOR_LEADER_ACTION,
		START_ORGANIZATION_VOTE_ACTION,
		HANDOVER_TAXES_ACTION,
		PLANT_COTTON_PLANT_ACTION,
		HARVEST_COTTON_ACTION,
		WEAVE_COTTON_SHIRT_ACTION,
		WEAVE_COTTON_HAT_ACTION,
		WEAVE_COTTON_BOOTS_ACTION,
		WEAVE_COTTON_GLOVES_ACTION,
		WEAVE_COTTON_PANTS_ACTION,
		VAMPIRE_BITE_ACTION,
		CONSTRUCT_BED_ACTION,
		MARK_AS_SELLABLE_ACTION,
		MARK_AS_NOT_SELLABLE_ACTION,
		CONSTRUCT_TRAINING_DUMMY_ACTION,
		DETERMINE_DEATH_REASON_ACTION,
		PLANT_NIGHT_SHADE_ACTION,
		SET_GOVERNANCE_ACTION,
		NON_LETHAL_MELEE_ATTACK_ACTION,
		CAPTURE_PERSON_ACTION,
		BUILD_JAIL_ACTION,
		UNLOCK_JAIL_DOOR_ACTION,
		BUILD_SACRIFICAL_ALTAR_ACTION,
		CAPTURE_PERSON_FOR_SACRIFICE_ACTION,
		EXTRACT_OIL_ACTION,
		THROW_OIL_ACTION,
		CONSTRUCT_FISHING_POLE_ACTION,
		CATCH_FISH_ACTION,
		BUILD_ARENA_ACTION,
		DONATE_MONEY_ACTION,
		CRAFT_REPAIR_HAMMER_ACTION,
		BUTCHER_ACTION,
		CREATE_HUMAN_MEAT_ACTION,
		MINE_SOUL_GEMS_ACTION,
		EQUIP_INVENTORY_ITEM_ACTION,
		BUILD_INN_ACTION,
		CREATE_NEWS_PAPER_ACTION,
		OBFUSCATE_DEATH_REASON_ACTION,
		INVESTIGATE_ACTION,
		CREATE_BLOOD_ACTION,
		CRAFT_IRON_MACE_ACTION,
		CRAFT_IRON_KATAR_ACTION,
		BREW_SLEEPING_POTION_ACTION,
		SLEEPING_POISON_ACTION,
		STAND_STILL_TO_TALK_ACTION,
		BUILD_WEAVERY_ACTION,
		BUILD_WORKBENCH_ACTION,
		BUILD_BREWERY_ACTION,
		LIFE_STEAL_ACTION,
		LEASH_ACTION,
		UNLEASH_ACTION,
		CLAIM_CATTLE_ACTION,
		CLAIM_BUILDING_ACTION,
		BUILD_APOTHECARY_ACTION,
		CONSTRUCT_CHEST_ACTION,
		DISMISS_SECRET_CHEST_ACTION,
		EAT_REMAINS_ACTION,
		SET_PRICES_ACTION,
		CONSTRUCT_PICK_AXE_ACTION,
		CONSTRUCT_SCYTHE_ACTION,
		CONSTRUCT_LOCKPICK_ACTION,
		OPEN_LOCK_ACTION,
		CONSTRUCT_BUTCHER_KNIFE_ACTION,
		FIRE_PUBLIC_EMPLOYEE_ACTION,
		BREW_HEALING_POTION_ACTION,
		BREW_CURE_POISON_POTION_ACTION,
		BREW_CURE_DISEASE_POTION_ACTION,
		REVERSE_PICK_POCKET_ACTION,
		WEAVE_LEATHER_SHIRT_ACTION,
		WEAVE_LEATHER_HAT_ACTION,
		WEAVE_LEATHER_BOOTS_ACTION,
		WEAVE_LEATHER_GLOVES_ACTION,
		WEAVE_LEATHER_PANTS_ACTION,
		CREATE_STEEL_ACTION,
		CRAFT_STEEL_CLAYMORE_ACTION,
		CRAFT_STEEL_GREATSWORD_ACTION,
		CRAFT_STEEL_AXE_ACTION,
		CRAFT_STEEL_GREATAXE_ACTION,
		CRAFT_STEEL_CUIRASS_ACTION,
		CRAFT_STEEL_HELMET_ACTION,
		CRAFT_STEEL_GAUNTLETS_ACTION,
		CRAFT_STEEL_GREAVES_ACTION,
		CRAFT_STEEL_SHIELD_ACTION,
		CRAFT_STEEL_BOOTS_ACTION,
		CRAFT_STEEL_MACE_ACTION,
		CRAFT_STEEL_KATAR_ACTION,
		CRAFT_SHORT_BOW_ACTION,
		BREW_CHANGE_GENDER_POTION_ACTION,
		BREW_REMOVE_CURSE_POTION_ACTION,
		CRAFT_IRON_DAGGER_ACTION,
		CRAFT_STEEL_DAGGER_ACTION,
		CRAFT_IRON_MORNINGSTAR_ACTION,
		CRAFT_STEEL_MORNINGSTAR_ACTION,
		CONSTRUCT_LAMP_ACTION,
		CONSTRUCT_SAW_ACTION,
		PLANT_PALM_TREE_ACTION,
		CRAFT_IRON_TRIDENT_ACTION,
		CRAFT_STEEL_TRIDENT_ACTION,
		CONSTRUCT_KITCHEN_ACTION,
		COOK_ACTION
	));
	
	static {
		ALL_ACTIONS.addAll(MAGIC_SPELLS);
		ALL_ACTIONS.addAll(RESEARCH_SKILL_ACTIONS);
		
		for(MagicSpell magicSpell : MAGIC_SPELLS) {
			ResearchSpellAction researchSpellAction = new ResearchSpellAction(magicSpell);
			ALL_ACTIONS.add(researchSpellAction);
			
			ScribeMagicSpellAction scribeMagicSpellAction = new ScribeMagicSpellAction(magicSpell);
			ALL_ACTIONS.add(scribeMagicSpellAction);
		}
		
		ALL_ACTIONS.addAll(INVENTORY_ACTIONS);
	}
	
	public static ResearchSpellAction getResearchSpellActionFor(MagicSpell magicSpell) {
		for(ManagedOperation managedOperation : ALL_ACTIONS) {
			if (managedOperation.getClass() == ResearchSpellAction.class) {
				ResearchSpellAction researchSpellAction = (ResearchSpellAction) managedOperation;
				if (researchSpellAction.getSpell() == magicSpell) {
					return researchSpellAction;
				}
			}
		}
		
		throw new IllegalStateException("Problem getting ResearchSpellAction for MagicSpell " + magicSpell);
	}
	
	public static ScribeMagicSpellAction getScribeMagicSpellActionFor(MagicSpell magicSpell) {
		for(ManagedOperation managedOperation : ALL_ACTIONS) {
			if (managedOperation.getClass() == ScribeMagicSpellAction.class) {
				ScribeMagicSpellAction scribeMagicSpellAction = (ScribeMagicSpellAction) managedOperation;
				if (scribeMagicSpellAction.getSpell() == magicSpell) {
					return scribeMagicSpellAction;
				}
			}
		}
		
		throw new IllegalStateException("Problem getting ScribeMagicSpellAction for MagicSpell " + magicSpell);
	}
	
	private static List<ManagedOperation> getAllScribeMagicSpellActions() {
		List<ManagedOperation> scribeMagicSpells = ALL_ACTIONS.stream().filter(operation -> operation.getClass() == ScribeMagicSpellAction.class).collect(Collectors.toList());
		Collections.sort(scribeMagicSpells, new Comparator<ManagedOperation>() {

			@Override
			public int compare(ManagedOperation o1, ManagedOperation o2) {
				return o1.getSimpleDescription().compareTo(o2.getSimpleDescription());
			}
		});
		return scribeMagicSpells;
		
	}
	
	public static Map<SkillProperty, List<ManagedOperation>> getScribeMagicSpellActions() {
		List<ManagedOperation> allScribeMagicSpells = getAllScribeMagicSpellActions();
		Map<SkillProperty, List<ManagedOperation>> scribeActionsMap = new HashMap<>();
		for (ManagedOperation action : allScribeMagicSpells) {
			ScribeMagicSpellAction scribeMagicSpellAction = (ScribeMagicSpellAction) action;
			SkillProperty skillProperty = scribeMagicSpellAction.getSpell().getSkill();
			List<ManagedOperation> scribeMagicSpells = scribeActionsMap.get(skillProperty);
			if (scribeMagicSpells == null) {
				scribeMagicSpells = new ArrayList<>();
				scribeActionsMap.put(skillProperty, scribeMagicSpells);
			}
			scribeMagicSpells.add(scribeMagicSpellAction);
		}
		return scribeActionsMap;
	}
	
	public static List<SkillProperty> getSortedSkillProperties(Map<SkillProperty, List<ManagedOperation>> scribeActionsMap) {
		List<SkillProperty> skillsList = new ArrayList<>(scribeActionsMap.keySet());
		skillsList.sort(new SkillComparator());
		return skillsList;
	}
	
	public static List<SkillProperty> sortSkillProperties(Collection<SkillProperty> skillProperties) {
		List<SkillProperty> skillPropertiesList = new ArrayList<>(skillProperties);
		Collections.sort(skillPropertiesList, new SkillComparator());
		return skillPropertiesList;
	}
	
	public static List<MagicSpell> getMagicSpells() {
		return MAGIC_SPELLS;
	}

	public static List<MagicSpell> getMagicSpellsToResearch(WorldObject performer) {
		List<MagicSpell> allSpells = new ArrayList<>(MAGIC_SPELLS);
		List<ManagedOperation> knownSpells = performer.getProperty(Constants.KNOWN_SPELLS);
		
		allSpells.removeAll(knownSpells);
		sortActionsByDescription(allSpells);
		allSpells = allSpells.stream().filter(s -> getResearchSpellActionFor(s).distance(performer, performer, Args.EMPTY, null) == 0).collect(Collectors.toList());
		
		return allSpells;
	}
	
	public static List<String> getMagicSpellDescriptions(List<MagicSpell> magicSpells) {
		return magicSpells.stream().map(s -> s.getSimpleDescription()).collect(Collectors.toList());
	}
	
	public static List<ImageIds> getMagicSpellImageIds(WorldObject performer, List<MagicSpell> magicSpells) {
		return magicSpells.stream().map(s -> s.getImageIds(performer)).collect(Collectors.toList());
	}
	
	public static List<InventoryAction> getInventoryActions() {
		return INVENTORY_ACTIONS;
	}
	
	public static void sortActionsByDescription(List<? extends ManagedOperation> actions) {
		Collections.sort(actions, new ActionComparator());
	}
	
	private static class SkillComparator implements Comparator<SkillProperty> {
		@Override
		public int compare(SkillProperty o1, SkillProperty o2) {
			return o1.getName().compareTo(o2.getName());
		}
	}

	private static class ActionComparator implements Comparator<ManagedOperation> {

		@Override
		public int compare(ManagedOperation o1, ManagedOperation o2) {
			return o1.getSimpleDescription().compareTo(o2.getSimpleDescription());
		}
	}
	
	public static ResearchKnowledgeSkillAction getResearchKnowledgeSkillActionFor(SkillProperty skillProperty) {
		for(ResearchKnowledgeSkillAction researchKnowledgeSkillAction : RESEARCH_SKILL_ACTIONS) {
			if (researchKnowledgeSkillAction.getSkillProperty() == skillProperty) {
				return researchKnowledgeSkillAction;
			}
		}
		throw new IllegalStateException("Problem getting ResearchKnowledgeSkillAction for skillProperty " + skillProperty);
	}

	public static List<String> getDeadlyActionDescriptions() {
		return ALL_ACTIONS.stream().filter(operation -> operation instanceof DeadlyAction).map(operation -> operation.getSimpleDescription()).collect(Collectors.toList());
	}
	
	public static boolean isMutuallyAgreedAction(ManagedOperation action) {
		return ((action == BUY_ACTION) || (action == SELL_ACTION));
	}
	
	public static List<ManagedOperation> getActionsWithTargetProperty(WorldObject performer, IntProperty intProperty, World world) {
		List<ManagedOperation> actions = new ArrayList<>();
		WorldObject sampleWorldObject = BuildingGenerator.generateShack(0, 0, 0, performer, -1);
		sampleWorldObject.setProperty(intProperty, 5);
		sampleWorldObject.removeProperty(Constants.HIT_POINTS);
		sampleWorldObject.removeProperty(Constants.ARMOR);
		sampleWorldObject.removeProperty(Constants.INVENTORY);
		sampleWorldObject.removeProperty(Constants.SLEEP_COMFORT);
		sampleWorldObject.removeProperty(Constants.BUILDING_TYPE);
		sampleWorldObject.removeProperty(Constants.TEXT);
		sampleWorldObject.removeProperty(Constants.CONDITIONS);
		sampleWorldObject.removeProperty(Constants.LOCKED);
		
		for(ManagedOperation action : ALL_ACTIONS) {
			if (action.isValidTarget(performer, sampleWorldObject, world)) {
				actions.add(action);
			}
		}
		actions.remove(Actions.MINOR_ILLUSION_ACTION);
		actions.remove(Actions.MAJOR_ILLUSION_ACTION);
		
		return actions;
	}
}
