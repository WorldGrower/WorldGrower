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
import org.worldgrower.goal.GroupPropertyUtils;

public class UTestComplimentConversation {

	private final ComplimentConversation conversation = Conversations.COMPLIMENT_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(true, replyPhrases.size() == 3);
		assertEquals("Thanks, you too", replyPhrases.get(0).getResponsePhrase());
		assertEquals("Stop doing that", replyPhrases.get(1).getResponsePhrase());
		assertEquals("Get lost", replyPhrases.get(2).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject organization = GroupPropertyUtils.create(null, "OrgName", world);
		
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -2000);
		
		ConversationContext context = new ConversationContext(performer, target, organization, null, world, 0);
		assertEquals(2, conversation.getReplyPhrase(context).getId());
		
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, 2000);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList().add(0));
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(2, questions.size());
		assertEquals("You look very strong", questions.get(0).getQuestionPhrase());
		assertEquals("You are very handsome", questions.get(1).getQuestionPhrase());
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		WorldObject target = TestUtils.createSkilledWorldObject(2);
		performer.setProperty(Constants.RELATIONSHIPS, new IdRelationshipMap());
		target.setProperty(Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(0, context);
		assertEquals(50, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(50, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}

	@Test
	public void testHandleResponse1() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		WorldObject target = TestUtils.createSkilledWorldObject(2);
		performer.setProperty(Constants.RELATIONSHIPS, new IdRelationshipMap());
		target.setProperty(Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(1, context);
		assertEquals(-5, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-5, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
	
	@Test
	public void testHandleResponse2() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		WorldObject target = TestUtils.createSkilledWorldObject(2);
		performer.setProperty(Constants.RELATIONSHIPS, new IdRelationshipMap());
		target.setProperty(Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(2, context);
		assertEquals(-50, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-50, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
}
