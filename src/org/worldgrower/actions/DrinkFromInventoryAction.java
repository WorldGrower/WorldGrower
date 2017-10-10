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
import org.worldgrower.goal.WaterPropertyUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class DrinkFromInventoryAction extends InventoryAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int inventoryIndex = args[0];
		
		performer.increment(Constants.WATER, 150);
		
		WorldObject waterTarget = performer.getProperty(Constants.INVENTORY).get(inventoryIndex);
		WaterPropertyUtils.drink(performer, waterTarget, world);
		
		performer.getProperty(Constants.INVENTORY).removeQuantity(Constants.WATER, 1);
		
		world.logAction(this, performer, target, args, null);
	}

	@Override
	public boolean isValidInventoryItem(WorldObject inventoryItem, WorldObjectContainer inventory, WorldObject performer) {
		return inventoryItem.hasProperty(Constants.WATER);
	}
		
	@Override
	public boolean isActionPossibleOnInventoryItem(WorldObject inventoryItem, WorldObjectContainer inventory, WorldObject performer) {
		return true;
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.WATER, 1);
	}
	
	@Override
	public String getDescription() {
		return "drink water to quench thirst";
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "drinking from " + target.getProperty(Constants.NAME);
	}
	
	@Override
	public String getSimpleDescription() {
		return "drink from inventory";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.WATER;
	}
	
	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.DRINK;
	}
}