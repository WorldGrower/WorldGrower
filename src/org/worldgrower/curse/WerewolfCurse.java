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
package org.worldgrower.curse;

import java.util.ArrayList;
import java.util.List;

import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;

public class WerewolfCurse implements Curse {

	@Override
	public List<Goal> getCurseGoals(List<Goal> normalGoals) {
		List<Goal> allGoals = new ArrayList<>();
		allGoals.add(Goals.KILL_OUTSIDERS_GOAL);
		allGoals.add(Goals.IDLE_GOAL);
		return allGoals;
	}
	
	@Override
	public void perform(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation, World world) {
	}
	
	@Override
	public boolean canMove() {
		return true;
	}
	
	@Override
	public boolean canTalk() {
		return false;
	}

	@Override
	public String getExplanation() {
		return "I've been turned into a werewolf";
	}

	@Override
	public String getName() {
		return "werewolf";
	}
}
