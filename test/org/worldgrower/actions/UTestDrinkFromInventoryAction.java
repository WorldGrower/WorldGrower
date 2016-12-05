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
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.generator.Item;

public class UTestDrinkFromInventoryAction {

	@Test
	public void testExecuteDrinkWater() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WATER.generate(1f));
		
		assertEquals(800, performer.getProperty(Constants.WATER).intValue());
		Actions.DRINK_FROM_INVENTORY_ACTION.execute(performer, performer, new int[] {0}, world);
		
		assertEquals(950, performer.getProperty(Constants.WATER).intValue());
	}
	
	@Test
	public void testExecuteDrinkAlcohol() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		WorldObject wine = Item.WINE.generate(1f);
		wine.setProperty(Constants.ALCOHOL_LEVEL, 9000);
		performer.getProperty(Constants.INVENTORY).addQuantity(wine);
		
		assertEquals(800, performer.getProperty(Constants.WATER).intValue());
		Actions.DRINK_FROM_INVENTORY_ACTION.execute(performer, performer, new int[] {0}, world);
		
		assertEquals(true, performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.INTOXICATED_CONDITION));
	}
	
	@Test
	public void testExecuteDrinkPoison() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		WorldObject water = Item.WATER.generate(1f);
		water.setProperty(Constants.POISON_DAMAGE, 10);
		performer.getProperty(Constants.INVENTORY).addQuantity(water);
		
		assertEquals(800, performer.getProperty(Constants.WATER).intValue());
		Actions.DRINK_FROM_INVENTORY_ACTION.execute(performer, performer, new int[] {0}, world);
		
		assertEquals(true, performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.POISONED_CONDITION));
	}

	@Test
	public void testIsValidInventoryItem() {
		WorldObject performer = createPerformer(2);		
		WorldObject water = Item.WATER.generate(1f);
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		inventory.addQuantity(water);
		WorldObject food = Item.BERRIES.generate(1f);
		
		assertEquals(true, Actions.DRINK_FROM_INVENTORY_ACTION.isValidInventoryItem(water, inventory, performer));
		assertEquals(false, Actions.DRINK_FROM_INVENTORY_ACTION.isValidInventoryItem(food, inventory, performer));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.WATER, 800);
		performer.setProperty(Constants.ALCOHOL_LEVEL, 0);
		performer.setProperty(Constants.CONDITIONS, new Conditions());
		return performer;
	}
}