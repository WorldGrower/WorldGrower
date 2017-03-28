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
import org.worldgrower.attribute.ArmorType;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.generator.Item;

public class ArmorPropertyUtils {

	public static int calculateArmor(WorldObject worldObject) {
		int armor = 0;
		
		WorldObject headEquipment = worldObject.getProperty(Constants.HEAD_EQUIPMENT);
		if (headEquipment != null) {
			
			double skillBonus = getSkillBonus(worldObject, headEquipment);
			armor += (skillBonus * headEquipment.getProperty(Constants.ARMOR));
		}
		
		WorldObject torsoEquipment = worldObject.getProperty(Constants.TORSO_EQUIPMENT);
		if (torsoEquipment != null) {
			double skillBonus = getSkillBonus(worldObject, torsoEquipment);
			armor += (skillBonus * torsoEquipment.getProperty(Constants.ARMOR));
		}
		
		WorldObject armsEquipment = worldObject.getProperty(Constants.ARMS_EQUIPMENT);
		if (armsEquipment != null) {
			double skillBonus = getSkillBonus(worldObject, armsEquipment);
			armor += (skillBonus * armsEquipment.getProperty(Constants.ARMOR));
		}
		
		WorldObject legsEquipment = worldObject.getProperty(Constants.LEGS_EQUIPMENT);
		if (legsEquipment != null) {
			double skillBonus = getSkillBonus(worldObject, legsEquipment);
			armor += (skillBonus * legsEquipment.getProperty(Constants.ARMOR));
		}
		
		WorldObject feetEquipment = worldObject.getProperty(Constants.FEET_EQUIPMENT);
		if (feetEquipment != null) {
			double skillBonus = getSkillBonus(worldObject, feetEquipment);
			armor += (skillBonus * feetEquipment.getProperty(Constants.ARMOR));
		}
		
		WorldObject shieldEquipment = worldObject.getProperty(Constants.RIGHT_HAND_EQUIPMENT);
		if (shieldEquipment != null && shieldEquipment.getProperty(Constants.ARMOR) != null) {
			double skillBonus = getSkillBonus(worldObject, shieldEquipment);
			armor += (skillBonus * shieldEquipment.getProperty(Constants.ARMOR));
		}
		
		return armor;
	}
	
	public static int calculateDamageResist(WorldObject worldObject) {
		int armor = calculateArmor(worldObject);
		return (int) Math.log((1000 * armor) / Item.COMBAT_MULTIPLIER + 1);
	}
	
	public static double getSkillBonus(WorldObject worldObject, WorldObject equipment) {
		SkillProperty skill;
		Integer armorTypeValue = equipment.getProperty(Constants.ARMOR_TYPE);
		if (armorTypeValue == null) {
			throw new IllegalStateException("WorldObject " + equipment + " has no armortype");
		}
		int armorType = armorTypeValue;
		if (armorType == ArmorType.LIGHT) {
			skill = Constants.LIGHT_ARMOR_SKILL;
		} else if (armorType == ArmorType.HEAVY) {
			skill = Constants.HEAVY_ARMOR_SKILL;
		} else {
			throw new IllegalStateException("armorType " + armorType + " isn't supported in worldobject " + worldObject + " and equipment " + equipment);
		}
		 
		double skillBonus = SkillUtils.getSkillBonus(worldObject, skill);
		return skillBonus;
	}
}
