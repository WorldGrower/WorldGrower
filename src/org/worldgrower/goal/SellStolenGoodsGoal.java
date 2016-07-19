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

public class SellStolenGoodsGoal implements Goal {

	public SellStolenGoodsGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}
	
	@Override
	public final OperationInfo calculateGoal(WorldObject performer, World world) {
		int indexOfItemsToSell = getIndexOfItemsToSell(performer);
		if (indexOfItemsToSell != -1) {
			List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.SELL_ACTION, w -> BuySellUtils.buyerWillBuyGoods(performer, w, indexOfItemsToSell, world) , world);
			if (targets.size() > 0) {
			
				int price = BuySellUtils.getPrice(performer, indexOfItemsToSell);
				return new OperationInfo(performer, targets.get(0), new int[] { indexOfItemsToSell, price }, Actions.SELL_ACTION);
			}
		}
		
		return null;
	}

	private int getIndexOfItemsToSell(WorldObject performer) {
		return performer.getProperty(Constants.INVENTORY).getIndexFor(w -> shouldSell(performer, w));
	}

	private boolean shouldSell(WorldObject performer, WorldObject w) {
		boolean isWorn = EquipmentPropertyUtils.isEquipmentWorn(performer, w);
		boolean isBasicNeed = w.hasProperty(Constants.FOOD) || w.hasProperty(Constants.WATER);
		return !isWorn && !isBasicNeed;
	}
	
	@Override
	public final boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}
	
	@Override
	public String getDescription() {
		return "selling stolen goods";
	}
	
	@Override
	public final void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public final boolean isGoalMet(WorldObject performer, World world) {
		int indexOfItemsToSell = getIndexOfItemsToSell(performer);
		return indexOfItemsToSell == -1;
	}
	
	@Override
	public final int evaluate(WorldObject performer, World world) {
		int indexOfItemsToSell = getIndexOfItemsToSell(performer);
		return indexOfItemsToSell != -1 ? 1 : 0;
	}
}
