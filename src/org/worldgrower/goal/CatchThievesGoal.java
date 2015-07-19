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
import org.worldgrower.conversation.Conversations;
import org.worldgrower.history.HistoryItem;

public class CatchThievesGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<HistoryItem> theftHistoryItems = world.getHistory().findHistoryItems(Actions.STEAL_ACTION);
		
		if (theftHistoryItems.size() > 0) {
			HistoryItem theftHistoryItem = theftHistoryItems.get(0);
			int[] args = Conversations.createArgs(Conversations.BROKEN_LAW_CONVERSATION, theftHistoryItem);
			return new OperationInfo(performer, theftHistoryItem.getOperationInfo().getPerformer(), args, Actions.TALK_ACTION);
		} else {
			return null;
		}
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}
	
	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		List<HistoryItem> theftHistoryItems = world.getHistory().findHistoryItems(Actions.STEAL_ACTION);
		if (theftHistoryItems.isEmpty()) {
			return true;
		} else {
			//TODO: if bounty paid, return true
			return false;
		}
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
		//TODO: if bounty paid, return true
		List<HistoryItem> theftHistoryItems = world.getHistory().findHistoryItems(Actions.STEAL_ACTION);
		return Integer.MAX_VALUE - theftHistoryItems.size();
	}
}
