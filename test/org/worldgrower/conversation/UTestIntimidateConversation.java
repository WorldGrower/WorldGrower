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
import org.worldgrower.attribute.Skill;
import org.worldgrower.personality.Personality;
import org.worldgrower.personality.PersonalityTrait;

public class UTestIntimidateConversation {

	private final IntimidateConversation conversation = (IntimidateConversation) new Conversations().getIntimidateConversation();
	
	@Test
	public void testGetReplyPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(2, replyPhrases.size());
		assertEquals("Get lost", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("I'll comply", replyPhrases.get(1).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testGetReplyPhraseGetLost() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		WorldObject target = TestUtils.createSkilledWorldObject(2);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(-999, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetReplyPhraseComply() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		WorldObject target = TestUtils.createSkilledWorldObject(2);
		
		performer.setProperty(Constants.RELATIONSHIPS, new IdRelationshipMap());
		target.setProperty(Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		performer.setProperty(Constants.INTIMIDATE_SKILL, new Skill(20));
		performer.setProperty(Constants.INSIGHT_SKILL, new Skill(10));
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetReplyPhraseCowardlyTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		WorldObject target = TestUtils.createSkilledWorldObject(2);
		
		performer.setProperty(Constants.RELATIONSHIPS, new IdRelationshipMap());
		target.setProperty(Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		performer.setProperty(Constants.INTIMIDATE_SKILL, new Skill(10));
		performer.setProperty(Constants.INSIGHT_SKILL, new Skill(11));
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(-999, conversation.getReplyPhrase(context).getId());
		
		Personality personality = new Personality();
		personality.changeValue(PersonalityTrait.COURAGEOUS, -1000, "cowardly");
		target.setProperty(Constants.PERSONALITY, personality);
		
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetReplyPhraseCourageousTarget() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1);
		WorldObject target = TestUtils.createSkilledWorldObject(2);
		
		performer.setProperty(Constants.RELATIONSHIPS, new IdRelationshipMap());
		target.setProperty(Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		performer.setProperty(Constants.INTIMIDATE_SKILL, new Skill(11));
		performer.setProperty(Constants.INSIGHT_SKILL, new Skill(10));
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
		
		Personality personality = new Personality();
		personality.changeValue(PersonalityTrait.COURAGEOUS, 1000, "courageous");
		target.setProperty(Constants.PERSONALITY, personality);
		
		assertEquals(-999, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(1, questions.size());
		assertEquals("I think you better help me or I'll slit your throat. What is your name?", questions.get(0).getQuestionPhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createSkilledWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(0, context);
		assertEquals(1000, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(50, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}

	@Test
	public void testHandleResponseMinus999() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createSkilledWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(-999, context);
		assertEquals(-50, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-50, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
}
