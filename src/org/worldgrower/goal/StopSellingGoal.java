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
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.conversation.Conversations;

//TODO: check if already asked, goalMet/evaluate should be fast
public class StopSellingGoal implements Goal {

	public StopSellingGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		for(WorldObject nonMember : getNonMembersSellingItems(performer, world)) {
			return new OperationInfo(performer, nonMember, Conversations.createArgs(Conversations.STOP_SELLING_CONVERSATION), Actions.TALK_ACTION);
		}
		return null;
	}

	private List<WorldObject> getNonMembersSellingItems(WorldObject performer, World world) {
		List<WorldObject> organizations = GroupPropertyUtils.findOrganizationsUsingLeader(performer, world);
		
		for(WorldObject organization : organizations) {
			return world.findWorldObjectsByProperty(Constants.STRENGTH, w -> nonMemberSoldItem(organization, w));
		}
		return new ArrayList<>();
	}
	
	private boolean nonMemberSoldItem(WorldObject organization, WorldObject w) {
		return w.hasProperty(Constants.ITEMS_SOLD) && !w.getProperty(Constants.ITEMS_SOLD).isEmpty() && !w.getProperty(Constants.GROUP).contains(organization);
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
		return "trying to stop other sellers";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
