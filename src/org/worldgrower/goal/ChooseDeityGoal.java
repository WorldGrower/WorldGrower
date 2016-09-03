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
import java.util.stream.Collectors;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.deity.Deity;
import org.worldgrower.profession.Professions;

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
			int chosenDeityIndex = chooseDeityIndex(performer, deityReasons, world);
			Deity chosenDeity = deityReasons.get(chosenDeityIndex).getDeity();
			indexOfDeity = Deity.ALL_DEITIES.indexOf(chosenDeity);
			reasonIndex = deityReasons.get(chosenDeityIndex).getReasonIndex();
		} else {
			int randomDeityIndex = getRandomValue(performer, 0, Deity.ALL_DEITIES.size() - 1);
			indexOfDeity = randomDeityIndex;
			reasonIndex = -1;
		}
		
		return new OperationInfo(performer, performer, new int[] { indexOfDeity, reasonIndex }, Actions.CHOOSE_DEITY_ACTION);
	}

	int chooseDeityIndex(WorldObject performer, List<DeityReason> deityReasons, World world) {
		if (performer.getProperty(Constants.PROFESSION) == Professions.PRIEST_PROFESSION) {
			int bestIndex = -1;
			int bestEvaluation = Integer.MIN_VALUE;
			for(int i=0; i<deityReasons.size(); i++) {
				DeityReason deityReason = deityReasons.get(i);
				int deityEvaluation = evaluateDeity(deityReason.getDeity(), world);
				if (deityEvaluation > bestEvaluation) {
					bestIndex = i;
					bestEvaluation = deityEvaluation;
				}
			}
			return bestIndex;
		} else {
			return getRandomValue(performer, 0, deityReasons.size() - 1);
		}
	}
	
	private int evaluateDeity(Deity deity, World world) {
		List<WorldObject> worshippers = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> w.hasProperty(Constants.DEITY) &&  w.getProperty(Constants.DEITY) == deity);
		int priestWorshipperCount = worshippers.stream().filter(w -> w.getProperty(Constants.PROFESSION) == Professions.PRIEST_PROFESSION).collect(Collectors.toList()).size();
		int nonPriestWorshipperCount = worshippers.size() - priestWorshipperCount;
		return (100 * nonPriestWorshipperCount) / (1 + priestWorshipperCount);
	}

	private int getRandomValue(WorldObject performer, int min, int max) {
		int range = (max - min) + 1;
		
		String performerName = performer.getProperty(Constants.NAME);
		if (performerName.length() > 0) {
			performerName = performerName.toUpperCase();
			char firstLetter = performerName.charAt(0);
			int charOffset = 'Z' - firstLetter;
			
			int index = charOffset % range;
			return index + min;
		} else {
			return 0;
		}
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
		return "looking for a deity to worship";
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
