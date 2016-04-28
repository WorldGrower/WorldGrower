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
package org.worldgrower.attribute;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;

public class UTestIdList {

	@Test
	public void testAdd() {
		IdList idList = new IdList();
		
		assertEquals(0, idList.size());
		
		idList.add(6);
		assertEquals(1, idList.size());
		
		idList.add(TestUtils.createWorldObject(7, "Test"));
		assertEquals(2, idList.size());
	}
	
	@Test
	public void testRemove() {
		IdList idList = new IdList();
		idList.add(6);
		idList.add(TestUtils.createWorldObject(7, "Test"));
		
		assertEquals(2, idList.size());
		
		idList.remove(6);
		assertEquals(1, idList.size());
		
		idList.remove(TestUtils.createWorldObject(7, "Test"));
		assertEquals(0, idList.size());
	}
	
	@Test
	public void testContains() {
		IdList idList = new IdList();
		idList.add(6);
		idList.add(TestUtils.createWorldObject(7, "Test"));
		
		assertEquals(true, idList.contains(TestUtils.createWorldObject(7, "Test")));
		assertEquals(true, idList.contains(TestUtils.createWorldObject(6, "Test")));
		assertEquals(false, idList.contains(TestUtils.createWorldObject(5, "Test")));
	}
	
	@Test
	public void testIntersects() {
		IdList idList1 = new IdList().add(3);
		IdList idList2 = new IdList().add(4);
		IdList idList3 = new IdList().add(3).add(4);
		
		assertEquals(false, idList1.intersects(idList2));
		assertEquals(false, idList2.intersects(idList1));
		assertEquals(true, idList1.intersects(idList1));
		assertEquals(true, idList1.intersects(idList3));
	}
	
	@Test
	public void testMapToWorldObjects() {
		IdList idList = new IdList().add(1).add(2);
		World world = new WorldImpl(1, 1, null, null);
		world.addWorldObject(TestUtils.createWorldObject(1, "Test1"));
		world.addWorldObject(TestUtils.createWorldObject(2, "Test2"));
		
		List<WorldObject> worldObjects = idList.mapToWorldObjects(world, w -> w.getProperty(Constants.NAME).equals("Test1"));
		assertEquals(1, worldObjects.size());
		assertEquals(1, worldObjects.get(0).getProperty(Constants.ID).intValue());
	}
}
