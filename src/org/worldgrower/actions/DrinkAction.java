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
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.goal.AlcoholLevelPropertyUtils;
import org.worldgrower.goal.WaterPropertyUtils;
import org.worldgrower.gui.ImageIds;

public class DrinkAction implements ManagedOperation {

	private static final int DISTANCE = 1;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int waterInTarget = target.getProperty(Constants.WATER_SOURCE);

		int waterDrunk = Math.min(100, waterInTarget);
		performer.increment(Constants.WATER, waterDrunk);
		target.increment(Constants.WATER_SOURCE, -waterDrunk);
		
		//TODO: refactor out poison & alcohol code with DrinkFromInventoryAction
		if (target.hasProperty(Constants.POISON_DAMAGE) && target.getProperty(Constants.POISON_DAMAGE) > 0) {
			Conditions.add(performer, Condition.POISONED_CONDITION, 20, world);
			WaterPropertyUtils.everyoneInVicinityKnowsOfPoisoning(performer, target, world);
		}
		
		if (target.hasProperty(Constants.ALCOHOL_LEVEL)) {
			performer.increment(Constants.ALCOHOL_LEVEL, target.getProperty(Constants.ALCOHOL_LEVEL));
			if (performer.getProperty(Constants.ALCOHOL_LEVEL) > AlcoholLevelPropertyUtils.getIntoxicatedLimit(performer)) {
				Conditions.add(performer, Condition.INTOXICATED_CONDITION, Integer.MAX_VALUE, world);
			}
		}
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, DISTANCE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, DISTANCE);
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (target.hasProperty(Constants.WATER_SOURCE)) && (target.getProperty(Constants.WATER_SOURCE) > 0) && WaterPropertyUtils.isWaterSafeToDrink(performer, target);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "drinking from " + target.getProperty(Constants.NAME);
	}
	
	@Override
	public String getSimpleDescription() {
		return "drink";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.WATER;
	}
}