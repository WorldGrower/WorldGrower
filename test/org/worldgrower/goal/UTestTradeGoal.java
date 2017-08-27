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

import org.junit.Assert;
import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.Demands;
import org.worldgrower.generator.Item;
import org.worldgrower.profession.Professions;

public class UTestTradeGoal {

	private final TradeGoal goal;
	
	public UTestTradeGoal() {
		Demands demands = new Demands();
		demands.add(Constants.FOOD, 1);
		goal = new TradeGoal(demands);
	}
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalSellItem() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		world.addWorldObject(target);
		
		addSellableBerriesToInventory(performer);
		
		target.getProperty(Constants.DEMANDS).add(Constants.FOOD, 5);
		target.setProperty(Constants.GOLD, 1000);
		
		assertEquals(Actions.SELL_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(target, goal.calculateGoal(performer, world).getTarget());
		int quantity = 20;
		Assert.assertArrayEquals(new int[] { 0, 1, quantity, Item.BERRIES.ordinal() }, goal.calculateGoal(performer, world).getArgs());
	}

	private void addSellableBerriesToInventory(WorldObject performer) {
		WorldObject berries = Item.BERRIES.generate(1f);
		berries.setProperty(Constants.SELLABLE, true);
		performer.getProperty(Constants.INVENTORY).addQuantity(berries, 20);
	}
	
	@Test
	public void testCalculateGoalBuyItem() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		world.addWorldObject(target);
		
		addSellableBerriesToInventory(target);
		
		performer.setProperty(Constants.PROFESSION, Professions.MERCHANT_PROFESSION);
		performer.setProperty(Constants.GOLD, 1000);
		
		assertEquals(Actions.BUY_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(target, goal.calculateGoal(performer, world).getTarget());
		int quantity = 20;
		Assert.assertArrayEquals(new int[] { 0, 1, quantity, Item.BERRIES.ordinal() }, goal.calculateGoal(performer, world).getArgs());
	}
	
	@Test
	public void testCalculateGoalBuyItemCheckNull() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		world.addWorldObject(target);
		
		addSellableBerriesToInventory(target);
		target.getProperty(Constants.INVENTORY).remove(0);
		
		performer.setProperty(Constants.PROFESSION, Professions.MERCHANT_PROFESSION);
		performer.setProperty(Constants.GOLD, 1000);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}

	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createIntelligentWorldObject(id, Goals.FOOD_GOAL);
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}