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
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.condition.LichUtils;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class BecomeLichGoal implements Goal {

	public BecomeLichGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (!Actions.LICH_TRANSFORMATION_ACTION.hasRequiredSoulGems(performer)) {
			return Goals.FILL_SOUL_GEM_GOAL.calculateGoal(performer, world);
		} else if (!Actions.LICH_TRANSFORMATION_ACTION.hasRequiredEnergy(performer)) {
			return Goals.REST_GOAL.calculateGoal(performer, world);
		} else {
			//TODO: knowledge that someone is lich? And transform in isolation?
			return new OperationInfo(performer, performer, Args.EMPTY, Actions.LICH_TRANSFORMATION_ACTION);
		}
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}
	
	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return LichUtils.isLich(performer);
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_BECOME_LICH);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return LichUtils.isLich(performer) ? 1 : 0;
	}


}