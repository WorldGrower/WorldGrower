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
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class MineResourcesGoal implements Goal {

	private static final int RESOURCE_CUTOFF = 7;

	public MineResourcesGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		
		if (inventory.getQuantityFor(Constants.STONE) < RESOURCE_CUTOFF) {
			OperationInfo mineOperationInfo = Goals.MINE_STONE_GOAL.calculateGoal(performer, world);
			if (mineOperationInfo != null) {
				return mineOperationInfo;
			}
		}
		if (inventory.getQuantityFor(Constants.ORE) < RESOURCE_CUTOFF) {
			OperationInfo mineOperationInfo = Goals.MINE_ORE_GOAL.calculateGoal(performer, world);
			if (mineOperationInfo != null) {
				return mineOperationInfo;
			}
		}
		if (inventory.getQuantityFor(Constants.GOLD) < RESOURCE_CUTOFF) {
			OperationInfo mineOperationInfo = Goals.MINE_GOLD_GOAL.calculateGoal(performer, world);
			if (mineOperationInfo != null) {
				return mineOperationInfo;
			}
		}
		return null;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		return (inventory.getQuantityFor(Constants.STONE) >= RESOURCE_CUTOFF)
				&& (inventory.getQuantityFor(Constants.ORE) >= RESOURCE_CUTOFF) 
				&& (inventory.getQuantityFor(Constants.GOLD) >= RESOURCE_CUTOFF);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_MINE_RESOURCES, Item.STONE, Item.ORE, Item.GOLD);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		return inventory.getQuantityFor(Constants.STONE) 
				+ inventory.getQuantityFor(Constants.ORE) 
				+ inventory.getQuantityFor(Constants.GOLD);
	}
}
