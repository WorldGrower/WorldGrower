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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.generator.PlantGenerator;

public class UTestFoodPropertyUtils {

	@Test
	public void testFoodSourceExhausted() {
		World world = new WorldImpl(1, 1, null, null);
		int berryBushId = PlantGenerator.generateBerryBush(0, 0, world);
		WorldObject berryBush = world.findWorldObjectById(berryBushId);
		
		berryBush.setProperty(Constants.HIT_POINTS, 1);
		berryBush.setProperty(Constants.FOOD_SOURCE, 0);
		berryBush.setProperty(Constants.FOOD_PRODUCED, 0);
		
		FoodPropertyUtils.checkFoodSourceExhausted(berryBush);
		assertEquals(1, berryBush.getProperty(Constants.HIT_POINTS).intValue());
		
		berryBush.setProperty(Constants.HIT_POINTS, 1);
		berryBush.setProperty(Constants.FOOD_SOURCE, 0);
		berryBush.setProperty(Constants.FOOD_PRODUCED, 500);
		
		FoodPropertyUtils.checkFoodSourceExhausted(berryBush);
		assertEquals(0, berryBush.getProperty(Constants.HIT_POINTS).intValue());
		
		berryBush.setProperty(Constants.HIT_POINTS, 1);
		berryBush.setProperty(Constants.FOOD_SOURCE, 200);
		berryBush.setProperty(Constants.FOOD_PRODUCED, 499);
		
		FoodPropertyUtils.checkFoodSourceExhausted(berryBush);
		assertEquals(1, berryBush.getProperty(Constants.HIT_POINTS).intValue());
	}

}
