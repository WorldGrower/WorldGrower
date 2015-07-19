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
import java.util.List;

import org.worldgrower.goal.Goal;

public class GoalCalculator implements Serializable {

	public GoalAndOperationInfo calculateGoal(WorldObject performer, World world, List<Goal> triedGoals) {
		List<Goal> prioritizedGoals = performer.getPriorities(world);
		
		for (Goal prioritizedGoal : prioritizedGoals) {
			if (prioritizedGoal.isGoalMet(performer, world)) {
				prioritizedGoal.goalMetOrNot(performer, world, true);
			} else {
				prioritizedGoal.goalMetOrNot(performer, world, false);
				OperationInfo goal = prioritizedGoal.calculateGoal(performer, world);
				if ((goal != null) && (!triedGoals.contains(prioritizedGoal))) {
					return new GoalAndOperationInfo(prioritizedGoal, goal);
				}
			}
		}

		throw new IllegalStateException("No goal could be calculated for " + performer);
	}
	
	public boolean moreUrgentImportantGoalIsNotMet(WorldObject performer, World world, Goal currentGoal) {
		List<Goal> prioritizedGoals = performer.getPriorities(world);
		
		for (Goal prioritizedGoal : prioritizedGoals) {
			if (prioritizedGoal == currentGoal) {
				return false;
			} else if (!prioritizedGoal.isUrgentGoalMet(performer, world)) {
				if (prioritizedGoal.calculateGoal(performer, world) != null) {
					//System.out.println("performer " + performer.getProperty(Constants.NAME) + " with current goal " + currentGoal + " has unmet goal in " + prioritizedGoal);
					return true;
				}
			}
		}
		
		throw new IllegalStateException("Current goal " + currentGoal + " is not a possible goal for  " + performer);
	}
}
