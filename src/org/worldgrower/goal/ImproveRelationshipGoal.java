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

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.conversation.Conversations;

public class ImproveRelationshipGoal implements Goal {

	private final int personId;
	private WorldObject target;
	private final int goalRelationshipValue;

	public ImproveRelationshipGoal(int personId, int relationshipValue, World world) {
		this.personId = personId;
		this.target = world.findWorldObjectById(personId);
		this.goalRelationshipValue = relationshipValue;
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		target = GoalUtils.findNearestPersonLookingLike(performer, target.getProperty(Constants.ID), world);
		if (!GoalUtils.actionAlreadyPerformed(performer, target, Actions.TALK_ACTION, Conversations.createArgs(Conversations.COMPLIMENT_CONVERSATION), world)) {
			return new OperationInfo(performer, target, Conversations.createArgs(Conversations.COMPLIMENT_CONVERSATION), Actions.TALK_ACTION);
		} else if (!GoalUtils.actionAlreadyPerformed(performer, target, Actions.TALK_ACTION, Conversations.createArgs(Conversations.FAMILY_CONVERSATION), world)) {
			return new OperationInfo(performer, target, Conversations.createArgs(Conversations.FAMILY_CONVERSATION), Actions.TALK_ACTION);
		} else if (!GoalUtils.actionAlreadyPerformed(performer, target, Actions.TALK_ACTION, Conversations.createArgs(Conversations.PROFESSION_CONVERSATION), world)) {
			return new OperationInfo(performer, target, Conversations.createArgs(Conversations.PROFESSION_CONVERSATION), Actions.TALK_ACTION);
		} else if (Actions.KISS_ACTION.canExecuteIgnoringDistance(performer, target, Args.EMPTY, world)) {
			if (!GoalUtils.actionAlreadyPerformed(performer, target, Actions.TALK_ACTION, Conversations.createArgs(Conversations.KISS_CONVERSATION), world)) {
				return new OperationInfo(performer, target, Conversations.createArgs(Conversations.KISS_CONVERSATION), Actions.TALK_ACTION);
			} else {
				return new OperationInfo(performer, target, Args.EMPTY, Actions.KISS_ACTION);
			}
		}
		return null;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		int relationshipValue = performer.getProperty(Constants.RELATIONSHIPS).getValue(personId);
		return (relationshipValue > goalRelationshipValue);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "improving my relationship with " + target.getProperty(Constants.NAME);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.RELATIONSHIPS).getValue(personId);
	}
}
