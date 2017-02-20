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
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class KillVillagersGoal implements Goal {

	public KillVillagersGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.POISON_DAMAGE) == 0) {
			return Goals.CREATE_POISON_GOAL.calculateGoal(performer, world);
		} else {
			List<WorldObject> targets = GoalUtils.findNearestTargetsByProperty(performer, Actions.POISON_ACTION, Constants.WATER_SOURCE, w -> !w.hasIntelligence(), world);
			if (targets.size() > 0) {
				return new OperationInfo(performer, targets.get(0), Args.EMPTY, Actions.POISON_ACTION);
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
		List<WorldObject> worldObjects = GoalUtils.findNearestTargets(performer, w -> isTarget(w), world);
		return worldObjects.isEmpty();
	}

	boolean isTarget(WorldObject w) {
		return isDeceased(w) || BuildingGenerator.isGrave(w);
	}
	
	private boolean isDeceased(WorldObject worldObject) {
		Boolean isDeceased = worldObject.getProperty(Constants.DECEASED_WORLD_OBJECT);
		return ((isDeceased != null) && (isDeceased.booleanValue()));
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_KILL_VILLAGERS);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
