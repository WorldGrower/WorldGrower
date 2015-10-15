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
import org.worldgrower.actions.MockCommonerNameGenerator;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Condition;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.generator.TerrainGenerator;
import org.worldgrower.gui.CommonerImageIds;

public class UTestGetPoisonCuredGoal {

	private GetPoisonCuredGoal goal = Goals.GET_POISON_CURED_GOAL;
	private final CommonerGenerator commonerGenerator = new CommonerGenerator(666, new CommonerImageIds(), new MockCommonerNameGenerator());

	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}

	@Test
	public void testCalculateGoalPerformerKnowsCurePoisonSpell() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		performer.getProperty(Constants.KNOWN_SPELLS).add(Actions.CURE_POISON_ACTION);
		
		assertEquals(Actions.CURE_POISON_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(performer, goal.calculateGoal(performer, world).getPerformer());
		assertEquals(performer, goal.calculateGoal(performer, world).getTarget());
	}
	
	@Test
	public void testCalculateGoalPerformerKnowsCurePoisonSpellNotEnoughEnergy() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		performer.getProperty(Constants.KNOWN_SPELLS).add(Actions.CURE_POISON_ACTION);
		performer.setProperty(Constants.ENERGY, 0);
		
		assertEquals(Actions.REST_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(performer, goal.calculateGoal(performer, world).getPerformer());
		assertEquals(performer, goal.calculateGoal(performer, world).getTarget());
	}
	
	@Test
	public void testCalculateGoalTargetKnowsCurePoisonSpell() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		
		WorldObject target = createCommoner(world, organization);
		target.getProperty(Constants.KNOWN_SPELLS).add(Actions.CURE_POISON_ACTION);
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(performer, goal.calculateGoal(performer, world).getPerformer());
		assertEquals(target, goal.calculateGoal(performer, world).getTarget());
		assertEquals(true, goal.calculateGoal(performer, world).firstArgsIs(Conversations.createArgs(Conversations.CURE_POISON_CONVERSATION)[0]));
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		
		assertEquals(true, goal.isGoalMet(performer, world));
		
		performer.getProperty(Constants.CONDITIONS).addCondition(Condition.POISONED_CONDITION, 8, world);
		assertEquals(false, goal.isGoalMet(performer, world));
	}

	private WorldObject createCommoner(World world, WorldObject organization) {
		int commonerId = commonerGenerator.generateCommoner(0, 0, world, organization);
		WorldObject performer = world.findWorldObject(Constants.ID, commonerId);
		return performer;
	}
}