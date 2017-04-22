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

import java.util.EnumMap;

public enum TerrainType {
	WATER(		+0, +0, +0),
	DESERT(		-1, -1, -1),
	GRASLAND(	+1, +1, +0),
	HILL(		-1, +1, +1),
	MOUNTAIN(	-1, -1, -1),
	PLAINS(		+1, +1, +1),
	SNOW(		-1, -1, -1),
	TUNDRA(		-1, -1, -1),
	MARSH(		-1, -1, -1);
	
	private final EnumMap<TerrainResource, Integer> terrainResources = new EnumMap<>(TerrainResource.class);
	
	private TerrainType(int foodBonus, int woodBonus, int cottonBonus) {
		terrainResources.put(TerrainResource.FOOD, foodBonus);
		terrainResources.put(TerrainResource.WOOD, woodBonus);
		terrainResources.put(TerrainResource.COTTON, cottonBonus);
	}

	public String getDescription() {
		return name().toLowerCase();
	}

	public int getBonus(TerrainResource terrainResource) {
		return terrainResources.get(terrainResource);
	}

	public String getPercentageBonus(TerrainResource terrainResource, int defaultResourceIncrease) {
		int bonus = getBonus(terrainResource);
		String sign = (bonus >= 0 ? "+" : "");
		int bonusPercentage = (100 * bonus) / defaultResourceIncrease;
		return sign + bonusPercentage + "%";
	}
}
