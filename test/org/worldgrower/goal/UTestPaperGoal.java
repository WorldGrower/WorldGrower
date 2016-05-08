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
import org.worldgrower.attribute.BuildingList;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.Item;
import org.worldgrower.generator.PlantGenerator;

public class UTestPaperGoal {

	private PaperGoal goal = Goals.PAPER_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer();
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalWood() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		PlantGenerator.generateTree(5, 5, world);
		
		assertEquals(Actions.CUT_WOOD_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalCollectWater() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		BuildingGenerator.buildWell(5, 5, world, 1f);
		
		assertEquals(Actions.COLLECT_WATER_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalCollectWood() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WATER.generate(1f), 20);
		PlantGenerator.generateTree(5, 5, world);
		
		assertEquals(Actions.CUT_WOOD_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalCreatePaperMill() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WATER.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		
		assertEquals(Actions.BUILD_PAPER_MILL_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalCreatePaper() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer();
		
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WATER.generate(1f), 20);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WOOD.generate(1f), 20);
		
		addPaperMill(world, performer);
		
		assertEquals(Actions.CREATE_PAPER_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}

	private void addPaperMill(World world, WorldObject performer) {
		int paperMillId = BuildingGenerator.generatePaperMill(0, 0, world);
		performer.setProperty(Constants.BUILDINGS, new BuildingList().add(paperMillId, BuildingType.PAPERMILL));
	}
	
	private WorldObject createPerformer() {
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		return performer;
	}
}