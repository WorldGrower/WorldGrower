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
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class ConstructBedAction implements CraftEquipmentAction {
	private static final int DISTANCE = 1;
	private static final int WOOD_REQUIRED = 3;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		
		double skillBonus = SkillUtils.useSkill(performer, Constants.CARPENTRY_SKILL, world.getWorldStateChangedListeners());
		int quantity =target.getProperty(Constants.WORKBENCH_QUALITY);
		inventory.addQuantity(Item.BED.generate(skillBonus), quantity);

		inventory.removeQuantity(Constants.WOOD, WOOD_REQUIRED);
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return CraftUtils.hasEnoughResources(performer, Constants.WOOD, WOOD_REQUIRED);
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, DISTANCE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.WOOD, WOOD_REQUIRED);
	}
	
	@Override
	public String getDescription() {
		return "sleeping in a residence with a bed increases the energy recovered";
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
		return "constructing a bed";
	}

	@Override
	public FormattableText getFormattableSimpleDescription() {
		return new FormattableText(TextId.CRAFT_ITEM, Item.BED);
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	public static boolean hasEnoughWood(WorldObject performer) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WOOD) >= WOOD_REQUIRED;
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.BED;
	}

	@Override
	public EquipmentType getEquipmentType() {
		return EquipmentType.FURNITURE;
	}

	@Override
	public Item getItem() {
		return Item.BED;
	}

	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.SAW;
	}
}