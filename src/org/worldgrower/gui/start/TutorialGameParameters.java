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
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.CommonerGenerator;
import org.worldgrower.generator.CreatureGenerator;
import org.worldgrower.generator.PlantGenerator;
import org.worldgrower.gui.AdditionalManagedOperationListenerFactory;

public class TutorialGameParameters implements GameParameters {

	@Override
	public String getInitialStatusMessage() {
		return StatusMessages.WELCOME_TUTORIAL;
	}

	@Override
	public void addDefaultWorldObjects(World world, CommonerGenerator commonerGenerator, CreatureGenerator creatureGenerator, WorldObject organization, int villagerCount, int seed) {

		BuildingGenerator.generateSignPost(5, 4, world, "Well done. Now use the right arrow key to move your player character to the right, next to the tree.\n Then right-click on the tree to cut wood from it.");
		
		PlantGenerator.generateTree(9, 5, world);
		
		int berryBushId = PlantGenerator.generateBerryBush(8, 11, world);
		WorldObject berryBush = world.findWorldObject(Constants.ID, berryBushId);
		berryBush.setProperty(Constants.FOOD_SOURCE, 100);
		
		int commonerId = commonerGenerator.generateCommoner(0, 10, world, organization);
		WorldObject commoner = world.findWorldObject(Constants.ID, commonerId);
		Conditions.add(commoner, Condition.PARALYZED_CONDITION, Integer.MAX_VALUE, world);
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
	public String getGender() {
		return "male";
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
	public boolean getPlayBackgroundMusic() {
		return true;
	}

	@Override
	public AdditionalManagedOperationListenerFactory getAdditionalManagedOperationListenerFactory() {
		return new TutorialAdditionalManagedOperationListenerFactory();
	}

	@Override
	public int getStartTurn() {
		return 0;
	}
}
