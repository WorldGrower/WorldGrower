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

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.VotingPropertyUtils;
import org.worldgrower.personality.PersonalityTrait;

public class OrganizationCandidateGoal implements Goal {

	public OrganizationCandidateGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObject votingBox = VotingPropertyUtils.getVotingBox(performer, world);
		
		boolean performerWantsToBecomeCandidate = performerWantsToBecomeCandidate(performer, votingBox, world);
		
		if (performerWantsToBecomeCandidate) {
			return new OperationInfo(performer, votingBox, new int[0] , Actions.BECOME_LEADER_CANDIDATE_ACTION);
		} else {
			return null;
		}
	}
	
	private boolean performerWantsToBecomeCandidate(WorldObject performer, WorldObject votingBox, World world) {
		int organizationId = votingBox.getProperty(Constants.ORGANIZATION_ID);
		WorldObject organization = world.findWorldObject(Constants.ID, organizationId);
		List<WorldObject> organizationMembers = GroupPropertyUtils.findOrganizationMembers(organization, world);
		
		if (isCurrentLeader(performer, organization)) {
			return true;
		} else if (hasBestRelationshipWithMembers(performer, organizationMembers)) {
			return true;
		} else if (wantsToTry(performer, votingBox)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isCurrentLeader(WorldObject performer, WorldObject organization) {
		Integer organizationLeaderId = organization.getProperty(Constants.ORGANIZATION_LEADER_ID);
		return (organizationLeaderId != null) && (organizationLeaderId.intValue() == performer.getProperty(Constants.ID));
	}
	
	private boolean wantsToTry(WorldObject performer, WorldObject votingBox) {
		boolean isPowerHungry = performer.getProperty(Constants.PERSONALITY).getValue(PersonalityTrait.POWER_HUNGRY) > 100;
		int currentNumberOfCandidates = votingBox.getProperty(Constants.CANDIDATES).size();
		
		return isPowerHungry && currentNumberOfCandidates < 3;
	}
	
	private boolean hasBestRelationshipWithMembers(WorldObject target, List<WorldObject> worldObjects) {
		int bestWorldObjectId = -1;
		int bestAverageRelationshipValue = Integer.MIN_VALUE;
		for(WorldObject worldObject : worldObjects) {
			int averageRelationshipValue = getAverageRelationshipValue(worldObject, worldObjects);
			if (averageRelationshipValue > bestAverageRelationshipValue) {
				bestWorldObjectId = worldObject.getProperty(Constants.ID);
				averageRelationshipValue = bestAverageRelationshipValue;
			}
		}
		return target.getProperty(Constants.ID).intValue() == bestWorldObjectId;
	}
	
	private int getAverageRelationshipValue(WorldObject target, List<WorldObject> worldObjects) {
		int total = 0;
		for(WorldObject worldObject : worldObjects) {
			total += worldObject.getProperty(Constants.RELATIONSHIPS).getValue(target);
		}
		return total / worldObjects.size();
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		WorldObject votingBox = VotingPropertyUtils.getVotingBox(performer, world);
		if (votingBox != null && VotingPropertyUtils.votingBoxAcceptsCandidates(votingBox)) {
			boolean performerWantsToBecomeCandidate = performerWantsToBecomeCandidate(performer, votingBox, world);
			if (performerWantsToBecomeCandidate) {
				boolean performerAlreadyVoted = world.getHistory().findHistoryItems(performer, votingBox, Actions.BECOME_LEADER_CANDIDATE_ACTION).size() > 0;
				return performerAlreadyVoted;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "concerned about the organization leadership";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
