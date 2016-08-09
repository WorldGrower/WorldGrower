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
import org.worldgrower.gui.AnimationId;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class CreateBloodAction implements ManagedOperation, AnimatedAction {

	private static final int DISTANCE = 1;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventoryPerformer = performer.getProperty(Constants.INVENTORY);
		
		WorldObject collectedBlood = Item.BLOOD.generate(1f);
		collectedBlood.setProperty(Constants.CREATURE_TYPE, performer.getProperty(Constants.CREATURE_TYPE));
		
		inventoryPerformer.addQuantity(collectedBlood, 1);
		
		target.increment(Constants.HIT_POINTS, -1 * Item.COMBAT_MULTIPLIER);
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		boolean targetHasEnoughHitPoints = target.getProperty(Constants.HIT_POINTS) > 1 * Item.COMBAT_MULTIPLIER;
		boolean performerIsVampire = performer.hasProperty(Constants.VAMPIRE_BLOOD_LEVEL);
		return targetHasEnoughHitPoints && performerIsVampire;
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, DISTANCE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return "target needs to have blood";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return target.hasProperty(Constants.CREATURE_TYPE) && target.getProperty(Constants.CREATURE_TYPE).hasBlood();
	}

	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "creating blood";
	}

	@Override
	public String getSimpleDescription() {
		return "create blood";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.BLOOD;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.KNIFE_SLICE;
	}

	@Override
	public AnimationId getAnimationId() {
		return AnimationId.RED_ORB;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
}