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
import java.util.Map;

import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.legal.DefaultActionLegalHandler;
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.actions.legal.LegalActions;

public class LegalizeVampirismGoal implements Goal {

	public LegalizeVampirismGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (GroupPropertyUtils.performerIsLeaderOfVillagers(performer, world)) {
			return legalizeVampireBiteAction(performer, world);
		} else {
			WorldObject leaderOfVillagers = GroupPropertyUtils.getLeaderOfVillagers(world);
			if (leaderOfVillagers != null) {
				
			} else {
				
			}
					
		}
		WorldObject target = GoalUtils.findNearestTarget(performer, Actions.VAMPIRE_BITE_ACTION, world);
		if (target != null) {
			return new OperationInfo(performer, target, new int[0], Actions.VAMPIRE_BITE_ACTION);
		} else {
			return null;
		}
	}

	private OperationInfo legalizeVampireBiteAction(WorldObject performer, World world) {
		LegalActions legalActions = LegalActionsPropertyUtils.getLegalActions(world);
		Map<LegalAction, Boolean> legalFlags = legalActions.getLegalActions();
		legalFlags.put(new LegalAction(Actions.VAMPIRE_BITE_ACTION, new DefaultActionLegalHandler()), Boolean.TRUE);
		int[] args = LegalActions.legalFlagsToArgs(legalFlags);
		return new OperationInfo(performer, performer, args, Actions.SET_LEGAL_ACTIONS_ACTION);
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return LegalActionsPropertyUtils.getLegalActions(world).getLegalActions().get(Actions.VAMPIRE_BITE_ACTION);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "looking to legalize vampirism";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return LegalActionsPropertyUtils.getLegalActions(world).getLegalActions().get(Actions.VAMPIRE_BITE_ACTION) ? 1 : 0;
	}
}
