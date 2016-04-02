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

public class LibraryGoal implements Goal {

	public LibraryGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WOOD) < 6) {
			return Goals.WOOD_GOAL.calculateGoal(performer, world);
		} else {
			WorldObject targetLocation = BuildLocationUtils.findOpenLocationNearExistingProperty(performer, 2, 3, world);
			if (targetLocation != null) {
				return new OperationInfo(performer, targetLocation, Args.EMPTY, Actions.BUILD_LIBRARY_ACTION);
			} else {
				return null;
			}
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
		defaultGoalMetOrNot(performer, world, goalMet, Constants.WOOD);
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		List<WorldObject> libraries = findLibraries(world);
		return libraries.size() > 0;
	}

	private List<WorldObject> findLibraries(World world) {
		List<WorldObject> libraries = world.findWorldObjects(w -> w.hasProperty(Constants.LIBRARY_QUALITY));
		return libraries;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "building a library";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		List<WorldObject> libraries = findLibraries(world);
		return libraries.size();
	}
}
