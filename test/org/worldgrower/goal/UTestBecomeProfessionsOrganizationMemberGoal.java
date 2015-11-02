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

public class UTestBecomeProfessionsOrganizationMemberGoal {

	private BecomeProfessionOrganizationMemberGoal goal = Goals.BECOME_PROFESSION_ORGANIZATION_MEMBER_GOAL;
	private final CommonerGenerator commonerGenerator = new CommonerGenerator(666, new CommonerImageIds(), new MockCommonerNameGenerator());
	
	@Test
	public void testCalculateGoalCandidateCreateProfessionOrganization() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		
		assertEquals(Actions.CREATE_PROFESSION_ORGANIZATION_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalNoProfessionOrganization() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.create(null, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		
		assertEquals(Actions.CREATE_PROFESSION_ORGANIZATION_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalJoinOrganization() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		performer.getProperty(Constants.GROUP).removeAll();
		
		WorldObject target = createCommoner(world, organization);
		target.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		organization.setProperty(Constants.ORGANIZATION_LEADER_ID, target.getProperty(Constants.ID));
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		AssertUtils.assertConversation(goal.calculateGoal(performer, world), Conversations.JOIN_TARGET_ORGANIZATION_CONVERSATION);
	}
	
	@Test
	public void testCalculateGoalDislikeLeader() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		performer.getProperty(Constants.GROUP).removeAll();
		
		WorldObject target = createCommoner(world, organization);
		target.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		organization.setProperty(Constants.ORGANIZATION_LEADER_ID, target.getProperty(Constants.ID));
		
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(target, -1000);
		
		assertEquals(Actions.CREATE_PROFESSION_ORGANIZATION_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		
		assertEquals(true, goal.isGoalMet(performer, world));
	}
	
	@Test
	public void testIsGoalMetDifferentProfession() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.PROFESSION, Professions.PRIEST_PROFESSION);
		
		assertEquals(false, goal.isGoalMet(performer, world));
	}

	private WorldObject createCommoner(World world, WorldObject organization) {
		int commonerId = commonerGenerator.generateCommoner(0, 0, world, organization);
		WorldObject commoner = world.findWorldObject(Constants.ID, commonerId);
		return commoner;
	}
}