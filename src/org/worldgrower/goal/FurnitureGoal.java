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
import org.worldgrower.actions.ConstructBedAction;
import org.worldgrower.generator.Item;

public class FurnitureGoal implements Goal {

	private static final int QUANTITY_TO_BUY = 1;
	
	public FurnitureGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		boolean hasInventoryFurniture = performer.getProperty(Constants.INVENTORY).getWorldObjects(Constants.NAME, Item.BED_NAME).size() > 0;
		List<WorldObject> targets = BuySellUtils.findBuyTargets(performer, Constants.SLEEP_COMFORT, QUANTITY_TO_BUY, world);
		if (hasInventoryFurniture) {
			int indexOfFurniture = performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.SLEEP_COMFORT);
			WorldObject target = HousePropertyUtils.getBestHouse(performer, world);
			if (target != null) {
				OperationInfo avoidTrappedContainer = ContainerUtils.avoidTrappedContainer(performer, target, world);
				if (avoidTrappedContainer != null) {
					return avoidTrappedContainer;
				}
				return new OperationInfo(performer, target, new int[] { indexOfFurniture }, Actions.PUT_ITEM_INTO_INVENTORY_ACTION);
			} else {
				return Goals.HOUSE_GOAL.calculateGoal(performer, world);
			}
		} else if (targets.size() > 0) {
			return BuySellUtils.create(performer, targets.get(0), Item.BED, QUANTITY_TO_BUY);
		} else if (ConstructBedAction.hasEnoughWood(performer)) {
			Integer workbenchId = performer.getProperty(Constants.WORKBENCH_ID);
			if (workbenchId == null) {
				return Goals.WORKBENCH_GOAL.calculateGoal(performer, world);
			} else {
				WorldObject workbench = world.findWorldObject(Constants.ID, workbenchId);
				return new OperationInfo(performer, workbench, Args.EMPTY, Actions.CONSTRUCT_BED_ACTION);
			}
		} else {
			return Goals.WOOD_GOAL.calculateGoal(performer, world);
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
	public String getDescription() {
		return "looking for furniture";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		boolean hasInventoryFurniture = performer.getProperty(Constants.INVENTORY).getWorldObjects(Constants.NAME, Item.BED_NAME).size() > 0;
		boolean hasHouseWithBed = HousePropertyUtils.hasHouseWithBed(performer, world);
		
		int inventoryFurnitureEvaluation = hasInventoryFurniture ? 1 : 0;
		int houseWithBed = hasHouseWithBed ? 2 : 0;
		
		return inventoryFurnitureEvaluation + houseWithBed;
		
	}
}
