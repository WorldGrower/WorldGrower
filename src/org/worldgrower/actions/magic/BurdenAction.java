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
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.CraftUtils;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.goal.MagicSpellUtils;
import org.worldgrower.gui.ImageIds;

public class BurdenAction implements MagicSpell {

	private static final int ENERGY_USE = 200;
	private static final int DISTANCE = 1;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		if (target.getProperty(Constants.CONDITIONS).hasCondition(Condition.FEATHERED_CONDITION)) {
			Conditions.remove(target, Condition.FEATHERED_CONDITION, world);
		} else {
			int turns = (int)(20 * SkillUtils.getSkillBonus(performer, getSkill()));
			Conditions.add(target, Condition.BURDENED_CONDITION, turns, world);
		}
		
		SkillUtils.useEnergy(performer, getSkill(), ENERGY_USE, world.getWorldStateChangedListeners());
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return ((target.hasProperty(Constants.INVENTORY)) 
				&& target.hasProperty(Constants.CONDITIONS)
				&& !target.getProperty(Constants.CONDITIONS).hasCondition(Condition.BURDENED_CONDITION)
				&& (target.hasIntelligence()) 
				&& MagicSpellUtils.canCast(performer, this));
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		int distanceBetweenPerformerAndTarget = Reach.evaluateTarget(performer, args, target, DISTANCE);
		return distanceBetweenPerformerAndTarget 
				+ SkillUtils.distanceForEnergyUse(performer, getSkill(), ENERGY_USE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.ENERGY, ENERGY_USE, Constants.DISTANCE, DISTANCE);
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "casting burden on " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "burden";
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
		return "makes everything a person is carrying heavier";
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.BURDEN_MAGIC_SPELL;
	}
}
