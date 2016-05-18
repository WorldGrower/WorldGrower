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
import org.worldgrower.attribute.IdMap;
import org.worldgrower.conversation.Conversations;

public class CatchThievesGoal implements Goal {

	public CatchThievesGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		int thiefId = findThiefId(world);
		
		if (thiefId != -1) {
			WorldObject thief = world.findWorldObject(Constants.ID, thiefId);
			int[] args = Conversations.createArgs(Conversations.BROKEN_LAW_CONVERSATION);
			return new OperationInfo(performer, thief, args, Actions.TALK_ACTION);
		} else {
			return null;
		}
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}
	
	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return findThiefId(world) == -1;
	}

	private int findThiefId(World world) {
		IdMap bountyMap = GroupPropertyUtils.getVillagersOrganization(world).getProperty(Constants.BOUNTY);
		return bountyMap.findBestId(w -> true, world);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "catching thieves";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
