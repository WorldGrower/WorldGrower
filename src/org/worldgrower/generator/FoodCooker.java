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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.gui.ImageIds;

public class FoodCooker {

	private static final int COOKED_FOOD_VALUE = 2;
	private static final String COOKED_PREFIX = "cooked ";
	
	private static final Map<ImageIds, ImageIds> COOKED_IMAGES = new HashMap<>();
	static {
		COOKED_IMAGES.put(ImageIds.BERRY, ImageIds.COOKED_BERRIES);
		COOKED_IMAGES.put(ImageIds.MEAT, ImageIds.COOKED_MEAT);
		COOKED_IMAGES.put(ImageIds.RAW_FISH, ImageIds.COOKED_FISH);
		COOKED_IMAGES.put(ImageIds.EGG, ImageIds.COOKED_EGG);
	}
	
	public static void cook(WorldObject food) {
		food.setProperty(Constants.FOOD, COOKED_FOOD_VALUE);
		food.setProperty(Constants.IMAGE_ID, COOKED_IMAGES.get(food.getProperty(Constants.IMAGE_ID)));
	
		String originalName = food.getProperty(Constants.NAME);
		if (!originalName.startsWith(COOKED_PREFIX)) {
			food.setProperty(Constants.NAME, COOKED_PREFIX + originalName);
		}
	}
	
	public static int getIndexOfUncookedFood(WorldObjectContainer inventory) {
		return inventory.getIndexFor(Constants.FOOD, (Function<WorldObject,Boolean>)(w -> !isFoodCooked(w)));
	}
	
	public static int getIndexOfCookedFood(WorldObjectContainer inventory) {
		return inventory.getIndexFor(Constants.FOOD, (Function<WorldObject,Boolean>)(w -> isFoodCooked(w)));
	}
	
	private static boolean isFoodCooked(WorldObject food) {
		return food.getProperty(Constants.FOOD).intValue() == COOKED_FOOD_VALUE;
	}
}
