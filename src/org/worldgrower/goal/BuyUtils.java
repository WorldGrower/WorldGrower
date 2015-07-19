/*******************************************************************************
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*******************************************************************************/
package org.worldgrower.goal;

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IntProperty;

public class BuyUtils {

	public static List<WorldObject> findBuyTargets(WorldObject performer, IntProperty property, World world) {
		List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.BUY_ACTION, w -> w.getProperty(Constants.INVENTORY).getQuantityFor(property, Constants.PRICE, inventoryItem -> inventoryItem.getProperty(Constants.SELLABLE)) > 0, world);
		return targets;
	}
}
