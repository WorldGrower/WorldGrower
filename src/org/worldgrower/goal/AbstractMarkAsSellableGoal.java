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
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IntProperty;

public abstract class AbstractMarkAsSellableGoal implements Goal {

	private final IntProperty propertyToSell;
	
	public AbstractMarkAsSellableGoal(IntProperty propertyToSell) {
		this.propertyToSell = propertyToSell;
	}

	@Override
	public final OperationInfo calculateGoal(WorldObject performer, World world) {
		int indexOfProperty = indexOfProperty(performer);
		return new OperationInfo(performer, performer, new int[] { indexOfProperty }, Actions.MARK_INVENTORY_ITEM_AS_SELLABLE_ACTION);
	}
	
	@Override
	public final boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}
	
	@Override
	public final void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	private WorldObject getWorldObjectFromInventory(WorldObject performer) {
		int indexOfProperty = indexOfProperty(performer);
		if (indexOfProperty != -1) {
			return performer.getProperty(Constants.INVENTORY).get(indexOfProperty);
		} else {
			return null;
		}
	}

	private int indexOfProperty(WorldObject performer) {
		int indexOfProperty = performer.getProperty(Constants.INVENTORY).getIndexFor(propertyToSell);
		return indexOfProperty;
	}
	
	@Override
	public final boolean isGoalMet(WorldObject performer, World world) {
		WorldObject worldObject = getWorldObjectFromInventory(performer);
		if (worldObject != null) {
			return worldObject.hasProperty(Constants.SELLABLE) && worldObject.getProperty(Constants.SELLABLE);
		} else {
			return true;
		}
	}

	@Override
	public final String getDescription() {
		return "marking " + propertyToSell.getName() + " as sellable";
	}
	
	@Override
	public final int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
