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
import org.worldgrower.conversation.DemandMoneyConversation;
import org.worldgrower.conversation.GiveMoneyConversation;

public class RedistributeGoldAmongFamilyGoal implements Goal {

	public RedistributeGoldAmongFamilyGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		int performerGold = performer.getProperty(Constants.GOLD);
		Integer mateId = performer.getProperty(Constants.MATE_ID);
		if (mateId != null) {
			WorldObject mate = world.findWorldObject(Constants.ID, mateId);
			int mateGold = mate.getProperty(Constants.GOLD);
			if ((performerGold < 20) && (mateGold > 100) && isTargetForConversation(performer, mate, Conversations.DEMAND_MONEY_CONVERSATION, world)) {
				return new OperationInfo(performer, mate, Conversations.createArgs(Conversations.DEMAND_MONEY_CONVERSATION), Actions.TALK_ACTION);
			}
			if ((performerGold > 100) && (mateGold < 20) && isTargetForConversation(performer, mate, Conversations.GIVE_MONEY_CONVERSATION, world)) {
				return new OperationInfo(performer, mate, Conversations.createArgs(Conversations.GIVE_MONEY_CONVERSATION), Actions.TALK_ACTION);
			}
		}
		
		return null;
	}
		
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}
	
	boolean isTargetForConversation(WorldObject performer, WorldObject target, DemandMoneyConversation conversation, World world) {
		return !GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, target)
				&& !conversation.previousAnswerWasNegative(getPreviousResponseIds(performer, target, conversation, world));
	}
	
	boolean isTargetForConversation(WorldObject performer, WorldObject target, GiveMoneyConversation conversation, World world) {
		return !GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, target)
				&& !conversation.previousAnswerWasNegative(getPreviousResponseIds(performer, target, conversation, world));
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return performer.getProperty(Constants.MATE_ID) == null
				&& performer.getProperty(Constants.CHILDREN).size() == 0;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "redistributing money among family members";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
