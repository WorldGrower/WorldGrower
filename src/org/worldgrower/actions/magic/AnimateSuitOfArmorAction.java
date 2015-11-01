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
package org.worldgrower.actions.magic;

import java.io.ObjectStreamException;

import org.worldgrower.ArgumentRange;
import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.CraftUtils;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.generator.CreatureGenerator;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.gui.ImageIds;

public class AnimateSuitOfArmorAction implements MagicSpell {

	private static final int ENERGY_USE = 200;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObject minionOrganization = GroupPropertyUtils.createMinionOrganization(performer, world);
		
		CreatureGenerator creatureGenerator = new CreatureGenerator(minionOrganization);
		Integer targetX = target.getProperty(Constants.X);
		Integer targetY = target.getProperty(Constants.Y);
		int animatedSuitOfArmorId = creatureGenerator.generateAnimatedSuitOfArmor(targetX, targetY, world, performer);
		WorldObject skeleton = world.findWorldObject(Constants.ID, animatedSuitOfArmorId);
		skeleton.getProperty(Constants.GROUP).addAll(performer.getProperty(Constants.GROUP));
		
		performer.getProperty(Constants.INVENTORY).removeQuantity(Constants.SOUL_GEM_FILLED, 1);
		performer.getProperty(Constants.INVENTORY).remove(performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.EQUIPMENT_SLOT, Constants.TORSO_EQUIPMENT));
		SkillUtils.useEnergy(performer, getSkill(), ENERGY_USE, world.getWorldStateChangedListeners());
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (performer.equals(target) 
				&& (performer.hasProperty(Constants.INVENTORY)) 
				&& (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.SOUL_GEM_FILLED) > 0)
				&& (performer.getProperty(Constants.INVENTORY).getWorldObjects(Constants.EQUIPMENT_SLOT, Constants.TORSO_EQUIPMENT).size() > 0));
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return SkillUtils.distanceForEnergyUse(performer, getSkill(), ENERGY_USE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.ENERGY, ENERGY_USE, "has torso equipment and a soulgem in inventory");
	}
	
	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "animating a suit of armor";
	}

	@Override
	public String getSimpleDescription() {
		return "animate suit of armor";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public int getResearchCost() {
		return 40;
	}

	@Override
	public SkillProperty getSkill() {
		return Constants.NECROMANCY_SKILL;
	}

	@Override
	public int getRequiredSkillLevel() {
		return 3;
	}

	@Override
	public String getDescription() {
		return "animates a suit of armor which you control";
	}

	@Override
	public ImageIds getImageIds() {
		return ImageIds.ANIMATED_SUIT_OF_ARMOR;
	}
}
