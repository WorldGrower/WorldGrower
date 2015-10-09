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

import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.ItemGenerator;
import org.worldgrower.gui.ImageIds;

public class UTestWorldObjectContainer {

	private static final ImageIds NO_IMAGE_ID = null;
	
	@Test
	public void testAddRemove() {
		WorldObjectContainer container = new WorldObjectContainer();
		container.add(TestUtils.createWorldObject(0, "Test1"));
		container.add(TestUtils.createWorldObject(1, "Test2"));
		container.remove(0);
		container.add(TestUtils.createWorldObject(2, "Test3"));
		
		assertEquals(3, container.size());
		assertEquals(null, container.get(0));
		assertEquals("Test2", container.get(1).getProperty(Constants.NAME));
		assertEquals("Test3", container.get(2).getProperty(Constants.NAME));
	}
	
	@Test
	public void testAddQuantity() {
		WorldObjectContainer container = new WorldObjectContainer();
		container.addQuantity(Constants.WOOD, 2, NO_IMAGE_ID);
		container.addQuantity(Constants.WOOD, 3, NO_IMAGE_ID);
		
		assertEquals(1, container.size());
		assertEquals(5, container.get(0).getProperty(Constants.QUANTITY).intValue());
	}
	
	@Test
	public void testAddQuantityByIndex() {
		WorldObjectContainer container = new WorldObjectContainer();
		container.addQuantity(Constants.WOOD, 2, NO_IMAGE_ID);
		container.addQuantity(0);
		
		assertEquals(1, container.size());
		assertEquals(3, container.get(0).getProperty(Constants.QUANTITY).intValue());
	}
	
	@Test
	public void testAddQuantityByWorldObject() {
		WorldObjectContainer container = new WorldObjectContainer();
		container.addQuantity(TestUtils.createWorldObject(0, "Weapon"));
		container.addQuantity(TestUtils.createWorldObject(0, "Weapon"));
		container.addQuantity(TestUtils.createWorldObject(0, "Shield"));
		
		assertEquals(2, container.size());
		assertEquals(2, container.get(0).getProperty(Constants.QUANTITY).intValue());
		assertEquals(1, container.get(1).getProperty(Constants.QUANTITY).intValue());
	}
	
	@Test
	public void testRemoveQuantity() {
		WorldObjectContainer container = new WorldObjectContainer();
		container.addQuantity(Constants.WOOD, 4, NO_IMAGE_ID);
		container.removeQuantity(Constants.WOOD, 1);
		
		assertEquals(1, container.size());
		assertEquals(3, container.get(0).getProperty(Constants.QUANTITY).intValue());
	}
	
	@Test
	public void testGetQuantityFor() {
		WorldObjectContainer container = new WorldObjectContainer();
		container.addQuantity(Constants.WOOD, 4, NO_IMAGE_ID);
		
		assertEquals(4, container.getQuantityFor(Constants.WOOD));
		assertEquals(0, container.getQuantityFor(Constants.STONE));
	}
	
	@Test
	public void testGetWorldObjects() {
		WorldObjectContainer container = new WorldObjectContainer();
		
		container.add(TestUtils.createIntelligentWorldObject(0, Constants.NAME, "Test1"));
		container.add(TestUtils.createIntelligentWorldObject(0, Constants.EXPERIENCE, 10));
		
		List<WorldObject> nameWorldObjects = container.getWorldObjects(Constants.NAME, "Test1");
		assertEquals(1, nameWorldObjects.size());
		assertEquals("Test1", nameWorldObjects.get(0).getProperty(Constants.NAME));
		
		List<WorldObject> raceWorldObjects = container.getWorldObjects(Constants.EXPERIENCE, 20);
		assertEquals(0, raceWorldObjects.size());
	}
	
	@Test
	public void testGetIndexFor() {
		WorldObjectContainer container = new WorldObjectContainer();
		container.addQuantity(Constants.WOOD, 1, NO_IMAGE_ID);
		
		assertEquals(0, container.getIndexFor(Constants.WOOD));
		assertEquals(-1, container.getIndexFor(Constants.STONE));
	}
	
	@Test
	public void testGetWorldObjectsByFunction() {
		WorldObjectContainer container = new WorldObjectContainer();
		container.addQuantity(Constants.WOOD, 1, NO_IMAGE_ID);
		container.addQuantity(Constants.FOOD, 10, NO_IMAGE_ID);
		
		List<WorldObject> items = container.getWorldObjectsByFunction(Constants.WOOD, w -> w.getProperty(Constants.WOOD) > 0);
		assertEquals(1, items.size());
	}
	
	@Test
	public void testGetIndexForWithFunction() {
		WorldObjectContainer container = new WorldObjectContainer();
		container.add(TestUtils.createWorldObject(1, "Test"));
		container.addQuantity(Constants.FOOD, 10, NO_IMAGE_ID);
		
		assertEquals(0, container.getIndexFor(Constants.NAME, "Test", w -> w.getProperty(Constants.ID) == 1));
	}
	
	@Test
	public void testGetIndexForWithFunctionAndEquipment() {
		WorldObjectContainer container = new WorldObjectContainer();
		container.add(ItemGenerator.getIronCuirass(1f));
		container.addQuantity(Constants.FOOD, 10, NO_IMAGE_ID);
		
		assertEquals(0, container.getIndexFor(Constants.EQUIPMENT_SLOT, Constants.TORSO_EQUIPMENT, w -> w.getProperty(Constants.ARMOR) > 0));
	}
	
	@Test
	public void testGetIndexForFunction() {
		WorldObjectContainer container = new WorldObjectContainer();
		container.add(TestUtils.createWorldObject(1, "Test"));
		container.addQuantity(Constants.FOOD, 10, NO_IMAGE_ID);
		
		assertEquals(0, container.getIndexFor(w -> w.getProperty(Constants.ID) == 1));
	}
	
	@Test
	public void testGetIndexForWithValue() {
		WorldObjectContainer container = new WorldObjectContainer();
		container.add(TestUtils.createWorldObject(1, "Test"));
		container.addQuantity(Constants.FOOD, 10, NO_IMAGE_ID);
		
		assertEquals(1, container.getIndexFor(Constants.FOOD, 1));
	}
	
	@Test
	public void testRemoveAllQuantity() {
		WorldObjectContainer container = new WorldObjectContainer();
		container.addQuantity(Constants.WOOD, 1, NO_IMAGE_ID);
		
		container.removeAllQuantity(Constants.WOOD);
		
		assertEquals(null, container.get(0));
	}
	
	@Test
	public void testContains() {
		WorldObjectContainer container = new WorldObjectContainer();
		WorldObject worldObject1 = TestUtils.createWorldObject(1, "Test");
		container.add(worldObject1);
		WorldObject worldObject2 = TestUtils.createWorldObject(2, "Test2");
		
		assertEquals(true, container.contains(worldObject1));
		assertEquals(false, container.contains(worldObject2));
	}
}
