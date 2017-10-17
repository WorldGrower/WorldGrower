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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.FoodCooker;
import org.worldgrower.generator.Item;

public class UTestEatFromInventoryAction {

	private final EatFromInventoryAction action = Actions.EAT_FROM_INVENTORY_ACTION;
	
	@Test
	public void testExecute() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.BERRIES.generate(1f));
		performer.setProperty(Constants.FOOD, 600);
		
		action.execute(performer, performer, new int[] {0}, world);
		
		assertEquals(775, performer.getProperty(Constants.FOOD).intValue());
		assertEquals(0, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FOOD));
	}
	
	@Test
	public void testExecuteCookedFood() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		WorldObject berries = Item.BERRIES.generate(1f);
		FoodCooker.cook(berries);
		performer.getProperty(Constants.INVENTORY).addQuantity(berries);
		performer.setProperty(Constants.FOOD, 600);
		
		action.execute(performer, performer, new int[] {0}, world);
		
		assertEquals(950, performer.getProperty(Constants.FOOD).intValue());
		assertEquals(0, performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FOOD));
	}
	
	@Test
	public void testIsValidInventoryItem() {
		WorldObject performer = createPerformer(2);
		
		WorldObject inventoryItem = Item.BERRIES.generate(1f);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.BERRIES.generate(1f));
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		
		assertTrue(action.isValidInventoryItem(inventoryItem, inventory, performer));
		
		inventoryItem = Item.COTTON_BOOTS.generate(1f);
		assertFalse(action.isValidInventoryItem(inventoryItem, inventory, performer));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}