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
package org.worldgrower.generator;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.Condition;
import org.worldgrower.gui.ImageIds;

public class BerryBushImageCalculator {

	public static ImageIds getImageId(WorldObject berryBush, World world) {
		final ImageIds berryBushImageId;
		if (berryBush.getProperty(Constants.FOOD_SOURCE).hasEnoughFood()) {
			if (isWilting(berryBush)) {
				berryBushImageId = ImageIds.WILTING_BERRY_BUSH;
			} else {
				berryBushImageId = ImageIds.BUSH;
			}
		} else {
			if (isWilting(berryBush)) {
				berryBushImageId = ImageIds.YOUNG_WILTING_BERRY_BUSH;
			} else {
				berryBushImageId = ImageIds.YOUNG_BERRY_BUSH;
			}
		}
		return berryBushImageId;
	}

	private static boolean isWilting(WorldObject berryBush) {
		return berryBush.getProperty(Constants.CONDITIONS).hasCondition(Condition.WILTING_CONDITION);
	}
}
