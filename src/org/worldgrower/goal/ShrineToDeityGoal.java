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
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.actions.legal.LegalActions;
import org.worldgrower.actions.legal.WorshipDeityLegalHandler;
import org.worldgrower.deity.Deity;

public class ShrineToDeityGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		Deity performerDeity = performer.getProperty(Constants.DEITY);
		List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.WORSHIP_DEITY_ACTION, w -> w.getProperty(Constants.DEITY) == performerDeity, world);
		boolean notWorshippedYet = targets.size() > 0 ? !GoalUtils.actionAlreadyPerformed(performer, targets.get(0), Actions.WORSHIP_DEITY_ACTION, new int[0], world) : true;
		if (targets.size() > 0 && notWorshippedYet && isWorshipAllowed(performerDeity, world)) {
			return new OperationInfo(performer, targets.get(0), new int[0], Actions.WORSHIP_DEITY_ACTION);
		} else if ((targets.size() == 0) && performer.getProperty(Constants.INVENTORY).getQuantityFor(Constants.STONE) < 8) {
				return new StoneGoal().calculateGoal(performer, world);
		} else if (targets.size() == 0) {
			WorldObject target = BuildLocationUtils.findOpenLocationNearExistingProperty(performer, 2, 3, world);
			if (target != null) {
				return new OperationInfo(performer, target, new int[0], Actions.BUILD_SHRINE_ACTION);
			}
		}
		return null;
	}
	
	private boolean isWorshipAllowed(Deity deity, World world) {
		LegalAction legalAction = new LegalAction(Actions.WORSHIP_DEITY_ACTION, new WorshipDeityLegalHandler(deity));
		LegalActions legalActions = LegalActionsPropertyUtils.getLegalActions(world);
		return legalActions.getLegalFlag(legalAction);
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return (performer.getProperty(Constants.PLACE_OF_WORSHIP_ID) != null);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "choosing a deity";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return (performer.getProperty(Constants.PLACE_OF_WORSHIP_ID) != null) ? 1 : 0;
	}
}
