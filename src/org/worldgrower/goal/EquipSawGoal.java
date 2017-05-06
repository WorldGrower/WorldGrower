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
import org.worldgrower.generator.Item;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class EquipSawGoal implements Goal {

	public EquipSawGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (!leftHandContainsSaw(performer)) {
			if (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.SAW_QUALITY) > 0) {
				int indexOfSaw = performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.SAW_QUALITY);
				return new OperationInfo(performer, performer, new int[] { indexOfSaw }, Actions.EQUIP_INVENTORY_ITEM_ACTION);
			} else {
				return Goals.SAW_GOAL.calculateGoal(performer, world);
			}
		}
		
		return null;
	}
	
	public static boolean leftHandContainsSaw(WorldObject performer) {
		WorldObject leftHand = performer.getProperty(Constants.LEFT_HAND_EQUIPMENT);
		return (leftHand != null && leftHand.hasProperty(Constants.SAW_QUALITY));
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return leftHandContainsSaw(performer);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_EQUIP_SAW, Item.SAW);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
