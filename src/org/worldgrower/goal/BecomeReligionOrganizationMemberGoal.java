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
import org.worldgrower.actions.OrganizationNamer;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.deity.Deity;

public class BecomeReligionOrganizationMemberGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> organizations = GroupPropertyUtils.findReligionOrganizationsInWorld(performer, world);
		if (organizations.size() > 0) {
			Integer leaderId = GroupPropertyUtils.getMostLikedLeaderId(performer, organizations);
			if (leaderId != null) {
				WorldObject organizationLeader = world.findWorldObject(Constants.ID, leaderId);
				int relationshipValue = performer.getProperty(Constants.RELATIONSHIPS).getValue(organizationLeader);
				if (relationshipValue >= 0) {
					return new OperationInfo(performer, organizationLeader, Conversations.createArgs(Conversations.JOIN_TARGET_ORGANIZATION_CONVERSATION, organizations.get(0)), Actions.TALK_ACTION);
				} else {
					return createReligionOrganization(performer, world);
				}
			} else {
				return createReligionOrganization(performer, world);
			}
		} else {
			return createReligionOrganization(performer, world);
		}
	}

	private OperationInfo createReligionOrganization(WorldObject performer, World world) {
		int deityIndex = Deity.ALL_DEITIES.indexOf(performer.getProperty(Constants.DEITY));
		List<String> organizationNames = new OrganizationNamer().getDeityOrganizationNames(performer.getProperty(Constants.DEITY), world);
		int organizationIndex = GroupPropertyUtils.getRandomOrganizationIndex(performer, organizationNames);
		return new OperationInfo(performer, performer, new int[] {deityIndex, organizationIndex, -1}, Actions.CREATE_RELIGION_ORGANIZATION_ACTION);
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return GroupPropertyUtils.isPerformerMemberOfReligionOrganization(performer, world);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking for an religion organization to join";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return GroupPropertyUtils.isPerformerMemberOfReligionOrganization(performer, world) ? 1 : 0;
	}
}
