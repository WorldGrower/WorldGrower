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
package org.worldgrower.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class UTestBuildingList {

	@Test
	public void testAdd() {
		BuildingList buildingList = new BuildingList();
		
		buildingList.add(6, BuildingType.SHACK);
		assertEquals(true, buildingList.contains(6));
		
		buildingList.add(7, BuildingType.HOUSE);
		assertEquals(true, buildingList.contains(BuildingType.HOUSE));
	}
	
	@Test
	public void testCount() {
		BuildingList buildingList = new BuildingList();
		buildingList.add(6, BuildingType.SHACK);
		buildingList.add(7, BuildingType.SHACK);
		buildingList.add(8, BuildingType.HOUSE);
		
		assertEquals(2, buildingList.count(BuildingType.SHACK));
		assertEquals(1, buildingList.count(BuildingType.HOUSE));
		assertEquals(0, buildingList.count(BuildingType.BREWERY));
	}
	
	@Test
	public void testCountTwoBuildingTypes() {
		BuildingList buildingList = new BuildingList();
		buildingList.add(6, BuildingType.SHACK);
		buildingList.add(7, BuildingType.SHACK);
		buildingList.add(8, BuildingType.HOUSE);
		
		assertEquals(3, buildingList.count(BuildingType.SHACK, BuildingType.HOUSE));
		assertEquals(1, buildingList.count(BuildingType.HOUSE, BuildingType.BREWERY));
		assertEquals(0, buildingList.count(BuildingType.BREWERY, BuildingType.APOTHECARY));
	}
	
	@Test
	public void testGetFirstId() {
		BuildingList buildingList = new BuildingList();
		buildingList.add(6, BuildingType.SHACK);
		buildingList.add(7, BuildingType.SHACK);
		buildingList.add(8, BuildingType.HOUSE);
		
		assertEquals(6, buildingList.getFirstId(BuildingType.SHACK));
		assertEquals(8, buildingList.getFirstId(BuildingType.HOUSE));
		
		try {
			buildingList.getFirstId(BuildingType.BREWERY);
			fail("method should fail");
		} catch(IllegalStateException ex) {
			assertEquals("No entries found for buildingType BREWERY", ex.getMessage());
		}
	}
	
	@Test
	public void testGetFirstIdOrNull() {
		BuildingList buildingList = new BuildingList();
		buildingList.add(6, BuildingType.SHACK);
		buildingList.add(7, BuildingType.SHACK);
		buildingList.add(8, BuildingType.HOUSE);
		
		assertEquals(6, buildingList.getFirstIdOrNull(BuildingType.SHACK).intValue());
		assertEquals(8, buildingList.getFirstIdOrNull(BuildingType.HOUSE).intValue());
		assertEquals(null, buildingList.getFirstIdOrNull(BuildingType.BREWERY));
	}
}
