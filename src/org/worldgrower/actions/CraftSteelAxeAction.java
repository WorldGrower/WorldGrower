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
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class CraftSteelAxeAction implements CraftEquipmentAction, AnimatedAction {
	private static final int DISTANCE = 1;
	private static final int WOOD_REQUIRED = 5;
	private static final int STEEL_REQUIRED = 3;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		
		double skillBonus = SkillUtils.useSkill(performer, Constants.SMITHING_SKILL, world.getWorldStateChangedListeners());
		int quantity = SmithPropertyUtils.calculateSmithingQuantity(performer, target);
		inventory.addQuantity(Item.STEEL_AXE.generate(skillBonus), quantity);

		inventory.removeQuantity(Constants.WOOD, WOOD_REQUIRED);
		inventory.removeQuantity(Constants.STEEL, STEEL_REQUIRED);
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return CraftUtils.hasEnoughResources(performer, Constants.WOOD, WOOD_REQUIRED, Constants.STEEL, STEEL_REQUIRED);
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, DISTANCE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.WOOD, WOOD_REQUIRED, Constants.STEEL, STEEL_REQUIRED);
	}
	
	@Override
	public String getDescription() {
		return "a steel axe is used as a weapon or as a wood cutting tool";
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return target.hasProperty(Constants.SMITH_QUALITY);
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "crafting steel axe";
	}

	@Override
	public String getSimpleDescription() {
		return "craft steel axe";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.STEEL_AXE;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.SMITH;
	}
	
	public static boolean hasEnoughWood(WorldObject performer) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WOOD) >= WOOD_REQUIRED;
	}
	
	public static boolean hasEnoughSteel(WorldObject performer) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.STEEL) >= STEEL_REQUIRED;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.STEEL_AXE_ANIMATION;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
	
	@Override
	public EquipmentType getEquipmentType() {
		return EquipmentType.STEEL;
	}
}