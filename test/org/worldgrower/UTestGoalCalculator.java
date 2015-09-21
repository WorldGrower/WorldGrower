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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.GoalAndOperationInfo;
import org.worldgrower.GoalCalculator;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.goal.Goal;

public class UTestGoalCalculator {

	private final GoalCalculator goalCalculator = new GoalCalculator();
	
	@Test
	public void testCalculateGoal() {
		TestWorldObjectPriorities worldObjectPriorities = new TestWorldObjectPriorities(Arrays.asList(new TestGoal()));
		WorldObject performer = TestUtils.createIntelligentWorldObject(0, Constants.FOOD, 1000, worldObjectPriorities);
		World world = new WorldImpl(10, 10, null, null);
		List<Goal> triedGoals = new ArrayList<>();
		
		GoalAndOperationInfo goalAndOperationInfo = goalCalculator.calculateGoal(performer, world, triedGoals);
		assertEquals(TestGoal.class, goalAndOperationInfo.getGoal().getClass());
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
			return new OperationInfo(performer, performer, new int[0], Actions.CUT_WOOD_ACTION);
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
		public String getDescription() {
			return null;
		}

		@Override
		public int evaluate(WorldObject performer, World world) {
			return 0;
		}
	}
}
