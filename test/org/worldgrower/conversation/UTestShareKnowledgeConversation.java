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
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.attribute.KnowledgeMap;
import org.worldgrower.deity.Deity;

public class UTestShareKnowledgeConversation {

	private final ShareKnowledgeConversation conversation = Conversations.SHARE_KNOWLEDGE_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(true, replyPhrases.size() == 2);
		assertEquals("Thanks for the information", replyPhrases.get(0).getResponsePhrase());
		assertEquals("Get lost", replyPhrases.get(1).getResponsePhrase());
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
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, world);
		assertEquals(0, questions.size());
		
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		world.addWorldObject(subject);
		performer.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(subject, Constants.DEITY, Deity.HADES);
		
		questions = conversation.getQuestionPhrases(performer, target, null, null, world);
		assertEquals(1, questions.size());
		assertEquals("Did you know that subject worships Hades?", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		performer.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		target.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		int knowledgeId = performer.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(subject, Constants.DEITY, Deity.HADES);
		ConversationContext context = new ConversationContext(performer, target, subject, null, world, knowledgeId);
		
		conversation.handleResponse(0, context);
		assertEquals(50, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(50, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
	
	@Test
	public void testHandleResponse1() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		performer.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		target.setProperty(Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		int knowledgeId = performer.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(subject, Constants.DEITY, Deity.HADES);
		ConversationContext context = new ConversationContext(performer, target, subject, null, world, knowledgeId);
		
		conversation.handleResponse(1, context);
		assertEquals(-50, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-5, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
}
