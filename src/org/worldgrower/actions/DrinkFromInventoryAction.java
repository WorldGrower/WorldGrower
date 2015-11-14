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
import org.worldgrower.condition.Conditions;
import org.worldgrower.goal.AlcoholLevelPropertyUtils;
import org.worldgrower.gui.ImageIds;

public class DrinkFromInventoryAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		performer.increment(Constants.WATER, 100);
		
		int indexOfWater = performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.WATER);
		WorldObject waterTarget = performer.getProperty(Constants.INVENTORY).get(indexOfWater);
		if (waterTarget.hasProperty(Constants.POISON_DAMAGE) && performer.getProperty(Constants.INVENTORY).get(indexOfWater).getProperty(Constants.POISON_DAMAGE) > 0) {
			Conditions.add(performer, Condition.POISONED_CONDITION, 20, world);
		}
		
		if (waterTarget.hasProperty(Constants.ALCOHOL_LEVEL)) {
			performer.increment(Constants.ALCOHOL_LEVEL, waterTarget.getProperty(Constants.ALCOHOL_LEVEL));
			if (performer.getProperty(Constants.ALCOHOL_LEVEL) > AlcoholLevelPropertyUtils.getIntoxicatedLimit(performer)) {
				Conditions.add(performer, Condition.INTOXICATED_CONDITION, Integer.MAX_VALUE, world);
			}
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