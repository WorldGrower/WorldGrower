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
import org.worldgrower.condition.Condition;
import org.worldgrower.conversation.Conversations;

public class GetPoisonCuredGoal implements Goal {

	public GetPoisonCuredGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (MagicSpellUtils.canCast(performer, Actions.CURE_POISON_ACTION)) {
			if (Actions.CURE_POISON_ACTION.hasRequiredEnergy(performer)) {
				return new OperationInfo(performer, performer, new int[0], Actions.CURE_POISON_ACTION);
			} else {
				return Goals.REST_GOAL.calculateGoal(performer, world);
			}
		} else {
			List<WorldObject> targets = world.findWorldObjects(w -> MagicSpellUtils.canCast(w, Actions.CURE_POISON_ACTION)  && !GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w));
			if (targets.size() > 0) {
				return new OperationInfo(performer, targets.get(0), Conversations.createArgs(Conversations.CURE_POISON_CONVERSATION), Actions.TALK_ACTION);
			}
		}
		return null;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return !performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.POISONED_CONDITION);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking to have my poisoned condition cured";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return (!performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.POISONED_CONDITION)) ? 1 : 0;
	}
}
