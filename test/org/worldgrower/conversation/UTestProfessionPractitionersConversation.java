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
import org.worldgrower.profession.Professions;

public class UTestProfessionPractitionersConversation {

	private final ProfessionPractitionersConversation conversation = Conversations.PROFESSION_PRACTITIONERS_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		int indexOfProfession = Professions.indexOf(Professions.FISHER_PROFESSION);
		ConversationContext context = new ConversationContext(performer, target, null, null, world, indexOfProfession);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(2, replyPhrases.size());
		assertEquals("I know no-one that is a fisher", replyPhrases.get(0).getResponsePhrase());
		assertEquals("No", replyPhrases.get(1).getResponsePhrase());
		
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		world.addWorldObject(subject);
		target.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(subject, Constants.PROFESSION, Professions.FISHER_PROFESSION);
		
		replyPhrases = conversation.getReplyPhrases(context);
		assertEquals("I know that subject is a fisher", replyPhrases.get(0).getResponsePhrase());
	
		WorldObject subject2 = TestUtils.createIntelligentWorldObject(4, Constants.NAME, "subject2");
		world.addWorldObject(subject2);
		target.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(subject2, Constants.PROFESSION, Professions.FISHER_PROFESSION);
	
		replyPhrases = conversation.getReplyPhrases(context);
		assertEquals("I know that subject, subject2 are fishers", replyPhrases.get(0).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhrasesTargetHasProfession() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		target.setProperty(Constants.PROFESSION, Professions.FISHER_PROFESSION);
		
		int indexOfProfession = Professions.indexOf(Professions.FISHER_PROFESSION);
		ConversationContext context = new ConversationContext(performer, target, null, null, world, indexOfProfession);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(2, replyPhrases.size());
		assertEquals("I'm a fisher", replyPhrases.get(0).getResponsePhrase());
	}

	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.KNOWLEDGE_MAP, new KnowledgeMap());
		
		int indexOfProfession = Professions.indexOf(Professions.FISHER_PROFESSION);
		ConversationContext context = new ConversationContext(performer, target, null, null, world, indexOfProfession);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
		
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		world.addWorldObject(subject);
		target.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(subject, Constants.PROFESSION, Professions.FISHER_PROFESSION);

		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList().add(0));
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(true, questions.size() > 0);
		assertEquals("Do you know any people who are farmers?", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(0, context);
		assertEquals(10, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(10, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
}
