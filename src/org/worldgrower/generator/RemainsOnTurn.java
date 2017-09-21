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
package org.worldgrower.generator;

import org.worldgrower.Constants;
import org.worldgrower.OnTurn;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.DeathInformation;
import org.worldgrower.attribute.GhostImageIds;
import org.worldgrower.condition.WorldStateChangedListeners;
import org.worldgrower.gui.ImageIds;

public class RemainsOnTurn implements OnTurn {

	private static final int GHOST_SPAWN_TIME = 100;

	@Override
	public void onTurn(WorldObject worldObject, World world, WorldStateChangedListeners worldStateChangedListeners) {
		DeathInformation deathInformation = worldObject.getProperty(Constants.DEATH_INFORMATION);
		int deathTurn = deathInformation.getDeathTurn();
		boolean ghostCanSpawn = deathTurn + GHOST_SPAWN_TIME > world.getCurrentTurn().getValue();
		if (ghostCanSpawn && !deathInformation.isGhostSpawned()) {
			int x = worldObject.getProperty(Constants.X);
			int y = worldObject.getProperty(Constants.Y);
			ImageIds originalImageId = deathInformation.getOriginalImageId();
			String originalName = deathInformation.getOriginalName();
			GhostImageIds ghostImageIds = world.getUserData(GhostImageIds.class);
			CreatureGenerator.generateGhost(x, y, world, ghostImageIds, originalImageId, originalName);

			deathInformation.setGhostSpawned(true);
		}
	}
}
