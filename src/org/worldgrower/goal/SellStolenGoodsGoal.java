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
			List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.SELL_ACTION, w -> performerCanSell(performer, world, indexOfItemsToSell, w) , world);
			if (targets.size() > 0) {
				WorldObject target = targets.get(0);
				int[] args = createArgs(performer, indexOfItemsToSell);
				return new OperationInfo(performer, target, args, Actions.SELL_ACTION);
			}
		}
		
		return null;
	}

	private int[] createArgs(WorldObject performer, int indexOfItemsToSell) {
		int price = BuySellUtils.getPrice(performer, indexOfItemsToSell);
		WorldObject sellableWorldObject = performer.getProperty(Constants.INVENTORY).get(indexOfItemsToSell);
		int quantity = sellableWorldObject.getProperty(Constants.QUANTITY);
		int itemId = sellableWorldObject.getProperty(Constants.ITEM_ID).ordinal();
		int[] args = new int[] { indexOfItemsToSell, price, quantity, itemId };
		return args;
	}

	private boolean performerCanSell(WorldObject performer, World world, int indexOfItemsToSell, WorldObject w) {
		int[] args = createArgs(performer, indexOfItemsToSell);
		return Actions.SELL_ACTION.canExecuteIgnoringDistance(performer, w, args, world);
	}

	private int getIndexOfItemsToSell(WorldObject performer) {
		return performer.getProperty(Constants.INVENTORY).getIndexFor(w -> shouldSell(performer, w));
	}

	private boolean shouldSell(WorldObject performer, WorldObject w) {
		boolean isWorn = EquipmentPropertyUtils.isEquipmentWorn(performer, w);
		boolean isBasicNeed = w.hasProperty(Constants.FOOD) || w.hasProperty(Constants.WATER);
		boolean isKey = w.hasProperty(Constants.LOCK_ID);
		return !isWorn && !isBasicNeed && !isKey;
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
