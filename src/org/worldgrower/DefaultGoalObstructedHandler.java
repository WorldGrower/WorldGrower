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
package org.worldgrower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.worldgrower.actions.Actions;
import org.worldgrower.actions.legal.LegalActions;
import org.worldgrower.attribute.IdList;
import org.worldgrower.condition.Condition;
import org.worldgrower.goal.ArenaPropertyUtils;
import org.worldgrower.goal.BrawlPropertyUtils;
import org.worldgrower.goal.FacadeUtils;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.LegalActionsPropertyUtils;

public class DefaultGoalObstructedHandler implements GoalObstructedHandler {

	// this structure is for debugging purposes, to check what npcs have
	// been thrown out of groups and for what.
	private static final List<String> THROWN_OUT_OF_GROUP_EVENTS = new ArrayList<>();
	
	@Override
	public void goalHindered(Goal obstructedGoal, WorldObject performer, WorldObject target, int stepsUntilLastGoal, int goalEvaluationDecrease, WorldObject actionTarget, ManagedOperation managedOperation, int[] args, World world) {
		if (!Actions.isMutuallyAgreedAction(managedOperation)) {
			if (performer.hasProperty(Constants.RELATIONSHIPS) && target.hasProperty(Constants.RELATIONSHIPS)) {
				if (hasAnyoneSeenAction(performer, actionTarget, managedOperation, args, world)) {
					int value = -100 * stepsUntilLastGoal;
					
					WorldObject performerFacade = FacadeUtils.createFacade(performer, performer, target, world);
					WorldObject targetFacade = calculateTarget(performer, target, world);
					
					logToBackground(obstructedGoal, target, actionTarget, managedOperation, args, performerFacade, world);
					
					alterRelationships(performer, target, actionTarget, args, managedOperation, world, value, performerFacade, targetFacade);
				}
			} else if (target.hasProperty(Constants.ANIMAL_ENEMIES) && performerAttacked(managedOperation)) {
				performerAttacksAnimal(performer, target);
			}
		}
	}

	static void performerAttacksAnimal(WorldObject performer, WorldObject target) {
		IdList animalEnemies = target.getProperty(Constants.ANIMAL_ENEMIES);
		if (!animalEnemies.contains(performer)) {
			animalEnemies.add(performer);
		}
	}

	static WorldObject calculateTarget(WorldObject performer, WorldObject target, World world) {
		WorldObject targetFacade = FacadeUtils.createFacade(target, performer, target, world);
		if (targetFacade.hasProperty(Constants.ILLUSION_CREATOR_ID)) {
			WorldObject maskedWorldObject = new WorldFacade(performer, world).getWorldObjectMaskedByIllusion(targetFacade, world);
			if (maskedWorldObject != null) {
				targetFacade = maskedWorldObject;
			}
		}
		return targetFacade;
	}
	
	//TODO: radius should depend on perception
	static boolean hasAnyoneSeenAction(WorldObject performer, WorldObject actionTarget, ManagedOperation managedOperation, int[] args, World world) {
		if (performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.INVISIBLE_CONDITION)) {
			return false;
		} else {
			List<WorldObject> targets = world.findWorldObjects(w -> (Reach.distance(performer, w) < 6 || Reach.distance(actionTarget, w) < 6) && w.hasIntelligence() && !w.equals(performer));
			return targets.size() > 0;
		}
	}

	static void alterRelationships(WorldObject performer, WorldObject target, WorldObject actionTarget, int[] args, ManagedOperation managedOperation, World world, int value, WorldObject performerFacade, WorldObject targetFacade) {
		if (!isLegallyFighting(performer, actionTarget, managedOperation)) {
			if (world.exists(performer) && world.exists(target) && world.exists(performerFacade) && world.exists(targetFacade)) {
				performer.getProperty(Constants.RELATIONSHIPS).incrementValue(targetFacade.getProperty(Constants.ID), value);
				target.getProperty(Constants.RELATIONSHIPS).incrementValue(performerFacade.getProperty(Constants.ID), value);
			}
		}
	}

	static int calculateBounty(ManagedOperation managedOperation) {
		final int bounty;
		if (performerAttacked(managedOperation)) {
			bounty = 200;
		} else {
			bounty = 40;
		}
		return bounty;
	}

	private static void throwOutOfGroup(WorldObject performer, WorldObject target, WorldObject actionTarget, int[] args, ManagedOperation managedOperation, World world, WorldObject performerFacade) {
		THROWN_OUT_OF_GROUP_EVENTS.add(world.getCurrentTurn().getValue() + ": " + performer.getProperty(Constants.NAME) + " performed action " + managedOperation + " on " + target.getProperty(Constants.NAME) + " (id:" + target.getProperty(Constants.ID) + ") and was thrown out of groups (goal performer = " + performer.getProperty(Constants.META_INFORMATION).getFinalGoal() + "). perf.has(BrawlOpponentId) = " + performer.hasProperty(Constants.BRAWL_OPPONENT_ID) + ", perf.get(BrawlOpponentId) = " + performer.getProperty(Constants.BRAWL_OPPONENT_ID) + ", trgt.has(BrawlOpponentId) = " + target.hasProperty(Constants.BRAWL_OPPONENT_ID) + ", trgt.get(BrawlOpponentId) = " + target.getProperty(Constants.BRAWL_OPPONENT_ID));		
		
		IdList oldGroup = performer.getProperty(Constants.GROUP).copy();
		GroupPropertyUtils.throwPerformerOutGroup(performerFacade, target, world);
		
		WorldObject realPerformer = world.findWorldObjectById(performerFacade.getProperty(Constants.ID));
		GroupPropertyUtils.throwPerformerOutGroup(realPerformer, target, world);
		
		IdList newGroup = performer.getProperty(Constants.GROUP).copy();
		world.getWorldStateChangedListeners().thrownOutOfGroup(performer, actionTarget, args, managedOperation, oldGroup, newGroup);
	}

	static boolean areBrawling(WorldObject performer, WorldObject actionTarget, ManagedOperation managedOperation) {
		return BrawlPropertyUtils.isBrawling(performer) 
				&& BrawlPropertyUtils.isBrawling(actionTarget) 
				&& managedOperation == Actions.NON_LETHAL_MELEE_ATTACK_ACTION
				&& performer.getProperty(Constants.LEFT_HAND_EQUIPMENT) == null
				&& performer.getProperty(Constants.RIGHT_HAND_EQUIPMENT) == null;
	}

	static boolean areFightingInArena(WorldObject performer, WorldObject actionTarget, ManagedOperation managedOperation) {
		return ArenaPropertyUtils.peopleAreFightingEachOther(performer, actionTarget);
	}
	
	//TODO: clean up this code
	public static boolean performerAttacked(ManagedOperation managedOperation) {
		Class<?> actionClass = managedOperation.getClass();
		return (actionClass == Actions.MELEE_ATTACK_ACTION.getClass())
				|| (actionClass == Actions.NON_LETHAL_MELEE_ATTACK_ACTION.getClass())
				|| (actionClass == Actions.RANGED_ATTACK_ACTION.getClass())
				|| (actionClass == Actions.FIRE_BOLT_ATTACK_ACTION.getClass())
				|| (actionClass == Actions.RAY_OF_FROST_ATTACK_ACTION.getClass()
				|| (actionClass == Actions.INFLICT_WOUNDS_ACTION.getClass()
				|| (actionClass == Actions.VAMPIRE_BITE_ACTION.getClass())
				|| (actionClass == Actions.CREATE_BLOOD_ACTION.getClass()))
				|| (actionClass == Actions.FIRE_BALL_ATTACK_ACTION.getClass())
				);
	}
	
	public static List<ManagedOperation> getNonAttackingIllegalActions() {
		return Arrays.asList(
				Actions.STEAL_ACTION, 
				Actions.STEAL_GOLD_ACTION,
				Actions.BURDEN_ACTION,
				Actions.DISINTEGRATE_ARMOR_ACTION,
				Actions.DISINTEGRATE_WEAPON_ACTION,
				Actions.FEAR_MAGIC_SPELL_ACTION,
				Actions.PARALYZE_SPELL_ACTION,
				Actions.REDUCE_ACTION,
				Actions.SILENCE_MAGIC_ACTION,
				Actions.SLEEP_MAGIC_SPELL_ACTION
				);
	}

	static boolean performerViolatedGroupRules(WorldObject performer, WorldObject actionTarget, int[] args, ManagedOperation managedOperation, World world) {
		LegalActions legalActions = LegalActionsPropertyUtils.getLegalActions(world);
		Boolean isLegal = legalActions.isLegalAction(performer, actionTarget, args, managedOperation);
		if (isLegal != null) {
			boolean violatedGroupRules = !isLegal.booleanValue();
			if (violatedGroupRules) {
				if (actionTarget.hasProperty(Constants.GROUP)) {
					boolean performerCanAttackCriminals = performerCanAttackCriminals(performer);
					boolean actionTargetIsCriminal = actionTargetIsCriminal(actionTarget, world);
					boolean sheriffAttacksCriminal = performerCanAttackCriminals && performerAttacked(managedOperation) && actionTargetIsCriminal;
					boolean selfDefenseAgainstCriminal = performerAttacked(managedOperation) && actionTargetIsCriminal;
					if (sheriffAttacksCriminal || selfDefenseAgainstCriminal) {
						violatedGroupRules = false;
					}
				}
			}
			
			return violatedGroupRules;
		} else {
			return false;
		}
	}

	static boolean performerCanAttackCriminals(WorldObject performer) {
		Boolean performerCanAttackCriminals = performer.getProperty(Constants.CAN_ATTACK_CRIMINALS);
		return performerCanAttackCriminals != null && performerCanAttackCriminals.booleanValue();
	}

	static boolean actionTargetIsCriminal(WorldObject actionTarget, World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		boolean actionTargetIsCriminal = !actionTarget.getProperty(Constants.GROUP).contains(villagersOrganization);
		return actionTargetIsCriminal;
	}

	static void logToBackground(Goal obstructedGoal, WorldObject target, WorldObject actionTarget, ManagedOperation managedOperation, int[] args, WorldObject performerFacade, World world) {
		if (world.exists(target) && world.exists(performerFacade) && actionTarget.hasProperty(Constants.ID)) {
			if (target.hasProperty(Constants.BACKGROUND)) {
				target.getProperty(Constants.BACKGROUND).addGoalObstructed(obstructedGoal, performerFacade, actionTarget, managedOperation, args, world);
			}
		}
	}

	static boolean isLegal(WorldObject performer, WorldObject target, ManagedOperation managedOperation, int[] args, World world) {
		if (!isLegallyFighting(performer, target, managedOperation)) {
			if (hasAnyoneSeenAction(performer, target, managedOperation, args, world)) {
				if (performerViolatedGroupRules(performer, target, args, managedOperation, world)) {
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public void checkLegality(WorldObject performer, WorldObject target, ManagedOperation managedOperation, int[] args, World world) {
		if (!isLegal(performer, target, managedOperation, args, world)) {
			WorldObject performerFacade = FacadeUtils.createFacade(performer, performer, target, world);
			//WorldObject targetFacade = FacadeUtils.createFacade(target, performer, target, world);
			
			if (performerAttacked(managedOperation)) {
				throwOutOfGroup(performer, target, target, args, managedOperation, world, performerFacade);
			}
			int bounty = calculateBounty(managedOperation);
			GroupPropertyUtils.getVillagersOrganization(world).getProperty(Constants.BOUNTY).incrementValue(performerFacade, bounty);
		}
	}

	public static boolean isLegallyFighting(WorldObject performer, WorldObject target, ManagedOperation managedOperation) {
		return areBrawling(performer, target, managedOperation) || areFightingInArena(performer, target, managedOperation);
	}

	public static List<String> getThrownOutOfGroupEvents() {
		return THROWN_OUT_OF_GROUP_EVENTS;
	}
}
