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
package org.worldgrower.goal;

import java.util.ArrayList;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.deity.Deity;

public class Goals {

	public static final List<Goal> ALL_GOALS = new ArrayList<>();
	
	public static final FoodGoal FOOD_GOAL = new FoodGoal(ALL_GOALS);
	public static final DrinkWaterGoal DRINK_WATER_GOAL = new DrinkWaterGoal(ALL_GOALS);
	public static final CollectWaterGoal COLLECT_WATER_GOAL = new CollectWaterGoal(ALL_GOALS);
	public static final WoodGoal WOOD_GOAL = new WoodGoal(ALL_GOALS);
	public static final CreateWoodGoal CREATE_WOOD_GOAL = new CreateWoodGoal(ALL_GOALS);
	public static final KillOutsidersGoal KILL_OUTSIDERS_GOAL = new KillOutsidersGoal(ALL_GOALS);
	public static final ShackGoal SHACK_GOAL = new ShackGoal(ALL_GOALS);
	public static final ProtectOneSelfGoal PROTECT_ONE_SELF_GOAL = new ProtectOneSelfGoal(ALL_GOALS);
	public static final HouseGoal HOUSE_GOAL = new HouseGoal(ALL_GOALS);
	public static final StoneGoal STONE_GOAL = new StoneGoal(ALL_GOALS);
	public static final OreGoal ORE_GOAL = new OreGoal(ALL_GOALS);
	public static final ChooseProfessionGoal CHOOSE_PROFESSION_GOAL = new ChooseProfessionGoal(ALL_GOALS);
	public static final SmithGoal SMITH_GOAL = new SmithGoal(ALL_GOALS);
	public static final GatherFoodGoal GATHER_FOOD_GOAL = new GatherFoodGoal(ALL_GOALS);
	public static final CreateFoodSourcesGoal CREATE_FOOD_SOURCES_GOAL = new CreateFoodSourcesGoal(ALL_GOALS);
	public static final CreateWineGoal CREATE_WINE_GOAL = new CreateWineGoal(ALL_GOALS);
	public static final ChildrenGoal CHILDREN_GOAL = new ChildrenGoal(ALL_GOALS);
	public static final OffspringGoal OFFSPRING_GOAL = new OffspringGoal(ALL_GOALS);
	public static final SocializeGoal SOCIALIZE_GOAL = new SocializeGoal(ALL_GOALS);
	public static final CraftEquipmentGoal CRAFT_EQUIPMENT_GOAL = new CraftEquipmentGoal(ALL_GOALS);
	public static final ChooseDeityGoal CHOOSE_DEITY_GOAL = new ChooseDeityGoal(ALL_GOALS);
	public static final ShrineToDeityGoal SHRINE_TO_DEITY_GOAL = new ShrineToDeityGoal(ALL_GOALS);
	public static final RestGoal REST_GOAL = new RestGoal(ALL_GOALS);
	public static final IdleGoal IDLE_GOAL = new IdleGoal(ALL_GOALS);
	public static final SellWoodGoal SELL_WOOD_GOAL = new SellWoodGoal(ALL_GOALS);
	public static final SellStoneGoal SELL_STONE_GOAL = new SellStoneGoal(ALL_GOALS);
	public static final SellOreGoal SELL_ORE_GOAL = new SellOreGoal(ALL_GOALS);
	public static final CatchThievesGoal CATCH_THIEVES_GOAL = new CatchThievesGoal(ALL_GOALS);
	public static final CurseKissGoal CURSE_KISS_GOAL = new CurseKissGoal(ALL_GOALS);
	public static final SubdueOutsidersGoal SUBDUE_OUTSIDERS_GOAL = new SubdueOutsidersGoal(ALL_GOALS);
	public static final CocoonOutsidersGoal COCOON_OUTSIDERS_GOAL = new CocoonOutsidersGoal(ALL_GOALS);
	public static final MateGoal MATE_GOAL = new MateGoal(ALL_GOALS);
	public static final SexGoal SEX_GOAL = new SexGoal(ALL_GOALS);
	public static final MintGoldGoal MINT_GOLD_GOAL = new MintGoldGoal(ALL_GOALS);
	public static final MineResourcesGoal MINE_RESOURCES_GOAL = new MineResourcesGoal(ALL_GOALS);
	public static final MineGoldGoal MINE_GOLD_GOAL = new MineGoldGoal(ALL_GOALS);
	public static final SellGoldGoal SELL_GOLD_GOAL = new SellGoldGoal(ALL_GOALS);
	public static final PaperMillGoal PAPER_MILL_GOAL = new PaperMillGoal(ALL_GOALS);
	public static final CreatePaperGoal CREATE_PAPER_GOAL = new CreatePaperGoal(ALL_GOALS);
	public static final BecomeProfessionOrganizationMemberGoal BECOME_PROFESSION_ORGANIZATION_MEMBER_GOAL = new BecomeProfessionOrganizationMemberGoal(ALL_GOALS);
	public static final LearnSkillGoal LEARN_SKILL_GOAL = new LearnSkillGoal(ALL_GOALS);
	public static final StopSellingGoal STOP_SELLING_GOAL = new StopSellingGoal(ALL_GOALS);
	public static final LibraryGoal LIBRARY_GOAL = new LibraryGoal(ALL_GOALS);
	public static final ScribeClericSpellsGoal SCRIBE_CLERIC_SPELLS_GOAL = new ScribeClericSpellsGoal(ALL_GOALS);
	public static final ScribeWizardSpellsGoal SCRIBE_WIZARD_SPELLS_GOAL = new ScribeWizardSpellsGoal(ALL_GOALS);
	public static final PaperGoal PAPER_GOAL = new PaperGoal(ALL_GOALS);
	public static final GetPoisonCuredGoal GET_POISON_CURED_GOAL = new GetPoisonCuredGoal(ALL_GOALS);
	public static final GatherRemainsGoal GATHER_REMAINS_GOAL = new GatherRemainsGoal(ALL_GOALS);
	public static final CreateGraveGoal CREATE_GRAVE_GOAL = new CreateGraveGoal(ALL_GOALS);
	public static final OrganizationVoteGoal ORGANIZATION_VOTE_GOAL = new OrganizationVoteGoal(ALL_GOALS);
	public static final OrganizationCandidateGoal ORGANIZATION_CANDIDATE_GOAL = new OrganizationCandidateGoal(ALL_GOALS);
	public static final StartOrganizationVoteGoal START_ORGANIZATION_VOTE_GOAL = new StartOrganizationVoteGoal(ALL_GOALS);
	public static final CollectTaxesGoal COLLECT_TAXES_GOAL = new CollectTaxesGoal(ALL_GOALS);
	public static final SetTaxesGoal SET_TAXES_GOAL = new SetTaxesGoal(ALL_GOALS);
	public static final HandoverTaxesGoal HANDOVER_TAXES_GOAL = new HandoverTaxesGoal(ALL_GOALS);
	public static final CollectPayCheckGoal COLLECT_PAY_CHECK_GOAL = new CollectPayCheckGoal(ALL_GOALS);
	public static final CottonGoal COTTON_GOAL = new CottonGoal(ALL_GOALS);
	public static final WeaveClothesGoal WEAVE_CLOTHES_GOAL = new WeaveClothesGoal(ALL_GOALS);
	public static final BuyClothesGoal BUY_CLOTHES_GOAL = new BuyClothesGoal(ALL_GOALS);
	public static final VampireBloodLevelGoal VAMPIRE_BLOOD_LEVEL_GOAL = new VampireBloodLevelGoal(ALL_GOALS);
	public static final CreateHouseGoal CREATE_HOUSE_GOAL = new CreateHouseGoal(ALL_GOALS);
	public static final GetHealedGoal GET_HEALED_GOAL = new GetHealedGoal(ALL_GOALS);
	public static final SellHouseGoal SELL_HOUSE_GOAL = new SellHouseGoal(ALL_GOALS);
	public static final MarkBuildingAsSellableGoal MARK_HOUSE_AS_SELLABLE_GOAL = new MarkBuildingAsSellableGoal(BuildingType.HOUSE, ALL_GOALS);
	public static final MarkBuildingAsSellableGoal MARK_SMITH_AS_SELLABLE_GOAL = new MarkBuildingAsSellableGoal(BuildingType.SMITH, ALL_GOALS);
	public static final CreateFurnitureGoal CREATE_FURNITURE_GOAL = new CreateFurnitureGoal(ALL_GOALS);
	public static final SellBedsGoal SELL_BEDS_GOAL = new SellBedsGoal(ALL_GOALS);
	public static final SellKitchensGoal SELL_KITCHENS_GOAL = new SellKitchensGoal(ALL_GOALS);
	public static final FurnitureGoal FURNITURE_GOAL = new FurnitureGoal(ALL_GOALS);
	public static final CreateOrPlantWoodGoal CREATE_OR_PLANT_WOOD_GOAL = new CreateOrPlantWoodGoal(ALL_GOALS);
	public static final TrainGoal TRAIN_GOAL = new TrainGoal(ALL_GOALS);
	public static final ScribeNecromancerSpellsGoal SCRIBE_NECROMANCER_SPELLS_GOAL = new ScribeNecromancerSpellsGoal(ALL_GOALS);
	public static final KillVillagersGoal KILL_VILLAGERS_GOAL = new KillVillagersGoal(ALL_GOALS);
	public static final RevengeGoal REVENGE_GOAL = new RevengeGoal(ALL_GOALS);
	public static final BecomeReligionOrganizationMemberGoal BECOME_RELIGION_ORGANIZATION_MEMBER_GOAL = new BecomeReligionOrganizationMemberGoal(ALL_GOALS);
	public static final DestroyShrinesToOtherDeitiesGoal DESTROY_SHRINES_TO_OTHER_DEITIES_GOAL = new DestroyShrinesToOtherDeitiesGoal(ALL_GOALS);
	public static final HuntUndeadGoal HUNT_UNDEAD_GOAL = new HuntUndeadGoal(ALL_GOALS);
	public static final HealOthersGoal HEAL_OTHERS_GOAL = new HealOthersGoal(ALL_GOALS);
	public static final JailGoal JAIL_GOAL = new JailGoal(ALL_GOALS);
	public static final CaptureCriminalsGoal CAPTURE_CRIMINALS_GOAL = new CaptureCriminalsGoal(ALL_GOALS);
	public static final ReleaseCapturedCriminalsGoal RELEASE_CAPTURED_CRIMINALS_GOAL = new ReleaseCapturedCriminalsGoal(ALL_GOALS);
	public static final SacrificePeopleToDeityGoal SACRIFICE_PEOPLE_TO_DEITY_GOAL = new SacrificePeopleToDeityGoal(ALL_GOALS);
	public static final FishingPoleGoal FISHING_POLE_GOAL = new FishingPoleGoal(ALL_GOALS);
	public static final CatchFishGoal CATCH_FISH_GOAL = new CatchFishGoal(ALL_GOALS);
	public static final MarkFoodAsSellableGoal MARK_FOOD_AS_SELLABLE_GOAL = new MarkFoodAsSellableGoal(ALL_GOALS);
	public static final MarkWoodAsSellableGoal MARK_WOOD_AS_SELLABLE_GOAL = new MarkWoodAsSellableGoal(ALL_GOALS);
	public static final MarkStoneAsSellableGoal MARK_STONE_AS_SELLABLE_GOAL = new MarkStoneAsSellableGoal(ALL_GOALS);
	public static final MarkOreAsSellableGoal MARK_ORE_AS_SELLABLE_GOAL = new MarkOreAsSellableGoal(ALL_GOALS);
	public static final MarkGoldAsSellableGoal MARK_GOLD_AS_SELLABLE_GOAL = new MarkGoldAsSellableGoal(ALL_GOALS);
	public static final BrawlGoal BRAWL_GOAL = new BrawlGoal(ALL_GOALS);
	public static final ArenaGoal ARENA_GOAL = new ArenaGoal(ALL_GOALS);
	public static final ArenaFightGoal ARENA_FIGHT_GOAL = new ArenaFightGoal(ALL_GOALS);
	public static final FightInArenaGoal FIGHT_IN_ARENA_GOAL = new FightInArenaGoal(ALL_GOALS);
	public static final CollectArenaRewardGoal COLLECT_ARENA_REWARD_GOAL = new CollectArenaRewardGoal(ALL_GOALS);
	public static final DonateMoneyToArenaGoal DONATE_MONEY_TO_ARENA_GOAL = new DonateMoneyToArenaGoal(ALL_GOALS);
	public static final EquipmentGoal EQUIPMENT_GOAL = new EquipmentGoal(ALL_GOALS);
	public static final MarkEquipmentAsSellableGoal MARK_EQUIPMENT_AS_SELLABLE_GOAL = new MarkEquipmentAsSellableGoal(ALL_GOALS);
	public static final FeedOthersGoal FEED_OTHERS_GOAL = new FeedOthersGoal(ALL_GOALS);
	public static final ResearchMagicSkillsKnowledgeGoal RESEARCH_MAGIC_SKILLS_KNOWLEDGE_GOAL = new ResearchMagicSkillsKnowledgeGoal(ALL_GOALS);
	public static final GhoulGoal GHOUL_GOAL = new GhoulGoal(ALL_GOALS);
	public static final CreatePoisonGoal CREATE_POISON_GOAL = new CreatePoisonGoal(ALL_GOALS);
	public static final RecruitProfessionOrganizationMembersGoal RECRUIT_PROFESSION_ORGANIZATION_MEMBERS_GOAL = new RecruitProfessionOrganizationMembersGoal(ALL_GOALS);
	public static final MarkClothesAsSellableGoal MARK_CLOTHES_AS_SELLABLE_GOAL = new MarkClothesAsSellableGoal(ALL_GOALS);
	public static final CreateNewsPaperGoal CREATE_NEWS_PAPER_GOAL = new CreateNewsPaperGoal(ALL_GOALS);
	public static final SellNewsPaperGoal SELL_NEWS_PAPER_GOAL = new SellNewsPaperGoal(ALL_GOALS);
	public static final ReadNewsPaperGoal READ_NEWS_PAPER_GOAL = new ReadNewsPaperGoal(ALL_GOALS);
	public static final WineGoal WINE_GOAL = new WineGoal(ALL_GOALS);
	public static final StartBrawlGoal START_BRAWL_GOAL = new StartBrawlGoal(ALL_GOALS);
	public static final StealGoal STEAL_GOAL = new StealGoal(ALL_GOALS);
	public static final SellStolenGoodsGoal SELL_STOLEN_GOODS_GOAL = new SellStolenGoodsGoal(ALL_GOALS);
	public static final CreateRepairHammerGoal CREATE_REPAIR_HAMMER_GOAL = new CreateRepairHammerGoal(ALL_GOALS);
	public static final RepairEquipmentGoal REPAIR_EQUIPMENT_GOAL = new RepairEquipmentGoal(ALL_GOALS);
	public static final StartDrinkingContestGoal START_DRINKING_CONTEST_GOAL = new StartDrinkingContestGoal(ALL_GOALS);
	public static final BreakupWithMateGoal BREAKUP_WITH_MATE_GOAL = new BreakupWithMateGoal(ALL_GOALS);
	public static final LegalizeVampirismGoal LEGALIZE_VAMPIRISM_GOAL = new LegalizeVampirismGoal(ALL_GOALS);
	public static final SoulGemGoal SOUL_GEM_GOAL = new SoulGemGoal(ALL_GOALS);
	public static final FillSoulGemGoal FILL_SOUL_GEM_GOAL = new FillSoulGemGoal(ALL_GOALS);
	public static final BecomeLichGoal BECOME_LICH_GOAL = new BecomeLichGoal(ALL_GOALS);
	public static final GetDiseaseCuredGoal GET_DISEASE_CURED_GOAL = new GetDiseaseCuredGoal(ALL_GOALS);
	public static final SwitchDeityGoal SWITCH_DEITY_GOAL = new SwitchDeityGoal(ALL_GOALS);
	public static final AssassinateTargetGoal ASSASSINATE_TARGET_GOAL = new AssassinateTargetGoal(ALL_GOALS);
	public static final FindAssassinationClientGoal FIND_ASSASSINATION_CLIENT_GOAL = new FindAssassinationClientGoal(ALL_GOALS);
	public static final CreateSleepingPotionGoal CREATE_SLEEPING_POTION_GOAL = new CreateSleepingPotionGoal(ALL_GOALS);
	public static final SellPoisonGoal SELL_POISON_GOAL = new SellPoisonGoal(ALL_GOALS);
	public static final SellSleepingPotionGoal SELL_SLEEPING_POTION_GOAL = new SellSleepingPotionGoal(ALL_GOALS);
	public static final PoisonWeaponGoal POISON_WEAPON_GOAL = new PoisonWeaponGoal(ALL_GOALS);
	public static final StandStillToTalkGoal STAND_STILL_TO_TALK_GOAL = new StandStillToTalkGoal(ALL_GOALS);
	public static final SellWineGoal SELL_WINE_GOAL = new SellWineGoal(ALL_GOALS);
	public static final RedistributeGoldAmongFamilyGoal REDISTRIBUTE_GOLD_AMONG_FAMILY_GOAL = new RedistributeGoldAmongFamilyGoal(ALL_GOALS);
	public static final WeaveryGoal WEAVERY_GOAL = new WeaveryGoal(ALL_GOALS);
	public static final WorkbenchGoal WORKBENCH_GOAL = new WorkbenchGoal(ALL_GOALS);
	public static final GoldGoal GOLD_GOAL = new GoldGoal(ALL_GOALS);
	public static final BreweryGoal BREWERY_GOAL = new BreweryGoal(ALL_GOALS);
	public static final ApothecaryGoal APOTHECARY_GOAL = new ApothecaryGoal(ALL_GOALS);
	public static final GhoulMeatLevelGoal GHOUL_MEAT_LEVEL_GOAL = new GhoulMeatLevelGoal(ALL_GOALS);
	public static final PayBountyGoal PAY_BOUNTY_GOAL = new PayBountyGoal(ALL_GOALS);
	public static final AnimalFoodGoal ANIMAL_FOOD_GOAL = new AnimalFoodGoal(ALL_GOALS);
	public static final ScribeTricksterSpellsGoal SCRIBE_TRICKSTER_SPELLS_GOAL = new ScribeTricksterSpellsGoal(ALL_GOALS);
	public static final SwindleMoneyGoal SWINDLE_MONEY_GOAL = new SwindleMoneyGoal(ALL_GOALS);
	public static final MineSoulGemsGoal MINE_SOUL_GEMS_GOAL = new MineSoulGemsGoal(ALL_GOALS);
	public static final MineStoneGoal MINE_STONE_GOAL = new MineStoneGoal(ALL_GOALS);
	public static final HarvestNightShadeGoal HARVEST_NIGHT_SHADE_GOAL = new HarvestNightShadeGoal(ALL_GOALS);
	public static final HarvestCottonGoal HARVEST_COTTON_GOAL = new HarvestCottonGoal(ALL_GOALS);
	public static final HarvestGrapesGoal HARVEST_GRAPES_GOAL = new HarvestGrapesGoal(ALL_GOALS);
	public static final MineOreGoal MINE_ORE_GOAL = new MineOreGoal(ALL_GOALS);
	public static final AdjustPricesGoal ADJUST_PRICES_GOAL = new AdjustPricesGoal(ALL_GOALS);
	public static final MarkNewsPaperAsSellableGoal MARK_NEWS_PAPER_AS_SELLABLE_GOAL = new MarkNewsPaperAsSellableGoal(ALL_GOALS);
	public static final RepairHammerGoal REPAIR_HAMMER_GOAL = new RepairHammerGoal(ALL_GOALS);
	public static final MarkRepairHammersAsSellableGoal MARK_REPAIR_HAMMERS_AS_SELLABLE_GOAL = new MarkRepairHammersAsSellableGoal(ALL_GOALS);
	public static final CreateFishingPoleGoal CREATE_FISHING_POLE_GOAL = new CreateFishingPoleGoal(ALL_GOALS);
	public static final CreatePickaxeGoal CREATE_PICKAXE_GOAL = new CreatePickaxeGoal(ALL_GOALS);
	public static final MarkFishingPoleAsSellableGoal MARK_FISHING_POLE_AS_SELLABLE_GOAL = new MarkFishingPoleAsSellableGoal(ALL_GOALS);
	public static final MarkPickaxeAsSellableGoal MARK_PICKAXE_AS_SELLABLE_GOAL = new MarkPickaxeAsSellableGoal(ALL_GOALS);
	public static final PickaxeGoal PICKAXE_GOAL = new PickaxeGoal(ALL_GOALS);
	public static final EquipPickaxeGoal EQUIP_PICKAXE_GOAL = new EquipPickaxeGoal(ALL_GOALS);
	public static final CreateScytheGoal CREATE_SCYTHE_GOAL = new CreateScytheGoal(ALL_GOALS);
	public static final ScytheGoal SCYTHE_GOAL = new ScytheGoal(ALL_GOALS);
	public static final EquipScytheGoal EQUIP_SCYTHE_GOAL = new EquipScytheGoal(ALL_GOALS);
	public static final MarkScytheAsSellableGoal MARK_SCYTHE_AS_SELLABLE_GOAL = new MarkScytheAsSellableGoal(ALL_GOALS);
	public static final WoodCuttingToolGoal WOOD_CUTTING_TOOL_GOAL = new WoodCuttingToolGoal(ALL_GOALS);
	public static final EquipWoodCuttingToolGoal EQUIP_WOOD_CUTTING_TOOL_GOAL = new EquipWoodCuttingToolGoal(ALL_GOALS);
	public static final CreateIronAxeGoal CREATE_IRON_AXE_GOAL = new CreateIronAxeGoal(ALL_GOALS);
	public static final SellFoodGoal SELL_FOOD_GOAL = new SellFoodGoal(ALL_GOALS);
	public static final CreateSmithGoal CREATE_SMITH_GOAL = new CreateSmithGoal(ALL_GOALS);
	public static final CreateWeaveryGoal CREATE_WEAVERY_GOAL = new CreateWeaveryGoal(ALL_GOALS);
	public static final CreatePaperMillGoal CREATE_PAPER_MILL_GOAL = new CreatePaperMillGoal(ALL_GOALS);
	public static final CreateWorkbenchGoal CREATE_WORKBENCH_GOAL = new CreateWorkbenchGoal(ALL_GOALS);
	public static final CreateApothecaryGoal CREATE_APOTHECARY_GOAL = new CreateApothecaryGoal(ALL_GOALS);
	public static final CreateBreweryGoal CREATE_BREWERY_GOAL = new CreateBreweryGoal(ALL_GOALS);
	public static final ClaimCattleGoal CLAIM_CATTLE_GOAL = new ClaimCattleGoal(ALL_GOALS);
	public static final ButcherOwnedCattleGoal BUTCHER_OWNED_CATTLE_GOAL = new ButcherOwnedCattleGoal(ALL_GOALS);
	public static final CreateButcherKnifeGoal CREATE_BUTCHER_KNIFE_GOAL = new CreateButcherKnifeGoal(ALL_GOALS);
	public static final ButcherKnifeGoal BUTCHER_KNIFE_GOAL = new ButcherKnifeGoal(ALL_GOALS);
	public static final EquipButcherKnifeGoal EQUIP_BUTCHER_KNIFE_GOAL = new EquipButcherKnifeGoal(ALL_GOALS);
	public static final EquipRepairHammerGoal EQUIP_REPAIR_HAMMER_GOAL = new EquipRepairHammerGoal(ALL_GOALS);
	public static final EquipSawGoal EQUIP_SAW_GOAL = new EquipSawGoal(ALL_GOALS);
	
	public static final DeityBoonGoal ARES_BOON_GOAL = new DeityBoonGoal(Deity.ARES);
	public static final DeityBoonGoal HEPHAESTUS_BOON_GOAL = new DeityBoonGoal(Deity.HEPHAESTUS);
	public static final DeityBoonGoal DEMETER_BOON_GOAL = new DeityBoonGoal(Deity.DEMETER);
	public static final DeityBoonGoal POSEIDON_BOON_GOAL = new DeityBoonGoal(Deity.POSEIDON);

	public static final MarkUnusedBuildingAsSellableGoal MARK_WORKBENCH_AS_SELLABLE_AFTER_EQUIPING_SCYTHE = new MarkUnusedBuildingAsSellableGoal(Constants.SCYTHE_QUALITY, BuildingType.WORKBENCH, ALL_GOALS);
	public static final MarkUnusedBuildingAsSellableGoal MARK_WORKBENCH_AS_SELLABLE_AFTER_EQUIPING_WOOD_CUTTING = new MarkUnusedBuildingAsSellableGoal(Constants.WOOD_CUTTING_QUALITY, BuildingType.WORKBENCH, ALL_GOALS);
	public static final MarkUnusedBuildingAsSellableGoal MARK_WORKBENCH_AS_SELLABLE_AFTER_EQUIPING_BUTCHER_KNIFE = new MarkUnusedBuildingAsSellableGoal(Constants.BUTCHER_QUALITY, BuildingType.WORKBENCH, ALL_GOALS);
	public static final MarkUnusedBuildingAsSellableGoal MARK_WORKBENCH_AS_SELLABLE_AFTER_EQUIPING_PICKAXE = new MarkUnusedBuildingAsSellableGoal(Constants.PICKAXE_QUALITY, BuildingType.WORKBENCH, ALL_GOALS);
	public static final MarkUnusedBuildingAsSellableGoal MARK_WORKBENCH_AS_SELLABLE_AFTER_EQUIPING_FISHING_POLE = new MarkUnusedBuildingAsSellableGoal(Constants.FISHING_POLE_QUALITY, BuildingType.WORKBENCH, ALL_GOALS);

	public static final CreateHealingPotionGoal CREATE_HEALING_POTION_GOAL = new CreateHealingPotionGoal(ALL_GOALS);
	public static final SellHealingPotionGoal SELL_HEALING_POTION_GOAL = new SellHealingPotionGoal(ALL_GOALS);
	public static final MarkAsSellableOrDropGoal MARK_LEATHER_AS_SELLABLE_OR_DROP_GOAL = new MarkAsSellableOrDropGoal(Constants.LEATHER, ALL_GOALS);
	public static final LeatherGoal LEATHER_GOAL = new LeatherGoal(ALL_GOALS);
	public static final WeaveLeatherArmorGoal WEAVE_LEATHER_ARMOR_GOAL = new WeaveLeatherArmorGoal(ALL_GOALS);
	public static final UseEquipmentGoal USE_EQUIPMENT_GOAL = new UseEquipmentGoal(ALL_GOALS);
	public static final SellUnusedItemsGoal SELL_UNUSED_ITEMS_GOAL = new SellUnusedItemsGoal(ALL_GOALS);
	public static final RemoveCurseGoal REMOVE_CURSE_GOAL = new RemoveCurseGoal(ALL_GOALS);
	public static final RebellionGoal REBELLION_GOAL = new RebellionGoal(ALL_GOALS);
	public static final CreateSawGoal CREATE_SAW_GOAL = new CreateSawGoal(ALL_GOALS);
	public static final SawGoal SAW_GOAL = new SawGoal(ALL_GOALS);
	public static final FireTaxCollectorGoal FIRE_TAX_COLLECTOR_GOAL = new FireTaxCollectorGoal(ALL_GOALS);
	public static final WantedProfessionGoal WANTED_PROFESSION_GOAL = new WantedProfessionGoal(ALL_GOALS);
	public static final PlantNightShadeGoal PLANT_NIGHT_SHADE_GOAL = new PlantNightShadeGoal(ALL_GOALS);
	public static final BedGoal BED_GOAL = new BedGoal(ALL_GOALS);
}
