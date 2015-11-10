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

import org.worldgrower.generator.CommonerGenerator;

public class TutorialGameParameters implements GameParameters {

	@Override
	public String getInitialStatusMessage() {
		return StatusMessages.WELCOME_TUTORIAL;
	}

	@Override
	public void addDefaultWorldObjects(World world, CommonerGenerator commonerGenerator, WorldObject organization, int villagerCount, int seed) {

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
		return 10;
	}

	@Override
	public int getWorldHeight() {
		return 10;
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
}
