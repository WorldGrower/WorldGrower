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
import org.worldgrower.goal.GroupPropertyUtils;
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
		assertEquals(true, replyPhrases.size() == 2);
		assertEquals("I know no-one that is a fisher", replyPhrases.get(0).getResponsePhrase());
		assertEquals("No", replyPhrases.get(1).getResponsePhrase());
		
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		world.addWorldObject(subject);
		target.getProperty(Constants.KNOWLEDGE_MAP).addKnowledge(subject, Constants.PROFESSION, Professions.FISHER_PROFESSION);
		
		replyPhrases = conversation.getReplyPhrases(context);
		assertEquals("I know that subject,  are fishers", replyPhrases.get(0).getResponsePhrase());
	}
	/*
	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject organization = GroupPropertyUtils.create(null, "OrgName", world);
		
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -2000);
		
		ConversationContext context = new ConversationContext(performer, target, organization, null, world, 0);
		assertEquals(-999, conversation.getReplyPhrase(context).getId());
		
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, 2000);
		target.setProperty(Constants.GOLD, 0);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
		
		target.setProperty(Constants.GOLD, 200);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
		
		target.setProperty(Constants.GOLD, 50);
		assertEquals(2, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList().add(0));
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(1, questions.size());
		assertEquals("Can you give me 100 gold?", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testHandleResponseMinus999() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(-999, context);
		assertEquals(-50, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-50, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		performer.setProperty(Constants.GOLD, 100);
		target.setProperty(Constants.GOLD, 100);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(0, context);
		assertEquals(50, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-50, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
		assertEquals(200, performer.getProperty(Constants.GOLD).intValue());
		assertEquals(0, target.getProperty(Constants.GOLD).intValue());
	}
	
	@Test
	public void testHandleResponse1() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(1, context);
		assertEquals(-50, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-10, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
	
	@Test
	public void testHandleResponse2() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		performer.setProperty(Constants.GOLD, 50);
		target.setProperty(Constants.GOLD, 50);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(2, context);
		assertEquals(20, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-5, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
		assertEquals(100, performer.getProperty(Constants.GOLD).intValue());
		assertEquals(0, target.getProperty(Constants.GOLD).intValue());
	}*/
}
