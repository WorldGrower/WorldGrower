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
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;

public class EquipmentGoal implements Goal {

	public EquipmentGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		int ironClaymoreCount = inventory.getWorldObjects(Constants.EQUIPMENT_SLOT, Constants.LEFT_HAND_EQUIPMENT).size();
		int ironCuirassCount = inventory.getWorldObjects(Constants.EQUIPMENT_SLOT, Constants.TORSO_EQUIPMENT).size();
		int ironHelmetCount = inventory.getWorldObjects(Constants.EQUIPMENT_SLOT, Constants.HEAD_EQUIPMENT).size();
		int ironGauntletsCount = inventory.getWorldObjects(Constants.EQUIPMENT_SLOT, Constants.ARMS_EQUIPMENT).size();
		int ironBootsCount = inventory.getWorldObjects(Constants.EQUIPMENT_SLOT, Constants.FEET_EQUIPMENT).size();
		
		OperationInfo equipOperationInfo = Goals.USE_EQUIPMENT_GOAL.calculateGoal(performer, world);
		
		if (equipOperationInfo != null) {
			return equipOperationInfo;
		} else if (ironClaymoreCount == 0) {
			return getEquipment(performer, Item.IRON_CLAYMORE, world);
		} else if (UseEquipmentGoal.hasUnusedEquipment(performer, Constants.LEFT_HAND_EQUIPMENT)) {
			return UseEquipmentGoal.equipUnusedEquipment(performer, Constants.LEFT_HAND_EQUIPMENT, world);
		} else if (ironCuirassCount == 0) {
			return getEquipment(performer, Item.IRON_CUIRASS, world);
		} else if (ironHelmetCount == 0) {
			return getEquipment(performer, Item.IRON_HELMET, world);
		} else if (ironGauntletsCount == 0) {
			return getEquipment(performer, Item.IRON_GAUNTLETS, world);
		} else if (ironBootsCount == 0) {
			return getEquipment(performer, Item.IRON_BOOTS, world);
		} else {
			return null;
		}
	}

	private OperationInfo getEquipment(WorldObject performer, Item item, World world) {
		//TODO: buy items should work with light/heavy armor
		OperationInfo buyOperationInfo = BuySellUtils.getBuyOperationInfo(performer, item, 1, world);
		if (buyOperationInfo != null) {
			return buyOperationInfo;
		} else {
			if (craftHeavyArmor(performer)) {
				return Goals.CRAFT_EQUIPMENT_GOAL.calculateGoal(performer, world);
			} else {
				return Goals.WEAVE_CLOTHES_GOAL.calculateGoal(performer, world);
			}
		}
	}
	
	private boolean craftHeavyArmor(WorldObject performer) {
		int strength = performer.getProperty(Constants.STRENGTH);
		return strength >= 12;
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
		return inventory.getWorldObjectsByFunction(Constants.EQUIPMENT_HEALTH, w -> true).size();
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