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
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.StringProperty;

public class BuySellUtils {

	public static List<WorldObject> findBuyTargets(WorldObject performer, IntProperty property, World world) {
		List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.BUY_ACTION, w -> w.getProperty(Constants.INVENTORY).getQuantityFor(property, Constants.PRICE, inventoryItem -> inventoryItem.getProperty(Constants.SELLABLE)) > 0, world);
		return targets;
	}

	public static List<WorldObject> findBuyTargets(WorldObject performer, StringProperty property, String value, World world) {
		List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.BUY_ACTION, w -> w.getProperty(Constants.INVENTORY).getIndexFor(property, value, inventoryItem -> inventoryItem.hasProperty(Constants.PRICE) && inventoryItem.getProperty(Constants.SELLABLE)) > 0, world);
		return targets;
	}
	
	public static int getPrice(WorldObject performer, int inventoryIndex) {
		WorldObject inventoryItem = getInventoryItem(performer, inventoryIndex);
		return getPrice(performer, inventoryItem);
	}

	public static int getPrice(WorldObject performer, WorldObject worldObject) {
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
		boolean demandsGoods = getDemandGoods(performer, target, worldObject);
		
		int price = BuySellUtils.getPrice(performer, worldObject);
		/*boolean betterPriceExists = betterPriceExists(worldObject, world, price);*/
		
		boolean hasMoneyToBuyGoods = (price <= target.getProperty(Constants.GOLD));
		
		return demandsGoods && /*!betterPriceExists &&*/ hasMoneyToBuyGoods;
	}

	//TODO: fix demands
	private static boolean getDemandGoods(WorldObject performer, WorldObject target, WorldObject worldObject) {
		List<ManagedProperty<?>> propertyKeys = worldObject.getPropertyKeys();
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
}
