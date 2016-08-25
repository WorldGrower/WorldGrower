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

import java.util.Arrays;

import org.junit.Test;
import org.worldgrower.AssertUtils;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.SecludedAction;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.conversation.Conversations;

public class UTestSwindleMoneyGoal {

	private SwindleMoneyGoal goal = Goals.SWINDLE_MONEY_GOAL;
	
	@Test
	public void testCalculateGoalNull() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = createPerformer(2);
		
		assertEquals(null, goal.calculateGoal(performer, world));
	}
	
	@Test
	public void testCalculateGoalDisguise() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		world.addWorldObject(target);
		WorldObject targetMate = createPerformer(4);
		world.addWorldObject(targetMate);
		
		target.setProperty(Constants.MATE_ID, 4);
		targetMate.setProperty(Constants.MATE_ID, 3);
		
		assertEquals(new SecludedAction(Actions.DISGUISE_MAGIC_SPELL_ACTION), goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(4, goal.calculateGoal(performer, world).getArgs()[0]);
	}
	
	@Test
	public void testCalculateGoalAlreadySecluded() {
		World world = new WorldImpl(20, 20, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		target.setProperty(Constants.X, 19);
		target.setProperty(Constants.Y, 19);
		world.addWorldObject(target);
		WorldObject targetMate = createPerformer(4);
		targetMate.setProperty(Constants.X, 19);
		targetMate.setProperty(Constants.Y, 19);
		world.addWorldObject(targetMate);
		
		target.setProperty(Constants.MATE_ID, 4);
		targetMate.setProperty(Constants.MATE_ID, 3);
		
		assertEquals(Actions.DISGUISE_MAGIC_SPELL_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		assertEquals(4, goal.calculateGoal(performer, world).getArgs()[0]);
	}

	@Test
	public void testCalculateGoalDemandMoney() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		world.addWorldObject(target);
		WorldObject targetMate = createPerformer(4);
		world.addWorldObject(targetMate);
		
		target.setProperty(Constants.MATE_ID, 4);
		targetMate.setProperty(Constants.MATE_ID, 3);
		
		FacadeUtils.disguise(performer, 4, world);

		assertEquals(Actions.TALK_ACTION, goal.calculateGoal(performer, world).getManagedOperation());
		AssertUtils.assertConversation(goal.calculateGoal(performer, world), Conversations.DEMAND_MONEY_CONVERSATION);
		assertEquals(target, goal.calculateGoal(performer, world).getTarget());
	}

	@Test
	public void testIsGoalMet() {
		World world = new WorldImpl(10, 10, null, null);
		WorldObject performer = createPerformer(2);
		
		performer.setProperty(Constants.GOLD, 1000);
		assertEquals(true, goal.isGoalMet(performer, world));
		
		performer.setProperty(Constants.GOLD, 0);
		assertEquals(false, goal.isGoalMet(performer, world));
	}

	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.KNOWN_SPELLS, Arrays.asList(Actions.DISGUISE_MAGIC_SPELL_ACTION));
		return performer;
	}
}