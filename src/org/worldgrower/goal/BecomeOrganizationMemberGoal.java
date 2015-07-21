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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectFacade;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.OrganizationNamer;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.profession.Professions;

public class BecomeOrganizationMemberGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObject performerFacade = getPerformerFacade(performer);
		List<WorldObject> organizations = GroupPropertyUtils.findProfessionOrganizationsInWorld(performerFacade, world);
		if (organizations.size() > 0) {
			Collections.sort(organizations, new OrganizationComparator(performer));
			Integer leaderId = organizations.get(0).getProperty(Constants.ORGANIZATION_LEADER_ID);
			if (leaderId != null) {
				WorldObject organizationLeader = world.findWorldObject(Constants.ID, leaderId);
				int relationshipValue = performer.getProperty(Constants.RELATIONSHIPS).getValue(organizationLeader);
				if (relationshipValue >= 0) {
					return new OperationInfo(performer, organizationLeader, Conversations.createArgs(Conversations.JOIN_TARGET_ORGANIZATION_CONVERSATION, organizations.get(0)), Actions.TALK_ACTION);
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

	private WorldObject getPerformerFacade(WorldObject performer) {
		final WorldObject performerFacade;
		if (performer.getProperty(Constants.FACADE) != null) {
			performerFacade = new WorldObjectFacade(performer, performer.getProperty(Constants.FACADE));
		} else {
			performerFacade = performer;
		}
		return performerFacade;
	}

	private static class OrganizationComparator implements  Comparator<WorldObject> {

		private final WorldObject performer;
		
		public OrganizationComparator(WorldObject performer) {
			this.performer = performer;
		}

		@Override
		public int compare(WorldObject organization1, WorldObject organization2) {
			Integer leaderId1 = organization1.getProperty(Constants.ORGANIZATION_LEADER_ID);
			Integer leaderId2 = organization2.getProperty(Constants.ORGANIZATION_LEADER_ID);
			
			int relationshipValue1 = performer.getProperty(Constants.RELATIONSHIPS).getValue(leaderId1);
			int relationshipValue2 = performer.getProperty(Constants.RELATIONSHIPS).getValue(leaderId2);
			
			return Integer.compare(relationshipValue1, relationshipValue2);
		}
		
	}
	
	private OperationInfo createOrganization(WorldObject performer, World world) {
		WorldObject performerToFind = getPerformerFacade(performer);
		int professionIndex = Professions.indexOf(performerToFind.getProperty(Constants.PROFESSION));
		int organizationIndex = getOrganizationIndex(performerToFind, world);
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
		return GroupPropertyUtils.isPerformerMemberOfProfessionOrganization(getPerformerFacade(performer), world);
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
