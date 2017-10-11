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
package org.worldgrower.generator;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.generator.WorldGenerator.AddWorldObjectFunction;
import org.worldgrower.goal.GoalUtils;
import org.worldgrower.goal.LocationUtils;
import org.worldgrower.terrain.TerrainType;

public class PregnancyPropertyUtils {

	public static boolean checkPregnancy(WorldObject worldObject, World world, int currentTurn, int pregnancyDuration, AddWorldObjectFunction addWorldObjectFunction) {
		Integer pregnancy = worldObject.getProperty(Constants.PREGNANCY);
		if (pregnancy != null) {
			pregnancy = pregnancy + 1;
			worldObject.setProperty(Constants.PREGNANCY, pregnancy);
			
			if (pregnancy > pregnancyDuration) {
				int performerX = worldObject.getProperty(Constants.X);
				int performerY = worldObject.getProperty(Constants.Y);
				int[] position = GoalUtils.findOpenSpace(worldObject, 1, 1, world);
				if (position != null) {
					int x = position[0] + performerX;
					int y = position[1] + performerY;
					if (!LocationUtils.areInvalidCoordinates(x, y, world)) {
						TerrainType terrainType = world.getTerrain().getTerrainType(x, y);
						if (terrainType != TerrainType.WATER) {
							int childId = addWorldObjectFunction.addToWorld(x, y, world);
							WorldObject child = world.findWorldObjectById(childId);
							child.setProperty(Constants.CATTLE_OWNER_ID, worldObject.getProperty(Constants.CATTLE_OWNER_ID));
							
							worldObject.removeProperty(Constants.PREGNANCY);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
