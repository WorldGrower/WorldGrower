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

import java.util.List;

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
import org.worldgrower.generator.Item;

public class UTestBuySellUtils {

	@Test
	public void testGetPrice() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		performer.getProperty(Constants.INVENTORY).add(Item.IRON_CUIRASS.generate(0f));
		performer.getProperty(Constants.PRICES).put(Item.IRON_CUIRASS, 200);
		
		assertEquals(200, BuySellUtils.getPrice(performer, 0));
	}
	
	@Test
	public void testGetDemandGoods() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.DEMANDS, new PropertyCountMap<ManagedProperty<?>>());
		WorldObject inventoryItem = Item.BERRIES.generate(1f);
		
		assertEquals(false, BuySellUtils.hasDemandForInventoryItemGoods(performer, inventoryItem));
		
		performer.getProperty(Constants.DEMANDS).add(Constants.FOOD, 1);
		assertEquals(true, BuySellUtils.hasDemandForInventoryItemGoods(performer, inventoryItem));
	}
	
	@Test
	public void testPerformerCanBuyGoods() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.GOLD, 0);
		
		WorldObject target = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject inventoryItem = Item.BERRIES.generate(1f);
		target.getProperty(Constants.INVENTORY).add(inventoryItem);
		target.getProperty(Constants.PRICES).put(Item.BERRIES, 2);
		
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
		
		WorldObject target = TestUtils.createIntelligentWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject inventoryItem = Item.BERRIES.generate(1f);
		inventoryItem.setProperty(Constants.SELLABLE, Boolean.TRUE);
		target.getProperty(Constants.INVENTORY).addQuantity(inventoryItem);
		target.getProperty(Constants.PRICES).put(Item.BERRIES, 2);
		world.addWorldObject(target);
		
		assertEquals(Actions.BUY_ACTION, BuySellUtils.getBuyOperationInfo(performer, Constants.FOOD, world).getManagedOperation());
	}
	
	@Test
	public void testGetBuyOperationInfoForEquipment() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.GOLD, 100);
		
		assertEquals(null, BuySellUtils.getBuyOperationInfo(performer, Constants.FOOD, world));
		
		WorldObject target = TestUtils.createIntelligentWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject inventoryItem = Item.IRON_CUIRASS.generate(1f);
		inventoryItem.setProperty(Constants.SELLABLE, Boolean.TRUE);
		target.getProperty(Constants.INVENTORY).addQuantity(inventoryItem);
		target.getProperty(Constants.PRICES).put(Item.IRON_CUIRASS, 200);
		world.addWorldObject(target);
		
		assertEquals(true, BuySellUtils.hasSellableEquipment(Constants.EQUIPMENT_SLOT, Constants.TORSO_EQUIPMENT, target));
	}
	
	@Test
	public void testTargetWillBuyGoods() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		
		WorldObject target = TestUtils.createIntelligentWorldObject(0, Constants.INVENTORY, new WorldObjectContainer());
		target.setProperty(Constants.GOLD, 100);
		
		WorldObject inventoryItem = Item.BERRIES.generate(1f);
		performer.getProperty(Constants.INVENTORY).add(inventoryItem);
		performer.getProperty(Constants.PRICES).put(Item.BERRIES, 2);
		
		assertEquals(false, BuySellUtils.targetWillBuyGoods(performer, target, 0, world));
		
		target.setProperty(Constants.DEMANDS, new PropertyCountMap<>());
		target.getProperty(Constants.DEMANDS).add(Constants.FOOD, 1);
		assertEquals(true, BuySellUtils.targetWillBuyGoods(performer, target, 0, world));
	}
	
	//TODO: worldObject cannot buy from itself
	//TODO: worldObject may not buy null object
	
	@Test
	public void testFindBuyTargets() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		world.addWorldObject(target);
		WorldObject cottonShirt = Item.COTTON_SHIRT.generate(1f);
		cottonShirt.setProperty(Constants.SELLABLE, Boolean.TRUE);
		target.getProperty(Constants.INVENTORY).add(cottonShirt);
		
		List<WorldObject> targets = BuySellUtils.findBuyTargets(performer, Constants.NAME, Item.COTTON_SHIRT_NAME, world);
		assertEquals(1, targets.size());
		assertEquals(target, targets.get(0));
	}
	
	@Test
	public void testBetterPriceExistsNull() {
		World world = new WorldImpl(0, 0, null, null);
		assertEquals(false, BuySellUtils.betterPriceExists(Item.BERRIES.generate(1f), world, 1));
	}
	
	@Test
	public void testBetterPriceExistsTrue() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject worldObjectToBuy = Item.BERRIES.generate(1f);
		worldObjectToBuy.setProperty(Constants.PRICE, 10);
		
		WorldObject target = TestUtils.createIntelligentWorldObject(2, "target");
		WorldObject otherBerries = Item.BERRIES.generate(1f);
		otherBerries.setProperty(Constants.PRICE, 2);
		otherBerries.setProperty(Constants.SELLABLE, Boolean.TRUE);
		target.getProperty(Constants.INVENTORY).addQuantity(otherBerries);
		world.addWorldObject(target);
		
		assertEquals(true, BuySellUtils.betterPriceExists(worldObjectToBuy, world, 10));
	}
}
