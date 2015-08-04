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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.GoalUtils;
import org.worldgrower.terrain.TerrainType;

/**
 * This class is used for populating the world instance with worldobjects.
 */
public class WorldGenerator {

	private final Random random;
	
	public WorldGenerator(int seed) {
		this.random = new Random(seed);
	}

	public void addWorldObjects(World world, int width, int height, AddWorldObjectFunction addWorldObjectFunction) {
		int numberOfStartingPlaces = random.nextInt(world.getWidth()) + 4;
		List<Integer> ids = new ArrayList<>();
		for(int i=0; i<numberOfStartingPlaces; i++) {
			int x = random.nextInt(world.getWidth());
			int y = random.nextInt(world.getHeight());
			
			if (GoalUtils.isOpenSpace(x, y, width, height, world)) {
				ids.add(addWorldObjectFunction.addToWorld(x, y, world));
			}
		}
		
		int numberOfAdditionalWorldObjects = random.nextInt(5 * (world.getWidth() + world.getHeight())) + 20;
		for(int i=0; i<numberOfAdditionalWorldObjects; i++) {
			int id = ids.get(random.nextInt(ids.size()));
			WorldObject worldObject = world.findWorldObjects(w -> w.getProperty(Constants.ID) == id).get(0);
			int x = worldObject.getProperty(Constants.X) + (random.nextBoolean() ? width : 0);
			int y = worldObject.getProperty(Constants.Y) + (random.nextBoolean() ? height : 0);
			
			if (GoalUtils.isOpenSpace(x, y, width, height, world) && (x < world.getWidth() - width) && (y < world.getHeight() - height)) {
				ids.add(addWorldObjectFunction.addToWorld(x, y, world));
			}
		}
	}
	
	public void addWorldObjects(World world, int width, int height, int numberOfWorldObjects, TerrainType preferredTerrainType, AddWorldObjectFunction addWorldObjectFunction) {
		
		for(int i=0; i<numberOfWorldObjects; i++) {
			boolean found = false;
			
			while (!found) {
				int x = random.nextInt(world.getWidth());
				int y = random.nextInt(world.getHeight());
				
				TerrainType terrainType = world.getTerrain().getTerrainInfo(x, y).getTerrainType();
				
				if (terrainType == preferredTerrainType) {
					if (GoalUtils.isOpenSpace(x, y, width, height, world)) {
						addWorldObjectFunction.addToWorld(x, y, world);
						found = true;
					}
				}
			}
		}
	}

	public static interface AddWorldObjectFunction extends Serializable {
		public int addToWorld(int x, int y, World world);
	}
}
