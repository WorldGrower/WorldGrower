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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.ItemCountMap;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.PropertyCountMap;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;

public class UTestBuySellUtils {

	@Test
	public void testGetPrice() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		performer.getProperty(Constants.INVENTORY).add(Item.IRON_CUIRASS.generate(0f));
		performer.getProperty(Constants.PRICES).setPrice(Item.IRON_CUIRASS, 200);
		
		assertEquals(200, BuySellUtils.getPrice(performer, 0));
	}
	
	@Test
	public void testGetDemandGoods() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.DEMANDS, new PropertyCountMap<ManagedProperty<?>>());
		performer.setProperty(Constants.INVENTORY, new WorldObjectContainer());
		WorldObject inventoryItem = Item.BERRIES.generate(1f);
		
		assertEquals(false, BuySellUtils.hasDemandForInventoryItemGoods(performer, inventoryItem));
		
		performer.getProperty(Constants.DEMANDS).add(Constants.FOOD, 1);
		assertEquals(true, BuySellUtils.hasDemandForInventoryItemGoods(performer, inventoryItem));
	
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.BERRIES.generate(1f), 1);
		assertEquals(false, BuySellUtils.hasDemandForInventoryItemGoods(performer, inventoryItem));
	}
	
	@Test
	public void testPerformerCanBuyGoods() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.GOLD, 0);
		
		WorldObject target = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject inventoryItem = Item.BERRIES.generate(1f);
		target.getProperty(Constants.INVENTORY).add(inventoryItem);
		target.getProperty(Constants.PRICES).setPrice(Item.BERRIES, 2);
		
		assertEquals(false, BuySellUtils.performerCanBuyGoods(performer, target, 0, 1));
		
		performer.setProperty(Constants.GOLD, 100);
		assertEquals(true, BuySellUtils.performerCanBuyGoods(performer, target, 0, 1));
	}
	
	@Test
	public void testGetBuyOperationInfo() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.GOLD, 100);
		
		assertEquals(null, BuySellUtils.getBuyOperationInfo(performer, Constants.FOOD, 5, world));
		
		WorldObject target = TestUtils.createIntelligentWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject inventoryItem = Item.BERRIES.generate(1f);
		inventoryItem.setProperty(Constants.SELLABLE, Boolean.TRUE);
		target.getProperty(Constants.INVENTORY).addQuantity(inventoryItem, 5);
		target.getProperty(Constants.PRICES).setPrice(Item.BERRIES, 2);
		world.addWorldObject(target);
		
		assertEquals(Actions.BUY_ACTION, BuySellUtils.getBuyOperationInfo(performer, Constants.FOOD, 5, world).getManagedOperation());
	}
	
	@Test
	public void testGetBuyOperationInfoEnemy() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.GOLD, 100);
		
		assertEquals(null, BuySellUtils.getBuyOperationInfo(performer, Constants.FOOD, 5, world));
		
		WorldObject target = TestUtils.createIntelligentWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject inventoryItem = Item.BERRIES.generate(1f);
		inventoryItem.setProperty(Constants.SELLABLE, Boolean.TRUE);
		target.getProperty(Constants.INVENTORY).addQuantity(inventoryItem, 5);
		target.getProperty(Constants.PRICES).setPrice(Item.BERRIES, 2);
		target.getProperty(Constants.GROUP).removeAll();
		world.addWorldObject(target);
		
		assertEquals(null, BuySellUtils.getBuyOperationInfo(performer, Constants.FOOD, 5, world));
	}
	
	@Test
	public void testGetBuyOperationInfoForEquipmentNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.GOLD, 100);
		
		assertEquals(null, BuySellUtils.getBuyOperationInfo(performer, Constants.FOOD, 5, world));
		
		WorldObject target = TestUtils.createIntelligentWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject inventoryItem = Item.IRON_CUIRASS.generate(1f);
		inventoryItem.setProperty(Constants.SELLABLE, Boolean.TRUE);
		target.getProperty(Constants.INVENTORY).addQuantity(inventoryItem);
		target.getProperty(Constants.PRICES).setPrice(Item.IRON_CUIRASS, 200);
		world.addWorldObject(target);
		
		assertEquals(true, BuySellUtils.hasSellableEquipment(Item.IRON_CUIRASS, target));
	}
	
	@Test
	public void testTargetWillBuyGoods() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		
		WorldObject target = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		target.setProperty(Constants.GOLD, 100);
		
		WorldObject inventoryItem = Item.BERRIES.generate(1f);
		performer.getProperty(Constants.INVENTORY).add(inventoryItem);
		performer.getProperty(Constants.PRICES).setPrice(Item.BERRIES, 2);
		
		assertEquals(false, BuySellUtils.buyerWillBuyGoods(performer, target, 0, world));
		
		target.setProperty(Constants.DEMANDS, new PropertyCountMap<>());
		target.getProperty(Constants.DEMANDS).add(Constants.FOOD, 1);
		assertEquals(true, BuySellUtils.buyerWillBuyGoods(performer, target, 0, world));
	}
	
	//TODO: worldObject cannot buy from itself
	//TODO: worldObject may not buy null object
	
	@Test
	public void testFindBuyTargets() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		world.addWorldObject(target);
		performer.setProperty(Constants.GOLD, 2000);
		WorldObject cottonShirt = Item.COTTON_SHIRT.generate(1f);
		cottonShirt.setProperty(Constants.SELLABLE, Boolean.TRUE);
		target.getProperty(Constants.INVENTORY).add(cottonShirt);
		
		List<WorldObject> targets = BuySellUtils.findBuyTargets(performer, Item.COTTON_SHIRT, 5, world);
		assertEquals(1, targets.size());
		assertEquals(target, targets.get(0));
	}
	
	@Test
	public void testBetterPriceExistsNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		assertEquals(false, BuySellUtils.betterPriceExists(performer, Item.BERRIES.generate(1f), world, 1));
	}
	
	@Test
	public void testBetterPriceExistsTrue() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject worldObjectToBuy = Item.BERRIES.generate(1f);
		worldObjectToBuy.setProperty(Constants.PRICE, 10);
		
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
		WorldObject otherBerries = Item.BERRIES.generate(1f);
		otherBerries.setProperty(Constants.PRICE, 2);
		otherBerries.setProperty(Constants.SELLABLE, Boolean.TRUE);
		target.getProperty(Constants.INVENTORY).addQuantity(otherBerries);
		world.addWorldObject(target);
		
		assertEquals(true, BuySellUtils.betterPriceExists(performer, worldObjectToBuy, world, 10));
	}
	
	@Test
	public void testGetBuyOperationInfoForEquipment() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.GOLD, 1000);
		
		assertEquals(null, BuySellUtils.getBuyOperationInfo(performer, Constants.FOOD, 5, world));
		
		WorldObject target = TestUtils.createIntelligentWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject inventoryItem = Item.IRON_CUIRASS.generate(1f);
		inventoryItem.setProperty(Constants.SELLABLE, Boolean.TRUE);
		target.getProperty(Constants.INVENTORY).addQuantity(inventoryItem);
		target.getProperty(Constants.PRICES).setPrice(Item.IRON_CUIRASS, 200);
		world.addWorldObject(target);
		
		assertEquals(Actions.BUY_ACTION, BuySellUtils.getBuyOperationInfo(performer, Item.IRON_CUIRASS, 1, world).getManagedOperation());
	}
	
	@Test
	public void testExecuteBuyClothesOperationInfo() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(3, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.GOLD, 1000);
		
		WorldObject target = TestUtils.createIntelligentWorldObject(4, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject inventoryItem = Item.COTTON_SHIRT.generate(1f);
		inventoryItem.setProperty(Constants.SELLABLE, Boolean.TRUE);
		target.getProperty(Constants.INVENTORY).addQuantity(inventoryItem);
		target.getProperty(Constants.PRICES).setPrice(Item.COTTON_SHIRT, 5);
		target.setProperty(Constants.ITEMS_SOLD, new ItemCountMap());
		target.setProperty(Constants.GOLD, 1000);
		world.addWorldObject(target);
		
		OperationInfo buyOperationInfo = BuySellUtils.create(performer, target, Item.COTTON_SHIRT, 1, world);
		
		buyOperationInfo.getManagedOperation().execute(performer, target, buyOperationInfo.getArgs(), world);
		
		assertEquals(0, performer.getProperty(Constants.INVENTORY).getIndexFor(Constants.ITEM_ID, Item.COTTON_SHIRT));
		assertEquals(995, performer.getProperty(Constants.GOLD).intValue());
		assertEquals(1005, target.getProperty(Constants.GOLD).intValue());
	}
	
	@Test
	public void testGetPriceNullWorldObject() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		
		try {
			BuySellUtils.getPrice(performer, null);
			fail("method should fail");
		} catch(IllegalStateException e) {
			assertEquals("WorldObject is null", e.getMessage());
		}
	}
	
	@Test
	public void testGetPriceNullWorldObjectPrice() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		
		try {
			WorldObject food = Item.BERRIES.generate(1f);
			food.removeProperty(Constants.PRICE);
			BuySellUtils.getPrice(performer, food);
			fail("method should fail");
		} catch(IllegalStateException e) {
			assertTrue(e.getMessage(), e.getMessage().endsWith("has no price"));
		}
	}
	
	@Test
	public void testCalculateQuantity() {
		WorldObject buyer = TestUtils.createIntelligentWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		buyer.setProperty(Constants.GOLD, 1000);

		WorldObject seller = TestUtils.createIntelligentWorldObject(3, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject berries = Item.BERRIES.generate(1f);
		berries.setProperty(Constants.SELLABLE, Boolean.TRUE);
		seller.getProperty(Constants.INVENTORY).addQuantity(berries, 20);
		
		assertEquals(20, BuySellUtils.calculateQuantity(buyer, seller, 0));
		
		buyer.setProperty(Constants.GOLD, 1);
		assertEquals(1, BuySellUtils.calculateQuantity(buyer, seller, 0));
	}
}
