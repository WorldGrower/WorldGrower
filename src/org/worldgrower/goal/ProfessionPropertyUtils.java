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

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.history.HistoryItem;

public class ProfessionPropertyUtils {

	public static void enableTaxCollecting(WorldObject performer, World world) {
		performer.setProperty(Constants.CAN_COLLECT_TAXES, Boolean.TRUE);
		performer.setProperty(Constants.PROFESSION_START_TURN, world.getCurrentTurn().getValue());
	}
	
	public static void endTaxCollecting(WorldObject performer) {
		performer.removeProperty(Constants.CAN_COLLECT_TAXES);
		performer.removeProperty(Constants.PROFESSION_START_TURN);
	}
	
	public static boolean wasFiredOnce(WorldObject worldObject, World world) {
		List<HistoryItem> historyItems = world.getHistory().findHistoryItems(Actions.FIRE_PUBLIC_EMPLOYEE_ACTION);
		for(HistoryItem historyItem : historyItems) {
			if (historyItem.getTarget().equals(worldObject)) {
				return true;
			}
		}
		return false;
	}
}
