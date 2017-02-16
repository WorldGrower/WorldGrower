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
import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.DefaultConversationFormatter;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.BackgroundImpl;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.goal.Goals;

public class UTestWhyAngryConversation {

	private final WhyAngryConversation conversation = Conversations.WHY_ANGRY_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.BACKGROUND, new BackgroundImpl());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.BACKGROUND, new BackgroundImpl());

		performer.setProperty(Constants.GENDER, "male");
		performer.setProperty(Constants.NAME, "performerName");
		target.setProperty(Constants.NAME, "targetname");
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(true, replyPhrases.size() == 2);
		assertEquals("I don't remember", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("Get lost", replyPhrases.get(1).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		
		target.getProperty(Constants.BACKGROUND).addGoalObstructed(Goals.PROTECT_ONE_SELF_GOAL, performer, target, Actions.MELEE_ATTACK_ACTION, Args.EMPTY, world);
		replyPhrases = conversation.getReplyPhrases(context);
		assertEquals("You were attacking me", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));

		target.getProperty(Constants.BACKGROUND).addGoalObstructed(Goals.PROTECT_ONE_SELF_GOAL, performer, target, Actions.PARALYZE_SPELL_ACTION, Args.EMPTY, world);
		replyPhrases = conversation.getReplyPhrases(context);
		assertEquals("You were attacking me; You were casting paralyze on me", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}

	@Test
	public void testGetReplyPhrase() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.BACKGROUND, new BackgroundImpl());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.BACKGROUND, new BackgroundImpl());
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.GENDER, "female");
		
		ConversationContext context = new ConversationContext(performer, target, subject, null, null, 0);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.CHILDREN, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.CHILDREN, new IdList());
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(1, questions.size());
		assertEquals("Why are you angry with me?", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(0, context);
		assertEquals(10, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(10, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
	
	@Test
	public void testHandleResponse1() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(1, context);
		assertEquals(-20, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-5, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
	
	@Test
	public void testIsConversationAvailable() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		assertEquals(false, conversation.isConversationAvailable(performer, target, null, null));
		
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -2000);
		assertEquals(true, conversation.isConversationAvailable(performer, target, null, null));
	}
}
