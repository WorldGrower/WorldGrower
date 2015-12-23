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
package org.worldgrower.terrain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UTestTerrainImpl {

	@Test
	public void testExplored() {
		Terrain terrain = new TerrainImpl(10, 10);
		assertEquals(false, terrain.isExplored(0, 0));
		assertEquals(false, terrain.isExplored(-1, -1));
	}
	
	@Test
	public void testExplore() {
		Terrain terrain = new TerrainImpl(10, 10);
		assertEquals(false, terrain.isExplored(0, 0));
		
		terrain.explore(1, 1, 2);
		assertEquals(true, terrain.isExplored(0, 0));
		assertEquals(true, terrain.isExplored(1, 1));
		assertEquals(true, terrain.isExplored(2, 2));
		assertEquals(false, terrain.isExplored(3, 3));
	}
}