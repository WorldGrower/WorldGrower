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
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;

public class UTestSellUnusedItemsGoal {

	private SellUnusedItemsGoal goal = Goals.SELL_UNUSED_ITEMS_GOAL;

	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());

		assertEquals(null, goal.calculateGoal(performer, world));
	}

	@Test
	public void testCalculateScythe() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.SCYTHE.generate(1f), 3);
		
		assertEquals(Actions.MARK_INVENTORY_ITEM_AS_SELLABLE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(0, goal.calculateGoal(performer, world).getArgs()[0]);
		assertEquals(20, goal.calculateGoal(performer, world).getArgs()[1]);
	}
	
	@Test
	public void testCalculateBook() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_BOOTS.generate(1f), 3);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.SPELLBOOK.generate(1f), 3);
		
		assertEquals(Actions.MARK_INVENTORY_ITEM_AS_SELLABLE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(1, goal.calculateGoal(performer, world).getArgs()[0]);
		assertEquals(100, goal.calculateGoal(performer, world).getArgs()[1]);
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		assertEquals(true, goal.isGoalMet(performer, world));
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.SCYTHE.generate(1f), 3);
		assertEquals(false, goal.isGoalMet(performer, world));
	}
}