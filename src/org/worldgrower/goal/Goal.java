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
package org.worldgrower.goal;

import java.io.Serializable;

import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

/**
 * A Goal describes something a non-player character wants to achieve.
 * This class is responsible for calculating the next ManagedOperation to perform, if the Goal is achieved or not and if the Goal is getting closer or not.
 */
public interface Goal extends Serializable {
	public OperationInfo calculateGoal(WorldObject performer, World world);
	public boolean isGoalMet(WorldObject performer, World world);
	public boolean isUrgentGoalMet(WorldObject performer, World world);
	public String getDescription();
	
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet);
	public int evaluate(WorldObject performer, World world);
}
