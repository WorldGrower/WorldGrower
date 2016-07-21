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
import org.worldgrower.generator.Item;

public class PaperGoal implements Goal {

	private static final int QUANTITY_TO_BUY = 5;
	
	public PaperGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> targets = BuySellUtils.findBuyTargets(performer, Constants.PAPER, QUANTITY_TO_BUY, world);
		if (targets.size() > 0) {
			return BuySellUtils.create(performer, targets.get(0), Item.PAPER, QUANTITY_TO_BUY, world);
		} else {
			return Goals.CREATE_PAPER_GOAL.calculateGoal(performer, world);
		}
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
		defaultGoalMetOrNot(performer, world, goalMet, Constants.PAPER);
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
		return "looking for paper";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
