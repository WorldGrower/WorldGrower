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
import org.worldgrower.actions.SmithPropertyUtils;
import org.worldgrower.generator.Item;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class EquipRepairHammerGoal implements Goal {

	public EquipRepairHammerGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (!SmithPropertyUtils.leftHandContainsRepairHammer(performer)) {
			if (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.REPAIR_QUALITY) > 0) {
				int indexOfRepairHammer = performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.REPAIR_QUALITY);
				return new OperationInfo(performer, performer, new int[] { indexOfRepairHammer }, Actions.EQUIP_INVENTORY_ITEM_ACTION);
			} else {
				return Goals.CREATE_REPAIR_HAMMER_GOAL.calculateGoal(performer, world);
			}
		}
		
		return null;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return SmithPropertyUtils.leftHandContainsRepairHammer(performer);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_EQUIP_REPAIR_HAMMER, Item.REPAIR_HAMMER);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
