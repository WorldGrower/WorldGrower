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
import org.worldgrower.generator.ItemGenerator;

public class FurnitureGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		boolean hasInventoryFurniture = performer.getProperty(Constants.INVENTORY).getWorldObjects(Constants.NAME, ItemGenerator.BED_NAME).size() > 0;
		List<WorldObject> targets = BuySellUtils.findBuyTargets(performer, Constants.SLEEP_COMFORT, world);
		if (hasInventoryFurniture) {
			int indexOfFurniture = performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.SLEEP_COMFORT);
			return new OperationInfo(performer, performer, new int[] { indexOfFurniture }, Actions.PUT_ITEM_INTO_INVENTORY_ACTION);
		} else if (targets.size() > 0) {
			return new OperationInfo(performer, targets.get(0), new int[] { targets.get(0).getProperty(Constants.INVENTORY).getIndexFor(Constants.SLEEP_COMFORT), 1 }, Actions.BUY_ACTION);
		} else {
			return new OperationInfo(performer, performer, new int[0], Actions.CONSTRUCT_BED_ACTION);
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
		if (performer.hasProperty(Constants.DEMANDS)) {
			if (goalMet) {
				performer.getProperty(Constants.DEMANDS).remove(Constants.SLEEP_COMFORT);
			} else {
				performer.getProperty(Constants.DEMANDS).add(Constants.SLEEP_COMFORT, 1);
			}
		}
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
		boolean hasInventoryFurniture = performer.getProperty(Constants.INVENTORY).getWorldObjects(Constants.NAME, ItemGenerator.BED_NAME).size() > 0;
		boolean hasHouseWithBed = HousePropertyUtils.hasHouseWithBed(performer, world);
		
		int inventoryFurnitureEvaluation = hasInventoryFurniture ? 1 : 0;
		int houseWithBed = hasHouseWithBed ? 2 : 0;
		
		return inventoryFurnitureEvaluation + houseWithBed;
		
	}
}
