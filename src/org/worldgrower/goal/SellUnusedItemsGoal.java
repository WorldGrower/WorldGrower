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

import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.generator.ItemType;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class SellUnusedItemsGoal implements Goal {

	public SellUnusedItemsGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY);
		int index = inventory.getIndexFor(inventoryItem ->
		{
			Item item = inventoryItem.getProperty(Constants.ITEM_ID);
			ItemType itemType = item.getItemType();
			return (itemType == ItemType.TOOL || itemType == ItemType.BOOK);
		});
		
		if (index != -1) {
			WorldObject inventoryItem = inventory.get(index);
			Item item = inventoryItem.getProperty(Constants.ITEM_ID);
			int price = performer.getProperty(Constants.PRICES).getPrice(item);
			int[] args = new int[] { index, price };
			return new OperationInfo(performer, performer, args, Actions.MARK_INVENTORY_ITEM_AS_SELLABLE_ACTION);
		} else {
			return null;
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return calculateGoal(performer, world) == null;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_SELL_UNUSED_ITEMS);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
