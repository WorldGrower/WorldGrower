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
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.goal.GroupPropertyUtils;
import org.worldgrower.personality.Personality;
import org.worldgrower.personality.PersonalityTrait;

public class UTestGiveMoneyConversation {

	private final GiveMoneyConversation conversation = Conversations.GIVE_MONEY_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(4, replyPhrases.size());
		assertEquals("Thanks", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("Get lost", replyPhrases.get(1).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("Thanks again", replyPhrases.get(2).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("I won't take your bribe money, get lost", replyPhrases.get(3).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testGetReplyPhrase0() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject organization = GroupPropertyUtils.create(null, "OrgName", world);
		target.setProperty(Constants.PERSONALITY, new Personality());
		
		ConversationContext context = new ConversationContext(performer, target, organization, null, world, 0);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetReplyPhrase1() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject organization = GroupPropertyUtils.create(null, "OrgName", world);
		target.setProperty(Constants.PERSONALITY, new Personality());
		target.getProperty(Constants.PERSONALITY).changeValue(PersonalityTrait.HONORABLE, -1000, "");
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -1000);
		
		ConversationContext context = new ConversationContext(performer, target, organization, null, world, 0);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetReplyPhrase3() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject organization = GroupPropertyUtils.create(null, "OrgName", world);
		target.setProperty(Constants.PERSONALITY, new Personality());
		target.getProperty(Constants.PERSONALITY).changeValue(PersonalityTrait.HONORABLE, 1000, "");
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -1000);
		
		ConversationContext context = new ConversationContext(performer, target, organization, null, world, 0);
		assertEquals(3, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(1, questions.size());
		assertEquals("Would you like to have 100 gold?", questions.get(0).getQuestionPhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createSkilledWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		target.setProperty(Constants.PERSONALITY, new Personality());
		
		performer.setProperty(Constants.GOLD, 200);
		target.setProperty(Constants.GOLD, 200);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(0, context);
		assertEquals(5, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(5, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
		assertEquals(100, performer.getProperty(Constants.GOLD).intValue());
		assertEquals(300, target.getProperty(Constants.GOLD).intValue());
	}
	
	@Test
	public void testHandleResponse0Greedy() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createSkilledWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		Personality personality = new Personality();
		personality.changeValue(PersonalityTrait.GREEDY, 1000, "hungry");
		target.setProperty(Constants.PERSONALITY, personality);
		
		performer.setProperty(Constants.GOLD, 200);
		target.setProperty(Constants.GOLD, 200);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(0, context);
		assertEquals(15, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(15, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
		assertEquals(100, performer.getProperty(Constants.GOLD).intValue());
		assertEquals(300, target.getProperty(Constants.GOLD).intValue());
	}

	@Test
	public void testHandleResponse1() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createSkilledWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		target.setProperty(Constants.PERSONALITY, new Personality());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(1, context);
		assertEquals(-20, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-20, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
	
	@Test
	public void testHandleResponse2() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createSkilledWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		target.setProperty(Constants.PERSONALITY, new Personality());
		
		performer.setProperty(Constants.GOLD, 200);
		target.setProperty(Constants.GOLD, 200);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(2, context);
		assertEquals(5, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(5, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
		assertEquals(100, performer.getProperty(Constants.GOLD).intValue());
		assertEquals(300, target.getProperty(Constants.GOLD).intValue());
	}
	
	@Test
	public void testIsConversationAvailable() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target = TestUtils.createSkilledWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());
		
		performer.setProperty(Constants.GOLD, 0);
		assertEquals(false, conversation.isConversationAvailable(performer, target, null, world));
		
		performer.setProperty(Constants.GOLD, 200);
		assertEquals(true, conversation.isConversationAvailable(performer, target, null, world));
	}
}
