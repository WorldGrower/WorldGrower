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
import org.worldgrower.conversation.Conversations;
import org.worldgrower.deity.Deity;
import org.worldgrower.generator.CommonerGenerator;

public class UTestBecomeReligionOrganizationMemberGoal {

	private BecomeReligionOrganizationMemberGoal goal = Goals.BECOME_RELIGION_ORGANIZATION_MEMBER_GOAL;
	private final CommonerGenerator commonerGenerator = new MockCommonerGenerator();
	
	@Test
	public void testCalculateGoalCandidateCreateReligionOrganization() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.createReligionOrganization(null, "TestOrg", Deity.ARES, Goals.BRAWL_GOAL, world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.DEITY, Deity.ARES);
		
		assertEquals(Actions.CREATE_RELIGION_ORGANIZATION_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalNoReligionOrganization() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.DEITY, Deity.ARES);
		
		assertEquals(Actions.CREATE_RELIGION_ORGANIZATION_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalJoinOrganization() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.createReligionOrganization(null, "TestOrg", Deity.ARES, Goals.BRAWL_GOAL, world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.DEITY, Deity.ARES);
		performer.getProperty(Constants.GROUP).removeAll();
		
		WorldObject target = createCommoner(world, organization);
		target.setProperty(Constants.DEITY, Deity.ARES);
		organization.setProperty(Constants.ORGANIZATION_LEADER_ID, target.getProperty(Constants.ID));
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		AssertUtils.assertConversation(goal.calculateGoal(performer, world), Conversations.JOIN_TARGET_ORGANIZATION_CONVERSATION);
	}
	
	@Test
	public void testCalculateGoalDislikeLeader() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.createReligionOrganization(null, "TestOrg", Deity.ARES, Goals.BRAWL_GOAL, world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.DEITY, Deity.ARES);
		performer.getProperty(Constants.GROUP).removeAll();
		
		WorldObject target = createCommoner(world, organization);
		target.setProperty(Constants.DEITY, Deity.ARES);
		organization.setProperty(Constants.ORGANIZATION_LEADER_ID, target.getProperty(Constants.ID));
		
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(target, -1000);
		
		assertEquals(Actions.CREATE_RELIGION_ORGANIZATION_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.createReligionOrganization(null, "TestOrg", Deity.ARES, Goals.BRAWL_GOAL, world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.DEITY, Deity.ARES);
		
		assertEquals(true, goal.isGoalMet(performer, world));
	}
	
	@Test
	public void testIsGoalMetDifferentDeity() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.createReligionOrganization(null, "TestOrg", Deity.ARES, Goals.BRAWL_GOAL, world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.DEITY, Deity.HEPHAESTUS);
		
		assertEquals(false, goal.isGoalMet(performer, world));
	}

	private WorldObject createCommoner(World world, WorldObject organization) {
		int commonerId = commonerGenerator.generateCommoner(0, 0, world, organization, CommonerGenerator.NO_PARENT);
		WorldObject commoner = world.findWorldObjectById(commonerId);
		return commoner;
	}
}