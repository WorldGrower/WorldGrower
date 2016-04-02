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
import org.worldgrower.actions.Actions;
import org.worldgrower.curse.Curse;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.PlantGenerator;
import org.worldgrower.goal.Goals;
import org.worldgrower.history.Turn;
import org.worldgrower.personality.Personality;

public class UTestDungeonMaster {

	private final DungeonMaster dungeonMaster;
	
	public UTestDungeonMaster() {
		this.dungeonMaster = new DungeonMaster();
		this.dungeonMaster.setTaskCalculator(new MockTaskCalculator());
	}
	
	@Test
	public void testGetImmediateGoalNoImmediateGoal() {
		World world = createWorld();
		WorldObject worldObject = createWorldObject();
		world.addWorldObject(worldObject);
		
		assertEquals(null, dungeonMaster.getImmediateGoal(worldObject, world));
	}

	private WorldImpl createWorld() {
		return new WorldImpl(10, 10, dungeonMaster, null);
	}
	
	private WorldImpl createWorld(DungeonMaster dungeonMaster) {
		return new WorldImpl(10, 10, dungeonMaster, null);
	}

	@Test
	public void testGetImmediateGoalAnImmediateGoal() {
		World world = createWorld();
		WorldObject worldObject = createWorldObject();
		OperationInfo operationInfo = new OperationInfo(worldObject, worldObject, Args.EMPTY, Actions.CUT_WOOD_ACTION);
		worldObject.getProperty(Constants.META_INFORMATION).setCurrentTask(Arrays.asList(operationInfo), GoalChangedReason.EMPTY_META_INFORMATION);
		world.addWorldObject(worldObject);
		
		OperationInfo actualImmediateGoal = dungeonMaster.getImmediateGoal(worldObject, world);
		assertEquals("[CutWoodAction([])]", actualImmediateGoal.toShortString());
	}
	
	@Test
	public void testGetImmediateGoalFromHistory() {
		World world = createWorld();
		WorldObject worldObject = createWorldObject();
		world.addWorldObject(worldObject);
		OperationInfo operationInfo = new OperationInfo(worldObject, worldObject, Args.EMPTY, Actions.CUT_WOOD_ACTION);
		world.getHistory().actionPerformed(operationInfo, new Turn());
		
		assertEquals("[CutWoodAction([])]", dungeonMaster.getImmediateGoal(worldObject, world).toShortString());
	}
	
	@Test
	public void testCalculateTasksWorldObjectCanMove() {
		World world = createWorld();
		WorldObject worldObject = createWorldObject();
		world.addWorldObject(worldObject);
		OperationInfo immediateGoal = new OperationInfo(worldObject, worldObject, Args.EMPTY, Actions.CUT_WOOD_ACTION);
		List<OperationInfo> tasks = dungeonMaster.calculateTasks(worldObject, world, immediateGoal);
		assertEquals(2, tasks.size());
		assertEquals("[CutWoodAction([])]", tasks.get(0).toShortString());
		assertEquals("[MoveAction([1, 1])]", tasks.get(1).toShortString());
	}
	
	@Test
	public void testCalculateTasksWorldObjectCanNotMove() {
		World world = createWorld();
		WorldObject worldObject = createWorldObject();
		worldObject.setProperty(Constants.CURSE, Curse.SIREN_CURSE);
		world.addWorldObject(worldObject);
		OperationInfo immediateGoal = new OperationInfo(worldObject, worldObject, Args.EMPTY, Actions.CUT_WOOD_ACTION);
		List<OperationInfo> tasks = dungeonMaster.calculateTasks(worldObject, world, immediateGoal);
		assertEquals(0, tasks.size());
	}
	
	@Test
	public void testRunWorldObjectWithInvalidTarget() {
		DungeonMaster dungeonMaster = new DungeonMaster();
		World world = createWorld(dungeonMaster);
		WorldObject commoner = TestUtils.createIntelligentWorldObject(1, Goals.DRINK_WATER_GOAL);
		commoner.setProperty(Constants.NAME, "performer");
		commoner.setProperty(Constants.PERSONALITY, new Personality());
		commoner.setProperty(Constants.X, 5);
		commoner.setProperty(Constants.Y, 5);
		world.addWorldObject(commoner);
		int wellId = BuildingGenerator.buildWell(2, 2, world, 0f);
		PlantGenerator.generateTree(8, 8, world);
		
		dungeonMaster.runWorldObject(commoner, world);
		
		assertEquals(4, commoner.getProperty(Constants.X).intValue());
		assertEquals(4, commoner.getProperty(Constants.Y).intValue());
		assertEquals(Goals.DRINK_WATER_GOAL, world.getGoal(commoner));
		assertEquals(Actions.DRINK_ACTION, world.getImmediateGoal(commoner, world).getManagedOperation());
		
		commoner.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(wellId, Constants.POISON_DAMAGE, 5);
		
		dungeonMaster.runWorldObject(commoner, world);

		assertEquals(5, commoner.getProperty(Constants.X).intValue());
		assertEquals(5, commoner.getProperty(Constants.Y).intValue());
		assertEquals(Goals.DRINK_WATER_GOAL, world.getGoal(commoner));
		assertEquals(Actions.CUT_WOOD_ACTION, world.getImmediateGoal(commoner, world).getManagedOperation());

	}
	
	private WorldObject createWorldObject() {
		WorldObject worldObject = TestUtils.createWorldObject(1, "test");
		MetaInformation metaInformation = new MetaInformation(worldObject);
		worldObject.setProperty(Constants.META_INFORMATION, metaInformation);
		return worldObject;
	}
}
