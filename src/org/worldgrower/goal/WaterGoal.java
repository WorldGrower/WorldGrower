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
import org.worldgrower.gui.ImageIds;

public class WaterGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> targets = BuySellUtils.findBuyTargets(performer, Constants.WATER, world);
		if (targets.size() > 0) {
			return new OperationInfo(performer, targets.get(0), new int[] { targets.get(0).getProperty(Constants.INVENTORY).getIndexFor(Constants.WATER), 5 }, Actions.BUY_ACTION);
		} else {
			WorldObject target = GoalUtils.findNearestTarget(performer, Actions.DRINK_ACTION, world);
			if (target != null) {
				return new OperationInfo(performer, target, new int[0], Actions.DRINK_ACTION);
			} else {
				if (performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.WOOD) < 6) {
					return new WoodGoal().calculateGoal(performer, world);
				} else {
					WorldObject targetLocation = BuildLocationUtils.findOpenLocationNearExistingProperty(performer, 2, 2, world);
					return new OperationInfo(performer, targetLocation, new int[0], Actions.BUILD_WELL_ACTION);
				}
			}
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
		if (performer.hasProperty(Constants.DEMANDS)) {
			if (goalMet) {
				performer.getProperty(Constants.DEMANDS).removeAllQuantity(Constants.WATER);
			} else {
				ImageIds noImage = null;
				performer.getProperty(Constants.DEMANDS).addQuantity(Constants.WATER, 1, noImage);
			}
		}
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return performer.getProperty(Constants.WATER) > 750;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return performer.getProperty(Constants.WATER) > 250;
	}

	@Override
	public String getDescription() {
		return "thirsty and looking for water";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.WATER);
	}
}
