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

import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class MarkNonEquipedItemsAsSellableGoal implements Goal {
	
	private final List<ManagedProperty<?>> sellingProperties;
	
	public MarkNonEquipedItemsAsSellableGoal(List<ManagedProperty<?>> sellingProperties) {
		this.sellingProperties = sellingProperties;
	}

	@Override
	public final OperationInfo calculateGoal(WorldObject performer, World world) {
		for(ManagedProperty<?> propertyToSell : sellingProperties) {
			OperationInfo sellOperationInfo = SellablePropertyUtils.calculateGoal(performer, propertyToSell, world);
			if (sellOperationInfo != null) {
				return sellOperationInfo;
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
		for(ManagedProperty<?> propertyToSell : sellingProperties) {
			boolean hasNoItemToMarkSellable = SellablePropertyUtils.hasNoItemToMarkSellable(performer, propertyToSell, world);
			if (!hasNoItemToMarkSellable) {
				return false;
			}
		}
		return true;
	}

	@Override
	public final FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_MARK_NON_EQUIPED_ITEMS_AS_SELLABLE);
	}
	
	@Override
	public final int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
