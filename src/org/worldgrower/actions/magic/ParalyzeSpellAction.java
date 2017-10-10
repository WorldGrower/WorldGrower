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
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.AnimatedAction;
import org.worldgrower.actions.AttackUtils;
import org.worldgrower.actions.CraftUtils;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.ConditionUtils;
import org.worldgrower.condition.Conditions;
import org.worldgrower.goal.MagicSpellUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class ParalyzeSpellAction implements MagicSpell, AnimatedAction {

	private static final int ENERGY_USE = 600;
	private static final int DISTANCE = 4;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int turns = (int)(8 * SkillUtils.getSkillBonus(performer, getSkill()));
		if (ConditionUtils.performerHasCondition(performer, Condition.APHRODITE_BOON_CONDITION)) {
			turns += 5;
		}
		Conditions.add(target, Condition.PARALYZED_CONDITION, turns, world);

		SkillUtils.useEnergy(performer, getSkill(), ENERGY_USE, world.getWorldStateChangedListeners());
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (target.hasProperty(Constants.CONDITIONS) && target.hasIntelligence() && MagicSpellUtils.canCast(performer, this));
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return SkillUtils.hasEnoughEnergy(performer, getSkill(), ENERGY_USE);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return AttackUtils.distanceWithFreeLeftHand(performer, target, DISTANCE);
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
		return "casting paralyze on " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "paralyze";
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
		return Constants.ENCHANTMENT_SKILL;
	}

	@Override
	public int getRequiredSkillLevel() {
		return 2;
	}

	@Override
	public String getDescription() {
		return "paralyze target for several turns";
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.PARALYZED_INDICATOR;
	}
	
	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.WIND;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.PARALYZED_INDICATOR_ANIMATION;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
}
