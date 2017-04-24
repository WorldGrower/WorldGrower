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
import org.worldgrower.deity.Deity;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.generator.PlantGenerator;
import org.worldgrower.generator.TerrainGenerator;
import org.worldgrower.goal.Goals;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.start.CharacterAttributes;
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
		OperationInfo operationInfo = new OperationInfo(worldObject, worldObject, Args.EMPTY, Actions.TALK_ACTION);
		world.getHistory().actionPerformed(operationInfo, new Turn());
		
		assertEquals("[TalkAction([])]", dungeonMaster.getImmediateGoal(worldObject, world).toShortString());
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
		int wellId = BuildingGenerator.buildWell(2, 2, world, 1f);
		PlantGenerator.generateOldTree(8, 8, world);
		
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
	
	@Test
	public void testRunWorldObjectWithImpossibleAction() {
		DungeonMaster dungeonMaster = new DungeonMaster();
		World world = createWorld(dungeonMaster);
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId();
		createVillagersOrganization(world);
		WorldObject commoner = TestUtils.createIntelligentWorldObject(2, Goals.SHRINE_TO_DEITY_GOAL);
		commoner.setProperty(Constants.NAME, "performer");
		commoner.setProperty(Constants.DEITY, Deity.ARES);
		commoner.setProperty(Constants.PERSONALITY, new Personality());
		commoner.setProperty(Constants.X, 5);
		commoner.setProperty(Constants.Y, 5);
		world.addWorldObject(commoner);
		BuildingGenerator.generateShrine(0, 0, world, commoner);
		
		dungeonMaster.runWorldObject(commoner, world);
		
		assertEquals(4, commoner.getProperty(Constants.X).intValue());
		assertEquals(4, commoner.getProperty(Constants.Y).intValue());
		assertEquals(Goals.SHRINE_TO_DEITY_GOAL, world.getGoal(commoner));
		assertEquals(Actions.WORSHIP_DEITY_ACTION, world.getImmediateGoal(commoner, world).getManagedOperation());
		
		TerrainGenerator.generateStoneResource(7, 7, world);
		commoner.setProperty(Constants.DEITY, Deity.DEMETER);
		
		dungeonMaster.runWorldObject(commoner, world);
		
		assertEquals(5, commoner.getProperty(Constants.X).intValue());
		assertEquals(5, commoner.getProperty(Constants.Y).intValue());
		assertEquals(Goals.SHRINE_TO_DEITY_GOAL, world.getGoal(commoner));
		assertEquals(Actions.MINE_STONE_ACTION, world.getImmediateGoal(commoner, world).getManagedOperation());
	}
	
	@Test
	public void testRunWorldObjectWithRemovedTarget() {
		DungeonMaster dungeonMaster = new DungeonMaster();
		World world = createWorld(dungeonMaster);
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId();
		createVillagersOrganization(world);
		WorldObject commoner = TestUtils.createIntelligentWorldObject(2, Goals.SHRINE_TO_DEITY_GOAL);
		commoner.setProperty(Constants.NAME, "performer");
		commoner.setProperty(Constants.DEITY, Deity.ARES);
		commoner.setProperty(Constants.PERSONALITY, new Personality());
		commoner.setProperty(Constants.X, 5);
		commoner.setProperty(Constants.Y, 5);
		world.addWorldObject(commoner);
		int shrineId = BuildingGenerator.generateShrine(0, 0, world, commoner);
		WorldObject shrine = world.findWorldObjectById(shrineId);
		
		int stoneResourceId = TerrainGenerator.generateStoneResource(0, 0, world);
		WorldObject stoneResource = world.findWorldObjectById(stoneResourceId);
		
		dungeonMaster.runWorldObject(commoner, world);
		
		assertEquals(Goals.SHRINE_TO_DEITY_GOAL, world.getGoal(commoner));
		assertEquals(Actions.WORSHIP_DEITY_ACTION, world.getImmediateGoal(commoner, world).getManagedOperation());
		assertEquals(shrine, world.getImmediateGoal(commoner, world).getTarget());
		
		world.removeWorldObject(shrine);
		
		dungeonMaster.runWorldObject(commoner, world);
		
		assertEquals(Goals.SHRINE_TO_DEITY_GOAL, world.getGoal(commoner));
		assertEquals(Actions.MINE_STONE_ACTION, world.getImmediateGoal(commoner, world).getManagedOperation());
		assertEquals(stoneResource, world.getImmediateGoal(commoner, world).getTarget());
	}
	
	@Test
	public void testRunWorldObjectWithMovingNPCTarget() {
		WorldObject commoner = TestUtils.createIntelligentWorldObject(2, Goals.SOCIALIZE_GOAL);
		WorldObject target = TestUtils.createIntelligentWorldObject(3, "target");
		
		DungeonMaster dungeonMaster = new DungeonMaster();
		World world = createWorld(dungeonMaster);
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId();
		createVillagersOrganization(world);
		
		testRunWorldObjectWithMovingTarget(commoner, target, dungeonMaster, world);
	}
	
	@Test
	public void testRunWorldObjectWithMovingPlayerCharacterTarget() {
		DungeonMaster dungeonMaster = new DungeonMaster();
		World world = createWorld(dungeonMaster);
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId();
		WorldObject organization = createVillagersOrganization(world);
		
		WorldObject commoner = TestUtils.createIntelligentWorldObject(2, Goals.SOCIALIZE_GOAL);
		WorldObject playerCharacter = CommonerGenerator.createPlayerCharacter(0, "player", "profession", "male", world, null, organization, new CharacterAttributes(10, 10, 10, 10, 10, 10), ImageIds.KNIGHT);
		playerCharacter.getProperty(Constants.GROUP).addAll(commoner.getProperty(Constants.GROUP));
		testRunWorldObjectWithMovingTarget(commoner, playerCharacter, dungeonMaster, world);
	}
	
	@Test
	public void testRunWorldObjectWithMoreUrgentGoal() {
		DungeonMaster dungeonMaster = new DungeonMaster();
		World world = createWorld(dungeonMaster);
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId();
		
		WorldObjectPriorities worldObjectPriorities = (performer, world2) -> Arrays.asList(Goals.FOOD_GOAL, Goals.DRINK_WATER_GOAL);
		WorldObject commoner = TestUtils.createIntelligentWorldObject(2, worldObjectPriorities);

		createBerryBush(world);
		createWell(world);
		
		commoner.setProperty(Constants.META_INFORMATION, new MetaInformation(commoner));
		commoner.setProperty(Constants.FOOD, 1000);
		commoner.setProperty(Constants.WATER, 0);
		
		dungeonMaster.runWorldObject(commoner, world);		
		assertEquals(Goals.DRINK_WATER_GOAL, world.getGoal(commoner));
		assertEquals(GoalChangedReason.EMPTY_META_INFORMATION, commoner.getProperty(Constants.META_INFORMATION).getGoalChangedReason());
		
		commoner.setProperty(Constants.FOOD, 0);
		dungeonMaster.runWorldObject(commoner, world);		
		assertEquals(Goals.FOOD_GOAL, world.getGoal(commoner));
		assertEquals(GoalChangedReason.MORE_IMPORTANT_GOAL_NOT_MET, commoner.getProperty(Constants.META_INFORMATION).getGoalChangedReason());
	}
	
	@Test
	public void testRunWorldObjectWithUnreachableTarget() {
		DungeonMaster dungeonMaster = new DungeonMaster();
		World world = createWorld(dungeonMaster);
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId();
		
		WorldObjectPriorities worldObjectPriorities = (performer, world2) -> Arrays.asList(Goals.FOOD_GOAL, Goals.IDLE_GOAL);
		WorldObject commoner = TestUtils.createIntelligentWorldObject(2, worldObjectPriorities);

		createUnreachableBerryBush(world, true);
		
		commoner.setProperty(Constants.META_INFORMATION, new MetaInformation(commoner));
		commoner.setProperty(Constants.FOOD, 0);
		
		dungeonMaster.runWorldObject(commoner, world);		
		assertEquals(Goals.IDLE_GOAL, world.getGoal(commoner));
		assertEquals(0, commoner.getProperty(Constants.META_INFORMATION).getCurrentTask().size());
	}
	
	@Test
	public void testRunWorldObjectTargetBecomesUnreachable() {
		DungeonMaster dungeonMaster = new DungeonMaster();
		World world = createWorld(dungeonMaster);
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId();
		
		WorldObjectPriorities worldObjectPriorities = (performer, world2) -> Arrays.asList(Goals.FOOD_GOAL, Goals.IDLE_GOAL);
		WorldObject commoner = TestUtils.createIntelligentWorldObject(2, worldObjectPriorities);

		createUnreachableBerryBush(world, false);
		
		commoner.setProperty(Constants.META_INFORMATION, new MetaInformation(commoner));
		commoner.setProperty(Constants.FOOD, 0);
		
		dungeonMaster.runWorldObject(commoner, world);		
		assertEquals(Goals.FOOD_GOAL, world.getGoal(commoner));
		assertEquals(6, commoner.getProperty(Constants.META_INFORMATION).getCurrentTask().size());
		assertEquals(1, commoner.getProperty(Constants.X).intValue());
		assertEquals(1, commoner.getProperty(Constants.Y).intValue());
		
		dungeonMaster.runWorldObject(commoner, world);
		assertEquals(2, commoner.getProperty(Constants.X).intValue());
		assertEquals(2, commoner.getProperty(Constants.Y).intValue());
		
		createSignpost(5, 4, world);
		dungeonMaster.runWorldObject(commoner, world);
		dungeonMaster.runWorldObject(commoner, world);
		dungeonMaster.runWorldObject(commoner, world);
		dungeonMaster.runWorldObject(commoner, world);
		assertEquals(Goals.IDLE_GOAL, world.getGoal(commoner));
		assertEquals(0, commoner.getProperty(Constants.META_INFORMATION).getCurrentTask().size());
		assertEquals(4, commoner.getProperty(Constants.X).intValue());
		assertEquals(3, commoner.getProperty(Constants.Y).intValue());
	}

	private void createUnreachableBerryBush(World world, boolean addTopObstacle) {
		createBerryBush(world); // at location 5,5
		
		createSignpost(4, 4, world);
		createSignpost(4, 5, world);
		createSignpost(4, 6, world);
		createSignpost(5, 6, world);
		createSignpost(6, 6, world);
		createSignpost(6, 5, world);
		createSignpost(6, 4, world);
		if (addTopObstacle) {
			createSignpost(5, 4, world);
		}
	}

	private void createWell(World world) {
		int wellId = BuildingGenerator.buildWell(5, 5, world, 1f);
		WorldObject well = world.findWorldObjectById(wellId);
		well.setProperty(Constants.WATER_SOURCE, 1000);
	}

	private void createBerryBush(World world) {
		int berryBushId = PlantGenerator.generateBerryBush(5, 5, world);
		WorldObject berryBush = world.findWorldObjectById(berryBushId);
		berryBush.setProperty(Constants.FOOD_SOURCE, 500);
	}
	
	private void createSignpost(int x, int y, World world) {
		BuildingGenerator.generateSignPost(x, y, world, "");
	}
	
	private void testRunWorldObjectWithMovingTarget(WorldObject commoner, WorldObject target, DungeonMaster dungeonMaster, World world) {

		commoner.setProperty(Constants.X, 5);
		commoner.setProperty(Constants.Y, 5);
		world.addWorldObject(commoner);
		target.setProperty(Constants.X, 3);
		target.setProperty(Constants.Y, 3);
		world.addWorldObject(target);
		
		dungeonMaster.runWorldObject(commoner, world);
		
		assertEquals(4, commoner.getProperty(Constants.X).intValue());
		assertEquals(4, commoner.getProperty(Constants.Y).intValue());
		assertEquals(Goals.SOCIALIZE_GOAL, world.getGoal(commoner));
		assertEquals(Actions.TALK_ACTION, world.getImmediateGoal(commoner, world).getManagedOperation());
		
		target.setProperty(Constants.X, 8);
		target.setProperty(Constants.Y, 8);
		world.getHistory().actionPerformed(new OperationInfo(target, target, Args.EMPTY, Actions.MOVE_ACTION), new Turn());
		
		dungeonMaster.runWorldObject(commoner, world);
		
		assertEquals(5, commoner.getProperty(Constants.X).intValue());
		assertEquals(5, commoner.getProperty(Constants.Y).intValue());
		assertEquals(Goals.SOCIALIZE_GOAL, world.getGoal(commoner));
		assertEquals(Actions.TALK_ACTION, world.getImmediateGoal(commoner, world).getManagedOperation());
	}
	
	private WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		return organization;
	}
	
	private WorldObject createWorldObject() {
		WorldObject worldObject = TestUtils.createWorldObject(1, "test");
		MetaInformation metaInformation = new MetaInformation(worldObject);
		worldObject.setProperty(Constants.META_INFORMATION, metaInformation);
		return worldObject;
	}
}
