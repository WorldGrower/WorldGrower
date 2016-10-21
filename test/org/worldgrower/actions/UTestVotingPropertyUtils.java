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
package org.worldgrower.actions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.profession.Professions;

public class UTestVotingPropertyUtils {

	@Test
	public void testIsVotingBox() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject nonVotingBox = TestUtils.createIntelligentWorldObject(1, "Test");
		
		assertEquals(false, VotingPropertyUtils.isVotingBox(nonVotingBox));
		
		int votingBoxId = BuildingGenerator.generateVotingBox(0, 0, world);
		WorldObject votingBox = world.findWorldObjectById(votingBoxId);
		votingBox.setProperty(Constants.ORGANIZATION_ID, 2);
		assertEquals(true, VotingPropertyUtils.isVotingBox(votingBox));
	}
	
	@Test
	public void testIsVotingBoxForOrganization() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject nonVotingBox = TestUtils.createIntelligentWorldObject(1, "Test");
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(1, "TestOrg", Professions.FARMER_PROFESSION, world);
		
		assertEquals(false, VotingPropertyUtils.isVotingBoxForOrganization(nonVotingBox, organization));
		
		int votingBoxId = BuildingGenerator.generateVotingBox(0, 0, world);
		WorldObject votingBox = world.findWorldObjectById(votingBoxId);
		votingBox.setProperty(Constants.ORGANIZATION_ID, organization.getProperty(Constants.ID));
		assertEquals(true, VotingPropertyUtils.isVotingBoxForOrganization(votingBox, organization));
	}
	
	@Test
	public void testVotingBoxAcceptsCandidates() {
		WorldObject votingBox = createVotingBox();
		
		assertEquals(true, VotingPropertyUtils.votingBoxAcceptsCandidates(votingBox));
		
		votingBox.setProperty(Constants.TURN_COUNTER, 5000);
		assertEquals(false, VotingPropertyUtils.votingBoxAcceptsCandidates(votingBox));
	}
	
	@Test
	public void testVotingBoxAcceptsVotes() {
		WorldObject votingBox = createVotingBox();
		
		assertEquals(false, VotingPropertyUtils.votingBoxAcceptsVotes(votingBox));
		
		votingBox.setProperty(Constants.TURN_COUNTER, 400);
		assertEquals(true, VotingPropertyUtils.votingBoxAcceptsVotes(votingBox));
		
		votingBox.setProperty(Constants.TURN_COUNTER, 5000);
		assertEquals(false, VotingPropertyUtils.votingBoxAcceptsVotes(votingBox));
	}
	
	@Test
	public void testIsVotingDone() {
		WorldObject votingBox = createVotingBox();
		
		assertEquals(false, VotingPropertyUtils.isVotingdone(votingBox));
		
		votingBox.setProperty(Constants.TURN_COUNTER, 5000);
		assertEquals(true, VotingPropertyUtils.isVotingdone(votingBox));
	}
	
	@Test
	public void testCreateVotingBox() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject target = TestUtils.createSkilledWorldObject(1);
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(1, "TestOrg", Professions.FARMER_PROFESSION, world);
		
		target.setProperty(Constants.X, 0);
		target.setProperty(Constants.Y, 0);
		
		int votingBoxId = VotingPropertyUtils.createVotingBox(target, organization, world);
		WorldObject votingBox = world.findWorldObjectById(votingBoxId);
		assertEquals(organization.getProperty(Constants.ID).intValue(), votingBox.getProperty(Constants.ORGANIZATION_ID).intValue());
	}
	
	@Test
	public void testVotingBoxExistsForOrganization() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(1, "TestOrg", Professions.FARMER_PROFESSION, world);
		
		assertEquals(false, VotingPropertyUtils.votingBoxExistsForOrganization(organization, world));
		
		WorldObject target = TestUtils.createSkilledWorldObject(1);
		target.setProperty(Constants.X, 0);
		target.setProperty(Constants.Y, 0);
		VotingPropertyUtils.createVotingBox(target, organization, world);
		assertEquals(true, VotingPropertyUtils.votingBoxExistsForOrganization(organization, world));
	}

	private WorldObject createVotingBox() {
		World world = new WorldImpl(1, 1, null, null);
		int votingBoxId = BuildingGenerator.generateVotingBox(0, 0, world);
		WorldObject votingBox = world.findWorldObjectById(votingBoxId);
		return votingBox;
	}
	
	@Test
	public void testCanVoteOwner() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		WorldObject target = TestUtils.createSkilledWorldObject(2);
		
		assertEquals(true, VotingPropertyUtils.canVote(target, world));
		
		villagersOrganization.setProperty(Constants.ONLY_OWNERS_CAN_VOTE, true);
		assertEquals(false, VotingPropertyUtils.canVote(target, world));
		
		int houseId = BuildingGenerator.generateHouse(0, 0, world, target);
		target.getProperty(Constants.BUILDINGS).add(houseId, BuildingType.HOUSE);
		assertEquals(true, VotingPropertyUtils.canVote(target, world));
	}
	
	@Test
	public void testCanVoteMale() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		WorldObject target = TestUtils.createSkilledWorldObject(2);
		
		assertEquals(true, VotingPropertyUtils.canVote(target, world));
		
		target.setProperty(Constants.GENDER, "male");
		villagersOrganization.setProperty(Constants.ONLY_FEMALES_CAN_VOTE, true);
		assertEquals(false, VotingPropertyUtils.canVote(target, world));
		
		target.setProperty(Constants.GENDER, "female");
		assertEquals(true, VotingPropertyUtils.canVote(target, world));
	}
	
	@Test
	public void testCanVoteFemale() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		WorldObject target = TestUtils.createSkilledWorldObject(2);
		
		assertEquals(true, VotingPropertyUtils.canVote(target, world));
		
		target.setProperty(Constants.GENDER, "female");
		villagersOrganization.setProperty(Constants.ONLY_MALES_CAN_VOTE, true);
		assertEquals(false, VotingPropertyUtils.canVote(target, world));
		
		target.setProperty(Constants.GENDER, "male");
		assertEquals(true, VotingPropertyUtils.canVote(target, world));
	}
	
	@Test
	public void testCanVoteUndead() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject villagersOrganization = createVillagersOrganization(world);
		WorldObject target = TestUtils.createSkilledWorldObject(2);
		
		assertEquals(true, VotingPropertyUtils.canVote(target, world));
		
		target.setProperty(Constants.CREATURE_TYPE, CreatureType.HUMAN_CREATURE_TYPE);
		villagersOrganization.setProperty(Constants.ONLY_UNDEAD_CAN_VOTE, true);
		assertEquals(false, VotingPropertyUtils.canVote(target, world));
		
		target.setProperty(Constants.CREATURE_TYPE, CreatureType.LICH_CREATURE_TYPE);
		assertEquals(true, VotingPropertyUtils.canVote(target, world));
	}
	
	private WorldObject createVillagersOrganization(World world) {
		world.generateUniqueId(); world.generateUniqueId(); world.generateUniqueId(); 
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		return organization;
	}
}
