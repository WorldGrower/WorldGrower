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
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;

public class GatherFoodGoal implements Goal {

	private final int foodQuantityGoalMet;
	
	public GatherFoodGoal() {
		this(10);
	}

	public GatherFoodGoal(int foodQuantityGoalMet) {
		this.foodQuantityGoalMet = foodQuantityGoalMet;
	}

	public GatherFoodGoal(List<Goal> allGoals) {
		this();
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObject target = GoalUtils.findNearestTarget(performer, Actions.HARVEST_FOOD_ACTION, world);
		OperationInfo butcherOperationInfo = createButcherOperationInfo(performer, world);
		if (target != null && Reach.distance(performer, target) < 15) {
			return new OperationInfo(performer, target, Args.EMPTY, Actions.HARVEST_FOOD_ACTION);
		} else if (butcherOperationInfo != null) {
			return butcherOperationInfo;
		} else {
			return null;
		}
	}
	
	private static OperationInfo createButcherOperationInfo(WorldObject performer, World world) {
		WorldObject target = GoalUtils.findNearestTarget(performer, Actions.BUTCHER_ACTION, world);
		if (target != null && Reach.distance(performer, target) < 15) {
			return new OperationInfo(performer, target, Args.EMPTY, Actions.BUTCHER_ACTION);
		} else {
			return null;
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		return performerInventory.getQuantityFor(Constants.FOOD) > foodQuantityGoalMet;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		return performerInventory.getQuantityFor(Constants.FOOD) > 2;
	}

	@Override
	public String getDescription() {
		return "harvesting food";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		return performerInventory.getQuantityFor(Constants.FOOD);
	}
}
