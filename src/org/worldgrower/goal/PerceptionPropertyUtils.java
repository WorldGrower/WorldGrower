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
import org.worldgrower.World;
import org.worldgrower.WorldObject;

public class PerceptionPropertyUtils {

	public static int calculateRadius(WorldObject worldObject, World world) {
		int currentTurn = world.getCurrentTurn().getValue();
		int perception = worldObject.getProperty(Constants.PERCEPTION_SKILL).getLevel(worldObject);
		int perceptionRadius = (int) Math.log(perception + 1);
		double darknessModifier = Math.sin(currentTurn * Math.PI / 100);
		int radius = 13 + (int)(darknessModifier * 5) + perceptionRadius;
		return radius;
	}
}
