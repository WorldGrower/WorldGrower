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
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;

public class CreateFoodSourcesGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		
		WorldObject target = BuildLocationUtils.findOpenLocationNearExistingProperty(performer, 2, 2, world);

		if (target != null) {
			return new OperationInfo(performer, target, new int[0], Actions.PLANT_BERRY_BUSH_ACTION);
		} else {
			return null;
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		List<WorldObject> targets = GoalUtils.findNearestTargets(
				performer, 
				Actions.HARVEST_FOOD_ACTION, 
				w -> (w.getProperty(Constants.FOOD_SOURCE) > 100) && (Reach.distance(performer, w) < 20), 
				world);
		return targets.size() > 0;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "planting foods";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		List<WorldObject> targets = GoalUtils.findNearestTargets(
				performer, 
				Actions.HARVEST_FOOD_ACTION, 
				w -> (Reach.distance(performer, w) < 20), 
				world);
		int eval = 0;
		for(WorldObject target : targets) {
			eval += target.getProperty(Constants.FOOD_SOURCE);
		}
		return eval;
	}
}