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
import org.worldgrower.attribute.SkillUtils;

public class RangedAttackAction implements DeadlyAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		AttackUtils.attack(this, performer, target, args, world, SkillUtils.useSkill(performer, Constants.ARCHERY_SKILL));
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return ((target.hasProperty(Constants.ARMOR)) && (target.getProperty(Constants.HIT_POINTS) > 0));
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		WorldObject attackWeapon = performer.getProperty(Constants.LEFT_HAND_EQUIPMENT);
		if (attackWeapon != null && attackWeapon.hasProperty(Constants.RANGE)) {
			int range = attackWeapon.getProperty(Constants.RANGE);
			int distance = Reach.distance(performer, target);
			if (distance <= range) {
				return 0;
			} else {
				return distance - range;
			}
		} else {
			return 1;
		}
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
		return "ranged attack";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public String getDeathDescription(WorldObject performer, WorldObject target) {
		return "shot by an arrow";
	}
}
