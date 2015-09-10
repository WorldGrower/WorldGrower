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

	public static void startBrawl(WorldObject performer, WorldObject target) {
		performer.setProperty(Constants.BRAWL_OPPONENT_ID, target.getProperty(Constants.ID));
		target.setProperty(Constants.BRAWL_OPPONENT_ID, performer.getProperty(Constants.ID));
		
		performer.setProperty(Constants.LEFT_HAND_EQUIPMENT, null);
		performer.setProperty(Constants.RIGHT_HAND_EQUIPMENT, null);
		
		target.setProperty(Constants.LEFT_HAND_EQUIPMENT, null);
		target.setProperty(Constants.RIGHT_HAND_EQUIPMENT, null);
	}
	
	public static boolean isBrawling(WorldObject worldObject) {
		Integer brawlOpponentId = worldObject.getProperty(Constants.BRAWL_OPPONENT_ID);
		return brawlOpponentId != null;
	}

	public static void endBrawl(WorldObject performer, WorldObject target) {
		performer.setProperty(Constants.BRAWL_OPPONENT_ID, null);
		target.setProperty(Constants.BRAWL_OPPONENT_ID, null);
	}
}
