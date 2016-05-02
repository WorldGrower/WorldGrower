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

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.goal.BuildLocationUtils;
import org.worldgrower.goal.GroupPropertyUtils;

public class VotingPropertyUtils {

	private static final int VOTING_CANDIDATES_PHASE_END = 300;
	private static final int VOTING_PHASE_END = 600;
	
	public static boolean isVotingBox(WorldObject target) {
		return (target.hasProperty(Constants.ORGANIZATION_ID)) && (target.getProperty(Constants.ORGANIZATION_ID) != null) && (target.hasProperty(Constants.TURN_COUNTER));
	}
	
	public static boolean isVotingBoxForOrganization(WorldObject w, WorldObject organization) {
		int organizationId = organization.getProperty(Constants.ID);
		return w.hasProperty(Constants.ORGANIZATION_ID) 
				&& w.getProperty(Constants.ORGANIZATION_ID) != null 
				&& w.getProperty(Constants.ORGANIZATION_ID).intValue() == organizationId;
	}
	
	public static boolean votingBoxAcceptsCandidates(WorldObject votingBox) {
		return votingBox.hasProperty(Constants.TURN_COUNTER) && votingBox.getProperty(Constants.TURN_COUNTER) < VOTING_CANDIDATES_PHASE_END;
	}
	
	public static boolean votingBoxAcceptsVotes(WorldObject votingBox) {
		int turnCounter = votingBox.getProperty(Constants.TURN_COUNTER);
		return (turnCounter >= VOTING_CANDIDATES_PHASE_END) && (turnCounter < VOTING_PHASE_END);
	}

	public static boolean isVotingdone(WorldObject votingBox) {
		int turnCounter = votingBox.getProperty(Constants.TURN_COUNTER);
		return turnCounter >= VOTING_PHASE_END;
	}
	
	public static WorldObject getVotingBox(WorldObject performer, World world) {
		WorldObject organization = GroupPropertyUtils.findProfessionOrganization(performer, world);
		if (organization != null) {
			List<WorldObject> voteBoxes = world.findWorldObjectsByProperty(Constants.CANDIDATES, w -> VotingPropertyUtils.isVotingBoxForOrganization(w, organization));
			if (voteBoxes.size() == 1) {
				return voteBoxes.get(0);
			}
		}
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		if (villagersOrganization != null) {
			List<WorldObject> voteBoxes = world.findWorldObjectsByProperty(Constants.CANDIDATES, w -> VotingPropertyUtils.isVotingBoxForOrganization(w, villagersOrganization));
			if (voteBoxes.size() == 1) {
				return voteBoxes.get(0);
			}
		}
		return null;
	}
	
	public static boolean votingBoxExistsForOrganization(WorldObject organization, World world) {
		List<WorldObject> voteBoxes = world.findWorldObjectsByProperty(Constants.CANDIDATES, w -> VotingPropertyUtils.isVotingBoxForOrganization(w, organization));
		return voteBoxes.size() > 0;
	}
	
	public static int getNumberOfTurnsCandidatesMayBeProposed() {
		return VOTING_CANDIDATES_PHASE_END;
	}
	
	public static int createVotingBox(WorldObject target, WorldObject organization, World world) {
		WorldObject location = BuildLocationUtils.findOpenLocationNearExistingProperty(target, 2, 2, world);
		int votingBoxId = BuildingGenerator.generateVotingBox(location.getProperty(Constants.X), location.getProperty(Constants.Y), world);
		
		WorldObject votingBox = world.findWorldObject(Constants.ID, votingBoxId);
		votingBox.setProperty(Constants.ORGANIZATION_ID, organization.getProperty(Constants.ID));
		votingBox.setProperty(Constants.TEXT, "Voting box for " + organization.getProperty(Constants.NAME));
		
		return votingBoxId;
	}
}
