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

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObject target = GoalUtils.findNearestTarget(performer, Actions.HARVEST_FOOD_ACTION, world);
		if (target != null && Reach.distance(performer, target) < 15) {
			return new OperationInfo(performer, target, new int[0], Actions.HARVEST_FOOD_ACTION);
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
		return performerInventory.getQuantityFor(Constants.FOOD_SOURCE) > foodQuantityGoalMet;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		return performerInventory.getQuantityFor(Constants.FOOD_SOURCE) > 2;
	}

	@Override
	public String getDescription() {
		return "harvesting food";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		return performerInventory.getQuantityFor(Constants.FOOD_SOURCE);
	}
}
