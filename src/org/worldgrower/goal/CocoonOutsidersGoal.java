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
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.condition.Condition;

public class CocoonOutsidersGoal implements Goal {

	public CocoonOutsidersGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.COCOON_ACTION, w -> GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w) && Reach.distance(performer, w) < 10 && w.getProperty(Constants.CONDITIONS).hasCondition(Condition.PARALYZED_CONDITION) && !w.getProperty(Constants.CONDITIONS).hasCondition(Condition.COCOONED_CONDITION), world);
		if (targets.size() > 0) {
			return new OperationInfo(performer, targets.get(0), Args.EMPTY, Actions.COCOON_ACTION);
		} else {
			return null;
		}
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}
	
	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		List<WorldObject> worldObjects = world.findWorldObjects(w -> GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w) && Reach.distance(performer, w) < 10 && w.getProperty(Constants.CONDITIONS).hasCondition(Condition.PARALYZED_CONDITION) && !w.getProperty(Constants.CONDITIONS).hasCondition(Condition.COCOONED_CONDITION));
		return worldObjects.isEmpty();
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "cocooning an outsider";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		//TODO
		return 0;
	}
}
