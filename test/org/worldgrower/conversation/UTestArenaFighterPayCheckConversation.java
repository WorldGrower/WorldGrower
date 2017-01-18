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
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.goal.Goals;

public class UTestArenaFighterPayCheckConversation {

	private final ArenaFighterPayCheckConversation conversation = Conversations.ARENA_FIGHTER_PAY_CHECK_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		performer.setProperty(Constants.ARENA_PAY_CHECK_GOLD, 50);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(2, replyPhrases.size());
		assertEquals("yes, here is your 50 gold", replyPhrases.get(0).getResponsePhrase());
		assertEquals("No", replyPhrases.get(1).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GOLD, 0);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Goals.COLLECT_WATER_GOAL);
		
		performer.setProperty(Constants.ARENA_PAY_CHECK_GOLD, 50);
		target.setProperty(Constants.GOLD, 0);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
		
		target.setProperty(Constants.GOLD, 100);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(1, questions.size());
		assertEquals("I'm here to receive my reward for participating in the arena", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testHandleResponse0() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GOLD, 0);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GOLD, 0);

		performer.setProperty(Constants.ARENA_PAY_CHECK_GOLD, 50);
		target.setProperty(Constants.GOLD, 100);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		
		conversation.handleResponse(0, context);
		assertEquals(50, performer.getProperty(Constants.GOLD).intValue());
		assertEquals(50, target.getProperty(Constants.GOLD).intValue());
	}
	
	@Test
	public void testHandleResponse1() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());

		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		
		conversation.handleResponse(1, context);
		assertEquals(-50, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-50, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
	
	@Test
	public void testIsConversationAvailable() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());

		assertEquals(false, conversation.isConversationAvailable(performer, target, null, null));
		
		target.setProperty(Constants.ARENA_IDS, new IdList().add(3));
		target.setProperty(Constants.ARENA_FIGHTER_IDS, new IdList().add(1));
		assertEquals(false, conversation.isConversationAvailable(performer, target, null, null));
	}
}
