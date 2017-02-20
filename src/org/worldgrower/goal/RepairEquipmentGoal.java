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
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class RepairEquipmentGoal implements Goal {

	public RepairEquipmentGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		int numberOfRepairHammersInInventory = inventory.getQuantityFor(Constants.REPAIR_QUALITY);
		if (numberOfRepairHammersInInventory > 0) {
			int indexOfDamagedEquipment = inventory.getIndexFor(w -> w.hasProperty(Constants.EQUIPMENT_HEALTH) && w.getProperty(Constants.EQUIPMENT_HEALTH).intValue() < 1000);
			return new OperationInfo(performer, performer, new int[] { indexOfDamagedEquipment }, Actions.REPAIR_EQUIPMENT_IN_INVENTORY_ACTION);
		} else {
			return Goals.REPAIR_HAMMER_GOAL.calculateGoal(performer, world);
		}
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}
	
	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return getDamageOfEquipmentItemsInInventory(performer) <= 1000;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_REPAIR_EQUIPMENT);
	}
	
	private int getDamageOfEquipmentItemsInInventory(WorldObject performer) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		List<WorldObject> equipmentList = inventory.getWorldObjectsByFunction(Constants.EQUIPMENT_HEALTH, w -> true);
		int damage = 0;
		for(WorldObject equipment : equipmentList) {
			damage += (1000 - equipment.getProperty(Constants.EQUIPMENT_HEALTH));
		}
		return damage;
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return Integer.MAX_VALUE - getDamageOfEquipmentItemsInInventory(performer);
	}
}
