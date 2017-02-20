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
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class HealOthersGoal implements Goal {

	public HealOthersGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (MagicSpellUtils.canCast(performer, Actions.MINOR_HEAL_ACTION)) {
			if (Actions.MINOR_HEAL_ACTION.hasRequiredEnergy(performer)) {
				List<WorldObject> healTargets = findTargetsToHeal(performer, world);
				if (healTargets.size()  > 0) {
					return new OperationInfo(performer, performer, Args.EMPTY, Actions.MINOR_HEAL_ACTION);
				}
			} else {
				return Goals.REST_GOAL.calculateGoal(performer, world);
			}
		} else {
			return new ScribeClericSpellsGoal().calculateGoal(performer, world);
		}
		return null;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return findTargetsToHeal(performer, world).size() == 0;
	}
	
	private List<WorldObject> findTargetsToHeal(WorldObject performer, World world) {
		List<WorldObject> targetsToHeal = GoalUtils.findNearestTargetsByProperty(performer, Actions.MINOR_HEAL_ACTION, Constants.STRENGTH, w -> isTargetForMinorHeal(performer, w), world);
		return targetsToHeal;
	}

	private boolean isTargetForMinorHeal(WorldObject performer, WorldObject w) {
		return (!GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w)) && w.getProperty(Constants.HIT_POINTS) < w.getProperty(Constants.HIT_POINTS_MAX);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_HEAL_OTHERS);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return Integer.MAX_VALUE - findTargetsToHeal(performer, world).size();
	}
}
