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

public class Goals {

	public static final List<Goal> ALL_GOALS = new ArrayList<>();
	
	public static final FoodGoal FOOD_GOAL = new FoodGoal(ALL_GOALS);
	public static final DrinkWaterGoal DRINK_WATER_GOAL = new DrinkWaterGoal(ALL_GOALS);
	public static final CollectWaterGoal COLLECT_WATER_GOAL = new CollectWaterGoal(ALL_GOALS);
	public static final WoodGoal WOOD_GOAL = new WoodGoal(ALL_GOALS);
	public static final CreateWoodGoal CREATE_WOOD_GOAL = new CreateWoodGoal(ALL_GOALS);
	public static final KillOutsidersGoal KILL_OUTSIDERS_GOAL = new KillOutsidersGoal(ALL_GOALS);
	public static final ShackGoal SHACK_GOAL = new ShackGoal(ALL_GOALS);
	public static final ProtectOnseSelfGoal PROTECT_ONSE_SELF_GOAL = new ProtectOnseSelfGoal(ALL_GOALS);
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
	public static final MarkHouseAsSellableGoal MARK_HOUSE_AS_SELLABLE_GOAL = new MarkHouseAsSellableGoal(ALL_GOALS);
	public static final CreateFurnitureGoal CREATE_FURNITURE_GOAL = new CreateFurnitureGoal(ALL_GOALS);
	public static final SellFurnitureGoal SELL_FURNITURE_GOAL = new SellFurnitureGoal(ALL_GOALS);
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
	public static final RepairHammerGoal REPAIR_HAMMER_GOAL = new RepairHammerGoal(ALL_GOALS);
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
}
