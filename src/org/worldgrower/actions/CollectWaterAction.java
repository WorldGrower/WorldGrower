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
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.generator.WellImageCalculator;
import org.worldgrower.goal.WaterPropertyUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class CollectWaterAction implements ManagedOperation, AnimatedAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventoryPerformer = performer.getProperty(Constants.INVENTORY);
		
		WorldObject collectedWater = Item.WATER.generate(1f);
		int quantity = 1;
		inventoryPerformer.addQuantity(collectedWater, quantity);
		
		if (target.hasProperty(Constants.POISON_DAMAGE) && target.getProperty(Constants.POISON_DAMAGE) > 0) {
			int indexOfWater = inventoryPerformer.getIndexFor(Constants.WATER);
			inventoryPerformer.get(indexOfWater).setProperty(Constants.POISON_DAMAGE, target.getProperty(Constants.POISON_DAMAGE));
		}
		
		//TODO :alcohol level?

		target.increment(Constants.WATER_SOURCE, -1);
		
		target.setProperty(Constants.IMAGE_ID, WellImageCalculator.getImageId(target, world));
	
		world.logAction(this, performer, target, args, quantity + " "+ Constants.WATER + " added to inventory");
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return true;
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, 1);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1);
	}
	
	@Override
	public String getDescription() {
		return "collecting water removes water from a water source and stores it in the inventory";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return WaterPropertyUtils.waterSourceHasEnoughWater(target);
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "collecting water from " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "collect water";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.WATER;
	}
	
	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.DRINK;
	}
	
	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.BLUE_ORB;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
}