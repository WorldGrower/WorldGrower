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

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.BuildBreweryAction;
import org.worldgrower.attribute.BuildingType;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.history.HistoryItem;

public class TradeGoal implements Goal {

	public TradeGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> targets = GoalUtils.findNearestTargetsByProperty(performer, Actions.BUY_ACTION, Constants.STRENGTH, w -> isTradeTarget(performer, w, world), world);
		
		List<WorldObject> unownedBreweries = BuildingGenerator.findUnownedBuildingsForClaiming(performer, Constants.BREWERY_QUALITY, w -> BuildingGenerator.isBrewery(w), world);
		if (unownedBreweries.size() > 0) {
			return new OperationInfo(performer, unownedBreweries.get(0), Args.EMPTY, Actions.CLAIM_BUILDING_ACTION);
		} else if (!BuildBreweryAction.hasEnoughStone(performer)) {
			return Goals.STONE_GOAL.calculateGoal(performer, world);
		} else if (!BuildBreweryAction.hasEnoughWood(performer)) {
			return Goals.WOOD_GOAL.calculateGoal(performer, world);
		} else {
			WorldObject target = BuildLocationUtils.findOpenLocationNearExistingProperty(performer, 4, 3, world);
			if (target != null) {
				return new OperationInfo(performer, target, Args.EMPTY, Actions.BUILD_BREWERY_ACTION);
			} else {
				return null;
			}
		}
	}
	
	private boolean isTradeTarget(WorldObject performer, WorldObject target, World world) {
		List<HistoryItem> buyHistoryItems = world.getHistory().findHistoryItems(performer, target, Actions.BUY_ACTION);
		List<HistoryItem> sellHistoryItems = world.getHistory().findHistoryItems(performer, target, Actions.SELL_ACTION);
		
		List<WorldObject> performerSellableWorldObjects = BuySellUtils.getSellableWorldObjects(performer);
		List<ManagedProperty<?>> targetBuyingProperties = BuySellUtils.getBuyingProperties(target);
		WorldObject sellableObject = getSellableWorldObject(performerSellableWorldObjects, targetBuyingProperties);
		if (sellableObject != null) {
			return true;
		}
		
		List<WorldObject> targetSellableWorldObjects = BuySellUtils.getSellableWorldObjects(target);
		List<ManagedProperty<?>> performerBuyingProperties = BuySellUtils.getBuyingProperties(target);
		WorldObject buyableObject = getSellableWorldObject(targetSellableWorldObjects, performerBuyingProperties);
		if (buyableObject != null) {
			return true;
		}
		
		return false;
	}

	private WorldObject getSellableWorldObject(List<WorldObject> sellableWorldObjects, List<ManagedProperty<?>> buyingProperties) {
		WorldObject sellableObject = null;
		for(ManagedProperty<?> targetBuyingProperty : buyingProperties) {
			for(WorldObject performerSellableWorldObject : sellableWorldObjects) {
				if (performerSellableWorldObject.hasProperty(targetBuyingProperty)) {
					sellableObject = performerSellableWorldObject;
				}
			}
		}
		return sellableObject;
	}

	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		Integer breweryId = BuildingGenerator.getBreweryId(performer);
		if (breweryId != null) {
			WorldObject brewery = world.findWorldObjectById(breweryId.intValue());
			return (brewery.getProperty(Constants.BREWERY_QUALITY) > 0);
		}
		return false;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "trading";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}