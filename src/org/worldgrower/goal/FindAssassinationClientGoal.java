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

public class FindAssassinationClientGoal implements Goal {

	public FindAssassinationClientGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		IdMap performerRelationShips = performer.getProperty(Constants.RELATIONSHIPS);
		int bestId = performer.getProperty(Constants.RELATIONSHIPS).findBestId(w -> performerRelationShips.getValue(w) > 100, new AssassinClientComparator(world), world);
		if (bestId != -1) {
			WorldObject target = world.findWorldObject(Constants.ID, bestId);
			int worstTargetId = target.getProperty(Constants.RELATIONSHIPS).findWorstId(world);
			WorldObject subject = world.findWorldObject(Constants.ID, worstTargetId);
			return new OperationInfo(performer, target, Conversations.createArgs(Conversations.ASSASSINATE_TARGET_CONVERSATION, subject), Actions.TALK_ACTION);
		}
		return null;
	}

	private static class AssassinClientComparator implements Comparator<WorldObject> {
		private final World world;
		
		public AssassinClientComparator(World world) {
			super();
			this.world = world;
		}

		@Override
		public int compare(WorldObject w1, WorldObject w2) {
			int worstId1 = w1.getProperty(Constants.RELATIONSHIPS).findWorstId(world);
			int worstId2 = w2.getProperty(Constants.RELATIONSHIPS).findWorstId(world);
			
			if ((worstId1 != -1) && (worstId2 != -1)) {
				int relationshipValue1 = w1.getProperty(Constants.RELATIONSHIPS).getValue(worstId1);
				int relationshipValue2 = w2.getProperty(Constants.RELATIONSHIPS).getValue(worstId2);
				return Integer.compare(relationshipValue1, relationshipValue2);
			} else if (worstId1 != -1) {
				return -1;
			} else if (worstId2 != -1) {
				return 1;
			} else {
				return 0;
			}
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return performer.getProperty(Constants.ASSASSINATE_TARGET_ID) != null;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "finding an assassination client";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.ASSASSINATE_TARGET_ID) != null ? 1 : 0;
	}
}
