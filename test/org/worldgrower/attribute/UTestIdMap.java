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

import java.util.Arrays;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;

public class UTestIdMap {
	
	@Test
	public void testIncrement() {
		IdMap idMap = new IdToIntegerMap();
		assertEquals(0, idMap.getValue(6));
		
		idMap.incrementValue(6, 2);
		assertEquals(2, idMap.getValue(6));
	}
	
	@Test
	public void testFindBestId() {
		IdMap idMap = new IdToIntegerMap();
		World world = new WorldImpl(0, 0, null, null);
		
		WorldObject person1 = TestUtils.createIntelligentWorldObject(1, Constants.GOLD, 10);
		WorldObject person2 = TestUtils.createIntelligentWorldObject(2, Constants.GOLD, 0);
		
		world.addWorldObject(person1);
		world.addWorldObject(person2);
		
		idMap.incrementValue(person1, 60);
		idMap.incrementValue(person2, 80);
		
		assertEquals(2, idMap.findBestId(w -> true, world));
		assertEquals(1, idMap.findBestId(w -> w.getProperty(Constants.GOLD) > 0, world));
	}
	
	@Test
	public void testGetIdsWithoutTarget() {
		IdMap idMap = new IdToIntegerMap();
		WorldObject person1 = TestUtils.createWorldObject(1, "Test1");
		WorldObject person2 = TestUtils.createWorldObject(2, "Test2");
		
		idMap.incrementValue(person1, 60);
		idMap.incrementValue(person2, 80);
		
		assertEquals(Arrays.asList(2), idMap.getIdsWithoutTarget(person1));
		assertEquals(Arrays.asList(1), idMap.getIdsWithoutTarget(person2));
	}
}
