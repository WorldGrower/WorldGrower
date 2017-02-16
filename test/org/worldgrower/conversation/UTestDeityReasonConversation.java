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
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.ReasonsImpl;
import org.worldgrower.deity.Deity;

public class UTestDeityReasonConversation {

	private final DeityReasonConversation conversation = Conversations.DEITY_REASON_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.APHRODITE);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.DEITY, Deity.HADES);

		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(5, replyPhrases.size());
		assertEquals("Hades rules the underworld and I worship him as a sign of respect for my relatives who are there.", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}


	@Test
	public void testGetReplyPhrase() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.APHRODITE);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.DEITY, Deity.HADES);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		
		target.setProperty(Constants.REASONS, new ReasonsImpl());
		target.getProperty(Constants.REASONS).addReason(Constants.DEITY, conversation.getReplyPhrases(context).get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.APHRODITE);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.DEITY, Deity.HADES);
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(1, questions.size());
		assertEquals("Why did you choose to follow Hades?", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testIsConversationAvailable() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.DEITY, Deity.APHRODITE);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		
		assertEquals(false, conversation.isConversationAvailable(performer, target, null, null));
		
		target.setProperty(Constants.DEITY, Deity.HADES);
		assertEquals(true, conversation.isConversationAvailable(performer, target, null, null));
	}
}
