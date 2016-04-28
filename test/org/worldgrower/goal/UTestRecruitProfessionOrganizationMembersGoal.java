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
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.MockCommonerNameGenerator;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.gui.CommonerImageIds;
import org.worldgrower.profession.Professions;

public class UTestRecruitProfessionOrganizationMembersGoal {

	private RecruitProfessionOrganizationMembersGoal goal = Goals.RECRUIT_PROFESSION_ORGANIZATION_MEMBERS_GOAL;
	private final CommonerGenerator commonerGenerator = new CommonerGenerator(666, new CommonerImageIds(), new MockCommonerNameGenerator());
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(1, "TestOrg", Professions.FARMER_PROFESSION, world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		
		performer.getProperty(Constants.GROUP).add(organization);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalOrganizationProfit() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(1, "TestOrg", Professions.FARMER_PROFESSION, world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		performer.getProperty(Constants.GROUP).add(organization);
		
		WorldObject target = createCommoner(world, organization);
		target.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		target.getProperty(Constants.GROUP).add(organization);
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		AssertUtils.assertConversation(goal.calculateGoal(performer, world), Conversations.SET_ORGANIZATION_PROFIT_PERCENTAGE);
	}
	
	@Test
	public void testCalculateGoalJoinUnfriendlyTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(1, "TestOrg", Professions.FARMER_PROFESSION, world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		performer.getProperty(Constants.GROUP).add(organization);
		
		WorldObject target = createCommoner(world, organization);
		target.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		target.getProperty(Constants.GROUP).removeAll();
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		AssertUtils.assertConversation(goal.calculateGoal(performer, world), Conversations.COMPLIMENT_CONVERSATION);
	}

	@Test
	public void testCalculateGoalJoinFriendlyTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(1, "TestOrg", Professions.FARMER_PROFESSION, world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		performer.getProperty(Constants.GROUP).add(organization);
		
		WorldObject target = createCommoner(world, organization);
		target.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		target.getProperty(Constants.GROUP).removeAll();
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, 1000);
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		AssertUtils.assertConversation(goal.calculateGoal(performer, world), Conversations.JOIN_PERFORMER_ORGANIZATION_CONVERSATION);
	}
	
	private WorldObject createCommoner(World world, WorldObject organization) {
		int commonerId = commonerGenerator.generateCommoner(0, 0, world, organization);
		WorldObject commoner = world.findWorldObject(Constants.ID, commonerId);
		return commoner;
	}
}