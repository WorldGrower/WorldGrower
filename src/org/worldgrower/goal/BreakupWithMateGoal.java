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
import org.worldgrower.attribute.IdMap;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.conversation.Conversations;

public class BreakupWithMateGoal implements Goal {

	public BreakupWithMateGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		Integer performerMateId = performer.getProperty(Constants.MATE_ID);
		if (performerMateId != null) {
			WorldObject target = GoalUtils.findNearestPersonLookingLike(performer, performerMateId, world);
			IdMap relationships = performer.getProperty(Constants.RELATIONSHIPS);
			
			if ((relationships.getValue(target) < -500) || mateHasCheated(performer, target, world)) {
				return new OperationInfo(performer, target, Conversations.createArgs(Conversations.BREAKUP_WITH_MATE_CONVERSATION), Actions.TALK_ACTION);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	boolean mateHasCheated(WorldObject performer, WorldObject target, World world) {
		int becomeMateTurn = performer.getProperty(Constants.MATE_TURN).intValue();
		KnowledgeMap knowledgeMap = performer.getProperty(Constants.KNOWLEDGE_MAP);
		return knowledgeMap.hasEvent(target, t -> t > becomeMateTurn, w -> !w.equals(performer) ,world, Actions.KISS_ACTION, Actions.SEX_ACTION);
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return performer.getProperty(Constants.MATE_ID) == null;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking to breakup with a mate";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return (performer.getProperty(Constants.MATE_ID) == null) ? 1 : 0;
	}
}
