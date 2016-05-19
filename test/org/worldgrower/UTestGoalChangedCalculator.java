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

import org.junit.Test;
import org.worldgrower.goal.Goal;

public class UTestGoalChangedCalculator {

	@Test
	public void testGoalAffectedUnintelligentWorldObjects() {
		GoalObstructedHandlerImpl goalObstructedHandler = new GoalObstructedHandlerImpl();
		GoalChangedCalculator goalChangedCalculator = new GoalChangedCalculator(goalObstructedHandler);
		WorldObject t1 = TestUtils.createWorldObject(0, "Test1");
		WorldObject t2 = TestUtils.createWorldObject(0, "Test2");
		World world = new WorldImpl(10, 10, null, null);
		
		goalChangedCalculator.recordStartState(t1, t2, world);
		goalChangedCalculator.recordEndState(t1, t2, null, null, world);
		assertEquals(false, goalObstructedHandler.isGoalHindered());
	}

	private static class GoalObstructedHandlerImpl implements GoalObstructedHandler {

		private boolean goalHindered = false;
		
		@Override
		public void goalHindered(Goal obstructedGoal, WorldObject performer, WorldObject target, int stepsUntilLastGoal, int goalEvaluationDecrease, WorldObject actionTarget, ManagedOperation managedOperation, int[] args, World world) {
			goalHindered = true;
		}

		public boolean isGoalHindered() {
			return goalHindered;
		}

		@Override
		public void checkLegality(WorldObject performer, WorldObject target, ManagedOperation managedOperation, int[] args, World world) {
			
		}
	}
}
