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
package org.worldgrower.actions;

import java.io.ObjectStreamException;

import org.worldgrower.ArgumentRange;
import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.SkillProperty;

public class MeleeAttackAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		AttackUtils.attack(this, performer, target, args, world, useSkill(performer));
	}
	
	private SkillProperty determineSkill(WorldObject performer) {
		WorldObject leftHandEquipment = performer.getProperty(Constants.LEFT_HAND_EQUIPMENT);
		WorldObject rightHandEquipment = performer.getProperty(Constants.RIGHT_HAND_EQUIPMENT);
		
		if ((leftHandEquipment == null) && (rightHandEquipment == null)) {
			return Constants.HAND_TO_HAND_SKILL;
		} else if ((leftHandEquipment != null) && (rightHandEquipment != null) && (leftHandEquipment == rightHandEquipment)) {
			return Constants.TWO_HANDED_SKILL;
		} else {
			return Constants.ONE_HANDED_SKILL;
		}
	}
	
	private double useSkill(WorldObject performer) {
		SkillProperty skill = determineSkill(performer);
		double result = 1.0f + (performer.getProperty(skill).getLevel() / 100.0f);
		performer.getProperty(skill).use();
		return result;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return ((target.hasProperty(Constants.ARMOR)) && (target.getProperty(Constants.HIT_POINTS) > 0));
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, args, target, 1);
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
		return "melee attack";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
}
