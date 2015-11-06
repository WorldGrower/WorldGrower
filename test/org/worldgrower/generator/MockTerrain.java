package org.worldgrower.generator;

import org.worldgrower.terrain.Terrain;
import org.worldgrower.terrain.TerrainInfo;
import org.worldgrower.terrain.TerrainType;

class MockTerrain implements Terrain {

	private final TerrainType terrainType;
	
	public MockTerrain(TerrainType terrainType) {
		super();
		this.terrainType = terrainType;
	}

	@Override
	public TerrainInfo getTerrainInfo(int x, int y) {
		return new TerrainInfo(terrainType);
	}

	@Override
	public int getWidth() {
		return 10;
	}

	@Override
	public int getHeight() {
		return 10;
	}

	@Override
	public boolean isExplored(int x, int y) {
		return false;
	}

	@Override
	public void explore(int x, int y, int radius) {
	}
	
}