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
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class FurnitureGoal implements Goal {

	public FurnitureGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		int indexOfBed = performerInventory.getIndexFor(Constants.SLEEP_COMFORT);
		int indexOfKitchen = performerInventory.getIndexFor(Constants.COOKING_QUALITY);
		int indexOfFurniture = Math.max(indexOfBed, indexOfKitchen);
		
		WorldObject house = HousePropertyUtils.getBestHouse(performer, world);
		if (house == null) {
			return Goals.HOUSE_GOAL.calculateGoal(performer, world);
		} else if (indexOfFurniture != -1) {
			return putItemIntoHouse(performer, indexOfFurniture, house, world);
		} else if (indexOfBed == -1 && !houseHasFurniture(performer, house, Constants.SLEEP_COMFORT, world)) {
			return Goals.BED_GOAL.calculateGoal(performer, world);
		} else if (indexOfKitchen == -1 && !houseHasFurniture(performer, house, Constants.COOKING_QUALITY, world)) {
			return Goals.KITCHEN_GOAL.calculateGoal(performer, world);
		} else {
			return null;
		}
	}

	private OperationInfo putItemIntoHouse(WorldObject performer, int indexOfFurniture, WorldObject house, World world) {
		OperationInfo avoidTrappedContainer = ContainerUtils.avoidTrappedContainer(performer, house, world);
		if (avoidTrappedContainer != null) {
			return avoidTrappedContainer;
		}
		if (LockUtils.performerCanAccessContainer(performer, house)) {
			return new OperationInfo(performer, house, new int[] { indexOfFurniture }, Actions.PUT_ITEM_INTO_INVENTORY_ACTION);
		} else {
			//TODO: how to handle own house is locked?
			return null;
		}
	}

	private boolean houseHasFurniture(WorldObject performer, WorldObject house, IntProperty property, World world) {
		return house.getProperty(Constants.INVENTORY).getQuantityFor(property) > 0;
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
		
		boolean hasBed = HousePropertyUtils.hasHouseWithBed(performer, world);
		defaultGoalMetOrNot(performer, world, hasBed, Constants.SLEEP_COMFORT);
		
		boolean hasKitchen = HousePropertyUtils.hasHouseWithKitchen(performer, world);
		defaultGoalMetOrNot(performer, world, hasKitchen, Constants.COOKING_QUALITY);
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return HousePropertyUtils.hasHouseWithBed(performer, world)
				&& HousePropertyUtils.hasHouseWithKitchen(performer, world);
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
