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
package org.worldgrower.condition;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.gui.ImageIds;

public class GhoulUtils {

	public static void eatFood(WorldObject performer, WorldObject food, World world) {
		if (canBecomeGhoul(performer) && isFoodMadeFromHuman(food)) {
			ghoulifyPerson(performer, world);
		}
	}

	private static boolean isFoodMadeFromHuman(WorldObject food) {
		return food.hasProperty(Constants.CREATURE_TYPE) && food.getProperty(Constants.CREATURE_TYPE) == CreatureType.HUMAN_CREATURE_TYPE;
	}
	
	public static void ghoulifyPerson(WorldObject worldObject, World world) {
		worldObject.setProperty(Constants.CREATURE_TYPE, CreatureType.GHOUL_CREATURE_TYPE);
		worldObject.setProperty(Constants.GROUP, new IdList());
		worldObject.setProperty(Constants.IMAGE_ID, ImageIds.GHOUL);
		
		world.getWorldStateChangedListeners().fireCreatureTypeChanged(worldObject, CreatureType.GHOUL_CREATURE_TYPE, "You crave human flesh, you must have become a ghoul");
	}
	
	public static boolean canBecomeGhoul(WorldObject worldObject) {
		return worldObject.getProperty(Constants.CREATURE_TYPE) == CreatureType.HUMAN_CREATURE_TYPE;
	}

	public static boolean isGhoul(WorldObject w) {
		return w.getProperty(Constants.CREATURE_TYPE) == CreatureType.GHOUL_CREATURE_TYPE;
	}
}
