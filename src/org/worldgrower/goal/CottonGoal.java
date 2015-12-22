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

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;

public class CottonGoal implements Goal {

	public CottonGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObject target = GoalUtils.findNearestTarget(performer, Actions.HARVEST_COTTON_ACTION, world);
		List<WorldObject> targets = BuySellUtils.findBuyTargets(performer, Constants.COTTON, world);
		if (targets.size() > 0) {
			return new OperationInfo(performer, targets.get(0), new int[] { targets.get(0).getProperty(Constants.INVENTORY).getIndexFor(Constants.COTTON), 5 }, Actions.BUY_ACTION);
		} else if (target != null) {
			return new OperationInfo(performer, target, new int[0], Actions.HARVEST_COTTON_ACTION);
		} else {
			WorldObject buildLocation = BuildLocationUtils.findOpenLocationNearExistingProperty(performer, 2, 3, world);
			
			if (buildLocation != null) {
				return new OperationInfo(performer, buildLocation, new int[0], Actions.PLANT_COTTON_PLANT_ACTION);
			} else {
				return null;
			}
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
		defaultGoalMetOrNot(performer, world, goalMet, Constants.COTTON);
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.COTTON) > 5;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking for cotton";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.COTTON);
	}
}
