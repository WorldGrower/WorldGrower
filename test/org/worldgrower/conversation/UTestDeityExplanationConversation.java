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
import org.worldgrower.DefaultConversationFormatter;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.deity.Deity;
import org.worldgrower.profession.Professions;

public class UTestDeityExplanationConversation {

	private final DeityExplanationConversation conversation = Conversations.DEITY_EXPLANATION_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.APHRODITE);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.DEITY, Deity.HADES);

		int deityIndex = 0;
		ConversationContext context = new ConversationContext(performer, target, null, null, null, deityIndex);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(3, replyPhrases.size());
		assertEquals("Aphrodite is the Goddess of love, beauty, desire, sex and pleasure.", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("I don't know more about Aphrodite", replyPhrases.get(1).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("Get lost", replyPhrases.get(2).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}

	@Test
	public void testGetReplyPhrase() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.APHRODITE);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.DEITY, Deity.HADES);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		
		target.setProperty(Constants.PROFESSION, Professions.PRIEST_PROFESSION);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
		
		target.setProperty(Constants.PROFESSION, null);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
		
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -1000);
		assertEquals(2, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.APHRODITE);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.DEITY, Deity.HADES);
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(13, questions.size());
		assertEquals("What can you tell me about Aphrodite", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testIsConversationAvailable() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.APHRODITE);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());

		assertEquals(true, conversation.isConversationAvailable(performer, target, null, null));
	}
	
	@Test
	public void testHandleResponse2() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(2, context);
		assertEquals(-100, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-100, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
}
