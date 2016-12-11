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

public class UTestMarkAsSellableOrDropGoal {

	private MarkAsSellableOrDropGoal goal = Goals.MARK_LEATHER_AS_SELLABLE_OR_DROP_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalMarkSellable() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.LEATHER.generate(1f), 10);
		
		assertEquals(Actions.MARK_INVENTORY_ITEM_AS_SELLABLE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalDropItem() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.LEATHER.generate(1f), 10);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_CUIRASS.generate(1f));
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_GREATAXE.generate(1f));
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_GREATSWORD.generate(1f));
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_SHIELD.generate(1f));
		
		assertEquals(Actions.DROP_ITEM_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		
		assertEquals(true, goal.isGoalMet(performer, world));
	}
	
	@Test
	public void testIsGoalMetMarkedSellable() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.LEATHER.generate(1f), 10);
		performer.getProperty(Constants.INVENTORY).get(0).setProperty(Constants.SELLABLE, Boolean.TRUE);
		
		assertEquals(true, goal.isGoalMet(performer, world));
	}
	
	@Test
	public void testIsGoalMetNotMarkedSellable() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.LEATHER.generate(1f), 10);
		
		assertEquals(false, goal.isGoalMet(performer, world));
	}
	
	@Test
	public void testIsGoalMetOverburdened() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.LEATHER.generate(1f), 10);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_CUIRASS.generate(1f));
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_GREATAXE.generate(1f));
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_GREATSWORD.generate(1f));
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.IRON_SHIELD.generate(1f));
		
		assertEquals(false, goal.isGoalMet(performer, world));
	}
}