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

/**
 * Convenience class that checks whether a worldObject occupies a certain position
 */
public class CreaturePositionCondition implements WorldObjectCondition {
	private final int row;
	private final int column;

	public CreaturePositionCondition(int row, int column) {
		this.row = row;
		this.column = column;
	}

	@Override
	public boolean isWorldObjectValid(WorldObject worldObject) {
		int x = worldObject.getProperty(Constants.X);
		int y = worldObject.getProperty(Constants.Y);
		int width = worldObject.getProperty(Constants.WIDTH);
		int height = worldObject.getProperty(Constants.HEIGHT);
		if ((width == 1) && (height == 1)) {
			return ((column == x) && (row == y));
		} else {
			return (x <= column) && (column < x + width) && (y <= row) && (row < y + height);
		}
	}
}