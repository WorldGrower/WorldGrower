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
package org.worldgrower.gui.start;

import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.generator.CreatureGenerator;
import org.worldgrower.generator.PlantGenerator;
import org.worldgrower.generator.TerrainGenerator;
import org.worldgrower.generator.WorldGenerator;
import org.worldgrower.gui.AdditionalManagedOperationListenerFactory;
import org.worldgrower.terrain.TerrainType;

public class CustomGameParameters implements GameParameters {

	private final String playerName;
	private final String playerProfession;
	private final String gender;
	private final int worldWidth;
	private final int worldHeight;
	private final int enemyDensity;
	private final int villagerCount;
	private final int seed;
	private final int startTurn;
	private final boolean playBackgroundMusic; 
	
	public CustomGameParameters(String playerName, String playerProfession, String gender, int worldWidth, int worldHeight, int enemyDensity, int villagerCount, int seed, int startTurn, boolean playBackgroundMusic) {
		this.playerName = playerName;
		this.playerProfession = playerProfession;
		this.gender = gender;
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		this.enemyDensity = enemyDensity;
		this.villagerCount = villagerCount;
		this.seed = seed;
		this.startTurn = startTurn;
		this.playBackgroundMusic = playBackgroundMusic;
	}

	@Override
	public String getPlayerName() {
		return playerName;
	}

	@Override
	public String getPlayerProfession() {
		return playerProfession;
	}

	@Override
	public String getInitialStatusMessage() {
		return StatusMessages.WELCOME;
	}
	
	@Override
	public void addDefaultWorldObjects(World world, CommonerGenerator commonerGenerator, CreatureGenerator creatureGenerator, WorldObject organization, int villagerCount, int seed) {
		
		PlantGenerator.generateBerryBush(3, 3, world);
		
		for(int i=0; i<villagerCount; i++) {
			commonerGenerator.generateCommoner(1, 1, world, organization);
		}
		
		creatureGenerator.generateCow(7, 2, world);
		creatureGenerator.generateCow(8, 2, world);
		creatureGenerator.generateCow(9, 2, world);
		
		PlantGenerator.generateTreeTrunk(3, 5, world);
		PlantGenerator.generateTree(3, 8, world);

		WorldGenerator worldGenerator = new WorldGenerator(seed);
		worldGenerator.addWorldObjects(world, 2, 2, PlantGenerator::generateTree);
		worldGenerator.addWorldObjects(world, 2, 2, world.getWidth() / 10, TerrainType.HILL, TerrainGenerator::generateStoneResource);
		worldGenerator.addWorldObjects(world, 2, 2, world.getWidth() / 10, TerrainType.MOUNTAIN, TerrainGenerator::generateOreResource);
		worldGenerator.addWorldObjects(world, 2, 2, world.getWidth() / 20, TerrainType.MOUNTAIN, TerrainGenerator::generateGoldResource);
		worldGenerator.addWorldObjects(world, 1, 1, world.getWidth() / 50, TerrainType.GRASLAND, PlantGenerator::generateNightShade);
		worldGenerator.addWorldObjects(world, 1, 1, world.getWidth() / 50, TerrainType.PLAINS, TerrainGenerator::generateOilResource);
		
		worldGenerator.addWorldObjects(world, 1, 1, 20, TerrainType.PLAINS, PlantGenerator::generateBerryBush);
	}

	@Override
	public String getGender() {
		return gender;
	}

	@Override
	public int getWorldWidth() {
		return worldWidth;
	}

	@Override
	public int getWorldHeight() {
		return worldHeight;
	}

	@Override
	public int getEnemyDensity() {
		return enemyDensity;
	}

	@Override
	public int getVillagerCount() {
		return villagerCount;
	}

	@Override
	public int getSeed() {
		return seed;
	}
	
	@Override
	public int getStartTurn() {
		return startTurn;
	}

	@Override
	public boolean getPlayBackgroundMusic() {
		return playBackgroundMusic;
	}

	@Override
	public AdditionalManagedOperationListenerFactory getAdditionalManagedOperationListenerFactory() {
		return new NullAdditionalManagedOperationListenerFactory();
	}
}
