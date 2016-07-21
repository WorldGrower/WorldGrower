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

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.UnCheckedProperty;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;

public class PoisonWeaponGoal implements Goal {

	private static final int QUANTITY_TO_BUY = 5;
	
	public PoisonWeaponGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		if (inventory.getQuantityFor(Constants.POISON_DAMAGE) > 0) {
			int indexOfWeapon = -1;
			if (isEquipmentUnPoisoned(performer, Constants.LEFT_HAND_EQUIPMENT)) {
				indexOfWeapon = inventory.getIndexFor(w -> w == performer.getProperty(Constants.LEFT_HAND_EQUIPMENT));
			}
			if (isEquipmentUnPoisoned(performer, Constants.RIGHT_HAND_EQUIPMENT)) {
				indexOfWeapon = inventory.getIndexFor(w -> w == performer.getProperty(Constants.RIGHT_HAND_EQUIPMENT));
			}
			if (indexOfWeapon != -1) {
				return new OperationInfo(performer, performer, new int[] { indexOfWeapon }, Actions.POISON_WEAPON_ACTION);
			} else {
				return null;
			}
		} else {
			List<WorldObject> targets = BuySellUtils.findBuyTargets(performer, Constants.POISON_DAMAGE, QUANTITY_TO_BUY, world);
			if (targets.size() > 0) {
				return BuySellUtils.create(performer, targets.get(0), Item.POISON, QUANTITY_TO_BUY, world);
			} else {
				return Goals.CREATE_POISON_GOAL.calculateGoal(performer, world);
			}
		}
	}

	private boolean isEquipmentUnPoisoned(WorldObject performer, UnCheckedProperty<WorldObject> equipment) {
		return performer.getProperty(equipment) != null && !performer.getProperty(equipment).hasProperty(Constants.POISON_DAMAGE);
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
		defaultGoalMetOrNot(performer, world, goalMet, Constants.POISON_DAMAGE);
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return !isEquipmentUnPoisoned(performer, Constants.LEFT_HAND_EQUIPMENT)
				&& !isEquipmentUnPoisoned(performer, Constants.RIGHT_HAND_EQUIPMENT);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "poisoning weapons";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}