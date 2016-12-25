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

public class FreedomOfMovementAction implements MagicSpell, AnimatedAction {

	private static final int ENERGY_USE = 200;
	private static final int DISTANCE = 1;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		if (target.getProperty(Constants.CONDITIONS).hasCondition(Condition.PARALYZED_CONDITION)) {
			Conditions.remove(target, Condition.PARALYZED_CONDITION, world);
		}
		if (target.getProperty(Constants.CONDITIONS).hasCondition(Condition.ENTANGLED_CONDITION)) {
			Conditions.remove(target, Condition.ENTANGLED_CONDITION, world);
		}
		
		int turns = (int)(20 * SkillUtils.getSkillBonus(performer, getSkill()));
		Conditions.add(target, Condition.FREEDOM_OF_MOVEMENT_CONDITION, turns, world);
				
		SkillUtils.useEnergy(performer, getSkill(), ENERGY_USE, world.getWorldStateChangedListeners());
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (target.hasProperty(Constants.CONDITIONS)
				&& (target.hasIntelligence()) 
				&& MagicSpellUtils.canCast(performer, this));
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return SkillUtils.hasEnoughEnergy(performer, getSkill(), ENERGY_USE);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, DISTANCE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.ENERGY, ENERGY_USE, Constants.DISTANCE, DISTANCE);
	}

	@Override
	public boolean requiresArguments() {
		return false;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "casting freedom of movement on " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "freedom of movement";
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
		return Constants.TRANSMUTATION_SKILL;
	}

	@Override
	public int getRequiredSkillLevel() {
		return 2;
	}

	@Override
	public String getDescription() {
		return "enables a person to move and attack normally, even when under effect of magic spells that limit movement like paralysis or entangle";
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.FREEDOM_OF_MOVEMENT_MAGIC_SPELL;
	}

	@Override
	public SoundIds getSoundId() {
		return SoundIds.ENCHANT2;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.FREEDOM_OF_MOVEMENT_MAGIC_SPELL_ANIMATION;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}	
}
