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
package org.worldgrower.deity;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.actions.Actions;

public class DeityAttributes implements Serializable {

	private static final int MIN_HAPINESS_VALUE = -3000;
	private static final int MAX_HAPINESS_VALUE = 3000;
	
	private final Map<Deity, Integer> deityHapinessMap = new HashMap<>();

	public DeityAttributes() {
		for(Deity deity : Deity.ALL_DEITIES) {
			deityHapinessMap.put(deity, MAX_HAPINESS_VALUE);
		}
	}
	
	public int getHappiness(Deity deity) {
		return deityHapinessMap.get(deity);
	}
	
	public int getMinHapinessValue() {
		return MIN_HAPINESS_VALUE;
	}

	public int getMaxHapinessValue() {
		return MAX_HAPINESS_VALUE;
	}

	public void onTurn(World world) {
		Map<Deity, Integer> worshippersByDeity = DeityPropertyUtils.getWorshippersByDeity(world);
		int totalNumberOfWorshippers = worshippersByDeity.size();
		WorshipActionStatistics worshipActionStatistics = worshipActionStatistics(world);
		
		for(Deity deity : Deity.ALL_DEITIES) {
			int hapinessDelta = calculateHapinessDelta(worshippersByDeity, totalNumberOfWorshippers, worshipActionStatistics, deity);
		
			int newHapinessValue = deityHapinessMap.get(deity) + hapinessDelta;
			if (newHapinessValue < MIN_HAPINESS_VALUE) {
				newHapinessValue = MIN_HAPINESS_VALUE;
			}
			if (newHapinessValue > MAX_HAPINESS_VALUE) {
				newHapinessValue = MAX_HAPINESS_VALUE;
			}
			deityHapinessMap.put(deity, newHapinessValue);
		}
	}

	int calculateHapinessDelta(Map<Deity, Integer> worshippersByDeity, int totalNumberOfWorshippers, WorshipActionStatistics worshipActionStatistics, Deity deity) {
		int hapinessDelta = 0;
		int deityWorshipperCount = worshippersByDeity.get(deity);
		float expectedWorshipperCount = totalNumberOfWorshippers / 18f;
		if (deityWorshipperCount == expectedWorshipperCount) {
			hapinessDelta = 0;
		} else if (deityWorshipperCount < expectedWorshipperCount) {
			hapinessDelta--;
		} else {
			hapinessDelta += 3;
		}
		if (worshipActionStatistics.getTotalWorshipActions() > 0) {
			float hapinessPerWorshipAction = 30f / (worshipActionStatistics.getTotalWorshipActions());
			
			hapinessDelta += (hapinessPerWorshipAction * worshipActionStatistics.getWorshipCount(deity));
		}
		return hapinessDelta;
	}
	
	private WorshipActionStatistics worshipActionStatistics(World world) {
		WorshipActionStatistics worshipActionStatistics = new WorshipActionStatistics();
		int totalWorshipActions = 0;
		
		Collection<OperationInfo> allLastPerformedOperations = world.getHistory().getAllLastPerformedOperations();
		for(OperationInfo operationInfo : allLastPerformedOperations) {
			if (operationInfo.getManagedOperation() == Actions.WORSHIP_DEITY_ACTION) {
				Deity deity = operationInfo.getPerformer().getProperty(Constants.DEITY);
				worshipActionStatistics.incrementWorshipCount(deity);
			}
		}
		
		worshipActionStatistics.setTotalWorshipActions(totalWorshipActions);
		return worshipActionStatistics;
	}
	
	static class WorshipActionStatistics {
		private final Map<Deity, Integer> worshipActionsByDeity = new HashMap<>();
		private int totalWorshipActions;

		public WorshipActionStatistics() {
			for(Deity deity : Deity.ALL_DEITIES) {
				worshipActionsByDeity.put(deity, 0);
			}
		}
		
		public void incrementWorshipCount(Deity deity) {
			worshipActionsByDeity.put(deity, worshipActionsByDeity.get(deity) + 1);
		}
		
		public int getWorshipCount(Deity deity) {
			return worshipActionsByDeity.get(deity);
		}

		public int getTotalWorshipActions() {
			return totalWorshipActions;
		}

		public void setTotalWorshipActions(int totalWorshipActions) {
			this.totalWorshipActions = totalWorshipActions;
		}
	}

	public boolean isUnHappy(Deity deity) {
		return getHappiness(deity) == MIN_HAPINESS_VALUE;
	}
}
