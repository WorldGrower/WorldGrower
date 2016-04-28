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
package org.worldgrower;

import org.worldgrower.goal.LocationPropertyUtils;

/**
 * Convenience class that checks whether a worldObject occupies a certain position
 */
public class UnpassableCreaturePositionCondition implements WorldObjectCondition {
	private final CreaturePositionCondition creaturePositionCondition;

	public UnpassableCreaturePositionCondition(int row, int column) {
		this.creaturePositionCondition = new CreaturePositionCondition(row, column);
	}

	@Override
	public boolean isWorldObjectValid(WorldObject worldObject) {
		if (LocationPropertyUtils.isPassable(worldObject)) {
			return false;
		} else {
			return creaturePositionCondition.isWorldObjectValid(worldObject);
		}
	}
}