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
import org.worldgrower.actions.BrawlListener;
import org.worldgrower.attribute.BackgroundImpl;
import org.worldgrower.attribute.Gender;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.generator.CreatureGenerator;
import org.worldgrower.generator.Item;
import org.worldgrower.generator.PlantGenerator;
import org.worldgrower.goal.BountyPropertyUtils;
import org.worldgrower.goal.BrawlPropertyUtils;
import org.worldgrower.goal.FacadeUtils;
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
		
		assertEquals(true, DefaultGoalObstructedHandler.areBrawling(performer, actionTarget, Actions.NON_LETHAL_MELEE_ATTACK_ACTION));
		
		performer.removeProperty(Constants.BRAWL_OPPONENT_ID);
		actionTarget.removeProperty(Constants.BRAWL_OPPONENT_ID);
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
		WorldObject actionTarget = TestUtils.createWorldObject(2, 2, 1, 1, Constants.ID, 2);
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
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GENDER, Gender.MALE);
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
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GENDER, Gender.MALE);
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
	
	@Test
	public void testCalculateBounty() {
		assertEquals(40, DefaultGoalObstructedHandler.calculateBounty(Actions.STEAL_ACTION));
		assertEquals(200, DefaultGoalObstructedHandler.calculateBounty(Actions.MELEE_ATTACK_ACTION));
	}
	
	@Test
	public void testIsLegalAttackOtherVillager() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList().add(villagersOrganization));
		WorldObject actionTarget = TestUtils.createIntelligentWorldObject(3, Constants.GROUP, new IdList().add(villagersOrganization));
		world.addWorldObject(performer);
		world.addWorldObject(actionTarget);
		
		assertEquals(false, DefaultGoalObstructedHandler.isLegal(performer, actionTarget, Actions.FIRE_BOLT_ATTACK_ACTION, Args.EMPTY, world));
	}
	
	@Test
	public void testIsLegalAttackTree() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList().add(villagersOrganization));
		int targetId = PlantGenerator.generateTree(0, 0, world);
		WorldObject actionTarget = world.findWorldObjectById(targetId);
		world.addWorldObject(performer);
		world.addWorldObject(TestUtils.createIntelligentWorldObject(3, "observer"));
		
		villagersOrganization = createVillagersOrganization(world);
		
		assertEquals(true, DefaultGoalObstructedHandler.isLegal(performer, actionTarget, Actions.FIRE_BOLT_ATTACK_ACTION, Args.EMPTY, world));
	}
	
	@Test
	public void testCheckLegality() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList().add(villagersOrganization));
		MockMetaInformation.setMetaInformation(performer, Goals.BRAWL_GOAL);
		WorldObject actionTarget = TestUtils.createIntelligentWorldObject(3, Constants.GROUP, new IdList().add(villagersOrganization));
		world.addWorldObject(performer);
		world.addWorldObject(TestUtils.createIntelligentWorldObject(3, "observer"));
		
		villagersOrganization = createVillagersOrganization(world);
		
		new DefaultGoalObstructedHandler().checkLegality(performer, actionTarget, Actions.FIRE_BOLT_ATTACK_ACTION, Args.EMPTY, world);
		
		assertEquals(200, BountyPropertyUtils.getBounty(performer, world));
		assertEquals(0, performer.getProperty(Constants.GROUP).size());
	}
	
	@Test
	public void testCheckLegalityAfterBrawlVictory() {
		World world = new WorldImpl(10, 10, null, null);
		world.addListener(new BrawlListener());
		WorldObject villagersOrganization = createVillagersOrganization(world);
		WorldObject performer = TestUtils.createSkilledWorldObject(2, Constants.GROUP, new IdList().add(villagersOrganization));
		MockMetaInformation.setMetaInformation(performer, Goals.BRAWL_GOAL);
		WorldObject actionTarget = TestUtils.createSkilledWorldObject(3, Constants.GROUP, new IdList().add(villagersOrganization));
		world.addWorldObject(performer);
		world.addWorldObject(actionTarget);
		world.addWorldObject(TestUtils.createIntelligentWorldObject(4, "observer"));
		
		villagersOrganization = createVillagersOrganization(world);
		
		BrawlPropertyUtils.startBrawl(performer, actionTarget, 20);
		actionTarget.setProperty(Constants.HIT_POINTS, 1);
		
		new OperationInfo(performer, actionTarget, Args.EMPTY, Actions.NON_LETHAL_MELEE_ATTACK_ACTION).perform(world);
		
		assertEquals(1, performer.getProperty(Constants.GROUP).size());
		assertEquals(true, BrawlPropertyUtils.isBrawling(performer));
		assertEquals(true, BrawlPropertyUtils.isBrawling(actionTarget));
		
		BrawlPropertyUtils.completelyEndBrawling(performer);
		BrawlPropertyUtils.completelyEndBrawling(actionTarget);
		assertEquals(false, BrawlPropertyUtils.isBrawling(performer));
		assertEquals(false, BrawlPropertyUtils.isBrawling(actionTarget));

	}
	
	@Test
	public void testPerformerAttacksAnimal() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		
		CreatureGenerator creatureGenerator = new CreatureGenerator(TestUtils.createIntelligentWorldObject(1, "cow"));
		int cowId = creatureGenerator.generateCow(0, 0, world);
		WorldObject cow = world.findWorldObjectById(cowId);
		
		assertEquals(false, cow.getProperty(Constants.ANIMAL_ENEMIES).contains(performer));
		
		DefaultGoalObstructedHandler.performerAttacksAnimal(performer, cow);
		
		assertEquals(true, cow.getProperty(Constants.ANIMAL_ENEMIES).contains(performer));
	}
	
	@Test
	public void testCalculateTargetNoDeception() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(3, "target");
		
		assertEquals(target, DefaultGoalObstructedHandler.calculateTarget(performer, target, world));
	}
	
	@Test
	public void testCalculateTargetDisguised() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(3, "target");
		WorldObject disguise = TestUtils.createIntelligentWorldObject(4, "disguise");
		world.addWorldObject(disguise);
		
		FacadeUtils.disguise(target, disguise.getProperty(Constants.ID), world);
		performer.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		assertEquals(disguise, DefaultGoalObstructedHandler.calculateTarget(performer, target, world));
	}
	
	@Test
	public void testCalculateTargetMaskedIllusion() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(2, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(3, Constants.ILLUSION_CREATOR_ID, 7);
		target.setProperty(Constants.X, 1);
		target.setProperty(Constants.Y, 1);
		
		WorldObject realTarget = TestUtils.createIntelligentWorldObject(3, "realTarget");
		realTarget.setProperty(Constants.X, 1);
		realTarget.setProperty(Constants.Y, 1);
		
		assertEquals(realTarget, DefaultGoalObstructedHandler.calculateTarget(performer, target, world));
	}

	private WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		return organization;
	}
}
