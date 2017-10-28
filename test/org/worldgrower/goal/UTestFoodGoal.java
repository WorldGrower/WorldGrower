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
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.FoodCooker;
import org.worldgrower.generator.Item;
import org.worldgrower.generator.PlantGenerator;

public class UTestFoodGoal {

	private FoodGoal goal = Goals.FOOD_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer();
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalInventoryFood() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.BERRIES.generate(1f));
		
		assertEquals(Actions.EAT_FROM_INVENTORY_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalCookInventoryFood() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.BERRIES.generate(1f));
		
		WorldObject house = addHouse(performer, world);
		house.getProperty(Constants.INVENTORY).addQuantity(Item.KITCHEN.generate(1f));
		
		assertEquals(Actions.COOK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalEatCookedInventoryFood() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.BERRIES.generate(1f));
		WorldObject cookedBerries = Item.BERRIES.generate(1f);
		FoodCooker.cook(cookedBerries);
		performer.getProperty(Constants.INVENTORY).addQuantity(cookedBerries);
		
		WorldObject house = addHouse(performer, world);
		house.getProperty(Constants.INVENTORY).addQuantity(Item.KITCHEN.generate(1f));
		
		assertEquals(Actions.EAT_FROM_INVENTORY_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(1, goal.calculateGoal(performer, world).getArgs()[0]);
	}
	
	@Test
	public void testCalculateGoalEatTarget() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		int berryBushId = PlantGenerator.generateBerryBush(5, 5, world);
		WorldObject berryBush = world.findWorldObjectById(berryBushId);
		berryBush.getProperty(Constants.FOOD_SOURCE).increaseFoodAmount(500, berryBush, world);
		
		assertEquals(Actions.EAT_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		performer.setProperty(Constants.FOOD, 0);
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		performer.setProperty(Constants.FOOD, 1000);
		assertEquals(true, goal.isGoalMet(performer, world));
	}

	private WorldObject createPerformer() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
	
	private WorldObject addHouse(WorldObject performer, World world) {
		int houseId = BuildingGenerator.generateHouse(5, 5, world, performer);
		performer.getProperty(Constants.BUILDINGS).add(houseId, BuildingType.HOUSE);
		
		return world.findWorldObjectById(houseId);
	}
}