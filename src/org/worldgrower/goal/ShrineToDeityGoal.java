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

public class ShrineToDeityGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObject performerDeity = performer.getProperty(Constants.DEITY);
		List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.WORSHIP_DEITY_ACTION, w -> w.getProperty(Constants.DEITY) == performerDeity, world);
		if (targets.size() > 0) {
			return new OperationInfo(performer, targets.get(0), new int[0], Actions.WORSHIP_DEITY_ACTION);
		} else if (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.STONE) < 8) {
				return new StoneGoal().calculateGoal(performer, world);
		} else {
			WorldObject target = BuildLocationUtils.findOpenLocationNearExistingProperty(performer, 2, 3, world);
			return new OperationInfo(performer, target, new int[0], Actions.BUILD_SHRINE_ACTION);
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return (performer.hasProperty(Constants.PLACE_OF_WORSHIP_ID) && performer.getProperty(Constants.PLACE_OF_WORSHIP_ID) != null);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "choosing a deity";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return (performer.getProperty(Constants.PLACE_OF_WORSHIP_ID) != null) ? 1 : 0;
	}
}
