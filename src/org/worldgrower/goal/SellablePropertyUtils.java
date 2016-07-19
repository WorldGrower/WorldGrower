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
import org.worldgrower.actions.MarkInventoryItemAsSellableAction;
import org.worldgrower.attribute.ManagedProperty;

public class SellablePropertyUtils {

	public static OperationInfo calculateGoal(WorldObject performer, ManagedProperty<?> propertyToSell, World world) {
		int indexOfProperty = indexOfProperty(performer, propertyToSell);
		if (indexOfProperty != -1) {
			int[] args = MarkInventoryItemAsSellableAction.createArgs(indexOfProperty, true);
			return new OperationInfo(performer, performer, args, Actions.MARK_INVENTORY_ITEM_AS_SELLABLE_ACTION);
		} else {
			return null;
		}
	}
	
	private static int indexOfProperty(WorldObject performer, ManagedProperty<?> propertyToSell) {
		int indexOfProperty = performer.getProperty(Constants.INVENTORY).getIndexFor(propertyToSell);
		return indexOfProperty;
	}
	
	public static WorldObject getWorldObjectFromInventory(WorldObject performer, ManagedProperty<?> propertyToSell) {
		int indexOfProperty = indexOfProperty(performer, propertyToSell);
		if (indexOfProperty != -1) {
			return performer.getProperty(Constants.INVENTORY).get(indexOfProperty);
		} else {
			return null;
		}
	}
	
	public static boolean hasNoItemToMarkSellable(WorldObject performer, ManagedProperty<?> propertyToSell, World world) {
		WorldObject worldObject = SellablePropertyUtils.getWorldObjectFromInventory(performer, propertyToSell);
		if (worldObject != null) {
			return worldObject.hasProperty(Constants.SELLABLE) && worldObject.getProperty(Constants.SELLABLE);
		} else {
			return true;
		}
	}
}
