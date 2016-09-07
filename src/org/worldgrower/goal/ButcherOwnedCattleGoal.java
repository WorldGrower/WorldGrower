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
import java.util.stream.Collectors;

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.FoodPropertyUtils;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.CowOnTurn;

public class ButcherOwnedCattleGoal implements Goal {

	public ButcherOwnedCattleGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		List<WorldObject> ownedCattle = getOwnedCattle(performer, world);
		int ownedCattleCount = ownedCattle.size();
		int maleOwnedCattleCount = getMaleCattleCount(ownedCattle);
		int femaleOwnedCattleCount = ownedCattleCount - maleOwnedCattleCount;
		if (maleOwnedCattleCount > 2 && femaleOwnedCattleCount > 2) {
			List<WorldObject> fullyGrownCattle = getFullyGrownCattle(ownedCattle);
			if (fullyGrownCattle.size() > 0) {
				WorldObject target = fullyGrownCattle.get(0);
				return new OperationInfo(performer, target, Args.EMPTY, Actions.BUTCHER_ACTION);
			}
		}
		return null;
	}

	private int getMaleCattleCount(List<WorldObject> ownedCattle) {
		return (int) FoodPropertyUtils.getMaleCattle(ownedCattle).size();
	}

	private List<WorldObject> getFullyGrownCattle(List<WorldObject> ownedCattle) {
		return ownedCattle.stream().filter(w -> CowOnTurn.cowIsFullyGrown(w)).collect(Collectors.toList());
	}

	private List<WorldObject> getOwnedCattle(WorldObject performer, World world) {
		return GoalUtils.findNearestTargetsByProperty(performer, Actions.BUTCHER_ACTION, Constants.MEAT_SOURCE, w -> isOwnedCattle(performer, w), world);
	}

	private static boolean isOwnedCattle(WorldObject performer, WorldObject w) {
		return w.getProperty(Constants.CATTLE_OWNER_ID) != null
				&& w.getProperty(Constants.CATTLE_OWNER_ID) == performer.getProperty(Constants.ID).intValue();
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		WorldObjectContainer performerInventory = performer.getProperty(Constants.INVENTORY);
		return performerInventory.getQuantityFor(Constants.FOOD) > 15;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "butchering cattle";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
