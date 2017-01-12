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
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.conversation.Conversation;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.history.Turn;

public class UTestImproveRelationshipGoal {
	
	private final CommonerGenerator commonerGenerator = new MockCommonerGenerator();
	
	@Test
	public void testCalculateGoal() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject target = createCommoner(world, organization);
		ImproveRelationshipGoal goal = new ImproveRelationshipGoal(target.getProperty(Constants.ID), 500, world);
		WorldObject performer = createCommoner(world, organization);
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		AssertUtils.assertConversation(goal.calculateGoal(performer, world), Conversations.COMPLIMENT_CONVERSATION);
	}
	
	@Test
	public void testCalculateGoalFamilyConversation() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject target = createCommoner(world, organization);
		ImproveRelationshipGoal goal = new ImproveRelationshipGoal(target.getProperty(Constants.ID), 500, world);
		WorldObject performer = createCommoner(world, organization);
		
		addActionToHistory(world, performer, target, Conversations.COMPLIMENT_CONVERSATION);
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		AssertUtils.assertConversation(goal.calculateGoal(performer, world), Conversations.FAMILY_CONVERSATION);
	}
	
	
	@Test
	public void testCalculateGoalProfessionConversation() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject target = createCommoner(world, organization);
		ImproveRelationshipGoal goal = new ImproveRelationshipGoal(target.getProperty(Constants.ID), 500, world);
		WorldObject performer = createCommoner(world, organization);
		
		addActionToHistory(world, performer, target, Conversations.COMPLIMENT_CONVERSATION);
		addActionToHistory(world, performer, target, Conversations.FAMILY_CONVERSATION);
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		AssertUtils.assertConversation(goal.calculateGoal(performer, world), Conversations.PROFESSION_CONVERSATION);
	}
	
	@Test
	public void testCalculateGoalKissConversation() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject target = createCommoner(world, organization);
		ImproveRelationshipGoal goal = new ImproveRelationshipGoal(target.getProperty(Constants.ID), 500, world);
		WorldObject performer = createCommoner(world, organization);
		
		addActionToHistory(world, performer, target, Conversations.COMPLIMENT_CONVERSATION);
		addActionToHistory(world, performer, target, Conversations.FAMILY_CONVERSATION);
		addActionToHistory(world, performer, target, Conversations.PROFESSION_CONVERSATION);
		
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(target, 1000);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, 1000);
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		AssertUtils.assertConversation(goal.calculateGoal(performer, world), Conversations.KISS_CONVERSATION);
	}
	
	@Test
	public void testCalculateGoalRepeatedKiss() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject target = createCommoner(world, organization);
		ImproveRelationshipGoal goal = new ImproveRelationshipGoal(target.getProperty(Constants.ID), 500, world);
		WorldObject performer = createCommoner(world, organization);
		
		addActionToHistory(world, performer, target, Conversations.COMPLIMENT_CONVERSATION);
		addActionToHistory(world, performer, target, Conversations.FAMILY_CONVERSATION);
		addActionToHistory(world, performer, target, Conversations.PROFESSION_CONVERSATION);
		addActionToHistory(world, performer, target, Conversations.KISS_CONVERSATION);
		
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(target, 1000);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, 1000);
		
		assertEquals(Actions.KISS_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}


	private void addActionToHistory(World world, WorldObject performer, WorldObject target, Conversation conversation) {
		world.getHistory().actionPerformed(new OperationInfo(performer, target, Conversations.createArgs(conversation), Actions.TALK_ACTION), new Turn());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject target = createCommoner(world, organization);
		ImproveRelationshipGoal goal = new ImproveRelationshipGoal(target.getProperty(Constants.ID), 500, world);
		WorldObject performer = createCommoner(world, organization);
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(target, 1000);
		assertEquals(true, goal.isGoalMet(performer, world));
	}

	private WorldObject createCommoner(World world, WorldObject organization) {
		int commonerId = commonerGenerator.generateCommoner(0, 0, world, organization, CommonerGenerator.NO_PARENT);
		WorldObject commoner = world.findWorldObjectById(commonerId);
		return commoner;
	}
}