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

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.ManagedProperty;

public abstract class AbstractSellGoal implements Goal {

	private final ManagedProperty<?> propertyToSell;
	
	public AbstractSellGoal(ManagedProperty<?> propertyToSell) {
		this.propertyToSell = propertyToSell;
	}

	@Override
	public final OperationInfo calculateGoal(WorldObject performer, World world) {
		OperationInfo sellOperationInfo = BuySellUtils.getSellOperationInfo(performer, world);
		if (sellOperationInfo != null) {
			return sellOperationInfo;
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
