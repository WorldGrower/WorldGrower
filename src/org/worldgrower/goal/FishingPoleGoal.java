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
import org.worldgrower.actions.ConstructFishingPoleAction;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;

public class FishingPoleGoal implements Goal {

	private static final int QUANTITY_TO_BUY = 1;
	
	public FishingPoleGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> targets = BuySellUtils.findBuyTargets(performer, Constants.FISHING_POLE_QUALITY, QUANTITY_TO_BUY, world);
		if (targets.size() > 0) {
			return BuySellUtils.create(performer, targets.get(0), Item.FISHING_POLE, QUANTITY_TO_BUY);
		} else {
			Integer workbenchId = BuildingGenerator.getWorkbenchId(performer);
			if (workbenchId == null) {
				return Goals.WORKBENCH_GOAL.calculateGoal(performer, world);
			} else {
				if (ConstructFishingPoleAction.hasEnoughWood(performer)) {
					WorldObject workbench = world.findWorldObject(Constants.ID, workbenchId);
					return new OperationInfo(performer, workbench, Args.EMPTY, Actions.CONSTRUCT_FISHING_POLE_ACTION);
				} else {
					return Goals.WOOD_GOAL.calculateGoal(performer, world);
				}
			}
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
		defaultGoalMetOrNot(performer, world, goalMet, Constants.FISHING_POLE_QUALITY);
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FISHING_POLE_QUALITY) > 0;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking for a fishing pole";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FISHING_POLE_QUALITY);
	}
}
