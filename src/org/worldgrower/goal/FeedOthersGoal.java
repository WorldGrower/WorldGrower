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

public class FeedOthersGoal implements Goal {

	public FeedOthersGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FOOD) < 6) {
			return Goals.GATHER_FOOD_GOAL.calculateGoal(performer, world);
		} else {
			List<WorldObject> targets = findTargetsToFeed(performer, world);
			if (targets.size() > 0) {
				return new OperationInfo(performer, targets.get(0), Conversations.createArgs(Conversations.GIVE_FOOD_CONVERSATION), Actions.TALK_ACTION);
			}
		}
		return null;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return findTargetsToFeed(performer, world).size() == 0;
	}
	
	private List<WorldObject> findTargetsToFeed(WorldObject performer, World world) {
		List<WorldObject> targetsToHeal = GoalUtils.findNearestTargetsByProperty(performer, Actions.TALK_ACTION, Constants.STRENGTH, w -> (!GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w)) && w.getProperty(Constants.FOOD) < 100, world);
		return targetsToHeal;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking to feed the hungry";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return Integer.MAX_VALUE - findTargetsToFeed(performer, world).size();
	}
}
