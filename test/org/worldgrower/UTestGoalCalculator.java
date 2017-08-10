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
package org.worldgrower;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.worldgrower.actions.Actions;
import org.worldgrower.goal.Goal;
import org.worldgrower.text.FormattableText;

public class UTestGoalCalculator {

	private final GoalCalculator goalCalculator = new GoalCalculator();
	
	@Test
	public void testCalculateGoal() {
		TestWorldObjectPriorities worldObjectPriorities = new TestWorldObjectPriorities(Arrays.asList(new TestGoal()));
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.FOOD, 1000, worldObjectPriorities);
		World world = new WorldImpl(10, 10, null, null);
		Set<Goal> triedGoals = new HashSet<>();
		
		GoalAndOperationInfo goalAndOperationInfo = goalCalculator.calculateGoal(performer, world, triedGoals);
		assertEquals(TestGoal.class, goalAndOperationInfo.getGoal().getClass());
	}
	
	@Test
	public void testCalculateGoalTriedGoals() {
		TestGoal goal1 = new TestGoal();
		TestGoal goal2 = new TestGoal();
		TestWorldObjectPriorities worldObjectPriorities = new TestWorldObjectPriorities(Arrays.asList(goal1, goal2));
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.FOOD, 1000, worldObjectPriorities);
		World world = new WorldImpl(10, 10, null, null);
		Set<Goal> triedGoals = new HashSet<>();
		
		GoalAndOperationInfo goalAndOperationInfo = goalCalculator.calculateGoal(performer, world, triedGoals);
		assertEquals(goal1, goalAndOperationInfo.getGoal());
		
		triedGoals.add(goal1);		
		goalAndOperationInfo = goalCalculator.calculateGoal(performer, world, triedGoals);
		assertEquals(goal2, goalAndOperationInfo.getGoal());
	}
	
	@Test
	public void testMoreUrgentImportantGoalIsNotMet() {
		TestWorldObjectPriorities worldObjectPriorities = new TestWorldObjectPriorities(Arrays.asList(new TestGoal()));
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.FOOD, 1000, worldObjectPriorities);
		World world = new WorldImpl(10, 10, null, null);
		Goal currentGoal = new TestGoal();
		
		assertEquals(true, goalCalculator.moreUrgentImportantGoalIsNotMet(performer, world, currentGoal));
	}
	
	private static class TestGoal implements Goal {

		@Override
		public OperationInfo calculateGoal(WorldObject performer, World world) {
			return new OperationInfo(performer, performer, Args.EMPTY, Actions.CUT_WOOD_ACTION);
		}

		@Override
		public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
		}

		
		@Override
		public boolean isGoalMet(WorldObject performer, World world) {
			return false;
		}

		@Override
		public boolean isUrgentGoalMet(WorldObject performer, World world) {
			return false;
		}
		
		@Override
		public FormattableText getDescription() {
			return null;
		}

		@Override
		public int evaluate(WorldObject performer, World world) {
			return 0;
		}
	}
}
