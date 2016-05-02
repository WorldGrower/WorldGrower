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

import java.util.List;

import org.junit.Test;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.BackgroundImpl;
import org.worldgrower.attribute.IdList;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.generator.Item;
import org.worldgrower.goal.Goals;
import org.worldgrower.goal.GroupPropertyUtils;

public class UTestDefaultGoalObstructedHandler {

	@Test
	public void testPerformerAttacked() {
		assertEquals(true, DefaultGoalObstructedHandler.performerAttacked(Actions.MELEE_ATTACK_ACTION));
		assertEquals(true, DefaultGoalObstructedHandler.performerAttacked(Actions.NON_LETHAL_MELEE_ATTACK_ACTION));
		assertEquals(true, DefaultGoalObstructedHandler.performerAttacked(Actions.FIRE_BOLT_ATTACK_ACTION));
		assertEquals(false, DefaultGoalObstructedHandler.performerAttacked(Actions.CURE_POISON_ACTION));
	}
	
	@Test
	public void testAreBrawling() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.BRAWL_OPPONENT_ID, 2);
		WorldObject actionTarget = TestUtils.createIntelligentWorldObject(2, Constants.BRAWL_OPPONENT_ID, 1);
		
		assertEquals(true, DefaultGoalObstructedHandler.areBrawling(performer, actionTarget, Actions.NON_LETHAL_MELEE_ATTACK_ACTION));
		assertEquals(false, DefaultGoalObstructedHandler.areBrawling(performer, actionTarget, Actions.MELEE_ATTACK_ACTION));
	}
	
	@Test
	public void testAreBrawlingNoBrawlOpponentId() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.BRAWL_OPPONENT_ID, null);
		WorldObject actionTarget = TestUtils.createIntelligentWorldObject(2, Constants.BRAWL_OPPONENT_ID, null);
		
		assertEquals(false, DefaultGoalObstructedHandler.areBrawling(performer, actionTarget, Actions.NON_LETHAL_MELEE_ATTACK_ACTION));
	}
	
	@Test
	public void testAreBrawlingItemEquiped() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.BRAWL_OPPONENT_ID, 2);
		WorldObject actionTarget = TestUtils.createIntelligentWorldObject(2, Constants.BRAWL_OPPONENT_ID, 1);
		
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, Item.IRON_CLAYMORE.generate(1f));
		
		assertEquals(false, DefaultGoalObstructedHandler.areBrawling(performer, actionTarget, Actions.NON_LETHAL_MELEE_ATTACK_ACTION));
	}
	
	@Test
	public void testAreFightingInArena() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.ARENA_OPPONENT_ID, 2);
		WorldObject actionTarget = TestUtils.createIntelligentWorldObject(2, Constants.ARENA_OPPONENT_ID, 1);
		
		assertEquals(true, DefaultGoalObstructedHandler.areFightingInArena(performer, actionTarget, null));
	}
	
	@Test
	public void testAreFightingInArenaNoFight() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.ARENA_OPPONENT_ID, null);
		WorldObject actionTarget = TestUtils.createIntelligentWorldObject(2, Constants.ARENA_OPPONENT_ID, null);
		
		assertEquals(false, DefaultGoalObstructedHandler.areFightingInArena(performer, actionTarget, null));
	}
	
	@Test
	public void testActionTargetIsCriminal() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = createVillagersOrganization(world);
		
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		assertEquals(true, DefaultGoalObstructedHandler.actionTargetIsCriminal(performer, world));
		
		performer.getProperty(Constants.GROUP).add(organization);
		assertEquals(false, DefaultGoalObstructedHandler.actionTargetIsCriminal(performer, world));
	}
	
	@Test
	public void testPerformerCanAttackCriminals() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.CAN_ATTACK_CRIMINALS, Boolean.TRUE);
		
		assertEquals(true, DefaultGoalObstructedHandler.performerCanAttackCriminals(performer));
		
		performer = TestUtils.createIntelligentWorldObject(1, Constants.FOOD, 500);
		assertEquals(false, DefaultGoalObstructedHandler.performerCanAttackCriminals(performer));
	}
	
	@Test
	public void testPerformerViolatedGroupRules() {
		World world = new WorldImpl(10, 10, null, null);
		createVillagersOrganization(world);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList().add(1));
		WorldObject actionTarget = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList().add(1));
		
		assertEquals(false, DefaultGoalObstructedHandler.performerViolatedGroupRules(performer, actionTarget, null, Actions.TALK_ACTION, world));
		assertEquals(true, DefaultGoalObstructedHandler.performerViolatedGroupRules(performer, actionTarget, null, Actions.MELEE_ATTACK_ACTION, world));
	}
	
	@Test
	public void testPerformerViolatedGroupRulesAttackingCriminal() {
		World world = new WorldImpl(10, 10, null, null);
		createVillagersOrganization(world);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList().add(1));
		WorldObject actionTarget = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		
		assertEquals(false, DefaultGoalObstructedHandler.performerViolatedGroupRules(performer, actionTarget, null, Actions.MELEE_ATTACK_ACTION, world));
	}
	
	@Test
	public void testHasAnyoneSeenAction() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList().add(1));
		WorldObject actionTarget = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList().add(1));
		world.addWorldObject(performer);
		world.addWorldObject(actionTarget);
		
		performer.setProperty(Constants.X, 1);
		performer.setProperty(Constants.Y, 1);
		
		actionTarget.setProperty(Constants.X, 2);
		actionTarget.setProperty(Constants.Y, 2);
		
		assertEquals(true, DefaultGoalObstructedHandler.hasAnyoneSeenAction(performer, actionTarget, Actions.TALK_ACTION, Args.EMPTY, world));
	}
	
	@Test
	public void testHasAnyoneSeenActionNoWitnesses() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList().add(1));
		WorldObject actionTarget = TestUtils.createWorldObject(2, 2, 1, 1);
		world.addWorldObject(performer);
		world.addWorldObject(actionTarget);
		
		performer.setProperty(Constants.X, 1);
		performer.setProperty(Constants.Y, 1);
		
		assertEquals(false, DefaultGoalObstructedHandler.hasAnyoneSeenAction(performer, actionTarget, Actions.TALK_ACTION, Args.EMPTY, world));
	}
	
	@Test
	public void testHasAnyoneSeenActionInvisible() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList().add(1));
		WorldObject actionTarget = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList().add(1));
		world.addWorldObject(performer);
		world.addWorldObject(actionTarget);
		
		Conditions.add(performer, Condition.INVISIBLE_CONDITION, 8, world);
		
		performer.setProperty(Constants.X, 1);
		performer.setProperty(Constants.Y, 1);
		
		actionTarget.setProperty(Constants.X, 2);
		actionTarget.setProperty(Constants.Y, 2);
		
		assertEquals(false, DefaultGoalObstructedHandler.hasAnyoneSeenAction(performer, actionTarget, Actions.TALK_ACTION, Args.EMPTY, world));
	}
	
	@Test
	public void testLogToBackground() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GENDER, "male");
		WorldObject actionTarget = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		world.addWorldObject(performer);
		world.addWorldObject(actionTarget);
		
		actionTarget.setProperty(Constants.BACKGROUND, new BackgroundImpl());
		
		DefaultGoalObstructedHandler.logToBackground(Goals.PROTECT_ONE_SELF_GOAL, actionTarget, actionTarget, Actions.MELEE_ATTACK_ACTION, Args.EMPTY, performer, world);
		List<String> angryReasons = actionTarget.getProperty(Constants.BACKGROUND).getAngryReasons(true, 2, performer, world);
		assertEquals(1, angryReasons.size());
	}
	
	@Test
	public void testAlterRelationships() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GENDER, "male");
		WorldObject actionTarget = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		world.addWorldObject(performer);
		world.addWorldObject(actionTarget);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		
		performer.setProperty(Constants.GROUP, new IdList().add(villagersOrganization));
		
		DefaultGoalObstructedHandler.alterRelationships(performer, actionTarget, actionTarget, null, Actions.MELEE_ATTACK_ACTION, world, -10, performer, actionTarget);
		
		assertEquals(-10, performer.getProperty(Constants.RELATIONSHIPS).getValue(actionTarget));
		assertEquals(-10, actionTarget.getProperty(Constants.RELATIONSHIPS).getValue(performer));
		assertEquals(true, actionTarget.getProperty(Constants.GROUP).getIds().isEmpty());
	}

	private WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		return organization;
	}
}
