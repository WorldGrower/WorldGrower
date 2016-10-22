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
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.goal.GoalUtils;

public class CraftUtils {

	public static boolean hasEnoughResources(WorldObject performer, int woodQuantity, int oreQuantity) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		int wood = inventory.getQuantityFor(Constants.WOOD);
		int ore = inventory.getQuantityFor(Constants.ORE);
		if ((wood < woodQuantity) || (ore < oreQuantity)) {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean hasEnoughResources(WorldObject performer, IntProperty property, int quantity) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		int inventoryQuantity = inventory.getQuantityFor(property);
		if (inventoryQuantity < quantity) {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		int performerId = performer.getProperty(Constants.ID);
		int targetId = target.getProperty(Constants.ID);
		return (performerId == targetId);
	}

	public static String getRequirementsDescription(IntProperty property, int quantity) {
		return "Requirements: " + formatAsString(property, quantity);
	}
	
	public static String getRequirementsDescription(String description) {
		return "Requirements: " + description;
	}
	
	public static String getRequirementsDescription(String description, String description2) {
		return "Requirements: " + description + ", " + description2;
	}
	
	private static String formatAsString(IntProperty property, int quantity) {
		if (property == Constants.ENERGY) {
			return "sufficient energy";
		} else if (property == Constants.DISTANCE) {
			if (quantity <= 1) {
				return "person needs to be adjacent";
			} else {
				return "distance to target less than " + quantity + " tiles";
			}
		} else {
			return quantity + " " + property.getName();
		}
	}
	
	public static boolean isValidBuildTarget(BuildAction buildAction, WorldObject performer, WorldObject target, World world) {
		int x = (Integer)target.getProperty(Constants.X);
		int y = (Integer)target.getProperty(Constants.Y);
		return !target.hasProperty(Constants.ID) && GoalUtils.isOpenSpace(x, y, buildAction.getWidth(), buildAction.getHeight(), world);
	}
	
	public static String getRequirementsDescription(IntProperty property, int quantity, IntProperty property2, int quantity2) {
		return "Requirements: " + formatAsString(property, quantity) + ", " + formatAsString(property2, quantity2);
	}
	
	public static String getRequirementsDescription(IntProperty property, int quantity, IntProperty property2, int quantity2, String description) {
		return getRequirementsDescription(property, quantity, property2, quantity2) + ", " + description;
	}
	
	public static String getRequirementsDescription(IntProperty property, int quantity, IntProperty property2, int quantity2, String description, String description2) {
		return getRequirementsDescription(property, quantity, property2, quantity2) + ", " + description + ", " + description2;
	}

	public static String getRequirementsDescription(IntProperty property, int quantity, String description) {
		return getRequirementsDescription(property, quantity) + ", " + description;
	}
	
	public static String getRequirementsDescription(IntProperty property, int quantity, String description, String description2) {
		return getRequirementsDescription(property, quantity) + ", " + description + ", " + description2;
	}
	
	public static String getRequirementsDescription(IntProperty property, int quantity, String description, String description2, String description3) {
		return getRequirementsDescription(property, quantity) + ", " + description + ", " + description2 + ", " + description3;
	}

	public static String getRequirementsDescription(SkillProperty skill, int requiredSkillLevel, IntProperty property, int quantity) {
		return "Requirements: skill " + skill.getName() + " " + requiredSkillLevel + ", " + formatAsString(property, quantity);
	}
}
