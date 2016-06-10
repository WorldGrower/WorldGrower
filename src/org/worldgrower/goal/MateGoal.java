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

import java.util.Comparator;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.IdMap;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.personality.PersonalityTrait;

public class MateGoal implements Goal {

	public MateGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		IdMap relationships = performer.getProperty(Constants.RELATIONSHIPS);
		int bestId = getBestMate(performer, world);
		
		if ((bestId != -1) && (relationships.getValue(bestId) > 750)) {
			WorldObject target = GoalUtils.findNearestPersonLookingLike(performer, bestId, world);
			return new OperationInfo(performer, target, Conversations.createArgs(Conversations.PROPOSE_MATE_CONVERSATION), Actions.TALK_ACTION);
		} else if (bestId != -1) {
			return new ImproveRelationshipGoal(bestId, 750, world).calculateGoal(performer, world);
		} else {
			List<WorldObject> targets = GoalUtils.findNearestTargetsByProperty(performer, Actions.TALK_ACTION, Constants.STRENGTH, w -> isPotentialMate(performer, w, world) ,world);
			if (targets.size() > 0) {
				WorldObject target = targets.get(0);
				return new ImproveRelationshipGoal(target.getProperty(Constants.ID), 750, world).calculateGoal(performer, world);
			} else {
				return null;
			}
		}
	}
	
	int getBestMate(WorldObject performer, World world) {
		IdMap relationships = performer.getProperty(Constants.RELATIONSHIPS);
		return relationships.findBestId(w -> isPotentialMate(performer, w, world), new MateComparator(performer), world);
	}

	private boolean isPotentialMate(WorldObject performer, WorldObject w, World world) {
		if (isTargetForProposeMateConversation(performer, w, world)) {
			boolean performerIsHonorable = performer.getProperty(Constants.PERSONALITY).getValue(PersonalityTrait.HONORABLE) > 0;
			if (performerIsHonorable) {
				return RacePropertyUtils.canHaveOffspring(performer, w) && w.getProperty(Constants.MATE_ID) == null;
			} else {
				return RacePropertyUtils.canHaveOffspring(performer, w);
			}
		} else {
			return false;
		}
	}
	
	private boolean isTargetForProposeMateConversation(WorldObject performer, WorldObject target, World world) {
		return !Conversations.PROPOSE_MATE_CONVERSATION.previousAnswerWasNegative(getPreviousResponseIds(performer, target, Conversations.PROPOSE_MATE_CONVERSATION, world));
	}
	
	private static class MateComparator implements Comparator<WorldObject> {

		private final WorldObject performer;
		
		public MateComparator(WorldObject performer) {
			this.performer = performer;
		}

		@Override
		public int compare(WorldObject w1, WorldObject w2) {
			int houseCount1 = w1.getProperty(Constants.BUILDINGS).getIds(BuildingType.HOUSE).size();
			int houseCount2 = w2.getProperty(Constants.BUILDINGS).getIds(BuildingType.HOUSE).size();
			
			if (houseCount1 == houseCount2) {
				boolean performerIsGreedy = performer.getProperty(Constants.PERSONALITY).getValue(PersonalityTrait.GREEDY) > 100;
				if (performerIsGreedy) {
					return Integer.compare(w1.getProperty(Constants.GOLD), w2.getProperty(Constants.GOLD));
				} else {
					IdMap performerRelationships = performer.getProperty(Constants.RELATIONSHIPS);
					return Integer.compare(performerRelationships.getValue(w1), performerRelationships.getValue(w2));
				}
			} else {
				return Integer.compare(houseCount1, houseCount2);
			}
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return performer.getProperty(Constants.MATE_ID) != null;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking to have a mate";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return (performer.getProperty(Constants.MATE_ID) != null) ? 1 : 0;
	}
}
