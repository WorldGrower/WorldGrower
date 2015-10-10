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
import org.worldgrower.condition.Condition;
import org.worldgrower.gui.ImageIds;

public class DrinkFromInventoryAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int water = performer.getProperty(Constants.WATER);
		
		water = water + 100;

		performer.setProperty(Constants.WATER, water);
		
		int indexOfWater = performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.WATER);
		if (performer.getProperty(Constants.INVENTORY).get(indexOfWater).hasProperty(Constants.POISON_DAMAGE) && performer.getProperty(Constants.INVENTORY).get(indexOfWater).getProperty(Constants.POISON_DAMAGE) > 0) {
			performer.getProperty(Constants.CONDITIONS).addCondition(Condition.POISONED_CONDITION, 20, world);
		}
		
		performer.getProperty(Constants.INVENTORY).removeQuantity(Constants.WATER, 1);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return 0;
	}
	
	@Override
	public String getRequirementsDescription() {
		return "";
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (performer.hasProperty(Constants.INVENTORY)) && (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WATER) > 0);
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
	public ImageIds getImageIds() {
		return ImageIds.WATER;
	}
}