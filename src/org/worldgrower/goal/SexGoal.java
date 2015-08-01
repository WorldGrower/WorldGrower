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
import org.worldgrower.attribute.IdMap;

public class SexGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		Integer mateId = performer.getProperty(Constants.MATE_ID);
		IdMap relationships = performer.getProperty(Constants.RELATIONSHIPS);
				
		int bestId = relationships.findBestId(w -> true, world);
		
		if ((mateId != null)) {
			WorldObject target = world.findWorldObject(Constants.ID, mateId);
			return new OperationInfo(performer, target, new int[0], Actions.SEX_ACTION);
		} else if (bestId != -1 && relationships.getValue(bestId) > 750) {
			WorldObject target = world.findWorldObject(Constants.ID, bestId);
			return new OperationInfo(performer, target, new int[0], Actions.SEX_ACTION);
		} else {
			return new ImproveRelationshipGoal(bestId, 750, world).calculateGoal(performer, world);
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return false;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking to have sex";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
