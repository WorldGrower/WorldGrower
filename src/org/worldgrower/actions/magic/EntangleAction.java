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
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.AnimatedAction;
import org.worldgrower.actions.BuildAction;
import org.worldgrower.actions.CraftUtils;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.goal.MagicSpellUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class EntangleAction implements BuildAction, MagicSpell, AnimatedAction {

	private static final int ENERGY_USE = 400;
	private static final int DISTANCE = 1;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		List<WorldObject> targets = getAffectedTargets(target, world);
		int turns = (int)(5 * SkillUtils.getSkillBonus(performer, getSkill()));
		for(WorldObject spellTarget : targets) {
			Conditions.add(spellTarget, Condition.ENTANGLED_CONDITION, turns, world);
		}
		
		SkillUtils.useEnergy(performer, getSkill(), ENERGY_USE, world.getWorldStateChangedListeners());
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return !target.hasProperty(Constants.ID) && MagicSpellUtils.canCast(performer, this);
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		return SkillUtils.hasEnoughEnergy(performer, getSkill(), ENERGY_USE);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, DISTANCE);
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
		return "casting entangle";
	}

	@Override
	public String getSimpleDescription() {
		return "entangle";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public int getWidth() {
		return 4;
	}

	@Override
	public int getHeight() {
		return 4;
	}

	@Override
	public int getResearchCost() {
		return 50;
	}

	@Override
	public SkillProperty getSkill() {
		return Constants.EVOCATION_SKILL;
	}

	@Override
	public int getRequiredSkillLevel() {
		return 2;
	}

	@Override
	public String getDescription() {
		return "entangles everyone inside an area";
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.ENTANGLED_INDICATOR;
	}

	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.RUSTLE01;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.ENTANGLED_INDICATOR_ANIMATION;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		int x = (Integer)target.getProperty(Constants.X);
		int y = (Integer)target.getProperty(Constants.Y);
		
		return world.findWorldObjectsByProperty(Constants.STRENGTH, w -> isInAreaOfEffect(x, y, w));
	}
}
