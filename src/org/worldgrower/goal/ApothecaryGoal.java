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

public class ApothecaryGoal implements Goal {

	public ApothecaryGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> unownedApothecaries = BuildingGenerator.findUnownedBuildingsForClaiming(performer, Constants.APOTHECARY_QUALITY, w -> BuildingGenerator.isApothecary(w), world);
		if (unownedApothecaries.size() > 0) {
			return new OperationInfo(performer, unownedApothecaries.get(0), Args.EMPTY, Actions.CLAIM_BUILDING_ACTION);
		} else {
			OperationInfo buyBuildingOperationInfo = HousePropertyUtils.createBuyBuildingOperationInfo(performer, BuildingType.APOTHECARY, world);
			if (buyBuildingOperationInfo != null) {
				return buyBuildingOperationInfo;
			} else {
				return Goals.CREATE_APOTHECARY_GOAL.calculateGoal(performer, world);
			}
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
		defaultGoalMetOrNot(performer, world, goalMet, Constants.APOTHECARY_QUALITY);
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		Integer apothecaryId = BuildingGenerator.getApothecaryId(performer);
		if (apothecaryId != null) {
			WorldObject apothecary = world.findWorldObjectById(apothecaryId.intValue());
			return (apothecary.getProperty(Constants.APOTHECARY_QUALITY) > 0);
		}
		return false;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_APOTHECARY);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.APOTHECARY).size();
	}
}