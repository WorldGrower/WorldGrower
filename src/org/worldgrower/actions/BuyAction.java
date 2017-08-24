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

public class BuyAction implements ManagedOperation, AnimatedAction {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int index = args[0];
		int price = args[1];
		int quantity = args[2];
		int itemIndex = args[3];
		
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		WorldObjectContainer targetInventory = target.getProperty(Constants.INVENTORY);
		
		int goldPaid = calculateGoldPaid(target, index, quantity);
		
		checkArgs(index, quantity, itemIndex, targetInventory);
		
		WorldObject boughtWorldObject = targetInventory.get(index).deepCopy();
		targetInventory.removeQuantity(index, quantity);
		
		boughtWorldObject.setProperty(Constants.SELLABLE, Boolean.FALSE);
		target.getProperty(Constants.ITEMS_SOLD).add(boughtWorldObject);
		
		performerInventory.addQuantity(boughtWorldObject, quantity);
		
		target.setProperty(Constants.GOLD, target.getProperty(Constants.GOLD) + goldPaid);
		performer.setProperty(Constants.GOLD, performer.getProperty(Constants.GOLD) - goldPaid);
		
		InventoryPropertyUtils.cleanupEquipmentSlots(target);
		
		String description = boughtWorldObject.getProperty(Constants.NAME);
		world.logAction(this, performer, target, args, performer.getProperty(Constants.NAME) + " bought " + quantity + " " + description + " at " + price + " gold a piece for a total of " + goldPaid + " gold");
	}

	private void checkArgs(int index, int quantity, int itemIndex, WorldObjectContainer targetInventory) {
		int targetQuantity = targetInventory.get(index).getProperty(Constants.QUANTITY).intValue();
		if (quantity > targetQuantity) {
			throw new IllegalStateException("Quantity is incorrect: quantity " + quantity + " is larger than quantity " + targetQuantity + " for index " + index + " for target inventory " + targetInventory);
		}
		
		if (Item.value(itemIndex) != targetInventory.get(index).getProperty(Constants.ITEM_ID)) {
			throw new IllegalStateException("ItemIndex is incorrect: " + itemIndex + " is different than quantity for index " + index + " for target inventory " + targetInventory);
		}
	}

	int calculateGoldPaid(WorldObject target, int index, int quantity) {
		WorldObject worldObject = target.getProperty(Constants.INVENTORY).get(index);
		if (worldObject != null) {
			return BuySellUtils.getPrice(target, worldObject) * quantity;
		} else {
			return 0;
		}
	}

	@Override
	public boolean isActionPossible(WorldObject performer, WorldObject target, int[] args, World world) {
		int index = args[0];
		int price = args[1];
		int quantity = args[2];
		
		return canPerformerBuy(performer, target, index, quantity) && targetHasSufficientQuantity(index, quantity, target);
	}
	
	private boolean targetHasSufficientQuantity(int index, int quantity, WorldObject target) {
		WorldObjectContainer targetInventory = target.getProperty(Constants.INVENTORY);
		return targetInventory.get(index).getProperty(Constants.QUANTITY) >= quantity;
	}
	
	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return Reach.evaluateTarget(performer, target, 1);
	}
	
	private boolean canPerformerBuy(WorldObject performer, WorldObject target, int index, int quantity) {
		final boolean canPerformerBuy;
		WorldObject worldObjectToBuy = target.getProperty(Constants.INVENTORY).get(index);
		if (worldObjectToBuy != null) {
			if (BuySellUtils.performerCanBuyGoods(performer, target, index, quantity)) {
				canPerformerBuy = true;
			} else {
				canPerformerBuy = false;
			}
		} else {
			canPerformerBuy = false;
		}
		return canPerformerBuy;
	}
	
	@Override
	public String getRequirementsDescription() {
		return CraftUtils.getRequirementsDescription(Constants.DISTANCE, 1);
	}
	
	@Override
	public String getDescription() {
		return "buy items in exchange for gold";
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
	public ImageIds getImageIds(WorldObject performer) {
		return ImageIds.GOLD_COIN;
	}
	
	@Override
	public SoundIds getSoundId() {
		return SoundIds.HANDLE_COINS;
	}

	@Override
	public ImageIds getAnimationImageId() {
		return ImageIds.GOLD_COIN_ANIMATION;
	}

	@Override
	public List<WorldObject> getAffectedTargets(WorldObject target, World world) {
		return Arrays.asList(target);
	}
}