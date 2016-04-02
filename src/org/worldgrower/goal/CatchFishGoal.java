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

public class CatchFishGoal implements Goal {

	public CatchFishGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObject leftHandEquipment = performer.getProperty(Constants.LEFT_HAND_EQUIPMENT);
		boolean leftHandContainsFishingPole = leftHandEquipment != null ? leftHandEquipment.hasProperty(Constants.FISHING_POLE_QUALITY) : false;
		if (leftHandContainsFishingPole) {
			WorldObject target = GoalUtils.findNearestTarget(performer, Actions.CATCH_FISH_ACTION, world);
			if (target != null) {
				return new OperationInfo(performer, target, Args.EMPTY, Actions.CATCH_FISH_ACTION);
			}
		} else {
			if (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FISHING_POLE_QUALITY) > 0) {
				int indexOfFishingPole = performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.FISHING_POLE_QUALITY);
				return new OperationInfo(performer, performer, new int[] { indexOfFishingPole }, Actions.EQUIP_INVENTORY_ITEM_ACTION);
			} else {
				return Goals.FISHING_POLE_GOAL.calculateGoal(performer, world);
			}
		}
		
		return null;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FOOD) > 30;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking to gather fish";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FOOD);
	}
}
