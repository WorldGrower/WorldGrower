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

import java.util.Arrays;
import java.util.List;

import org.worldgrower.ManagedOperation;

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
	public static final CraftIronBootsAction CRAFT_IRON_BOOTS_ACTION = new CraftIronBootsAction();
	public static final CraftLongBowAction CRAFT_LONG_BOW_ACTION = new CraftLongBowAction();
	public static final ChooseDeityAction CHOOSE_DEITY_ACTION = new ChooseDeityAction();
	public static final WorshipDeityAction WORSHIP_DEITY_ACTION = new WorshipDeityAction();
	public static final BuildShrineAction BUILD_SHRINE_ACTION = new BuildShrineAction();
	public static final RestAction REST_ACTION = new RestAction();
	public static final SleepAction SLEEP_ACTION = new SleepAction();
	public static final StealAction STEAL_ACTION = new StealAction();
	public static final KissAction KISS_ACTION = new KissAction();
	public static final PoisonAttackAction POISON_ATTACK_ACTION = new PoisonAttackAction();
	public static final CocoonAction COCOON_ACTION = new CocoonAction();
	public static final ReadAction READ_ACTION = new ReadAction();
	public static final DisguiseAction DISGUISE_ACTION = new DisguiseAction();
	public static final FireBoltAttackAction FIRE_BOLT_ATTACK_ACTION = new FireBoltAttackAction();
	public static final RayOfFrostAttackAction RAY_OF_FROST_ATTACK_ACTION = new RayOfFrostAttackAction();
	public static final BrewWineAction BREW_WINE_ACTION = new BrewWineAction();
	public static final MinorIllusionAction MINOR_ILLUSION_ACTION = new MinorIllusionAction();
	public static final BuildPaperMillAction BUILD_PAPER_MILL_ACTION = new BuildPaperMillAction();
	public static final CreatePaperAction CREATE_PAPER_ACTION = new CreatePaperAction();
	public static final CollectWaterAction COLLECT_WATER_ACTION = new CollectWaterAction();
	public static final CreateOrganizationAction CREATE_ORGANIZATION_ACTION = new CreateOrganizationAction();
	public static final MinorHealAction MINOR_HEAL_ACTION = new MinorHealAction();
	public static final BuildLibraryAction BUILD_LIBRARY_ACTION = new BuildLibraryAction();
	public static final ResearchReligionSkillAction RESEARCH_RELIGION_SKILL_ACTION = new ResearchReligionSkillAction();
	public static final ResearchIllusionSkillAction RESEARCH_ILLUSION_SKILL_ACTION = new ResearchIllusionSkillAction();
	public static final ResearchEvocationSkillAction RESEARCH_EVOCATION_SKILL_ACTION = new ResearchEvocationSkillAction();
	public static final ResearchSpellAction RESEARCH_MINOR_HEALING_ACTION = new ResearchSpellAction(MINOR_HEAL_ACTION);
	public static final ResearchSpellAction RESEARCH_MINOR_ILLUSION_ACTION = new ResearchSpellAction(MINOR_ILLUSION_ACTION);
	public static final ResearchSpellAction RESEARCH_FIRE_BOLT_ACTION = new ResearchSpellAction(FIRE_BOLT_ATTACK_ACTION);
	public static final ResearchSpellAction RESEARCH_RAY_OF_FROST_ACTION = new ResearchSpellAction(RAY_OF_FROST_ATTACK_ACTION);
	public static final GetItemFromInventoryAction GET_ITEM_FROM_INVENTORY_ACTION = new GetItemFromInventoryAction();
	public static final PutItemIntoInventoryAction PUT_ITEM_INTO_INVENTORY_ACTION = new PutItemIntoInventoryAction();
	public static final ScribeMagicSpellAction SCRIBE_MINOR_HEALING_ACTION = new ScribeMagicSpellAction(MINOR_HEAL_ACTION);
	public static final ScribeMagicSpellAction SCRIBE_MINOR_ILLUSION_ACTION = new ScribeMagicSpellAction(MINOR_ILLUSION_ACTION);
	public static final ScribeMagicSpellAction SCRIBE_FIRE_BOLT_ACTION = new ScribeMagicSpellAction(FIRE_BOLT_ATTACK_ACTION);
	public static final ScribeMagicSpellAction SCRIBE_RAY_OF_FROST_ACTION = new ScribeMagicSpellAction(RAY_OF_FROST_ATTACK_ACTION);
	
	public static final List<ManagedOperation> ALL_ACTIONS = Arrays.asList(
		MOVE_ACTION,
		EAT_ACTION,
		EAT_NIGHT_SHADE_ACTION,
		EAT_FROM_INVENTORY_ACTION,
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
		CRAFT_IRON_BOOTS_ACTION,
		CRAFT_LONG_BOW_ACTION,
		CHOOSE_DEITY_ACTION,
		WORSHIP_DEITY_ACTION,
		BUILD_SHRINE_ACTION,
		REST_ACTION,
		SLEEP_ACTION,
		STEAL_ACTION,
		KISS_ACTION,
		POISON_ATTACK_ACTION,
		COCOON_ACTION,
		READ_ACTION,
		DISGUISE_ACTION,
		FIRE_BOLT_ATTACK_ACTION,
		RAY_OF_FROST_ATTACK_ACTION,
		MINT_GOLD_ACTION,
		BREW_WINE_ACTION,
		MINOR_ILLUSION_ACTION,
		CREATE_PAPER_ACTION,
		COLLECT_WATER_ACTION,
		CREATE_ORGANIZATION_ACTION,
		MINOR_HEAL_ACTION,
		BUILD_LIBRARY_ACTION,
		RESEARCH_RELIGION_SKILL_ACTION,
		RESEARCH_ILLUSION_SKILL_ACTION,
		RESEARCH_EVOCATION_SKILL_ACTION,
		RESEARCH_MINOR_HEALING_ACTION,
		RESEARCH_MINOR_ILLUSION_ACTION,
		RESEARCH_RAY_OF_FROST_ACTION,
		GET_ITEM_FROM_INVENTORY_ACTION,
		PUT_ITEM_INTO_INVENTORY_ACTION,
		SCRIBE_MINOR_HEALING_ACTION,
		SCRIBE_MINOR_ILLUSION_ACTION,
		SCRIBE_FIRE_BOLT_ACTION,
		SCRIBE_RAY_OF_FROST_ACTION 
	);
}
