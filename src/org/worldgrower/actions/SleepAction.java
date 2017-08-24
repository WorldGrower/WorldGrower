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
import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.generator.Item;
import org.worldgrower.goal.EnergyPropertyUtils;
import org.worldgrower.gui.ImageIds;

public class SleepAction implements ManagedOperation, AnimatedAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int energyIncrease = getEnergyIncreaseWhenRestingIn(target);
		
		List<WorldObject> beds = target.getProperty(Constants.INVENTORY).getWorldObjects(Constants.ITEM_ID, Item.BED);
		if (beds.size() > 0) {
			energyIncrease += beds.get(0).getProperty(Constants.SLEEP_COMFORT);
		}
		
		EnergyPropertyUtils.increment(performer, energyIncrease);
		world.logAction(this, performer, target, args, null);
	}
	
	public int getEnergyIncreaseWhenRestingIn(WorldObject target) {
		int sleepComfort = target.getProperty(Constants.SLEEP_COMFORT);
		int energyIncrease = 7 + sleepComfort;
		return energyIncrease;
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, 1);
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		boolean performerOwnsTarget = performer.getProperty(Constants.BUILDINGS).contains(target);
		return performerOwnsTarget;
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1, "a person can only sleep in houses it owns");
	}
	
	@Override
	public String getDescription() {
		return "sleep in a residence to regain energy, the amount of energy regained depends on the sleep comfort of the residence";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (target.hasProperty(Constants.SLEEP_COMFORT));
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "sleeping in " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "sleep";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.SLEEPING_INDICATOR;
	}
	
	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.SLEEPING_INDICATOR_ANIMATION;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
}