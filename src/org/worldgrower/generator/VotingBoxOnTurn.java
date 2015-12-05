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

	@Override
	public void onTurn(WorldObject worldObject, World world, WorldStateChangedListeners worldStateChangedListeners) {
		
		worldObject.increment(Constants.TURN_COUNTER, 1);
		
		if (VotingPropertyUtils.isVotingdone(worldObject)) {
			int newLeaderId = getLeaderId(worldObject, world);
			WorldObject organization = setLeaderOfOrganization(worldObject, world, newLeaderId);
			
			world.removeWorldObject(worldObject);
			
			fireElectionFinished(worldObject, world, worldStateChangedListeners, newLeaderId, organization);
			
			List<WorldObject> organizationMembers = GroupPropertyUtils.findOrganizationMembers(organization, world);
			KnowledgeMapPropertyUtils.peopleKnowOfProperty(organizationMembers, organization, Constants.ORGANIZATION_LEADER_ID, newLeaderId, world);
		}
	}

	private void fireElectionFinished(WorldObject worldObject, World world, WorldStateChangedListeners worldStateChangedListeners, int newLeaderId, WorldObject organization) {
		IdList candidates = worldObject.getProperty(Constants.CANDIDATES);
		WorldObject winner = world.findWorldObject(Constants.ID, newLeaderId);
		worldStateChangedListeners.fireElectionFinished(winner, organization, candidates.copy());
	}

	private WorldObject setLeaderOfOrganization(WorldObject worldObject, World world, int newLeaderId) {
		int organizationId = worldObject.getProperty(Constants.ORGANIZATION_ID);
		WorldObject organization = world.findWorldObject(Constants.ID, organizationId);
		if (newLeaderId != -1) {
			organization.setProperty(Constants.ORGANIZATION_LEADER_ID, newLeaderId);
		} else {
			organization.setProperty(Constants.ORGANIZATION_LEADER_ID, null);
		}
		return organization;
	}

	private int getLeaderId(WorldObject worldObject, World world) {
		IdMap votes = worldObject.getProperty(Constants.VOTES);
		int newLeaderId = votes.findBestId(w -> true, world);
		return newLeaderId;
	}
}
