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

import java.util.Map;

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.generator.Item;

public class HitPointPropertyUtils {

	public static void addHitPointProperties(Map<ManagedProperty<?>, Object> properties) {
		int hitPoints = calculateHitPoints(properties);
		
		properties.put(Constants.HIT_POINTS, hitPoints);
		properties.put(Constants.HIT_POINTS_MAX, hitPoints);
	}

	private static int calculateHitPoints(Map<ManagedProperty<?>, Object> properties) {
		int constitution = (int) properties.get(Constants.CONSTITUTION);
		int level = (int) properties.get(Constants.LEVEL);
		return calculateHitPoints(constitution, level);
	}

	private static int calculateHitPoints(int constitution, int level) {
		int hitPoints = (10 + constitution + (level - 1)) * Item.COMBAT_MULTIPLIER;
		return hitPoints;
	}
	
	public static int calculateHitPoints(WorldObject worldObject) {
		int constitution = worldObject.getProperty(Constants.CONSTITUTION);
		int level = worldObject.getProperty(Constants.LEVEL);
		return calculateHitPoints(constitution, level);
	}
}
