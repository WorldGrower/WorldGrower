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
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.legal.LegalAction;
import org.worldgrower.actions.legal.LegalActions;
import org.worldgrower.attribute.WorldObjectContainer;

public class GatherFoodGoal implements Goal {

	private final int foodQuantityGoalMet;
	
	public GatherFoodGoal() {
		this(20);
	}

	public GatherFoodGoal(int foodQuantityGoalMet) {
		this.foodQuantityGoalMet = foodQuantityGoalMet;
	}

	public GatherFoodGoal(List<Goal> allGoals) {
		this();
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		WorldObject target = GoalUtils.findNearestTarget(performer, Actions.HARVEST_FOOD_ACTION, world);
		OperationInfo butcherOperationInfo = createButcherOperationInfo(performer, world);
		int distanceToMeatSource = calculateDistanceToMeatSource(performer, butcherOperationInfo);
		int distanceToFoodSource = calculateDistanceToFoodSource(performer, target);
		
		//System.out.println("distanceToFoodSource=" + distanceToFoodSource + "distanceToMeatSource=" + distanceToMeatSource);
		if (target != null && distanceToFoodSource < 15 && distanceToFoodSource < distanceToMeatSource) {
			return new OperationInfo(performer, target, Args.EMPTY, Actions.HARVEST_FOOD_ACTION);
		} else if (butcherOperationInfo != null && distanceToMeatSource < 15 && distanceToMeatSource < distanceToFoodSource) {
			return butcherOperationInfo;
		} else {
			return Goals.CREATE_FOOD_SOURCES_GOAL.calculateGoal(performer, world);
		}
	}

	private int calculateDistanceToFoodSource(WorldObject performer, WorldObject target) {
		if (target != null) {
			return Reach.distance(performer, target);
		} else {
			return Integer.MAX_VALUE;
		}
	}

	private int calculateDistanceToMeatSource(WorldObject performer, OperationInfo butcherOperationInfo) {
		final int distanceToMeatSource;
		if (butcherOperationInfo != null) {
			distanceToMeatSource = Reach.distance(performer, butcherOperationInfo.getTarget());
		} else {
			distanceToMeatSource = Integer.MAX_VALUE;
		}
		return distanceToMeatSource;
	}
	
	private static OperationInfo createButcherOperationInfo(WorldObject performer, World world) {
		boolean isButcheringUnownedCattleAllowed = isButcheringUnownedCattleAllowed(world);
		
		final WorldObject target;
		if (isButcheringUnownedCattleAllowed) {
			target = GoalUtils.findNearestTarget(performer, Actions.BUTCHER_ACTION, world);
		} else {
			List<WorldObject> targets = GoalUtils.findNearestTargetsByProperty(performer, Actions.BUTCHER_ACTION, Constants.MEAT_SOURCE, w -> isButcherTarget(performer, w), world);
			if (targets.size() > 0) {
				target = targets.get(0);
			} else {
				target = null;
			}
		}
		if (target != null) {
			return new OperationInfo(performer, target, Args.EMPTY, Actions.BUTCHER_ACTION);
		} else {
			return null;
		}
	}

	private static boolean isButcherTarget(WorldObject performer, WorldObject w) {
		return !w.hasProperty(Constants.CATTLE_OWNER_ID) 
				|| w.getProperty(Constants.CATTLE_OWNER_ID) == null
				|| w.getProperty(Constants.CATTLE_OWNER_ID) == performer.getProperty(Constants.ID).intValue();
	}
	
	private static boolean isButcheringUnownedCattleAllowed(World world) {
		LegalActions legalActions = LegalActionsPropertyUtils.getLegalActions(world);
		return legalActions.getLegalFlag(LegalAction.BUTCHER);
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		return performerInventory.getQuantityFor(Constants.FOOD) > foodQuantityGoalMet;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		return performerInventory.getQuantityFor(Constants.FOOD) > 2;
	}

	@Override
	public String getDescription() {
		return "harvesting food";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		return performerInventory.getQuantityFor(Constants.FOOD);
	}
}
