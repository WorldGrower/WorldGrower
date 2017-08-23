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

import java.awt.Rectangle;

import org.worldgrower.terrain.Terrain;
import org.worldgrower.terrain.TerrainType;

public class MockTerrain implements Terrain {
	private static final int DIMENSION = 10;
	
	private final TerrainType[][] terrainTypes;
	
	public MockTerrain(TerrainType terrainType) {
		super();
		terrainTypes = new TerrainType[DIMENSION][DIMENSION];
		for(int i=0; i<DIMENSION; i++) {
			for(int j=0; j<DIMENSION; j++) {
				terrainTypes[i][j] = terrainType;
			}
		}
	}

	@Override
	public TerrainType getTerrainType(int x, int y) {
		return terrainTypes[x][y];
	}

	@Override
	public int getWidth() {
		return DIMENSION;
	}

	@Override
	public int getHeight() {
		return DIMENSION;
	}

	@Override
	public boolean isExplored(int x, int y) {
		return false;
	}

	@Override
	public void explore(int x, int y, int radius) {
	}
	
	public void setTerrainType(int x, int y, TerrainType terrainType) {
		terrainTypes[x][y] = terrainType;
	}

	@Override
	public Rectangle getExploredBoundsInSquares() {
		return null;
	}
}