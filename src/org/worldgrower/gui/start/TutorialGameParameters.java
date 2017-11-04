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

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.TreeWoodSource;
import org.worldgrower.attribute.Gender;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.generator.CreatureGenerator;
import org.worldgrower.generator.Item;
import org.worldgrower.generator.PlantGenerator;
import org.worldgrower.gui.AdditionalManagedOperationListenerFactory;
import org.worldgrower.terrain.TerrainMapper;

public class TutorialGameParameters implements GameParameters {

	@Override
	public String getInitialStatusMessage() {
		return StatusMessages.WELCOME_TUTORIAL;
	}

	@Override
	public void addDefaultWorldObjects(World world, CommonerGenerator commonerGenerator, CreatureGenerator creatureGenerator, WorldObject organization, int villagerCount, int seed) {

		BuildingGenerator.generateSignPost(5, 4, world, "signpost");
		
		createTree(9, 3, world);
		createTree(9, 5, world);
		
		int berryBushId = PlantGenerator.generateBerryBush(8, 11, world);
		WorldObject berryBush = world.findWorldObjectById(berryBushId);
		berryBush.getProperty(Constants.FOOD_SOURCE).increaseFoodAmount(100, berryBush, world);
		
		int commonerId = commonerGenerator.generateCommoner(0, 10, world, organization, CommonerGenerator.NO_PARENT);
		WorldObject commoner = world.findWorldObjectById(commonerId);
		Conditions.addPermanent(commoner, Condition.PARALYZED_CONDITION, world);
	}

	private void createTree(int x, int y, World world) {
		int treeId = PlantGenerator.generateTree(x, y, world);
		WorldObject tree = world.findWorldObjectById(treeId);
		tree.setProperty(Constants.WOOD_SOURCE, new TreeWoodSource(1000));
	}

	@Override
	public String getPlayerName() {
		return "player";
	}

	@Override
	public String getPlayerProfession() {
		return "adventurer";
	}

	@Override
	public Gender getGender() {
		return Gender.MALE;
	}

	@Override
	public int getWorldWidth() {
		return 12;
	}

	@Override
	public int getWorldHeight() {
		return 12;
	}

	@Override
	public int getEnemyDensity() {
		return 0;
	}

	@Override
	public int getVillagerCount() {
		return 0;
	}

	@Override
	public int getSeed() {
		return 666;
	}

	@Override
	public AdditionalManagedOperationListenerFactory getAdditionalManagedOperationListenerFactory() {
		return new TutorialAdditionalManagedOperationListenerFactory();
	}

	@Override
	public int getStartTurn() {
		return 0;
	}

	@Override
	public void initializePlayerCharacter(WorldObject playerCharacter) {
		WorldObjectContainer inventory = playerCharacter.getProperty(Constants.INVENTORY);

		inventory.addQuantity(Item.IRON_CLAYMORE.generate(1f));
		inventory.addQuantity(Item.IRON_GREATSWORD.generate(1f));
		inventory.addQuantity(Item.IRON_CUIRASS.generate(1f));
		inventory.addQuantity(Item.LONGBOW.generate(1f));
	}

	@Override
	public double getWaterCutoff() {
		return TerrainMapper.NORMAL_WATER_CUTOFF;
	}
}
