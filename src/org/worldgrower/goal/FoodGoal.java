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
import org.worldgrower.personality.PersonalityTrait;

public class FoodGoal implements Goal {

	private static final int QUANTITY_TO_BUY = 5;
	
	public FoodGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		boolean hasInventoryFood = performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FOOD) > 0;
		WorldObject target = GoalUtils.findNearestTarget(performer, Actions.EAT_ACTION, world);
		OperationInfo buyOperationInfo = BuySellUtils.getBuyOperationInfo(performer, Constants.FOOD, QUANTITY_TO_BUY, world);
		if (hasInventoryFood) {
			int indexOfFood = performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.FOOD);
			return new OperationInfo(performer, performer, new int[] {indexOfFood}, Actions.EAT_FROM_INVENTORY_ACTION);
		} else if (buyOperationInfo != null) {
			return buyOperationInfo;
		} else if (target != null) {
			return new OperationInfo(performer, target, new int[0], Actions.EAT_ACTION);
		} else {
			return null;
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
	public String getDescription() {
		return "hungry and looking for food";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.FOOD);
	}
}
