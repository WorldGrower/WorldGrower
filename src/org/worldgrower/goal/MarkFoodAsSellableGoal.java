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

public class MarkFoodAsSellableGoal implements Goal {
	
	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		int indexOfFood = indexOfFood(performer);
		return new OperationInfo(performer, performer, new int[] { indexOfFood }, Actions.MARK_INVENTORY_ITEM_AS_SELLABLE_ACTION);
	}
	
	@Override
	public final boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}
	
	@Override
	public final void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	private WorldObject getFoodWorldObjectFromInventory(WorldObject performer) {
		int indexOfFood = indexOfFood(performer);
		if (indexOfFood != -1) {
			return performer.getProperty(Constants.INVENTORY).get(indexOfFood);
		} else {
			return null;
		}
	}

	private int indexOfFood(WorldObject performer) {
		int indexOfFood = performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.FOOD);
		return indexOfFood;
	}
	
	@Override
	public final boolean isGoalMet(WorldObject performer, World world) {
		WorldObject worldObject = getFoodWorldObjectFromInventory(performer);
		if (worldObject != null) {
			return worldObject.hasProperty(Constants.SELLABLE) && worldObject.getProperty(Constants.SELLABLE);
		} else {
			return true;
		}
	}

	@Override
	public String getDescription() {
		return "marking food as sellable";
	}
	
	@Override
	public final int evaluate(WorldObject performer, World world) {
		return 0;
	}


}
