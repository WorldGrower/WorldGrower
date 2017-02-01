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
import java.util.StringJoiner;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.AnimatedAction;
import org.worldgrower.actions.AttackUtils;
import org.worldgrower.actions.CraftUtils;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.curse.Curse;
import org.worldgrower.goal.MagicSpellUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class BestowCurseAction implements MagicSpell, AnimatedAction {

	private static final int ENERGY_USE = 300;
	private static final int DISTANCE = 1;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int curseIndex = args[0];
		Curse curse = Curse.BESTOWABLE_CURSES.get(curseIndex);
		target.setProperty(Constants.CURSE, curse);
		curse.curseStarts(target, world.getWorldStateChangedListeners());
		SkillUtils.useEnergy(performer, getSkill(), ENERGY_USE, world.getWorldStateChangedListeners());
	
		world.getWorldStateChangedListeners().creatureCursed(performer, target, curse);
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return ((target.hasProperty(Constants.ARMOR)) && (target.getProperty(Constants.HIT_POINTS) > 0) && target.hasIntelligence() && MagicSpellUtils.canCast(performer, this));
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return SkillUtils.hasEnoughEnergy(performer, getSkill(), ENERGY_USE) && (target.getProperty(Constants.CURSE) == null);
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
		return true;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "bestowing a curse on " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "bestow curse";
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
		return 2;
	}
	
	public boolean hasRequiredEnergy(WorldObject performer) {
		return performer.getProperty(Constants.ENERGY) >= ENERGY_USE;
	}

	@Override
	public String getDescription() {
		String bestowableCursesDescription = createBestowableCursesDescription();
		return "bestow a curse on a person like " + bestowableCursesDescription;
	}

	private String createBestowableCursesDescription() {
		StringJoiner stringJoiner = new StringJoiner(", ");
		for(Curse bestowableCurse : Curse.BESTOWABLE_CURSES) {
			stringJoiner.add(bestowableCurse.getName());
		}
		return stringJoiner.toString();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.BESTOW_CURSE;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.CURSE4;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.BESTOW_CURSE_ANIMATION;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
}
