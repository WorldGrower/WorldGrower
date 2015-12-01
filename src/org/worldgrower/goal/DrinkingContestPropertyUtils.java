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

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;

public class DrinkingContestPropertyUtils {

	public static void startDrinkingContest(WorldObject performer, WorldObject target, int drinkingContestStakeGold) {
		performer.setProperty(Constants.DRINKING_CONTEST_OPPONENT_ID, target.getProperty(Constants.ID));
		target.setProperty(Constants.DRINKING_CONTEST_OPPONENT_ID, performer.getProperty(Constants.ID));
		
		performer.setProperty(Constants.DRINKING_CONTEST_STAKE_GOLD, drinkingContestStakeGold);
		target.setProperty(Constants.DRINKING_CONTEST_STAKE_GOLD, drinkingContestStakeGold);
	}
	
	public static boolean isDrinking(WorldObject worldObject) {
		Integer opponentId = worldObject.getProperty(Constants.DRINKING_CONTEST_OPPONENT_ID);
		return opponentId != null;
	}

	public static int endDrinkingContestWithPerformerVictory(WorldObject performer, WorldObject target) {
		performer.setProperty(Constants.DRINKING_CONTEST_OPPONENT_ID, null);
		target.setProperty(Constants.DRINKING_CONTEST_OPPONENT_ID, null);
		
		int goldWon = calculateGoldWon(performer, target);		
		performer.increment(Constants.GOLD, goldWon);
		target.increment(Constants.GOLD, -goldWon);
		
		performer.setProperty(Constants.DRINKING_CONTEST_STAKE_GOLD, null);
		target.setProperty(Constants.DRINKING_CONTEST_STAKE_GOLD, null);
		
		return goldWon;
	}

	private static int calculateGoldWon(WorldObject performer, WorldObject target) {
		int drinkingContestStakeGold = performer.getProperty(Constants.DRINKING_CONTEST_STAKE_GOLD);
		int targetGold = target.getProperty(Constants.GOLD);
		int goldWon = Math.min(drinkingContestStakeGold, targetGold);
		return goldWon;
	}
}
