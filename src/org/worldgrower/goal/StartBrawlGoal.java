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
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class StartBrawlGoal implements Goal {

	public StartBrawlGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (performerHasTalentForBrawling(performer) && isAtMaximumHealth(performer)) {
			if (!BrawlPropertyUtils.isBrawling(performer)) {
				List<WorldObject> targets = GoalUtils.findNearestTargetsByProperty(performer, Actions.NON_LETHAL_MELEE_ATTACK_ACTION, Constants.STRENGTH, w -> isBrawlTarget(performer, w, world), world);
				if (targets.size() > 0) {
					return new OperationInfo(performer, targets.get(0), Conversations.createArgs(Conversations.BRAWL_CONVERSATION), Actions.TALK_ACTION);
				}
			}
		}
		return null;
	}

	private boolean isBrawlTarget(WorldObject performer, WorldObject w, World world) {
		return !w.equals(performer) 
				&& w.hasIntelligence() 
				&& !BrawlPropertyUtils.isBrawling(w)
				&& Actions.TALK_ACTION.canExecuteIgnoringDistance(performer, w, Conversations.createArgs(Conversations.BRAWL_CONVERSATION), world);
	}
	
	private boolean performerHasTalentForBrawling(WorldObject performer) {
		int strength = performer.getProperty(Constants.STRENGTH);
		int constitution = performer.getProperty(Constants.CONSTITUTION);
		return strength + constitution > 20;
	}
	
	private boolean isAtMaximumHealth(WorldObject performer) {
		int hitPoints = performer.getProperty(Constants.HIT_POINTS);
		int hitPointsMax = performer.getProperty(Constants.HIT_POINTS_MAX);
		return hitPoints == hitPointsMax;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return BrawlPropertyUtils.isBrawling(performer);
		
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_START_BRAWL);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
