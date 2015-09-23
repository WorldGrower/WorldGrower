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
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.goal.BuildLocationUtils;

public class UTestBuildLocationUtils {

	@Test
	public void testFindOpenLocationNearExistingProperty() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject house = TestUtils.createWorldObject(3, 3, 1, 1);
		world.addWorldObject(house);
		
		WorldObject location = BuildLocationUtils.findOpenLocationNearExistingProperty(null, 1, 1, world, Arrays.asList(house));
		assertEquals(0, location.getProperty(Constants.X).intValue());
		assertEquals(0, location.getProperty(Constants.Y).intValue());
		assertEquals(1, location.getProperty(Constants.WIDTH).intValue());
		assertEquals(1, location.getProperty(Constants.HEIGHT).intValue());
	}
	
	@Test
	public void testFindOpenLocationNearExistingPropertyUsingPerformer() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.HOUSES, new IdList().add(3));
		performer.setProperty(Constants.X, 4);
		performer.setProperty(Constants.Y, 4);
		
		WorldObject house = TestUtils.createWorldObject(3, 3, 1, 1, Constants.ID, 3);
		world.addWorldObject(house);
		
		WorldObject location = BuildLocationUtils.findOpenLocationNearExistingProperty(performer, 3, 3, world);
		assertEquals(1, location.getProperty(Constants.X).intValue());
		assertEquals(5, location.getProperty(Constants.Y).intValue());
	}
	
	@Test
	public void testFindOpenLocationAwayFromExistingProperty() {
		World world = new WorldImpl(15, 15, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.HOUSES, new IdList().add(3));
		performer.setProperty(Constants.X, 4);
		performer.setProperty(Constants.Y, 4);
		
		WorldObject house = TestUtils.createWorldObject(3, 3, 1, 1, Constants.ID, 3);
		world.addWorldObject(house);
		
		WorldObject location = BuildLocationUtils.findOpenLocationAwayFromExistingProperty(performer, 3, 3, world);
		assertEquals(1, location.getProperty(Constants.X).intValue());
		assertEquals(4, location.getProperty(Constants.Y).intValue());
	}
}
