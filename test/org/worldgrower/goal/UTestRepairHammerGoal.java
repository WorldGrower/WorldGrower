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
import org.worldgrower.generator.PlantGenerator;
import org.worldgrower.generator.TerrainGenerator;

public class UTestRepairHammerGoal {

	private RepairHammerGoal goal = Goals.REPAIR_HAMMER_GOAL;

	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());

		assertEquals(null, goal.calculateGoal(performer, world));
	}

	@Test
	public void testCalculateNotEnoughWood() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		
		PlantGenerator.generateTree(0, 0, world);
		
		assertEquals(Actions.CUT_WOOD_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateNotEnoughOre() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		
		TerrainGenerator.generateOreResource(0, 0, world);
		
		assertEquals(Actions.MINE_ORE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}

	@Test
	public void testCalculateGoalCraftRepairHammer() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());

		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.ORE.generate(1f), 20);

		assertEquals(Actions.CRAFT_REPAIR_HAMMER_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.REPAIR_HAMMER.generate(1f), 20);
		
		assertEquals(true, goal.isGoalMet(performer, world));
	}
}