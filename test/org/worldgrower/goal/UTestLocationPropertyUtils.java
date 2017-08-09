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

import java.util.Arrays;

import org.junit.Test;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.TerrainGenerator;

public class UTestLocationPropertyUtils {

	@Test
	public void testIspassable() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, "test");
		int libraryId = BuildingGenerator.generateLibrary(0, 0, world, performer);
		WorldObject library = world.findWorldObjectById(libraryId);
		int fireTrapId = TerrainGenerator.generateFireTrap(0, 0, world, 1f);
		WorldObject fireTrap = world.findWorldObjectById(fireTrapId);
		
		assertEquals(false, LocationPropertyUtils.isPassable(performer));
		assertEquals(false, LocationPropertyUtils.isPassable(library));
		assertEquals(true, LocationPropertyUtils.isPassable(fireTrap));
	}
	
	@Test
	public void testGetWorldObjects() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, "test");
		world.addWorldObject(performer);
		int libraryId = BuildingGenerator.generateLibrary(0, 0, world, performer);
		WorldObject library = world.findWorldObjectById(libraryId);
		int fireTrapId = TerrainGenerator.generateFireTrap(0, 0, world, 1f);
		WorldObject fireTrap = world.findWorldObjectById(fireTrapId);
		
		assertEquals(Arrays.asList(performer, library), LocationPropertyUtils.getWorldObjects(0, 0, world));
		assertEquals(Arrays.asList(library), LocationPropertyUtils.getWorldObjects(0, 1, world));
		assertEquals(Arrays.asList(library), LocationPropertyUtils.getWorldObjects(1, 0, world));
		assertEquals(Arrays.asList(), LocationPropertyUtils.getWorldObjects(5, 5, world));
	}
}
