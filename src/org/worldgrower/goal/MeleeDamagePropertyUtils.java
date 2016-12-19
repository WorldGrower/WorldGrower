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

public class MeleeDamagePropertyUtils {

	public static int calculateMeleeDamage(WorldObject worldObject) {
		int meleeDamage = 0;
		
		WorldObject leftHandEquipment = worldObject.getProperty(Constants.LEFT_HAND_EQUIPMENT);
		if (leftHandEquipment != null) {
			meleeDamage += leftHandEquipment.getProperty(Constants.DAMAGE);
		}
		
		WorldObject rightHandEquipment = worldObject.getProperty(Constants.RIGHT_HAND_EQUIPMENT);
		if (rightHandEquipment != null && leftHandEquipment != rightHandEquipment) {
			meleeDamage += rightHandEquipment.getProperty(Constants.DAMAGE);
		}
		
		if (meleeDamage == 0) {
			meleeDamage = 2 * Item.COMBAT_MULTIPLIER;
			WorldObject armsEquipment = worldObject.getProperty(Constants.ARMS_EQUIPMENT);
			if (armsEquipment != null) {
				int armsEquipmentArmor = armsEquipment.getProperty(Constants.ARMOR) / 2;
				meleeDamage += armsEquipmentArmor;
			}
		}
		
		return meleeDamage;
	}
	
	public static void setTwoHandedWeapons(WorldObject worldObject, UnCheckedProperty<WorldObject> lastModifiedHandEquipmentProperty) {
		WorldObject leftHandEquipment = worldObject.getProperty(Constants.LEFT_HAND_EQUIPMENT);
		WorldObject rightHandEquipment = worldObject.getProperty(Constants.RIGHT_HAND_EQUIPMENT);
		
		if (lastModifiedHandEquipmentProperty == Constants.LEFT_HAND_EQUIPMENT && rightHandEquipment != null && isTwoHandedWeapon(rightHandEquipment)) {
			worldObject.setProperty(Constants.RIGHT_HAND_EQUIPMENT, null);
		}
		if (lastModifiedHandEquipmentProperty == Constants.RIGHT_HAND_EQUIPMENT && leftHandEquipment != null && isTwoHandedWeapon(leftHandEquipment)) {
			worldObject.setProperty(Constants.LEFT_HAND_EQUIPMENT, null);
		}
		
		handleTwoHandedWeapons(worldObject, leftHandEquipment, rightHandEquipment, Constants.RIGHT_HAND_EQUIPMENT);
		
		leftHandEquipment = worldObject.getProperty(Constants.LEFT_HAND_EQUIPMENT);
		rightHandEquipment = worldObject.getProperty(Constants.RIGHT_HAND_EQUIPMENT);
		handleTwoHandedWeapons(worldObject, rightHandEquipment, leftHandEquipment, Constants.LEFT_HAND_EQUIPMENT);
	}

	private static void handleTwoHandedWeapons(WorldObject worldObject, WorldObject leftHandEquipment, WorldObject rightHandEquipment, UnCheckedProperty<WorldObject> propertyToSet) {
		
		
		
		if (leftHandEquipment != null) {
			if (isTwoHandedWeapon(leftHandEquipment)) {
				worldObject.setProperty(propertyToSet, leftHandEquipment);
			} else if (rightHandEquipment != null && isTwoHandedWeapon(rightHandEquipment)) {
				worldObject.setProperty(propertyToSet, null);
			}
		}
	}

	public static boolean isTwoHandedWeapon(WorldObject leftHandEquipment) {
		return leftHandEquipment.hasProperty(Constants.TWO_HANDED_WEAPON) && leftHandEquipment.getProperty(Constants.TWO_HANDED_WEAPON);
	}
}
