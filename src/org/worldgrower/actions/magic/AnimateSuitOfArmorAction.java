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

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.InventoryAction;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.CreatureGenerator;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.goal.MagicSpellUtils;
import org.worldgrower.gui.ImageIds;

public class AnimateSuitOfArmorAction extends InventoryAction implements MagicSpell {

	private static final int ENERGY_USE = 200;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int inventoryIndex = args[0];
		WorldObject minionOrganization = GroupPropertyUtils.createMinionOrganization(performer, world);
		
		CreatureGenerator creatureGenerator = new CreatureGenerator(minionOrganization);
		Integer targetX = target.getProperty(Constants.X);
		Integer targetY = target.getProperty(Constants.Y);
		int animatedSuitOfArmorId = creatureGenerator.generateAnimatedSuitOfArmor(targetX, targetY, world, performer);
		WorldObject skeleton = world.findWorldObject(Constants.ID, animatedSuitOfArmorId);
		skeleton.getProperty(Constants.GROUP).addAll(performer.getProperty(Constants.GROUP));
		
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		performerInventory.removeQuantity(Constants.SOUL_GEM_FILLED, 1);
		performerInventory.remove(inventoryIndex);
		SkillUtils.useEnergy(performer, getSkill(), ENERGY_USE, world.getWorldStateChangedListeners());
	}
	
	@Override
	public boolean isValidInventoryItem(WorldObject inventoryItem, WorldObjectContainer inventory, WorldObject performer) {
		boolean isTorsoEquipment = inventoryItem.hasProperty(Constants.EQUIPMENT_SLOT) && inventoryItem.getProperty(Constants.EQUIPMENT_SLOT) == Constants.TORSO_EQUIPMENT;
		return isTorsoEquipment && MagicSpellUtils.canCast(performer, this);
	}
	
	@Override
	public int distanceToInventoryItem(WorldObject inventoryItem, WorldObjectContainer inventory, WorldObject performer) {
		int energyUseDistance = SkillUtils.distanceForEnergyUse(performer, getSkill(), ENERGY_USE);
		boolean hasFilledSoulGem = (inventory.getQuantityFor(Constants.SOUL_GEM_FILLED) > 0);
		return (hasFilledSoulGem ? 0 : 1) + energyUseDistance;
	}
	
	@Override
	public String getRequirementsDescription() {
		return "Requirements: filled soul gem : 1, torso equipment : 1, enough energy to cast spell";
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
