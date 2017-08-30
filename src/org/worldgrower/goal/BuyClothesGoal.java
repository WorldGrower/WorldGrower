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
import org.worldgrower.attribute.IntProperty;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class BuyClothesGoal implements Goal {

	private static final int QUANTITY_TO_BUY = 1;
	
	public BuyClothesGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY); 
		boolean hasShirt = hasShirt(inventory);
		boolean hasPants = hasPants(inventory);
		boolean hasBoots = hasBoots(inventory);
		
		BuyTargets buyTargets = getTargetsToBuyFrom(performer, world, hasShirt, hasPants, hasBoots);
		
		if (buyTargets != null) {
			int targetInventoryIndex = buyTargets.getTargetInventoryIndex();
			if (targetInventoryIndex != -1) {
				WorldObject target = buyTargets.getTargets().get(0);
				WorldObject targetWorldObjectToBuy = target.getProperty(Constants.INVENTORY).get(targetInventoryIndex);
				return BuySellUtils.create(performer, target, targetWorldObjectToBuy.getProperty(Constants.ITEM_ID), QUANTITY_TO_BUY, world);
			}
		}
		return null;
	}

	private boolean hasBoots(WorldObjectContainer inventory) {
		return (inventory.getIndexFor(Constants.BOOTS_LIGHT_ARMOR)) != -1;
	}

	private boolean hasPants(WorldObjectContainer inventory) {
		return (inventory.getIndexFor(Constants.PANTS_LIGHT_ARMOR)) != -1;
	}

	private boolean hasShirt(WorldObjectContainer inventory) {
		return (inventory.getIndexFor(Constants.SHIRT_LIGHT_ARMOR)) != -1;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY); 
		boolean hasShirt = hasShirt(inventory);
		boolean hasPants = hasPants(inventory);
		boolean hasBoots = hasBoots(inventory);
		
		defaultGoalMetOrNot(performer, world, hasShirt, Constants.SHIRT_LIGHT_ARMOR);
		defaultGoalMetOrNot(performer, world, hasPants, Constants.PANTS_LIGHT_ARMOR);
		defaultGoalMetOrNot(performer, world, hasBoots, Constants.BOOTS_LIGHT_ARMOR);
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY); 
		boolean hasShirt = hasShirt(inventory);
		boolean hasPants = hasPants(inventory);
		boolean hasBoots = hasBoots(inventory);
		
		return (hasShirt && hasPants && hasBoots);
	}
	
	private BuyTargets getTargetsToBuyFrom(WorldObject performer, World world, boolean hasShirt, boolean hasPants, boolean hasBoots) {
		BuyTargets buyTargets;
		if (!hasShirt) {
			buyTargets = getBuyTargets(performer, Constants.SHIRT_LIGHT_ARMOR, world);
			if (buyTargets.hasTargets()) { return buyTargets; }
		}
		
		if (!hasPants) {
			buyTargets = getBuyTargets(performer, Constants.PANTS_LIGHT_ARMOR, world);
			if (buyTargets.hasTargets()) { return buyTargets; }
		}
		
		if (!hasBoots) {
			buyTargets = getBuyTargets(performer, Constants.BOOTS_LIGHT_ARMOR, world);
			if (buyTargets.hasTargets()) { return buyTargets; }
		}
		
		return null;
	}

	private BuyTargets getBuyTargets(WorldObject performer, IntProperty property, World world) {
		List<WorldObject> targets = BuySellUtils.findBuyTargets(performer, property, QUANTITY_TO_BUY, world);
		return new BuyTargets(property, targets);
	}
	
	private static class BuyTargets {
		private final IntProperty property;
		private final List<WorldObject> targets;
		
		public BuyTargets(IntProperty property, List<WorldObject> targets) {
			super();
			this.property = property;
			this.targets = targets;
		}

		public IntProperty getProperty() {
			return property;
		}

		public List<WorldObject> getTargets() {
			return targets;
		}

		public int getTargetInventoryIndex() {
			return BuySellUtils.getIndexFor(targets.get(0), property);
		}
		
		public boolean hasTargets() {
			return targets.size() > 0;
		}
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_BUY_CLOTHES);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY); 
		return inventory.getQuantityFor(Constants.ARMOR);
	}
}
