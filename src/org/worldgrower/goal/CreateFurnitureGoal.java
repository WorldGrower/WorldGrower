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
import org.worldgrower.generator.Item;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class CreateFurnitureGoal implements Goal {

	public CreateFurnitureGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		Integer workbenchId = BuildingGenerator.getWorkbenchId(performer);
		if (workbenchId == null) {
			return Goals.WORKBENCH_GOAL.calculateGoal(performer, world);
		} else {
			if (!hasEnoughBeds(performer)) {
				if (!Actions.CONSTRUCT_BED_ACTION.hasEnoughWood(performer)) {
					return Goals.WOOD_GOAL.calculateGoal(performer, world);
				} else {
					WorldObject workbench = world.findWorldObjectById(workbenchId);
					return new OperationInfo(performer, workbench, Args.EMPTY, Actions.CONSTRUCT_BED_ACTION);
				}
			} else if (!hasEnoughKitchens(performer)) {
				if (!Actions.CONSTRUCT_KITCHEN_ACTION.hasEnoughWood(performer)) {
					return Goals.WOOD_GOAL.calculateGoal(performer, world);
				} else {
					WorldObject workbench = world.findWorldObjectById(workbenchId);
					return new OperationInfo(performer, workbench, Args.EMPTY, Actions.CONSTRUCT_KITCHEN_ACTION);
				}
			}
		}
		return null;
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return hasEnoughBeds(performer);
	}

	private boolean hasEnoughBeds(WorldObject performer) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.SLEEP_COMFORT) > 1;
	}
	
	private boolean hasEnoughKitchens(WorldObject performer) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.COOKING_QUALITY) > 1;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_CREATE_FURNITURE);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.INVENTORY).getWorldObjects(Constants.ITEM_ID, Item.BED).size();
	}
}