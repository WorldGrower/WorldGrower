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
import java.util.Random;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.OrganizationNamer;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.profession.Professions;

public class BecomeOrganizationMemberGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObject organization = GroupPropertyUtils.findProfessionOrganization(performer, world);
		if (organization != null) {
			Integer leaderId = organization.getProperty(Constants.ORGANIZATION_LEADER_ID);
			if (leaderId != null) {
				WorldObject organizationLeader = world.findWorldObject(Constants.ID, leaderId);
				int relationshipValue = performer.getProperty(Constants.RELATIONSHIPS).getValue(organizationLeader);
				if (relationshipValue >= 0) {
					return new OperationInfo(performer, organizationLeader, Conversations.createArgs(Conversations.JOIN_ORGANIZATION_CONVERSATION, organization), Actions.TALK_ACTION);
				} else {
					return createOrganization(performer, world);
				}
			} else {
				return createOrganization(performer, world);
			}
		} else {
			return createOrganization(performer, world);
		}
	}

	private OperationInfo createOrganization(WorldObject performer, World world) {
		int professionIndex = Professions.indexOf(performer.getProperty(Constants.PROFESSION));
		int organizationIndex = getOrganizationIndex(performer, world);
		return new OperationInfo(performer, performer, new int[] {professionIndex, organizationIndex}, Actions.CREATE_ORGANIZATION_ACTION);
	}

	private int getOrganizationIndex(WorldObject performer, World world) {
		List<String> organizationNames = new OrganizationNamer().getNames(performer.getProperty(Constants.PROFESSION), world);
		Random r = new Random();
		int low = 0;
		int high = organizationNames.size() - 1;
		int organizationIndex = r.nextInt(high-low) + low;
		return organizationIndex;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return GroupPropertyUtils.isPerformerMemberOfProfessionOrganization(performer, world);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking for an organization to join";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return GroupPropertyUtils.isPerformerMemberOfProfessionOrganization(performer, world) ? 1 : 0;
	}
}
