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
import org.worldgrower.actions.AttackUtils;
import org.worldgrower.actions.CraftUtils;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.goal.LocationPropertyUtils;
import org.worldgrower.goal.MagicSpellUtils;
import org.worldgrower.gui.ImageIds;

public class ReduceAction implements MagicSpell {

	private static final int ENERGY_USE = 500;
	private static final int DISTANCE = 4;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		if (target.getProperty(Constants.CONDITIONS).hasCondition(Condition.ENLARGED_CONDITION)) {
			Conditions.remove(target, Condition.ENLARGED_CONDITION, world);
		} else {
			target.setProperty(Constants.ORIGINAL_HEIGHT, target.getProperty(Constants.HEIGHT));
			target.setProperty(Constants.ORIGINAL_WIDTH, target.getProperty(Constants.WIDTH));
			
			int newWidth = halveDimension(target.getProperty(Constants.WIDTH));
			int newHeight = halveDimension(target.getProperty(Constants.HEIGHT));
			
			LocationPropertyUtils.updateLocation(
					target,
					target.getProperty(Constants.X), 
					target.getProperty(Constants.Y), 
					newWidth, 
					newHeight, 
					world);
			
			int turns = (int)(8 * SkillUtils.getSkillBonus(performer, getSkill()));
			Conditions.add(target, Condition.REDUCED_CONDITION, turns, world);
		}		
		SkillUtils.useEnergy(performer, getSkill(), ENERGY_USE, world.getWorldStateChangedListeners());
	}

	private int halveDimension(int dimension) {
		dimension = dimension / 2;
		if (dimension < 1) {
			dimension = 1;
		}
		return dimension;
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (target.hasProperty(Constants.CONDITIONS) && !target.getProperty(Constants.CONDITIONS).hasCondition(Condition.REDUCED_CONDITION) && MagicSpellUtils.canCast(performer, this));
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return AttackUtils.distanceWithFreeLeftHand(performer, target, DISTANCE)
				+ SkillUtils.distanceForEnergyUse(performer, getSkill(), ENERGY_USE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.ENERGY, ENERGY_USE, Constants.DISTANCE, DISTANCE, "free left hand");
	}
	
	@Override
	public boolean requiresArguments() {
		return false;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "reducing " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "reduce";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public int getResearchCost() {
		return 30;
	}

	@Override
	public SkillProperty getSkill() {
		return Constants.TRANSMUTATION_SKILL;
	}

	@Override
	public int getRequiredSkillLevel() {
		return 1;
	}

	@Override
	public String getDescription() {
		return "makes target smaller, halving its size and making it weaker if it can fight";
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.REDUCE_MAGIC_SPELL;
	}
}
