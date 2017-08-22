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
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Condition;

public class WeightPropertyUtils {

	public static int getTotalWeight(WorldObject worldObject) {
		WorldObjectContainer inventory = worldObject.getProperty(Constants.INVENTORY);
		int totalWeight = inventory.getUnmodifiedTotalWeight();
		if (worldObject.getProperty(Constants.CONDITIONS).hasCondition(Condition.BURDENED_CONDITION)) {
			totalWeight *= 2;
		}
		if (worldObject.getProperty(Constants.CONDITIONS).hasCondition(Condition.FEATHERED_CONDITION)) {
			totalWeight /= 2;
		}
		
		return totalWeight;
	}

	public static boolean isCarryingTooMuch(WorldObject worldObject) {
		int totalWeight = getTotalWeight(worldObject);
		int carryingCapacity = getCarryingCapacity(worldObject);
		return totalWeight > carryingCapacity;
	}

	public static int getCarryingCapacity(WorldObject worldObject) {
		int strength = worldObject.getProperty(Constants.STRENGTH);
		int level = worldObject.hasProperty(Constants.LEVEL) ? worldObject.getProperty(Constants.LEVEL) : 1;
		int carryingCapacity = (strength) * 10 + (level - 1) * strength;
		return carryingCapacity;
	}
}
