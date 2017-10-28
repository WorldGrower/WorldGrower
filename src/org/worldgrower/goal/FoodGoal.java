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
import org.worldgrower.generator.FoodCooker;
import org.worldgrower.personality.PersonalityTrait;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class FoodGoal implements Goal {

	private static final int QUANTITY_TO_BUY = 5;
	
	public FoodGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		boolean hasInventoryFood = performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FOOD) > 0;
		
		if (hasInventoryFood) {
			boolean hasHouseWithKitchen = HousePropertyUtils.hasHouseWithKitchen(performer, world);
			int indexOfUncookedFood = FoodCooker.getIndexOfUncookedFood(performer.getProperty(Constants.INVENTORY));
			int indexOfCookedFood = FoodCooker.getIndexOfCookedFood(performer.getProperty(Constants.INVENTORY));
			if (indexOfUncookedFood != -1 && hasHouseWithKitchen && indexOfCookedFood == -1) {
				return cookUncookedFood(performer, world, indexOfUncookedFood);
			} else {
				return eatFoodFromInventory(performer, indexOfCookedFood);
				
			}
		} else {
			return acquireFood(performer, world);
		}
	}

	private OperationInfo cookUncookedFood(WorldObject performer, World world, int indexOfUncookedFood) {
		WorldObject houseWithKitchen = HousePropertyUtils.getHouseWithKitchen(performer, world);
		return new OperationInfo(performer, houseWithKitchen, new int[] { indexOfUncookedFood }, Actions.COOK_ACTION);
	}

	private OperationInfo eatFoodFromInventory(WorldObject performer, int indexOfCookedFood) {
		int indexOfFood = performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.FOOD);
		
		if (indexOfCookedFood != -1) {
			return new OperationInfo(performer, performer, new int[] {indexOfCookedFood}, Actions.EAT_FROM_INVENTORY_ACTION);
		} else {
			return new OperationInfo(performer, performer, new int[] {indexOfFood}, Actions.EAT_FROM_INVENTORY_ACTION);
		}
	}

	private OperationInfo acquireFood(WorldObject performer, World world) {
		OperationInfo buyOperationInfo = BuySellUtils.getBuyOperationInfo(performer, Constants.FOOD, QUANTITY_TO_BUY, world);
		if (buyOperationInfo != null) {
			return buyOperationInfo;
		} else {
			WorldObject eatTarget = GoalUtils.findNearestTarget(performer, Actions.EAT_ACTION, world);
			if (eatTarget != null && Reach.distance(performer, eatTarget) < 15) {
				return new OperationInfo(performer, eatTarget, Args.EMPTY, Actions.EAT_ACTION);
			} else {
				return Goals.CREATE_FOOD_SOURCES_GOAL.calculateGoal(performer, world);
			}
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
		defaultGoalMetOrNot(performer, world, goalMet, Constants.FOOD);
		changePersonality(performer, PersonalityTrait.GREEDY, 10, goalMet, "hungry", world);
	}
	
	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return performer.getProperty(Constants.FOOD) > 750;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return performer.getProperty(Constants.FOOD) > 250;
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_FOOD);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.FOOD);
	}
}
