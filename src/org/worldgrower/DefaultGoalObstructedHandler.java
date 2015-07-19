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

import org.worldgrower.goal.FacadeUtils;
import org.worldgrower.goal.GroupPropertyUtils;


public class DefaultGoalObstructedHandler implements GoalObstructedHandler {

	@Override
	public void goalHindered(WorldObject performer, WorldObject target, int stepsUntilLastGoal, int goalEvaluationDecrease, WorldObject actionTarget, ManagedOperation managedOperation, int[] args, World world) {
		if (performer.hasProperty(Constants.RELATIONSHIPS) && target.hasProperty(Constants.RELATIONSHIPS)) {
			int value = -100 * stepsUntilLastGoal;
			
			WorldObject performerFacade = FacadeUtils.createFacade(performer, performer, target);
			WorldObject targetFacade = FacadeUtils.createFacade(target, performer, target);
			
			logToBackground(target, actionTarget, managedOperation, args, performerFacade, world);
			
			alterRelationships(performer, target, stepsUntilLastGoal, world, value, performerFacade, targetFacade);
		}
	}

	private void alterRelationships(WorldObject performer, WorldObject target, int stepsUntilLastGoal, World world, int value, WorldObject performerFacade, WorldObject targetFacade) {
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(targetFacade.getProperty(Constants.ID), value);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performerFacade.getProperty(Constants.ID), value);
		
		if (stepsUntilLastGoal > 10) {
			GroupPropertyUtils.throwPerformerOutGroup(performerFacade, target);
			
			WorldObject realPerformer = world.findWorldObject(Constants.ID, performerFacade.getProperty(Constants.ID));
			GroupPropertyUtils.throwPerformerOutGroup(realPerformer, target);
		}
	}

	private void logToBackground(WorldObject target, WorldObject actionTarget, ManagedOperation managedOperation, int[] args, WorldObject performerFacade, World world) {
		if (target.hasProperty(Constants.BACKGROUND)) {
			target.getProperty(Constants.BACKGROUND).addGoalObstructed(performerFacade, actionTarget, managedOperation, args, world);
		}
	}
}
