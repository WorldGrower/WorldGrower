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
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class RepairEquipmentInInventoryAction extends InventoryAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int inventoryIndex = args[0];
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		WorldObject damagedEquipment = inventory.get(inventoryIndex);
		
		double skillBonus = SkillUtils.useSkill(performer, Constants.SMITHING_SKILL, world.getWorldStateChangedListeners());
		damagedEquipment.increment(Constants.EQUIPMENT_HEALTH, (int)(100 * skillBonus));
		
		inventory.removeQuantity(Constants.REPAIR_QUALITY, 1);
	}

	@Override
	public boolean isValidInventoryItem(WorldObject inventoryItem, WorldObjectContainer inventory, WorldObject performer) {
		boolean damagedEquipment = inventoryItem.hasProperty(Constants.EQUIPMENT_HEALTH) && inventoryItem.getProperty(Constants.EQUIPMENT_HEALTH) < 1000;
		return damagedEquipment;
	}
	
	@Override
	public boolean isActionPossibleOnInventoryItem(WorldObject inventoryItem, WorldObjectContainer inventory, WorldObject performer) {
		int numberOfRepairTools = inventory.getQuantityFor(Constants.REPAIR_QUALITY);
		return numberOfRepairTools > 0;
	}
	
	@Override
	public String getRequirementsDescription() {
		return "Requirements: repair hammer : 1, damaged equipment";
	}
	
	@Override
	public String getDescription() {
		return "repair damaged equipment using a repair hammer";
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "repairing inventory equipment";
	}

	@Override
	public String getSimpleDescription() {
		return "repair inventory equipment";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.REPAIR_HAMMER;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.SMITH;
	}
}