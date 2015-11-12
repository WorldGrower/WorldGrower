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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.generator.ItemGenerator;

public class UTestHousePropertyUtils {

	@Test
	public void testHasHouses() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.HOUSES, new IdList());
		
		assertEquals(false, HousePropertyUtils.hasHouses(performer));
		
		performer.getProperty(Constants.HOUSES).add(2);
		assertEquals(true, HousePropertyUtils.hasHouses(performer));
	}
	
	@Test
	public void testGetHouseForSale() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject target = TestUtils.createIntelligentWorldObject(1, Constants.HOUSES, new IdList().add(2));
		WorldObject house = TestUtils.createIntelligentWorldObject(2, Constants.SELLABLE, Boolean.TRUE);
		world.addWorldObject(house);
		
		assertEquals(house, HousePropertyUtils.getHouseForSale(target, world));
		
		house.setProperty(Constants.SELLABLE, Boolean.FALSE);
		assertEquals(null, HousePropertyUtils.getHouseForSale(target, world));
	}
	
	@Test
	public void testGetHousingOfOwners() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject owner1 = TestUtils.createIntelligentWorldObject(1, Constants.HOUSES, new IdList());
		WorldObject owner2 = TestUtils.createIntelligentWorldObject(1, Constants.HOUSES, new IdList().add(2));
		
		WorldObject house = TestUtils.createIntelligentWorldObject(2, Constants.SELLABLE, Boolean.TRUE);
		world.addWorldObject(house);
		
		
		List<WorldObject> houses = HousePropertyUtils.getHousingOfOwners(Arrays.asList(owner1, owner2), world);
		assertEquals(1, houses.size());
		assertEquals(house, houses.get(0));
	}
	
	@Test
	public void testGetBestHouseNotOwner() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.HOUSES, new IdList());
		
		assertEquals(null, HousePropertyUtils.getBestHouse(performer, world));
	}
	
	@Test
	public void testGetBestHouseOwnsOneHouse() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.HOUSES, new IdList().add(2));
		
		WorldObject house = TestUtils.createIntelligentWorldObject(2, Constants.SLEEP_COMFORT, 5);
		world.addWorldObject(house);
		
		assertEquals(house, HousePropertyUtils.getBestHouse(performer, world));
	}
	
	@Test
	public void testGetBestHouseOwnsTwoHouses() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.HOUSES, new IdList().add(2).add(3));
		
		WorldObject house = TestUtils.createIntelligentWorldObject(2, Constants.SLEEP_COMFORT, 5);
		world.addWorldObject(house);
		
		WorldObject house2 = TestUtils.createIntelligentWorldObject(3, Constants.SLEEP_COMFORT, 8);
		world.addWorldObject(house2);
		
		assertEquals(house2, HousePropertyUtils.getBestHouse(performer, world));
	}
	
	@Test
	public void testHasHouseWithBed() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.HOUSES, new IdList());
		
		assertEquals(false, HousePropertyUtils.hasHouseWithBed(performer, world));
		
		performer.getProperty(Constants.HOUSES).add(2);
		
		WorldObject house = TestUtils.createIntelligentWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		house.getProperty(Constants.INVENTORY).add(ItemGenerator.getBed(1f));
		world.addWorldObject(house);
		
		assertEquals(true, HousePropertyUtils.hasHouseWithBed(performer, world));
	}
	
	@Test
	public void testAllHousesButFirstSellableNoHouses() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.HOUSES, new IdList());

		assertEquals(true, HousePropertyUtils.allHousesButFirstSellable(performer, world));
	}
	
	@Test
	public void testAllHousesButFirstSellableOneHouse() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.HOUSES, new IdList());

		int houseId = BuildingGenerator.generateHouse(0, 0, world, 1f);
		performer.getProperty(Constants.HOUSES).add(houseId);
		
		assertEquals(true, HousePropertyUtils.allHousesButFirstSellable(performer, world));
	}
	
	@Test
	public void testAllHousesButFirstSellableTwoHouses() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.HOUSES, new IdList());

		int houseId = BuildingGenerator.generateHouse(0, 0, world, 1f);
		performer.getProperty(Constants.HOUSES).add(houseId);
		
		int houseId2 = BuildingGenerator.generateHouse(0, 0, world, 1f);
		performer.getProperty(Constants.HOUSES).add(houseId2);
		
		assertEquals(false, HousePropertyUtils.allHousesButFirstSellable(performer, world));
	}
}
