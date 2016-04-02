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

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Condition;

public class FillSoulGemGoal implements Goal {

	private static final int SOUL_GEM_COUNT = 3;
	
	public FillSoulGemGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		if (performerInventory.getQuantityFor(Constants.SOUL_GEM) < SOUL_GEM_COUNT) {
			return Goals.SOUL_GEM_GOAL.calculateGoal(performer, world);
		} else if (performer.getProperty(Constants.KNOWN_SPELLS).contains(Actions.SOUL_TRAP_ACTION) 
				&& Actions.SOUL_TRAP_ACTION.hasRequiredEnergy(performer)) {
			List<WorldObject> poisonedVillagers = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> w.getProperty(Constants.CONDITIONS).hasCondition(Condition.POISONED_CONDITION));
			if (poisonedVillagers.size() > 0) {
				return new OperationInfo(performer, poisonedVillagers.get(0), Args.EMPTY, Actions.SOUL_TRAP_ACTION);
			}
		}
		return null;
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}
	
	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.SOUL_GEM_FILLED) >= SOUL_GEM_COUNT;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "filling soulgems";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.SOUL_GEM_FILLED);
	}
}