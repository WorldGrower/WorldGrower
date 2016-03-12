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

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.worldgrower.actions.Actions;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;

/**
 * default implementation if a goal changes:
 * if the npc wants to hide the goal, a better goal is added to the facade.
 */
class DefaultGoalChangedListener implements GoalChangedListener {

	private Map<Goal, GoalChangedHandler> goalChangedMap = new HashMap<>();
	
	public DefaultGoalChangedListener() {
		goalChangedMap.put(Goals.STEAL_GOAL, new GoalChangedHandler(Goals.FOOD_GOAL, Actions.EAT_FROM_INVENTORY_ACTION));
	}
	
	@Override
	public void goalChanged(WorldObject performer, Goal oldGoal, Goal newGoal) {
		if (performer.hasProperty(Constants.FACADE)) {
			WorldObject facade = performer.getProperty(Constants.FACADE);
			if (facade != null) {
				MetaInformation metaInformation = facade.getProperty(Constants.META_INFORMATION);
				if (metaInformation == null) {
					metaInformation = new MetaInformation(performer);
					facade.setProperty(Constants.META_INFORMATION, metaInformation);
				}
				
				GoalChangedHandler goalChangedHandler = goalChangedMap.get(newGoal);
				if (goalChangedHandler != null) {
					goalChangedHandler.setGoalAndTasks(performer, metaInformation);
				}
			}
		}
	}
	
	private static class GoalChangedHandler implements Serializable {
		
		private final Goal goal;
		private final ManagedOperation action;
		
		public GoalChangedHandler(Goal goal, ManagedOperation action) {
			this.goal = goal;
			this.action = action;
		}

		public void setGoalAndTasks(WorldObject performer, MetaInformation metaInformation) {
			metaInformation.setFinalGoal(goal);
			metaInformation.setCurrentTask(Arrays.asList(new OperationInfo(performer, performer, new int[0], action)), GoalChangedReason.EMPTY_META_INFORMATION);
		}
	}
}
