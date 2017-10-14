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

public class CutWoodAction implements ManagedOperation, AnimatedAction {

	private static final int ENERGY_USE = 50;
	private static final int DISTANCE = 1;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObject harvestedWood = target.getProperty(Constants.WOOD_SOURCE).harvest(performer, target, world);
		
		SkillUtils.useEnergy(performer, Constants.LUMBERING_SKILL, ENERGY_USE, world.getWorldStateChangedListeners());
		int quantity = harvestedWood.getProperty(Constants.QUANTITY);
		world.logAction(this, performer, target, args, quantity + " "+ Constants.WOOD + " added to inventory");
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return target.hasProperty(Constants.WOOD_SOURCE) && target.getProperty(Constants.WOOD_SOURCE).hasEnoughWood();
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return SkillUtils.hasEnoughEnergy(performer, Constants.LUMBERING_SKILL, ENERGY_USE);
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, DISTANCE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, DISTANCE, Constants.ENERGY, ENERGY_USE);
	}
	
	@Override
	public String getDescription() {
		return "remove wood from the target and store it in the inventory";
	}
	
	public boolean hasRequiredEnergy(WorldObject performer) {
		return performer.getProperty(Constants.ENERGY) >= ENERGY_USE;
	}
	
	@Override
	public boolean requiresArguments() {
		return false;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "cutting down the " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "cut wood";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.WOOD;
	}
	
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.CUT_WOOD;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.HORIZONTAL_SLASH;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
}
