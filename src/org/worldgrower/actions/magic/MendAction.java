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
import org.worldgrower.generator.Item;
import org.worldgrower.goal.MagicSpellUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class MendAction implements MagicSpell, AnimatedAction {

	private static final int BASE_AMOUNT = 5 * Item.COMBAT_MULTIPLIER;
	private static final int ENERGY_USE = 100;
	private static final int DISTANCE = 4;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int hitPointsRestored = (int)(BASE_AMOUNT * SkillUtils.getSkillBonus(performer, getSkill()));
		target.increment(Constants.HIT_POINTS, hitPointsRestored);
		if (target.getProperty(Constants.HIT_POINTS) > target.getProperty(Constants.HIT_POINTS_MAX)) {
			target.setProperty(Constants.HIT_POINTS, target.getProperty(Constants.HIT_POINTS_MAX));
		}
		
		SkillUtils.useEnergy(performer, getSkill(), ENERGY_USE, world.getWorldStateChangedListeners());
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return ((target.hasProperty(Constants.ARMOR)) && (target.getProperty(Constants.HIT_POINTS) > 0) && !target.hasIntelligence() && MagicSpellUtils.canCast(performer, this));
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
		return "mending " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "mend";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public int getResearchCost() {
		return 15;
	}

	@Override
	public SkillProperty getSkill() {
		return Constants.TRANSMUTATION_SKILL;
	}

	@Override
	public int getRequiredSkillLevel() {
		return 0;
	}
	
	public boolean hasRequiredEnergy(WorldObject performer) {
		return performer.getProperty(Constants.ENERGY) >= ENERGY_USE;
	}

	@Override
	public String getDescription() {
		return "mends and repairs a non-living item for " + BASE_AMOUNT + " hit points";
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.MEND_MAGIC_SPELL;
	}
	
	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.HEALING;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.MEND_MAGIC_SPELL_ANIMATION;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
}
