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
import org.worldgrower.condition.VampireUtils;
import org.worldgrower.personality.PersonalityTrait;

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
				return new OperationInfo(performer, target, new int[0], Actions.VAMPIRE_BITE_ACTION);
			}
		} else {
			boolean performerIsHonorable = performer.getProperty(Constants.PERSONALITY).getValue(PersonalityTrait.HONORABLE) > 0;
			//TODO: implement
			WorldObject target = LocationUtils.findIsolatedPerson(performer, world);
			if (target != null) {
				return new OperationInfo(performer, target, new int[0], Actions.VAMPIRE_BITE_ACTION);
			}
		}
		return null;
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
	public String getDescription() {
		return "looking to bite people";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.VAMPIRE_BLOOD_LEVEL);
	}
}
