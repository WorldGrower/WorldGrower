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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.TaskCalculator;
import org.worldgrower.TaskCalculatorImpl;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.SecludedAction;
import org.worldgrower.attribute.WorldObjectContainer;

public class UTestFindSecludedLocationGoal {

	private FindSecludedLocationGoal goal = new FindSecludedLocationGoal(Args.EMPTY, Actions.REST_ACTION);
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		world.addWorldObject(performer);
		
		assertEquals(new SecludedAction(Actions.REST_ACTION), goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testCalculateGoalOther() {
		World world = new WorldImpl(15, 15, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		world.addWorldObject(target);
		
		assertEquals(new SecludedAction(Actions.REST_ACTION), goal.calculateGoal(performer, world).getManagedOperation());
		
		TaskCalculator taskCalculator = new TaskCalculatorImpl();
		List<OperationInfo> tasks = taskCalculator.calculateTask(performer, world, goal.calculateGoal(performer, world));
		assertEquals(11, tasks.size());
		assertEquals(Actions.MOVE_ACTION, tasks.get(0).getManagedOperation());
		assertEquals(0, tasks.get(0).getArgs()[0]);
		assertEquals(1, tasks.get(0).getArgs()[1]);
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		world.addWorldObject(performer);
		
		assertEquals(true, goal.isGoalMet(performer, world));
		
		WorldObject target = createPerformer(3);
		world.addWorldObject(target);
		assertEquals(false, goal.isGoalMet(performer, world));
	}

	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		return performer;
	}
}