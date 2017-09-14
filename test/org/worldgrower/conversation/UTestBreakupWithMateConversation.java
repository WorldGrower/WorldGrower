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
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.personality.Personality;
import org.worldgrower.personality.PersonalityTrait;

public class UTestBreakupWithMateConversation {

	private final BreakupWithMateConversation conversation = Conversations.BREAKUP_WITH_MATE_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(1, replyPhrases.size());
		assertEquals("Ok, I'll respect your wishes", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testGetReplyPhrase() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.NAME, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.NAME, "target");
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(1, questions.size());
		assertEquals("I want to break up with you", questions.get(0).getQuestionPhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testIsConversationAvailable() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.NAME, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.NAME, "target");

		assertEquals(false, conversation.isConversationAvailable(performer, target, null, null));
	
		performer.setProperty(Constants.MATE_ID, 2);
		target.setProperty(Constants.MATE_ID, 1);
		assertEquals(true, conversation.isConversationAvailable(performer, target, null, null));
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		performer.setProperty(Constants.MATE_ID, 2);
		target.setProperty(Constants.MATE_ID, 1);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(0, context);
		assertEquals(null, performer.getProperty(Constants.MATE_ID));
		assertEquals(null, target.getProperty(Constants.MATE_ID));
	}
	
	@Test
	public void testBreakup() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.NAME, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.NAME, "target");
		
		performer.setProperty(Constants.MATE_ID, 2);
		target.setProperty(Constants.MATE_ID, 1);
		
		conversation.breakup(performer, target, world);
		assertEquals(null, performer.getProperty(Constants.MATE_ID));
		assertEquals(null, target.getProperty(Constants.MATE_ID));
		assertEquals(-500, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-500, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
	
	@Test
	public void testBreakupWithForgivingTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.NAME, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.NAME, "target");
		
		performer.setProperty(Constants.MATE_ID, 2);
		target.setProperty(Constants.MATE_ID, 1);
		Personality personality = new Personality();
		personality.changeValue(PersonalityTrait.FORGIVING, 1000, "forgiving");
		target.setProperty(Constants.PERSONALITY, personality);
		
		conversation.breakup(performer, target, world);
		assertEquals(null, performer.getProperty(Constants.MATE_ID));
		assertEquals(null, target.getProperty(Constants.MATE_ID));
		assertEquals(-400, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-400, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
	
	@Test
	public void testBreakupWithUnforgivingTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.NAME, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.NAME, "target");
		
		performer.setProperty(Constants.MATE_ID, 2);
		target.setProperty(Constants.MATE_ID, 1);
		Personality personality = new Personality();
		personality.changeValue(PersonalityTrait.FORGIVING, -1000, "unforgiving");
		target.setProperty(Constants.PERSONALITY, personality);
		
		conversation.breakup(performer, target, world);
		assertEquals(null, performer.getProperty(Constants.MATE_ID));
		assertEquals(null, target.getProperty(Constants.MATE_ID));
		assertEquals(-600, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-600, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
}
