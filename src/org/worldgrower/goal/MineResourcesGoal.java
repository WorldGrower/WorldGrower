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
import org.worldgrower.WorldObjectContainer;
import org.worldgrower.actions.Actions;

public class MineResourcesGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		
		if (inventory.getQuantityFor(Constants.STONE) < 5) {
			WorldObject target = GoalUtils.findNearestTarget(performer, Actions.MINE_STONE_ACTION, world);
			if (target != null) {
				return new OperationInfo(performer, target, new int[0], Actions.MINE_STONE_ACTION);
			}
		}
		if (inventory.getQuantityFor(Constants.ORE) < 5) {
			WorldObject target = GoalUtils.findNearestTarget(performer, Actions.MINE_ORE_ACTION, world);
			if (target != null) {
				return new OperationInfo(performer, target, new int[0], Actions.MINE_ORE_ACTION);
			}
		}
		if (inventory.getQuantityFor(Constants.GOLD) < 5) {
			WorldObject target = GoalUtils.findNearestTarget(performer, Actions.MINE_GOLD_ACTION, world);
			if (target != null) {
				return new OperationInfo(performer, target, new int[0], Actions.MINE_GOLD_ACTION);
			}
		}
		return null;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		return (inventory.getQuantityFor(Constants.STONE) >= 5)
				&& (inventory.getQuantityFor(Constants.ORE) >= 5) 
				&& (inventory.getQuantityFor(Constants.GOLD) >= 5);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking for resources like stone, ore and gold";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		return inventory.getQuantityFor(Constants.STONE) 
				+ inventory.getQuantityFor(Constants.ORE) 
				+ inventory.getQuantityFor(Constants.GOLD);
	}
}
