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
import org.worldgrower.MockCommonerGenerator;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.VotingPropertyUtils;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.personality.PersonalityTrait;
import org.worldgrower.profession.Professions;

public class UTestOrganizationCandidateGoal {

	private OrganizationCandidateGoal goal = Goals.ORGANIZATION_CANDIDATE_GOAL;
	private final CommonerGenerator commonerGenerator = new MockCommonerGenerator();
	
	@Test
	public void testCalculateGoalCandidate() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		VotingPropertyUtils.createVotingBox(performer, organization, world);
		
		assertEquals(Actions.BECOME_LEADER_CANDIDATE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalUnpopularCandidate() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		WorldObject performer = createCommoner(world, organization);
		performer.getProperty(Constants.PERSONALITY).changeValue(PersonalityTrait.POWER_HUNGRY, -1000, "reason");
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		VotingPropertyUtils.createVotingBox(performer, organization, world);
		
		WorldObject target = createCommoner(world, organization);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -1000);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalUnpopularPowerHungryCandidate() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		WorldObject performer = createCommoner(world, organization);
		performer.getProperty(Constants.PERSONALITY).changeValue(PersonalityTrait.POWER_HUNGRY, 1000, "reason");
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		VotingPropertyUtils.createVotingBox(performer, organization, world);
		
		WorldObject target = createCommoner(world, organization);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -1000);
		
		assertEquals(Actions.BECOME_LEADER_CANDIDATE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalUnpopularCandidateAndCurrentLeader() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(1, "TestOrg", Professions.FARMER_PROFESSION, world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		VotingPropertyUtils.createVotingBox(performer, organization, world);
		
		WorldObject target = createCommoner(world, organization);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -1000);
		
		assertEquals(Actions.BECOME_LEADER_CANDIDATE_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testIsGoalMetNoVotingBox() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.create(1, "TestOrg", world);
		WorldObject performer = createCommoner(world, organization);
		
		assertEquals(true, goal.isGoalMet(performer, world));
	}

	@Test
	public void testIsGoalMetVotingBox() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		WorldObject performer = createCommoner(world, organization);
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		VotingPropertyUtils.createVotingBox(performer, organization, world);
		
		assertEquals(false, goal.isGoalMet(performer, world));
	}
	
	@Test
	public void testIsGoalMetVotingBoxAndUnpopular() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(null, "TestOrg", Professions.FARMER_PROFESSION, world);
		WorldObject performer = createCommoner(world, organization);
		performer.getProperty(Constants.PERSONALITY).changeValue(PersonalityTrait.POWER_HUNGRY, -1000, "reason");
		performer.setProperty(Constants.PROFESSION, Professions.FARMER_PROFESSION);
		VotingPropertyUtils.createVotingBox(performer, organization, world);
		
		WorldObject target = createCommoner(world, organization);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -1000);
		
		assertEquals(true, goal.isGoalMet(performer, world));
	}

	
	private WorldObject createCommoner(World world, WorldObject organization) {
		int commonerId = commonerGenerator.generateCommoner(0, 0, world, organization, CommonerGenerator.NO_PARENT);
		WorldObject commoner = world.findWorldObjectById(commonerId);
		return commoner;
	}
}