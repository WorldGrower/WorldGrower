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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.worldgrower.AssertUtils;
import org.worldgrower.Constants;
import org.worldgrower.MockCommonerGenerator;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdList;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.history.Turn;

public class UTestRebellionGoal {

	private RebellionGoal goal = Goals.REBELLION_GOAL;
	private final CommonerGenerator commonerGenerator = new MockCommonerGenerator();
	
	@Test
	public void testCalculateGoal() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId();
		WorldObject performer = createCommoner(world, villagersOrganization);
		villagersOrganization.setProperty(Constants.ORGANIZATION_LEADER_ID, performer.getProperty(Constants.ID));
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalOneTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId();
		WorldObject performer = createCommoner(world, villagersOrganization);
		WorldObject target = createCommoner(world, villagersOrganization);
		WorldObject leader = createCommoner(world, villagersOrganization);
		villagersOrganization.setProperty(Constants.ORGANIZATION_LEADER_ID, leader.getProperty(Constants.ID));
		
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(leader, -1000);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(leader, -1000);
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(target, goal.calculateGoal(performer, world).getTarget());
		AssertUtils.assertConversation(goal.calculateGoal(performer, world), Conversations.START_REBELLION_CONVERSATION);
		
		goal.calculateGoal(performer, world).perform(world);
		
		List<Integer> expectedIds = Arrays.asList(performer.getProperty(Constants.ID), target.getProperty(Constants.ID));
		assertEquals(expectedIds, villagersOrganization.getProperty(Constants.ORGANIZATION_REBEL_IDS).getIds());
	}

	@Test
	public void testCalculateGoalOneTargetAlreadySaidNo() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId();
		WorldObject performer = createCommoner(world, villagersOrganization);
		WorldObject target = createCommoner(world, villagersOrganization);
		WorldObject leader = createCommoner(world, villagersOrganization);
		villagersOrganization.setProperty(Constants.ORGANIZATION_LEADER_ID, leader.getProperty(Constants.ID));
		
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(leader, -1000);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(leader, -1000);
		
		world.getHistory().setNextAdditionalValue(1);
		world.getHistory().actionPerformed(new OperationInfo(performer, target, Conversations.createArgs(Conversations.START_REBELLION_CONVERSATION), Actions.TALK_ACTION), new Turn());
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId();
		WorldObject performer = createCommoner(world, villagersOrganization);
		
		assertEquals(true, goal.isGoalMet(performer, world));
		
		WorldObject leader = createCommoner(world, villagersOrganization);
		villagersOrganization.setProperty(Constants.ORGANIZATION_LEADER_ID, leader.getProperty(Constants.ID));
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(leader, -1000);
		assertEquals(false, goal.isGoalMet(performer, world));
	}
	
	@Test
	public void testIsTargetForRebellionConversation() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId();
		WorldObject performer = createCommoner(world, villagersOrganization);
		WorldObject target = createCommoner(world, villagersOrganization);
		WorldObject leader = createCommoner(world, villagersOrganization);
		IdList rebels = new IdList();
		
		assertEquals(false, goal.isTargetForRebellionConversation(performer, target, leader, rebels, world));

		villagersOrganization.setProperty(Constants.ORGANIZATION_LEADER_ID, leader.getProperty(Constants.ID));
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(leader, -1000);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(leader, -1000);
		assertEquals(true, goal.isTargetForRebellionConversation(performer, target, leader, rebels, world));
	
		rebels.add(target);
		assertEquals(false, goal.isTargetForRebellionConversation(performer, target, leader, rebels, world));
		
		assertEquals(false, goal.isTargetForRebellionConversation(performer, leader, leader, rebels, world));
		assertEquals(false, goal.isTargetForRebellionConversation(performer, performer, leader, rebels, world));
	
		rebels = new IdList();
		target.getProperty(Constants.GROUP).removeAll();
		assertEquals(false, goal.isTargetForRebellionConversation(performer, target, leader, rebels, world));
	}
	
	private WorldObject createCommoner(World world, WorldObject organization) {
		int commonerId = commonerGenerator.generateCommoner(0, 0, world, organization, CommonerGenerator.NO_PARENT);
		WorldObject commoner = world.findWorldObjectById(commonerId);
		return commoner;
	}
	
	private WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		return organization;
	}
}