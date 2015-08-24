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
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.terrain.TerrainType;

public class DrownUtils {

	public static void checkForDrowning(WorldObject worldObject, World world) {
		int x = worldObject.getProperty(Constants.X);
		int y = worldObject.getProperty(Constants.Y);
		
		if (!LocationUtils.areInvalidCoordinates(x, y, world)) {
			TerrainType terrainType = world.getTerrain().getTerrainInfo(x, y).getTerrainType();
			if (terrainType == TerrainType.WATER) {
				Conditions conditions = worldObject.getProperty(Constants.CONDITIONS);
				boolean hasWaterWalkCondition = conditions != null ? conditions.hasCondition(Condition.WATER_WALK_CONDITION) : false;
				if (!hasWaterWalkCondition) {
					worldObject.increment(Constants.HIT_POINTS, -5);
					if (worldObject.getProperty(Constants.HIT_POINTS) == 0) {
						if (worldObject.hasProperty(Constants.CONDITIONS)) {
							DeathReasonPropertyUtils.targetDiesByDrowning(worldObject);
						}
					}
				}
			}
		}
	}
}
