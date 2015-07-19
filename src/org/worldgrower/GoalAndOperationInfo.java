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

import org.worldgrower.goal.Goal;

public class GoalAndOperationInfo {

	private final Goal goal;
	private final OperationInfo operationInfo;
	
	public GoalAndOperationInfo(Goal goal, OperationInfo operationInfo) {
		this.goal = goal;
		this.operationInfo = operationInfo;
	}

	public Goal getGoal() {
		return goal;
	}

	public OperationInfo getOperationInfo() {
		return operationInfo;
	}
}
