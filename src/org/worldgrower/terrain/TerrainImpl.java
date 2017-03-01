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

import java.io.Serializable;


public class TerrainImpl implements Terrain, Serializable {

	private final int width;
	private final int height;
	
	private final TerrainInfo[][] terrainInfos;
	private final boolean[][] explored;
	
	public TerrainImpl(int width, int height, TerrainMapper terrainMapper) {
		this.width = width;
		this.height = height;
		this.terrainInfos = new TerrainInfo[width][height];
		fillTerrainInfo(terrainMapper);
		this.explored = new boolean[width][height];
	}

	private void fillTerrainInfo(TerrainMapper terrainMapper) {
		Noise noise = new Noise(666);
		double[] heights = noise.normalize(noise.smoothNoise(width, height, 32));
		int counter = 0;
		for(int x=0; x<width; x++) {
			for(int y=0; y<height; y++) {
				double terrainHeight = heights[counter++];
				terrainInfos[x][y] = new TerrainInfo(terrainMapper.map(terrainHeight));
			}
		}
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public TerrainInfo getTerrainInfo(int x, int y) {
		return terrainInfos[x][y];
	}

	@Override
	public boolean isExplored(int x, int y) {
		if (isWithinBounds(x, y)) {
			return explored[x][y];
		} else {
			return false;
		}
	}

	private boolean isWithinBounds(int x, int y) {
		return (x >= 0) && (x < width) && (y >= 0) && (y < height);
	}

	@Override
	public void explore(int x, int y, int radius) {
		for(int i=0; i<width; i++) {
			for(int j=0; j<height; j++) {
				explored[i][j] = false;
			}
		}
		
		for(int i=x-radius; i<x+radius; i++) {
			for(int j=y-radius; j<y+radius; j++) {
				if (isWithinBounds(i, j)) {
					explored[i][j] = true;
				}
			}
		}
		
	}
}
