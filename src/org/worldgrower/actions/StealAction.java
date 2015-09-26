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

import org.worldgrower.ArgumentRange;
import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.InventoryPropertyUtils;

public class StealAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int index = args[0];
		
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		WorldObjectContainer targetInventory = target.getProperty(Constants.INVENTORY);
		
		WorldObject worldObjectToSteal = targetInventory.get(index);
		int price = worldObjectToSteal.getProperty(Constants.PRICE);
		Integer weightInteger = worldObjectToSteal.getProperty(Constants.WEIGHT);
		int weight = weightInteger != null ? weightInteger.intValue() : 0;
		
		SkillUtils.useSkill(performer, Constants.THIEVERY_SKILL);
		int thievery = performer.getProperty(Constants.THIEVERY_SKILL).getLevel();
		
		boolean isSuccess = price + weight < thievery + 5;
		
		if (isSuccess) {
			WorldObject stolenWorldObject = targetInventory.remove(index);
			performerInventory.add(stolenWorldObject);
			
			InventoryPropertyUtils.cleanupEquipmentSlots(target);
		} else {
			GroupPropertyUtils.throwPerformerOutGroup(performer, target);
		}
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, 1);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1);
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		ArgumentRange[] argumentRanges = new ArgumentRange[1];
		argumentRanges[0] = new ArgumentRange(0, 100);
		return argumentRanges;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return target.hasProperty(Constants.INVENTORY);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "stealing from " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "steal";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
}