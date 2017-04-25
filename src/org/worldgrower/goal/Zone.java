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

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;

/**
 * A zone is an overlay of the world, with each square in the world assigned a value.
 * This makes some squares more desirable than others.
 */
public class Zone {

	private final int[][] zone;
	private final int worldWidth;
	private final int worldHeight;
	
	public Zone(int worldWidth, int worldHeight) {
		this.zone = new int[worldWidth][worldHeight];
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
	}
	
	public Zone(int worldWidth, int worldHeight, ZoneInitializer zoneInitializer) {
		this(worldWidth, worldHeight);
		zoneInitializer.initialize(this.zone, worldWidth, worldHeight);
	}
	
	public void addValues(List<WorldObject> worldObjects, int zoneLimit, int increment) {
		for(WorldObject target : worldObjects) {
			int targetX = target.getProperty(Constants.X);
			int targetY = target.getProperty(Constants.Y);

			for(int x=Math.max(0, targetX - zoneLimit); x<=Math.min(worldWidth-1, targetX + zoneLimit); x++) {
				for(int y=Math.max(0, targetY - zoneLimit); y<=Math.min(worldHeight-1, targetY + zoneLimit); y++) {
					zone[x][y] += increment;
				}
			}
		}
	}
	
	public int value(int x, int y) {
		return zone[x][y];
	}
	
	public List<Integer> getValuesX(int startX) {
		List<Integer> result = new ArrayList<Integer>();
		for(int x=Math.max(0, startX-1); x<=Math.min(worldWidth-1, startX+1); x++) {
			result.add(x);
		}
		return result;
	}
	
	public List<Integer> getValuesY(int startY) {
		List<Integer> result = new ArrayList<Integer>();
		for(int y=Math.max(0, startY-1); y<=Math.min(worldHeight-1, startY+1); y++) {
			result.add(y);
		}
		return result;
	}
}
