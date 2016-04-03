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

public class PoisonInventoryWaterWithSleepingPotionAction extends InventoryAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int inventoryIndex = args[0];
		WorldObject waterTarget = performer.getProperty(Constants.INVENTORY).get(inventoryIndex);
		
		WaterPropertyUtils.addSleepingPotionToWaterSource(performer, waterTarget, args, world);
	}
	
	@Override
	public boolean isValidInventoryItem(WorldObject inventoryItem, WorldObjectContainer inventory, WorldObject performer) {
		boolean inventoryContainsSleepingPotion = inventory.getQuantityFor(Constants.SLEEP_INDUCING_DRUG_STRENGTH) > 0;
		return inventoryItem.hasProperty(Constants.WATER) && inventoryContainsSleepingPotion;
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.WATER, 1);
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "adding sleeping potion to inventory water";
	}

	@Override
	public String getSimpleDescription() {
		return "add sleeping potion to inventory water";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.SLEEPING_POTION;
	}
}