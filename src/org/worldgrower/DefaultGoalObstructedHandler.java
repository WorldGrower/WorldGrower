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

import java.util.Arrays;
import java.util.List;

import org.worldgrower.actions.Actions;
import org.worldgrower.actions.legal.LegalActions;
import org.worldgrower.attribute.IdList;
import org.worldgrower.condition.Condition;
import org.worldgrower.goal.ArenaPropertyUtils;
import org.worldgrower.goal.BrawlPropertyUtils;
import org.worldgrower.goal.FacadeUtils;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.LegalActionsPropertyUtils;

public class DefaultGoalObstructedHandler implements GoalObstructedHandler {

	@Override
	public void goalHindered(WorldObject performer, WorldObject target, int stepsUntilLastGoal, int goalEvaluationDecrease, WorldObject actionTarget, ManagedOperation managedOperation, int[] args, World world) {
		if (performer.hasProperty(Constants.RELATIONSHIPS) && target.hasProperty(Constants.RELATIONSHIPS)) {
			if (hasAnyoneSeenAction(performer, actionTarget, managedOperation, args, world)) {
				int value = -100 * stepsUntilLastGoal;
				
				WorldObject performerFacade = FacadeUtils.createFacade(performer, performer, target, world);
				WorldObject targetFacade = FacadeUtils.createFacade(target, performer, target, world);
				
				logToBackground(target, actionTarget, managedOperation, args, performerFacade, world);
				
				alterRelationships(performer, target, actionTarget, args, managedOperation, world, value, performerFacade, targetFacade);
			}
		}
	}
	
	static boolean hasAnyoneSeenAction(WorldObject performer, WorldObject actionTarget, ManagedOperation managedOperation, int[] args, World world) {
		if (performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.INVISIBLE_CONDITION)) {
			return false;
		} else {
			List<WorldObject> targets = world.findWorldObjects(w -> (Reach.distance(performer, w) < 6 || Reach.distance(actionTarget, w) < 6) && w.hasIntelligence() && !w.equals(performer));
			return targets.size() > 0;
		}
	}

	static void alterRelationships(WorldObject performer, WorldObject target, WorldObject actionTarget, int[] args, ManagedOperation managedOperation, World world, int value, WorldObject performerFacade, WorldObject targetFacade) {
		if (!areBrawling(performer, actionTarget, managedOperation) && !areFightingInArena(performer, actionTarget, managedOperation)) {
			if (world.exists(performer) && world.exists(target) && world.exists(performerFacade) && world.exists(targetFacade)) {
				performer.getProperty(Constants.RELATIONSHIPS).incrementValue(targetFacade.getProperty(Constants.ID), value);
				target.getProperty(Constants.RELATIONSHIPS).incrementValue(performerFacade.getProperty(Constants.ID), value);
			}
			
			if (performerViolatedGroupRules(performer, actionTarget, args, managedOperation, world)) {
				IdList oldGroup = performer.getProperty(Constants.GROUP).copy();
				GroupPropertyUtils.throwPerformerOutGroup(performerFacade, target);
				
				WorldObject realPerformer = world.findWorldObject(Constants.ID, performerFacade.getProperty(Constants.ID));
				GroupPropertyUtils.throwPerformerOutGroup(realPerformer, target);
				
				IdList newGroup = performer.getProperty(Constants.GROUP).copy();
				world.getWorldStateChangedListeners().thrownOutOfGroup(performer, actionTarget, args, managedOperation, oldGroup, newGroup);
			}
		}
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
				|| (actionClass == Actions.INFLICT_WOUNDS_ACTION.getClass())
				);
	}
	
	public static List<ManagedOperation> getNonAttackingIllegalActions() {
		return Arrays.asList(Actions.STEAL_ACTION);
	}

	static boolean performerViolatedGroupRules(WorldObject performer, WorldObject actionTarget, int[] args, ManagedOperation managedOperation, World world) {
		LegalActions legalActions = LegalActionsPropertyUtils.getLegalActions(world);
		Boolean isLegal = legalActions.isLegalAction(performer, actionTarget, args, managedOperation);
		if (isLegal != null) {
			boolean violatedGroupRules = !isLegal.booleanValue();
			if (violatedGroupRules) {
				boolean performerCanAttackCriminals = performerCanAttackCriminals(performer);
				boolean actionTargetIsCriminal = actionTargetIsCriminal(actionTarget, world);
				boolean sheriffAttacksCriminal = performerCanAttackCriminals && performerAttacked(managedOperation) && actionTargetIsCriminal;
				boolean selfDefenseAgainstCriminal = performerAttacked(managedOperation) && actionTargetIsCriminal;
				if (sheriffAttacksCriminal || selfDefenseAgainstCriminal) {
					violatedGroupRules = false;
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

	static void logToBackground(WorldObject target, WorldObject actionTarget, ManagedOperation managedOperation, int[] args, WorldObject performerFacade, World world) {
		if (world.exists(target) && world.exists(performerFacade)) {
			if (target.hasProperty(Constants.BACKGROUND)) {
				target.getProperty(Constants.BACKGROUND).addGoalObstructed(performerFacade, actionTarget, managedOperation, args, world);
			}
		}
	}
}
