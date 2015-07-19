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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.MetaInformation;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.curse.Curse;
import org.worldgrower.history.Turn;

public class UTestDungeonMaster {

	private final DungeonMaster dungeonMaster;
	
	public UTestDungeonMaster() {
		this.dungeonMaster = new DungeonMaster();
		this.dungeonMaster.setTaskCalculator(new MockTaskCalculator());
	}
	
	@Test
	public void testGetImmediateGoalNoImmediateGoal() {
		World world = new WorldImpl(10, 10, null);
		WorldObject worldObject = createWorldObject();
		world.addWorldObject(worldObject);
		
		assertEquals(null, dungeonMaster.getImmediateGoal(worldObject, world));
	}

	@Test
	public void testGetImmediateGoalAnImmediateGoal() {
		World world = new WorldImpl(10, 10, null);
		WorldObject worldObject = createWorldObject();
		OperationInfo operationInfo = new OperationInfo(worldObject, worldObject, new int[0], Actions.CUT_WOOD_ACTION);
		worldObject.getProperty(Constants.META_INFORMATION).setCurrentTask(Arrays.asList(operationInfo));
		world.addWorldObject(worldObject);
		
		OperationInfo actualImmediateGoal = dungeonMaster.getImmediateGoal(worldObject, world);
		assertEquals("OperationInfo [args=[], managedOperation=org.worldgrower.actions.CutWoodAction]", actualImmediateGoal.toShortString());
	}
	
	@Test
	public void testGetImmediateGoalFromHistory() {
		World world = new WorldImpl(10, 10, null);
		WorldObject worldObject = createWorldObject();
		world.addWorldObject(worldObject);
		OperationInfo operationInfo = new OperationInfo(worldObject, worldObject, new int[0], Actions.CUT_WOOD_ACTION);
		world.getHistory().actionPerformed(operationInfo, new Turn());
		
		assertEquals("OperationInfo [args=[], managedOperation=org.worldgrower.actions.CutWoodAction]", dungeonMaster.getImmediateGoal(worldObject, world).toShortString());
	}
	
	@Test
	public void testCalculateTasksWorldObjectCanMove() {
		World world = new WorldImpl(10, 10, null);
		WorldObject worldObject = createWorldObject();
		world.addWorldObject(worldObject);
		OperationInfo immediateGoal = new OperationInfo(worldObject, worldObject, new int[0], Actions.CUT_WOOD_ACTION);
		List<OperationInfo> tasks = dungeonMaster.calculateTasks(worldObject, world, immediateGoal);
		assertEquals(2, tasks.size());
		assertEquals("OperationInfo [args=[], managedOperation=org.worldgrower.actions.CutWoodAction]", tasks.get(0).toShortString());
		assertEquals("OperationInfo [args=[1, 1], managedOperation=org.worldgrower.actions.MoveAction]", tasks.get(1).toShortString());
	}
	
	@Test
	public void testCalculateTasksWorldObjectCanNotMove() {
		World world = new WorldImpl(10, 10, null);
		WorldObject worldObject = createWorldObject();
		worldObject.setProperty(Constants.CURSE, Curse.SIREN_CURSE);
		world.addWorldObject(worldObject);
		OperationInfo immediateGoal = new OperationInfo(worldObject, worldObject, new int[0], Actions.CUT_WOOD_ACTION);
		List<OperationInfo> tasks = dungeonMaster.calculateTasks(worldObject, world, immediateGoal);
		assertEquals(0, tasks.size());
	}
	
	private WorldObject createWorldObject() {
		WorldObject worldObject = TestUtils.createWorldObject(1, "test");
		MetaInformation metaInformation = new MetaInformation(worldObject);
		worldObject.setProperty(Constants.META_INFORMATION, metaInformation);
		return worldObject;
	}
}
