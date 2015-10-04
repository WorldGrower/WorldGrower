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
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.BackgroundImpl;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;

public class UTestWhyAngryOtherConversation {

	private final WhyAngryOtherConversation conversation = Conversations.WHY_ANGRY_OTHER_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.BACKGROUND, new BackgroundImpl());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.BACKGROUND, new BackgroundImpl());
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.GENDER, "female");

		performer.setProperty(Constants.NAME, "performerName");
		target.setProperty(Constants.NAME, "targetname");
		
		ConversationContext context = new ConversationContext(performer, target, subject, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(true, replyPhrases.size() == 2);
		assertEquals("I don't remember", replyPhrases.get(0).getResponsePhrase());
		assertEquals("Get lost", replyPhrases.get(1).getResponsePhrase());
		
		target.getProperty(Constants.BACKGROUND).addGoalObstructed(subject, target, Actions.MELEE_ATTACK_ACTION, new int[0], world);
		replyPhrases = conversation.getReplyPhrases(context);
		assertEquals("She was attacking targetname; ", replyPhrases.get(0).getResponsePhrase());

		target.getProperty(Constants.BACKGROUND).addGoalObstructed(subject, performer, Actions.PARALYZE_SPELL_ACTION, new int[0], world);
		replyPhrases = conversation.getReplyPhrases(context);
		assertEquals("She was casting paralyze on performerName; She was attacking targetname; ", replyPhrases.get(0).getResponsePhrase());
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
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, subject, null);
		assertEquals(1, questions.size());
		assertEquals("Why are you angry with subject ?", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testGetPossibleSubjects() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		world.addWorldObject(subject);
		
		performer.getProperty(Constants.RELATIONSHIPS).incrementValue(subject, -2000);
		
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(subject, -2000);
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -2000);
		
		List<WorldObject> subjects = conversation.getPossibleSubjects(performer, target, null, world);
		assertEquals(1, subjects.size());
		assertEquals(subject, subjects.get(0));
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		
		ConversationContext context = new ConversationContext(performer, target, subject, null, world, 0);
		
		conversation.handleResponse(0, context);
		assertEquals(10, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(10, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
	
	@Test
	public void testHandleResponse1() {
		World world = new WorldImpl(0, 0, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject subject = TestUtils.createIntelligentWorldObject(3, Constants.NAME, "subject");
		
		ConversationContext context = new ConversationContext(performer, target, subject, null, world, 0);
		
		conversation.handleResponse(1, context);
		assertEquals(-20, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-5, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
}
