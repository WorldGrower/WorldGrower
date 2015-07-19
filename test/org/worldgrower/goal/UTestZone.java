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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.worldgrower.TestUtils;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.Zone;

public class UTestZone {

	@Test
	public void testGetValues() {
		Zone zone = new Zone(5, 5);
		
		assertEquals(Arrays.asList(1, 2, 3), zone.getValuesX(2));
		assertEquals(Arrays.asList(0, 1), zone.getValuesX(0));
		assertEquals(Arrays.asList(3, 4), zone.getValuesX(4));
		
		assertEquals(Arrays.asList(1, 2, 3), zone.getValuesY(2));
		assertEquals(Arrays.asList(0, 1), zone.getValuesY(0));
		assertEquals(Arrays.asList(3, 4), zone.getValuesY(4));
	}
	
	@Test
	public void testAddValues() {
		Zone zone = new Zone(5, 5);
		List<WorldObject> worldObjects = new ArrayList<>();
		worldObjects.add(TestUtils.createWorldObject(2, 2, 1, 1));
		
		zone.addValues(worldObjects, 1, 5);
		
		assertEquals(0, zone.value(0, 0));
		assertEquals(5, zone.value(1, 1));
		assertEquals(5, zone.value(2, 2));
		assertEquals(5, zone.value(3, 3));
		assertEquals(0, zone.value(4, 4));
	}
	
	@Test
	public void testAddValuesAtBorders() {
		Zone zone = new Zone(5, 5);
		List<WorldObject> worldObjects = new ArrayList<>();
		worldObjects.add(TestUtils.createWorldObject(0, 0, 1, 1));
		worldObjects.add(TestUtils.createWorldObject(4, 4, 1, 1));
		
		zone.addValues(worldObjects, 1, 5);
		
		assertEquals(5, zone.value(0, 0));
		assertEquals(5, zone.value(1, 1));
		assertEquals(0, zone.value(2, 2));
		assertEquals(5, zone.value(3, 3));
		assertEquals(5, zone.value(4, 4));
	}
	
	@Test
	public void testAddSquaresOccupiedData() {
		Zone zone = new Zone(5, 5);
		
		List<WorldObject> worldObjects = new ArrayList<>();
		worldObjects.add(TestUtils.createWorldObject(1, 1, 2, 2));
		worldObjects.add(TestUtils.createWorldObject(3, 3, 1, 1));
		
		zone.addSquaresOccupiedData(worldObjects);
		
		assertEquals(0, zone.value(0, 0));
		assertEquals(1, zone.value(1, 1));
		assertEquals(1, zone.value(1, 2));
		assertEquals(1, zone.value(2, 1));
		assertEquals(1, zone.value(2, 2));
		assertEquals(0, zone.value(2, 3));
		assertEquals(0, zone.value(3, 2));
		assertEquals(1, zone.value(3, 3));
		assertEquals(0, zone.value(4, 4));
	}
}
