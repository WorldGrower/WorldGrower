/*******************************************************************************
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*******************************************************************************/
package org.worldgrower.goal;

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.Prices;
import org.worldgrower.attribute.PropertyCountMap;
import org.worldgrower.attribute.StringProperty;
import org.worldgrower.attribute.UnCheckedProperty;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.profession.Professions;

public class BuySellUtils {

	public static List<WorldObject> findBuyTargets(WorldObject performer, IntProperty property, int quantity, World world) {
		List<WorldObject> targets = GoalUtils.findNearestTargetsByProperty(performer, Actions.BUY_ACTION, Constants.STRENGTH, w -> isBuyTarget(performer, property, quantity, w), world);
		return targets;
	}

	private static boolean isBuyTarget(WorldObject performer, IntProperty property, int quantity, WorldObject w) {
		return targetHasSufficientQuantity(property, quantity, w) && performerCanPay(performer, w, property, quantity) && !GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w);
	}

	private static boolean targetHasSufficientQuantity(IntProperty property, int quantity, WorldObject w) {
		return w.getProperty(Constants.INVENTORY).getQuantityFor(property, Constants.PRICE, inventoryItem -> inventoryItem.getProperty(Constants.SELLABLE)) >= quantity;
	}
	
	public static List<WorldObject> findBuyTargets(WorldObject performer, StringProperty property, String value, int quantity, World world) {
		List<WorldObject> targets = GoalUtils.findNearestTargetsByProperty(performer, Actions.BUY_ACTION, Constants.STRENGTH, w -> targetHasSellableItem(w, property, value) && performerCanPay(performer, w, property, quantity), world);
		return targets;
	}
	
	private static boolean performerCanPay(WorldObject performer, WorldObject target, ManagedProperty property, int quantity) {
		int indexOfItemToSell = target.getProperty(Constants.INVENTORY).getIndexFor(property);
		if (indexOfItemToSell != -1) {
			int performerGold = performer.getProperty(Constants.GOLD);
			return performerGold >= getPrice(target, indexOfItemToSell) * quantity;
		} else {
			return false;
		}
	}
	
	private static boolean targetHasSellableItem(WorldObject w, StringProperty property, String value) {
		return w.getProperty(Constants.INVENTORY).getIndexFor(property, value, inventoryItem -> isInventoryItemSellable(inventoryItem)) >= 0;
	}
	
	private static List<WorldObject> findBuyTargets(WorldObject performer, UnCheckedProperty<UnCheckedProperty<WorldObject>> equipmentSlotProperty, UnCheckedProperty<WorldObject> equipmentSlot, int quantity, World world) {
		List<WorldObject> targets = GoalUtils.findNearestTargetsByProperty(performer, Actions.BUY_ACTION, Constants.STRENGTH, w -> hasSellableEquipment(equipmentSlotProperty, equipmentSlot, w) && performerCanPay(performer, w, equipmentSlotProperty, quantity), world);
		return targets;
	}

	static boolean hasSellableEquipment(UnCheckedProperty<UnCheckedProperty<WorldObject>> equipmentSlotProperty, UnCheckedProperty<WorldObject> equipmentSlot, WorldObject w) {
		return w.getProperty(Constants.INVENTORY).getIndexFor(equipmentSlotProperty, equipmentSlot, inventoryItem -> isInventoryItemSellable(inventoryItem)) >= 0;
	}
	
	static boolean isInventoryItemSellable(WorldObject inventoryItem) {
		return inventoryItem.hasProperty(Constants.PRICE) && inventoryItem.getProperty(Constants.SELLABLE);
	}
	
	public static int getPrice(WorldObject performer, int inventoryIndex) {
		WorldObject inventoryItem = getInventoryItem(performer, inventoryIndex);
		return getPrice(performer, inventoryItem);
	}

	public static int getPrice(WorldObject performer, WorldObject worldObject) {
		if (worldObject == null) {
			throw new IllegalStateException("WorldObject is null");
		}
		
		if (worldObject.getProperty(Constants.PRICE) == null) {
			throw new IllegalStateException("WorldObject " + worldObject + " has no price");
		}
		Prices prices = performer.getProperty(Constants.PRICES);
		Item key = worldObject.getProperty(Constants.ITEM_ID);
		if (key != null) {
			return prices.getPrice(key);
		} else {
			//TODO: temporary for houses
			return worldObject.getProperty(Constants.PRICE);
		}
	}

	private static WorldObject getInventoryItem(WorldObject performer, int inventoryIndex) {
		return performer.getProperty(Constants.INVENTORY).get(inventoryIndex);
	}
	
	public static boolean targetWillBuyGoods(WorldObject performer, WorldObject target, int indexOfItemsToSell, World world) {
		WorldObject inventoryItem = getInventoryItem(performer, indexOfItemsToSell);
		return buyerWillBuyGoods(performer, target, inventoryItem, world);
	}
	
	public static boolean sellerWillSellGoods(WorldObject buyer, WorldObject seller, int indexOfItemsToSell, World world) {
		WorldObject inventoryItem = getInventoryItem(seller, indexOfItemsToSell);
		return buyerWillBuyGoods(seller, buyer, inventoryItem, world);
	}
	
	public static boolean buyerWillBuyGoods(WorldObject seller, WorldObject buyer, WorldObject worldObjectToBuy, World world) {
		boolean demandsGoods = hasDemandForInventoryItemGoods(buyer, worldObjectToBuy) || buyer.getProperty(Constants.PROFESSION) == Professions.MERCHANT_PROFESSION;
		
		int price = BuySellUtils.getPrice(seller, worldObjectToBuy);
		boolean betterPriceExists = betterPriceExists(seller, worldObjectToBuy, world, price);
		
		boolean hasMoneyToBuyGoods = (price <= buyer.getProperty(Constants.GOLD));
		
		return demandsGoods && !betterPriceExists && hasMoneyToBuyGoods;
	}
	
	public static boolean performerCanBuyGoods(WorldObject performer, WorldObject target, int indexOfItemsToSell, int quantity) {
		WorldObject worldObject = target.getProperty(Constants.INVENTORY).get(indexOfItemsToSell);
		int price = BuySellUtils.getPrice(target, worldObject) * quantity;
		boolean hasMoneyToBuyGoods = (price <= performer.getProperty(Constants.GOLD));
		return hasMoneyToBuyGoods;
	}

	//TODO: fix demands
	static boolean hasDemandForInventoryItemGoods(WorldObject target, WorldObject inventoryItem) {
		List<ManagedProperty<?>> propertyKeys = inventoryItem.getPropertyKeys();
		boolean demandsGoods = false;
		for(ManagedProperty<?> property : propertyKeys) {
			demandsGoods = demandsGoods || target.hasProperty(Constants.DEMANDS) && target.getProperty(Constants.DEMANDS).count(property) > 0;
		}
		return demandsGoods;
	}


	static boolean betterPriceExists(WorldObject performer, WorldObject worldObjectToBuy, World world, int price) {
		Item itemToBuy = worldObjectToBuy.getProperty(Constants.ITEM_ID);
		boolean betterPriceExists = false;
		List<WorldObject> targets = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> hasSellableItem(w));
		for(WorldObject target : targets) {
			WorldObjectContainer targetInventory = target.getProperty(Constants.INVENTORY);
			int indexForItem = targetInventory.getIndexFor(Constants.ITEM_ID, itemToBuy, w -> w.getProperty(Constants.SELLABLE));
			if (indexForItem != -1) {
				int priceTarget = getPrice(target, indexForItem);
				if (priceTarget < price) {
					betterPriceExists = true;
				}
			}
		}
		return betterPriceExists;
	}

	private static boolean hasSellableItem(WorldObject w) {
		return  w.hasProperty(Constants.INVENTORY) && w.getProperty(Constants.INVENTORY).getWorldObjects(Constants.SELLABLE, Boolean.TRUE).size() > 0;
	}

	public static int getIndexFor(WorldObject target, StringProperty property, String value) {
		return target.getProperty(Constants.INVENTORY).getIndexFor(property, value, inventoryItem -> isInventoryItemSellable(inventoryItem));
	}
	
	public static OperationInfo getBuyOperationInfo(WorldObject performer, IntProperty propertyToBuy, int quantity, World world) {
		List<WorldObject> targets = findBuyTargets(performer, propertyToBuy, quantity, world);
		if (targets.size() > 0) {
			WorldObject target = targets.get(0);
			int indexOfProperty = target.getProperty(Constants.INVENTORY).getIndexFor(propertyToBuy);
			if (performerCanBuyGoods(performer, target, indexOfProperty, quantity)) {
				int price = calculatePrice(target, indexOfProperty);
				int itemId = target.getProperty(Constants.INVENTORY).get(indexOfProperty).getProperty(Constants.ITEM_ID).ordinal();
				return new OperationInfo(performer, target, new int[] { indexOfProperty, price, quantity, itemId }, Actions.BUY_ACTION);
			}
		}
		return null;
	}
	
	public static OperationInfo getBuyOperationInfo(WorldObject performer, UnCheckedProperty<UnCheckedProperty<WorldObject>> equipmentSlotProperty, UnCheckedProperty<WorldObject> equipmentSlot, int quantity, World world) {
		List<WorldObject> targets = findBuyTargets(performer, equipmentSlotProperty, equipmentSlot, quantity, world);
		if (targets.size() > 0) {
			WorldObject target = targets.get(0);
			int indexOfProperty = target.getProperty(Constants.INVENTORY).getIndexFor(equipmentSlotProperty, equipmentSlot);
			if (performerCanBuyGoods(performer, target, indexOfProperty, quantity)) {
				int price = calculatePrice(target, indexOfProperty);
				int itemId = target.getProperty(Constants.INVENTORY).get(indexOfProperty).getProperty(Constants.ITEM_ID).ordinal();
				return new OperationInfo(performer, target, new int[] { indexOfProperty, price, quantity, itemId }, Actions.BUY_ACTION);
			}
		}
		return null;
	}

	private static int calculatePrice(WorldObject target, int indexOfProperty) {
		Item item = target.getProperty(Constants.INVENTORY).get(indexOfProperty).getProperty(Constants.ITEM_ID);
		int price = target.getProperty(Constants.PRICES).getPrice(item);
		return price;
	}
	
	public static OperationInfo create(WorldObject performer, WorldObject target, Item item, int quantity) {
		int targetInventoryIndex = target.getProperty(Constants.INVENTORY).getIndexFor(w -> w.getProperty(Constants.ITEM_ID) == item);
		int price = target.getProperty(Constants.PRICES).getPrice(item);
		if (performerCanBuyGoods(performer, target, targetInventoryIndex, quantity)) {
			return new OperationInfo(performer, target, new int[] { targetInventoryIndex, price, quantity, item.ordinal() }, Actions.BUY_ACTION);
		} else {
			return null;
		}
	}
	
	public static void performerGivesItemToTarget(WorldObject performer, WorldObject target, IntProperty itemProperty, int quantity) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		int indexOfFood = performerInventory.getIndexFor(itemProperty);
		WorldObject food = performerInventory.get(indexOfFood).deepCopy();
		performerInventory.removeQuantity(indexOfFood, quantity);
		
		WorldObjectContainer targetInventory = target.getProperty(Constants.INVENTORY);
		targetInventory.addQuantity(food, quantity);
	}
	
	public static List<ManagedProperty<?>> getBuyingProperties(WorldObject worldObject) {
    	PropertyCountMap<ManagedProperty<?>> demands = worldObject.getProperty(Constants.DEMANDS);
    	return demands.keySet();
    }
	
    public static boolean isSellableWorldObject(WorldObject worldObject) {
    	return worldObject.hasProperty(Constants.SELLABLE) && worldObject.getProperty(Constants.SELLABLE);
    }
    
    public static OperationInfo getSellOperationInfo(WorldObject performer, World world) {
		List<WorldObject> targets = GoalUtils.findNearestTargetsByProperty(performer, Actions.SELL_ACTION, Constants.STRENGTH, w -> true, world);
		for(WorldObject target : targets) {
			List<ManagedProperty<?>> targetBuyingProperties = getBuyingProperties(target);
			int indexOfSellableObject = getIndexOfSellableWorldObject(performer, targetBuyingProperties);
			if (indexOfSellableObject != -1 && targetWillBuyGoods(performer, target, indexOfSellableObject, world)) {
				int price = calculatePrice(performer, indexOfSellableObject);
				int quantity = calculateQuantity(target, performer, indexOfSellableObject);
				return new OperationInfo(performer, target, new int[] { indexOfSellableObject, price, quantity }, Actions.SELL_ACTION);
			}
		}

		return null;
	}
    
    public static OperationInfo getBuyOperationInfo(WorldObject buyer, List<ManagedProperty<?>> performerBuyingProperties, World world) {
		List<WorldObject> sellers = GoalUtils.findNearestTargetsByProperty(buyer, Actions.BUY_ACTION, Constants.STRENGTH, w -> true, world);
		for(WorldObject seller : sellers) {
			int indexOfSellableObject = getIndexOfSellableWorldObject(seller, performerBuyingProperties);
			if (indexOfSellableObject != -1 && sellerWillSellGoods(buyer, seller, indexOfSellableObject, world)) {
				int price = calculatePrice(seller, indexOfSellableObject);
				int quantity = calculateQuantity(buyer, seller, indexOfSellableObject);
				int itemId = seller.getProperty(Constants.INVENTORY).get(indexOfSellableObject).getProperty(Constants.ITEM_ID).ordinal();
				return new OperationInfo(buyer, seller, new int[] { indexOfSellableObject, price, quantity, itemId }, Actions.BUY_ACTION);
			}
		}

		return null;
	}
    
    static int calculateQuantity(WorldObject buyer, WorldObject seller, int indexOfSellableObject) {
    	WorldObject sellableWorldObject = seller.getProperty(Constants.INVENTORY).get(indexOfSellableObject);
		int sellableItemQuantity = sellableWorldObject.getProperty(Constants.QUANTITY);
    	
		int price = BuySellUtils.calculatePrice(seller, indexOfSellableObject);
		int buyerGold = buyer.getProperty(Constants.GOLD);
		int buyableItemQuantity = buyerGold / price;
		
		return Math.min(sellableItemQuantity, buyableItemQuantity);
    }
    
    private static int getIndexOfSellableWorldObject(WorldObject seller, List<ManagedProperty<?>> buyingProperties) {
    	WorldObjectContainer sellerInventory = seller.getProperty(Constants.INVENTORY);
		for(ManagedProperty<?> buyingProperty : buyingProperties) {
			for(int i=0; i<sellerInventory.size(); i++) {
				WorldObject sellableWorldObject = sellerInventory.get(i);
				if (sellableWorldObject != null && sellableWorldObject.hasProperty(buyingProperty)) {
					return i;
				}
			}
		}
		return -1;
	}
    
    public static List<WorldObject> getSellableWorldObjects(WorldObject worldObject) {
    	WorldObjectContainer inventory = worldObject.getProperty(Constants.INVENTORY);
    	return inventory.getWorldObjectsByFunction(Constants.SELLABLE, w -> w.getProperty(Constants.SELLABLE));
    }
}
