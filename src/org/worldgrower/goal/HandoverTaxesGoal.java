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

public class HandoverTaxesGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObject target = GroupPropertyUtils.getLeaderOfVillagers(world);
		if (target != null && !target.equals(performer)) {
			return new OperationInfo(performer, target, new int[0], Actions.HANDOVER_TAXES_ACTION);
		} else {
			return null;
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		int collectedTaxes = performer.getProperty(Constants.ORGANIZATION_GOLD);
		WorldObject leader = GroupPropertyUtils.getLeaderOfVillagers(world);
		if ((collectedTaxes > 100) && (leader != null)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "handing over taxes";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
