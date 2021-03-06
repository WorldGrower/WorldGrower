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
import org.worldgrower.OperationInfo;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.history.Turn;
import org.worldgrower.personality.Personality;
import org.worldgrower.personality.PersonalityTrait;

public class UTestProposeMateConversation {

	private final ProposeMateConversation conversation = Conversations.PROPOSE_MATE_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(4, replyPhrases.size());
		assertEquals("Yes", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("No", replyPhrases.get(1).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("My answer is still the same as the last time you asked, no", replyPhrases.get(2).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("This time my answer is no", replyPhrases.get(3).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
		
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, 1000);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetReplyPhraseAskedAgain() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		world.getHistory().setNextAdditionalValue(1);
		world.getHistory().actionPerformed(new OperationInfo(performer, target, Conversations.createArgs(conversation), Actions.TALK_ACTION), new Turn());
		
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, 1000);
		assertEquals(3, conversation.getReplyPhrase(context).getId());
		
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -4000);
		assertEquals(2, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.NAME, "performer");
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.NAME, "target");
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, subject, null);
		assertEquals(1, questions.size());
		assertEquals("Would you like to become my mate?", questions.get(0).getQuestionPhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(0, context);
		assertEquals(2, performer.getProperty(Constants.MATE_ID).intValue());
		assertEquals(1, target.getProperty(Constants.MATE_ID).intValue());
	}
	
	@Test
	public void testHandleResponse0WithPreviousMates() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createSkilledWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		WorldObject performerMate = TestUtils.createSkilledWorldObject(3, Constants.RELATIONSHIPS, new IdRelationshipMap());
		world.addWorldObject(performerMate);
		WorldObject targetMate = TestUtils.createSkilledWorldObject(4, Constants.RELATIONSHIPS, new IdRelationshipMap());
		world.addWorldObject(targetMate);
		
		performer.setProperty(Constants.MATE_ID, performerMate.getProperty(Constants.ID));
		target.setProperty(Constants.MATE_ID, targetMate.getProperty(Constants.ID));
		performerMate.setProperty(Constants.MATE_ID, performer.getProperty(Constants.ID));
		targetMate.setProperty(Constants.MATE_ID, target.getProperty(Constants.ID));
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(0, context);
		assertEquals(2, performer.getProperty(Constants.MATE_ID).intValue());
		assertEquals(1, target.getProperty(Constants.MATE_ID).intValue());
		assertEquals(null, performerMate.getProperty(Constants.MATE_ID));
		assertEquals(null, targetMate.getProperty(Constants.MATE_ID));
	}
	
	@Test
	public void testHandleResponse1() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(1, context);
		assertEquals(-50, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-50, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
	
	@Test
	public void testTargetAcceptsHonorableTarget() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		assertEquals(false, conversation.targetAccepts(target, performer));
		
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, 1000);
		assertEquals(true, conversation.targetAccepts(target, performer));
		
		Personality personality = new Personality();
		personality.changeValue(PersonalityTrait.HONORABLE, 1000, "betrayed");
		target.setProperty(Constants.PERSONALITY, personality);
		assertEquals(true, conversation.targetAccepts(target, performer));
		
		target.setProperty(Constants.MATE_ID, 8);
		assertEquals(false, conversation.targetAccepts(target, performer));
		
		personality.changeValue(PersonalityTrait.HONORABLE, -2000, "betrayed");
		assertEquals(true, conversation.targetAccepts(target, performer));
	}
}
