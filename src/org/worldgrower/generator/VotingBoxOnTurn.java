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
package org.worldgrower.generator;

import java.util.ArrayList;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.OnTurn;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.VotingPropertyUtils;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdMap;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.KnowledgeMapPropertyUtils;

public class VotingBoxOnTurn implements OnTurn {

	//used for debugging purposes
	private static final List<String> ELECTION_RESULTS = new ArrayList<>();
	
	@Override
	public void onTurn(WorldObject worldObject, World world, WorldStateChangedListeners worldStateChangedListeners) {
		
		worldObject.increment(Constants.TURN_COUNTER, 1);
		
		if (VotingPropertyUtils.isVotingdone(worldObject, world)) {
			int newLeaderId = getLeaderId(worldObject, world);
			WorldObject organization = setLeaderOfOrganization(worldObject, world, newLeaderId);
		
			ELECTION_RESULTS.add(organization.getProperty(Constants.NAME) + ": " + worldObject.getProperty(Constants.VOTES));
			
			world.removeWorldObject(worldObject);
			
			if (newLeaderId != -1) {
				int electionWonPercentage = calculatePercentageWon(worldObject, newLeaderId);
				fireElectionFinished(worldObject, world, worldStateChangedListeners, newLeaderId, organization, electionWonPercentage);
				
				List<WorldObject> organizationMembers = GroupPropertyUtils.findOrganizationMembers(organization, world);
				KnowledgeMapPropertyUtils.peopleKnowOfProperty(organizationMembers, organization, Constants.ORGANIZATION_LEADER_ID, newLeaderId, world);
			}
		}
	}

	int calculatePercentageWon(WorldObject worldObject, int newLeaderId) {
		IdMap votes = worldObject.getProperty(Constants.VOTES);
		int votesWon = votes.getValue(newLeaderId);
		int totalVotes = votes.getSumOfAllValues();
		return (votesWon *100) / totalVotes;
	}

	private void fireElectionFinished(WorldObject worldObject, World world, WorldStateChangedListeners worldStateChangedListeners, int newLeaderId, WorldObject organization, int electionWonPercentage) {
		IdList candidates = worldObject.getProperty(Constants.CANDIDATES);
		WorldObject winner = world.findWorldObjectById(newLeaderId);
		worldStateChangedListeners.fireElectionFinished(winner, organization, candidates.copy(), electionWonPercentage);
	}

	private WorldObject setLeaderOfOrganization(WorldObject worldObject, World world, int newLeaderId) {
		int organizationId = worldObject.getProperty(Constants.ORGANIZATION_ID);
		WorldObject organization = world.findWorldObjectById(organizationId);
		if (newLeaderId != -1) {
			GroupPropertyUtils.setVillageLeader(newLeaderId, world);
		} else {
			GroupPropertyUtils.setVillageLeader(null, world);
		}
		return organization;
	}

	private int getLeaderId(WorldObject worldObject, World world) {
		IdMap votes = worldObject.getProperty(Constants.VOTES);
		int newLeaderId = votes.findBestId(w -> true, world);
		return newLeaderId;
	}

	public static List<String> getElectionResults() {
		return ELECTION_RESULTS;
	}
}
