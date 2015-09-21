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

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;

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
}
