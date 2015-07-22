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
import org.worldgrower.attribute.WorldObjectContainer;

public class BuySellUtils {

	public static List<WorldObject> findBuyTargets(WorldObject performer, IntProperty property, World world) {
		List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.BUY_ACTION, w -> w.getProperty(Constants.INVENTORY).getQuantityFor(property, Constants.PRICE, inventoryItem -> inventoryItem.getProperty(Constants.SELLABLE)) > 0, world);
		return targets;
	}
	
	public static int getPrice(WorldObject performer, int inventoryIndex) {
		int price = performer.getProperty(Constants.INVENTORY).get(inventoryIndex).getProperty(Constants.PRICE);
		int profitPercentage = performer.getProperty(Constants.PROFIT_PERCENTAGE);
		price = price + ((price * profitPercentage) / 100);
		return price;
	}
	
	public static boolean worldObjectWillBuyGoods(WorldObject performer, WorldObject worldObject, int indexOfItemsToSell, World world) {
		boolean demandsGoods = getDemandGoods(performer, worldObject, indexOfItemsToSell);
		
		int price = BuySellUtils.getPrice(performer, indexOfItemsToSell);
		boolean betterPriceExists = betterPriceExists(indexOfItemsToSell, world, price);
		
		boolean hasMoneyToBuyGoods = (price <= worldObject.getProperty(Constants.GOLD));
		
		return demandsGoods && !betterPriceExists && hasMoneyToBuyGoods;
	}

	//TODO: fix demands
	private static boolean getDemandGoods(WorldObject performer, WorldObject worldObject, int indexOfItemsToSell) {
		List<ManagedProperty<?>> propertyKeys = performer.getProperty(Constants.INVENTORY).get(indexOfItemsToSell).getPropertyKeys();
		boolean demandsGoods = false;
		for(ManagedProperty<?> property : propertyKeys) {
			demandsGoods = demandsGoods || worldObject.hasProperty(Constants.DEMANDS) && worldObject.getProperty(Constants.DEMANDS).count(property) > 0;
		}
		return demandsGoods;
	}

	private static boolean betterPriceExists(int indexOfPropertyToSell, World world, int price) {
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
	}
}
