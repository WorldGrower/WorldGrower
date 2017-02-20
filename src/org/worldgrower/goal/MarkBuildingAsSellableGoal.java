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
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class MarkBuildingAsSellableGoal implements Goal {
	
	private final BuildingType buildingType;
	
	public MarkBuildingAsSellableGoal(BuildingType buildingType,List<Goal> allGoals) {
		this.buildingType = buildingType;
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> buildings = HousePropertyUtils.getUnmarkedBuildings(performer, buildingType, world);
		if (buildings.size() > 1) {
			WorldObject building = buildings.get(1);
			return new OperationInfo(performer, building, Args.EMPTY, Actions.MARK_AS_SELLABLE_ACTION);
		}
		return null;
	}

	@Override
	public final boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}
	
	@Override
	public final void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public final boolean isGoalMet(WorldObject performer, World world) {
		if (buildingType == BuildingType.HOUSE) {
			return HousePropertyUtils.allHousesButFirstSellable(performer, world);
		} else {
			return HousePropertyUtils.getUnmarkedBuildings(performer, buildingType, world).size() == 0;
		}
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_MARK_BUILDING_AS_SELLABLE, buildingType.getDescription());
	}
	
	@Override
	public final int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
