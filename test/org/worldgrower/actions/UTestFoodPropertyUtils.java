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
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.PlantGenerator;

public class UTestFoodPropertyUtils {

	@Test
	public void testFoodSourceExhausted() {
		World world = new WorldImpl(1, 1, null, null);
		int berryBushId = PlantGenerator.generateBerryBush(0, 0, world);
		WorldObject performer = createPerformer(2);
		WorldObject berryBush = world.findWorldObjectById(berryBushId);
		
		berryBush.setProperty(Constants.HIT_POINTS, 1);
		berryBush.setProperty(Constants.FOOD_SOURCE, new BerryBushFoodSource());
		
		FoodSource foodSource = berryBush.getProperty(Constants.FOOD_SOURCE);
		foodSource.increaseFoodAmount(100, berryBush, world);
		foodSource.eat(performer, berryBush, world);
		assertEquals(1, berryBush.getProperty(Constants.HIT_POINTS).intValue());
		
		foodSource.increaseFoodAmountToMax(berryBush, world);
		while(foodSource.hasEnoughFood()) {
			foodSource.eat(performer, berryBush, world);	
		}
		
		assertEquals(0, berryBush.getProperty(Constants.HIT_POINTS).intValue());
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.GROUP, new IdList());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.ENERGY, 1000);
		performer.setProperty(Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.GOLD, 1000);
		performer.setProperty(Constants.ORGANIZATION_GOLD, 0);
		performer.setProperty(Constants.DEATH_REASON, "");
		return performer;
	}
}
