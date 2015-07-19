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

public class ArmorPropertyUtils {

	public static int calculateArmor(WorldObject worldObject) {
		int armor = 0;
		
		WorldObject headEquipment = worldObject.getProperty(Constants.HEAD_EQUIPMENT);
		if (headEquipment != null) {
			armor += headEquipment.getProperty(Constants.ARMOR);
		}
		
		WorldObject torsoEquipment = worldObject.getProperty(Constants.TORSO_EQUIPMENT);
		if (torsoEquipment != null) {
			armor += torsoEquipment.getProperty(Constants.ARMOR);
		}
		
		WorldObject armsEquipment = worldObject.getProperty(Constants.ARMS_EQUIPMENT);
		if (armsEquipment != null) {
			armor += armsEquipment.getProperty(Constants.ARMOR);
		}
		
		WorldObject legsEquipment = worldObject.getProperty(Constants.LEGS_EQUIPMENT);
		if (legsEquipment != null) {
			armor += legsEquipment.getProperty(Constants.ARMOR);
		}
		
		WorldObject feetEquipment = worldObject.getProperty(Constants.FEET_EQUIPMENT);
		if (feetEquipment != null) {
			armor += feetEquipment.getProperty(Constants.ARMOR);
		}
		
		return armor;
	}
}
