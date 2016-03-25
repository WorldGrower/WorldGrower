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
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.InventoryPropertyUtils;
import org.worldgrower.goal.KnowledgeMapPropertyUtils;
import org.worldgrower.gui.ImageIds;

public class StealAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int index = args[0];
		
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		WorldObjectContainer targetInventory = target.getProperty(Constants.INVENTORY);
		
		WorldObject worldObjectToSteal = targetInventory.get(index);
		boolean isSuccess = isThieverySuccess(performer, world, worldObjectToSteal);
		
		if (isSuccess) {
			WorldObject stolenWorldObject = targetInventory.remove(index);
			performerInventory.add(stolenWorldObject);
			
			InventoryPropertyUtils.cleanupEquipmentSlots(target);
		} else {
			addThievingKnowledge(performer, target, world);
			GroupPropertyUtils.throwPerformerOutGroup(performer, target);
		}
	}

	private void addThievingKnowledge(WorldObject performer, WorldObject target, World world) {
		KnowledgeMapPropertyUtils.everyoneInVicinityKnowsOfEvent(performer, target, world);
		
	}

	private boolean isThieverySuccess(WorldObject performer, World world, WorldObject worldObjectToSteal) {
		int price = worldObjectToSteal.getProperty(Constants.PRICE);
		int weight = getWeight(worldObjectToSteal);
		
		SkillUtils.useSkill(performer, Constants.THIEVERY_SKILL, world.getWorldStateChangedListeners());
		int thievery = Constants.THIEVERY_SKILL.getLevel(performer);
		
		return price + weight < thievery + 5;
	}

	private int getWeight(WorldObject worldObjectToSteal) {
		Integer weightInteger = worldObjectToSteal.getProperty(Constants.WEIGHT);
		return weightInteger != null ? weightInteger.intValue() : 0;
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
	public boolean requiresArguments() {
		return true;
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
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.IRON_GAUNTLETS;
	}
}