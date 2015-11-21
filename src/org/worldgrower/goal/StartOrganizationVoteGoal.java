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
import org.worldgrower.attribute.IdList;

public class StartOrganizationVoteGoal implements Goal {

	public StartOrganizationVoteGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		IdList organizationIds = performer.getProperty(Constants.GROUP);
		
		for(int organizationId : organizationIds.getIds()) {
			WorldObject organization = world.findWorldObject(Constants.ID, organizationId);
			if (organization.getProperty(Constants.ORGANIZATION_LEADER_ID) == null) {
				if (!VotingPropertyUtils.votingBoxExistsForOrganization(organization, world)) {
					return new OperationInfo(performer, performer, new int[] {organizationId}, Actions.START_ORGANIZATION_VOTE_ACTION);
				}
			}
		}
		return null;
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		IdList organizationIds = performer.getProperty(Constants.GROUP);
		
		for(int organizationId : organizationIds.getIds()) {
			WorldObject organization = world.findWorldObject(Constants.ID, organizationId);
			if (organization.getProperty(Constants.ORGANIZATION_LEADER_ID) == null) {
				if (!VotingPropertyUtils.votingBoxExistsForOrganization(organization, world)) {
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "start electing a new leader for an organization";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
