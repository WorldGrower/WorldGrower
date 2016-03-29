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
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.generator.Item;

public class HitPointPropertyUtils {

	public static void addHitPointProperties(Map<ManagedProperty<?>, Object> properties) {
		int constitution = (int) properties.get(Constants.CONSTITUTION);
		int hitPoints = (10 + constitution) * Item.COMBAT_MULTIPLIER;
		
		properties.put(Constants.HIT_POINTS, hitPoints);
		properties.put(Constants.HIT_POINTS_MAX, hitPoints);
	}
}
