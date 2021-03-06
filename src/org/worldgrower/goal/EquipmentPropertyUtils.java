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
import org.worldgrower.attribute.UnCheckedProperty;
import org.worldgrower.generator.Item;

public class EquipmentPropertyUtils {

	public static boolean isEquipmentWorn(WorldObject performer, WorldObject worldObject) {
		if (worldObject.hasProperty(Constants.EQUIPMENT_SLOT)) {
			UnCheckedProperty<WorldObject> equipmentSlot = worldObject.getProperty(Constants.EQUIPMENT_SLOT);
			WorldObject equippedItem = performer.getProperty(equipmentSlot);
			return equippedItem == worldObject;
		} else {
			return false;
		}
	}
	
	public static void equip(WorldObject performer, Item item) {
		WorldObject equipment = item.generate(1f);
		UnCheckedProperty<WorldObject> equipmentSlot = equipment.getProperty(Constants.EQUIPMENT_SLOT);

		performer.getProperty(Constants.INVENTORY).addQuantity(equipment);
		performer.setProperty(equipmentSlot, equipment);
	}
	
	public static boolean isMeleeWeapon(WorldObject worldObject) {
		return (worldObject != null && worldObject.hasProperty(Constants.DAMAGE) && !worldObject.hasProperty(Constants.RANGE));
	}
	
	public static boolean isRangedWeapon(WorldObject worldObject) {
		return (worldObject != null && worldObject.hasProperty(Constants.DAMAGE) && worldObject.hasProperty(Constants.RANGE));
	}
}
