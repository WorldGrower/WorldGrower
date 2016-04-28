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
import org.worldgrower.AssertUtils;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.conversation.Conversations;

public class UTestArenaFightGoal {

	private ArenaFightGoal goal = Goals.ARENA_FIGHT_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalBecomeArenaFighter() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		world.addWorldObject(performer);
		
		WorldObject arenaOwner = createPerformer(3);
		arenaOwner.setProperty(Constants.ARENA_IDS, new IdList().add(7));
		arenaOwner.setProperty(Constants.ARENA_FIGHTER_IDS, new IdList());
		world.addWorldObject(arenaOwner);
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		AssertUtils.assertConversation(goal.calculateGoal(performer, world), Conversations.BECOME_ARENA_FIGHTER_CONVERSATION);
	}
	
	@Test
	public void testCalculateGoalStartArenaFight() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		world.addWorldObject(performer);
		
		WorldObject target = createPerformer(3);
		world.addWorldObject(target);
		
		WorldObject arenaOwner = createPerformer(4);
		arenaOwner.setProperty(Constants.ARENA_IDS, new IdList().add(7));
		arenaOwner.setProperty(Constants.ARENA_FIGHTER_IDS, new IdList().add(2).add(3));
		world.addWorldObject(arenaOwner);
		
		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		AssertUtils.assertConversation(goal.calculateGoal(performer, world), Conversations.START_ARENA_FIGHT_CONVERSATION);
	}
	
	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(false, goal.isGoalMet(performer, world));
		
		performer.setProperty(Constants.ARENA_OPPONENT_ID, -1);
		assertEquals(true, goal.isGoalMet(performer, world));
		
		performer.setProperty(Constants.ARENA_OPPONENT_ID, 3);
		assertEquals(true, goal.isGoalMet(performer, world));
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