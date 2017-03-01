package org.worldgrower.terrain;

public class TerrainMapper {

	private final double waterCutoff;
	
	public static final double NO_WATER_CUTOFF = 0.0f;
	public static final double SCARCE_WATER_CUTOFF = 0.05f;
	public static final double NORMAL_WATER_CUTOFF = 0.1f;
	public static final double ABUNDANT_WATER_CUTOFF = 0.2f;
	public static final double HUGE_WATER_CUTOFF = 0.4f;
	
	public TerrainMapper() {
		this(NORMAL_WATER_CUTOFF);
	}
	
	public TerrainMapper(double waterCutoff) {
		super();
		this.waterCutoff = waterCutoff;
	}

	public TerrainType map(double terrainHeight) {
		if (terrainHeight < waterCutoff) {
			return TerrainType.WATER;
		} else if (terrainHeight < 0.5f) {
			return TerrainType.GRASLAND;
		} else if (terrainHeight < 0.75f){
			return TerrainType.PLAINS;
		} else if (terrainHeight < 0.9f){
			return TerrainType.HILL;
		} else if (terrainHeight < 1.0f){
			return TerrainType.MOUNTAIN;
		} else {
			return TerrainType.MOUNTAIN;
		}
	}
}
