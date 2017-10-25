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
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.CraftFurnitureAction;
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;

public class FurniturePropertyUtils {

	private static final int QUANTITY_TO_BUY = 1;
	
	public static OperationInfo calculateGoal(WorldObject performer, IntProperty itemProperty, CraftFurnitureAction craftFurnitureAction, World world) {
		Item item = craftFurnitureAction.getItem();
		List<WorldObject> buyTargets = BuySellUtils.findBuyTargets(performer, itemProperty, QUANTITY_TO_BUY, world);
		if (buyTargets.size() > 0) {
			return BuySellUtils.create(performer, buyTargets.get(0), item, QUANTITY_TO_BUY, world);
		} else if (craftFurnitureAction.hasEnoughWood(performer)) {
			Integer workbenchId = BuildingGenerator.getWorkbenchId(performer);
			if (workbenchId == null) {
				return Goals.WORKBENCH_GOAL.calculateGoal(performer, world);
			} else {
				WorldObject workbench = world.findWorldObjectById(workbenchId);
				return new OperationInfo(performer, workbench, Args.EMPTY, craftFurnitureAction);
			}
		} else {
			return Goals.WOOD_GOAL.calculateGoal(performer, world);
		}
	}
}
