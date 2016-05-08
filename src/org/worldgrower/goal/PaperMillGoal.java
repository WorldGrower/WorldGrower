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

public class PaperMillGoal implements Goal {

	public PaperMillGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> unownedPapermills = BuildingGenerator.findUnownedBuildingsForClaiming(performer, Constants.PAPER_MILL_QUALITY, w -> BuildingGenerator.isPapermill(w), world);
		if (unownedPapermills.size() > 0) {
			return new OperationInfo(performer, unownedPapermills.get(0), Args.EMPTY, Actions.CLAIM_BUILDING_ACTION);
		} else if (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WOOD) < 4) {
			return Goals.WOOD_GOAL.calculateGoal(performer, world);
		} else {
			WorldObject target = BuildLocationUtils.findOpenLocationNearExistingProperty(performer, 2, 2, world);
			if (target != null) {
				return new OperationInfo(performer, target, Args.EMPTY, Actions.BUILD_PAPER_MILL_ACTION);
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
		Integer paperMillId = BuildingGenerator.getPapermillId(performer);
		if (paperMillId != null) {
			WorldObject paperMill = world.findWorldObjects(w -> w.getProperty(Constants.ID).intValue() == paperMillId.intValue()).get(0);
			return (paperMill.getProperty(Constants.PAPER_MILL_QUALITY) > 0);
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "building a papermill";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		Integer paperMillId = BuildingGenerator.getPapermillId(performer);
		return (paperMillId != null) ? 1 : 0;
	}
}