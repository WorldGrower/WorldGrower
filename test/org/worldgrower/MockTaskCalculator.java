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
import java.util.List;

import org.worldgrower.OperationInfo;
import org.worldgrower.TaskCalculator;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;

public class MockTaskCalculator implements TaskCalculator {

	@Override
	public List<OperationInfo> calculateTask(WorldObject performer, World world, OperationInfo goal) {
		return Arrays.asList(goal, new OperationInfo(performer, performer, new int[]{1, 1}, Actions.MOVE_ACTION));
	}

}
