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

import java.util.ArrayList;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.deity.Deity;

public class ChooseDeityGoal implements Goal {

	public ChooseDeityGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<DeityReason> deityReasons = new ArrayList<>();
		for(Deity deity : Deity.ALL_DEITIES) {
			int reasonIndex = deity.getReasonIndex(performer, world);
			if (reasonIndex != -1) {
				deityReasons.add(new DeityReason(deity, reasonIndex));
			}
		}
		final int indexOfDeity;
		final int reasonIndex;
		if (deityReasons.size() > 0) {
			int randomDeityIndex = getRandomValue(0, deityReasons.size() - 1);
			Deity randomDeity = deityReasons.get(randomDeityIndex).getDeity();
			indexOfDeity = Deity.ALL_DEITIES.indexOf(randomDeity);
			reasonIndex = deityReasons.get(randomDeityIndex).getReasonIndex();
		} else {
			int randomDeityIndex = getRandomValue(0, Deity.ALL_DEITIES.size() - 1);
			indexOfDeity = randomDeityIndex;
			reasonIndex = -1;
		}
		
		return new OperationInfo(performer, performer, new int[] { indexOfDeity, reasonIndex }, Actions.CHOOSE_DEITY_ACTION);
	}
	
	private int getRandomValue(int min, int max) {
		int range = (max - min) + 1;     
		return (int)(Math.random() * range) + min;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return (performer.hasProperty(Constants.DEITY) && performer.getProperty(Constants.DEITY) != null);
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
		return (performer.getProperty(Constants.DEITY) != null) ? 1 : 0;
	}
	
	private static class DeityReason {
		private final Deity deity;
		private final int reasonIndex;
		
		public DeityReason(Deity deity, int reasonIndex) {
			this.deity = deity;
			this.reasonIndex = reasonIndex;
		}

		public Deity getDeity() {
			return deity;
		}

		public int getReasonIndex() {
			return reasonIndex;
		}
	}
}
