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
import org.worldgrower.TestUtils;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;

public class UTestBecomeArenaFighterConversation {

	private final BecomeArenaFighterConversation conversation = Conversations.BECOME_ARENA_FIGHTER_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(2, replyPhrases.size());
		assertEquals("Yes, just let me know when you want to fight.", replyPhrases.get(0).getResponsePhrase());
		assertEquals("No", replyPhrases.get(1).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhrase() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
		
		
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -2000);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(1, questions.size());
		assertEquals("Can I become an arena fighter?", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testHandleResponse1() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.ARENA_FIGHTER_IDS, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.ARENA_FIGHTER_IDS, new IdList());

		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		conversation.handleResponse(1, context);
		assertEquals(false, target.getProperty(Constants.ARENA_FIGHTER_IDS).contains(performer));
		assertEquals(-30, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-30, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
	
	@Test
	public void testIsConversationAvailable() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.ARENA_FIGHTER_IDS, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.ARENA_FIGHTER_IDS, new IdList());
		
		assertEquals(false, conversation.isConversationAvailable(performer, target, null, null));
		
		target.setProperty(Constants.ARENA_IDS, new IdList());
		target.getProperty(Constants.ARENA_IDS).add(5);
		assertEquals(true, conversation.isConversationAvailable(performer, target, null, null));
		
		target.getProperty(Constants.ARENA_FIGHTER_IDS).add(performer);
		assertEquals(false, conversation.isConversationAvailable(performer, target, null, null));
	}
}
