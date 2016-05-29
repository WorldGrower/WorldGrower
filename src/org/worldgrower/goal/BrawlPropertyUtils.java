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

public class BrawlPropertyUtils {

	public static void startBrawl(WorldObject performer, WorldObject target, int brawlStakeGold) {
		performer.setProperty(Constants.BRAWL_OPPONENT_ID, target.getProperty(Constants.ID));
		target.setProperty(Constants.BRAWL_OPPONENT_ID, performer.getProperty(Constants.ID));
		
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, null);
		performer.setProperty(Constants.RIGHT_HAND_EQUIPMENT, null);
		performer.setProperty(Constants.BRAWL_STAKE_GOLD, brawlStakeGold);
		
		target.setProperty(Constants.LEFT_HAND_EQUIPMENT, null);
		target.setProperty(Constants.RIGHT_HAND_EQUIPMENT, null);
		target.setProperty(Constants.BRAWL_STAKE_GOLD, brawlStakeGold);
	}
	
	public static boolean isBrawling(WorldObject worldObject) {
		return worldObject.hasProperty(Constants.BRAWL_OPPONENT_ID);
	}

	public static int endBrawlWithPerformerVictory(WorldObject performer, WorldObject target) {
		performer.setProperty(Constants.BRAWL_OPPONENT_ID, BRAWLING_UNTIL_ON_TURN);
		target.setProperty(Constants.BRAWL_OPPONENT_ID, BRAWLING_UNTIL_ON_TURN);
		
		int goldWon = calculateGoldWon(performer, target);		
		performer.increment(Constants.GOLD, goldWon);
		target.increment(Constants.GOLD, -goldWon);
		
		performer.removeProperty(Constants.BRAWL_STAKE_GOLD);
		target.removeProperty(Constants.BRAWL_STAKE_GOLD);
		
		return goldWon;
	}
	
	private static final Integer BRAWLING_UNTIL_ON_TURN = null;
	
	public static void completelyEndBrawling(WorldObject performer) {
		if (performer.hasProperty(Constants.BRAWL_OPPONENT_ID) && performer.getProperty(Constants.BRAWL_OPPONENT_ID) == BRAWLING_UNTIL_ON_TURN) {
			performer.removeProperty(Constants.BRAWL_OPPONENT_ID);
		}
	}

	private static int calculateGoldWon(WorldObject performer, WorldObject target) {
		int brawlStakeGold = performer.getProperty(Constants.BRAWL_STAKE_GOLD);
		int targetGold = target.getProperty(Constants.GOLD);
		int goldWon = Math.min(brawlStakeGold, targetGold);
		return goldWon;
	}
}
