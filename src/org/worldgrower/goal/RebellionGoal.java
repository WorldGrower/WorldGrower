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
import org.worldgrower.attribute.IdList;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class RebellionGoal implements Goal {

	public RebellionGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObject leader = GroupPropertyUtils.getLeaderOfVillagers(world);
		IdList rebels = GroupPropertyUtils.getVillagerRebels(world);
		
		if (RebellionPropertyUtils.wantsToRebel(performer, leader)) {
			List<WorldObject> targets = GoalUtils.findNearestTargetsByProperty(performer, Actions.TALK_ACTION, Constants.STRENGTH, w -> isTargetForRebellionConversation(performer, w, leader, rebels, world) && Constants.RELATIONSHIP_VALUE.isAtMin(w.getProperty(Constants.RELATIONSHIPS).getValue(leader)), world);
			if (targets.size() > 0) {
				WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
				int[] args = Conversations.createArgs(Conversations.START_REBELLION_CONVERSATION, villagersOrganization);
				return new OperationInfo(performer, targets.get(0), args, Actions.TALK_ACTION);
			}
		}
		return null;
	}


	boolean isTargetForRebellionConversation(WorldObject performer, WorldObject target, WorldObject leader, IdList rebels, World world) {
		return !target.equals(performer)
			&& !target.equals(leader) 
			&& !rebels.contains(target)
			&& Conversations.START_REBELLION_CONVERSATION.isConversationAvailable(performer, target, null, world) 
			&& !GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, target)
			&& !Conversations.START_REBELLION_CONVERSATION.previousAnswerWasNegative(performer, target, world);
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		WorldObject leader = GroupPropertyUtils.getLeaderOfVillagers(world);
		IdList rebels = GroupPropertyUtils.getVillagerRebels(world);
		return leader == null || rebels.contains(performer);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_REBELLION);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
