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
import java.util.List;

import org.worldgrower.ArgumentRange;
import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.gui.ImageIds;

public class RepairEquipmentInInventoryAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		List<WorldObject> damagedEquipment = getDamagedEquipment(inventory);
		
		double skillBonus = SkillUtils.useSkill(performer, Constants.SMITHING_SKILL);
		damagedEquipment.get(0).increment(Constants.EQUIPMENT_HEALTH, (int)(100 * skillBonus));
		
		inventory.removeQuantity(Constants.REPAIR_QUALITY, 1);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		int numberOfRepairToolsDistance = inventory.getQuantityFor(Constants.REPAIR_QUALITY) > 0 ? 0 : 1;
		List<WorldObject> damagedEquipmentList = getDamagedEquipment(inventory);
		int damagedEquipmentDistance = damagedEquipmentList.size() > 0 ? 0 : 1;
		return numberOfRepairToolsDistance + damagedEquipmentDistance;
	}
	
	@Override
	public String getRequirementsDescription() {
		return "";
	}

	private List<WorldObject> getDamagedEquipment(WorldObjectContainer inventory) {
		List<WorldObject> damagedEquipmentList = inventory.getWorldObjectsByFunction(Constants.EQUIPMENT_HEALTH, w -> w.getProperty(Constants.EQUIPMENT_HEALTH) < 1000);
		return damagedEquipmentList;
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (performer.equals(target) && (performer.hasProperty(Constants.INVENTORY)) && (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.REPAIR_QUALITY) > 0));
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
	public ImageIds getImageIds() {
		return ImageIds.REPAIR_HAMMER;
	}
}