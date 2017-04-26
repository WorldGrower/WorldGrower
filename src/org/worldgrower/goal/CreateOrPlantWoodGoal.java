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
import org.worldgrower.generator.BuildingDimensions;
import org.worldgrower.generator.Item;
import org.worldgrower.terrain.TerrainResource;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class CreateOrPlantWoodGoal implements Goal {

	public CreateOrPlantWoodGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		OperationInfo createWoodOperationInfo = Goals.CREATE_WOOD_GOAL.calculateGoal(performer, world);
		if (createWoodOperationInfo != null && Reach.distance(performer, createWoodOperationInfo.getTarget()) < 11) {
			return createWoodOperationInfo;
		} else {
			WorldObject target = BuildLocationUtils.findOpenLocationNearExistingProperty(performer, BuildingDimensions.TREE, world, TerrainResource.WOOD);
			if (target != null) {
				return new OperationInfo(performer, target, Args.EMPTY, Actions.PLANT_TREE_ACTION);
			}
		}
		return null;
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}
	
	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		int wood = performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WOOD);
		return wood > 5;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_CREATE_OR_PLANT_WOOD, Item.WOOD);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WOOD);
	}
}
