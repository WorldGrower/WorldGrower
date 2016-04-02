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

import java.util.ArrayList;
import java.util.List;

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdMap;
import org.worldgrower.generator.BuildingGenerator;

public class ReleaseCapturedCriminalsGoal implements Goal {

	public ReleaseCapturedCriminalsGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> criminalsToBeReleased = findCriminalsToBeReleased(performer, world);
		
		if (criminalsToBeReleased.size() > 0) {
			for(WorldObject criminalToBeReleased : criminalsToBeReleased) {
				int criminalX = criminalToBeReleased.getProperty(Constants.X);
				int criminalY = criminalToBeReleased.getProperty(Constants.Y);
				List<WorldObject> jailDoors = world.findWorldObjects(w -> isJailDoor(criminalX, criminalY, w));
				if (jailDoors.size() > 0) {
					return new OperationInfo(performer, jailDoors.get(0), Args.EMPTY, Actions.UNLOCK_JAIL_DOOR_ACTION);
				}
			}
		}
		return null;
	}

	private boolean isJailDoor(int criminalX, int criminalY, WorldObject w) {
		return BuildingGenerator.isJailDoor(w) && w.getProperty(Constants.X) == criminalX && w.getProperty(Constants.Y) == criminalY+1;
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}
	
	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		List<WorldObject> worldObjects = findCriminalsToBeReleased(performer, world);
		return worldObjects.isEmpty();
	}

	private List<WorldObject> findCriminalsToBeReleased(WorldObject performer, World world) {
		List<WorldObject> criminalsToBeReleased = new ArrayList<>();
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		IdMap turnsInJail = villagersOrganization.getProperty(Constants.TURNS_IN_JAIL);
		for(int id : turnsInJail.getIds()) {
			int startTurnServed = turnsInJail.getValue(id);
			int currentTurn = world.getCurrentTurn().getValue();
			if ((currentTurn - startTurnServed) > 500) {
				criminalsToBeReleased.add(world.findWorldObject(Constants.ID, id));
			}
		}
		
		return criminalsToBeReleased;
	}

	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "releasing criminals that have served their time";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		List<WorldObject> worldObjects = findCriminalsToBeReleased(performer, world);
		return Integer.MAX_VALUE - worldObjects.size();
	}
}
