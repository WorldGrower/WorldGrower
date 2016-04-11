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
package org.worldgrower.actions;

import java.io.ObjectStreamException;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.goal.BuySellUtils;
import org.worldgrower.goal.InventoryPropertyUtils;
import org.worldgrower.gui.ImageIds;

public class BuyAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int index = args[0];
		int price = args[1];
		int quantity = args[2];
		
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		WorldObjectContainer targetInventory = target.getProperty(Constants.INVENTORY);
		
		WorldObject boughtWorldObject = targetInventory.get(index).deepCopy();
		targetInventory.removeQuantity(index, quantity);
		
		boughtWorldObject.setProperty(Constants.SELLABLE, Boolean.FALSE);
		target.getProperty(Constants.ITEMS_SOLD).add(boughtWorldObject);
		
		performerInventory.addQuantity(boughtWorldObject, quantity);
		int goldPaid = price * quantity;
		target.setProperty(Constants.GOLD, target.getProperty(Constants.GOLD) + goldPaid);
		performer.setProperty(Constants.GOLD, performer.getProperty(Constants.GOLD) - goldPaid);
		
		InventoryPropertyUtils.cleanupEquipmentSlots(target);
		
		String description = boughtWorldObject.getProperty(Constants.NAME);
		world.logAction(this, performer, target, args, performer.getProperty(Constants.NAME) + " bought " + quantity + " " + description + " at " + price + " gold a piece for a total of " + goldPaid + " gold");
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		int index = args[0];
		int price = args[1];
		int quantity = args[2];
		
		int buyDistance = calculateBuyDistance(performer, target, index, quantity);
		return Reach.evaluateTarget(performer, args, target, 1) + buyDistance;
	}

	private int calculateBuyDistance(WorldObject performer, WorldObject target, int index, int quantity) {
		final int buyDistance;
		WorldObject worldObjectToBuy = target.getProperty(Constants.INVENTORY).get(index);
		if (worldObjectToBuy != null) {
			if (BuySellUtils.performerCanBuyGoods(performer, target, index, quantity)) {
				buyDistance = 0;
			} else {
				buyDistance = 100;
			}
		} else {
			buyDistance = 1000;
		}
		return buyDistance;
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1);
	}

	@Override
	public boolean requiresArguments() {
		return true;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (target.hasIntelligence() && target.hasProperty(Constants.INVENTORY) && target.getProperty(Constants.CREATURE_TYPE).canTrade());
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "buying from " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "buy";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public ImageIds getImageIds() {
		return ImageIds.GOLD_COIN;
	}
}