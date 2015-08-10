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

import java.util.ArrayList;
import java.util.List;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.ItemGenerator;

public class BuyClothesGoal implements Goal {

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY); 
		boolean hasCottonShirt = inventory.getIndexFor(Constants.NAME, ItemGenerator.COTTON_SHIRT_NAME) != -1;
		boolean hasCottonPants = inventory.getIndexFor(Constants.NAME, ItemGenerator.COTTON_PANTS_NAME) != -1;
		boolean hasCottonBoots = inventory.getIndexFor(Constants.NAME, ItemGenerator.COTTON_BOOTS_NAME) != -1;
		
		List<WorldObject> targets = getTargetsToBuyFrom(performer, world, hasCottonShirt, hasCottonPants, hasCottonBoots);
		
		if (!hasCottonShirt) {
			int targetInventoryIndex = BuySellUtils.getIndexFor(targets.get(0), Constants.NAME, ItemGenerator.COTTON_SHIRT_NAME);
			if (targetInventoryIndex != -1) {
				return new OperationInfo(performer, targets.get(0), new int[] { targetInventoryIndex, 1 }, Actions.BUY_ACTION);
			}
		}
		
		if (!hasCottonPants) {
			int targetInventoryIndex = BuySellUtils.getIndexFor(targets.get(0), Constants.NAME, ItemGenerator.COTTON_PANTS_NAME);
			if (targetInventoryIndex != -1) {
				return new OperationInfo(performer, targets.get(0), new int[] { targetInventoryIndex, 1 }, Actions.BUY_ACTION);
			}
		}
		
		if (!hasCottonBoots) {
			int targetInventoryIndex = BuySellUtils.getIndexFor(targets.get(0), Constants.NAME, ItemGenerator.COTTON_BOOTS_NAME);
			if (targetInventoryIndex != -1) {
				return new OperationInfo(performer, targets.get(0), new int[] { targetInventoryIndex, 1 }, Actions.BUY_ACTION);
			}
		}
		
		return null;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY); 
		boolean hasCottonShirt = inventory.getIndexFor(Constants.NAME, ItemGenerator.COTTON_SHIRT_NAME) != -1;
		boolean hasCottonPants = inventory.getIndexFor(Constants.NAME, ItemGenerator.COTTON_PANTS_NAME) != -1;
		boolean hasCottonBoots = inventory.getIndexFor(Constants.NAME, ItemGenerator.COTTON_BOOTS_NAME) != -1;
		
		List<WorldObject> targetsToBuyFrom = getTargetsToBuyFrom(performer, world, hasCottonShirt, hasCottonPants, hasCottonBoots);
		
		if (targetsToBuyFrom.size() > 0) {
			return (hasCottonShirt && hasCottonPants && hasCottonBoots);
		} else {
			return true;
		}
	}
	
	private List<WorldObject> getTargetsToBuyFrom(WorldObject performer, World world, boolean hasCottonShirt, boolean hasCottonPants, boolean hasCottonBoots) {
		if (!hasCottonShirt) {
			return BuySellUtils.findBuyTargets(performer, Constants.NAME, ItemGenerator.COTTON_SHIRT_NAME, world);
		}
		
		if (!hasCottonPants) {
			return BuySellUtils.findBuyTargets(performer, Constants.NAME, ItemGenerator.COTTON_PANTS_NAME, world);
		}
		
		if (!hasCottonBoots) {
			return BuySellUtils.findBuyTargets(performer, Constants.NAME, ItemGenerator.COTTON_BOOTS_NAME, world);
		}
		
		return new ArrayList<>();
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "buying clothes";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		WorldObjectContainer inventory = performer.getProperty(Constants.INVENTORY); 
		return inventory.getQuantityFor(Constants.ARMOR);
	}
}
