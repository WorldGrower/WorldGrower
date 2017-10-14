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
package org.worldgrower.actions;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillUtils;

public class WoodPropertyUtils {
	
	public static boolean leftHandContainsWoodCuttingTool(WorldObject performer) {
		WorldObject leftHand = performer.getProperty(Constants.LEFT_HAND_EQUIPMENT);
		return (leftHand != null && leftHand.hasProperty(Constants.WOOD_CUTTING_QUALITY));
	}
	
	public static int calculateLumberingQuantity(WorldObject performer) {
		int quantity = SkillUtils.getLogarithmicSkillBonus(performer, Constants.LUMBERING_SKILL);
		if (leftHandContainsWoodCuttingTool(performer)) {
			quantity += 1;
		}
		return quantity;
	}
}
