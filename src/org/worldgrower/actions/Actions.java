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
import java.util.List;
import java.util.stream.Collectors;

import org.worldgrower.ManagedOperation;
import org.worldgrower.actions.magic.AnimateDeadAction;
import org.worldgrower.actions.magic.CurePoisonAction;
import org.worldgrower.actions.magic.FireBoltAttackAction;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.actions.magic.MinorHealAction;
import org.worldgrower.actions.magic.MinorIllusionAction;
import org.worldgrower.actions.magic.RayOfFrostAttackAction;
import org.worldgrower.actions.magic.ResearchSpellAction;
import org.worldgrower.actions.magic.ScribeMagicSpellAction;

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
	public static final BrewPoisonAction BREW_POISON_ACTION = new BrewPoisonAction();
	public static final MinorIllusionAction MINOR_ILLUSION_ACTION = new MinorIllusionAction();
	public static final BuildPaperMillAction BUILD_PAPER_MILL_ACTION = new BuildPaperMillAction();
	public static final CreatePaperAction CREATE_PAPER_ACTION = new CreatePaperAction();
	public static final CollectWaterAction COLLECT_WATER_ACTION = new CollectWaterAction();
	public static final CreateOrganizationAction CREATE_ORGANIZATION_ACTION = new CreateOrganizationAction();
	public static final MinorHealAction MINOR_HEAL_ACTION = new MinorHealAction();
	public static final CurePoisonAction CURE_POISON_ACTION = new CurePoisonAction();
	public static final BuildLibraryAction BUILD_LIBRARY_ACTION = new BuildLibraryAction();
	public static final ResearchReligionSkillAction RESEARCH_RELIGION_SKILL_ACTION = new ResearchReligionSkillAction();
	public static final ResearchIllusionSkillAction RESEARCH_ILLUSION_SKILL_ACTION = new ResearchIllusionSkillAction();
	public static final ResearchEvocationSkillAction RESEARCH_EVOCATION_SKILL_ACTION = new ResearchEvocationSkillAction();
	public static final GetItemFromInventoryAction GET_ITEM_FROM_INVENTORY_ACTION = new GetItemFromInventoryAction();
	public static final PutItemIntoInventoryAction PUT_ITEM_INTO_INVENTORY_ACTION = new PutItemIntoInventoryAction();
	public static final PoisonAction POISON_ACTION = new PoisonAction();
	public static final DrinkFromInventoryAction DRINK_FROM_INVENTORY_ACTION = new DrinkFromInventoryAction();
	public static final AnimateDeadAction ANIMATE_DEAD_ACTION = new AnimateDeadAction();
	public static final CommandAction COMMAND_ACTION = new CommandAction();
	public static final GatherRemainsAction GATHER_REMAINS_ACTION = new GatherRemainsAction();
	public static final CreateGraveAction CREATE_GRAVE_ACTION = new CreateGraveAction();
	public static final BecomeLeaderCandidateAction BECOME_LEADER_CANDIDATE_ACTION = new BecomeLeaderCandidateAction();
	public static final VoteForLeaderAction VOTE_FOR_LEADER_ACTION = new VoteForLeaderAction();
	
	private static final List<MagicSpell> MAGIC_SPELLS = Arrays.asList(
			MINOR_HEAL_ACTION, 
			MINOR_ILLUSION_ACTION, 
			FIRE_BOLT_ATTACK_ACTION, 
			RAY_OF_FROST_ATTACK_ACTION, 
			CURE_POISON_ACTION,
			ANIMATE_DEAD_ACTION);
	
	public static final List<ManagedOperation> ALL_ACTIONS = new ArrayList<>(Arrays.asList(
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
		MINT_GOLD_ACTION,
		BREW_WINE_ACTION,
		CREATE_PAPER_ACTION,
		COLLECT_WATER_ACTION,
		CREATE_ORGANIZATION_ACTION,
		BUILD_LIBRARY_ACTION,
		RESEARCH_RELIGION_SKILL_ACTION,
		RESEARCH_ILLUSION_SKILL_ACTION,
		RESEARCH_EVOCATION_SKILL_ACTION,
		GET_ITEM_FROM_INVENTORY_ACTION,
		PUT_ITEM_INTO_INVENTORY_ACTION,
		BREW_POISON_ACTION,
		POISON_ACTION,
		DRINK_FROM_INVENTORY_ACTION,
		COMMAND_ACTION,
		GATHER_REMAINS_ACTION,
		CREATE_GRAVE_ACTION,
		BUILD_PAPER_MILL_ACTION,
		BECOME_LEADER_CANDIDATE_ACTION,
		VOTE_FOR_LEADER_ACTION
	));
	
	static {
		ALL_ACTIONS.addAll(MAGIC_SPELLS);
		
		for(MagicSpell magicSpell : MAGIC_SPELLS) {
			ResearchSpellAction researchSpellAction = new ResearchSpellAction(magicSpell);
			ALL_ACTIONS.add(researchSpellAction);
			
			ScribeMagicSpellAction scribeMagicSpellAction = new ScribeMagicSpellAction(magicSpell);
			ALL_ACTIONS.add(scribeMagicSpellAction);
		}
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
	
	public static List<ManagedOperation> getAllScribeMagicSpellActions() {
		return ALL_ACTIONS.stream().filter(operation -> operation.getClass() == ScribeMagicSpellAction.class).collect(Collectors.toList());
	}
	
	public static List<MagicSpell> getMagicSpells() {
		return MAGIC_SPELLS;
	}
}
