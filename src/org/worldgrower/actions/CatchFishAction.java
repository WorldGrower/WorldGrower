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
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.generator.ItemGenerator;

public class CatchFishAction implements ManagedOperation {

	private static final int ENERGY_USE = 50;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventoryPerformer = performer.getProperty(Constants.INVENTORY);
		
		WorldObject harvestedFood = ItemGenerator.generateFish();
		int quantity = performer.getProperty(Constants.LEFT_HAND_EQUIPMENT).getProperty(Constants.FISHING_POLE_QUALITY) * 4;
		inventoryPerformer.addQuantity(harvestedFood, quantity);

		world.removeWorldObject(target);
		
		SkillUtils.useEnergy(performer, Constants.FISHING_SKILL, ENERGY_USE);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return AttackUtils.distanceWithLeftHandByProperty(performer, target, Constants.FISHING_POLE_QUALITY, 4)
				+ SkillUtils.distanceForEnergyUse(performer, Constants.FISHING_SKILL, ENERGY_USE);
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (target.hasProperty(Constants.FOOD_SOURCE)) && (target.getProperty(Constants.CREATURE_TYPE) == CreatureType.FISH_CREATURE_TYPE);
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "catching fish";
	}

	@Override
	public String getSimpleDescription() {
		return "catch fish";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
}