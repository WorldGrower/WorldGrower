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
import org.worldgrower.attribute.StringProperty;
import org.worldgrower.attribute.UnCheckedProperty;

public class BuySellUtils {

	public static List<WorldObject> findBuyTargets(WorldObject performer, IntProperty property, World world) {
		List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.BUY_ACTION, w -> w.getProperty(Constants.INVENTORY).getQuantityFor(property, Constants.PRICE, inventoryItem -> inventoryItem.getProperty(Constants.SELLABLE)) > 0, world);
		return targets;
	}

	public static List<WorldObject> findBuyTargets(WorldObject performer, StringProperty property, String value, World world) {
		List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.BUY_ACTION, w -> w.getProperty(Constants.INVENTORY).getIndexFor(property, value, inventoryItem -> inventoryItem.hasProperty(Constants.PRICE) && inventoryItem.getProperty(Constants.SELLABLE)) > 0, world);
		return targets;
	}
	
	private static List<WorldObject> findBuyTargets(WorldObject performer, UnCheckedProperty<UnCheckedProperty<WorldObject>> equipmentSlotProperty, UnCheckedProperty<WorldObject> equipmentSlot, World world) {
		List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.BUY_ACTION, w -> w.getProperty(Constants.INVENTORY).getIndexFor(equipmentSlotProperty, equipmentSlot, inventoryItem -> inventoryItem.hasProperty(Constants.PRICE) && inventoryItem.getProperty(Constants.SELLABLE)) > 0, world);
		return targets;
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
		int price = worldObject.getProperty(Constants.PRICE);
		int profitPercentage = performer.getProperty(Constants.PROFIT_PERCENTAGE);
		price = price + ((price * profitPercentage) / 100);
		return price;
	}

	private static WorldObject getInventoryItem(WorldObject performer, int inventoryIndex) {
		return performer.getProperty(Constants.INVENTORY).get(inventoryIndex);
	}
	
	public static boolean worldObjectWillBuyGoods(WorldObject performer, WorldObject target, int indexOfItemsToSell, World world) {
		WorldObject inventoryItem = getInventoryItem(performer, indexOfItemsToSell);
		return worldObjectWillBuyGoods(performer, target, inventoryItem, world);
	}
	
	public static boolean worldObjectWillBuyGoods(WorldObject performer, WorldObject target, WorldObject worldObject, World world) {
		boolean demandsGoods = hasDemandForInventoryItemGoods(target, worldObject);
		
		int price = BuySellUtils.getPrice(performer, worldObject);
		/*boolean betterPriceExists = betterPriceExists(worldObject, world, price);*/
		
		boolean hasMoneyToBuyGoods = (price <= target.getProperty(Constants.GOLD));
		
		return demandsGoods && /*!betterPriceExists &&*/ hasMoneyToBuyGoods;
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

	//TODO: fix
	/*private static boolean betterPriceExists(int indexOfPropertyToSell, World world, int price) {
		boolean betterPriceExists = false;
		List<WorldObject> targets = world.findWorldObjects(w -> w.hasProperty(Constants.INVENTORY) && w.getProperty(Constants.INVENTORY).getWorldObjects(Constants.SELLABLE, Boolean.TRUE).size() > 0);
		for(WorldObject target : targets) {
			WorldObjectContainer targetInventory = target.getProperty(Constants.INVENTORY);
			if (targetInventory.get(indexOfPropertyToSell).getProperty(Constants.SELLABLE)) {
				int priceTarget = getPrice(target, indexOfPropertyToSell);
				if (priceTarget < price) {
					betterPriceExists = true;
				}
			}
		}
		return betterPriceExists;
	}*/

	public static int getIndexFor(WorldObject target, StringProperty property, String value) {
		return target.getProperty(Constants.INVENTORY).getIndexFor(property, value, inventoryItem -> inventoryItem.hasProperty(Constants.PRICE) && (inventoryItem.getProperty(Constants.SELLABLE)));
	}
	
	public static OperationInfo getBuyOperationInfo(WorldObject performer, IntProperty propertyToBuy, World world) {
		List<WorldObject> targets = findBuyTargets(performer, propertyToBuy, world);
		if (targets.size() > 0) {
			WorldObject target = targets.get(0);
			int indexOfProperty = target.getProperty(Constants.INVENTORY).getIndexFor(propertyToBuy);
			if (performerCanBuyGoods(performer, target, indexOfProperty, 5)) {
				return new OperationInfo(performer, target, new int[] { indexOfProperty, 5 }, Actions.BUY_ACTION);
			}
		}
		return null;
	}
	
	public static OperationInfo getBuyOperationInfo(WorldObject performer, UnCheckedProperty<UnCheckedProperty<WorldObject>> equipmentSlotProperty, UnCheckedProperty<WorldObject> equipmentSlot, World world) {
		List<WorldObject> targets = findBuyTargets(performer, equipmentSlotProperty, equipmentSlot, world);
		if (targets.size() > 0) {
			WorldObject target = targets.get(0);
			int indexOfProperty = target.getProperty(Constants.INVENTORY).getIndexFor(equipmentSlotProperty, equipmentSlot);
			if (performerCanBuyGoods(performer, target, indexOfProperty, 1)) {
				return new OperationInfo(performer, target, new int[] { indexOfProperty, 1 }, Actions.BUY_ACTION);
			}
		}
		return null;
	}
}
