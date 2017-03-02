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
import org.worldgrower.gui.ImageIds;
import org.worldgrower.terrain.TerrainMapper;
import org.worldgrower.terrain.TerrainType;

public class PlayerCharacterInfo {

	private final String playerName;
	private final String playerProfession;
	private final String gender;
	private final ImageIds imageId;
		
	public PlayerCharacterInfo(String playerName, String playerProfession, String gender, ImageIds imageId) {
		this.playerName = playerName;
		this.playerProfession = playerProfession;
		this.gender = gender;
		this.imageId = imageId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public String getPlayerProfession() {
		return playerProfession;
	}

	public String getGender() {
		return gender;
	}

	public ImageIds getImageId() {
		return imageId;
	}
}
