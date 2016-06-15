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
import org.worldgrower.ManagedOperation;
import org.worldgrower.OperationInfo;
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.SecludedAction;
import org.worldgrower.creaturetype.CreatureType;

public class FindSecludedLocationGoal implements Goal {

	private static final int RANGE = 10;
	
	private final int[] args;
	private final SecludedAction secludedAction;
	
	public FindSecludedLocationGoal(int[] args, ManagedOperation managedOperation) {
		this.args = args;
		this.secludedAction = new SecludedAction(managedOperation);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		return new OperationInfo(performer, performer, args, secludedAction);
	}

	public static List<WorldObject> getTargetsWithinRange(WorldObject performer, World world) {
		return GoalUtils.findNearestTargetsByProperty(performer, Actions.MELEE_ATTACK_ACTION, Constants.STRENGTH, w -> isSomeoneWithinRange(performer, w, world), world);
	}

	
	private static boolean isSomeoneWithinRange(WorldObject performer, WorldObject w, World world) {
		return w.hasProperty(Constants.CREATURE_TYPE) && w.getProperty(Constants.CREATURE_TYPE) == CreatureType.HUMAN_CREATURE_TYPE && !w.equals(performer) && Reach.distance(performer, w) < RANGE;
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		return getTargetsWithinRange(performer, world).size() == 0;
	}

	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "finding a secluded locationr";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return 0;
	}
}
