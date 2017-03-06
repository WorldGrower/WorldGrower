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
import org.worldgrower.goal.GoalUtils;
import org.worldgrower.goal.Position;
import org.worldgrower.gui.AdditionalManagedOperationListenerFactory;
import org.worldgrower.terrain.TerrainMapper;
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
	private final float stoneResourceMultiplier;
	private final float oreResourceMultiplier;
	private final float goldResourceMultiplier;
	private final float oilResourceMultiplier;
	private final double waterCutoff;
	
	public CustomGameParameters() {
		this.playerName = getDefaultUsername();
		this.playerProfession = "adventurer";
		this.gender = "male";
		this.worldWidth = 100;
		this.worldHeight = 100;
		this.enemyDensity = 0;
		this.villagerCount = 6;
		this.seed = 666;
		this.startTurn = 0;
		this.stoneResourceMultiplier = 1.0f;
		this.oreResourceMultiplier = 1.0f;
		this.goldResourceMultiplier = 1.0f;
		this.oilResourceMultiplier = 1.0f;
		this.waterCutoff = TerrainMapper.NORMAL_WATER_CUTOFF;
	}
	
	private String getDefaultUsername() {
		String userName = System.getProperty("user.name");
		if (userName != null && userName.length() > 0) {
			return userName;
		} else {
			return "MyName";
		}
	}
	
	public CustomGameParameters(String playerName, String playerProfession, String gender, int worldWidth, int worldHeight, int enemyDensity, int villagerCount, int seed, int startTurn, float stoneResourceMultiplier, float oreResourceMultiplier, float goldResourceMultiplier, float oilResourceMultiplier, double waterCutoff) {
		this.playerName = playerName;
		this.playerProfession = playerProfession;
		this.gender = gender;
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		this.enemyDensity = enemyDensity;
		this.villagerCount = villagerCount;
		this.seed = seed;
		this.startTurn = startTurn;
		this.stoneResourceMultiplier = stoneResourceMultiplier;
		this.oreResourceMultiplier = oreResourceMultiplier;
		this.goldResourceMultiplier = goldResourceMultiplier;
		this.oilResourceMultiplier = oilResourceMultiplier;
		this.waterCutoff = waterCutoff;
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
		
		Position commonerPosition = GoalUtils.findOpenNonWaterSpace(1, 1, 1, 1, world);
		for(int i=0; i<villagerCount; i++) {
			commonerGenerator.generateCommoner(commonerPosition.getX(), commonerPosition.getY(), world, organization, CommonerGenerator.NO_PARENT);
		}
		
		creatureGenerator.generateCow(7, 2, world);
		creatureGenerator.generateCow(8, 2, world);
		creatureGenerator.generateCow(9, 2, world);
		
		PlantGenerator.generateTreeTrunk(3, 5, world);
		PlantGenerator.generateTree(3, 8, world, 1f);

		WorldGenerator worldGenerator = new WorldGenerator(seed);
		worldGenerator.addWorldObjects(world, 2, 2, PlantGenerator::generateTree);
		
		int stoneResourceCount = (int) ((world.getWidth() / 10) * stoneResourceMultiplier);
		worldGenerator.addWorldObjects(world, 2, 2, stoneResourceCount, TerrainType.HILL, TerrainGenerator::generateStoneResource);
		
		int oreResourceCount = (int)((world.getWidth() / 10) * oreResourceMultiplier);
		worldGenerator.addWorldObjects(world, 2, 2, oreResourceCount, TerrainType.MOUNTAIN, TerrainGenerator::generateOreResource);
		
		int goldResourceCount = (int)((world.getWidth() / 20) * goldResourceMultiplier);
		worldGenerator.addWorldObjects(world, 2, 2, goldResourceCount, TerrainType.MOUNTAIN, TerrainGenerator::generateGoldResource);

		worldGenerator.addWorldObjects(world, 1, 1, world.getWidth() / 50, TerrainType.GRASLAND, PlantGenerator::generateNightShade);
		
		int oilResourceCount = (int)((world.getWidth() / 50) * oilResourceMultiplier);
		worldGenerator.addWorldObjects(world, 1, 1, oilResourceCount, TerrainType.PLAINS, TerrainGenerator::generateOilResource);
		
		int berryBushCount = world.getWidth() / 5;
		worldGenerator.addWorldObjects(world, 1, 1, berryBushCount, TerrainType.PLAINS, PlantGenerator::generateBerryBush);
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
	public AdditionalManagedOperationListenerFactory getAdditionalManagedOperationListenerFactory() {
		return new NullAdditionalManagedOperationListenerFactory();
	}

	@Override
	public void initializePlayerCharacter(WorldObject playerCharacter) {
	}

	@Override
	public double getWaterCutoff() {
		return waterCutoff;
	}
}
