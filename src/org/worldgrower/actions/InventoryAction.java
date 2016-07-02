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
package org.worldgrower.actions;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;

public abstract class InventoryAction implements ManagedOperation {

	@Override
	public final int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return 0;
	}
	
	@Override
	public final boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		int index = args[0];
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		WorldObject worldObject = performerInventory.get(index);
		return isValidInventoryItem(worldObject, performerInventory, performer) && isActionPossibleOnInventoryItem(worldObject, performerInventory, performer);
	}
	
	public abstract boolean isValidInventoryItem(WorldObject inventoryItem, WorldObjectContainer inventory, WorldObject performer);
	public abstract boolean isActionPossibleOnInventoryItem(WorldObject inventoryItem, WorldObjectContainer inventory, WorldObject performer);
	
	@Override
	public boolean requiresArguments() {
		return true;
	}

	@Override
	public final boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (performer.equals(target) && (performer.hasProperty(Constants.INVENTORY)));
	}
}