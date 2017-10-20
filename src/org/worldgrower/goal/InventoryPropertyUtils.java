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

import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.UnCheckedProperty;
import org.worldgrower.attribute.WorldObjectContainer;

public class InventoryPropertyUtils {

	private static final List<UnCheckedProperty<WorldObject>> EQUIPMENT_SLOTS = Arrays.asList(
			Constants.HEAD_EQUIPMENT,
			Constants.TORSO_EQUIPMENT,
			Constants.ARMS_EQUIPMENT,
			Constants.LEGS_EQUIPMENT,
			Constants.FEET_EQUIPMENT,
			Constants.LEFT_HAND_EQUIPMENT,
			Constants.RIGHT_HAND_EQUIPMENT
			);
	
	public static void cleanupEquipmentSlots(WorldObject worldObject) {
		WorldObjectContainer inventory = worldObject.getProperty(Constants.INVENTORY);
		
		for(UnCheckedProperty<WorldObject> equipmentSlot : EQUIPMENT_SLOTS) {
			WorldObject equipmentItem = worldObject.getProperty(equipmentSlot);
			if (inventory.getIndexFor(equipmentItem) == -1) {
				worldObject.setProperty(equipmentSlot, null);
			}
		}
		
		worldObject.setProperty(Constants.ARMOR, ArmorPropertyUtils.calculateArmor(worldObject));
		worldObject.setProperty(Constants.DAMAGE, MeleeDamagePropertyUtils.calculateMeleeDamage(worldObject));
		worldObject.setProperty(Constants.DAMAGE_RESIST, ArmorPropertyUtils.calculateDamageResist(worldObject));
	}
	
}
