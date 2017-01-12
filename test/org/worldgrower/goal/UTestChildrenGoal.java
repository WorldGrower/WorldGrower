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
import org.worldgrower.AssertUtils;
import org.worldgrower.Constants;
import org.worldgrower.MockCommonerGenerator;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.generator.CommonerGenerator;

public class UTestChildrenGoal {

	private ChildrenGoal goal = Goals.CHILDREN_GOAL;
	private final CommonerGenerator commonerGenerator = new MockCommonerGenerator();
	
	@Test
	public void testCalculateGoal() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createPerformer(world, organization);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalOneMateBarelyKnown() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createPerformer(world, organization);
		performer.setProperty(Constants.GENDER, "female");
		
		WorldObject target = createPerformer(world, organization);
		target.setProperty(Constants.GENDER, "male");
		target.getProperty(Constants.BUILDINGS).add(7, BuildingType.HOUSE);
		
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(target, 1);
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		AssertUtils.assertConversation(goal.calculateGoal(performer, world), Conversations.COMPLIMENT_CONVERSATION);
	}
	
	@Test
	public void testCalculateGoalOneMateWellKnown() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createPerformer(world, organization);
		performer.setProperty(Constants.GENDER, "female");
		
		WorldObject target = createPerformer(world, organization);
		target.setProperty(Constants.GENDER, "male");
		target.getProperty(Constants.BUILDINGS).add(7, BuildingType.HOUSE);
		
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(target, 1000);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, 1000);
		
		assertEquals(Actions.SEX_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalOneMateUnknown() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createPerformer(world, organization);
		performer.setProperty(Constants.GENDER, "female");
		
		WorldObject target = createPerformer(world, organization);
		target.setProperty(Constants.GENDER, "male");
		target.getProperty(Constants.BUILDINGS).add(7, BuildingType.HOUSE);
		world.addWorldObject(target);
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		AssertUtils.assertConversation(goal.calculateGoal(performer, world), Conversations.COMPLIMENT_CONVERSATION);
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createPerformer(world, organization);
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		performer.getProperty(Constants.CHILDREN).add(7).add(8).add(9).add(10);
		
		assertEquals(true, goal.isGoalMet(performer, world));
	}
	
	private WorldObject createPerformer(World world, WorldObject organization) {
		int performerId = commonerGenerator.generateCommoner(0, 0, world, organization, CommonerGenerator.NO_PARENT);
		WorldObject performer = world.findWorldObjectById(performerId);
		return performer;
	}
}