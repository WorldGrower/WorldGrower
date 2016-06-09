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

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.conversation.Conversations;

public class GetDiseaseCuredGoal implements Goal {

	public GetDiseaseCuredGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (MagicSpellUtils.canCast(performer, Actions.CURE_DISEASE_ACTION)) {
			if (Actions.CURE_DISEASE_ACTION.hasRequiredEnergy(performer)) {
				return new OperationInfo(performer, performer, Args.EMPTY, Actions.CURE_DISEASE_ACTION);
			} else {
				return Goals.REST_GOAL.calculateGoal(performer, world);
			}
		} else {
			List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.TALK_ACTION, w -> isTargetForCureDiseaseConversation(performer, w, world), world);
			if (targets.size() > 0) {
				return new OperationInfo(performer, targets.get(0), Conversations.createArgs(Conversations.CURE_DISEASE_CONVERSATION), Actions.TALK_ACTION);
			}
		}
		return null;
	}
	
	private boolean isTargetForCureDiseaseConversation(WorldObject performer, WorldObject target, World world) {
		return !performer.equals(target)
				&& Conversations.CURE_DISEASE_CONVERSATION.isConversationAvailable(performer, target, null, world) 
				&& !GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, target)
				&& !Conversations.CURE_DISEASE_CONVERSATION.previousAnswerWasGetLost(Conversations.CURE_DISEASE_CONVERSATION.getPreviousResponseIds(performer, target, world));
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return !performer.getProperty(Constants.CONDITIONS).hasDiseaseCondition();
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking to have diseases cured";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return (!performer.getProperty(Constants.CONDITIONS).hasDiseaseCondition()) ? 1 : 0;
	}
}
