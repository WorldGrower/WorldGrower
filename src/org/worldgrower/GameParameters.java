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

public interface GameParameters {

	public String getPlayerName();
	public String getPlayerProfession();
	public String getGender();
	public int getWorldWidth();
	public int getWorldHeight();
	public int getEnemyDensity();
	public int getVillagerCount();
	public int getSeed();
	public boolean getPlayBackgroundMusic();
	
	public String getInitialStatusMessage();
	public void addDefaultWorldObjects(World world, CommonerGenerator commonerGenerator, WorldObject organization, int villagerCount, int seed);
}
