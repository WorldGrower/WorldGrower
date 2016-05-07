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

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.BuildBreweryAction;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.generator.BuildingGenerator;

public class BreweryGoal implements Goal {

	public BreweryGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> unownedBreweries = GoalUtils.findNearestTargetsByProperty(performer, Actions.CLAIM_BREWERY_ACTION, Constants.BREWERY_QUALITY, w -> BuildingGenerator.isBrewery(w) && Actions.CLAIM_BREWERY_ACTION.distance(performer, w, Args.EMPTY, world) == 0, world);
		if (unownedBreweries.size() > 0) {
			return new OperationInfo(performer, unownedBreweries.get(0), Args.EMPTY, Actions.CLAIM_BREWERY_ACTION);
		} else if (!BuildBreweryAction.hasEnoughStone(performer)) {
			return Goals.STONE_GOAL.calculateGoal(performer, world);
		} else if (!BuildBreweryAction.hasEnoughWood(performer)) {
			return Goals.WOOD_GOAL.calculateGoal(performer, world);
		} else {
			WorldObject target = BuildLocationUtils.findOpenLocationNearExistingProperty(performer, 4, 3, world);
			if (target != null) {
				return new OperationInfo(performer, target, Args.EMPTY, Actions.BUILD_BREWERY_ACTION);
			} else {
				return null;
			}
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		List<Integer> breweryIds = performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.BREWERY);
		if (breweryIds.size() > 0) {
			Integer breweryId = breweryIds.get(0);
			if (breweryId != null) {
				WorldObject brewery = world.findWorldObject(Constants.ID, breweryId.intValue());
				return (brewery.getProperty(Constants.BREWERY_QUALITY) > 0);
			}
		}
		return false;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "building a brewery";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.BREWERY).size();
	}
}