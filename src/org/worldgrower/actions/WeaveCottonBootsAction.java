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

public class WeaveCottonBootsAction implements CraftEquipmentAction, AnimatedAction {
	private static final int DISTANCE = 1;
	private static final int COTTON_REQUIRED = 2;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		getItem().addToInventory(performer, target, Constants.WEAVING_SKILL, Constants.WEAVERY_QUALITY, world);
		inventory.removeQuantity(Constants.COTTON, COTTON_REQUIRED);
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return CraftUtils.hasEnoughResources(performer, Constants.COTTON, COTTON_REQUIRED);
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, DISTANCE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.COTTON, COTTON_REQUIRED, Constants.DISTANCE, DISTANCE);
	}
	
	@Override
	public String getDescription() {
		return "cotton boots are used as armor to reduce the damage taken from attacks";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return target.hasProperty(Constants.WEAVERY_QUALITY);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "weaving cotton boots";
	}

	@Override
	public String getSimpleDescription() {
		return "weave cotton boots";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.COTTON_BOOTS;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.CLOTH;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.COTTON_BOOTS_ANIMATION;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
	
	@Override
	public EquipmentType getEquipmentType() {
		return EquipmentType.COTTON;
	}
	
	@Override
	public Item getItem() {
		return Item.COTTON_BOOTS;
	}
}