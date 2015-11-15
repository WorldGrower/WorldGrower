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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.generator.Item;

public class UTestWeightPropertyUtils {

	@Test
	public void testGetCarryingCapacity() {
		WorldObject performer = TestUtils.createSkilledWorldObject(0);
		assertEquals(100, WeightPropertyUtils.getCarryingCapacity(performer));
	}
	
	@Test
	public void testGetTotalWeight() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, "Test");
		
		assertEquals(0, WeightPropertyUtils.getTotalWeight(performer));
		
		performer.getProperty(Constants.INVENTORY).add(Item.IRON_CUIRASS.generate(1f));
		assertEquals(30, WeightPropertyUtils.getTotalWeight(performer));
		
		Conditions.add(performer, Condition.BURDENED_CONDITION, 8, world);
		assertEquals(60, WeightPropertyUtils.getTotalWeight(performer));
		
		Conditions.remove(performer, Condition.BURDENED_CONDITION, world);
		Conditions.add(performer, Condition.FEATHERED_CONDITION, 8, world);
		assertEquals(15, WeightPropertyUtils.getTotalWeight(performer));
	}
	
	@Test
	public void testIsCarryingTooMuch() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, "Test");
		
		assertEquals(false, WeightPropertyUtils.isCarryingTooMuch(performer));
		
		performer.getProperty(Constants.INVENTORY).add(Item.IRON_CUIRASS.generate(1f));
		performer.getProperty(Constants.INVENTORY).add(Item.IRON_CUIRASS.generate(1f));
		performer.getProperty(Constants.INVENTORY).add(Item.IRON_CUIRASS.generate(1f));
		performer.getProperty(Constants.INVENTORY).add(Item.IRON_CUIRASS.generate(1f));
		assertEquals(true, WeightPropertyUtils.isCarryingTooMuch(performer));
	}
}
