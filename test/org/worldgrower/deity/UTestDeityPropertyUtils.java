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
package org.worldgrower.deity;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.generator.BuildingGenerator;

public class UTestDeityPropertyUtils {

	@Test
	public void testDeityIsLessWorshippedThanOthers() {
		World world = new WorldImpl(1, 1, null, null);
		
		assertEquals(false, DeityPropertyUtils.deityIsLessWorshippedThanOthers(Deity.HADES, world));
		
		for(int i=0; i<20; i++) {
			WorldObject target = TestUtils.createIntelligentWorldObject(i, Constants.DEITY, Deity.APHRODITE);
			world.addWorldObject(target);
		}
		
		assertEquals(true, DeityPropertyUtils.deityIsLessWorshippedThanOthers(Deity.HADES, world));
	}
	
	@Test
	public void testGetTotalNumberOfWorshippers() {
		World world = new WorldImpl(1, 1, null, null);
		
		assertEquals(0, DeityPropertyUtils.getTotalNumberOfWorshippers(world));
		
		WorldObject target = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		world.addWorldObject(target);
		
		WorldObject target2 = TestUtils.createIntelligentWorldObject(2, Constants.DEITY, Deity.APHRODITE);
		world.addWorldObject(target2);
		assertEquals(1, DeityPropertyUtils.getTotalNumberOfWorshippers(world));
	}
	
	@Test
	public void testGetWorshippersFor() {
		World world = new WorldImpl(1, 1, null, null);
		
		assertEquals(new ArrayList<>(), DeityPropertyUtils.getWorshippersFor(Deity.HADES, world));
		
		WorldObject target = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.HADES);
		world.addWorldObject(target);
		
		assertEquals(Arrays.asList(target), DeityPropertyUtils.getWorshippersFor(Deity.HADES, world));
	}
	
	@Test
	public void testGetWorshippersForExcludeShrine() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.HADES);
		world.addWorldObject(performer);
		BuildingGenerator.generateShrine(0, 0, world, performer);
		assertEquals(Arrays.asList(performer), DeityPropertyUtils.getWorshippersFor(Deity.HADES, world));
	}
	
	@Test
	public void testgetAllWorshippers() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.HADES);
		world.addWorldObject(performer);
		BuildingGenerator.generateShrine(0, 0, world, performer);
		assertEquals(Arrays.asList(performer), DeityPropertyUtils.getAllWorshippers(world));

	}
}
