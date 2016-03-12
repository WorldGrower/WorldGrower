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
package org.worldgrower;

import java.util.Arrays;

import org.worldgrower.actions.Actions;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;

class DefaultGoalChangedListener implements GoalChangedListener {

	@Override
	public void goalChanged(WorldObject performer, Goal oldGoal, Goal newGoal) {
		if (performer.hasProperty(Constants.FACADE)) {
			WorldObject facade = performer.getProperty(Constants.FACADE);
			if (facade != null) {
				MetaInformation metaInformation = facade.getProperty(Constants.META_INFORMATION);
				if (metaInformation == null) {
					metaInformation = new MetaInformation(performer);
					facade.setProperty(Constants.META_INFORMATION, metaInformation);
				}
				
				if (newGoal == Goals.STEAL_GOAL) {
					metaInformation.setFinalGoal(Goals.FOOD_GOAL);
					metaInformation.setCurrentTask(Arrays.asList(new OperationInfo(performer, performer, new int[0], Actions.EAT_FROM_INVENTORY_ACTION)), GoalChangedReason.EMPTY_META_INFORMATION);
				}
			}
		}
	}
}
