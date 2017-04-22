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

public class CottonPlantOnTurn implements OnTurn {

	private static final int DEFAULT_COTTON_INCREASE = 10;
	
	@Override
	public void onTurn(WorldObject worldObject, World world, WorldStateChangedListeners creatureTypeChangedListeners) {
		increaseCottonAmount(worldObject, world);
		
		DrownUtils.checkForDrowning(worldObject, world);
	}

	private static void increaseCottonAmount(WorldObject worldObject, World world) {
		int cottonProduced = calculateCottonProduced(worldObject, world);
		worldObject.increment(Constants.COTTON_SOURCE, cottonProduced);
		
		worldObject.setProperty(Constants.IMAGE_ID, CottonPlantImageCalculator.getImageId(worldObject, world));
	}
	
	public static void increaseCottonAmountToMax(WorldObject worldObject, World world) {
		while(!Constants.COTTON_SOURCE.isAtMax(worldObject)) {
			increaseCottonAmount(worldObject, world);
		}
	}
	
	private static int calculateCottonProduced(WorldObject worldObject, World world) {
		int x = worldObject.getProperty(Constants.X);
		int y = worldObject.getProperty(Constants.Y);
		TerrainType terrainType = world.getTerrain().getTerrainInfo(x, y).getTerrainType();
		return DEFAULT_COTTON_INCREASE + terrainType.getBonus(TerrainResource.COTTON);
	}
	
	public static String getPercentageCottonBonus(TerrainType terrainType) {
		return terrainType.getPercentageBonus(TerrainResource.COTTON, DEFAULT_COTTON_INCREASE);
	}
}
