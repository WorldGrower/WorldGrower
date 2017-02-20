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
import org.worldgrower.actions.BuildShrineAction;
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.actions.legal.LegalActions;
import org.worldgrower.deity.Deity;
import org.worldgrower.generator.BuildingDimensions;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class ShrineToDeityGoal implements Goal {

	public ShrineToDeityGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		Deity performerDeity = performer.getProperty(Constants.DEITY);
		List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.WORSHIP_DEITY_ACTION, w -> w.getProperty(Constants.DEITY) == performerDeity, world);
		boolean notWorshippedYet = targets.size() > 0 ? !GoalUtils.actionAlreadyPerformed(performer, targets.get(0), Actions.WORSHIP_DEITY_ACTION, Args.EMPTY, world) : true;
		if (targets.size() > 0 && notWorshippedYet && isWorshipAllowed(performerDeity, world)) {
			return new OperationInfo(performer, targets.get(0), Args.EMPTY, Actions.WORSHIP_DEITY_ACTION);
		} else if ((targets.size() == 0) && !BuildShrineAction.hasEnoughStone(performer)) {
				return Goals.STONE_GOAL.calculateGoal(performer, world);
		} else if (targets.size() == 0) {
			WorldObject target = BuildLocationUtils.findOpenLocationNearExistingProperty(performer, BuildingDimensions.SHRINE, world);
			if (target != null) {
				return new OperationInfo(performer, target, Args.EMPTY, Actions.BUILD_SHRINE_ACTION);
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
		return (performer.getProperty(Constants.PLACE_OF_WORSHIP_ID) != null);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_SHRINE_TO_DEITY);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		int placeOfWorshipEvaluation = (performer.getProperty(Constants.PLACE_OF_WORSHIP_ID) != null) ? 1 : 0;
		Deity performerDeity = performer.getProperty(Constants.DEITY);
		final int worshipAllowedEvaluation;
		if (performerDeity != null) {
			worshipAllowedEvaluation = isWorshipAllowed(performerDeity, world) ? 1 : 0;
		} else {
			worshipAllowedEvaluation = 1;
		}
		return placeOfWorshipEvaluation + worshipAllowedEvaluation;
	}
}
