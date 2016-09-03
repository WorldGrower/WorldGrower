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
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.actions.legal.LegalActions;
import org.worldgrower.condition.ConditionUtils;
import org.worldgrower.deity.Deity;

public class DeityBoonGoal implements Goal {

	private final Deity deity;
	
	public DeityBoonGoal(Deity deity) {
		this.deity = deity;
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (performer.getProperty(Constants.DEITY) == deity && isWorshipAllowed(deity, world)) {
			List<WorldObject> targets = GoalUtils.findNearestTargetsByProperty(performer, Actions.WORSHIP_DEITY_ACTION, Constants.CAN_BE_WORSHIPPED, w -> w.getProperty(Constants.DEITY) == deity, world);
			if (targets.size() > 0) {
				return new OperationInfo(performer, targets.get(0), Args.EMPTY, Actions.WORSHIP_DEITY_ACTION);
			}
		}
		return null;
	}
	
	private boolean isWorshipAllowed(Deity deity, World world) {
		LegalActions legalActions = LegalActionsPropertyUtils.getLegalActions(world);
		return legalActions.getLegalFlag(LegalAction.getWorshipLegalActionFor(deity));
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		if (performer.getProperty(Constants.DEITY) == deity && isWorshipAllowed(deity, world)) {
			return ConditionUtils.performerHasCondition(performer, deity.getBoon());
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
		return "worshipping " + deity.getName();
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}