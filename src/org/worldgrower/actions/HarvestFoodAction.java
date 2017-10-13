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
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class HarvestFoodAction implements ManagedOperation, AnimatedAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObject harvestedFood = target.getProperty(Constants.FOOD_SOURCE).harvest(performer, target, world);
		SkillUtils.useSkill(performer, Constants.FARMING_SKILL, world.getWorldStateChangedListeners());
		
		int quantity = harvestedFood.getProperty(Constants.QUANTITY);
		world.logAction(this, performer, target, args, quantity + " "+ harvestedFood.getProperty(Constants.NAME) + " added to inventory");
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
		return "harvest food from target, using a scythe increases the amount of food harvested";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return target.hasProperty(Constants.FOOD_SOURCE) && target.getProperty(Constants.FOOD_SOURCE).hasEnoughFood();
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "harvesting " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "harvest food";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.BERRY;
	}
	
	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.HANDLE_SMALL_LEATHER;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.BLACK_CRESCENT_SLASH;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
}