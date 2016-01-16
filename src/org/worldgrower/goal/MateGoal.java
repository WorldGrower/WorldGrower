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
				
		int bestId = relationships.findBestId(w -> RacePropertyUtils.canHaveOffspring(performer, w) && (w.getProperty(Constants.HOUSES).size() > 0), world);
		
		if (bestId == -1) {
			bestId = relationships.findBestId(w -> RacePropertyUtils.canHaveOffspring(performer, w), world);
		}
		
		if ((bestId != -1) && (relationships.getValue(bestId) > 750)) {
			WorldObject target = world.findWorldObject(Constants.ID, bestId);
			return new OperationInfo(performer, target, Conversations.createArgs(Conversations.PROPOSE_MATE_CONVERSATION), Actions.TALK_ACTION);
		} else if (bestId != -1) {
			return new ImproveRelationshipGoal(bestId, 750, world).calculateGoal(performer, world);
		} else {
			List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.SEX_ACTION, w -> RacePropertyUtils.canHaveOffspring(performer, w) ,world);
			if (targets.size() > 0) {
				WorldObject target = targets.get(0);
				return new ImproveRelationshipGoal(target.getProperty(Constants.ID), 750, world).calculateGoal(performer, world);
			} else {
				return null;
			}
		}
	}
	
	private static class MateComparator implements Comparator<WorldObject> {

		private final WorldObject performer;
		
		public MateComparator(WorldObject performer) {
			this.performer = performer;
		}

		@Override
		public int compare(WorldObject w1, WorldObject w2) {
			int houseCount1 = w1.getProperty(Constants.HOUSES).size();
			int houseCount2 = w2.getProperty(Constants.HOUSES).size();
			
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
