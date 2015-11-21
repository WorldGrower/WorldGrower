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
import org.worldgrower.conversation.Conversations;

public class SellHouseGoal implements Goal {
	
	public SellHouseGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<Integer> houseIds = performer.getProperty(Constants.HOUSES).getIds();
		if (houseIds.size() > 0) {
			int houseId = houseIds.get(0);
			WorldObject house = world.findWorldObject(Constants.ID, houseId);
			List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.SELL_ACTION, w -> BuySellUtils.worldObjectWillBuyGoods(performer, w, house, world) , world);
			if (targets.size() > 0) {
				return new OperationInfo(performer, targets.get(0), Conversations.createArgs(Conversations.SELL_HOUSE_CONVERSATION), Actions.TALK_ACTION);
			}
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
		return performer.getProperty(Constants.HOUSES).size() == 1;
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
