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
import org.worldgrower.OperationInfo;
import org.worldgrower.TaskCalculator;
import org.worldgrower.TaskCalculatorImpl;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.CutWoodAction;

public class UTestTaskCalculator {

	private TaskCalculator taskCalculator = new TaskCalculatorImpl();
	
	@Test
	public void testPathFindingNoObstacle() {
		WorldObject performer = createWorldObject(5, 5, 1, 1);
		WorldObject target = createWorldObject(2, 2, 1, 1);
		World world = createWorld();
		world.addWorldObject(performer);
		world.addWorldObject(target);
		List<OperationInfo> tasks = taskCalculator.calculateTask(performer, world, new OperationInfo(performer, target, new int[0], new CutWoodAction()));
		
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
		WorldObject performer = createWorldObject(5, 5, 1, 1);
		WorldObject target = createWorldObject(2, 2, 1, 1);
		WorldObject obstacle = createWorldObject(3, 3, 1, 1);
		World world = createWorld();
		world.addWorldObject(performer);
		world.addWorldObject(target);
		world.addWorldObject(obstacle);
		List<OperationInfo> tasks = taskCalculator.calculateTask(performer, world, new OperationInfo(performer, target, new int[0], new CutWoodAction()));
		
		assertEquals(4, tasks.size());
		assertContains(tasks.get(0).toString(), "args=[-1, -1]");
		assertContains(tasks.get(1).toString(), "args=[-1, 0]");
		assertContains(tasks.get(2).toString(), "args=[-1, -1]");
		assertContains(tasks.get(3).toString(), CutWoodAction.class.getName());
	}
	
	@Test
	public void testPathFindingTrickyObstacle() {
		WorldObject performer = createWorldObject(2, 4, 1, 1);
		WorldObject target = createWorldObject(2, 2, 1, 1);
		WorldObject obstacle1 = createWorldObject(1, 3, 1, 1);
		WorldObject obstacle2 = createWorldObject(2, 3, 1, 1);
		WorldObject obstacle3 = createWorldObject(3, 3, 1, 1);
		World world = createWorld();
		world.addWorldObject(performer);
		world.addWorldObject(target);
		world.addWorldObject(obstacle1);
		world.addWorldObject(obstacle2);
		world.addWorldObject(obstacle3);
		List<OperationInfo> tasks = taskCalculator.calculateTask(performer, world, new OperationInfo(performer, target, new int[0], new CutWoodAction()));
		
		assertEquals(4, tasks.size());
		assertContains(tasks.get(0).toString(), "args=[-1, 0]");
		assertContains(tasks.get(1).toString(), "args=[-1, -1]");
		assertContains(tasks.get(2).toString(), "args=[1, -1]");
		assertContains(tasks.get(3).toString(), CutWoodAction.class.getName());
	}
	
	@Test
	public void testPathFindingUShapedObstacle() {
		WorldObject performer = createWorldObject(0, 10, 1, 1);
		WorldObject target = createWorldObject(0, 5, 2, 2);
		WorldObject obstacle1 = createWorldObject(0, 7, 2, 2);
		WorldObject obstacle2 = createWorldObject(1, 9, 2, 2);

		World world = new WorldImpl(15, 15, null, null);
		world.addWorldObject(performer);
		world.addWorldObject(target);
		world.addWorldObject(obstacle1);
		world.addWorldObject(obstacle2);
		List<OperationInfo> tasks = taskCalculator.calculateTask(performer, world, new OperationInfo(performer, target, new int[0], new CutWoodAction()));
		
		assertEquals(7, tasks.size());
		assertContains(tasks.get(0).toString(), "args=[1, 1]");
		assertContains(tasks.get(1).toString(), "args=[1, 0]");
		assertContains(tasks.get(2).toString(), "args=[1, -1]");
		assertContains(tasks.get(3).toString(), "args=[0, -1]");
		assertContains(tasks.get(4).toString(), "args=[-1, -1]");
		assertContains(tasks.get(6).toString(), CutWoodAction.class.getName());
	}
	
	private static void assertContains(String value, String substring) {
		assertTrue(value + " doesn't contain substring " + substring, value.contains(substring));
	}
}
