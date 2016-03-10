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
package org.worldgrower.conversation;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.DungeonMaster;
import org.worldgrower.ManagedOperation;
import org.worldgrower.MockMetaInformation;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.deity.Deity;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.Goals;

public class UTestImmediateGoalConversation {

	private final ImmediateGoalConversation conversation = Conversations.IMMEDIATE_GOAL_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		World world = new WorldImpl(0, 0, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.APHRODITE);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.DEITY, Deity.HADES);

		MockMetaInformation.setMetaInformation(target, Goals.BRAWL_GOAL);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(2, replyPhrases.size());
		assertEquals("I'm attacking targetName because I'm brawling", replyPhrases.get(0).getResponsePhrase());
		assertEquals("I'm not doing anything", replyPhrases.get(1).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhrasesForSpecificActions() {
		assertReplyPhrase("I'm attacking targetName because I'm brawling", Goals.BRAWL_GOAL, Actions.MELEE_ATTACK_ACTION);
		assertReplyPhrase("I'm resting because I'm tired", Goals.REST_GOAL, Actions.REST_ACTION);
		assertReplyPhrase("I'm biting targetName because I'm thirsty for blood", Goals.VAMPIRE_BLOOD_LEVEL_GOAL, Actions.VAMPIRE_BITE_ACTION);
		assertReplyPhrase("I'm eating targetName because I'm hungry and looking for food", Goals.FOOD_GOAL, Actions.EAT_ACTION);
		assertReplyPhrase("I'm cutting down the targetName because I'm building a shack", Goals.SHACK_GOAL, Actions.CUT_WOOD_ACTION);
		assertReplyPhrase("I'm drinking from targetName because I'm thirsty and looking for water", Goals.DRINK_WATER_GOAL, Actions.DRINK_ACTION);
		assertReplyPhrase("I'm having sex with targetName because I'm looking to have children", Goals.CHILDREN_GOAL, Actions.SEX_ACTION);
		assertReplyPhrase("I'm moving because I'm avoiding danger", Goals.PROTECT_ONSE_SELF_GOAL, Actions.MOVE_ACTION);
		assertReplyPhrase("I'm attacking targetName because I'm getting revenge", Goals.REVENGE_GOAL, Actions.FIRE_BOLT_ATTACK_ACTION);
		assertReplyPhrase("I'm planting a grape vine because I'm planting grape vines", Goals.CREATE_WINE_GOAL, Actions.PLANT_GRAPE_VINE_ACTION);
		assertReplyPhrase("I'm animating targetName because I'm looking to have a mate", Goals.MATE_GOAL, Actions.ANIMATE_DEAD_ACTION);
		assertReplyPhrase("I'm mining targetName because I'm looking for gold", Goals.MINE_GOLD_GOAL, Actions.MINE_GOLD_ACTION);
		assertReplyPhrase("I'm constructing fishing pole because I'm looking to gather fish", Goals.CATCH_FISH_GOAL, Actions.CONSTRUCT_FISHING_POLE_ACTION);
	}
	
	private void assertReplyPhrase(String replyPhrase, Goal goal, ManagedOperation action) {
		World world = new WorldImpl(0, 0, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.APHRODITE);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.DEITY, Deity.HADES);

		MockMetaInformation.setMetaInformation(target, goal, action);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(replyPhrase, replyPhrases.get(0).getResponsePhrase());
	}

	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(0, 0, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.APHRODITE);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.DEITY, Deity.HADES);

		MockMetaInformation.setMetaInformation(target, Goals.BRAWL_GOAL);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		assertEquals(0, conversation.getReplyPhrase(context).getId());
		
		MockMetaInformation.setMetaInformation(target, null);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.APHRODITE);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.DEITY, Deity.HADES);
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(1, questions.size());
		assertEquals("What are you doing?", questions.get(0).getQuestionPhrase());
	}
}
