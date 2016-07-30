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
import org.worldgrower.actions.FoodPropertyUtils;

public class EquipScytheGoal implements Goal {

	public EquipScytheGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (!FoodPropertyUtils.leftHandContainsScythe(performer)) {
			if (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.SCYTHE_QUALITY) > 0) {
				int indexOfScythe = performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.SCYTHE_QUALITY);
				return new OperationInfo(performer, performer, new int[] { indexOfScythe }, Actions.EQUIP_INVENTORY_ITEM_ACTION);
			} else {
				return Goals.SCYTHE_GOAL.calculateGoal(performer, world);
			}
		}
		
		return null;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return FoodPropertyUtils.leftHandContainsScythe(performer);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "equipping a scythe";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
