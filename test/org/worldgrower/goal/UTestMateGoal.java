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
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.MockCommonerNameGenerator;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.gui.CommonerImageIds;

public class UTestMateGoal {

	private MateGoal goal = Goals.MATE_GOAL;
	private final CommonerGenerator commonerGenerator = new CommonerGenerator(666, new CommonerImageIds(), new MockCommonerNameGenerator());
	
	@Test
	public void testCalculateGoal() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalOneTarget() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		WorldObject target = createCommoner(world, organization);
		
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(target, 1);
		performer.setProperty(Constants.GENDER, "male");
		target.setProperty(Constants.GENDER, "female");
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(target, goal.calculateGoal(performer, world).getTarget());
		assertEquals(true, goal.calculateGoal(performer, world).firstArgsIs(Conversations.createArgs(Conversations.COMPLIMENT_CONVERSATION)[0]));
	}
	
	@Test
	public void testCalculateGoalOneTargetWithGoodRelationship() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		WorldObject target = createCommoner(world, organization);
		
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(target, 900);
		performer.setProperty(Constants.GENDER, "male");
		target.setProperty(Constants.GENDER, "female");
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(target, goal.calculateGoal(performer, world).getTarget());
		assertEquals(true, goal.calculateGoal(performer, world).firstArgsIs(Conversations.createArgs(Conversations.PROPOSE_MATE_CONVERSATION)[0]));
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		performer.setProperty(Constants.MATE_ID, 7);
		assertEquals(true, goal.isGoalMet(performer, world));
	}

	private WorldObject createCommoner(World world, WorldObject organization) {
		int commonerId = commonerGenerator.generateCommoner(0, 0, world, organization);
		WorldObject commoner = world.findWorldObject(Constants.ID, commonerId);
		return commoner;
	}
}