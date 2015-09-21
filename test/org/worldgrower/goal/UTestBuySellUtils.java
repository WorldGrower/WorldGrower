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
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.PropertyCountMap;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.ItemGenerator;

public class UTestBuySellUtils {

	@Test
	public void testGetPrice() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		performer.getProperty(Constants.INVENTORY).add(ItemGenerator.getIronCuirass(0f));
		performer.setProperty(Constants.PROFIT_PERCENTAGE, 100);
		
		assertEquals(600, BuySellUtils.getPrice(performer, 0));
	}
	
	@Test
	public void testGetDemandGoods() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.DEMANDS, new PropertyCountMap<ManagedProperty<?>>());
		WorldObject inventoryItem = ItemGenerator.generateBerries();
		
		assertEquals(false, BuySellUtils.hasDemandForInventoryItemGoods(performer, inventoryItem));
		
		performer.getProperty(Constants.DEMANDS).add(Constants.FOOD, 1);
		assertEquals(true, BuySellUtils.hasDemandForInventoryItemGoods(performer, inventoryItem));
	}
	
	@Test
	public void testPerformerCanBuyGoods() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.GOLD, 0);
		
		WorldObject target = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject inventoryItem = ItemGenerator.generateBerries();
		target.getProperty(Constants.INVENTORY).add(inventoryItem);
		target.setProperty(Constants.PROFIT_PERCENTAGE, 100);
		
		assertEquals(false, BuySellUtils.performerCanBuyGoods(performer, target, 0, 1));
		
		performer.setProperty(Constants.GOLD, 100);
		assertEquals(true, BuySellUtils.performerCanBuyGoods(performer, target, 0, 1));
	}
	
	@Test
	public void testGetBuyOperationInfo() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.GOLD, 100);
		
		assertEquals(null, BuySellUtils.getBuyOperationInfo(performer, Constants.FOOD, world));
		
		WorldObject target = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject inventoryItem = ItemGenerator.generateBerries();
		inventoryItem.setProperty(Constants.SELLABLE, Boolean.TRUE);
		target.getProperty(Constants.INVENTORY).addQuantity(inventoryItem);
		target.setProperty(Constants.PROFIT_PERCENTAGE, 100);
		world.addWorldObject(target);
		
		assertEquals(Actions.BUY_ACTION, BuySellUtils.getBuyOperationInfo(performer, Constants.FOOD, world).getManagedOperation());
	}
}
