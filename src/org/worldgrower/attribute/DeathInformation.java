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
package org.worldgrower.attribute;

import java.io.Serializable;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.gui.ImageIds;

public final class DeathInformation implements Serializable {
	
	private static final int GHOST_SPAWN_TIME = 100;
	
	private final int deathTurn;
	private final ImageIds originalImageId;
	private final String originalName;
	
	private boolean ghostSpawned = false;
	
	public DeathInformation(WorldObject originalWorldObject, World world) {
		this.deathTurn = world.getCurrentTurn().getValue();
		this.originalImageId = originalWorldObject.getProperty(Constants.IMAGE_ID);
		this.originalName = originalWorldObject.getProperty(Constants.NAME);
	}
	
	public boolean shouldSpawnGhost(World world) {
		boolean ghostCanSpawn = world.getCurrentTurn().getValue() > deathTurn + GHOST_SPAWN_TIME;
		return (ghostCanSpawn && !ghostSpawned);
	}

	public ImageIds getOriginalImageId() {
		return originalImageId;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setGhostSpawned(boolean ghostSpawned) {
		this.ghostSpawned = ghostSpawned;
	}
}
