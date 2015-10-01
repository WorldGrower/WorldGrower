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
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.profession.Professions;

public class UTestVotingPropertyUtils {

	@Test
	public void testIsVotingBox() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject nonVotingBox = TestUtils.createIntelligentWorldObject(1, "Test");
		
		assertEquals(false, VotingPropertyUtils.isVotingBox(nonVotingBox));
		
		int votingBoxId = BuildingGenerator.generateVotingBox(0, 0, world);
		WorldObject votingBox = world.findWorldObject(Constants.ID, votingBoxId);
		votingBox.setProperty(Constants.ORGANIZATION_ID, 2);
		assertEquals(true, VotingPropertyUtils.isVotingBox(votingBox));
	}
	
	@Test
	public void testIsVotingBoxForOrganization() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject nonVotingBox = TestUtils.createIntelligentWorldObject(1, "Test");
		WorldObject organization = GroupPropertyUtils.createProfessionOrganization(1, "TestOrg", Professions.FARMER_PROFESSION, world);
		
		assertEquals(false, VotingPropertyUtils.isVotingBoxForOrganization(nonVotingBox, organization));
		
		int votingBoxId = BuildingGenerator.generateVotingBox(0, 0, world);
		WorldObject votingBox = world.findWorldObject(Constants.ID, votingBoxId);
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

	private WorldObject createVotingBox() {
		World world = new WorldImpl(0, 0, null, null);
		int votingBoxId = BuildingGenerator.generateVotingBox(0, 0, world);
		WorldObject votingBox = world.findWorldObject(Constants.ID, votingBoxId);
		return votingBox;
	}
}
