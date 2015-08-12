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

public class ChildrenGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		IdMap relationships = performer.getProperty(Constants.RELATIONSHIPS);
				
		int bestId = relationships.findBestId(w -> RacePropertyUtils.canHaveOffspring(performer, w) && (w.getProperty(Constants.HOUSES).size() > 0), world);
		
		if ((bestId != -1) && (relationships.getValue(bestId) > 750)) {
			WorldObject target = world.findWorldObject(Constants.ID, bestId);
			return new OperationInfo(performer, target, new int[0], Actions.SEX_ACTION);
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
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return performer.getProperty(Constants.CHILDREN).size() > 3;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking to have children";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.CHILDREN).size();
	}
}
