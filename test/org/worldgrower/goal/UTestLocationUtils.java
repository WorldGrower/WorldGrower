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
import org.worldgrower.MockTerrain;
import org.worldgrower.MockWorld;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.terrain.TerrainType;

public class UTestLocationUtils {

	@Test
	public void testFindWorldObjectsInSurroundingWater() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject house = TestUtils.createWorldObject(3, 3, 1, 1, Constants.ID, 7);
		world.addWorldObject(house);
		
		List<WorldObject> worldObjects = LocationUtils.findWorldObjectsInSurroundingWater(1, 1, new MockWorld(new MockTerrain(TerrainType.WATER), world));
		assertEquals(1, worldObjects.size());
		assertEquals(house, worldObjects.get(0));
	}
	
	@Test
	public void testFindIsolatedPersonNull() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerson(2);
		
		assertEquals(null, LocationUtils.findIsolatedPerson(performer, world));
	}
	
	@Test
	public void testFindIsolatedPersonPossibleTarget() {
		World world = new WorldImpl(11, 11, null, null);
		WorldObject performer = createPerson(2);
		WorldObject target = createPerson(3);
		
		target.setProperty(Constants.X, 10);
		target.setProperty(Constants.Y, 10);
		
		world.addWorldObject(performer);
		world.addWorldObject(target);
		
		assertEquals(target, LocationUtils.findIsolatedPerson(performer, world));
	}
	
	@Test
	public void testIsPersonIsolated() {
		World world = new WorldImpl(11, 11, null, null);
		WorldObject performer = createPerson(2);
		WorldObject target = createPerson(3);
		
		target.setProperty(Constants.X, 10);
		target.setProperty(Constants.Y, 10);
		world.addWorldObject(performer);
		world.addWorldObject(target);
		
		assertEquals(true, LocationUtils.isPersonIsolated(performer, target, world));
		
		WorldObject subject = createPerson(4);
		subject.setProperty(Constants.X, 9);
		subject.setProperty(Constants.Y, 9);
		world.addWorldObject(subject);
		assertEquals(false, LocationUtils.isPersonIsolated(performer, target, world));
	}
	
	private WorldObject createPerson(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}
