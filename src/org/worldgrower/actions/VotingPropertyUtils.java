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
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.creaturetype.CreatureTypeUtils;
import org.worldgrower.generator.BuildingDimensions;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.goal.BuildLocationUtils;
import org.worldgrower.goal.GenderPropertyUtils;
import org.worldgrower.goal.GroupPropertyUtils;

public class VotingPropertyUtils {

	public static boolean isVotingBox(WorldObject target) {
		return (target.hasProperty(Constants.ORGANIZATION_ID)) && (target.getProperty(Constants.ORGANIZATION_ID) != null) && (target.hasProperty(Constants.TURN_COUNTER));
	}
	
	public static boolean isVotingBoxForOrganization(WorldObject w, WorldObject organization) {
		int organizationId = organization.getProperty(Constants.ID);
		return w.hasProperty(Constants.ORGANIZATION_ID) 
				&& w.getProperty(Constants.ORGANIZATION_ID) != null 
				&& w.getProperty(Constants.ORGANIZATION_ID).intValue() == organizationId;
	}
	
	private static int getVotingCandidatePhaseEnd(WorldObject votingBox, World world) {
		int organizationId = votingBox.getProperty(Constants.ORGANIZATION_ID);
		WorldObject organization = world.findWorldObjectById(organizationId);
		return organization.getProperty(Constants.VOTING_CANDIDATE_TURNS);
	}
	
	private static int getVotingPhaseEnd(WorldObject votingBox, World world) {
		int organizationId = votingBox.getProperty(Constants.ORGANIZATION_ID);
		WorldObject organization = world.findWorldObjectById(organizationId);
		return organization.getProperty(Constants.VOTING_TOTAL_TURNS);
	}
	
	public static boolean votingBoxAcceptsCandidates(WorldObject votingBox, World world) {
		return votingBox.hasProperty(Constants.TURN_COUNTER) && votingBox.getProperty(Constants.TURN_COUNTER) < getVotingCandidatePhaseEnd(votingBox, world);
	}
	
	public static boolean votingBoxAcceptsVotes(WorldObject votingBox, World world) {
		int turnCounter = votingBox.getProperty(Constants.TURN_COUNTER);
		return (turnCounter >= getVotingCandidatePhaseEnd(votingBox, world)) && (turnCounter < getVotingPhaseEnd(votingBox, world));
	}

	public static boolean isVotingdone(WorldObject votingBox, World world) {
		int turnCounter = votingBox.getProperty(Constants.TURN_COUNTER);
		return turnCounter >= getVotingPhaseEnd(votingBox, world);
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
	
	public static int getNumberOfTurnsCandidatesMayBeProposed(World world) {
		return GroupPropertyUtils.getVillagersOrganization(world).getProperty(Constants.VOTING_CANDIDATE_TURNS);
	}
	
	public static int createVotingBox(WorldObject target, WorldObject organization, World world) {
		WorldObject location = BuildLocationUtils.findOpenLocationNearExistingProperty(target, BuildingDimensions.VOTING_BOX, world);
		if (location == null) {
			location = BuildLocationUtils.findOpenLocationNearPerformer(target, BuildingDimensions.VOTING_BOX, world);
		}
		int votingBoxId = BuildingGenerator.generateVotingBox(location.getProperty(Constants.X), location.getProperty(Constants.Y), world);
		
		WorldObject votingBox = world.findWorldObjectById(votingBoxId);
		votingBox.setProperty(Constants.ORGANIZATION_ID, organization.getProperty(Constants.ID));
		votingBox.setProperty(Constants.TEXT, "Voting box for " + organization.getProperty(Constants.NAME));
		
		return votingBoxId;
	}
	
	public static boolean canVote(WorldObject worldObject, WorldObject organization, World world) {
		boolean onlyOwnersCanVote = organization.getProperty(Constants.ONLY_OWNERS_CAN_VOTE);
		boolean onlyMalesCanVote = organization.getProperty(Constants.ONLY_MALES_CAN_VOTE);
		boolean onlyFemalesCanVote = organization.getProperty(Constants.ONLY_FEMALES_CAN_VOTE);
		boolean onlyUndeadCanVote = organization.getProperty(Constants.ONLY_UNDEAD_CAN_VOTE);
		
		boolean canVote = true;
		if (onlyOwnersCanVote) {
			canVote = canVote && worldObject.getProperty(Constants.BUILDINGS).count(BuildingType.SHACK, BuildingType.HOUSE) > 0;
		}
		if (onlyMalesCanVote) {
			canVote = canVote && GenderPropertyUtils.isMale(worldObject);
		}
		if (onlyFemalesCanVote) {
			canVote = canVote && GenderPropertyUtils.isFemale(worldObject);
		}
		if (onlyUndeadCanVote) {
			canVote = canVote && CreatureTypeUtils.isUndead(worldObject);
		}
		
		return canVote;
	}
	
	public static boolean canVoteAtVotingBox(WorldObject performer, WorldObject votingBox, World world) {
		int organizationId = votingBox.getProperty(Constants.ORGANIZATION_ID);
		WorldObject organization = world.findWorldObjectById(organizationId);
		return canVote(performer, organization, world);
	}
}
