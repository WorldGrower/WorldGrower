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
package org.worldgrower.terrain;

public enum TerrainType {
	WATER(0),
	DESERT(-1),
	GRASLAND(+1),
	HILL(-1),
	MOUNTAIN(-1),
	PLAINS(+1),
	SNOW(-1),
	TUNDRA(-1),
	MARSH(-1);
	
	private final int foodBonus;

	private TerrainType(int foodBonus) {
		this.foodBonus = foodBonus;
	}

	public int getFoodBonus() {
		return foodBonus;
	}
}
