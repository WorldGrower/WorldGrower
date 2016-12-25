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

import java.io.ObjectStreamException;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class MarkInventoryItemAsSellableAction extends InventoryAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int index = args[0];
		int value = args[1];
		WorldObject worldObject = performer.getProperty(Constants.INVENTORY).get(index);
		worldObject.setProperty(Constants.SELLABLE, value == 0);
	}
	
	public static int[] createArgs(int index, boolean value) {
		return new int[] { index, value ? 0 : 1 };
	}

	@Override
	public boolean isValidInventoryItem(WorldObject inventoryItem, WorldObjectContainer inventory, WorldObject performer) {
		return true;
	}
	
	@Override
	public boolean isActionPossibleOnInventoryItem(WorldObject inventoryItem, WorldObjectContainer inventory, WorldObject performer) {
		return true;
	}
	
	@Override
	public String getRequirementsDescription() {
		return "";
	}
	
	@Override
	public String getDescription() {
		return "marks an item as sellable, signaling to others that it is for sale";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "marking an inventory item for sale";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public String getSimpleDescription() {
		return "mark for sale";
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.SILVER_COIN;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.HANDLE_COINS;
	}
}
