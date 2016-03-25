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

public class UTestBuyClothesGoal {

	private BuyClothesGoal goal = Goals.BUY_CLOTHES_GOAL;
	
	@Test
	public void testCalculateGoalNoSellers() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalOneSeller() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		world.addWorldObject(target);
		performer.setProperty(Constants.GOLD, 2000);
		WorldObject cottonShirt = Item.COTTON_SHIRT.generate(1f);
		cottonShirt.setProperty(Constants.SELLABLE, Boolean.TRUE);
		target.getProperty(Constants.INVENTORY).add(cottonShirt);
		
		assertEquals(Actions.BUY_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalOnePantsSeller() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		world.addWorldObject(target);
		performer.setProperty(Constants.GOLD, 2000);
		
		WorldObject cottonShirt = Item.COTTON_SHIRT.generate(1f);
		performer.getProperty(Constants.INVENTORY).add(cottonShirt);
		
		WorldObject cottonPants = Item.COTTON_PANTS.generate(1f);
		cottonPants.setProperty(Constants.SELLABLE, Boolean.TRUE);
		target.getProperty(Constants.INVENTORY).add(cottonPants);
		
		assertEquals(Actions.BUY_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testIsGoalMetNoTargets() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());

		assertEquals(true, goal.isGoalMet(performer, world));
	}
}
