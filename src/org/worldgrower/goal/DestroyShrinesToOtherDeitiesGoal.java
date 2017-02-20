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
import org.worldgrower.deity.Deity;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class DestroyShrinesToOtherDeitiesGoal implements Goal {

	public DestroyShrinesToOtherDeitiesGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> targets = findShrinesToOtherDeities(performer, world);
		if (targets.size() > 0) {
			return new AttackTargetGoal(targets.get(0)).calculateGoal(performer, world);
		} else {
			return null;
		}
	}
	
	private List<WorldObject> findShrinesToOtherDeities(WorldObject performer, World world) {
		Deity performerDeity = performer.getProperty(Constants.DEITY);
		List<WorldObject> shrinesToOtherDeities = GoalUtils.findNearestTargetsByProperty(performer, Actions.WORSHIP_DEITY_ACTION, Constants.CAN_BE_WORSHIPPED, w -> (w.getProperty(Constants.DEITY) != performerDeity), world);
		return shrinesToOtherDeities;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return findShrinesToOtherDeities(performer, world).size() == 0;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_DESTROY_SHRINES_TO_OTHER_DEITIES);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return Integer.MAX_VALUE - findShrinesToOtherDeities(performer, world).size();
	}
}
