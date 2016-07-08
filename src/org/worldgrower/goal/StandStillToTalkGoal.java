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
import org.worldgrower.ManagedOperation;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;

public class StandStillToTalkGoal implements Goal {

	public StandStillToTalkGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> targets = someoneTalkedToPerformer(performer, world);
		if (targets.size() > 0) {
			WorldObject target = targets.get(0);
			if (performer.getProperty(Constants.RELATIONSHIPS).getValue(target) >= 0) {
				return new OperationInfo(performer, performer, Args.EMPTY, Actions.STAND_STILL_TO_TALK_ACTION);
			}
		}
		
		return null;
	}
	
	private List<WorldObject> someoneTalkedToPerformer(WorldObject performer, World world) {
		return world.findWorldObjectsByProperty(Constants.STRENGTH, w -> worldObjectTalkedWithPerformer(performer, w, world));
	}

	private boolean worldObjectTalkedWithPerformer(WorldObject performer, WorldObject w, World world) {
		OperationInfo lastPerformedOperation = world.getHistory().getLastPerformedOperation(w);
		if (lastPerformedOperation != null) {
			return lastPerformedOperation.getManagedOperation() == Actions.TALK_ACTION && lastPerformedOperation.getTarget().equals(performer);
		} else {
			return false;
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		if (movedLastTurn(performer, world)) {
			return someoneTalkedToPerformer(performer, world).size() == 0;
		} else {
			return true;
		}
	}
	
	private boolean movedLastTurn(WorldObject performer, World world) {
		OperationInfo lastPerformedOperation = world.getHistory().getLastPerformedOperation(performer);
		if (lastPerformedOperation != null) {
			ManagedOperation lastPerformedAction = lastPerformedOperation.getManagedOperation();
			return (lastPerformedAction == Actions.MOVE_ACTION) || (lastPerformedAction == Actions.STAND_STILL_TO_TALK_ACTION);
		} else {
			return false;
		}
	}

	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "interested in hearing what you have to say";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
