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

public class SwitchDeityGoal implements Goal {

	public SwitchDeityGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> targets = findPotentialTargets(performer, world);
		if (targets.size() > 0) {
			return new OperationInfo(performer, targets.get(0), Conversations.createArgs(Conversations.SWITCH_DEITY_CONVERSATION), Actions.TALK_ACTION);
		} else {
			return null;
		}
	}
	
	private List<WorldObject> findPotentialTargets(WorldObject performer, World world) {
		List<WorldObject> potentialTargets = GoalUtils.findNearestTargetsByProperty(performer, Actions.TALK_ACTION, Constants.STRENGTH, w -> isPotentialTarget(performer, w, world), world);
		return potentialTargets;
	}
	
	private boolean isPotentialTarget(WorldObject performer, WorldObject w, World world) {
		return performer.getProperty(Constants.DEITY) != null
				&& w.getProperty(Constants.DEITY) != null
				&& performer.getProperty(Constants.DEITY) != w.getProperty(Constants.DEITY)
				&& !GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w)
				&& !Conversations.SWITCH_DEITY_CONVERSATION.previousAnswerWasGetLost(Conversations.SWITCH_DEITY_CONVERSATION.getPreviousResponseIds(performer, w, world));
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return findPotentialTargets(performer, world).size() == 0;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "convert others to deity";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return Integer.MAX_VALUE - findPotentialTargets(performer, world).size();
	}
}
