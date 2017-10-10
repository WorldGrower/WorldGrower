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
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.profession.Profession;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class ProtectOneSelfGoal implements Goal {

	private static final int RANGE = 10;
	
	public ProtectOneSelfGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (avoidsEnemies(performer)) {
			List<WorldObject> targets = GoalUtils.findNearestTargetsByProperty(performer, Actions.MELEE_ATTACK_ACTION, Constants.STRENGTH, w -> isEnemyWithinRange(performer, w, world), world);
			
			if (targets.size() > 0) {
				MoveArgsResult moveArgsResult = calculateMoveArgs(performer, world, targets);
				int[] bestArgs = moveArgsResult.getMoveArgs();
				if (bestArgs != null) {
					List<WorldObject> enemiesNextToNewPosition = getEnemiesNextToNewPosition(performer, bestArgs, world);
					if (enemiesNextToNewPosition.size() == 0) {
						if (moveArgsResult.isCurrentLocationIsAsSafe()) {
							return new OperationInfo(performer, performer, Args.EMPTY, Actions.REST_ACTION);
						} else {
							return new OperationInfo(performer, performer, bestArgs, Actions.MOVE_ACTION);
						}
					} else {
						return new AttackTargetGoal(enemiesNextToNewPosition.get(0)).calculateGoal(performer, world);
					}
				}
			}
		}
		return null;
	}
	
	private List<WorldObject> getEnemiesNextToNewPosition(WorldObject performer, int[] moveArgs, World world) {
		int newX = performer.getProperty(Constants.X) + moveArgs[0];
		int newY = performer.getProperty(Constants.Y) + moveArgs[1];
		
		return GoalUtils.findNearestTargetsByProperty(performer, Actions.MELEE_ATTACK_ACTION, Constants.STRENGTH, w -> isEnemyWithinRange(performer, newX, newY, w, world, 2), world);
	}

	public MoveArgsResult calculateMoveArgs(WorldObject performer, World world, List<WorldObject> targets) {
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
		boolean currentLocationIsAsSafe = zone.value(performerX, performerY) <= lowestDangerValue;
		return new MoveArgsResult(bestArgs, currentLocationIsAsSafe);
	}
	
	private boolean isEnemyWithinRange(WorldObject performer, WorldObject w, World world) {
		return isEnemy(performer, w, world) && Reach.distance(performer, w) < RANGE;
	}
	
	private boolean isEnemyWithinRange(WorldObject performer, int newX, int newY, WorldObject w, World world, int range) {
		int wX = w.getProperty(Constants.X);
		int wY = w.getProperty(Constants.Y);
		return isEnemy(performer, w, world) && Reach.distance(newX, newY, wX, wY) < range;
	}
	
	private boolean isEnemy(WorldObject performer, WorldObject w, World world) {
		if (performer.getProperty(Constants.CREATURE_TYPE).isCattle()) {
			return performer.getProperty(Constants.ANIMAL_ENEMIES).contains(w);
		} else if (w.getProperty(Constants.CREATURE_TYPE).isCattle()) {
			return false;
		} else {
			return isIntelligentCharacterEnemy(performer, w, world);
		}
	}

	private boolean isIntelligentCharacterEnemy(WorldObject performer, WorldObject w, World world) {
		WorldObject villagersOrganization = GroupPropertyUtils.getVillagersOrganization(world);
		if (performer.getProperty(Constants.GROUP).contains(villagersOrganization)) {
			boolean isEnemy = GroupPropertyUtils.isWorldObjectPotentialEnemy(performer, w);
			if (isEnemy) {
				if (worldObjectIsInJail(w, world)) {
					isEnemy = false;
				}
			}
			return isEnemy;
		} else {
			return performer.getProperty(Constants.RELATIONSHIPS).getValue(w) < 0;
		}
	}
	
	private boolean worldObjectIsInJail(WorldObject w, World world) {
		return (BuildingGenerator.isPrisonerInJail(w, world));
	}

	private int[] createArgs(int performerX, int performerY, int x, int y) {
		return new int[]{ x - performerX, y - performerY };
	}
	
	private boolean movementIsPossible(WorldObject performer, int x, int y, World world) {
		int performerX = performer.getProperty(Constants.X);
		int performerY = performer.getProperty(Constants.Y);
		return new OperationInfo(performer, performer, createArgs(performerX, performerY, x, y), Actions.MOVE_ACTION).canExecute(performer, world);
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
		List<WorldObject> worldObjects = GoalUtils.findNearestTargetsByProperty(performer, Actions.MELEE_ATTACK_ACTION, Constants.STRENGTH, w -> isEnemyWithinRange(performer, w, world), world);
		return worldObjects.isEmpty();
	}

	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public FormattableText getDescription() {
		return new FormattableText(TextId.GOAL_PROTECT_ONE_SELF);
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return performer.getProperty(Constants.HIT_POINTS);
	}
}
