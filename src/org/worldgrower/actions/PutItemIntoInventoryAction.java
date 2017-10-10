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
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.goal.ContainerUtils;
import org.worldgrower.goal.InventoryPropertyUtils;
import org.worldgrower.goal.LockUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class PutItemIntoInventoryAction implements ManagedOperation {

	private static final int DISTANCE = 1;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int index = args[0];
		
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		WorldObjectContainer targetInventory = target.getProperty(Constants.INVENTORY);
		
		WorldObject takenWorldObject = performerInventory.remove(index);
		targetInventory.add(takenWorldObject);
		
		InventoryPropertyUtils.cleanupEquipmentSlots(performer);
		
		ContainerUtils.accessContainer(performer, target, world);
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return LockUtils.performerCanAccessContainer(performer, target);
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, DISTANCE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, DISTANCE, "items cannot be put into locked containers");
	}
	
	@Override
	public String getDescription() {
		return "put item from inventory into container";
	}

	@Override
	public boolean requiresArguments() {
		return true;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return ContainerUtils.canAccessContainer(performer, target, world);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "storing items in " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "store item";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.CHEST;
	}
	
	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.DOOR_OPEN;
	}
}