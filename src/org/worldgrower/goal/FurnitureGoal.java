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
import org.worldgrower.generator.Item;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class FurnitureGoal implements Goal {

	public FurnitureGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		boolean hasInventoryFurniture = performer.getProperty(Constants.INVENTORY).getWorldObjects(Constants.ITEM_ID, Item.BED).size() > 0;
		if (hasInventoryFurniture) {
			int indexOfFurniture = performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.SLEEP_COMFORT);
			WorldObject target = HousePropertyUtils.getBestHouse(performer, world);
			if (target != null) {
				OperationInfo avoidTrappedContainer = ContainerUtils.avoidTrappedContainer(performer, target, world);
				if (avoidTrappedContainer != null) {
					return avoidTrappedContainer;
				}
				if (LockUtils.performerCanAccessContainer(performer, target)) {
					return new OperationInfo(performer, target, new int[] { indexOfFurniture }, Actions.PUT_ITEM_INTO_INVENTORY_ACTION);
				} else {
					//TODO: how to handle own house is locked?
					return null;
				}
			} else {
				return Goals.HOUSE_GOAL.calculateGoal(performer, world);
			}
		} else {
			return Goals.BED_GOAL.calculateGoal(performer, world);
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
		defaultGoalMetOrNot(performer, world, goalMet, Constants.SLEEP_COMFORT);
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return HousePropertyUtils.hasHouseWithBed(performer, world);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_FURNITURE);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		boolean hasInventoryFurniture = performer.getProperty(Constants.INVENTORY).getWorldObjects(Constants.ITEM_ID, Item.BED).size() > 0;
		boolean hasHouseWithBed = HousePropertyUtils.hasHouseWithBed(performer, world);
		
		int inventoryFurnitureEvaluation = hasInventoryFurniture ? 1 : 0;
		int houseWithBed = hasHouseWithBed ? 2 : 0;
		
		return inventoryFurnitureEvaluation + houseWithBed;
		
	}
}
