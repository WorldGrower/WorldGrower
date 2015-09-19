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
import org.worldgrower.attribute.UnCheckedProperty;
import org.worldgrower.attribute.WorldObjectContainer;

public class EquipmentGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		int ironClaymoreCount = inventory.getWorldObjects(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT).size();
		int ironCuirassCount = inventory.getWorldObjects(Constants.EQUIPMENT_SLOT, Constants.TORSO_EQUIPMENT).size();
		int ironHelmetCount = inventory.getWorldObjects(Constants.EQUIPMENT_SLOT, Constants.HEAD_EQUIPMENT).size();
		int ironGauntletsCount = inventory.getWorldObjects(Constants.EQUIPMENT_SLOT, Constants.ARMS_EQUIPMENT).size();
		int ironBootsCount = inventory.getWorldObjects(Constants.EQUIPMENT_SLOT, Constants.FEET_EQUIPMENT).size();
			
		if (ironClaymoreCount == 0) {
			return getOperationInfoForEquipment(performer, Constants.LEFT_HAND_EQUIPMENT, world);
		} else if (ironCuirassCount == 0) {
			return getOperationInfoForEquipment(performer, Constants.TORSO_EQUIPMENT, world);
		} else if (ironHelmetCount == 0) {
			return getOperationInfoForEquipment(performer, Constants.HEAD_EQUIPMENT, world);
		} else if (ironGauntletsCount == 0) {
			return getOperationInfoForEquipment(performer, Constants.ARMS_EQUIPMENT, world);
		} else if (ironBootsCount == 0) {
			return getOperationInfoForEquipment(performer, Constants.FEET_EQUIPMENT, world);
		} else {
			return null;
		}
	}

	private OperationInfo getOperationInfoForEquipment(WorldObject performer, UnCheckedProperty<WorldObject> equipmentSlot, World world) {
		OperationInfo buyOperationInfo = BuySellUtils.getBuyOperationInfo(performer, Constants.EQUIPMENT_SLOT, equipmentSlot, world);
		if (buyOperationInfo != null) {
			return buyOperationInfo;
		} else {
			return new CraftEquipmentGoal().calculateGoal(performer, world);
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return getNumberOfEquipmentItemsInInventory(performer) >= 5;
	}
	
	private int getNumberOfEquipmentItemsInInventory(WorldObject performer) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		int numberOfDamageItems = inventory.getQuantityFor(Constants.DAMAGE);
		int numberOfArmorItems = inventory.getQuantityFor(Constants.ARMOR);
		return (numberOfDamageItems + numberOfArmorItems);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking for equipment";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return getNumberOfEquipmentItemsInInventory(performer);
	}
}