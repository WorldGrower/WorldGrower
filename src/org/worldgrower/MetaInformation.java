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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.worldgrower.goal.Goal;

class MetaInformation implements Serializable {
	private LinkedList<OperationInfo> currentTask = new LinkedList<OperationInfo>();
	private Goal finalGoal; 
	private final WorldObject worldObject;
	
	public MetaInformation(WorldObject worldObject) {
		this.worldObject = worldObject;
	}

	public Queue<OperationInfo> getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(List<OperationInfo> task) {
		if (task.size() == 0) {
			throw new IllegalStateException("task cannot be empty for WorldObject " + worldObject);
		}
		currentTask.clear();
		currentTask.addAll(task);
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
		this.finalGoal = finalGoal;
	}
	
	@Override
	public String toString() {
		return "finalGoal = " + finalGoal + ", currentTask = " + (currentTask.size() > 0 ? currentTask.getLast().toShortString() : "null");
	}
}