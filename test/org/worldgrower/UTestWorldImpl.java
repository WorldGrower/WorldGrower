/*******************************************************************************
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*******************************************************************************/
package org.worldgrower;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class UTestWorldImpl {

	private World world;
	
	public UTestWorldImpl() {
		world = new WorldImpl(0, 0, null);
		WorldObject worldObject = TestUtils.createWorldObject(6, "test");
		world.addWorldObject(worldObject);
	}
	
	@Test
	public void testFindWorldObjects() {
		List<WorldObject> result = world.findWorldObjects(w -> w.getProperty(Constants.ID).intValue() == 6);
		
		assertEquals(1, result.size());
		assertEquals(6, result.get(0).getProperty(Constants.ID).intValue());
	}
	
	@Test
	public void testFindWorldObject() {
		WorldObject result = world.findWorldObject(Constants.NAME, "test");
		assertEquals(6, result.getProperty(Constants.ID).intValue());
	}
	
	@Test
	public void testRemoveWorldObject() {
		World world = new WorldImpl(0, 0, null);
		WorldObject worldObject = TestUtils.createWorldObject(6, "test");
		world.addWorldObject(worldObject);
		
		assertEquals(1, world.findWorldObjects(w -> w.getProperty(Constants.ID).intValue() == 6).size());
		
		world.removeWorldObject(worldObject);
		assertEquals(0, world.findWorldObjects(w -> w.getProperty(Constants.ID).intValue() == 6).size());
	}
	
	@Test
	public void testRemoveDependentWorldObject() {
		World world = new WorldImpl(0, 0, null);
		WorldObject house = TestUtils.createWorldObject(6, "test");
		WorldObject person = TestUtils.createIntelligentWorldObject(7, Constants.HOUSE_ID, 6);
		
		world.addWorldObject(house);
		world.addWorldObject(person);
		
		assertEquals(6, person.getProperty(Constants.HOUSE_ID).intValue());
		
		world.removeWorldObject(house);
		assertEquals(null, person.getProperty(Constants.HOUSE_ID));
	}
}
