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
package org.worldgrower.goal;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;

public class AttackTargetGoal implements Goal {

	private final WorldObject target;
	
	public AttackTargetGoal(WorldObject target) {
		this.target = target;
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObject leftHandEquipment = performer.getProperty(Constants.LEFT_HAND_EQUIPMENT);
		boolean hasFreeHands = (leftHandEquipment == null);
		if (EquipmentPropertyUtils.isMeleeWeapon(leftHandEquipment)) {
			return new OperationInfo(performer, target, new int[0], Actions.MELEE_ATTACK_ACTION);
		} else if (EquipmentPropertyUtils.isRangedWeapon(leftHandEquipment)) {
			return new OperationInfo(performer, target, new int[0], Actions.RANGED_ATTACK_ACTION);
		} else if (hasFreeHands && MagicSpellUtils.canCast(performer, Actions.FIRE_BOLT_ATTACK_ACTION)) {
			return new OperationInfo(performer, target, new int[0], Actions.FIRE_BOLT_ATTACK_ACTION);
		} else if (hasFreeHands && MagicSpellUtils.canCast(performer, Actions.INFLICT_WOUNDS_ACTION)) {
			return new OperationInfo(performer, target, new int[0], Actions.INFLICT_WOUNDS_ACTION);
		} else {
			return new OperationInfo(performer, target, new int[0], Actions.MELEE_ATTACK_ACTION);
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return true;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "attacking " + target.getProperty(Constants.NAME);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
