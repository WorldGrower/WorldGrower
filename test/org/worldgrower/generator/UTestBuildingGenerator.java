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
package org.worldgrower.generator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;

public class UTestBuildingGenerator {

	@Test
	public void testisSellableShack() {
		World world = new WorldImpl(10, 10, null, null);
		int shackId = BuildingGenerator.generateShack(0, 0, world, TestUtils.createIntelligentWorldObject(3, "Test"));
		WorldObject shack = world.findWorldObjectById(shackId);

		assertEquals(true, BuildingGenerator.isSellable(shack));
	}
	
	@Test
	public void testisSellableHouse() {
		World world = new WorldImpl(10, 10, null, null);
		int houseId = BuildingGenerator.generateHouse(0, 0, world, TestUtils.createIntelligentWorldObject(0, "Test"));
		WorldObject house = world.findWorldObjectById(houseId);

		assertEquals(true, BuildingGenerator.isSellable(house));
	}
	
	@Test
	public void testisSellableWell() {
		World world = new WorldImpl(10, 10, null, null);
		int wellId = BuildingGenerator.buildWell(0, 0, world, 1f);
		WorldObject well = world.findWorldObjectById(wellId);

		assertEquals(false, BuildingGenerator.isSellable(well));
	}
	
	@Test
	public void testIsPrisonerInJail() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = TestUtils.createWorldObject(0, 0, 1, 1);
		BuildingGenerator.generateJail(0, 0, world, 1f);
		
		assertEquals(false, BuildingGenerator.isPrisonerInJail(performer, world));
		
		performer.setProperty(Constants.X, 1);
		performer.setProperty(Constants.Y, 1);
		assertEquals(true, BuildingGenerator.isPrisonerInJail(performer, world));
	}
}