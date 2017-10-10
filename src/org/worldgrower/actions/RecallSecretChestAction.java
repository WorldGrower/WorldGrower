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
import org.worldgrower.goal.GoalUtils;
import org.worldgrower.goal.LocationPropertyUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class RecallSecretChestAction extends InventoryAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int inventoryIndex = args[0];
		WorldObject miniatureChest = performer.getProperty(Constants.INVENTORY).get(inventoryIndex);
		int chestId = miniatureChest.getProperty(Constants.CHEST_ID);
		WorldObject chest = world.findWorldObjectById(chestId);
		int[] openSpace = GoalUtils.findOpenSpace(performer, 1, 1, world);
		if (openSpace != null) {
			int newX = performer.getProperty(Constants.X) + openSpace[0];
			int newY = performer.getProperty(Constants.Y) + openSpace[1];
			LocationPropertyUtils.moveOnscreen(chest, newX, newY, world);
		}
	}
	
	@Override
	public boolean isValidInventoryItem(WorldObject inventoryItem, WorldObjectContainer inventory, WorldObject performer) {
		return inventoryItem.hasProperty(Constants.CHEST_ID);
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
		return "recall secret chest from demiplane in which it resides";
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "recall chest";
	}

	@Override
	public String getSimpleDescription() {
		return "recall chest";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.CHEST;
	}
	
	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.TELEPORT;
	}
}