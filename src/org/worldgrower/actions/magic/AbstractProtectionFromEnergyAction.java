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
import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.AnimatedAction;
import org.worldgrower.actions.CraftUtils;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.goal.MagicSpellUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public abstract class AbstractProtectionFromEnergyAction implements MagicSpell, AnimatedAction {

	private static final int ENERGY_USE = 400;
	private static final int DISTANCE = 1;
	
	@Override
	public final void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int turns = (int)(20 * SkillUtils.getSkillBonus(performer, getSkill()));
		Conditions.add(target, getCondition(), turns, world);
		
		SkillUtils.useEnergy(performer, getSkill(), ENERGY_USE, world.getWorldStateChangedListeners());
	}
	
	@Override
	public final boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (target.hasProperty(Constants.CONDITIONS)
				&& (target.hasIntelligence()) 
				&& MagicSpellUtils.canCast(performer, this));
	}
	
	@Override
	public final boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return SkillUtils.hasEnoughEnergy(performer, getSkill(), ENERGY_USE);
	}

	@Override
	public final int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, DISTANCE);
	}
	
	@Override
	public final String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.ENERGY, ENERGY_USE, Constants.DISTANCE, DISTANCE);
	}

	@Override
	public final boolean requiresArguments() {
		return false;
	}
	
	@Override
	public final String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "casting protection from " + getEnergyDescription() + " on " + target.getProperty(Constants.NAME);
	}

	@Override
	public final String getSimpleDescription() {
		return "protection from " + getEnergyDescription();
	}
	
	public final Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public final int getResearchCost() {
		return 40;
	}

	@Override
	public final SkillProperty getSkill() {
		return Constants.RESTORATION_SKILL;
	}

	@Override
	public final int getRequiredSkillLevel() {
		return 1;
	}

	@Override
	public final String getDescription() {
		return "makes a person resistant to " + getEnergyDescription();
	}
	
	@Override
	public abstract ImageIds getImageIds(WorldObject performer);

	@Override
	public final SoundIds getSoundId() {
		return SoundIds.MAGIC_SHIELD;
	}

	@Override
	public abstract ImageIds getAnimationImageId();

	@Override
	public final List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
	
	public abstract Condition getCondition();
	public abstract String getEnergyDescription();
}
