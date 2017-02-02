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
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.curse.Curse;

public class RemoveCurseGoal implements Goal {

	private static final int QUANTITY_TO_BUY = 3;
	
	public RemoveCurseGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		Curse performerCurse = performer.getProperty(Constants.CURSE);
		if (performerCurse != null && performerCurse.performerWantsCurseRemoved(performer, world)) {
			return removeCurse(performer, world);
		} else {
			return null;
		}
	}

	private OperationInfo removeCurse(WorldObject performer, World world) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		if (performerInventory.getQuantityFor(Constants.REMOVE_CURSE) > 0) {
			int index = performerInventory.getIndexFor(Constants.REMOVE_CURSE);
			return new OperationInfo(performer, performer, new int[] { index }, Actions.DRINK_FROM_INVENTORY_ACTION);
		} else if (MagicSpellUtils.canCast(performer, Actions.REMOVE_CURSE_ACTION)) {
			if (Actions.REMOVE_CURSE_ACTION.hasRequiredEnergy(performer)) {
				return new OperationInfo(performer, performer, Args.EMPTY, Actions.REMOVE_CURSE_ACTION);
			} else {
				return Goals.REST_GOAL.calculateGoal(performer, world);
			}
		} else {
			OperationInfo buyRemoveCursePotionOperationInfo = BuySellUtils.getBuyOperationInfo(performer, Constants.REMOVE_CURSE, QUANTITY_TO_BUY, world);
			if (buyRemoveCursePotionOperationInfo != null) {
				return buyRemoveCursePotionOperationInfo;
			}
			
			List<WorldObject> targets = GoalUtils.findNearestTargetsByProperty(performer, Actions.TALK_ACTION, Constants.STRENGTH, w -> isTargetForRemoveCurseConversation(performer, w, world), world);
			if (targets.size() > 0) {
				return new OperationInfo(performer, targets.get(0), Conversations.createArgs(Conversations.REMOVE_CURSE_CONVERSATION), Actions.TALK_ACTION);
			}
		}
		return null;
	}
	
	private boolean isTargetForRemoveCurseConversation(WorldObject performer, WorldObject target, World world) {
		return !performer.equals(target)
				&& Conversations.REMOVE_CURSE_CONVERSATION.isConversationAvailable(performer, target, null, world) 
				&& !GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, target)
				&& !Conversations.REMOVE_CURSE_CONVERSATION.previousAnswerWasNegative(performer, target, world);
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		Curse performerCurse = performer.getProperty(Constants.CURSE);
		return performerCurse == null || !Curse.BESTOWABLE_CURSES.contains(performerCurse);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking to have my curse removed";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return (performer.getProperty(Constants.CURSE) == null) ? 1 : 0;
	}
}
