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

import org.worldgrower.ArgumentRange;
import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.gui.ImageIds;

public class ConstructFishingPoleAction implements CraftAction {

	private static final int WOOD_REQUIRED = 2;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		
		double skillBonus = SkillUtils.useSkill(performer, Constants.CARPENTRY_SKILL, world.getWorldStateChangedListeners());
		inventory.addQuantity(Item.FISHING_POLE.generate(skillBonus));

		inventory.removeQuantity(Constants.WOOD, WOOD_REQUIRED);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return CraftUtils.distance(performer, Constants.WOOD, WOOD_REQUIRED);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.WOOD, WOOD_REQUIRED);
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return CraftUtils.isValidTarget(performer, target, world);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "constructing fishing pole";
	}

	@Override
	public String getSimpleDescription() {
		return "construct fishing pole";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	public static boolean hasEnoughWood(WorldObject performer) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WOOD) >= WOOD_REQUIRED;
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.FISHING_POLE;
	}
}