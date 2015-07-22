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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.profession.Profession;

public class ImproveOrganizationGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> organizations = GroupPropertyUtils.findOrganizationsUsingLeader(performer, world);
		
		for(WorldObject organization : organizations) {
			Profession profession = organization.getProperty(Constants.PROFESSION);
			List<WorldObject> totalPersonsWithProfession = world.findWorldObjects(w -> w.getProperty(Constants.PROFESSION) == profession);
			List<WorldObject> members = GroupPropertyUtils.findOrganizationMembers(organization, world);
			
			if (members.size() > (totalPersonsWithProfession.size() * 0.8)) {
				int profitPercentage = 50;
				return new OperationInfo(performer, members.get(0), Conversations.createArgs(Conversations.SET_ORGANIZATION_PROFIT_PERCENTAGE, organizations.get(0), profitPercentage), Actions.TALK_ACTION);
			} else {
				List<WorldObject> nonMembers = new ArrayList<>(totalPersonsWithProfession);
				nonMembers.removeAll(members);
				Collections.sort(nonMembers, new RelationshipComparator(performer));
				
				if (nonMembers.size() > 0) {
					WorldObject target = nonMembers.get(0);
					int relationshipValue = target.getProperty(Constants.RELATIONSHIPS).getValue(performer);
					if (relationshipValue < 50) {
						return new ImproveRelationshipGoal(target.getProperty(Constants.ID), 200, world).calculateGoal(performer, world);
					} else {
						return new OperationInfo(performer, members.get(0), Conversations.createArgs(Conversations.JOIN_PERFORMER_ORGANIZATION_CONVERSATION, organizations.get(0)), Actions.TALK_ACTION);
					}
				}
			}
		}
		return null;
	}

	private static class RelationshipComparator implements  Comparator<WorldObject> {

		private final WorldObject performer;
		
		public RelationshipComparator(WorldObject performer) {
			this.performer = performer;
		}

		@Override
		public int compare(WorldObject person1, WorldObject person2) {
			int relationshipValue1 = person1.getProperty(Constants.RELATIONSHIPS).getValue(performer);
			int relationshipValue2 = person2.getProperty(Constants.RELATIONSHIPS).getValue(performer);
			
			return Integer.compare(relationshipValue1, relationshipValue2);
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return GroupPropertyUtils.isPerformerMemberOfProfessionOrganization(FacadeUtils.createFacadeForSelf(performer), world);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking to improve organization";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		List<WorldObject> organizations = GroupPropertyUtils.findOrganizationsUsingLeader(performer, world);
		
		int evaluation = 0;
		for(WorldObject organization : organizations) {
			evaluation += GroupPropertyUtils.findOrganizationMembers(organization, world).size();
		}
		return evaluation;
	}
}
