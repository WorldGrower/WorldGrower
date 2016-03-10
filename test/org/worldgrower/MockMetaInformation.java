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

public class MockMetaInformation {

	public static void setMetaInformation(WorldObject worldObject, Goal goal) {
		setMetaInformation(worldObject, goal, Actions.MELEE_ATTACK_ACTION);
	}
	
	public static void setMetaInformation(WorldObject worldObject, Goal goal, ManagedOperation action) {
		worldObject.setProperty(Constants.META_INFORMATION, new MetaInformation(worldObject));
		worldObject.getProperty(Constants.META_INFORMATION).setFinalGoal(goal);
		
		WorldObject target = TestUtils.createSkilledWorldObject(2, Constants.NAME, "targetName");
		OperationInfo operationInfo = new OperationInfo(TestUtils.createSkilledWorldObject(1), target, new int[0], action);
		worldObject.getProperty(Constants.META_INFORMATION).setCurrentTask(Arrays.asList(operationInfo), GoalChangedReason.EMPTY_META_INFORMATION);
	}
}
