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

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.MockWorld;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.terrain.Terrain;

public class UTestDrownUtils {

	@Test
	public void testCheckForDrowning() {
		Terrain terrain = new MockTerrain();
		World world = new MockWorld(terrain, new WorldImpl(1, 1, null, null));
		WorldObject performer = TestUtils.createWorldObject(0, 0, 1, 1, Constants.ID, 0);
		performer.setProperty(Constants.HIT_POINTS, 10);
		
		DrownUtils.checkForDrowning(performer, world);
		assertEquals(5, performer.getProperty(Constants.HIT_POINTS).intValue());
	}
}
