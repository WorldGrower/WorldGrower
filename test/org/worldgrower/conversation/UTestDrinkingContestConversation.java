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
import org.worldgrower.DungeonMaster;
import org.worldgrower.MockMetaInformation;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.generator.Item;
import org.worldgrower.goal.DrinkingContestPropertyUtils;
import org.worldgrower.goal.Goals;

public class UTestDrinkingContestConversation {

	private final DrinkingContestConversation conversation = Conversations.DRINKING_CONTEST_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		MockMetaInformation.setMetaInformation(target, Goals.COLLECT_WATER_GOAL);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(true, replyPhrases.size() == 4);
		assertEquals("Yes, the first one to become intoxicated or doesn't drink alcohol loses.", replyPhrases.get(0).getResponsePhrase());
		assertEquals("No", replyPhrases.get(1).getResponsePhrase());
		assertEquals("I'd love to, but I'm currently attacking targetName", replyPhrases.get(2).getResponsePhrase());
		assertEquals("Not for the moment, I can't match your bet", replyPhrases.get(3).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(1, 1, new DungeonMaster(), null);
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GOLD, 0);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Goals.COLLECT_WATER_GOAL);
		
		target.setProperty(Constants.GOLD, 0);
		MockMetaInformation.setMetaInformation(target, Goals.COLLECT_WATER_GOAL);
		
		int brawlStakeGold = 10;
		ConversationContext context = new ConversationContext(performer, target, null, null, world, brawlStakeGold);
		assertEquals(3, conversation.getReplyPhrase(context).getId());
		
		target.setProperty(Constants.GOLD, 100);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(5, questions.size());
		assertEquals("I want to have a drinking contest with you and I bet 20 gold that I'm going to win. Do you accept?", questions.get(0).getQuestionPhrase());
	}
	
	@Test
	public void testHandleResponse0() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		int brawlStakeGold = 10;
		ConversationContext context = new ConversationContext(performer, target, null, null, null, brawlStakeGold);
		
		conversation.handleResponse(0, context);
		assertEquals(10, performer.getProperty(Constants.DRINKING_CONTEST_STAKE_GOLD).intValue());
		assertEquals(10, target.getProperty(Constants.DRINKING_CONTEST_STAKE_GOLD).intValue());
		assertEquals(2, performer.getProperty(Constants.DRINKING_CONTEST_OPPONENT_ID).intValue());
		assertEquals(1, target.getProperty(Constants.DRINKING_CONTEST_OPPONENT_ID).intValue());
	}
	
	@Test
	public void testIsConversationAvailable() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.INVENTORY, new WorldObjectContainer());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.INVENTORY, new WorldObjectContainer());

		performer.getProperty(Constants.INVENTORY).addQuantity(Item.WINE.generate(1f), 10);
		target.getProperty(Constants.INVENTORY).addQuantity(Item.WINE.generate(1f), 10);
		assertEquals(true, conversation.isConversationAvailable(performer, target, null, null));
		
		DrinkingContestPropertyUtils.startDrinkingContest(performer, target, 10);
		assertEquals(false, conversation.isConversationAvailable(performer, target, null, null));
	}
}
