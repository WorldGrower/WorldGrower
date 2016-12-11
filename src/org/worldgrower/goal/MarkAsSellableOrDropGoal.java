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
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.WorldObjectContainer;

public class MarkAsSellableOrDropGoal implements Goal {

	private final ManagedProperty<?> propertyToSell;
	
	public MarkAsSellableOrDropGoal(ManagedProperty<?> propertyToSell, List<Goal> allGoals) {
		this.propertyToSell = propertyToSell;
		allGoals.add(this);
	}

	@Override
	public final OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		int index = performerInventory.getIndexFor(propertyToSell);
		if (index != -1) {
			if (WeightPropertyUtils.isCarryingTooMuch(performer)) {
				int quantity = performerInventory.get(index).getProperty(Constants.QUANTITY);
				int[] args = new int[] { index, quantity };
				return new OperationInfo(performer, performer, args, Actions.DROP_ITEM_ACTION);
			} else {
				return SellablePropertyUtils.calculateGoal(performer, propertyToSell, world);
			}
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
	public String getDescription() {
		return "getting rid of " + propertyToSell;
	}

	@Override
	public final boolean isGoalMet(WorldObject performer, World world) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		int index = performerInventory.getIndexFor(propertyToSell);
		return index == -1 || SellablePropertyUtils.isItemSellable(performerInventory.get(index));
	}
	
	@Override
	public final int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
