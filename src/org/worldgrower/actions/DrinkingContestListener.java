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
package org.worldgrower.actions;

import java.util.ArrayList;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.ManagedOperationListener;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.Condition;
import org.worldgrower.goal.DrinkingContestPropertyUtils;

public class DrinkingContestListener implements ManagedOperationListener {

	public final List<DrinkingContestFinishedListener> listeners = new ArrayList<>(); 
	
	@Override
	public void actionPerformed(ManagedOperation managedOperation, WorldObject performer, WorldObject target, int[] args, Object value) {
		if (DrinkingContestPropertyUtils.isDrinking(performer) && DrinkingContestPropertyUtils.isDrinking(target) && managedOperation == Actions.DRINK_FROM_INVENTORY_ACTION) {
			if (performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.INTOXICATED_CONDITION)) {
				performerWonDrinkingContest(performer, target);
			}
		} else if (DrinkingContestPropertyUtils.isDrinking(performer) && DrinkingContestPropertyUtils.isDrinking(target) && managedOperation != Actions.DRINK_FROM_INVENTORY_ACTION) {
			performerWonDrinkingContest(target, performer);
		}
	}

	private void performerWonDrinkingContest(WorldObject performer, WorldObject target) {
		int goldWon = DrinkingContestPropertyUtils.endDrinkingContestWithPerformerVictory(performer, target);
		
		for(DrinkingContestFinishedListener listener : listeners) {
			listener.drinkingContestFinished(performer, target, goldWon);
		}
	}

	public void addDrinkingContestFinishedListener(DrinkingContestFinishedListener drinkingContestFinishedListener) {
		listeners.add(drinkingContestFinishedListener);
	}
}
