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
import org.worldgrower.attribute.Skill;
import org.worldgrower.attribute.WorldObjectContainer;

public class CraftUtils {

	public static double useSmithingSkill(WorldObject performer) {
		Skill smithingSkill = performer.getProperty(Constants.SMITHING_SKILL);
		double result = 1.0f + (smithingSkill.getLevel() / 100.0f);
		smithingSkill.use();
		return result;
	}
	
	public static double useAlchemySkill(WorldObject performer) {
		Skill alchemySkill = performer.getProperty(Constants.ALCHEMY_SKILL);
		double result = 1.0f + (alchemySkill.getLevel() / 100.0f);
		alchemySkill.use();
		return result;
	}
	
	public static int distance(WorldObject performer, int woodQuantity, int oreQuantity) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		int wood = inventory.getQuantityFor(Constants.WOOD);
		int ore = inventory.getQuantityFor(Constants.ORE);
		if ((wood < woodQuantity) || (ore < oreQuantity)) {
			return (woodQuantity - wood) * 1000 + (oreQuantity - ore) * 1000;
		} else {
			return 0;
		}
	}
	
	public static int distance(WorldObject performer, IntProperty property, int quantity) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		int inventoryQuantity = inventory.getQuantityFor(property);
		if (inventoryQuantity < quantity) {
			return (quantity - inventoryQuantity) * 1000;
		} else {
			return 0;
		}
	}
	
	public static boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		int performerId = performer.getProperty(Constants.ID);
		int targetId = target.getProperty(Constants.ID);
		return (performerId == targetId);
	}
}
