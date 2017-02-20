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
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class SmithGoal implements Goal {

	public SmithGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> unownedSmiths = BuildingGenerator.findUnownedBuildingsForClaiming(performer, Constants.SMITH_QUALITY, w -> BuildingGenerator.isSmithy(w), world);
		if (unownedSmiths.size() > 0) {
			return new OperationInfo(performer, unownedSmiths.get(0), Args.EMPTY, Actions.CLAIM_BUILDING_ACTION);
		} else {
			OperationInfo buyBuildingOperationInfo = HousePropertyUtils.createBuyBuildingOperationInfo(performer, BuildingType.SMITH, world);
			if (buyBuildingOperationInfo != null) {
				return buyBuildingOperationInfo;
			} else {
				return Goals.CREATE_SMITH_GOAL.calculateGoal(performer, world);
			}
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
		defaultGoalMetOrNot(performer, world, goalMet, Constants.SMITH_QUALITY);
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		Integer smithId = BuildingGenerator.getSmithId(performer);
		if (smithId != null) {
			WorldObject smith = world.findWorldObjectById(smithId.intValue());
			return (smith.getProperty(Constants.SMITH_QUALITY) > 0);
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_SMITH);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		Integer smithId = BuildingGenerator.getSmithId(performer);
		return (smithId != null) ? 1 : 0;
	}
}