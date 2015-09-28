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
import org.worldgrower.attribute.IntProperty;
public abstract class AbstractSellGoal implements Goal {

	private final IntProperty propertyToSell;
	
	public AbstractSellGoal(IntProperty propertyToSell) {
		this.propertyToSell = propertyToSell;
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		int indexOfItemsToSell = performer.getProperty(Constants.INVENTORY).getIndexFor(propertyToSell);
		List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.SELL_ACTION, w -> BuySellUtils.targetWillBuyGoods(performer, w, indexOfItemsToSell, world) , world);
		if (targets.size() > 0) {
			
			int price = BuySellUtils.getPrice(performer, indexOfItemsToSell);
			return new OperationInfo(performer, targets.get(0), new int[] { indexOfItemsToSell, price }, Actions.SELL_ACTION);
		} else {
			return null;
		}
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
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(propertyToSell) < 5;
	}
	
	@Override
	public final int evaluate(WorldObject performer, World world) {
		return Integer.MAX_VALUE - performer.getProperty(Constants.INVENTORY).getQuantityFor(propertyToSell);
	}
}
