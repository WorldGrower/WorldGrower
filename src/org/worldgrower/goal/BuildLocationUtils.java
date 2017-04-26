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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.generator.BuildingDimensions;
import org.worldgrower.terrain.Terrain;
import org.worldgrower.terrain.TerrainResource;

public class BuildLocationUtils {

	public static WorldObject findOpenLocationNearExistingProperty(WorldObject performer, BuildingDimensions buildingDimensions, World world) {
		return findOpenLocationNearExistingProperty(performer, buildingDimensions.getPlacementWidth(), buildingDimensions.getPlacementHeight(), world, new DefaultZoneInitializer());
	}
	
	public static WorldObject findOpenLocationNearExistingProperty(WorldObject performer, BuildingDimensions buildingDimensions, World world, TerrainResource terrainResource) {
		TerrainResourceZoneInitializer zoneInitializer = new TerrainResourceZoneInitializer(terrainResource, world);
		return findOpenLocationNearExistingProperty(performer, buildingDimensions.getPlacementWidth(), buildingDimensions.getPlacementHeight(), world, zoneInitializer);
	}
	
	private static WorldObject findOpenLocationNearExistingProperty(WorldObject performer, int width, int height, World world, ZoneInitializer zoneInitializer) {
		List<WorldObject> housing = getHousing(performer, world);
		
		// add additional free space to left and top of open space
		width++;
		height++;
		
		WorldObject target = findOpenLocationNearExistingProperty(performer, width, height, world, housing, zoneInitializer);
		if (target != null) {
			target.increment(Constants.X, 1);
			target.increment(Constants.Y, 1);
			return target;
		} else {
			return target;
		}
	}
	
	private static List<WorldObject> getHousing(WorldObject performer, World world) {
		List<WorldObject> housingOwners = GroupPropertyUtils.findWorldObjectsInSameGroup(performer, world);
		List<WorldObject> housing = HousePropertyUtils.getHousingOfOwners(housingOwners, world);
		return housing;
	}

	public static WorldObject findOpenLocationAwayFromExistingProperty(WorldObject performer, BuildingDimensions buildingDimensions, World world) {
		return findOpenLocationAwayFromExistingProperty(performer, buildingDimensions.getPlacementWidth(),  buildingDimensions.getPlacementHeight(), world);
	}
	
	public static WorldObject findOpenLocationAwayFromExistingProperty(WorldObject performer, int width, int height, World world) {
		List<WorldObject> housing = getHousing(performer, world);
		
		Zone zone = new Zone(world.getWidth(), world.getHeight());
		zone.addValues(housing, 15, 1);
		
		int[] bestLocation = null;
		for(int x=0; x < world.getWidth(); x++) {
			for(int y=0; y<world.getHeight(); y++) {
				if ((zone.value(x, y) > 10) && zone.value(x, y) < 15) {
					if (GoalUtils.isOpenSpace(x, y, width, height, world)) {
						bestLocation = new int[]{ x, y };
					}
				}
			}
		}
		
		return createTargetWorldObject(performer, width, height, world, bestLocation);
	}

	private static WorldObject createTargetWorldObject(WorldObject performer, int width, int height, World world, int[] bestLocation) {
		if (bestLocation == null) {
			bestLocation = GoalUtils.findOpenSpace(performer, width, height, world);
			if (bestLocation == null) {
				return null;
			}
			int bestLocationX = performer.getProperty(Constants.X) + bestLocation[0];
			int bestLocationY = performer.getProperty(Constants.Y) + bestLocation[1];
			bestLocation = new int[] { bestLocationX, bestLocationY };
		}
		
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, bestLocation[0]);
		properties.put(Constants.Y, bestLocation[1]);
		properties.put(Constants.WIDTH, width);
		properties.put(Constants.HEIGHT, height);
		WorldObject target = new WorldObjectImpl(properties);
		return target;
	}
	
	public static WorldObject findOpenLocationNearPerformer(WorldObject performer, BuildingDimensions buildingDimensions, World world) {
		return findOpenLocationNearExistingProperty(performer, buildingDimensions.getPlacementWidth(), buildingDimensions.getPlacementHeight(), world, Arrays.asList(performer), new DefaultZoneInitializer());
	}
	
	static WorldObject findOpenLocationNearExistingProperty(WorldObject performer, int width, int height, World world, List<WorldObject> housing, ZoneInitializer zoneInitializer) {
		Zone zone = new Zone(world.getWidth(), world.getHeight(), zoneInitializer);
		zone.addValues(housing, 6, 1);
		
		int bestValue = 0;
		int[] bestLocation = null;
		for(int x=0; x < world.getWidth(); x++) {
			for(int y=0; y<world.getHeight(); y++) {
				if ((zone.value(x, y) > bestValue)) {
					if (GoalUtils.isOpenSpace(x, y, width, height, world)) {
						bestLocation = new int[]{ x, y };
						bestValue = zone.value(x, y);
					}
				}
			}
		}
		
		return createTargetWorldObject(performer, width, height, world, bestLocation);
	}
	
	static class DefaultZoneInitializer implements ZoneInitializer {

		@Override
		public void initialize(int[][] zone, int worldWidth, int worldHeight) {
		}
	}
	
	static class TerrainResourceZoneInitializer implements ZoneInitializer {

		private final TerrainResource terrainResource;
		private final Terrain terrain;
		
		public TerrainResourceZoneInitializer(TerrainResource terrainResource, World world) {
			this.terrainResource = terrainResource;
			this.terrain = world.getTerrain();
		}

		@Override
		public void initialize(int[][] zone, int worldWidth, int worldHeight) {
			for(int x=0; x<worldWidth; x++) {
				for(int y=0; y<worldHeight; y++) {
					zone[x][y] = calculateInitialValue(x, y);
				}
			}
			
		}

		private int calculateInitialValue(int x, int y) {
			return terrain.getTerrainInfo(x, y).getTerrainType().getBonus(terrainResource);
		}		
	}
}
