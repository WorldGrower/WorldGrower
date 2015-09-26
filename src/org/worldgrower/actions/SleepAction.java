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
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.generator.ItemGenerator;

public class SleepAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int sleepComfort = target.getProperty(Constants.SLEEP_COMFORT);
		int energyIncrease = 7 + sleepComfort;
		
		List<WorldObject> beds = target.getProperty(Constants.INVENTORY).getWorldObjects(Constants.NAME, ItemGenerator.BED_NAME);
		if (beds.size() > 0) {
			energyIncrease += beds.get(0).getProperty(Constants.SLEEP_COMFORT);
		}
		
		performer.increment(Constants.ENERGY, energyIncrease);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		int performerOwnsTargetDistance = performer.getProperty(Constants.HOUSES).contains(target) ? 0 : 1;
		return Reach.evaluateTarget(performer, args, target, 1) + performerOwnsTargetDistance;
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1, "a person can only sleep in houses it owns");
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (target.hasProperty(Constants.SLEEP_COMFORT));
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "resting at " + target.getProperty(Constants.NAME);
	}
	

	@Override
	public String getSimpleDescription() {
		return "rest";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
}