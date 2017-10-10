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
import org.worldgrower.actions.CraftUtils;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.LichUtils;
import org.worldgrower.creaturetype.CreatureTypeUtils;
import org.worldgrower.goal.MagicSpellUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class LichTransformationAction implements MagicSpell {
	private static final int SOUL_GEM_COUNT = 3;
	private static final int ENERGY_USE = 600;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		LichUtils.lichifyPerson(performer, world.getWorldStateChangedListeners());
		
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		performerInventory.removeQuantity(Constants.FILLED_SOUL_GEM, SOUL_GEM_COUNT);
		
		SkillUtils.useEnergy(performer, getSkill(), ENERGY_USE, world.getWorldStateChangedListeners());
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return CraftUtils.isValidTarget(performer, target, world) && (!CreatureTypeUtils.isUndead(performer)) && MagicSpellUtils.canCast(performer, this);
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		int filledSoulGem = performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FILLED_SOUL_GEM);
		return SkillUtils.hasEnoughEnergy(performer, getSkill(), ENERGY_USE)
				&& filledSoulGem >= SOUL_GEM_COUNT;
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return 0;
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.ENERGY, ENERGY_USE, Constants.FILLED_SOUL_GEM, SOUL_GEM_COUNT);
	}
	
	@Override
	public boolean requiresArguments() {
		return false;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "casting lich transformation";
	}

	@Override
	public String getSimpleDescription() {
		return "lich transformation";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public int getResearchCost() {
		return 100;
	}

	@Override
	public SkillProperty getSkill() {
		return Constants.NECROMANCY_SKILL;
	}

	@Override
	public int getRequiredSkillLevel() {
		return 5;
	}

	@Override
	public String getDescription() {
		return "transforms caster into a lich";
	}

	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.LICH;
	}
	
	public boolean hasRequiredEnergy(WorldObject performer) {
		return performer.getProperty(Constants.ENERGY) >= ENERGY_USE;
	}
	
	public boolean hasRequiredSoulGems(WorldObject performer) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.FILLED_SOUL_GEM) >= SOUL_GEM_COUNT;
	}
	
	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.ZAP2G;
	}
}
