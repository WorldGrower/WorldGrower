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
import org.worldgrower.personality.PersonalityTrait;

public class DrinkWaterGoal implements Goal {

	private static final int QUANTITY_TO_BUY = 5;
	
	public DrinkWaterGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> targets = BuySellUtils.findBuyTargets(performer, Constants.WATER, QUANTITY_TO_BUY, world);
		if (targets.size() > 0) {
			return new OperationInfo(performer, targets.get(0), new int[] { targets.get(0).getProperty(Constants.INVENTORY).getIndexFor(Constants.WATER), QUANTITY_TO_BUY }, Actions.BUY_ACTION);
		} else {
			boolean hasInventoryWater = performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WATER) > 0;
			WorldObject waterSourcetarget = WaterPropertyUtils.findWaterSource(performer, world);
			if (hasInventoryWater) {
				int indexOfWater = performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.WATER);
				return new OperationInfo(performer, performer, new int[]{indexOfWater}, Actions.DRINK_FROM_INVENTORY_ACTION);
			} else if (waterSourcetarget != null) {
				return new OperationInfo(performer, waterSourcetarget, Args.EMPTY, Actions.DRINK_ACTION);
			} else {
				if (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WOOD) < 6) {
					return Goals.WOOD_GOAL.calculateGoal(performer, world);
				} else {
					WorldObject targetLocation = BuildLocationUtils.findOpenLocationNearExistingProperty(performer, 2, 2, world);
					if (targetLocation != null) {
						List<WorldObject> existingWells = getExistingWellsNearTargetLocation(performer, targetLocation, world);
						if (existingWells.size() > 0) {
							return new OperationInfo(performer, existingWells.get(0), Args.EMPTY, Actions.DRINK_ACTION);
						} else {
							return new OperationInfo(performer, targetLocation, Args.EMPTY, Actions.BUILD_WELL_ACTION);
						}
					} else {
						return null;
					}
				}
			}
		}
	}
	
	private List<WorldObject> getExistingWellsNearTargetLocation(WorldObject performer, WorldObject targetLocation, World world) {
		List<WorldObject> existingWells = world.findWorldObjects(w -> w.hasProperty(Constants.WATER_SOURCE) && Reach.distance(targetLocation, w) < 10 && WaterPropertyUtils.isWaterSafeToDrink(performer, w));
		return existingWells;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
		defaultGoalMetOrNot(performer, world, goalMet, Constants.WATER);
		changePersonality(performer, PersonalityTrait.GREEDY, 10, goalMet, "thirsty", world);
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return performer.getProperty(Constants.WATER) > 750;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return performer.getProperty(Constants.WATER) > 250;
	}

	@Override
	public String getDescription() {
		return "thirsty and looking for water";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.WATER);
	}
}
