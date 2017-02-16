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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.DefaultConversationFormatter;
import org.worldgrower.DungeonMaster;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;

public class UTestCurePoisonConversation {

	private final CurePoisonConversation conversation = Conversations.CURE_POISON_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(3, replyPhrases.size());
		assertEquals("Yes", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("No", replyPhrases.get(1).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("Get lost", replyPhrases.get(2).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
		
		target.getProperty(Constants.RELATIONSHIPS).incrementValue(performer, -2000);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(1, questions.size());
		assertEquals("Can you cure my poisoned condition?", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testHandleResponse0() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createSkilledWorldObject(1, Constants.ENERGY, 1000);
		WorldObject target = TestUtils.createSkilledWorldObject(2, Constants.ENERGY, 1000);
		
		performer.setProperty(Constants.CONDITIONS, new Conditions());
		Conditions.add(performer, Condition.POISONED_CONDITION, 8, world);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(0, context);
		assertEquals(false, performer.getProperty(Constants.CONDITIONS).hasCondition(Condition.POISONED_CONDITION));
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
	public void testHandleResponse2() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		
		conversation.handleResponse(2, context);
		assertEquals(-70, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-70, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
	
	@Test
	public void testIsConversationAvailable() {
		World world = new WorldImpl(1, 1, null, null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.ENERGY, 1000);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.ENERGY, 1000);

		performer.setProperty(Constants.CONDITIONS, new Conditions());
		
		assertEquals(false, conversation.isConversationAvailable(performer, target, null, null));
		
		Conditions.add(performer, Condition.POISONED_CONDITION, 8, world);
		target.setProperty(Constants.KNOWN_SPELLS, Arrays.asList(Actions.CURE_POISON_ACTION));
		assertEquals(true, conversation.isConversationAvailable(performer, target, null, null));
	}
}
