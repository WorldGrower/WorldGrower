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

import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;

public class SirenCurse implements Curse {

	@Override
	public List<Goal> getCurseGoals(List<Goal> normalGoals) {
		return Arrays.asList(Goals.CURSE_KISS_GOAL, Goals.IDLE_GOAL);
	}
	
	@Override
	public void perform(WorldObject performer, WorldObject target, int[] args, ManagedOperation managedOperation, World world) {
		if (managedOperation == Actions.KISS_ACTION) {
			if (performer.getProperty(Constants.CURSE) == this) {
				performer.setProperty(Constants.CURSE, null);
			}
			
			if (target.getProperty(Constants.CURSE) == this) {
				performer.increment(Constants.HIT_POINTS, -100);
			}
		}
	}
	
	@Override
	public boolean canMove() {
		return false;
	}

	@Override
	public String getExplanation() {
		return "I've been cursed and I can't move from this spot. Only if someone kisses me will the curse be broken.";
	}

	@Override
	public boolean canTalk() {
		return true;
	}
}
