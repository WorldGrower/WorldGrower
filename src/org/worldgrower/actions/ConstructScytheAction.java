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
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class ConstructScytheAction implements CraftEquipmentAction, AnimatedAction {

	private static final int DISTANCE = 1;
	
	private static final int WOOD_REQUIRED = 2;
	private static final int ORE_REQUIRED = 2;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		getItem().addToInventory(performer, target, Constants.CARPENTRY_SKILL, Constants.WORKBENCH_QUALITY, world);

		inventory.removeQuantity(Constants.WOOD, WOOD_REQUIRED);
		inventory.removeQuantity(Constants.ORE, ORE_REQUIRED);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, DISTANCE);
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return CraftUtils.hasEnoughResources(performer, WOOD_REQUIRED, ORE_REQUIRED);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.WOOD, WOOD_REQUIRED, Constants.ORE, ORE_REQUIRED);
	}
	
	@Override
	public String getDescription() {
		return "Harvesting or eating food with a scythe increases the food recovered";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return target.hasProperty(Constants.WORKBENCH_QUALITY);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "constructing scythe";
	}

	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	public static boolean hasEnoughWood(WorldObject performer) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WOOD) >= WOOD_REQUIRED;
	}
	
	public static boolean hasEnoughOre(WorldObject performer) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.ORE) >= ORE_REQUIRED;
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.SCYTHE;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.SCYTHE_ANIMATION;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
	
	@Override
	public Item getItem() {
		return Item.SCYTHE;
	}

	@Override
	public EquipmentType getEquipmentType() {
		return EquipmentType.TOOL;
	}
	
	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.SAW;
	}
}