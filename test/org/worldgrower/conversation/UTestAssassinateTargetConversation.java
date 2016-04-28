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
import org.worldgrower.personality.Personality;

public class UTestAssassinateTargetConversation {

	private final AssassinateTargetConversation conversation = Conversations.ASSASSINATE_TARGET_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(2, replyPhrases.size());
		assertEquals("Yes", replyPhrases.get(0).getResponsePhrase());
		assertEquals("No", replyPhrases.get(1).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		target.setProperty(Constants.PERSONALITY, new Personality());
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -2000);
		
		ConversationContext context = new ConversationContext(performer, target, subject, null, world, 0);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
		
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, 4000);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(subject, -4000);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList().add(0));
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, subject, null);
		assertEquals(1, questions.size());
		assertEquals("Would you like me to get rid of subject? If you agree, it'll cost you 100 gold.", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testGetPossibleSubjects() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList().add(0));
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		
		world.addWorldObject(subject);
		
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(subject, 1000);
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(target, 1000);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(subject, 1000);
		
		List<WorldObject> possibleSubjects = conversation.getPossibleSubjects(performer, target, null, world);
		assertEquals(1, possibleSubjects.size());
		assertEquals(subject, possibleSubjects.get(0));
	}
	
	@Test
	public void testHandleResponse0() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.ARENA_FIGHTER_IDS, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.ARENA_FIGHTER_IDS, new IdList());
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.ARENA_FIGHTER_IDS, new IdList());

		ConversationContext context = new ConversationContext(performer, target, subject, null, null, 0);
		conversation.handleResponse(0, context);
		assertEquals(3, target.getProperty(Constants.ASSASSINATE_TARGET_ID).intValue());
	}
	
	@Test
	public void testHandleResponse1() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.ARENA_FIGHTER_IDS, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.ARENA_FIGHTER_IDS, new IdList());
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.ARENA_FIGHTER_IDS, new IdList());

		ConversationContext context = new ConversationContext(performer, target, subject, null, null, 0);
		conversation.handleResponse(1, context);
		assertEquals(-50, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-50, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
}
