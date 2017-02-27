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
import org.worldgrower.DefaultConversationFormatter;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.BuildingList;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;
import org.worldgrower.generator.PlantGenerator;
import org.worldgrower.generator.TerrainGenerator;

public class UTestCreateScytheGoal {

	private CreateScytheGoal goal = Goals.CREATE_SCYTHE_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.BUILDINGS, new BuildingList());
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalWood() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		PlantGenerator.generateTree(5, 5, world);
		
		addWorkbench(world, performer);
		
		assertEquals(Actions.CUT_WOOD_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalBuildWorkbench() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.STONE.generate(1f), 20);
		performer.setProperty(Constants.BUILDINGS, new BuildingList());
		
		assertEquals(Actions.BUILD_WORKBENCH_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalMineOre() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		
		addWorkbench(world, performer);
		
		TerrainGenerator.generateOreResource(0, 0, world);
		
		assertEquals(Actions.MINE_ORE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalConstructScythe() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.ORE.generate(1f), 20);
		
		addWorkbench(world, performer);
		
		assertEquals(Actions.CONSTRUCT_SCYTHE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}

	private void addWorkbench(World world, WorldObject performer) {
		int workbenchId = BuildingGenerator.generateWorkbench(0, 0, world, performer);
		performer.setProperty(Constants.BUILDINGS, new BuildingList().add(workbenchId, BuildingType.WORKBENCH));
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.SCYTHE.generate(1f), 10);
		assertEquals(true, goal.isGoalMet(performer, world));
	}
	
	@Test
	public void testGetDescription() {
		assertEquals("looking for a scythe", DefaultConversationFormatter.FORMATTER.format(goal.getDescription()));
	}
	
	private WorldObject createPerformer() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}