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

import org.junit.Test;
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.history.Turn;

public class UTestStandStillToTalkGoal {

	private StandStillToTalkGoal goal = Goals.STAND_STILL_TO_TALK_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalTargetTalked() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		performer.setProperty(Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = createPerformer(3);
		world.addWorldObject(target);
		
		world.getHistory().actionPerformed(new OperationInfo(target, performer, Args.EMPTY, Actions.TALK_ACTION), new Turn());
		
		assertEquals(Actions.STAND_STILL_TO_TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
	}
	
	@Test
	public void testIsGoalMetMovedLastTurn() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(true, goal.isGoalMet(performer, world));
		
		world.getHistory().actionPerformed(new OperationInfo(performer, performer, Args.EMPTY, Actions.MOVE_ACTION), new Turn());
		assertEquals(true, goal.isGoalMet(performer, world));
	}
	
	@Test
	public void testIsGoalMetMovedLastTurnAndTalked() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		world.getHistory().actionPerformed(new OperationInfo(performer, performer, Args.EMPTY, Actions.MOVE_ACTION), new Turn());
		
		WorldObject target = createPerformer(3);
		world.addWorldObject(target);
		world.getHistory().actionPerformed(new OperationInfo(target, performer, Args.EMPTY, Actions.TALK_ACTION), new Turn());
		assertEquals(false, goal.isGoalMet(performer, world));
	}

	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.HOUSES, new IdList());
		return performer;
	}
}