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
import org.worldgrower.OnTurn;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.DrownUtils;
import org.worldgrower.terrain.TerrainResource;
import org.worldgrower.terrain.TerrainType;

public class NightShadeOnTurn implements OnTurn {

	private static final int DEFAULT_NIGHTSHADE_INCREASE = 1;
	
	@Override
	public void onTurn(WorldObject worldObject, World world, WorldStateChangedListeners creatureTypeChangedListeners) {
		increaseNightShadeAmount(worldObject, world);
		
		DrownUtils.checkForDrowning(worldObject, world);
	}

	private static void increaseNightShadeAmount(WorldObject worldObject, World world) {
		int nightShadeProduced = calculateNightShadeProduced(worldObject, world);
		worldObject.increment(Constants.NIGHT_SHADE_SOURCE, nightShadeProduced);
		
		worldObject.setProperty(Constants.IMAGE_ID, NightShadeImageCalculator.getImageId(worldObject, world));
	}
	
	public static void increaseNightShadeAmountToMax(WorldObject worldObject, World world) {
		while (!Constants.NIGHT_SHADE_SOURCE.isAtMax(worldObject)) {
			increaseNightShadeAmount(worldObject, world);
		}
	}
	
	private static int calculateNightShadeProduced(WorldObject worldObject, World world) {
		int x = worldObject.getProperty(Constants.X);
		int y = worldObject.getProperty(Constants.Y);
		TerrainType terrainType = world.getTerrain().getTerrainInfo(x, y).getTerrainType();
		return DEFAULT_NIGHTSHADE_INCREASE + terrainType.getBonus(TerrainResource.NIGHTSHADE);
	}
	
	public static String getPercentageNightShadeBonus(TerrainType terrainType) {
		return terrainType.getPercentageBonus(TerrainResource.NIGHTSHADE, DEFAULT_NIGHTSHADE_INCREASE);
	}
}
