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
import org.worldgrower.condition.VampireUtils;
import org.worldgrower.personality.PersonalityTrait;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class VampireBloodLevelGoal implements Goal {

	public VampireBloodLevelGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		boolean isVampireBiteLegal = VampireUtils.isBitingPeopleLegal(world);
		if (isVampireBiteLegal) {
			WorldObject target = GoalUtils.findNearestTarget(performer, Actions.VAMPIRE_BITE_ACTION, world);
			if (target != null) {
				return new OperationInfo(performer, target, Args.EMPTY, Actions.VAMPIRE_BITE_ACTION);
			}
		} else {
			boolean performerIsHonorable = performer.getProperty(Constants.PERSONALITY).getValue(PersonalityTrait.HONORABLE) > 0;
			final WorldObject target;
			if (performerIsHonorable) {
				target = getNearbyNonIntelligentTarget(performer, world);
			} else {
				target = LocationUtils.findIsolatedPerson(performer, Actions.VAMPIRE_BITE_ACTION, world);
			}
				
			if (target != null) {
				return new OperationInfo(performer, target, Args.EMPTY, Actions.VAMPIRE_BITE_ACTION);
			}
		}
		return null;
	}

	private WorldObject getNearbyNonIntelligentTarget(WorldObject performer, World world) {
		WorldObject target = null;
		List<WorldObject> targets = GoalUtils.findNearestTargetsByProperty(performer, Actions.VAMPIRE_BITE_ACTION, Constants.CREATURE_TYPE, w -> isNearbyNonIntelligentBloodSource(performer, w), world);
		if (targets.size() > 0) {
			target = targets.get(0);
		}
		return target;
	}

	private boolean isNearbyNonIntelligentBloodSource(WorldObject performer, WorldObject w){
		return w.hasProperty(Constants.CREATURE_TYPE) && w.getProperty(Constants.CREATURE_TYPE).hasBlood() && Reach.distance(performer, w) < 20;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return performer.getProperty(Constants.VAMPIRE_BLOOD_LEVEL) > 500;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_VAMPIRE_BLOOD);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.VAMPIRE_BLOOD_LEVEL);
	}
}
