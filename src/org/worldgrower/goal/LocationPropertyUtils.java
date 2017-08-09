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

import java.util.List;
import java.util.stream.Collectors;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;

/**
 * All updating of X, Y, WIDTH and HEIGHT should be done through this class.
 * That way the locations of all worldobjects can be cached.
 *
 */
public class LocationPropertyUtils {

	public static void updateLocation(WorldObject worldObject, int newX, int newY, World world) {
		world.getWorldObjectsCache(Constants.X, Constants.Y).update(worldObject, newX, newY);
		
		worldObject.setProperty(Constants.X, newX);
		worldObject.setProperty(Constants.Y, newY);
	}
	
	public static void updateLocation(WorldObject worldObject, int newX, int newY, int newWidth, int newHeight, World world) {
		world.getWorldObjectsCache(Constants.X, Constants.Y).update(worldObject, newX, newY, newWidth, newHeight);
		
		worldObject.setProperty(Constants.X, newX);
		worldObject.setProperty(Constants.Y, newY);
		worldObject.setProperty(Constants.WIDTH, newWidth);
		worldObject.setProperty(Constants.HEIGHT, newHeight);
	}
	
	public static void moveOffscreen(WorldObject worldObject, World world) {
		world.getWorldObjectsCache(Constants.X, Constants.Y).remove(worldObject);
		
		worldObject.setProperty(Constants.X, -10);
		worldObject.setProperty(Constants.Y, -10);
	}
	
	public static void moveOnscreen(WorldObject worldObject, int x, int y, World world) {
		worldObject.setProperty(Constants.X, x);
		worldObject.setProperty(Constants.Y, y);
		
		world.getWorldObjectsCache(Constants.X, Constants.Y).add(worldObject);
	}
	
	public static List<WorldObject> getWorldObjects(int x, int y, World world) {
		List<WorldObject> worldObjects = world.getWorldObjectsCache(Constants.X, Constants.Y).getWorldObjectsFor(x, y);
		worldObjects = worldObjects.stream().filter(w -> !isPassable(w)).collect(Collectors.toList());
		return worldObjects;
	}
	
	public static boolean isPassable(WorldObject w) {
		Boolean isPassable = w.getProperty(Constants.PASSABLE);
		return isPassable != null && isPassable.booleanValue();
	}
}
