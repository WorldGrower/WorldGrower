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
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.Demands;
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.Prices;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.profession.Professions;

public class BuySellUtils {

	public static List<WorldObject> findBuyTargets(WorldObject performer, ManagedProperty<?> property, int quantity, World world) {
		return GoalUtils.findNearestTargetsByProperty(performer, Actions.BUY_ACTION, Constants.STRENGTH, w -> isBuyTarget(performer, property, quantity, w), world);
	}

	private static boolean isBuyTarget(WorldObject performer, ManagedProperty<?> property, int quantity, WorldObject w) {
		return targetHasSufficientQuantity(property, quantity, w) && buyerCanPay(performer, w, property, quantity) && !GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w);
	}

	private static boolean targetHasSufficientQuantity(ManagedProperty<?> property, int quantity, WorldObject w) {
		return w.getProperty(Constants.INVENTORY).getQuantityFor(property, Constants.PRICE, inventoryItem -> isInventoryItemSellable(inventoryItem)) >= quantity;
	}
	
	public static List<WorldObject> findBuyTargets(WorldObject performer, Item item, int quantity, World world) {
		return GoalUtils.findNearestTargetsByProperty(performer, Actions.BUY_ACTION, Constants.STRENGTH, w -> targetHasSellableItem(w, item) && buyerCanPay(performer, w, item, quantity), world);
	}
	
	private static boolean buyerCanPay(WorldObject buyer, WorldObject seller, ManagedProperty property, int quantity) {
		int indexOfItemToSell = seller.getProperty(Constants.INVENTORY).getIndexFor(property);
		return buyerCanPay(buyer, seller, quantity, indexOfItemToSell);
	}
	
	private static boolean buyerCanPay(WorldObject buyer, WorldObject seller, Item item, int quantity) {
		int indexOfItemToSell = seller.getProperty(Constants.INVENTORY).getIndexFor(Constants.ITEM_ID, item);
		return buyerCanPay(buyer, seller, quantity, indexOfItemToSell);
	}

	static boolean buyerCanPay(WorldObject buyer, WorldObject seller, int quantity, int indexOfItemToSell) {
		if (indexOfItemToSell != -1) {
			int performerGold = buyer.getProperty(Constants.GOLD);
			return performerGold >= getPrice(seller, indexOfItemToSell) * quantity;
		} else {
			return false;
		}
	}
	
	private static boolean targetHasSellableItem(WorldObject w, Item item) {
		return w.getProperty(Constants.INVENTORY).getIndexFor(Constants.ITEM_ID, item, inventoryItem -> isInventoryItemSellable(inventoryItem)) >= 0;
	}

	static boolean hasSellableEquipment(Item item, WorldObject w) {
		return w.getProperty(Constants.INVENTORY).getIndexFor(Constants.ITEM_ID, item, inventoryItem -> isInventoryItemSellable(inventoryItem)) >= 0;
	}
	
	static boolean isInventoryItemSellable(WorldObject inventoryItem) {
		Boolean isSellable = inventoryItem.getProperty(Constants.SELLABLE);
		return isSellable != null && isSellable.booleanValue();
	}
	
	public static int getPrice(WorldObject seller, int inventoryIndex) {
		WorldObject inventoryItem = getInventoryItem(seller, inventoryIndex);
		return getPrice(seller, inventoryItem);
	}

	public static int getPrice(WorldObject seller, WorldObject inventoryItem) {
		if (inventoryItem == null) {
			throw new IllegalStateException("WorldObject is null");
		}
		
		if (inventoryItem.getProperty(Constants.PRICE) == null) {
			throw new IllegalStateException("InventoryItem " + inventoryItem + " has no price");
		}
		Prices prices = seller.getProperty(Constants.PRICES);
		Item key = inventoryItem.getProperty(Constants.ITEM_ID);
		if (key != null) {
			return prices.getPrice(key);
		} else {
			//TODO: temporary for houses
			return inventoryItem.getProperty(Constants.PRICE);
		}
	}

	private static WorldObject getInventoryItem(WorldObject performer, int inventoryIndex) {
		return performer.getProperty(Constants.INVENTORY).get(inventoryIndex);
	}
	
	public static boolean buyerWillBuyGoods(WorldObject seller, WorldObject buyer, int indexOfItemsToSell, World world) {
		WorldObject inventoryItem = getInventoryItem(seller, indexOfItemsToSell);
		return buyerWillBuyGoods(seller, buyer, inventoryItem, world);
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
		int price = getPrice(target, worldObject) * quantity;
		boolean hasMoneyToBuyGoods = (price <= performer.getProperty(Constants.GOLD));
		return hasMoneyToBuyGoods;
	}

	static boolean hasDemandForInventoryItemGoods(WorldObject target, WorldObject inventoryItem) {
		List<ManagedProperty<?>> propertyKeys = inventoryItem.getPropertyKeys();
		boolean demandsGoods = false;
		if (target.hasProperty(Constants.DEMANDS)) {
			for(ManagedProperty<?> property : propertyKeys) {
				if (Constants.POSSIBLE_DEMAND_PROPERTIES.contains(property)) {
					demandsGoods = demandsGoods || hasDemandForInventoryItemGood(target, (IntProperty) property);
				}
			}
		}
		return demandsGoods;
	}
	
	private static boolean hasDemandForInventoryItemGood(WorldObject target, IntProperty property) {
		int demandCount = target.getProperty(Constants.DEMANDS).count(property);
		int inventoryCount = target.getProperty(Constants.INVENTORY).getQuantityFor(property);
		return demandCount > inventoryCount;
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

	public static int getIndexFor(WorldObject target, Item item) {
		return target.getProperty(Constants.INVENTORY).getIndexFor(Constants.ITEM_ID, item, inventoryItem -> isInventoryItemSellable(inventoryItem));
	}
	
	public static OperationInfo getBuyOperationInfo(WorldObject performer, ManagedProperty<?> propertyToBuy, int quantity, World world) {
		List<WorldObject> targets = findBuyTargets(performer, propertyToBuy, quantity, world);
		if (targets.size() > 0) {
			WorldObject target = targets.get(0);
			int indexOfProperty = target.getProperty(Constants.INVENTORY).getIndexFor(propertyToBuy);
			int price = calculatePrice(target, indexOfProperty);
			int itemId = target.getProperty(Constants.INVENTORY).get(indexOfProperty).getProperty(Constants.ITEM_ID).ordinal();
			int[] args = new int[] { indexOfProperty, price, quantity, itemId };
			if (Actions.BUY_ACTION.canExecuteIgnoringDistance(performer, target, args, world)) {				
				return new OperationInfo(performer, target, new int[] { indexOfProperty, price, quantity, itemId }, Actions.BUY_ACTION);
			}
		}
		return null;
	}
	
	public static OperationInfo getBuyOperationInfo(WorldObject performer, Item item, int quantity, World world) {
		List<WorldObject> targets = findBuyTargets(performer, item, quantity, world);
		if (targets.size() > 0) {
			WorldObject target = targets.get(0);
			int indexOfProperty = target.getProperty(Constants.INVENTORY).getIndexFor(Constants.ITEM_ID, item);
			int price = calculatePrice(target, indexOfProperty);
			int itemId = target.getProperty(Constants.INVENTORY).get(indexOfProperty).getProperty(Constants.ITEM_ID).ordinal();
			int[] args = new int[] { indexOfProperty, price, quantity, itemId };
			if (Actions.BUY_ACTION.canExecuteIgnoringDistance(performer, target, args, world)) {
				return new OperationInfo(performer, target, args, Actions.BUY_ACTION);
			}
		}
		return null;
	}

	private static int calculatePrice(WorldObject target, int indexOfProperty) {
		Item item = target.getProperty(Constants.INVENTORY).get(indexOfProperty).getProperty(Constants.ITEM_ID);
		return target.getProperty(Constants.PRICES).getPrice(item);
	}
	
	public static OperationInfo create(WorldObject performer, WorldObject target, Item item, int quantity, World world) {
		int targetInventoryIndex = target.getProperty(Constants.INVENTORY).getIndexFor(w -> w.getProperty(Constants.ITEM_ID) == item);
		int price = target.getProperty(Constants.PRICES).getPrice(item);
		int[] args = new int[] { targetInventoryIndex, price, quantity, item.ordinal() };
		if (Actions.BUY_ACTION.canExecuteIgnoringDistance(performer, target, args, world)) {
			return new OperationInfo(performer, target, args, Actions.BUY_ACTION);
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
	
	public static Demands getBuyingProperties(WorldObject worldObject) {
    	return worldObject.getProperty(Constants.DEMANDS);
    }
	
    public static boolean isSellableWorldObject(WorldObject worldObject) {
    	return worldObject.hasProperty(Constants.SELLABLE) && worldObject.getProperty(Constants.SELLABLE);
    }
    
    public static OperationInfo getSellOperationInfo(WorldObject seller, World world, int distance) {
		List<WorldObject> buyers = GoalUtils.findNearestTargetsByProperty(seller, Actions.SELL_ACTION, Constants.STRENGTH, w -> Reach.distance(seller, w) <= distance, world);
		for(WorldObject buyer : buyers) {
			Demands buyingProperties = getBuyingProperties(buyer);
			int indexOfSellableObject = getIndexOfSellableWorldObject(seller, buyingProperties);
			if (indexOfSellableObject != -1 && buyerWillBuyGoods(seller, buyer, indexOfSellableObject, world)) {
				int price = calculatePrice(seller, indexOfSellableObject);
				int quantity = calculateQuantity(buyer, seller, indexOfSellableObject);
				int itemId = seller.getProperty(Constants.INVENTORY).get(indexOfSellableObject).getProperty(Constants.ITEM_ID).ordinal();
				int[] args = new int[] { indexOfSellableObject, price, quantity, itemId };
				return new OperationInfo(seller, buyer, args, Actions.SELL_ACTION);
			}
		}

		return null;
	}
    
    public static OperationInfo getBuyOperationInfo(WorldObject buyer, Demands performerBuyingProperties, World world) {
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
    
    private static int getIndexOfSellableWorldObject(WorldObject seller, Demands buyingProperties) {
    	WorldObjectContainer sellerInventory = seller.getProperty(Constants.INVENTORY);
		for(IntProperty buyingProperty : buyingProperties.propertyKeys()) {
			int index = sellerInventory.getIndexFor(buyingProperty);
			if (index != -1) {
				return index;
			}
		}
		return -1;
	}
    
    public static List<WorldObject> getSellableWorldObjects(WorldObject worldObject) {
    	WorldObjectContainer inventory = worldObject.getProperty(Constants.INVENTORY);
    	return inventory.getWorldObjectsByFunction(Constants.SELLABLE, w -> w.getProperty(Constants.SELLABLE));
    }
}
