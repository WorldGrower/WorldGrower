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

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;

public class RestGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (performer.hasProperty(Constants.HOUSES) && performer.getProperty(Constants.HOUSES) != null && performer.getProperty(Constants.HOUSES).size() > 0) {
			WorldObject bestHouse = HousePropertyUtils.getBestHouse(performer, world);
			return new OperationInfo(performer, bestHouse, new int[0], Actions.SLEEP_ACTION);
		} else {
			return new OperationInfo(performer, performer, new int[0], Actions.REST_ACTION);
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return (performer.getProperty(Constants.ENERGY) > 800);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return (performer.getProperty(Constants.ENERGY) > 200);
	}

	@Override
	public String getDescription() {
		return "resting";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.ENERGY);
	}
}
