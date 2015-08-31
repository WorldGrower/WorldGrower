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
import java.util.Map;

import org.worldgrower.actions.Actions;
import org.worldgrower.condition.Condition;
import org.worldgrower.goal.FacadeUtils;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.LegalActionsPropertyUtils;

public class DefaultGoalObstructedHandler implements GoalObstructedHandler {

	@Override
	public void goalHindered(WorldObject performer, WorldObject target, int stepsUntilLastGoal, int goalEvaluationDecrease, WorldObject actionTarget, ManagedOperation managedOperation, int[] args, World world) {
		if (performer.hasProperty(Constants.RELATIONSHIPS) && target.hasProperty(Constants.RELATIONSHIPS)) {
			if (hasAnyoneSeenAction(performer, actionTarget, managedOperation, args, world)) {
				int value = -100 * stepsUntilLastGoal;
				
				WorldObject performerFacade = FacadeUtils.createFacade(performer, performer, target);
				WorldObject targetFacade = FacadeUtils.createFacade(target, performer, target);
				
				logToBackground(target, actionTarget, managedOperation, args, performerFacade, world);
				
				alterRelationships(performer, target, actionTarget, managedOperation, world, value, performerFacade, targetFacade);
			}
		}
	}
	
	private boolean hasAnyoneSeenAction(WorldObject performer, WorldObject actionTarget, ManagedOperation managedOperation, int[] args, World world) {
		if (performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.INVISIBLE_CONDITION)) {
			return false;
		} else {
			List<WorldObject> targets = world.findWorldObjects(w -> (Reach.distance(performer, w) < 6 || Reach.distance(actionTarget, w) < 6) && w.hasIntelligence() && !w.equals(performer));
			return targets.size() > 0;
		}
	}

	private void alterRelationships(WorldObject performer, WorldObject target, WorldObject actionTarget, ManagedOperation managedOperation, World world, int value, WorldObject performerFacade, WorldObject targetFacade) {
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(targetFacade.getProperty(Constants.ID), value);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performerFacade.getProperty(Constants.ID), value);
		
		if (performerViolatedGroupRules(performer, actionTarget, managedOperation, world)) {
			GroupPropertyUtils.throwPerformerOutGroup(performerFacade, target);
			
			WorldObject realPerformer = world.findWorldObject(Constants.ID, performerFacade.getProperty(Constants.ID));
			GroupPropertyUtils.throwPerformerOutGroup(realPerformer, target);
		}
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

	private boolean performerViolatedGroupRules(WorldObject performer,WorldObject actionTarget, ManagedOperation managedOperation, World world) {
		Map<ManagedOperation, Boolean> legalActions = LegalActionsPropertyUtils.getLegalActions(world);
		Boolean isLegal = legalActions.get(managedOperation);
		if (isLegal != null) {
			boolean violatedGroupRules = !isLegal.booleanValue();
			if (violatedGroupRules) {
				boolean performerCanAttackCriminals = performerCanAttackCriminals(performer);
				boolean actionTargetIsCriminal = actionTargetIsCriminal(actionTarget, world);
				if (performerCanAttackCriminals
						&& performerAttacked(managedOperation)
						&& actionTargetIsCriminal) {
					violatedGroupRules = false;
				}
			}
			
			return violatedGroupRules;
		} else {
			return false;
		}
	}

	private boolean performerCanAttackCriminals(WorldObject performer) {
		Boolean performerCanAttackCriminals = performer.getProperty(Constants.CAN_ATTACK_CRIMINALS);
		return performerCanAttackCriminals != null && performerCanAttackCriminals.booleanValue();
	}

	private boolean actionTargetIsCriminal(WorldObject actionTarget, World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		boolean actionTargetIsCriminal = actionTarget.getProperty(Constants.GROUP).contains(villagersOrganization);
		return actionTargetIsCriminal;
	}

	private void logToBackground(WorldObject target, WorldObject actionTarget, ManagedOperation managedOperation, int[] args, WorldObject performerFacade, World world) {
		if (target.hasProperty(Constants.BACKGROUND)) {
			target.getProperty(Constants.BACKGROUND).addGoalObstructed(performerFacade, actionTarget, managedOperation, args, world);
		}
	}
}
