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

public class GetHealedGoal implements Goal {

	public GetHealedGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (MagicSpellUtils.canCast(performer, Actions.MINOR_HEAL_ACTION)) {
			if (Actions.MINOR_HEAL_ACTION.hasRequiredEnergy(performer)) {
				return new OperationInfo(performer, performer, Args.EMPTY, Actions.MINOR_HEAL_ACTION);
			} else {
				return Goals.REST_GOAL.calculateGoal(performer, world);
			}
		} else {
			
			List<WorldObject> targets = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> isTargetForMinorHealConversation(performer, w, world));
			if (targets.size() > 0) {
				return new OperationInfo(performer, targets.get(0), Conversations.createArgs(Conversations.MINOR_HEAL_CONVERSATION), Actions.TALK_ACTION);
			}
		}
		return null;
	}
	
	private boolean isTargetForMinorHealConversation(WorldObject performer, WorldObject target, World world) {
		return MagicSpellUtils.canCast(target, Actions.MINOR_HEAL_ACTION) 
				&& !GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, target)
				&& !Conversations.MINOR_HEAL_CONVERSATION.previousAnswerWasNegative(Conversations.MINOR_HEAL_CONVERSATION.getPreviousResponseIds(performer, target, world));
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		int hitPoints = performer.getProperty(Constants.HIT_POINTS).intValue();
		int maxHitPoints = performer.getProperty(Constants.HIT_POINTS_MAX).intValue();
		return hitPoints == maxHitPoints;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking to get healed";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.HIT_POINTS).intValue();
	}
}
