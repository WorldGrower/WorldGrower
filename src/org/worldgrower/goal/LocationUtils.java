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
package org.worldgrower.goal;

import java.util.ArrayList;
import java.util.List;

import org.worldgrower.CreaturePositionCondition;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.terrain.TerrainType;

public class LocationUtils {

	public static boolean areInvalidCoordinates(int newX, int newY, World world) {
		return (newX < 0) || (newY < 0) || (newX >= world.getWidth()) || (newY >= world.getHeight());
	}

	public static List<WorldObject> findWorldObjectsInSurroundingWater(int targetX, int targetY, World world) {
		List<Location> surroundingWaterTiles = new ArrayList<>();
		List<Location> tilesToExamine = new ArrayList<>();
		tilesToExamine.add(new Location(targetX, targetY));
		
		while (tilesToExamine.size() > 0) {
			for(Location tileToExamine : new ArrayList<>(tilesToExamine)) {
				int tileX = tileToExamine.getX();
				int tileY = tileToExamine.getY();
				for(Location tile : getSurroundingWaterTiles(tileX, tileY, world)) {
					if (!surroundingWaterTiles.contains(tile)) {
						surroundingWaterTiles.add(tile);
						tilesToExamine.add(tile);
					}
				}
				tilesToExamine.remove(tileToExamine);
			}
		}
		return world.findWorldObjects(w -> isOnTile(w, surroundingWaterTiles));
	}
	
	private static List<Location> getSurroundingWaterTiles(int tileX, int tileY, World world) {
		List<Location> surroundingTiles = new ArrayList<>();
		
		for(int x=tileX-1; x<=tileX+1; x++) {
			for(int y=tileY-1; y<=tileY+1; y++) {
				if (!areInvalidCoordinates(x, y, world)) {
					if (world.getTerrain().getTerrainInfo(x, y).getTerrainType() == TerrainType.WATER) {
						Location tile = new Location(x, y);
						surroundingTiles.add(tile);
					}
				}
			}
		}
		
		return surroundingTiles;
	}
	
	private static boolean isOnTile(WorldObject worldObject, List<Location> surroundingWaterTiles) {
		for(Location surroundingWaterTile : surroundingWaterTiles) {
			int x = surroundingWaterTile.getX();
			int y = surroundingWaterTile.getY();
			boolean isOnTile = new CreaturePositionCondition(y, x).isWorldObjectValid(worldObject);
			if (isOnTile) {
				return true;
			}
		}
		
		return false;
	}

	private static class Location {
		private final int x;
		private final int y;
		
		public Location(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Location other = (Location) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}
		
		
	}
}
