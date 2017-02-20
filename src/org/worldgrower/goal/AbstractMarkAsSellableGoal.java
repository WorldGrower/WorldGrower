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

import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public abstract class AbstractMarkAsSellableGoal implements Goal {

	private final ManagedProperty<?> propertyToSell;
	
	public AbstractMarkAsSellableGoal(ManagedProperty<?> propertyToSell) {
		this.propertyToSell = propertyToSell;
	}

	@Override
	public final OperationInfo calculateGoal(WorldObject performer, World world) {
		return SellablePropertyUtils.calculateGoal(performer, propertyToSell, world);
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
		return SellablePropertyUtils.hasNoItemToMarkSellable(performer, propertyToSell, world);
	}

	@Override
	public final FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_MARK_AS_SELLABLE, propertyToSell.getName());
	}
	
	@Override
	public final int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
