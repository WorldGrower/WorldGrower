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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.MockTerrain;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.BuildingList;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.generator.BuildingDimensions;
import org.worldgrower.terrain.TerrainResource;
import org.worldgrower.terrain.TerrainType;

public class UTestBuildLocationUtils {

	@Test
	public void testFindOpenLocationNearExistingProperty() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject house = TestUtils.createWorldObject(3, 3, 1, 1, Constants.ID, 2);
		world.addWorldObject(house);
		
		WorldObject location = BuildLocationUtils.findOpenLocationNearExistingProperty(null, 1, 1, world, Arrays.asList(house), new BuildLocationUtils.DefaultZoneInitializer());
		assertEquals(0, location.getProperty(Constants.X).intValue());
		assertEquals(0, location.getProperty(Constants.Y).intValue());
		assertEquals(1, location.getProperty(Constants.WIDTH).intValue());
		assertEquals(1, location.getProperty(Constants.HEIGHT).intValue());
	}
	
	@Test
	public void testFindOpenLocationNearExistingPropertyUsingPerformer() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.BUILDINGS, new BuildingList().add(3, BuildingType.HOUSE));
		performer.setProperty(Constants.X, 4);
		performer.setProperty(Constants.Y, 4);
		
		WorldObject house = TestUtils.createWorldObject(3, 3, 1, 1, Constants.ID, 3);
		world.addWorldObject(house);
		
		WorldObject location = BuildLocationUtils.findOpenLocationNearExistingProperty(performer, BuildingDimensions.TREE, world);
		assertEquals(5, location.getProperty(Constants.X).intValue());
		assertEquals(5, location.getProperty(Constants.Y).intValue());
	}
	
	@Test
	public void testFindOpenLocationNearExistingPropertyNoPropertyExists() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, "Test");
		performer.setProperty(Constants.X, 4);
		performer.setProperty(Constants.Y, 4);
		world.addWorldObject(performer);
		
		WorldObject location = BuildLocationUtils.findOpenLocationNearExistingProperty(performer, 1, 1, world, new ArrayList<>(), new BuildLocationUtils.DefaultZoneInitializer());
		assertEquals(4, location.getProperty(Constants.X).intValue());
		assertEquals(5, location.getProperty(Constants.Y).intValue());
		assertEquals(1, location.getProperty(Constants.WIDTH).intValue());
		assertEquals(1, location.getProperty(Constants.HEIGHT).intValue());
	}
	
	@Test
	public void testFindOpenLocationNearExistingPropertyNoPropertyExistsAndNoRoom() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, "Test");
		performer.setProperty(Constants.X, 4);
		performer.setProperty(Constants.Y, 4);
		world.addWorldObject(performer);
		
		WorldObject location = BuildLocationUtils.findOpenLocationNearExistingProperty(performer, 6, 6, world, new ArrayList<>(), new BuildLocationUtils.DefaultZoneInitializer());
		assertEquals(null, location);
	}
	
	@Test
	public void testFindOpenLocationAwayFromExistingProperty() {
		World world = new WorldImpl(15, 15, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.BUILDINGS, new BuildingList().add(3, BuildingType.HOUSE));
		performer.setProperty(Constants.X, 4);
		performer.setProperty(Constants.Y, 4);
		
		WorldObject house = TestUtils.createWorldObject(3, 3, 1, 1, Constants.ID, 3);
		world.addWorldObject(house);
		
		WorldObject location = BuildLocationUtils.findOpenLocationAwayFromExistingProperty(performer, 3, 3, world);
		assertEquals(4, location.getProperty(Constants.X).intValue());
		assertEquals(4, location.getProperty(Constants.Y).intValue());
	}
	
	@Test
	public void testFindOpenLocationNearExistingPropertyAndSpecificTerrain() {
		MockTerrain terrain = new MockTerrain(TerrainType.GRASLAND);
		terrain.setTerrainType(0, 0, TerrainType.MOUNTAIN);
		terrain.setTerrainType(0, 1, TerrainType.MOUNTAIN);
		terrain.setTerrainType(1, 0, TerrainType.MOUNTAIN);
		terrain.setTerrainType(1, 1, TerrainType.MOUNTAIN);
		terrain.setTerrainType(1, 4, TerrainType.MOUNTAIN);
		terrain.setTerrainType(2, 2, TerrainType.MOUNTAIN);
		terrain.setTerrainType(2, 3, TerrainType.MOUNTAIN);
		terrain.setTerrainType(3, 3, TerrainType.MOUNTAIN);
		
		World world = new WorldImpl(terrain, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, "Test");
		performer.setProperty(Constants.X, 2);
		performer.setProperty(Constants.Y, 2);
		world.addWorldObject(performer);
		
		WorldObject location = BuildLocationUtils.findOpenLocationNearExistingProperty(performer, 1, 1, world, Arrays.asList(performer), new BuildLocationUtils.TerrainResourceZoneInitializer(TerrainResource.FOOD, world));
		assertEquals(0, location.getProperty(Constants.X).intValue());
		assertEquals(2, location.getProperty(Constants.Y).intValue());
		
		WorldObject target = TestUtils.createIntelligentWorldObject(8, "Test");
		target.setProperty(Constants.X, 9);
		target.setProperty(Constants.Y, 9);
		location = BuildLocationUtils.findOpenLocationNearExistingProperty(target, 1, 1, world, Arrays.asList(target), new BuildLocationUtils.TerrainResourceZoneInitializer(TerrainResource.FOOD, world));
		assertEquals(3, location.getProperty(Constants.X).intValue());
		assertEquals(4, location.getProperty(Constants.Y).intValue());
	}
}
