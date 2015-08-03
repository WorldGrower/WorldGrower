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

public class GatherRemainsGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		
		List<WorldObject> targets = getRemains(world);

		if (targets.size() > 0) {
			return new OperationInfo(performer, targets.get(0), new int[0], Actions.GATHER_REMAINS_ACTION);
		} else {
			return null;
		}
	}

	private List<WorldObject> getRemains(World world) {
		return world.findWorldObjects(w -> w.hasProperty(Constants.DECEASED_WORLD_OBJECT) && w.getProperty(Constants.DECEASED_WORLD_OBJECT));
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		boolean performerHasRemains = performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.DECEASED_WORLD_OBJECT) != -1;
		return ((getRemains(world).size() == 0) || (performerHasRemains));
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "gathering remains";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.DECEASED_WORLD_OBJECT) != -1 ? 1 : 0;
	}
}