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
package org.worldgrower;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UTestLocationWorldObjectsCache {

	@Test
	public void testAddGetMediumWorldObject() {
		LocationWorldObjectsCache cache = new LocationWorldObjectsCache(10, 10);
		WorldObject worldObject = TestUtils.createWorldObject(2, 2, 1, 1, Constants.ID, 2);
		cache.add(worldObject);
		
		assertEquals(1, cache.getWorldObjectsFor(2, 2).size());
		assertEquals(0, cache.getWorldObjectsFor(1, 1).size());
		assertEquals(0, cache.getWorldObjectsFor(3, 3).size());
	}
	
	@Test
	public void testAddGetLargeWorldObject() {
		LocationWorldObjectsCache cache = new LocationWorldObjectsCache(10, 10);
		WorldObject worldObject = TestUtils.createWorldObject(2, 2, 2, 2, Constants.ID, 2);
		cache.add(worldObject);
		
		assertEquals(1, cache.getWorldObjectsFor(2, 2).size());
		assertEquals(0, cache.getWorldObjectsFor(1, 1).size());
		assertEquals(1, cache.getWorldObjectsFor(3, 3).size());
		assertEquals(0, cache.getWorldObjectsFor(4, 4).size());
	}
	
	@Test
	public void testRemoveMediumWorldObject() {
		LocationWorldObjectsCache cache = new LocationWorldObjectsCache(10, 10);
		WorldObject worldObject = TestUtils.createWorldObject(2, 2, 1, 1, Constants.ID, 2);
		cache.add(worldObject);
		cache.remove(worldObject);
		
		assertEquals(0, cache.getWorldObjectsFor(2, 2).size());
		assertEquals(0, cache.getWorldObjectsFor(1, 1).size());
		assertEquals(0, cache.getWorldObjectsFor(3, 3).size());
	}
	
	@Test
	public void testRemoveLargeWorldObject() {
		LocationWorldObjectsCache cache = new LocationWorldObjectsCache(10, 10);
		WorldObject worldObject = TestUtils.createWorldObject(2, 2, 2, 2, Constants.ID, 2);
		cache.add(worldObject);
		cache.remove(worldObject);
		
		assertEquals(0, cache.getWorldObjectsFor(2, 2).size());
		assertEquals(0, cache.getWorldObjectsFor(1, 1).size());
		assertEquals(0, cache.getWorldObjectsFor(3, 3).size());
		assertEquals(0, cache.getWorldObjectsFor(4, 4).size());
	}
	
	@Test
	public void testUpdateMediumWorldObject() {
		LocationWorldObjectsCache cache = new LocationWorldObjectsCache(10, 10);
		WorldObject worldObject = TestUtils.createWorldObject(2, 2, 1, 1, Constants.ID, 2);
		cache.add(worldObject);
		cache.update(worldObject, 3, 3);
		
		assertEquals(0, cache.getWorldObjectsFor(2, 2).size());
		assertEquals(0, cache.getWorldObjectsFor(1, 1).size());
		assertEquals(1, cache.getWorldObjectsFor(3, 3).size());
	}
	
	@Test
	public void testUpdateLargeWorldObject() {
		LocationWorldObjectsCache cache = new LocationWorldObjectsCache(10, 10);
		WorldObject worldObject = TestUtils.createWorldObject(2, 2, 2, 2, Constants.ID, 2);
		cache.add(worldObject);
		cache.update(worldObject, 3, 3);
		
		assertEquals(0, cache.getWorldObjectsFor(2, 2).size());
		assertEquals(0, cache.getWorldObjectsFor(1, 1).size());
		assertEquals(1, cache.getWorldObjectsFor(3, 3).size());
		assertEquals(1, cache.getWorldObjectsFor(4, 4).size());
		assertEquals(0, cache.getWorldObjectsFor(5, 5).size());
	}
	
	@Test
	public void testUpdateEnlargeMediumWorldObject() {
		LocationWorldObjectsCache cache = new LocationWorldObjectsCache(10, 10);
		WorldObject worldObject = TestUtils.createWorldObject(2, 2, 1, 1, Constants.ID, 2);
		cache.add(worldObject);
		cache.update(worldObject, 3, 3, 2, 2);
		
		assertEquals(0, cache.getWorldObjectsFor(2, 2).size());
		assertEquals(0, cache.getWorldObjectsFor(1, 1).size());
		assertEquals(1, cache.getWorldObjectsFor(3, 3).size());
		assertEquals(1, cache.getWorldObjectsFor(4, 4).size());
	}
	
	@Test
	public void testValue() {
		LocationWorldObjectsCache cache = new LocationWorldObjectsCache(5, 5);
		
		cache.add(TestUtils.createWorldObject(1, 1, 2, 2));
		cache.add(TestUtils.createWorldObject(3, 3, 1, 1));
		
		assertEquals(0, cache.value(0, 0));
		assertEquals(1, cache.value(1, 1));
		assertEquals(1, cache.value(1, 2));
		assertEquals(1, cache.value(2, 1));
		assertEquals(1, cache.value(2, 2));
		assertEquals(0, cache.value(2, 3));
		assertEquals(0, cache.value(3, 2));
		assertEquals(1, cache.value(3, 3));
		assertEquals(0, cache.value(4, 4));
	}
}
