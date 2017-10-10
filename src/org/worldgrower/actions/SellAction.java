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
import java.util.Arrays;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.goal.BuySellUtils;
import org.worldgrower.goal.InventoryPropertyUtils;
import org.worldgrower.gui.ImageIds;
import org.worldgrower.gui.music.SoundIds;

public class SellAction implements ManagedOperation, AnimatedAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int index = args[0];
		int price = args[1]; 
		int quantity = args[2];
		int itemIndex = args[3];
		
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		WorldObjectContainer targetInventory = target.getProperty(Constants.INVENTORY);
		
		checkArgs(index, quantity, itemIndex, performerInventory);
		
		WorldObject soldWorldObject = performerInventory.get(index).deepCopy();
		int goldPaid = calculateGoldPaid(performer, index, quantity);
		
		soldWorldObject.setProperty(Constants.SELLABLE, Boolean.FALSE);
		performer.getProperty(Constants.ITEMS_SOLD).add(soldWorldObject);
		performerInventory.removeQuantity(index, quantity);
		
		targetInventory.addQuantity(soldWorldObject, quantity);
		target.setProperty(Constants.GOLD, target.getProperty(Constants.GOLD) - goldPaid);
		performer.setProperty(Constants.GOLD, performer.getProperty(Constants.GOLD) + goldPaid);
		
		InventoryPropertyUtils.cleanupEquipmentSlots(performer);
		
		String description = soldWorldObject.getProperty(Constants.NAME);
		world.logAction(this, performer, target, args, performer.getProperty(Constants.NAME) + " sold " + quantity + " " + description + " at " + price + " gold a piece for a total of " + goldPaid + " gold");
	}

	private void checkArgs(int index, int quantity, int itemIndex, WorldObjectContainer performerInventory) {
		int performerQuantity = performerInventory.get(index).getProperty(Constants.QUANTITY).intValue();
		if (quantity > performerQuantity) {
			throw new IllegalStateException("Quantity is incorrect: quantity " + quantity + " is larger than quantity " + performerQuantity + " for index " + index + " for performer inventory " + performerInventory);
		}
		
		if (Item.value(itemIndex) != performerInventory.get(index).getProperty(Constants.ITEM_ID)) {
			throw new IllegalStateException("ItemIndex is incorrect: " + itemIndex + " is different than quantity for index " + index + " for performer inventory " + performerInventory);
		}
	}

	int calculateGoldPaid(WorldObject performer, int index, int quantity) {
		WorldObject worldObject = performer.getProperty(Constants.INVENTORY).get(index);
		if (worldObject != null) {
			return BuySellUtils.getPrice(performer, worldObject) * quantity;
		} else {
			return 0;
		}
	}
	
	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		int index = args[0];
		int price = args[1];
		int quantity = args[2];
		
		return canPerformerSell(performer, target, index, quantity) && performerHasSufficientQuantity(index, quantity, performer);
	}
	
	private boolean canPerformerSell(WorldObject performer, WorldObject target, int index, int quantity) {
		final boolean canPerformerSell;
		WorldObject worldObjectToSell = performer.getProperty(Constants.INVENTORY).get(index);
		if (worldObjectToSell != null) {
			if (BuySellUtils.performerCanBuyGoods(target, performer, index, quantity)) {
				canPerformerSell = true;
			} else {
				canPerformerSell = false;
			}
		} else {
			canPerformerSell = false;
		}
		return canPerformerSell;
	}
	
	private boolean performerHasSufficientQuantity(int index, int quantity, WorldObject performer) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		return performerInventory.get(index).getProperty(Constants.QUANTITY) >= quantity;
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, 1);
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1);
	}
	
	@Override
	public String getDescription() {
		return "sell items in exchange for gold";
	}

	@Override
	public boolean requiresArguments() {
		return true;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (target.hasIntelligence() && target.hasProperty(Constants.INVENTORY) && !performer.equals(target) && target.getProperty(Constants.CREATURE_TYPE).canTrade());
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "selling to " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "sell";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
	
	@Override
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.SILVER_COIN;
	}
	
	@Override
	public SoundIds getSoundId(WorldObject target) {
		return SoundIds.HANDLE_COINS;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.SILVER_COIN_ANIMATION;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
}