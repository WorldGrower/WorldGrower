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
package org.worldgrower;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.worldgrower.TestUtils.createWorldObject;

import java.util.List;

import org.junit.Test;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.CutWoodAction;
import org.worldgrower.actions.TalkAction;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.PlantGenerator;

public class UTestTaskCalculator {

	private TaskCalculator taskCalculator = new TaskCalculatorImpl();
	
	@Test
	public void testPathFindingNoObstacle() {
		WorldObject performer = createWorldObject(5, 5, 1, 1, Constants.ID, 2);
		WorldObject target = createWorldObject(2, 2, 1, 1, Constants.ID, 3);
		World world = createWorld();
		world.addWorldObject(performer);
		world.addWorldObject(target);
		List<OperationInfo> tasks = taskCalculator.calculateTask(performer, world, new OperationInfo(performer, target, Args.EMPTY, new CutWoodAction()));
		
		assertEquals(3, tasks.size());
		assertContains(tasks.get(0).toString(), "args=[-1, -1]");
		assertContains(tasks.get(1).toString(), "args=[-1, -1]");
		assertContains(tasks.get(2).toString(), CutWoodAction.class.getName());
	}

	private WorldImpl createWorld() {
		return new WorldImpl(10, 10, null, null);
	}
	
	@Test
	public void testPathFindingObstacle() {
		WorldObject performer = createWorldObject(5, 5, 1, 1, Constants.ID, 2);
		WorldObject target = createWorldObject(2, 2, 1, 1, Constants.ID, 3);
		WorldObject obstacle = createWorldObject(3, 3, 1, 1, Constants.ID, 4);
		World world = createWorld();
		world.addWorldObject(performer);
		world.addWorldObject(target);
		world.addWorldObject(obstacle);
		List<OperationInfo> tasks = taskCalculator.calculateTask(performer, world, new OperationInfo(performer, target, Args.EMPTY, new CutWoodAction()));
		
		assertEquals(4, tasks.size());
		assertContains(tasks.get(0).toString(), "args=[-1, -1]");
		assertContains(tasks.get(1).toString(), "args=[-1, 0]");
		assertContains(tasks.get(2).toString(), "args=[-1, -1]");
		assertContains(tasks.get(3).toString(), CutWoodAction.class.getName());
	}
	
	@Test
	public void testPathFindingTrickyObstacle() {
		WorldObject performer = createWorldObject(2, 4, 1, 1, Constants.ID, 2);
		WorldObject target = createWorldObject(2, 2, 1, 1, Constants.ID, 3);
		WorldObject obstacle1 = createWorldObject(1, 3, 1, 1, Constants.ID, 4);
		WorldObject obstacle2 = createWorldObject(2, 3, 1, 1, Constants.ID, 5);
		WorldObject obstacle3 = createWorldObject(3, 3, 1, 1, Constants.ID, 6);
		World world = createWorld();
		world.addWorldObject(performer);
		world.addWorldObject(target);
		world.addWorldObject(obstacle1);
		world.addWorldObject(obstacle2);
		world.addWorldObject(obstacle3);
		List<OperationInfo> tasks = taskCalculator.calculateTask(performer, world, new OperationInfo(performer, target, Args.EMPTY, new CutWoodAction()));
		
		assertEquals(4, tasks.size());
		assertContains(tasks.get(0).toString(), "args=[-1, 0]");
		assertContains(tasks.get(1).toString(), "args=[-1, -1]");
		assertContains(tasks.get(2).toString(), "args=[1, -1]");
		assertContains(tasks.get(3).toString(), CutWoodAction.class.getName());
	}
	
	@Test
	public void testPathFindingUShapedObstacle() {
		WorldObject performer = createWorldObject(0, 10, 1, 1, Constants.ID, 2);
		WorldObject target = createWorldObject(0, 5, 2, 2, Constants.ID, 3);
		WorldObject obstacle1 = createWorldObject(0, 7, 2, 2, Constants.ID, 4);
		WorldObject obstacle2 = createWorldObject(1, 9, 2, 2, Constants.ID, 5);

		World world = new WorldImpl(15, 15, null, null);
		world.addWorldObject(performer);
		world.addWorldObject(target);
		world.addWorldObject(obstacle1);
		world.addWorldObject(obstacle2);
		List<OperationInfo> tasks = taskCalculator.calculateTask(performer, world, new OperationInfo(performer, target, Args.EMPTY, new CutWoodAction()));
		
		assertEquals(7, tasks.size());
		assertContains(tasks.get(0).toString(), "args=[1, 1]");
		assertContains(tasks.get(1).toString(), "args=[1, 0]");
		assertContains(tasks.get(2).toString(), "args=[1, -1]");
		assertContains(tasks.get(3).toString(), "args=[0, -1]");
		assertContains(tasks.get(4).toString(), "args=[-1, -1]");
		assertContains(tasks.get(6).toString(), CutWoodAction.class.getName());
	}
	
	@Test
	public void testPathFindingPassableObstacle() {
		WorldObject performer = createWorldObject(5, 5, 1, 1, Constants.ID, 2);
		WorldObject target = createWorldObject(2, 2, 1, 1, Constants.ID, 3);
		WorldObject obstacle = createWorldObject(3, 3, 1, 1, Constants.ID, 4);
		obstacle.setProperty(Constants.PASSABLE, Boolean.TRUE);
		World world = createWorld();
		world.addWorldObject(performer);
		world.addWorldObject(target);
		world.addWorldObject(obstacle);
		List<OperationInfo> tasks = taskCalculator.calculateTask(performer, world, new OperationInfo(performer, target, Args.EMPTY, new CutWoodAction()));
		
		assertEquals(3, tasks.size());
		assertContains(tasks.get(0).toString(), "args=[-1, -1]");
		assertContains(tasks.get(1).toString(), "args=[-1, -1]");
		assertContains(tasks.get(2).toString(), CutWoodAction.class.getName());
	}
	
	@Test
	public void testPathFindingLongerDistance() {
		WorldObject performer = createWorldObject(11, 1, 1, 1, Constants.ID, 2);
		WorldObject target = createWorldObject(7, 3, 1, 1, Constants.ID, 3);
		WorldObject obstacle1 = createWorldObject(8, 1, 1, 1, Constants.ID, 4);
		WorldObject obstacle2 = createWorldObject(8, 2, 1, 1, Constants.ID, 5);
		WorldObject obstacle3 = createWorldObject(9, 2, 1, 1, Constants.ID, 6);

		World world = new WorldImpl(30, 30, null, null);
		world.addWorldObject(performer);
		world.addWorldObject(target);
		world.addWorldObject(obstacle1);
		world.addWorldObject(obstacle2);
		world.addWorldObject(obstacle3);
		List<OperationInfo> tasks = taskCalculator.calculateTask(performer, world, new OperationInfo(performer, target, Args.EMPTY, new CutWoodAction()));
		
		assertEquals(4, tasks.size());
		assertContains(tasks.get(0).toString(), "args=[-1, 1]");
		assertContains(tasks.get(1).toString(), "args=[-1, 1]");
		assertContains(tasks.get(2).toString(), "args=[-1, 0]");
		assertContains(tasks.get(3).toString(), CutWoodAction.class.getName());
	}
	
	@Test
	public void testPathFindingLongerDiagonalDistance() {
		WorldObject performer = createWorldObject(6, 0, 1, 1, Constants.ID, 2);
		WorldObject target = createWorldObject(19, 11, 1, 1, Constants.ID, 3);

		World world = new WorldImpl(30, 30, null, null);
		world.addWorldObject(performer);
		world.addWorldObject(target);
		List<OperationInfo> tasks = taskCalculator.calculateTask(performer, world, new OperationInfo(performer, target, Args.EMPTY, new CutWoodAction()));
		
		assertEquals(13, tasks.size());
		assertContains(tasks.get(0).toString(), "args=[1, 1]");
		assertContains(tasks.get(1).toString(), "args=[1, 1]");
		assertContains(tasks.get(2).toString(), "args=[1, 1]");
		assertContains(tasks.get(12).toString(), CutWoodAction.class.getName());
	}
	
	@Test
	public void testPathFindingNoPath() {
		WorldObject performer = createWorldObject(0, 0, 1, 1, Constants.ID, 2);
		WorldObject target = createWorldObject(2, 2, 1, 1, Constants.ID, 3);
		
		World world = new WorldImpl(10, 10, null, null);
		world.addWorldObject(performer);
		world.addWorldObject(target);
		
		addBerryBushPrison(world);
		
		List<OperationInfo> tasks = taskCalculator.calculateTask(performer, world, new OperationInfo(performer, target, Args.EMPTY, new TalkAction()));
		assertEquals(0, tasks.size());
	}
	
	@Test
	public void testPathFindingNoPathOverLongDistance() {
		WorldObject performer = createWorldObject(25, 25, 1, 1, Constants.ID, 2);
		WorldObject target = createWorldObject(2, 2, 1, 1, Constants.ID, 3);
		
		World world = new WorldImpl(30, 30, null, null);
		world.addWorldObject(performer);
		world.addWorldObject(target);
		
		addBerryBushPrison(world);
		
		List<OperationInfo> tasks = taskCalculator.calculateTask(performer, world, new OperationInfo(performer, target, Args.EMPTY, new TalkAction()));
		assertEquals(0, tasks.size());
	}
	
	@Test
	public void testPathFindingNoPathOnLargeMap() {
		WorldObject performer = createWorldObject(25, 25, 1, 1, Constants.ID, 2);
		WorldObject target = createWorldObject(2, 2, 1, 1, Constants.ID, 3);
		
		World world = new WorldImpl(100, 100, null, null);
		world.addWorldObject(performer);
		world.addWorldObject(target);
		
		addBerryBushPrison(world);
		
		List<OperationInfo> tasks = taskCalculator.calculateTask(performer, world, new OperationInfo(performer, target, Args.EMPTY, new TalkAction()));
		assertEquals(0, tasks.size());
	}

	private void addBerryBushPrison(World world) {
		PlantGenerator.generateBerryBush(1, 1, world);
		PlantGenerator.generateBerryBush(1, 2, world);
		PlantGenerator.generateBerryBush(1, 3, world);
		PlantGenerator.generateBerryBush(2, 1, world);
		PlantGenerator.generateBerryBush(3, 1, world);
		PlantGenerator.generateBerryBush(3, 2, world);
		PlantGenerator.generateBerryBush(3, 3, world);
		PlantGenerator.generateBerryBush(2, 3, world);
	}
	
	private static void assertContains(String value, String substring) {
		assertTrue(value + " doesn't contain substring " + substring, value.contains(substring));
	}
}
