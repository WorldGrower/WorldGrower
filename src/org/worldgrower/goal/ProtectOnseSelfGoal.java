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
import org.worldgrower.Reach;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.profession.Profession;

public class ProtectOnseSelfGoal implements Goal {

	public ProtectOnseSelfGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (avoidsEnemies(performer)) {
			List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.MELEE_ATTACK_ACTION, w -> GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w) && Reach.distance(performer, w) < 6, world);
			
			if (targets.size() > 0) {
				Zone zone = new Zone(world.getWidth(), world.getHeight());
				zone.addValues(targets, 5, 1);
				
				int lowestDangerValue = Integer.MAX_VALUE;
				int performerX = performer.getProperty(Constants.X);
				int performerY = performer.getProperty(Constants.Y);
				int[] bestArgs = null;
				for(int x : zone.getValuesX(performerX)) {
					for(int y : zone.getValuesY(performerY)) {
						if ((zone.value(x, y) < lowestDangerValue) && (x != 0) && (y != 0)) {
							bestArgs = new int[]{ x - performerX, y - performerY };
						}
					}
				}
				
				if (bestArgs != null) {
					return new OperationInfo(performer, performer, bestArgs, Actions.MOVE_ACTION);
				}
			}
		}
		return null;
	}
	
	private boolean avoidsEnemies(WorldObject performer) {
		Profession profession = performer.getProperty(Constants.PROFESSION);
		if (profession != null) {
			return profession.avoidEnemies();
		} else {
			return true;
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		List<WorldObject> worldObjects = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> isEnemyWithinReach(performer, w));
		return worldObjects.isEmpty();
	}

	private boolean isEnemyWithinReach(WorldObject performer, WorldObject w) {
		return GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w) && Reach.distance(performer, w) < 10;
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "avoiding danger";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.HIT_POINTS);
	}
}
