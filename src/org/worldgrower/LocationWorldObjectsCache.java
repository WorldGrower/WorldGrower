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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.worldgrower.goal.LocationPropertyUtils.isPassable;

class LocationWorldObjectsCache implements WorldObjectsCache, Serializable {

	private final WorldObjectsList[][] cache;
	private final int[][] zone;
	
	public LocationWorldObjectsCache(int width, int height) {
		cache = new WorldObjectsList[width][height];
		
		for(int i=0; i<width; i++) {
			for(int j=0; j<height; j++) {
				cache[i][j] = new WorldObjectsList();
			}
		}
		
		this.zone = new int[width][height];
	}
	
	@Override
	public void add(WorldObject worldObject) {
		if (isPhysicalObject(worldObject)) {
			int x = worldObject.getProperty(Constants.X);
			int y = worldObject.getProperty(Constants.Y);
			int width = worldObject.getProperty(Constants.WIDTH);
			int height = worldObject.getProperty(Constants.HEIGHT);
			for(int i=x; i<x+width; i++) {
				for(int j=y; j<y+height; j++) {
					cache[i][j].add(worldObject);
					if (!isPassable(worldObject)) {
						zone[i][j]++;
					}
				}
			}
		}
	}

	private boolean isPhysicalObject(WorldObject worldObject) {
		return worldObject.hasProperty(Constants.X) && worldObject.getProperty(Constants.X).intValue() >= 0;
	}
	
	@Override
	public void remove(WorldObject worldObject) {
		if (isPhysicalObject(worldObject)) {
			int x = worldObject.getProperty(Constants.X);
			int y = worldObject.getProperty(Constants.Y);
			int width = worldObject.getProperty(Constants.WIDTH);
			int height = worldObject.getProperty(Constants.HEIGHT);
			for(int i=x; i<x+width; i++) {
				for(int j=y; j<y+height; j++) {
					cache[i][j].remove(worldObject);
					if (!isPassable(worldObject)) {
						zone[i][j]--;
					}
				}
			}
		}
	}

	@Override
	public List<WorldObject> getWorldObjectsFor(int x, int y) {
		return cache[x][y].getWorldObjects();
	}
	
	@Override
	public void update(WorldObject worldObject, int newX, int newY) {
		remove(worldObject);

		int width = worldObject.getProperty(Constants.WIDTH);
		int height = worldObject.getProperty(Constants.HEIGHT);
		for(int i=newX; i<newX+width; i++) {
			for(int j=newY; j<newY+height; j++) {
				cache[i][j].add(worldObject);
				if (!isPassable(worldObject)) {
					zone[i][j]++;
				}
			}
		}
	}

	@Override
	public void update(WorldObject worldObject, int newX, int newY, int newWidth, int newHeight) {
		remove(worldObject);

		for(int i=newX; i<newX+newWidth; i++) {
			for(int j=newY; j<newY+newHeight; j++) {
				cache[i][j].add(worldObject);
				if (!isPassable(worldObject)) {
					zone[i][j]++;
				}
			}
		}
	}
	
	public int value(int x, int y) {
		return zone[x][y];
	}

	private static class WorldObjectsList implements Serializable {
		private final List<WorldObject> worldObjects = new ArrayList<>();
		
		public void add(WorldObject worldObject) {
			worldObjects.add(worldObject);
		}
		
		public void remove(WorldObject worldObject) {
			worldObjects.remove(worldObject);
		}
		
		public List<WorldObject> getWorldObjects() {
			return Collections.unmodifiableList(worldObjects);
		}
	}
}
