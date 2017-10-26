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
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.BuildingList;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;
import org.worldgrower.generator.PlantGenerator;

public class UTestFurnitureGoal {

	private FurnitureGoal goal = Goals.FURNITURE_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		createVillagersOrganization(world);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalNoHouse() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.STONE.generate(1f), 20);
		
		createVillagersOrganization(world);
		
		assertEquals(Actions.BUILD_HOUSE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalWood() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		
		addHouse(performer, world);
		PlantGenerator.generateOldTree(0, 0, world);
		createVillagersOrganization(world);
		
		assertEquals(Actions.CUT_WOOD_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}

	@Test
	public void testCalculateGoalBuildWorkbench() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		createVillagersOrganization(world);
		addHouse(performer, world);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.STONE.generate(1f), 20);
		
		assertEquals(Actions.BUILD_WORKBENCH_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalConstructBed() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(7);
		world.addWorldObject(performer);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		createVillagersOrganization(world);
		addHouse(performer, world);
		addWorkbench(performer, world);
		
		assertEquals(Actions.CONSTRUCT_BED_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalConstructKitchen() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(7);
		world.addWorldObject(performer);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		createVillagersOrganization(world);
		WorldObject house = addHouse(performer, world);
		house.getProperty(Constants.INVENTORY).addQuantity(Item.BED.generate(1f));
		addWorkbench(performer, world);
		
		assertEquals(Actions.CONSTRUCT_KITCHEN_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	private void addWorkbench(WorldObject performer, World world) {
		int workbenchId = BuildingGenerator.generateWorkbench(0, 0, world, performer);
		performer.getProperty(Constants.BUILDINGS).add(workbenchId, BuildingType.WORKBENCH);
	}
	
	private WorldObject addHouse(WorldObject performer, World world) {
		int houseId = BuildingGenerator.generateHouse(5, 5, world, performer);
		performer.getProperty(Constants.BUILDINGS).add(houseId, BuildingType.HOUSE);
		
		return world.findWorldObjectById(houseId);
	}
	
	@Test
	public void testCalculateGoalPutBedIntoHouseNull() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.getProperty(Constants.INVENTORY).add(Item.BED.generate(1f));
		performer.setProperty(Constants.BUILDINGS, new BuildingList());
		performer.setProperty(Constants.GOLD, 0);
		
		createVillagersOrganization(world);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalPutBedIntoHouse() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.getProperty(Constants.INVENTORY).add(Item.BED.generate(1f));
		performer.setProperty(Constants.BUILDINGS, new BuildingList());
		performer.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		Actions.BUILD_HOUSE_ACTION.execute(performer, performer, Args.EMPTY, world);
		
		assertEquals(Actions.PUT_ITEM_INTO_INVENTORY_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalPutKitchenIntoHouse() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.getProperty(Constants.INVENTORY).add(Item.KITCHEN.generate(1f));
		performer.setProperty(Constants.BUILDINGS, new BuildingList());
		performer.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		Actions.BUILD_HOUSE_ACTION.execute(performer, performer, Args.EMPTY, world);
		WorldObject house = world.getWorldObjects().get(world.getWorldObjects().size() - 1);
		house.getProperty(Constants.INVENTORY).addQuantity(Item.BED.generate(1f));
		
		assertEquals(Actions.PUT_ITEM_INTO_INVENTORY_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalFurnitureSet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.BUILDINGS, new BuildingList());
		
		WorldObject house = addHouse(performer, world);
		house.getProperty(Constants.INVENTORY).addQuantity(Item.BED.generate(1f));
		house.getProperty(Constants.INVENTORY).addQuantity(Item.KITCHEN.generate(1f));
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.BUILDINGS, new BuildingList());
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		WorldObject house = addHouse(performer, world);
		house.getProperty(Constants.INVENTORY).add(Item.BED.generate(1f));
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		house.getProperty(Constants.INVENTORY).add(Item.KITCHEN.generate(1f));
		assertEquals(true, goal.isGoalMet(performer, world));
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
	
	private WorldObject createVillagersOrganization(World world) {
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId();
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		return organization;
	}
}