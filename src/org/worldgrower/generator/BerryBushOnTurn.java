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
import org.worldgrower.actions.FoodPropertyUtils;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.DrownUtils;
import org.worldgrower.terrain.TerrainResource;
import org.worldgrower.terrain.TerrainType;

public class BerryBushOnTurn implements OnTurn {

	private static final int DEFAULT_FOOD_INCREASE = 4;
	
	@Override
	public void onTurn(WorldObject worldObject, World world, WorldStateChangedListeners creatureTypeChangedListeners) {
		increaseFoodAmount(worldObject, world);
		
		DrownUtils.checkForDrowning(worldObject, world);
	}

	private static void increaseFoodAmount(WorldObject worldObject, World world) {
		int foodProduced = calculateFoodProduced(worldObject, world);
		if (!Constants.FOOD_PRODUCED.isAtMax(worldObject)) {
			worldObject.increment(Constants.FOOD_SOURCE, foodProduced);
		}
		worldObject.increment(Constants.FOOD_PRODUCED, foodProduced);
		FoodPropertyUtils.checkFoodSourceExhausted(worldObject);

		worldObject.setProperty(Constants.IMAGE_ID, BerryBushImageCalculator.getImageId(worldObject, world));
	}
	
	public static void increaseFoodAmountToMax(WorldObject worldObject, World world) {
		while (!Constants.FOOD_PRODUCED.isAtMax(worldObject)) {
			increaseFoodAmount(worldObject, world);
		}
	}
	
	private static int calculateFoodProduced(WorldObject worldObject, World world) {
		int x = worldObject.getProperty(Constants.X);
		int y = worldObject.getProperty(Constants.Y);
		TerrainType terrainType = world.getTerrain().getTerrainType(x, y);
		return DEFAULT_FOOD_INCREASE + terrainType.getBonus(TerrainResource.FOOD);
	}
	
	public static String getPercentageFoodBonus(TerrainType terrainType) {
		return terrainType.getPercentageBonus(TerrainResource.FOOD, DEFAULT_FOOD_INCREASE);
	}
}
