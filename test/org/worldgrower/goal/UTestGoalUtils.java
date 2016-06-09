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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.MockMetaInformation;
import org.worldgrower.OperationInfo;
import org.worldgrower.TestUtils;
import org.worldgrower.TestWorldObjectPriorities;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.generator.PlantGenerator;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.history.Turn;

public class UTestGoalUtils {

	@Test
	public void testIsOpenSpace() {
		World world = createWorld();
		world.addWorldObject(TestUtils.createWorldObject(3, 3, 1, 1));
		
		assertTrue(GoalUtils.isOpenSpace(0, 0, 2, 2, world));
		assertFalse(GoalUtils.isOpenSpace(3, 3, 1, 1, world));
		assertFalse(GoalUtils.isOpenSpace(2, 2, 2, 2, world));
		assertFalse(GoalUtils.isOpenSpace(9, 9, 2, 2, world));
	}

	private WorldImpl createWorld() {
		return new WorldImpl(10, 10, new DungeonMaster(), null);
	}
	
	@Test
	public void testFindOpenSpace() {
		World world = createWorld();
		WorldObject performer = TestUtils.createWorldObject(3, 3, 1, 1);
		world.addWorldObject(performer);
		int[] position = GoalUtils.findOpenSpace(performer, 1, 1, world);
		
		assertEquals(2, position.length);
		assertEquals(0, position[0]);
		assertEquals(1, position[1]);
	}
	
	@Test
	public void testFindNearestTarget() {
		World world = createWorld();
		WorldObject performer = TestUtils.createWorldObject(3, 3, 1, 1);
		world.addWorldObject(performer);
		
		world.addWorldObject(TestUtils.createWorldObject(4, 4, 1, 1, Constants.WOOD_SOURCE, 30));
		world.addWorldObject(TestUtils.createWorldObject(5, 5, 1, 1, Constants.WOOD_SOURCE, 30));
		
		WorldObject target = GoalUtils.findNearestTarget(performer, Actions.CUT_WOOD_ACTION, world);
		
		assertEquals(4, target.getProperty(Constants.X).intValue());
		assertEquals(4, target.getProperty(Constants.Y).intValue());
	}
	
	@Test
	public void testFindNearestTargetNoTarget() {
		World world = createWorld();
		WorldObject performer = TestUtils.createWorldObject(3, 3, 1, 1);
		world.addWorldObject(performer);
		
		WorldObject target = GoalUtils.findNearestTarget(performer, Actions.CUT_WOOD_ACTION, world);
		
		assertEquals(null, target);
	}
	
	@Test
	public void testfindNearestTargets() {
		World world = createWorld();
		WorldObject performer = TestUtils.createWorldObject(3, 3, 1, 1);
		world.addWorldObject(performer);
		
		world.addWorldObject(TestUtils.createWorldObject(4, 4, 1, 1, Constants.WOOD_SOURCE, 30));
		world.addWorldObject(TestUtils.createWorldObject(5, 5, 1, 1, Constants.WOOD_SOURCE, 40));
		
		List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.CUT_WOOD_ACTION, w -> w.getProperty(Constants.WOOD_SOURCE) > 30, world);
		
		assertEquals(1, targets.size());
		assertEquals(40, targets.get(0).getProperty(Constants.WOOD_SOURCE).intValue());

	}
	
	@Test
	public void testfindNearestTargetsDistance() {
		World world = createWorld();
		WorldObject performer = TestUtils.createWorldObject(3, 3, 1, 1);
		world.addWorldObject(performer);
		
		world.addWorldObject(TestUtils.createWorldObject(4, 4, 1, 1, Constants.WOOD_SOURCE, 30));
		world.addWorldObject(TestUtils.createWorldObject(6, 6, 1, 1, Constants.WOOD_SOURCE, 40));
		
		List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.CUT_WOOD_ACTION, w -> true, world);
		
		assertEquals(2, targets.size());
		assertEquals(30, targets.get(0).getProperty(Constants.WOOD_SOURCE).intValue());
		assertEquals(4, targets.get(0).getProperty(Constants.X).intValue());
	}
	
	@Test
	public void testActionAlreadyPerformed() {
		World world = createWorld();
		WorldObject performer = TestUtils.createIntelligentWorldObject(6, Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		world.addWorldObject(performer);
		WorldObject target = TestUtils.createIntelligentWorldObject(7, Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		world.addWorldObject(target);
		
		assertEquals(false, GoalUtils.actionAlreadyPerformed(performer, target, Actions.TALK_ACTION, Conversations.createArgs(Conversations.PROFESSION_CONVERSATION), world));
		
		OperationInfo operationInfo = new OperationInfo(performer, target, Conversations.createArgs(Conversations.PROFESSION_CONVERSATION), Actions.TALK_ACTION);
		world.getHistory().actionPerformed(operationInfo, new Turn());
		
		assertEquals(true, GoalUtils.actionAlreadyPerformed(performer, target, Actions.TALK_ACTION, Conversations.createArgs(Conversations.PROFESSION_CONVERSATION), world));
	}
	
	@Test
	public void testActionAlreadyPerformedMultipleTargets() {
		World world = createWorld();
		WorldObject performer = TestUtils.createIntelligentWorldObject(6, Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		world.addWorldObject(performer);
		WorldObject target = TestUtils.createIntelligentWorldObject(7, Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		world.addWorldObject(target);
		WorldObject target2 = TestUtils.createIntelligentWorldObject(8, Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		world.addWorldObject(target2);
		
		
		assertEquals(false, GoalUtils.actionAlreadyPerformed(performer, target, Actions.TALK_ACTION, Conversations.createArgs(Conversations.PROFESSION_CONVERSATION), world));
		
		OperationInfo operationInfo = new OperationInfo(performer, target, Conversations.createArgs(Conversations.PROFESSION_CONVERSATION), Actions.TALK_ACTION);
		world.getHistory().actionPerformed(operationInfo, new Turn());
		
		assertEquals(true, GoalUtils.actionAlreadyPerformed(performer, target, Actions.TALK_ACTION, Conversations.createArgs(Conversations.PROFESSION_CONVERSATION), world));
	}
	
	@Test
	public void testCanEnlarge() {
		World world = createWorld();
		WorldObject target = TestUtils.createWorldObject(2, 2, 1, 1, Constants.ID, 7);
		world.addWorldObject(target);
		
		assertEquals(true, GoalUtils.canEnlarge(target, world));
		
		WorldObject obstacle = TestUtils.createWorldObject(3, 3, 1, 1, Constants.ID, 8);
		world.addWorldObject(obstacle);
		assertEquals(false, GoalUtils.canEnlarge(target, world));
	}
	
	@Test
	public void testCurrentGoalHasLowerPriorityThan() {
		World world = createWorld();
		TestWorldObjectPriorities worldObjectPriorities = new TestWorldObjectPriorities(Arrays.asList(Goals.FOOD_GOAL, Goals.REST_GOAL, Goals.DRINK_WATER_GOAL));
		WorldObject target = TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 500, worldObjectPriorities);
		MockMetaInformation.setMetaInformation(target, Goals.REST_GOAL);
		
		world.addWorldObject(target);
		
		assertEquals(true, GoalUtils.currentGoalHasLowerPriorityThan(Goals.FOOD_GOAL, target, world));
		assertEquals(false, GoalUtils.currentGoalHasLowerPriorityThan(Goals.DRINK_WATER_GOAL, target, world));
	}
	
	@Test
	public void testCreateOperationInfoNull() {
		World world = createWorld();
		WorldObject performer = TestUtils.createWorldObject(2, 2, 1, 1, Constants.ID, 7);
		assertEquals(null, GoalUtils.createOperationInfo(performer, Actions.CUT_WOOD_ACTION, Args.EMPTY, world));
	}

	@Test
	public void testCreateOperationInfoTarget() {
		World world = createWorld();
		WorldObject performer = TestUtils.createWorldObject(2, 2, 1, 1, Constants.ID, 7);
		int treeId = PlantGenerator.generateTree(5, 5, world);
		OperationInfo operationInfo = GoalUtils.createOperationInfo(performer, Actions.CUT_WOOD_ACTION, Args.EMPTY, world);
		assertEquals(Actions.CUT_WOOD_ACTION, operationInfo.getManagedOperation());
		assertEquals(treeId, operationInfo.getTarget().getProperty(Constants.ID).intValue());
	}
	
	@Test
	public void testFindNearestPersonLookingLikeNoDeception() {
		World world = createWorld();
		WorldObject performer = TestUtils.createSkilledWorldObject(2);
		performer.setProperty(Constants.IMAGE_ID, ImageIds.KNIGHT);
		
		WorldObject target = TestUtils.createSkilledWorldObject(3);
		target.setProperty(Constants.IMAGE_ID, ImageIds.KNIGHT);
		world.addWorldObject(target);
		
		assertEquals(target, GoalUtils.findNearestPersonLookingLike(performer, 3, world));
	}
	
	@Test
	public void testFindNearestPersonLookingLikeIllusion() {
		World world = createWorld();
		WorldObject performer = TestUtils.createSkilledWorldObject(2);
		performer.setProperty(Constants.IMAGE_ID, ImageIds.KNIGHT);
		
		WorldObject target = TestUtils.createSkilledWorldObject(3);
		target.setProperty(Constants.IMAGE_ID, ImageIds.KNIGHT);
		target.setProperty(Constants.X, 9);
		target.setProperty(Constants.Y, 9);
		world.addWorldObject(target);
		
		int illusionId = IllusionPropertyUtils.createIllusion(performer, 3, world, 0, 0);
		WorldObject illusion = world.findWorldObject(Constants.ID, illusionId);
		assertEquals(illusion, GoalUtils.findNearestPersonLookingLike(performer, 3, world));
	}
	
	@Test
	public void testFindNearestPersonLookingLikeDisguise() {
		World world = createWorld();
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		performer.setProperty(Constants.IMAGE_ID, ImageIds.KNIGHT);
		
		WorldObject target = TestUtils.createSkilledWorldObject(3, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		target.setProperty(Constants.IMAGE_ID, ImageIds.KNIGHT);
		target.setProperty(Constants.X, 9);
		target.setProperty(Constants.Y, 9);
		world.addWorldObject(target);
		
		WorldObject disguiser = TestUtils.createSkilledWorldObject(4, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		disguiser.setProperty(Constants.IMAGE_ID, ImageIds.BLUE_HAIRED_COMMONER);
		disguiser.setProperty(Constants.X, 0);
		disguiser.setProperty(Constants.Y, 0);
		disguiser.setProperty(Constants.FACADE, target.deepCopy());
		disguiser = FacadeUtils.createFacade(disguiser, disguiser, target, world);
		world.addWorldObject(disguiser);
		
		assertEquals(disguiser, GoalUtils.findNearestPersonLookingLike(performer, 3, world));
	}
}
