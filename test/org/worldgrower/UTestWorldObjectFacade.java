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
package org.worldgrower;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.generator.PlantGenerator;
import org.worldgrower.goal.Goals;

public class UTestWorldObjectFacade {

	@Test
	public void testEquals() {
		WorldObject person1 = new WorldObjectFacade(TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 500), TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 500));
		WorldObject person2 = new WorldObjectFacade(TestUtils.createIntelligentWorldObject(2, Constants.FOOD, 500), TestUtils.createIntelligentWorldObject(2, Constants.FOOD, 500));
		
		assertEquals(false, person1.equals(person2));
		assertEquals(true, person1.equals(person1));
		assertEquals(false, person1.equals(new Object()));
	}
	
	@Test
	public void testGetProperty() {
		WorldObject person = new WorldObjectFacade(TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 500), TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 750));
		
		assertEquals(750, person.getProperty(Constants.FOOD).intValue());
	}
	
	@Test
	public void testHasProperty() {
		WorldObject person = new WorldObjectFacade(TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 500), TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 750));
		
		assertEquals(true, person.hasProperty(Constants.FOOD));
	}
	
	@Test
	public void testSetProperty() {
		WorldObject person = new WorldObjectFacade(TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 500), TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 750));
		
		person.setProperty(Constants.FOOD, 250);
		
		assertEquals(750, person.getProperty(Constants.FOOD).intValue());
	}
	
	@Test
	public void testIncrement() {
		WorldObject person = new WorldObjectFacade(TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 500), TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 750));
		
		person.increment(Constants.FOOD, 50);
		
		assertEquals(750, person.getProperty(Constants.FOOD).intValue());
	}
	
	@Test
	public void testHasIntelligence() {
		WorldObject berryBush = createBerryBush();
		WorldObject person = new WorldObjectFacade(TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 500), berryBush);
		
		//TODO: is this correct?
		assertEquals(true, person.hasIntelligence());
	}

	@Test
	public void testIsControlledByAI() {
		WorldObject berryBush = createBerryBush();
		
		WorldObject originalWorldObject = TestUtils.createIntelligentWorldObject(1, Goals.WOOD_GOAL);
		WorldObject person = new WorldObjectFacade(originalWorldObject, berryBush);
		
		assertEquals(true, person.isControlledByAI());
	}
	
	@Test
	public void testRemoveProperty() {
		WorldObject person = new WorldObjectFacade(TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 500), TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 750));
		
		person.removeProperty(Constants.FOOD);
		
		assertEquals(750, person.getProperty(Constants.FOOD).intValue());
	}

	private WorldObject createBerryBush() {
		World world = new WorldImpl(1, 1, null, null);
		int berryBushId = PlantGenerator.generateBerryBush(0, 0, world);
		WorldObject berryBush = world.findWorldObject(Constants.ID, berryBushId);
		return berryBush;
	}
}
