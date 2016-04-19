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

public class StartDrinkingContestGoal implements Goal {

	public StartDrinkingContestGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (performerHasTalentForDrinking(performer) && !isDrunk(performer)) {
			if (!DrinkingContestPropertyUtils.isDrinking(performer)) {
				int alcoholCount = performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.ALCOHOL_LEVEL);
				if (alcoholCount < 10) {
					return Goals.WINE_GOAL.calculateGoal(performer, world);
				} else {
					List<WorldObject> targets = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> isDrinkingContestTarget(performer, w, world));
					if (targets.size() > 0) {
						return new OperationInfo(performer, targets.get(0), Conversations.createArgs(Conversations.DRINKING_CONTEST_CONVERSATION), Actions.TALK_ACTION);
					}
				}
			}
		}
		return null;
	}

	private boolean isDrinkingContestTarget(WorldObject performer, WorldObject w, World world) {
		return !w.equals(performer) 
				&& w.hasIntelligence() 
				&& !DrinkingContestPropertyUtils.isDrinking(w) 
				&& w.getProperty(Constants.INVENTORY).getQuantityFor(Constants.ALCOHOL_LEVEL) >= 5
				&& !Conversations.DRINKING_CONTEST_CONVERSATION.previousAnswerWasNegative(getPreviousResponseIds(performer, w, Conversations.DRINKING_CONTEST_CONVERSATION, world));
	}
	
	private boolean performerHasTalentForDrinking(WorldObject performer) {
		int constitution = performer.getProperty(Constants.CONSTITUTION);
		return constitution >= 12;
	}
	
	private boolean isDrunk(WorldObject performer) {
		return performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.INTOXICATED_CONDITION);
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return DrinkingContestPropertyUtils.isDrinking(performer);
		
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking to have a drinking contest";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
