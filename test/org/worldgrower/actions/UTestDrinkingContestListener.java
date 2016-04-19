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
package org.worldgrower.actions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.generator.Item;
import org.worldgrower.goal.DrinkingContestPropertyUtils;

public class UTestDrinkingContestListener {

	@Test
	public void testExecute() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		DrinkingContestPropertyUtils.startDrinkingContest(performer, target, 100);
		
		Conditions.add(performer, Condition.INTOXICATED_CONDITION, 8, world);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WINE.generate(1f));
		
		new DrinkingContestListener().actionPerformed(Actions.DRINK_FROM_INVENTORY_ACTION, performer, target, null, null);
		
		assertEquals(null, performer.getProperty(Constants.DRINKING_CONTEST_OPPONENT_ID));
		assertEquals(null, target.getProperty(Constants.DRINKING_CONTEST_OPPONENT_ID));
		assertEquals(1100, performer.getProperty(Constants.GOLD).intValue());
		assertEquals(900, target.getProperty(Constants.GOLD).intValue());
	}
	
	@Test
	public void testExecuteStillTalking() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = createPerformer(2);
		WorldObject target = createPerformer(3);
		
		DrinkingContestPropertyUtils.startDrinkingContest(performer, target, 100);
		
		Conditions.add(performer, Condition.INTOXICATED_CONDITION, 8, world);
		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WINE.generate(1f));
		
		new DrinkingContestListener().actionPerformed(Actions.TALK_ACTION, performer, target, Conversations.createArgs(Conversations.DRINKING_CONTEST_CONVERSATION), null);
		
		assertEquals(3, performer.getProperty(Constants.DRINKING_CONTEST_OPPONENT_ID).intValue());
		assertEquals(2, target.getProperty(Constants.DRINKING_CONTEST_OPPONENT_ID).intValue());
		assertEquals(1000, performer.getProperty(Constants.GOLD).intValue());
		assertEquals(1000, target.getProperty(Constants.GOLD).intValue());
	}
	
	private WorldObject createPerformer(int id) {
		WorldObject performer = TestUtils.createSkilledWorldObject(id, Constants.INVENTORY, new WorldObjectContainer());
		performer.setProperty(Constants.X, 0);
		performer.setProperty(Constants.Y, 0);
		performer.setProperty(Constants.WIDTH, 1);
		performer.setProperty(Constants.HEIGHT, 1);
		performer.setProperty(Constants.GOLD, 1000);
		performer.setProperty(Constants.CONDITIONS, new Conditions());
		return performer;
	}
}