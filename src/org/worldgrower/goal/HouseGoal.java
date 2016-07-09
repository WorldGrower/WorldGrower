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
import org.worldgrower.conversation.Conversations;
import org.worldgrower.generator.BuildingGenerator;

public class HouseGoal implements Goal {

	public HouseGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (!GroupPropertyUtils.hasMoneyToPayHouseTaxes(performer, world)) {
			return null;
		} else {
			List<WorldObject> unownedHouses = BuildingGenerator.findUnownedBuildingsForClaiming(performer, Constants.SLEEP_COMFORT, w -> BuildingGenerator.isHouse(w), world);
			if (unownedHouses.size() > 0) {
				return new OperationInfo(performer, unownedHouses.get(0), Args.EMPTY, Actions.CLAIM_BUILDING_ACTION);
			} else {
				List<WorldObject> targets = GoalUtils.findNearestTargetsByProperty(performer, Actions.TALK_ACTION, Constants.STRENGTH, w -> HousePropertyUtils.hasHouseForSale(w, world), world);
				if (targets.size() > 0) {
					return new OperationInfo(performer, targets.get(0), Conversations.createArgs(Conversations.BUY_HOUSE_CONVERSATION), Actions.TALK_ACTION);
				} else { 
					return Goals.CREATE_HOUSE_GOAL.calculateGoal(performer, world);
				}
			}
		}
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
		defaultGoalMetOrNot(performer, world, goalMet, Constants.BUILDINGS);
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		List<Integer> houseIds = performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.HOUSE);
		if (houseIds.size() > 0) {
			int houseId = houseIds.get(0);
			WorldObject house = world.findWorldObjectById(houseId);
			return (BuildingGenerator.isHouse(house));
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
		return "looking to have a house of my own";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.HOUSE).size();
	}
}