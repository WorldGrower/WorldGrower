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

	private static final int RANGE = 10;
	
	public ProtectOnseSelfGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (avoidsEnemies(performer)) {
			List<WorldObject> targets = GoalUtils.findNearestTargets(performer, Actions.MELEE_ATTACK_ACTION, w -> isEnemyWithinRange(performer, w, world), world);
			
			if (targets.size() > 0) {
				int[] bestArgs = calculateMoveArgs(performer, world, targets);
				if (bestArgs != null) {
					return new OperationInfo(performer, performer, bestArgs, Actions.MOVE_ACTION);
				}
			}
		}
		return null;
	}

	public int[] calculateMoveArgs(WorldObject performer, World world, List<WorldObject> targets) {
		Zone zone = new Zone(world.getWidth(), world.getHeight());
		for(int i=1; i<RANGE; i++) {
			zone.addValues(targets, i, 1);
		}
		
		int lowestDangerValue = Integer.MAX_VALUE;
		int performerX = performer.getProperty(Constants.X);
		int performerY = performer.getProperty(Constants.Y);
		int[] bestArgs = null;
		for(int x : zone.getValuesX(performerX)) { // x = performerX -1 to performerX +1
			for(int y : zone.getValuesY(performerY)) { // y = performerY -1 to performerY +1
				if (!((x == performerX) && (y == performerY))) {
					//System.out.println("x=" + x + ",y="+y+",zone.value(x, y)="+zone.value(x, y));
					if ((zone.value(x, y) < lowestDangerValue) && movementIsPossible(performer, x, y, world)) {
						bestArgs = createArgs(performerX, performerY, x, y);
						lowestDangerValue = zone.value(x, y);
						//System.out.println("x=" + x + ",y="+y+",lowestDangerValue="+lowestDangerValue+",bestArgs="+bestArgs[0]+","+bestArgs[1]);
					}
				}
			}
		}
		return bestArgs;
	}

	private boolean isEnemyWithinRange(WorldObject performer, WorldObject w, World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		if (performer.getProperty(Constants.GROUP).contains(villagersOrganization)) {
			return GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w) && Reach.distance(performer, w) < RANGE;
		} else {
			return performer.getProperty(Constants.RELATIONSHIPS).getValue(w) < 0 && Reach.distance(performer, w) < RANGE;
		}
	}

	private int[] createArgs(int performerX, int performerY, int x, int y) {
		return new int[]{ x - performerX, y - performerY };
	}
	
	private boolean movementIsPossible(WorldObject performer, int x, int y, World world) {
		int performerX = performer.getProperty(Constants.X);
		int performerY = performer.getProperty(Constants.Y);
		return Actions.MOVE_ACTION.distance(performer, performer, createArgs(performerX, performerY, x, y), world) == 0;
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
		List<WorldObject> worldObjects = world.findWorldObjectsByProperty(Constants.STRENGTH, w -> isEnemyWithinRange(performer, w, world));
		return worldObjects.isEmpty();
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
