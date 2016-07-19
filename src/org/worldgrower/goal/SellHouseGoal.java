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
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.conversation.Conversations;

public class SellHouseGoal implements Goal {
	
	public SellHouseGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<Integer> houseIds = performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.HOUSE);
		if (houseIds.size() > 0) {
			int houseId = houseIds.get(0);
			WorldObject house = world.findWorldObjectById(houseId);
			List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.SELL_ACTION, w -> isSellHouseTarget(performer, world, house, w) , world);
			if (targets.size() > 0) {
				return new OperationInfo(performer, targets.get(0), Conversations.createArgs(Conversations.SELL_HOUSE_CONVERSATION), Actions.TALK_ACTION);
			}
		}
		return null;
	}

	private boolean isSellHouseTarget(WorldObject performer, World world, WorldObject house, WorldObject w) {
		return BuySellUtils.buyerWillBuyGoods(performer, w, house, world) && Actions.TALK_ACTION.canExecuteIgnoringDistance(performer, w, Conversations.createArgs(Conversations.SELL_HOUSE_CONVERSATION), world);
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
		return performer.getProperty(Constants.BUILDINGS).getIds(BuildingType.HOUSE).size() == 1;
	}

	@Override
	public String getDescription() {
		return "selling a house";
	}
	
	@Override
	public final int evaluate(WorldObject performer, World world) {
		return 0;
	}


}
