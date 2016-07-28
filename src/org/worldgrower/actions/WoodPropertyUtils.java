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

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;

public class WoodPropertyUtils {

	public static void checkWoodSourceExhausted(WorldObject target) {
		int targetWoodSource = target.getProperty(Constants.WOOD_SOURCE);
		if (targetWoodSource <= 20 && Constants.WOOD_PRODUCED.isAtMax(target)) {
			target.setProperty(Constants.HIT_POINTS, 0);
		}
	}
	
	public static boolean woodSourceHasEnoughWood(WorldObject target) {
		return (target.hasProperty(Constants.WOOD_SOURCE)) && (target.getProperty(Constants.WOOD_SOURCE) > 10);
	}
}
