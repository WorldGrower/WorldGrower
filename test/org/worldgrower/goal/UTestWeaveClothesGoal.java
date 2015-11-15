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

public class UTestWeaveClothesGoal {

	private WeaveClothesGoal goal = Goals.WEAVE_CLOTHES_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		assertEquals(Actions.PLANT_COTTON_PLANT_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalCottonShirt() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer();
		
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		performerInventory.addQuantity(Constants.COTTON, 20, null);
		
		assertEquals(Actions.WEAVE_COTTON_SHIRT_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalCottonPants() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer();
		
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		performerInventory.addQuantity(Constants.COTTON, 20, null);
		performerInventory.addQuantity(Item.COTTON_SHIRT.generate(1f));
		
		assertEquals(Actions.WEAVE_COTTON_PANTS_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalCottonBoots() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer();
		
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		performerInventory.addQuantity(Constants.COTTON, 20, null);
		performerInventory.addQuantity(Item.COTTON_SHIRT.generate(1f));
		performerInventory.addQuantity(Item.COTTON_PANTS.generate(1f));
		
		assertEquals(Actions.WEAVE_COTTON_BOOTS_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}

	@Test
	public void testCalculateGoalCottonBootsNothing() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer();
		
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		performerInventory.addQuantity(Constants.COTTON, 20, null);
		performerInventory.addQuantity(Item.COTTON_SHIRT.generate(1f));
		performerInventory.addQuantity(Item.COTTON_PANTS.generate(1f));
		performerInventory.addQuantity(Item.COTTON_BOOTS.generate(1f));
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer();
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		
		for(int i=0; i<10; i++) {
			performerInventory.addQuantity(Item.COTTON_SHIRT.generate(1f));
		}
		
		assertEquals(true, goal.isGoalMet(performer, world));
	}
	
	private WorldObject createPerformer() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.INVENTORY, new WorldObjectContainer());
		return performer;
	}
}