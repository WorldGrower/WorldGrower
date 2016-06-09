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
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.generator.BuildingGenerator;

public class CaptureCriminalsGoal implements Goal {

	public CaptureCriminalsGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObject jail = BuildingGenerator.findEmptyJail(world);
		if (jail != null) {
			List<WorldObject> targets = GoalUtils.findNearestTargetsByProperty(performer, Actions.CAPTURE_PERSON_ACTION, Constants.STRENGTH, w -> isCriminal(performer, w) && w.getProperty(Constants.CONDITIONS).hasCondition(Condition.UNCONSCIOUS_CONDITION), world);
			if (targets.size() > 0) {
				return new OperationInfo(performer, targets.get(0), Args.EMPTY, Actions.CAPTURE_PERSON_ACTION);
			} else {
				targets = GoalUtils.findNearestTargetsByProperty(performer, Actions.NON_LETHAL_MELEE_ATTACK_ACTION, Constants.STRENGTH, w -> isCriminal(performer, w), world);
				if (targets.size() > 0) {
					return new OperationInfo(performer, targets.get(0), Args.EMPTY, Actions.NON_LETHAL_MELEE_ATTACK_ACTION);
				}
			}
		} else {
			return Goals.JAIL_GOAL.calculateGoal(performer, world);
		}
		return null;
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}
	
	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		List<WorldObject> worldObjects = findCriminals(performer, world);
		return worldObjects.isEmpty();
	}

	private List<WorldObject> findCriminals(WorldObject performer, World world) {
		return GoalUtils.findNearestTargetsByProperty(performer, Actions.NON_LETHAL_MELEE_ATTACK_ACTION, Constants.STRENGTH, w -> isCriminal(performer, w), world);
	}

	private boolean isCriminal(WorldObject performer, WorldObject w) {
		return GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w) && Reach.distance(performer, w) < 10 && w.getProperty(Constants.CREATURE_TYPE) == CreatureType.HUMAN_CREATURE_TYPE;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "capturing criminals";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		List<WorldObject> worldObjects = findCriminals(performer, world);
		return Integer.MAX_VALUE - worldObjects.size();
	}
}
