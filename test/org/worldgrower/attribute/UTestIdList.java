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

import org.junit.Test;
import org.worldgrower.TestUtils;

import static org.junit.Assert.assertEquals;

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
}
