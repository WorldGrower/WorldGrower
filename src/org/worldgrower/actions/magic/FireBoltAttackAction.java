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
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.AttackUtils;
import org.worldgrower.actions.CraftUtils;
import org.worldgrower.actions.DeadlyAction;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.goal.MagicSpellUtils;
import org.worldgrower.gui.ImageIds;

public class FireBoltAttackAction implements MagicSpell, DeadlyAction {

	private static final int DISTANCE = 4;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		AttackUtils.magicAttack(5, this, performer, target, args, world, SkillUtils.useSkill(performer, getSkill(), world.getWorldStateChangedListeners()));
	
		if (target.hasProperty(Constants.FLAMMABLE) && target.getProperty(Constants.FLAMMABLE)) {
			Conditions.add(target, Condition.BURNING_CONDITION, 100, world);
		}
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return ((target.hasProperty(Constants.ARMOR)) && (target.getProperty(Constants.HIT_POINTS) > 0) && MagicSpellUtils.canCast(performer, this));
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return AttackUtils.distanceWithFreeLeftHand(performer, target, DISTANCE);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, DISTANCE);
	}
	
	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "attacking " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "fire bolt";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public int getResearchCost() {
		return 10;
	}

	@Override
	public SkillProperty getSkill() {
		return Constants.EVOCATION_SKILL;
	}

	@Override
	public int getRequiredSkillLevel() {
		return 0;
	}

	@Override
	public String getDeathDescription(WorldObject performer, WorldObject target) {
		return "burned to death";
	}

	@Override
	public String getDescription() {
		return "shoots a firebolt at the target, setting it on fire if it is flammable";
	}
	
	@Override
	public ImageIds getImageIds() {
		return ImageIds.FIRE_BOLT;
	}
}
