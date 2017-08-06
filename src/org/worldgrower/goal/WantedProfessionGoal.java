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
import org.worldgrower.attribute.WantedProfession;
import org.worldgrower.conversation.Conversation;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class WantedProfessionGoal implements Goal {

	public WantedProfessionGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WantedProfession wantedProfession = performer.getProperty(Constants.WANTED_PROFESSION);
		WorldObject villageLeader = GroupPropertyUtils.getLeaderOfVillagers(world);
		if (canAskToBecomePublicEmployee(performer, world)) {
			Conversation conversation = wantedProfession.getConversation();
			return new OperationInfo(performer, villageLeader, Conversations.createArgs(conversation), Actions.TALK_ACTION);
		} else {
			return null;
		}
	}

	public static boolean canAskToBecomePublicEmployee(WorldObject performer, World world) {
		WantedProfession wantedProfession = performer.getProperty(Constants.WANTED_PROFESSION);
		WorldObject villageLeader = GroupPropertyUtils.getLeaderOfVillagers(world);
		boolean previousAnswerWasNegative = wantedProfession.getConversation().previousAnswerWasNegative(performer, villageLeader, world);
		return !previousAnswerWasNegative && (villageLeader != null);
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
		defaultGoalMetOrNot(performer, world, goalMet, Constants.PAPER);
	}
	
	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return performer.getProperty(Constants.WANTED_PROFESSION) == null;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_WANTED_PROFESSION);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
