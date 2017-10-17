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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.WorldObject;

public class UTestFoodCooker {
	
	@Test
	public void testCookFood() {
		for(Item item : Item.values()) {
			WorldObject worldObjectItem = item.generate(1f);
			if (worldObjectItem.hasProperty(Constants.FOOD)) {
				FoodCooker.cook(worldObjectItem);
				assertEquals(2, worldObjectItem.getProperty(Constants.FOOD).intValue());
				assertNotNull(worldObjectItem.getProperty(Constants.IMAGE_ID));
			}
		}
		
		WorldObject meat = Item.MEAT.generate(1f);
		FoodCooker.cook(meat);
		assertEquals("cooked meat", meat.getProperty(Constants.NAME));
		FoodCooker.cook(meat);
		assertEquals("cooked meat", meat.getProperty(Constants.NAME));
	}
}