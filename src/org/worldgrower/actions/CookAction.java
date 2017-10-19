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
import org.worldgrower.generator.FoodCooker;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class CookAction implements ManagedOperation, AnimatedAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int indexOfFood = args[0];
		WorldObject foodToCook = performer.getProperty(Constants.INVENTORY).get(indexOfFood);
		
		FoodCooker.cook(foodToCook);
		
		world.logAction(this, performer, target, args, foodToCook.getProperty(Constants.NAME) + " added to inventory");
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, 1);
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		boolean performerOwnsTarget = performer.getProperty(Constants.BUILDINGS).contains(target);
		boolean targetHasKitchen = target.getProperty(Constants.INVENTORY).getIndexFor(Constants.COOKING_QUALITY) > -1;
		return performerOwnsTarget && targetHasKitchen;
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1, "a person can only cook in houses it owns", "a person can only cook with a kitchen");
	}
	
	@Override
	public String getDescription() {
		return "cook food to increase its nutritional value";
	}

	@Override
	public boolean requiresArguments() {
		return true;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (target.hasProperty(Constants.SLEEP_COMFORT));
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "cooking in " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "cook";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.COOKING;
	}
	
	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.COOKING_ANIMATION;
	}

	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.COOK;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
}