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

import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.MockWorld;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.terrain.Terrain;
import org.worldgrower.terrain.TerrainInfo;
import org.worldgrower.terrain.TerrainType;

public class UTestLocationUtils {

	@Test
	public void testFindWorldObjectsInSurroundingWater() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject house = TestUtils.createWorldObject(3, 3, 1, 1, Constants.ID, 7);
		world.addWorldObject(house);
		
		List<WorldObject> worldObjects = LocationUtils.findWorldObjectsInSurroundingWater(1, 1, new MockWorld(new MockTerrain(), world));
		assertEquals(1, worldObjects.size());
		assertEquals(house, worldObjects.get(0));
	}
	
	private static class MockTerrain implements Terrain {

		@Override
		public TerrainInfo getTerrainInfo(int x, int y) {
			return new TerrainInfo(TerrainType.WATER);
		}

		@Override
		public int getWidth() {
			return 10;
		}

		@Override
		public int getHeight() {
			return 10;
		}

		@Override
		public boolean isExplored(int x, int y) {
			return false;
		}

		@Override
		public void explore(int x, int y, int radius) {
		}
	}
}
