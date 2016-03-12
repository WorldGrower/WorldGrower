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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.worldgrower.actions.Actions;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;

/**
 * MetaInformation holds all information of a non-player character regarding goals, actions, etc.
 * It holds all the state regarding the decisions a non-player character makes.
 */
class MetaInformation implements Serializable {
	private LinkedList<OperationInfo> currentTask = new LinkedList<OperationInfo>();
	private GoalChangedReason goalChangedReason;
	private Goal finalGoal;
	private final WorldObject worldObject;
	
	private final List<GoalChangedListener> goalChangedListeners = new ArrayList<>();
	
	public MetaInformation(WorldObject worldObject) {
		this.worldObject = worldObject;
	}

	public Queue<OperationInfo> getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(List<OperationInfo> task, GoalChangedReason goalChangedReason) {
		if (task.size() == 0) {
			throw new IllegalStateException("task cannot be empty for WorldObject " + worldObject);
		}
		currentTask.clear();
		currentTask.addAll(task);
		
		this.goalChangedReason = goalChangedReason;
	}

	public boolean isEmpty() {
		return currentTask.isEmpty();
	}
	
	public OperationInfo getImmediateGoal() {
		return currentTask.getLast();
	}

	public Goal getFinalGoal() {
		return finalGoal;
	}

	public void setFinalGoal(Goal finalGoal) {
		for(GoalChangedListener goalChangedListener : goalChangedListeners) {
			goalChangedListener.goalChanged(worldObject, this.finalGoal, finalGoal);
		}
		
		this.finalGoal = finalGoal;
	}
	
	@Override
	public String toString() {
		StringBuilder currentTasksBuilder = new StringBuilder();
		if (currentTask.size() > 0) {
			for(OperationInfo operationInfo : currentTask) {
				currentTasksBuilder.append(operationInfo.toShortString()).append(" | ");
			}
		} else {
			currentTasksBuilder.append("null");
		}
		
		
		return "finalGoal = " + finalGoal.getClass().getSimpleName() + ", currentTask = " + currentTasksBuilder.toString() + ", goalChangedReason = " + goalChangedReason;
	}

	public void setNoActionPossible() {
		currentTask.clear();
		currentTask.add(new OperationInfo(worldObject, worldObject, new int[0], Actions.DO_NOTHING_ACTION));
		goalChangedReason = GoalChangedReason.NO_ACTION_POSSIBLE;
		finalGoal = Goals.IDLE_GOAL;
	}
	
	public void addGoalChangedListeners(GoalChangedListener goalChangedListener) {
		goalChangedListeners.add(goalChangedListener);
	}
}